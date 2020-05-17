#!/bin/bash

echo ""
echo "Applying migration RegistrationType"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /registrationType                        controllers.RegistrationTypeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /registrationType                        controllers.RegistrationTypeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeRegistrationType                  controllers.RegistrationTypeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeRegistrationType                  controllers.RegistrationTypeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "registrationType.title = registrationType" >> ../conf/messages.en
echo "registrationType.heading = registrationType" >> ../conf/messages.en
echo "registrationType.business = Business" >> ../conf/messages.en
echo "registrationType.individual = Individual" >> ../conf/messages.en
echo "registrationType.checkYourAnswersLabel = registrationType" >> ../conf/messages.en
echo "registrationType.error.required = Select registrationType" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRegistrationTypeUserAnswersEntry: Arbitrary[(RegistrationTypePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RegistrationTypePage.type]";\
    print "        value <- arbitrary[RegistrationType].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRegistrationTypePage: Arbitrary[RegistrationTypePage.type] =";\
    print "    Arbitrary(RegistrationTypePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRegistrationType: Arbitrary[RegistrationType] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(RegistrationType.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RegistrationTypePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def registrationType: Option[Row] = userAnswers.get(RegistrationTypePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"registrationType.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(msg\"registrationType.$answer\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.RegistrationTypeController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"registrationType.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration RegistrationType completed"
