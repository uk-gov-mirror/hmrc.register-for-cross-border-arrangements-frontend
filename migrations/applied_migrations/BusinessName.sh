#!/bin/bash

echo ""
echo "Applying migration BusinessName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /businessName                        controllers.BusinessNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /businessName                        controllers.BusinessNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeBusinessName                  controllers.BusinessNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeBusinessName                  controllers.BusinessNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "businessName.title = businessName" >> ../conf/messages.en
echo "businessName.heading = businessName" >> ../conf/messages.en
echo "businessName.businessName = businessName" >> ../conf/messages.en
echo "businessName.field2 = field2" >> ../conf/messages.en
echo "businessName.checkYourAnswersLabel = businessName" >> ../conf/messages.en
echo "businessName.error.businessName.required = Enter businessName" >> ../conf/messages.en
echo "businessName.error.field2.required = Enter field2" >> ../conf/messages.en
echo "businessName.error.businessName.length = businessName must be 105 characters or less" >> ../conf/messages.en
echo "businessName.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessNameUserAnswersEntry: Arbitrary[(BusinessNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[BusinessNamePage.type]";\
    print "        value <- arbitrary[BusinessName].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessNamePage: Arbitrary[BusinessNamePage.type] =";\
    print "    Arbitrary(BusinessNamePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessName: Arbitrary[BusinessName] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        businessName <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield BusinessName(businessName, field2)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(BusinessNamePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def businessName: Option[Row] = userAnswers.get(BusinessNamePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"businessName.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"${answer.businessName} ${answer.field2}\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.BusinessNameController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"businessName.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration BusinessName completed"
