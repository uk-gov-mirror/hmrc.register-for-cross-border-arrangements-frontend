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

package pages

import models.UserAnswers
import play.api.libs.json.JsPath

import scala.util.Try

case object DoYouHaveUTRPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "doYouHaveUTR"

  override def cleanup(value: Option[Boolean], userAnswers: UserAnswers): Try[UserAnswers] =
  //Not removing BusinessAddressPage as the page is used in both journeys
    value match {
      case Some(true) =>
        userAnswers.remove(RegistrationTypePage)
        .flatMap(_.remove(BusinessWithoutIDNamePage))
        .flatMap(_.remove(DoYouHaveANationalInsuranceNumberPage))
        .flatMap(_.remove(NinoPage))
        .flatMap(_.remove(NamePage))
        .flatMap(_.remove(DateOfBirthPage))
        .flatMap(_.remove(NonUkNamePage))
        .flatMap(_.remove(DoYouLiveInTheUKPage))
        .flatMap(_.remove(IndividualUKPostcodePage))
        .flatMap(_.remove(SelectAddressPage))
        .flatMap(_.remove(WhatIsYourAddressUkPage))
        .flatMap(_.remove(WhatIsYourAddressPage))
        .flatMap(_.remove(ContactEmailAddressPage))
        .flatMap(_.remove(TelephoneNumberQuestionPage))
        .flatMap(_.remove(ContactTelephoneNumberPage))
      case Some(false) =>
        userAnswers.remove(BusinessTypePage)
          .flatMap(_.remove(CorporationTaxUTRPage))
          .flatMap(_.remove(SelfAssessmentUTRPage))
          .flatMap(_.remove(BusinessNamePage))
          .flatMap(_.remove(ConfirmBusinessPage))
          .flatMap(_.remove(ContactNamePage))
          .flatMap(_.remove(ContactEmailAddressPage))
          .flatMap(_.remove(TelephoneNumberQuestionPage))
          .flatMap(_.remove(ContactTelephoneNumberPage))
          .flatMap(_.remove(HaveSecondContactPage))
          .flatMap(_.remove(SecondaryContactNamePage))
          .flatMap(_.remove(SecondaryContactPreferencePage))
          .flatMap(_.remove(SecondaryContactEmailAddressPage))
          .flatMap(_.remove(SecondaryContactTelephoneNumberPage))
      case None => super.cleanup(value, userAnswers)
    }

}
