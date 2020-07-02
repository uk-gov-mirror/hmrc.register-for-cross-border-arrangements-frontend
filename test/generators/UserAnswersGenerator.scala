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

package generators

import models.UserAnswers
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.TryValues
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersGenerator extends TryValues {
  self: Generators =>

  val generators: Seq[Gen[(QuestionPage[_], JsValue)]] =
    arbitrary[(SelectAddressPage.type, JsValue)] ::
    arbitrary[(SecondaryContactTelephoneNumberPage.type, JsValue)] ::
    arbitrary[(SecondaryContactEmailAddressPage.type, JsValue)] ::
    arbitrary[(CorporationTaxUTRPage.type, JsValue)] ::
    arbitrary[(SecondaryContactNamePage.type, JsValue)] ::
    arbitrary[(SecondaryContactPreferencePage.type, JsValue)] ::
    arbitrary[(WhatIsYourAddressUkPage.type, JsValue)] ::
    arbitrary[(TelephoneNumberQuestionPage.type, JsValue)] ::
    arbitrary[(ContactTelephoneNumberPage.type, JsValue)] ::
    arbitrary[(HaveSecondContactPage.type, JsValue)] ::
    arbitrary[(ConfirmBusinessPage.type, JsValue)] ::
    arbitrary[(ContactNamePage.type, JsValue)] ::
    arbitrary[(ContactEmailAddressPage.type, JsValue)] ::
    arbitrary[(BusinessAddressPage.type, JsValue)] ::
    arbitrary[(BusinessWithoutIDNamePage.type, JsValue)] ::
    arbitrary[(BusinessNamePage.type, JsValue)] ::
    arbitrary[(WhatIsYourAddressPage.type, JsValue)] ::
    arbitrary[(IndividualUKPostcodePage.type, JsValue)] ::
    arbitrary[(DoYouLiveInTheUKPage.type, JsValue)] ::
    arbitrary[(DateOfBirthPage.type, JsValue)] ::
    arbitrary[(DoYouHaveUTRPage.type, JsValue)] ::
    arbitrary[(RegistrationTypePage.type, JsValue)] ::
    arbitrary[(NamePage.type, JsValue)] ::
    arbitrary[(NinoPage.type, JsValue)] ::
    arbitrary[(DoYouHaveANationalInsuranceNumberPage.type, JsValue)] ::
    arbitrary[(PostCodePage.type, JsValue)] ::
    arbitrary[(SelfAssessmentUTRPage.type, JsValue)] ::
    arbitrary[(BusinessTypePage.type, JsValue)] ::
    arbitrary[(SoleTraderNamePage.type, JsValue)] ::
    Nil

  implicit lazy val arbitraryUserData: Arbitrary[UserAnswers] = {

    import models._

    Arbitrary {
      for {
        id      <- nonEmptyString
        data    <- generators match {
          case Nil => Gen.const(Map[QuestionPage[_], JsValue]())
          case _   => Gen.mapOf(oneOf(generators))
        }
      } yield UserAnswers (
        id = id,
        data = data.foldLeft(Json.obj()) {
          case (obj, (path, value)) =>
            obj.setObject(path.path, value).get
        }
      )
    }
  }
}
