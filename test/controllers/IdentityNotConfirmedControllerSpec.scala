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

import base.SpecBase
import matchers.JsonMatchers
import models.{BusinessType, NormalMode, UserAnswers}
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.{BusinessTypePage, DoYouHaveANationalInsuranceNumberPage}
import play.api.libs.json.{JsObject, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.Html

import scala.concurrent.Future

class IdentityNotConfirmedControllerSpec extends SpecBase with MockitoSugar with JsonMatchers {

  "IdentityNotConfirmed Controller" - {

    "return OK and the correct view for a GET if business isn't confirmed" in {

      when(mockRenderer.render(any(), any())(any()))
        .thenReturn(Future.successful(Html("")))

      val userAnswers: UserAnswers = UserAnswers(userAnswersId).set(BusinessTypePage, BusinessType.CorporateBody).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()
      val request = FakeRequest(GET, routes.IdentityNotConfirmedController.onPageLoad().url)
      val templateCaptor = ArgumentCaptor.forClass(classOf[String])
      val jsonCaptor = ArgumentCaptor.forClass(classOf[JsObject])

      val result = route(application, request).value

      val expectedJson = Json.obj(
        "pageTitle" -> "identityNotConfirmed.business.title",
        "pageHeading" -> "identityNotConfirmed.business.heading",
        "tryAgainLink" -> "/register-for-cross-border-arrangements-frontend/register/do-you-have-a-utr"
      )

      status(result) mustEqual OK

      verify(mockRenderer, times(1)).render(templateCaptor.capture(), jsonCaptor.capture())(any())

      templateCaptor.getValue mustEqual "identityNotConfirmed.njk"
      jsonCaptor.getValue must containJson(expectedJson)

      application.stop()
    }

    "return OK and the correct view for a GET if identity isn't confirmed" in {

      when(mockRenderer.render(any(), any())(any()))
        .thenReturn(Future.successful(Html("")))

      val userAnswers: UserAnswers = UserAnswers(userAnswersId).set(DoYouHaveANationalInsuranceNumberPage, true).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()
      val request = FakeRequest(GET, routes.IdentityNotConfirmedController.onPageLoad().url)
      val templateCaptor = ArgumentCaptor.forClass(classOf[String])
      val jsonCaptor = ArgumentCaptor.forClass(classOf[JsObject])

      val result = route(application, request).value

      val expectedJson = Json.obj(
        "pageTitle" -> "identityNotConfirmed.identity.title",
        "pageHeading" -> "identityNotConfirmed.identity.heading",
        "tryAgainLink" -> "/register-for-cross-border-arrangements-frontend/register/do-you-have-a-utr"
      )

      status(result) mustEqual OK

      verify(mockRenderer, times(1)).render(templateCaptor.capture(), jsonCaptor.capture())(any())

      templateCaptor.getValue mustEqual "identityNotConfirmed.njk"
      jsonCaptor.getValue must containJson(expectedJson)

      application.stop()
    }
  }
}
