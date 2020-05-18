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

package controllers.auth

import config.FrontendAppConfig
import controllers.auth
import models.requests.UserRequest
import org.scalatest.{FreeSpec, MustMatchers}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.Results.Ok
import play.api.mvc.{AnyContent, MessagesControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.Enrolment

import scala.concurrent.{ExecutionContext, Future}

class NotEnrolledForDac6ActionSpec extends FreeSpec with MustMatchers with MockitoSugar with GuiceOneAppPerSuite {

  val mockFrontendAppConfig = mock[FrontendAppConfig]
  val mockDac6Enrolment = mock[Enrolment]

  implicit val executionContext: ExecutionContext = ExecutionContext.global
  lazy val appConfig = app.injector.instanceOf[FrontendAppConfig]
  lazy val mockMcc: MessagesControllerComponents = app.injector.instanceOf[MessagesControllerComponents]


  def harness[A]()(implicit request: AuthenticatedRequest[A]): Future[Result] = {

    lazy val actionProvider = app.injector.instanceOf[NotEnrolledForDAC6Action]

    actionProvider.invokeBlock(
      request, { _: UserRequest[_] =>
        Future.successful(Ok(""))
      }
    )
  }

  def createAuthenticatedRequest(dac6Enrolment: Set[Enrolment]): AuthenticatedRequest[AnyContent] =
    auth.AuthenticatedRequest(
      dac6Enrolment,
      FakeRequest()
    )


  "NotEnrolledForDAC6Action must " - {

    "redirect to the unauthorised page when" - {

      "the user has a DAC6 enrolment" in {
        implicit val request = createAuthenticatedRequest(dac6Enrolment = Set(Enrolment("DAC6")))
        val result = harness()(request)
        status(result) mustBe SEE_OTHER
      }
    }

    "create an authorised request when" - {

      "the user has no DAC6 enrolment key" in {
        implicit val request = createAuthenticatedRequest(dac6Enrolment = Set(Enrolment("FOO")))
        val result = harness()(request)
        status(result) mustBe OK
      }
    }
  }
}
