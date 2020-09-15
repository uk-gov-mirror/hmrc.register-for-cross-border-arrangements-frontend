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

import config.FrontendAppConfig
import controllers.routes
import javax.inject.Inject
import models.{CheckMode, SubscriptionInfo, UserAnswers}
import pages.{CorporationTaxUTRPage, NinoPage, SelfAssessmentUTRPage, WhatIsYourAddressPage}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.viewmodels.SummaryList.{Action, Key, Row, Value}

import scala.concurrent.{ExecutionContext, Future}

class SubscriptionConnector @Inject()(val config: FrontendAppConfig, val http: HttpClient) {

  def createSubscription(userAnswers: UserAnswers)
                        (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {

    val submissionUrl = s"${config.businessMatchingUrl}/enrolment/create-enrolment"
    http.PUT[SubscriptionInfo, HttpResponse](submissionUrl, SubscriptionInfo.createSubscriptionInfo(userAnswers))
  }
}
