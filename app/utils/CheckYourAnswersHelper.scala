/*
 * Copyright 2021 HM Revenue & Customs
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
import helpers.JourneyHelpers
import models.{Address, BusinessType, CheckMode, UserAnswers}
import pages._
import uk.gov.hmrc.viewmodels.SummaryList._
import uk.gov.hmrc.viewmodels.Text.Literal
import uk.gov.hmrc.viewmodels._
import utils.CheckYourAnswersHelper._

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def secondaryContactTelephoneQuestion: Option[Row] = userAnswers.get(SecondaryContactTelephoneQuestionPage) map {
    answer =>
      Row(
        key     = Key(msg"secondaryContactTelephoneQuestion.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(yesOrNo(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.SecondaryContactTelephoneQuestionController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"secondaryContactTelephoneQuestion.checkYourAnswersLabel"))
          )
        )
      )
  }

  def selectAddress: Option[Row] = userAnswers.get(SelectAddressPage) map {
    answer =>
      Row(
        key     = Key(msg"selectAddress.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(Html(s"${answer.replace(",", "<br>")}")),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.SelectAddressController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"selectAddress.checkYourAnswersLabel"))
          )
        )
      )
  }

  def secondaryContactTelephoneNumber: Option[Row] = userAnswers.get(SecondaryContactTelephoneNumberPage) map {
    answer =>
      Row(
        key     = Key(msg"secondaryContactTelephoneNumber.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.SecondaryContactTelephoneNumberController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"secondaryContactTelephoneNumber.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-additional-contact-telephone-number")
          )
        )
      )
  }

  def secondaryContactEmailAddress: Option[Row] = userAnswers.get(SecondaryContactEmailAddressPage) map {
    answer =>
      Row(
        key     = Key(msg"secondaryContactEmailAddress.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.SecondaryContactEmailAddressController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"secondaryContactEmailAddress.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-additional-contact-email-address")
          )
        )
      )
  }

  def secondaryContactName: Option[Row] = userAnswers.get(SecondaryContactNamePage) map {
    answer =>
      Row(
        key     = Key(msg"secondaryContactName.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.SecondaryContactNameController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"secondaryContactName.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-additional-contact-name")
          )
        )
      )
  }

  def haveSecondContact: Option[Row] = userAnswers.get(HaveSecondContactPage) map {
    answer =>
      Row(
        key     = Key(msg"haveSecondContact.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(yesOrNo(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.HaveSecondContactController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"haveSecondContact.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-do-you-have-additional-contact")
          )
        )
      )
  }


  def whatIsYourAddressUk: Option[Row] = userAnswers.get(WhatIsYourAddressUkPage) map {
    answer =>
      Row(
        key     = Key(msg"whatIsYourUkAddress.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(formatAddress(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.WhatIsYourAddressUkController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"whatIsYourAddressUk.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-uk-address")
          )
        )
      )
  }

  def telephoneNumberQuestion: Option[Row] = userAnswers.get(TelephoneNumberQuestionPage) map {
    answer =>
      val checkYourAnswersLabel =
        if (JourneyHelpers.isOrganisationJourney(userAnswers)) {
          msg"telephoneNumberQuestion.organisation.checkYourAnswersLabel"
        } else {
          msg"telephoneNumberQuestion.individual.checkYourAnswersLabel"
        }

      Row(
        key     = Key(checkYourAnswersLabel, classes = Seq("govuk-!-width-one-half")),
        value   = Value(yesOrNo(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.TelephoneNumberQuestionController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(checkYourAnswersLabel)),
            attributes         = Map("id" -> "change-do-you-have-a-telephone")

          )
        )
      )
  }

  def contactTelephoneNumber: Option[Row] = userAnswers.get(ContactTelephoneNumberPage) map {
    answer =>
      Row(
        key     = Key(msg"contactTelephoneNumber.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.ContactTelephoneNumberController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"contactTelephoneNumber.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-telephone-number")
          )
        )
      )
  }

  def confirmBusiness: Option[Row] = userAnswers.get(ConfirmBusinessPage) map {
    answer =>
      val businessName = userAnswers.get(RetrievedNamePage).get
      val address = userAnswers.get(BusinessAddressPage).get

      Row(
        key     = Key(msg"confirmBusiness.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(
          Html(s"""
              $businessName<br><br>
              ${address.addressLine1}<br>
              ${address.addressLine2.fold("")(address => s"$address<br>")}<br>
              ${address.addressLine3}
              ${address.addressLine4.fold("")(address => s"$address<br>")}
              ${address.postCode.fold("")(postcode => s"$postcode<br>")}
              ${address.country.description}
              """)
        ),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.DoYouHaveUTRController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"confirmBusiness.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-your-business")
          )
        )
      )
  }

  def contactName: Option[Row] = userAnswers.get(ContactNamePage) map {
    answer =>
      Row(
        key     = Key(msg"contactName.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.ContactNameController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"contactName.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-contact-name")
          )
        )
      )
  }

  def contactEmailAddress: Option[Row] = userAnswers.get(ContactEmailAddressPage) map {
    answer =>
      Row(
        key     = Key(msg"contactEmailAddress.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.ContactEmailAddressController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"contactEmailAddress.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-email-address")
          )
        )
      )
  }

  def businessAddress: Option[Row] = userAnswers.get(BusinessAddressPage) map {
    answer =>
      Row(
        key     = Key(msg"businessAddress.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(formatAddress(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.BusinessAddressController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"businessAddress.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-business-address")
          )
        )
      )
  }

  def businessWithoutIDName: Option[Row] = userAnswers.get(BusinessWithoutIDNamePage) map {
    answer =>
      Row(
        key     = Key(msg"businessWithoutIDName.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"$answer"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.BusinessWithoutIDNameController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"businessWithoutIDName.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-business-name")
          )
        )
      )
  }

  def businessNamePage: Option[Row] = {
    val returnUrl = userAnswers.get(BusinessTypePage) map {
      case BusinessType.NotSpecified => routes.SoleTraderNameController.onPageLoad(CheckMode).url
      case BusinessType.Partnership => routes.BusinessNamePartnershipController.onPageLoad(CheckMode).url
      case BusinessType.LimitedLiability | BusinessType.CorporateBody => routes.BusinessNameRegisteredBusinessController.onPageLoad(CheckMode).url
      case BusinessType.UnIncorporatedBody => routes.BusinessNameOrganisationController.onPageLoad(CheckMode).url
    } getOrElse("")

    userAnswers.get(BusinessNamePage) map {
      answer =>
        Row(
          key     = Key(msg"businessNamePage.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
          value   = Value(lit"$answer"),
          actions = List(
            Action(
              content            = msg"site.edit",
              href               = returnUrl,
              visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"businessNamePage.checkYourAnswersLabel"))
            )
          )
        )
    }
  }

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
        value   = Value(formatAddress(answer)),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.WhatIsYourAddressController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"whatIsYourAddress.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-address")
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
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"doYouLiveInTheUK.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-do-you-live-in-the-uk")
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
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"nonUkName.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-name")
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
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"dateOfBirth.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-dob")
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
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"doYouHaveUTR.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-do-you-have-a-utr")
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
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"registrationType.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-registration-type")
          )
        )
      )
  }

  def namePage: Option[Row] = userAnswers.get(NamePage) map {
    answer =>
      Row(
        key = Key(msg"name.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value = Value(lit"${answer.firstName} ${answer.secondName}"),
        actions = List(
          Action(
            content             = msg"site.edit",
            href                = routes.NameController.onPageLoad(CheckMode).url,
            visuallyHiddenText  = Some(msg"site.edit.hidden".withArgs(msg"namePage.checkYourAnswersLabel")),
            attributes          = Map("id" -> "change-name")
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
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"nino.checkYourAnswersLabel")),
            attributes         = Map("id" -> "change-nino")
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
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"doYouHaveANationalInsuranceNumber.checkYourAnswersLabel")),
            attributes = Map("id" -> "change-do-you-have-national-insurance-number")
          )
        )
      )
  }

  def selfAssessmentUTR: Option[Row] = userAnswers.get(SelfAssessmentUTRPage) map {
    answer =>
      Row(
        key     = Key(msg"selfAssessmentUTR.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"${answer.uniqueTaxPayerReference}"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.SelfAssessmentUTRController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"selfAssessmentUTR.checkYourAnswersLabel"))
          )
        )
      )
  }

  def corporationTaxUTR: Option[Row] = userAnswers.get(CorporationTaxUTRPage) map {
    answer =>
      Row(
        key     = Key(msg"corporationTaxUTR.checkYourAnswersLabel", classes = Seq("govuk-!-width-one-half")),
        value   = Value(lit"${answer.uniqueTaxPayerReference}"),
        actions = List(
          Action(
            content            = msg"site.edit",
            href               = routes.CorporationTaxUTRController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"corporationTaxUTR.checkYourAnswersLabel"))
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

  private def formatAddress(answer: Address): Html = {
    Html(s"""
        ${answer.addressLine1}<br>
        ${answer.addressLine2.fold("")(address => s"$address<br>")}
        ${answer.addressLine3}<br>
        ${answer.addressLine4.fold("")(address => s"$address<br>")}
        ${answer.postCode.fold("")(postcode => s"$postcode<br>")}
        ${answer.country.description}
     """)
  }
}

object CheckYourAnswersHelper {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
}
