#!/bin/bash

echo ""
echo "Applying migration CorporationTaxUTR"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /corporationTaxUTR                        controllers.CorporationTaxUTRController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /corporationTaxUTR                        controllers.CorporationTaxUTRController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeCorporationTaxUTR                  controllers.CorporationTaxUTRController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeCorporationTaxUTR                  controllers.CorporationTaxUTRController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "corporationTaxUTR.title = corporationTaxUTR" >> ../conf/messages.en
echo "corporationTaxUTR.heading = corporationTaxUTR" >> ../conf/messages.en
echo "corporationTaxUTR.checkYourAnswersLabel = corporationTaxUTR" >> ../conf/messages.en
echo "corporationTaxUTR.error.required = Enter corporationTaxUTR" >> ../conf/messages.en
echo "corporationTaxUTR.error.length = CorporationTaxUTR must be 22 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCorporationTaxUTRUserAnswersEntry: Arbitrary[(CorporationTaxUTRPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CorporationTaxUTRPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCorporationTaxUTRPage: Arbitrary[CorporationTaxUTRPage.type] =";\
    print "    Arbitrary(CorporationTaxUTRPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CorporationTaxUTRPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def corporationTaxUTR: Option[Row] = userAnswers.get(CorporationTaxUTRPage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"corporationTaxUTR.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"$answer\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.CorporationTaxUTRController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"corporationTaxUTR.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration CorporationTaxUTR completed"
