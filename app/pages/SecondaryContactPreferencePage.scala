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

import models.SecondaryContactPreference.{Email, Telephone}
import models.{SecondaryContactPreference, UserAnswers}
import play.api.libs.json.JsPath

import scala.util.Try

case object SecondaryContactPreferencePage extends QuestionPage[Set[SecondaryContactPreference]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "secondaryContactPreference"

  override def cleanup(value: Option[Set[SecondaryContactPreference]], userAnswers: UserAnswers): Try[UserAnswers] =
    value match {
      case Some(preferences) if !preferences.contains(Telephone) => userAnswers.remove(SecondaryContactTelephoneNumberPage)
      case Some(preferences) if !preferences.contains(Email) => userAnswers.remove(SecondaryContactEmailAddressPage)
      case _ => super.cleanup(value, userAnswers)
    }
}
