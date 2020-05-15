#!/bin/bash

echo ""
echo "Applying migration Nino"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /nino                        controllers.NinoController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /nino                        controllers.NinoController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeNino                  controllers.NinoController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeNino                  controllers.NinoController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "nino.title = nino" >> ../conf/messages.en
echo "nino.heading = nino" >> ../conf/messages.en
echo "nino.checkYourAnswersLabel = nino" >> ../conf/messages.en
echo "nino.error.required = Enter nino" >> ../conf/messages.en
echo "nino.error.length = Nino must be 9 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNinoUserAnswersEntry: Arbitrary[(NinoPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NinoPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNinoPage: Arbitrary[NinoPage.type] =";\
    print "    Arbitrary(NinoPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NinoPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def nino: Option[Row] = userAnswers.get(NinoPage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"nino.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"$answer\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.NinoController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"nino.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration Nino completed"
