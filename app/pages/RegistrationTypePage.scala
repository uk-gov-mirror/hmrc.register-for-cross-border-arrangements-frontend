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

import models.RegistrationType.{Business, Individual}
import models.{RegistrationType, UserAnswers}
import play.api.libs.json.JsPath

import scala.util.Try

case object RegistrationTypePage extends QuestionPage[RegistrationType] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "registrationType"

  override def cleanup(value: Option[RegistrationType], userAnswers: UserAnswers): Try[UserAnswers] =
    (value, userAnswers.get(HaveSecondContactPage)) match {
      case (Some(Business), _) =>
        userAnswers.remove(DoYouHaveANationalInsuranceNumberPage)
          .flatMap(_.remove(NinoPage))
          .flatMap(_.remove(NamePage))
          .flatMap(_.remove(DateOfBirthPage))
          .flatMap(_.remove(NonUkNamePage))
          .flatMap(_.remove(DoYouLiveInTheUKPage))
          .flatMap(_.remove(WhatIsYourAddressUkPage))
          .flatMap(_.remove(WhatIsYourAddressPage))
      case (Some(Individual), Some(_)) =>
        userAnswers.remove(BusinessWithoutIDNamePage)
          .flatMap(_.remove(BusinessAddressPage))
          .flatMap(_.remove(HaveSecondContactPage))
          .flatMap(_.remove(SecondaryContactNamePage))
          .flatMap(_.remove(SecondaryContactPreferencePage))
          .flatMap(_.remove(SecondaryContactEmailAddressPage))
          .flatMap(_.remove(SecondaryContactTelephoneNumberPage))
      case (Some(Individual), None) =>
        userAnswers.remove(BusinessWithoutIDNamePage)
          .flatMap(_.remove(BusinessAddressPage))
      case _ => super.cleanup(value, userAnswers)
    }
}
