#!/bin/bash

echo ""
echo "Applying migration DoYouLiveInTheUK"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouLiveInTheUK                        controllers.DoYouLiveInTheUKController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouLiveInTheUK                        controllers.DoYouLiveInTheUKController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouLiveInTheUK                  controllers.DoYouLiveInTheUKController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouLiveInTheUK                  controllers.DoYouLiveInTheUKController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouLiveInTheUK.title = doYouLiveInTheUK" >> ../conf/messages.en
echo "doYouLiveInTheUK.heading = doYouLiveInTheUK" >> ../conf/messages.en
echo "doYouLiveInTheUK.checkYourAnswersLabel = doYouLiveInTheUK" >> ../conf/messages.en
echo "doYouLiveInTheUK.error.required = Select yes if doYouLiveInTheUK" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouLiveInTheUKUserAnswersEntry: Arbitrary[(DoYouLiveInTheUKPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouLiveInTheUKPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouLiveInTheUKPage: Arbitrary[DoYouLiveInTheUKPage.type] =";\
    print "    Arbitrary(DoYouLiveInTheUKPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouLiveInTheUKPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def doYouLiveInTheUK: Option[Row] = userAnswers.get(DoYouLiveInTheUKPage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"doYouLiveInTheUK.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(yesOrNo(answer)),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.DoYouLiveInTheUKController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"doYouLiveInTheUK.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration DoYouLiveInTheUK completed"
