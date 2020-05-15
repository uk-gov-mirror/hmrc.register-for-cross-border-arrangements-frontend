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
import connectors.BusinessMatchingConnector
import generators.Generators
import models.{Name, UserAnswers}
import org.mockito.Mockito.reset
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.{DateOfBirthPage, NamePage, NinoPage}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject._
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BusinessMatchingServiceSpec extends SpecBase
  with MockitoSugar
  with Generators
  with ScalaCheckPropertyChecks {

  val mockBusinessMatchingConnector = mock[BusinessMatchingConnector]

  override lazy val app: Application = new GuiceApplicationBuilder()
    .overrides(
      bind[BusinessMatchingConnector].toInstance(mockBusinessMatchingConnector)
    )
    .build()

  val businessMatchingService = app.injector.instanceOf[BusinessMatchingService]

  override def beforeEach: Unit =
    reset(
      mockBusinessMatchingConnector
    )

  "Business Matching Servce" - {
    "when able to construct an individual matching submission" - {
      "should send a request to the business matching connector" in {
        forAll(arbitrary[UserAnswers], arbitrary[Name], arbitrary[LocalDate], arbitrary[Nino]){
          (userAnswers, name, dob, nino) =>
            val answers = userAnswers
              .set(NamePage, name)
              .success
              .value
              .set(DateOfBirthPage, dob)
              .success
              .value
              .set(NinoPage, nino)
              .success
              .value

            when(mockBusinessMatchingConnector.sendIndividualMatchingInformation(any(), any())(any(), any()))
              .thenReturn(
                Future.successful(HttpResponse(200, None))
              )
            val result = businessMatchingService.sendIndividualMatchingInformation(answers)

            whenReady(result){
              _.map(_.status) mustBe Some(200)
            }
        }
      }
    }

    "when unable to construct an individual matching submission" - {
      "should return a future with no value" in {
        forAll(arbitrary[UserAnswers]){
          userAnswers =>
            val answers = userAnswers
                .remove(NamePage)
                .success
                .value
                .remove(DateOfBirthPage)
                .success
                .value
            val result = businessMatchingService.sendIndividualMatchingInformation(answers)

            whenReady(result){
              _ mustBe None
            }
        }
      }
    }




  }
}
