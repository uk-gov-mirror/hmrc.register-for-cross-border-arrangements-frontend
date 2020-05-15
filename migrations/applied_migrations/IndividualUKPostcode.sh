#!/bin/bash

echo ""
echo "Applying migration IndividualUKPostcode"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /individualUKPostcode                        controllers.IndividualUKPostcodeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /individualUKPostcode                        controllers.IndividualUKPostcodeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIndividualUKPostcode                  controllers.IndividualUKPostcodeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIndividualUKPostcode                  controllers.IndividualUKPostcodeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "individualUKPostcode.title = individualUKPostcode" >> ../conf/messages.en
echo "individualUKPostcode.heading = individualUKPostcode" >> ../conf/messages.en
echo "individualUKPostcode.checkYourAnswersLabel = individualUKPostcode" >> ../conf/messages.en
echo "individualUKPostcode.error.required = Enter individualUKPostcode" >> ../conf/messages.en
echo "individualUKPostcode.error.length = IndividualUKPostcode must be 8 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIndividualUKPostcodeUserAnswersEntry: Arbitrary[(IndividualUKPostcodePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IndividualUKPostcodePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIndividualUKPostcodePage: Arbitrary[IndividualUKPostcodePage.type] =";\
    print "    Arbitrary(IndividualUKPostcodePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IndividualUKPostcodePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def individualUKPostcode: Option[Row] = userAnswers.get(IndividualUKPostcodePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"individualUKPostcode.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"$answer\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.IndividualUKPostcodeController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"individualUKPostcode.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration IndividualUKPostcode completed"
