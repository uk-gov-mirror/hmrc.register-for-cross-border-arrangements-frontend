#!/bin/bash

echo ""
echo "Applying migration UniqueTaxpayerReference"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /uniqueTaxpayerReference                        controllers.UniqueTaxpayerReferenceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /uniqueTaxpayerReference                        controllers.UniqueTaxpayerReferenceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeUniqueTaxpayerReference                  controllers.UniqueTaxpayerReferenceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeUniqueTaxpayerReference                  controllers.UniqueTaxpayerReferenceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "uniqueTaxpayerReference.title = uniqueTaxpayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.heading = uniqueTaxpayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.uniqueTaxPayerReference = uniqueTaxPayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.field2 = field2" >> ../conf/messages.en
echo "uniqueTaxpayerReference.checkYourAnswersLabel = uniqueTaxpayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.error.uniqueTaxPayerReference.required = Enter uniqueTaxPayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.error.field2.required = Enter field2" >> ../conf/messages.en
echo "uniqueTaxpayerReference.error.uniqueTaxPayerReference.length = uniqueTaxPayerReference must be 13 characters or less" >> ../conf/messages.en
echo "uniqueTaxpayerReference.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUniqueTaxpayerReferenceUserAnswersEntry: Arbitrary[(UniqueTaxpayerReferencePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[UniqueTaxpayerReferencePage.type]";\
    print "        value <- arbitrary[UniqueTaxpayerReference].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUniqueTaxpayerReferencePage: Arbitrary[UniqueTaxpayerReferencePage.type] =";\
    print "    Arbitrary(UniqueTaxpayerReferencePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUniqueTaxpayerReference: Arbitrary[UniqueTaxpayerReference] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        uniqueTaxPayerReference <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield UniqueTaxpayerReference(uniqueTaxPayerReference, field2)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(UniqueTaxpayerReferencePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def uniqueTaxpayerReference: Option[Row] = userAnswers.get(UniqueTaxpayerReferencePage) map {";\
     print "    answer =>";\
     print "      Row(";\
     print "        key     = Key(msg\"uniqueTaxpayerReference.checkYourAnswersLabel\", classes = Seq(\"govuk-!-width-one-half\")),";\
     print "        value   = Value(lit\"${answer.uniqueTaxPayerReference} ${answer.field2}\"),";\
     print "        actions = List(";\
     print "          Action(";\
     print "            content            = msg\"site.edit\",";\
     print "            href               = routes.UniqueTaxpayerReferenceController.onPageLoad(CheckMode).url,";\
     print "            visuallyHiddenText = Some(msg\"site.edit.hidden\".withArgs(msg\"uniqueTaxpayerReference.checkYourAnswersLabel\"))";\
     print "          )";\
     print "        )";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration UniqueTaxpayerReference completed"
