#!/bin/bash

echo ""
echo "Applying migration NamePage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /namePage                        controllers.NamePageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /namePage                        controllers.NamePageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeNamePage                  controllers.NamePageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeNamePage                  controllers.NamePageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "namePage.title = namePage" >> ../conf/messages.en
echo "namePage.heading = namePage" >> ../conf/messages.en
echo "namePage.checkYourAnswersLabel = namePage" >> ../conf/messages.en
echo "namePage.error.required = Enter namePage" >> ../conf/messages.en
echo "namePage.error.length = NamePage must be 50 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNamePageUserAnswersEntry: Arbitrary[(NamePagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NamePagePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNamePagePage: Arbitrary[NamePagePage.type] =";\
    print "    Arbitrary(NamePagePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NamePagePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def namePage: Option[Row] = userAnswers.get(NamePagePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"namePage.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"$answer\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.NamePageController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"namePage.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration NamePage completed"
