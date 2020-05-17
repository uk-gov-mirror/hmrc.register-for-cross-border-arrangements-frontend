#!/bin/bash

echo ""
echo "Applying migration DoYouHaveUTR"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouHaveUTR                        controllers.DoYouHaveUTRController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouHaveUTR                        controllers.DoYouHaveUTRController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouHaveUTR                  controllers.DoYouHaveUTRController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouHaveUTR                  controllers.DoYouHaveUTRController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouHaveUTR.title = doYouHaveUTR" >> ../conf/messages.en
echo "doYouHaveUTR.heading = doYouHaveUTR" >> ../conf/messages.en
echo "doYouHaveUTR.checkYourAnswersLabel = doYouHaveUTR" >> ../conf/messages.en
echo "doYouHaveUTR.error.required = Select yes if doYouHaveUTR" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveUTRUserAnswersEntry: Arbitrary[(DoYouHaveUTRPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouHaveUTRPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveUTRPage: Arbitrary[DoYouHaveUTRPage.type] =";\
    print "    Arbitrary(DoYouHaveUTRPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouHaveUTRPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def doYouHaveUTR: Option[Row] = userAnswers.get(DoYouHaveUTRPage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"doYouHaveUTR.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(yesOrNo(answer)),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.DoYouHaveUTRController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"doYouHaveUTR.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration DoYouHaveUTR completed"
