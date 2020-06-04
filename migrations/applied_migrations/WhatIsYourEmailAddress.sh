#!/bin/bash

echo ""
echo "Applying migration WhatIsYourEmailAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourEmailAddress                        controllers.WhatIsYourEmailAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourEmailAddress                        controllers.WhatIsYourEmailAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourEmailAddress                  controllers.WhatIsYourEmailAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourEmailAddress                  controllers.WhatIsYourEmailAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourEmailAddress.title = whatIsYourEmailAddress" >> ../conf/messages.en
echo "whatIsYourEmailAddress.heading = whatIsYourEmailAddress" >> ../conf/messages.en
echo "whatIsYourEmailAddress.checkYourAnswersLabel = whatIsYourEmailAddress" >> ../conf/messages.en
echo "whatIsYourEmailAddress.error.required = Enter whatIsYourEmailAddress" >> ../conf/messages.en
echo "whatIsYourEmailAddress.error.length = WhatIsYourEmailAddress must be 254 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourEmailAddressUserAnswersEntry: Arbitrary[(WhatIsYourEmailAddressPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourEmailAddressPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourEmailAddressPage: Arbitrary[WhatIsYourEmailAddressPage.type] =";\
    print "    Arbitrary(WhatIsYourEmailAddressPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourEmailAddressPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def whatIsYourEmailAddress: Option[Row] = userAnswers.get(WhatIsYourEmailAddressPage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"whatIsYourEmailAddress.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"$answer\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.WhatIsYourEmailAddressController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"whatIsYourEmailAddress.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration WhatIsYourEmailAddress completed"
