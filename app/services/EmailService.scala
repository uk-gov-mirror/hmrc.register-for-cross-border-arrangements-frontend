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

import connectors.EmailConnector
import javax.inject.{Inject, Singleton}
import models.{EmailRequest, UserAnswers}
import pages.{ContactEmailAddressPage, ContactNamePage, SecondaryContactEmailAddressPage, SecondaryContactNamePage}
import uk.gov.hmrc.emailaddress.EmailAddress
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmailService @Inject()(emailConnector:EmailConnector)(implicit executionContext: ExecutionContext) {

  def sendEmail(userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[Option[HttpResponse]] = {

    val emailAddress = userAnswers.get(ContactEmailAddressPage)
    val name = userAnswers.get(ContactNamePage).map(n => n.firstName + " " + n.secondName)

    val secondaryEmailAddress = userAnswers.get(SecondaryContactEmailAddressPage)
    val secondaryName = userAnswers.get(SecondaryContactNamePage)

    for {
      primaryResponse <- emailAddress
                          .filter(EmailAddress.isValid)
                          .fold(Future.successful(Option.empty[HttpResponse])) { email =>
                             emailConnector.sendEmail(EmailRequest.registration(email, name)).map(Some.apply)}

      _ <- secondaryEmailAddress
         .filter(EmailAddress.isValid)
          .fold(Future.successful(Option.empty[HttpResponse])) { secondaryEmailAddress =>
            emailConnector.sendEmail(EmailRequest.registration(secondaryEmailAddress, secondaryName)).map(Some.apply)
            }
    }
      yield primaryResponse
  }
}
