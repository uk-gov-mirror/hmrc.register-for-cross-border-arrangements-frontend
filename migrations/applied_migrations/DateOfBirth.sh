#!/bin/bash

echo ""
echo "Applying migration DateOfBirth"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /dateOfBirth                  controllers.DateOfBirthController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /dateOfBirth                  controllers.DateOfBirthController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDateOfBirth                        controllers.DateOfBirthController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDateOfBirth                        controllers.DateOfBirthController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "dateOfBirth.title = DateOfBirth" >> ../conf/messages.en
echo "dateOfBirth.heading = DateOfBirth" >> ../conf/messages.en
echo "dateOfBirth.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "dateOfBirth.checkYourAnswersLabel = DateOfBirth" >> ../conf/messages.en
echo "dateOfBirth.error.required.all = Enter the dateOfBirth" >> ../conf/messages.en
echo "dateOfBirth.error.required.two = The dateOfBirth" must include {0} and {1} >> ../conf/messages.en
echo "dateOfBirth.error.required = The dateOfBirth must include {0}" >> ../conf/messages.en
echo "dateOfBirth.error.invalid = Enter a real DateOfBirth" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDateOfBirthUserAnswersEntry: Arbitrary[(DateOfBirthPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DateOfBirthPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDateOfBirthPage: Arbitrary[DateOfBirthPage.type] =";\
    print "    Arbitrary(DateOfBirthPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DateOfBirthPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def dateOfBirth: Option[Row] = userAnswers.get(DateOfBirthPage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"dateOfBirth.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(Literal(answer.format(dateFormatter))),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.DateOfBirthController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"dateOfBirth.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration DateOfBirth completed"
