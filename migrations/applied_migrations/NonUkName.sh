#!/bin/bash

echo ""
echo "Applying migration NonUkName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /nonUkName                        controllers.NonUkNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /nonUkName                        controllers.NonUkNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeNonUkName                  controllers.NonUkNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeNonUkName                  controllers.NonUkNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "nonUkName.title = nonUkName" >> ../conf/messages.en
echo "nonUkName.heading = nonUkName" >> ../conf/messages.en
echo "nonUkName.firstName = firstName" >> ../conf/messages.en
echo "nonUkName.secondName = secondName" >> ../conf/messages.en
echo "nonUkName.checkYourAnswersLabel = nonUkName" >> ../conf/messages.en
echo "nonUkName.error.firstName.required = Enter firstName" >> ../conf/messages.en
echo "nonUkName.error.secondName.required = Enter secondName" >> ../conf/messages.en
echo "nonUkName.error.firstName.length = firstName must be 50 characters or less" >> ../conf/messages.en
echo "nonUkName.error.secondName.length = secondName must be 50 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNonUkNameUserAnswersEntry: Arbitrary[(NonUkNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NonUkNamePage.type]";\
    print "        value <- arbitrary[NonUkName].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNonUkNamePage: Arbitrary[NonUkNamePage.type] =";\
    print "    Arbitrary(NonUkNamePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNonUkName: Arbitrary[NonUkName] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        firstName <- arbitrary[String]";\
    print "        secondName <- arbitrary[String]";\
    print "      } yield NonUkName(firstName, secondName)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NonUkNamePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def nonUkName: Option[Row] = userAnswers.get(NonUkNamePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"nonUkName.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"${answer.firstName} ${answer.secondName}\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.NonUkNameController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"nonUkName.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration NonUkName completed"
