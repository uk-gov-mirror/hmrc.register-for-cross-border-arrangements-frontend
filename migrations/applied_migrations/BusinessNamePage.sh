#!/bin/bash

echo ""
echo "Applying migration BusinessNamePage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /businessNamePage                        controllers.BusinessNamePageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /businessNamePage                        controllers.BusinessNamePageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeBusinessNamePage                  controllers.BusinessNamePageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeBusinessNamePage                  controllers.BusinessNamePageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "businessNamePage.title = businessNamePage" >> ../conf/messages.en
echo "businessNamePage.heading = businessNamePage" >> ../conf/messages.en
echo "businessNamePage.checkYourAnswersLabel = businessNamePage" >> ../conf/messages.en
echo "businessNamePage.error.required = Enter businessNamePage" >> ../conf/messages.en
echo "businessNamePage.error.length = BusinessNamePage must be 105 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessNamePageUserAnswersEntry: Arbitrary[(BusinessNamePagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[BusinessNamePagePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessNamePagePage: Arbitrary[BusinessNamePagePage.type] =";\
    print "    Arbitrary(BusinessNamePagePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(BusinessNamePagePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def businessNamePage: Option[Row] = userAnswers.get(BusinessNamePagePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"businessNamePage.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"$answer\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.BusinessNamePageController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"businessNamePage.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration BusinessNamePage completed"
