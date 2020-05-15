#!/bin/bash

echo ""
echo "Applying migration WhatIsYourAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourAddress                        controllers.WhatIsYourAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourAddress                        controllers.WhatIsYourAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourAddress                  controllers.WhatIsYourAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourAddress                  controllers.WhatIsYourAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourAddress.title = whatIsYourAddress" >> ../conf/messages.en
echo "whatIsYourAddress.heading = whatIsYourAddress" >> ../conf/messages.en
echo "whatIsYourAddress.addressLine1 = addressLine1" >> ../conf/messages.en
echo "whatIsYourAddress.addressLine2 = addressLine2" >> ../conf/messages.en
echo "whatIsYourAddress.checkYourAnswersLabel = whatIsYourAddress" >> ../conf/messages.en
echo "whatIsYourAddress.error.addressLine1.required = Enter addressLine1" >> ../conf/messages.en
echo "whatIsYourAddress.error.addressLine2.required = Enter addressLine2" >> ../conf/messages.en
echo "whatIsYourAddress.error.addressLine1.length = addressLine1 must be 35 characters or less" >> ../conf/messages.en
echo "whatIsYourAddress.error.addressLine2.length = addressLine2 must be 35 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourAddressUserAnswersEntry: Arbitrary[(WhatIsYourAddressPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourAddressPage.type]";\
    print "        value <- arbitrary[WhatIsYourAddress].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourAddressPage: Arbitrary[WhatIsYourAddressPage.type] =";\
    print "    Arbitrary(WhatIsYourAddressPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourAddress: Arbitrary[WhatIsYourAddress] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        addressLine1 <- arbitrary[String]";\
    print "        addressLine2 <- arbitrary[String]";\
    print "      } yield WhatIsYourAddress(addressLine1, addressLine2)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourAddressPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def whatIsYourAddress: Option[Row] = userAnswers.get(WhatIsYourAddressPage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"whatIsYourAddress.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"${answer.addressLine1} ${answer.addressLine2}\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.WhatIsYourAddressController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"whatIsYourAddress.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration WhatIsYourAddress completed"
