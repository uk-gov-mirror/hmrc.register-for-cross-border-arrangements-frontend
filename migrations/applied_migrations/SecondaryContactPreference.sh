#!/bin/bash

echo ""
echo "Applying migration SecondaryContactPreference"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /secondaryContactPreference                        controllers.SecondaryContactPreferenceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /secondaryContactPreference                        controllers.SecondaryContactPreferenceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSecondaryContactPreference                  controllers.SecondaryContactPreferenceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSecondaryContactPreference                  controllers.SecondaryContactPreferenceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "secondaryContactPreference.title = How can we contact them?" >> ../conf/messages.en
echo "secondaryContactPreference.heading = How can we contact them?" >> ../conf/messages.en
echo "secondaryContactPreference.email = Email" >> ../conf/messages.en
echo "secondaryContactPreference.telephone = Telephone" >> ../conf/messages.en
echo "secondaryContactPreference.checkYourAnswersLabel = How can we contact them?" >> ../conf/messages.en
echo "secondaryContactPreference.error.required = Select secondaryContactPreference" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarySecondaryContactPreferenceUserAnswersEntry: Arbitrary[(SecondaryContactPreferencePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[SecondaryContactPreferencePage.type]";\
    print "        value <- arbitrary[SecondaryContactPreference].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarySecondaryContactPreferencePage: Arbitrary[SecondaryContactPreferencePage.type] =";\
    print "    Arbitrary(SecondaryContactPreferencePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarySecondaryContactPreference: Arbitrary[SecondaryContactPreference] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(SecondaryContactPreference.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(SecondaryContactPreferencePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def secondaryContactPreference: Option[Row] = userAnswers.get(SecondaryContactPreferencePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"secondaryContactPreference.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(Html(answer.map(a => msg\"secondaryContactPreference.$a\".resolve).mkString(\",<br>\"))),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.SecondaryContactPreferenceController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"secondaryContactPreference.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration SecondaryContactPreference completed"
