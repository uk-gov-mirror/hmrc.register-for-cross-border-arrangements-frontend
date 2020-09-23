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

import java.util.UUID

import connectors.RegistrationConnector
import javax.inject.Inject
import models.{IndRegistration, OrgRegistration, RegisterWithoutIDRequest, RequestCommon, Registration, UserAnswers}
import org.joda.time.{DateTime, DateTimeZone}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

class RegistrationService @Inject()(registrationConnector: RegistrationConnector){

  val acknRef: String = UUID.randomUUID().toString
  val dateTime: String = DateTime.now(DateTimeZone.UTC).toString

  def sendIndividualRegistration(userAnswers: UserAnswers)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[HttpResponse]] = {
    IndRegistration(userAnswers) match {
      case Some(registration) =>

        val subscribe = Registration(RegisterWithoutIDRequest(
          RequestCommon(dateTime, "DAC", acknRef, None),
          registration)
        )
        registrationConnector.sendWithoutIDInformation(subscribe).map(Some(_))
      case _ => Future.successful(None)
    }
  }

  def sendOrganisationRegistration(userAnswers: UserAnswers)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[HttpResponse]] = {
    OrgRegistration(userAnswers) match {
      case Some(organisationRegistration) =>
        val subscribe = Registration(RegisterWithoutIDRequest(
          RequestCommon(dateTime, "DAC", acknRef, None),
          organisationRegistration))

        registrationConnector.sendWithoutIDInformation(subscribe).map(Some(_))
      case _ => Future.successful(None)
    }
  }
}

