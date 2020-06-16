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

package navigation

import java.time.LocalDate

import base.SpecBase
import controllers.routes
import generators.Generators
import models.RegistrationType.{Business, Individual}
import models.{BusinessType, CheckMode, Name, RegistrationType, SecondaryContactPreference, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages._
import uk.gov.hmrc.domain.Generator

class CheckModeNavigatorSpec extends SpecBase with ScalaCheckPropertyChecks with Generators {

  val navigator = new Navigator

  val name: Name = Name("FirstName", "LastName")

  "Navigator in Check mode" - {

    "must go from Do you have a UK Unique Taxpayer Reference (UTR)? page to" - {
      "What type of business do you have? when answer is 'Yes'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouHaveUTRPage, true)
                .success
                .value

            navigator
              .nextPage(DoYouHaveUTRPage, CheckMode, updatedAnswers)
              .mustBe(routes.BusinessTypeController.onPageLoad(CheckMode))
        }
      }

      "What are you registering as? page when answer is 'No'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouHaveUTRPage, false)
                .success
                .value

            navigator
              .nextPage(DoYouHaveUTRPage, CheckMode, updatedAnswers)
              .mustBe(routes.RegistrationTypeController.onPageLoad(CheckMode))
        }
      }
    }

    "must got from What are you registering as? page to" - {
      "What is the name of your business? when answer is 'A business'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(RegistrationTypePage, Business)
                .success
                .value

            navigator
              .nextPage(RegistrationTypePage, CheckMode, updatedAnswers)
              .mustBe(routes.BusinessWithoutIDNameController.onPageLoad(CheckMode))
        }
      }

      "Do you have a National Insurance number? page when answer is 'An individual'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(RegistrationTypePage, Individual)
                .success
                .value

            navigator
              .nextPage(RegistrationTypePage, CheckMode, updatedAnswers)
              .mustBe(routes.DoYouHaveANationalInsuranceNumberController.onPageLoad(CheckMode))
        }
      }
    }

    "must go from What is the name of your business? page to" - {
      "Check your answers page when answer is a new business name" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(BusinessWithoutIDNamePage, "New business name")
                .success
                .value

            navigator
              .nextPage(BusinessWithoutIDNamePage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from Do you have a National Insurance number? page to" - {
      "What is your National Insurance number? when answer is 'Yes'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouHaveANationalInsuranceNumberPage, true)
                .success
                .value

            navigator
              .nextPage(DoYouHaveANationalInsuranceNumberPage, CheckMode, updatedAnswers)
              .mustBe(routes.NinoController.onPageLoad(CheckMode))
        }
      }

      "What is your name? (non-UK) page when answer is 'No'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouHaveANationalInsuranceNumberPage, false)
                .success
                .value

            navigator
              .nextPage(DoYouHaveANationalInsuranceNumberPage, CheckMode, updatedAnswers)
              .mustBe(routes.NonUkNameController.onPageLoad(CheckMode))
        }
      }
    }

    "must go from What is your National Insurance number? page to" - {
      "Check your answers page when answer is a nino" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val nino = new Generator().nextNino
            val updatedAnswers =
              answers
                .set(NinoPage, nino)
                .success
                .value

            navigator
              .nextPage(NinoPage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from What is your name? page (non-UK) to" - {
      "Check your answers page when answer is a name" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(NonUkNamePage, name)
                .success
                .value

            navigator
              .nextPage(NonUkNamePage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from What is your name? page to" - {
      "Check your answers page when answer is a name" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(NamePage, name)
                .success
                .value

            navigator
              .nextPage(NamePage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from What is your date of birth? page to" - {
      "Business matching controller when answer is a date of birth and Individual user has a nino" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouHaveANationalInsuranceNumberPage, true)
                .success
                .value
                .set(DateOfBirthPage, LocalDate.now())
                .success
                .value

            navigator
              .nextPage(DateOfBirthPage, CheckMode, updatedAnswers)
              .mustBe(routes.BusinessMatchingController.matchIndividual())
        }
      }

      "Do you live in the UK? page when answer is a date of birth and Individual user doesn't have a nino" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouHaveANationalInsuranceNumberPage, false)
                .success
                .value
                .set(DateOfBirthPage, LocalDate.now())
                .success
                .value

            navigator
              .nextPage(DateOfBirthPage, CheckMode, updatedAnswers)
              .mustBe(routes.DoYouLiveInTheUKController.onPageLoad(CheckMode))
        }
      }
    }

    "must go from Do you live in the UK? page to" - {
      "What is your home address? when answer is 'Yes'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouLiveInTheUKPage, true)
                .success
                .value

            navigator
              .nextPage(DoYouLiveInTheUKPage, CheckMode, updatedAnswers)
              .mustBe(routes.WhatIsYourAddressUkController.onPageLoad(CheckMode))
        }
      }

      "What is your home address? (non-UK) page when answer is 'No'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(DoYouLiveInTheUKPage, false)
                .success
                .value

            navigator
              .nextPage(DoYouLiveInTheUKPage, CheckMode, updatedAnswers)
              .mustBe(routes.WhatIsYourAddressController.onPageLoad(CheckMode))
        }
      }
    }

      //**************************Contact Details etc.**********************************


    "must go from Who should we contact...? page to" - {
      "Check your answers page when answer is a contact name" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(ContactNamePage, name)
                .success
                .value

            navigator
              .nextPage(ContactNamePage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from What is *name*'s email address? page to" - {
      "Check your answers page when answer is an email" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(ContactEmailAddressPage, "email@email.com")
                .success
                .value

            navigator
              .nextPage(ContactEmailAddressPage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from Do you have a telephone number? page to" - {
      "What is your telephone number? when answer is 'Yes'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(TelephoneNumberQuestionPage, true)
                .success
                .value

            navigator
              .nextPage(TelephoneNumberQuestionPage, CheckMode, updatedAnswers)
              .mustBe(routes.ContactTelephoneNumberController.onPageLoad(CheckMode))
        }
      }

      "Is there someone else we can contact...? page when answer is 'No' and user is an organisation" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(BusinessTypePage, BusinessType.LimitedLiability)
                .success
                .value
                .set(TelephoneNumberQuestionPage, false)
                .success
                .value

            navigator
              .nextPage(TelephoneNumberQuestionPage, CheckMode, updatedAnswers)
              .mustBe(routes.HaveSecondContactController.onPageLoad(CheckMode))
        }
      }

      "Check your answers page when answer is 'No' and user is an Individual/Sole trader" in {
        val userAnswers = UserAnswers(userAnswersId)
          .set(RegistrationTypePage, RegistrationType.Individual)
          .success
          .value
          .set(TelephoneNumberQuestionPage, false)
          .success
          .value

        navigator
          .nextPage(TelephoneNumberQuestionPage, CheckMode, userAnswers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }
    }

    "must go from What is your telephone number? page to" - {
      "Is there someone else we can contact...? page when answer is a telephone number and user is an organisation" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(BusinessTypePage, BusinessType.CorporateBody)
                .success
                .value
                .set(ContactTelephoneNumberPage, "07888888888")
                .success
                .value

            navigator
              .nextPage(ContactTelephoneNumberPage, CheckMode, updatedAnswers)
              .mustBe(routes.HaveSecondContactController.onPageLoad(CheckMode))
        }
      }

      "Confirm your answers page when answer is a telephone number and user is an Individual/Sole trader" in {
        val userAnswers = UserAnswers(userAnswersId)
          .set(RegistrationTypePage, RegistrationType.Individual)
          .success
          .value
          .set(ContactTelephoneNumberPage, "07888888888")
          .success
          .value

        navigator
          .nextPage(ContactTelephoneNumberPage, CheckMode, userAnswers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }
    }

    "must go from Is there someone else we can contact if *name* is not available? page to" - {
      "What is the name of the individual or team we should contact? when answer is 'Yes'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(HaveSecondContactPage, true)
                .success
                .value

            navigator
              .nextPage(HaveSecondContactPage, CheckMode, updatedAnswers)
              .mustBe(routes.SecondaryContactNameController.onPageLoad(CheckMode))
        }
      }

      "Check your answers page when answer is 'No'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(HaveSecondContactPage, false)
                .success
                .value

            navigator
              .nextPage(HaveSecondContactPage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from What is the name of the individual or team we should contact? page to" - {
      "Check your answers page when answer is a name" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(SecondaryContactNamePage, "Secondary contact name")
                .success
                .value

            navigator
              .nextPage(SecondaryContactNamePage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

    "must go from How can we contact *name*? page to" - {
      "What is the email address for *name*? when answer is 'Email'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(SecondaryContactPreferencePage, SecondaryContactPreference.enumerable.withName("email").toSet)
                .success
                .value

            navigator
              .nextPage(SecondaryContactPreferencePage, CheckMode, updatedAnswers)
              .mustBe(routes.SecondaryContactEmailAddressController.onPageLoad(CheckMode))
        }
      }

      "What is the telephone number for *name*? when answer is 'Telephone'" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(SecondaryContactPreferencePage, SecondaryContactPreference.enumerable.withName("telephone").toSet)
                .success
                .value

            navigator
              .nextPage(SecondaryContactPreferencePage, CheckMode, updatedAnswers)
              .mustBe(routes.SecondaryContactTelephoneNumberController.onPageLoad(CheckMode))
        }
      }
    }

    "must go from What is the email address for *name*? page to" - {
      "Check your answers page when answer is an email and user only selected email as preference" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val updatedAnswers =
              answers
                .set(SecondaryContactPreferencePage, SecondaryContactPreference.enumerable.withName("email").toSet)
                .success
                .value
                .set(SecondaryContactEmailAddressPage, "email@email.com")
                .success
                .value

            navigator
              .nextPage(SecondaryContactEmailAddressPage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }

      "What is the telephone number for *name*? page when answer is an email and " +
        "user selected email and telephone as preferences" in {
        forAll(arbitrary[UserAnswers]) {
          answers =>
            val preferences = SecondaryContactPreference.values.toSet
            val updatedAnswers =
              answers
                .set(SecondaryContactPreferencePage, preferences)
                .success
                .value
                .set(SecondaryContactTelephoneNumberPage, "email@email.com")
                .success
                .value

            navigator
              .nextPage(SecondaryContactTelephoneNumberPage, CheckMode, updatedAnswers)
              .mustBe(routes.CheckYourAnswersController.onPageLoad())
        }
      }
    }

//    "must go from a page that doesn't exist in the edit route map  to Check Your Answers" in {
//
//      case object UnknownPage extends Page
//
//      forAll(arbitrary[UserAnswers]) {
//        answers =>
//
//          navigator.nextPage(UnknownPage, CheckMode, answers)
//            .mustBe(routes.CheckYourAnswersController.onPageLoad())
//      }
//    }
  }

}
