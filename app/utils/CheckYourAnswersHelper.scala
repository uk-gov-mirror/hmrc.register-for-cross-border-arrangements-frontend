/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils

import java.time.format.DateTimeFormatter

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.i18n.Messages
import CheckYourAnswersHelper._
import uk.gov.hmrc.viewmodels._
import uk.gov.hmrc.viewmodels.SummaryList._
import uk.gov.hmrc.viewmodels.Text.Literal

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def individualUKPostcode: Option[Row] = userAnswers.get(IndividualUKPostcodePage) map {
    answer =>
      Row(
        key     = Key(msg"individualUKPostcode.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.IndividualUKPostcodeController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"individualUKPostcode.checkYourAnswersLabel"))
          )
        )
      )
  }

  def whatIsYourAddress: Option[Row] = userAnswers.get(WhatIsYourAddressPage) map {
    answer =>
      Row(
        key     = Key(msg"whatIsYourAddress.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"${answer.lines}"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.WhatIsYourAddressController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"whatIsYourAddress.checkYourAnswersLabel"))
          )
        )
      )
  }

  def doYouLiveInTheUK: Option[Row] = userAnswers.get(DoYouLiveInTheUKPage) map {
    answer =>
      Row(
        key     = Key(msg"doYouLiveInTheUK.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(yesOrNo(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.DoYouLiveInTheUKController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"doYouLiveInTheUK.checkYourAnswersLabel"))
          )
        )
      )
  }

  def nonUkName: Option[Row] = userAnswers.get(NonUkNamePage) map {
    answer =>
      Row(
        key     = Key(msg"nonUkName.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"${answer.firstName} ${answer.secondName}"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.NonUkNameController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"nonUkName.checkYourAnswersLabel"))
          )
        )
      )
  }

  def dateOfBirth: Option[Row] = userAnswers.get(DateOfBirthPage) map {
    answer =>
      Row(
        key     = Key(msg"dateOfBirth.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(Literal(answer.format(dateFormatter))),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.DateOfBirthController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"dateOfBirth.checkYourAnswersLabel"))
          )
        )
      )
  }
            
  def doYouHaveUTR: Option[Row] = userAnswers.get(DoYouHaveUTRPage) map {
    answer =>
      Row(
        key     = Key(msg"doYouHaveUTR.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(yesOrNo(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.DoYouHaveUTRController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"doYouHaveUTR.checkYourAnswersLabel"))
          )
        )
      )
  }

  def registrationType: Option[Row] = userAnswers.get(RegistrationTypePage) map {
    answer =>
      Row(
        key     = Key(msg"registrationType.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(msg"registrationType.$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.RegistrationTypeController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"registrationType.checkYourAnswersLabel")))))
  }

  def namePage: Option[Row] = userAnswers.get(NamePage) map {
    answer =>
      Row(
        key = Key(msg"name.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value = Value(lit"$answer"),
        actions = List(
          Action(
            content = msg"site.edit",
            href = routes.NameController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"namePage.checkYourAnswersLabel"))
          )
        )
      )
  }

    def postCode: Option[Row] = userAnswers.get(PostCodePage) map {
      answer =>
        Row(
          key     = Key(msg"postCode.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
          value   = Value(lit"$answer"),
          actions = List(
            Action(
              content            = msg"site.edit",
              href               = routes.PostCodeController.onPageLoad(CheckMode).url,
              visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"postCode.checkYourAnswersLabel"))
          )
        )
      )
  }

  def nino: Option[Row] = userAnswers.get(NinoPage) map {
    answer =>
      Row(
        key     = Key(msg"nino.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.NinoController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"nino.checkYourAnswersLabel"))
          )
        )
      )
  }

  def doYouHaveANationalInsuranceNumber: Option[Row] = userAnswers.get(DoYouHaveANationalInsuranceNumberPage) map {
    answer =>
      Row(
        key = Key(msg"doYouHaveANationalInsuranceNumber.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value = Value(yesOrNo(answer)),
        actions = List(
          Action(
            content = msg"site.edit",
            href = routes.DoYouHaveANationalInsuranceNumberController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"doYouHaveANationalInsuranceNumber.checkYourAnswersLabel"))
          )
        )
      )
  }

  def uniqueTaxpayerReference: Option[Row] = userAnswers.get(UniqueTaxpayerReferencePage) map {
    answer =>
      Row(
        key     = Key(msg"uniqueTaxpayerReference.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"${answer.uniqueTaxPayerReference}"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.UniqueTaxpayerReferenceController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"uniqueTaxpayerReference.checkYourAnswersLabel"))
          )
        )
      )
  }

  def businessType: Option[Row] = userAnswers.get(BusinessTypePage) map {
    answer =>
      Row(
        key     = Key(msg"businessType.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(msg"businessType.$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.BusinessTypeController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"businessType.checkYourAnswersLabel"))
          )
        )
      )
  }

  private def yesOrNo(answer: Boolean): Content =
    if (answer) {
      msg"site.yes"
    } else {
      msg"site.no"
    }
}

object CheckYourAnswersHelper {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
}
