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

package controllers

import java.time.LocalDate

import base.SpecBase
import matchers.JsonMatchers
import models.{Name, UserAnswers}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import pages.{DateOfBirthPage, NamePage, NinoPage}
import play.api.inject._
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.BusinessMatchingService
import uk.gov.hmrc.domain.Generator
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.viewmodels.NunjucksSupport

import scala.concurrent.Future

class BusinessMatchingControllerSpec extends SpecBase with MockitoSugar with NunjucksSupport with JsonMatchers {

  lazy val businessMatchingRoute = routes.BusinessMatchingController.matchIndividual().url

  def getRequest(): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, businessMatchingRoute)

  val mockBusinessMatchingService = mock[BusinessMatchingService]



  "BusinessMatching Controller" - {
    "when a correct submission can be created and returns a match" - {

      "must redirect the user to the check your answers page" in {

        val userAnswers = UserAnswers(userAnswersId)
          .set(DateOfBirthPage, LocalDate.now())
          .success
          .value
          .set(NamePage, Name("", ""))
          .success
          .value
          .set(NinoPage, (new Generator()).nextNino)
          .success
          .value

        val application = applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(
            bind[BusinessMatchingService].toInstance(mockBusinessMatchingService)
          ).build()

        when(mockBusinessMatchingService.sendIndividualMatchingInformation(any())(any(), any()))
          .thenReturn(Future.successful(Some(HttpResponse(200, None))))

        val result = route(application, getRequest).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result) mustBe Some("/register-for-cross-border-arrangements-frontend/check-your-answers")
      }
    }

    "when a correct submission can be created and returns no match" - {

      "must redirect the user to the cant find identity page" in {

        val userAnswers = UserAnswers(userAnswersId)
          .set(DateOfBirthPage, LocalDate.now())
          .success
          .value
          .set(NamePage, Name("", ""))
          .success
          .value
          .set(NinoPage, (new Generator()).nextNino)
          .success
          .value

        val application = applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(
            bind[BusinessMatchingService].toInstance(mockBusinessMatchingService)
          ).build()

        when(mockBusinessMatchingService.sendIndividualMatchingInformation(any())(any(), any()))
          .thenReturn(Future.successful(Some(HttpResponse(404, None))))

        val result = route(application, getRequest).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result) mustBe Some("/register-for-cross-border-arrangements-frontend/register/individual-identity-not-confirmed")
      }
    }
  }

}
