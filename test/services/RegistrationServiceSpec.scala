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

package services

import java.time.LocalDate

import base.SpecBase
import connectors.RegistrationConnector
import generators.Generators
import models.RegistrationType.Individual
import models.{Address, Country, Name, UserAnswers}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages._
import play.api.Application
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RegistrationServiceSpec extends SpecBase
  with MockitoSugar
  with Generators
  with ScalaCheckPropertyChecks with BeforeAndAfterEach {

  val mockRegistrationConnector: RegistrationConnector = mock[RegistrationConnector]
  val registrationService: RegistrationService = app.injector.instanceOf[RegistrationService]

  override lazy val app: Application = new GuiceApplicationBuilder()
    .overrides(
      bind[RegistrationConnector].toInstance(mockRegistrationConnector)
    )
    .build()

"registration service" - {
  "when able to construct a registration object" - {
    "should send a request to the registration connector" in {

      val validNoIdAddress = Address("address1", "address2", Some("address3"), Some("address4"),Some("postcode"), Country("active","GB","UK"))


      forAll(arbitrary[UserAnswers], validPersonalName, validPersonalName, arbitrary[LocalDate]){

        (userAnswers, firstName, lastName, dob) =>
          val answers = userAnswers
            .set(NonUkNamePage, Name(firstName, lastName))
            .success
            .value
            .set(DateOfBirthPage, dob)
            .success
            .value
            .set(WhatIsYourAddressPage, validNoIdAddress )
            .success
            .value
            .set(ContactTelephoneNumberPage, "07000000000")
            .success
            .value
            .set(ContactEmailAddressPage, "test@test.com")
            .success
            .value
            .set(RegistrationTypePage, Individual)
            .success
            .value
            .set(DoYouLiveInTheUKPage, false)
            .success
            .value




          when(mockRegistrationConnector.sendIndividualWithoutIDInformation(any())(any(), any()))
            .thenReturn(
              Future.successful(HttpResponse(OK, ""))
            )

          val result = registrationService.sendIndividualRegistration(answers)

          whenReady(result){
            _.map(_.status) mustBe Some(OK)
          }

          verify(mockRegistrationConnector, times(1)).sendIndividualWithoutIDInformation(any())(any(),any())

          reset(mockRegistrationConnector)
      }
    }
  }
}
}
