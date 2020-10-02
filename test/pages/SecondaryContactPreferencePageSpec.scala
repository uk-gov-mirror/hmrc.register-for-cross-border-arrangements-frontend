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

import models.{SecondaryContactPreference, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours


class SecondaryContactPreferencePageSpec extends PageBehaviours {

  "SecondaryContactPreferencePage" - {

    beRetrievable[Set[SecondaryContactPreference]](SecondaryContactPreferencePage)

    beSettable[Set[SecondaryContactPreference]](SecondaryContactPreferencePage)

    beRemovable[Set[SecondaryContactPreference]](SecondaryContactPreferencePage)

    "must remove secondary contact telephone number when user changes answer to 'Email' only" in {
      forAll(arbitrary[UserAnswers]) {
        answers =>
          val result = answers
            .set(SecondaryContactTelephoneNumberPage, "07888888888")
            .success
            .value
            .set(SecondaryContactPreferencePage, SecondaryContactPreference.enumerable.withName("email").toSet)
            .success
            .value

          result.get(SecondaryContactTelephoneNumberPage) must not be defined
      }
    }

    "must remove secondary contact email address when user changes answer to 'Telephone' only" in {
      forAll(arbitrary[UserAnswers]) {
        answers =>
          val result = answers
            .set(SecondaryContactEmailAddressPage, "email@email.com")
            .success
            .value
            .set(SecondaryContactPreferencePage, SecondaryContactPreference.enumerable.withName("telephone").toSet)
            .success
            .value

          result.get(SecondaryContactEmailAddressPage) must not be defined
      }
    }
  }
}
