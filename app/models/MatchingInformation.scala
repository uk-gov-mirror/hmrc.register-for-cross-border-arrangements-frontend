/*
 * Copyright 2021 HM Revenue & Customs
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

package models

import models.error.RegisterError
import models.readSubscription.DisplaySubscriptionForDACResponse

trait MatchingInformation[T] {
  val details: Option[T]
  val safeId: Option[String]
  val existingSubscriptionInfo: Option[DisplaySubscriptionForDACResponse]
  val error: Option[RegisterError]
  val hasErrors: Boolean = error.isDefined
}

object MatchingInformation {

  case class IndividualMatchingInformation
  (  details: Option[PayloadRegistrationWithIDResponse]
   , safeId:  Option[String]
   , existingSubscriptionInfo: Option[DisplaySubscriptionForDACResponse] = None
   , error: Option[RegisterError] = None
  )  extends MatchingInformation[PayloadRegistrationWithIDResponse]

  case class BusinessMatchingInformation
  (  details: Option[BusinessDetails]
   , safeId:  Option[String]
   , existingSubscriptionInfo: Option[DisplaySubscriptionForDACResponse] = None
   , error: Option[RegisterError] = None
  )  extends MatchingInformation[BusinessDetails]

  object IndividualMatchingInformation {
    def apply(error: RegisterError): IndividualMatchingInformation = this(None, None, None, Some(error))
  }

  object BusinessMatchingInformation {
    def apply(error: RegisterError): BusinessMatchingInformation = this(None, None, None, Some(error))
  }
}