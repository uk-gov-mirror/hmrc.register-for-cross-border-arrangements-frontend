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
import models.Registration
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
    "for a registration submission" - {
      "must return status as OK for submission of registration" in {


        forAll(arbitrary[Registration]) {
          sub =>
            stubResponse(s"/register-for-cross-border-arrangements/registration/02.00.00/individual", OK)

            val result = connector.sendIndividualWithoutIDInformation(sub)
            result.futureValue.status mustBe OK
        }
      }

      "must return status as BAD_REQUEST for submission of invalid registration" in {


        forAll(arbitrary[Registration]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangements/registration/02.00.00/individual", BAD_REQUEST)

            val result = connector.sendIndividualWithoutIDInformation(sub)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as INTERNAL_SERVER_ERROR for submission for a technical error" in {


        forAll(arbitrary[Registration]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangements/registration/02.00.00/individual", INTERNAL_SERVER_ERROR)

            val result = connector.sendIndividualWithoutIDInformation(sub)
            result.futureValue.status mustBe INTERNAL_SERVER_ERROR
        }
      }

      "for an organisation registration submission" - {
        "must return status as OK for submission of valid Organisation registration" in {

          forAll(arbitrary[Registration]) {
            sub =>
              stubResponse("/register-for-cross-border-arrangements/registration/02.00.00/organisation", OK)

              val result = connector.sendOrganisationWithoutIDInformation(sub)
              result.futureValue.status mustBe OK
          }
        }

        "must return status as BAD_REQUEST for submission of invalid Organisation registration" in {

          forAll(arbitrary[Registration]) {
            sub =>
              stubResponse("/register-for-cross-border-arrangements/registration/02.00.00/organisation", BAD_REQUEST)

              val result = connector.sendOrganisationWithoutIDInformation(sub)
              result.futureValue.status mustBe BAD_REQUEST
          }
        }

        "must return status as INTERNAL_SERVER_ERROR for submission for a technical error" in {

          forAll(arbitrary[Registration]) {
            sub =>
              stubResponse("/register-for-cross-border-arrangements/registration/02.00.00/organisation", INTERNAL_SERVER_ERROR)

              val result = connector.sendOrganisationWithoutIDInformation(sub)
              result.futureValue.status mustBe INTERNAL_SERVER_ERROR
          }
        }
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

