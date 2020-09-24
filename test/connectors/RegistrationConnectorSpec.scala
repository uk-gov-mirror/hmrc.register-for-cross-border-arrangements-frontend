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

package connectors

import base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import generators.Generators
import helpers.WireMockServerHandler
import models.Register
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.ExecutionContext.Implicits.global

class RegistrationConnectorSpec extends SpecBase
  with WireMockServerHandler
  with Generators
  with ScalaCheckPropertyChecks {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      conf = "microservice.services.business-matching.port" -> server.port()
    )
    .build()

  lazy val connector: RegistrationConnector = app.injector.instanceOf[RegistrationConnector]

  "registrationConnector" - {
      "must return status as OK for registration" in {


        forAll(arbitrary[Register]) {
          reg =>
            stubResponse(s"/register-for-cross-border-arrangements/registration/02.00.00/noId", OK)

            val result = connector.sendWithoutIDInformation(reg)
            result.futureValue.status mustBe OK
        }
      }

      "must return status as BAD_REQUEST for  registration" in {


        forAll(arbitrary[Register]) {
          reg =>
            stubResponse("/register-for-cross-border-arrangements/registration/02.00.00/noId", BAD_REQUEST)

            val result = connector.sendWithoutIDInformation(reg)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as INTERNAL_SERVER_ERROR for a technical error" in {


        forAll(arbitrary[Register]) {
          reg =>
            stubResponse("/register-for-cross-border-arrangements/registration/02.00.00/noId", INTERNAL_SERVER_ERROR)

            val result = connector.sendWithoutIDInformation(reg)
            result.futureValue.status mustBe INTERNAL_SERVER_ERROR
        }
      }
  }

    private def stubResponse(expectedUrl: String, expectedStatus: Int): StubMapping =
      server.stubFor(
        post(urlEqualTo(expectedUrl))
          .willReturn(
            aResponse()
              .withStatus(expectedStatus)
          )
      )
}

