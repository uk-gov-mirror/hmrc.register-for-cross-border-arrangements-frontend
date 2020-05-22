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

package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class BusinessWithoutIDNameFormProviderSpec extends StringFieldBehaviours {

  val form = new BusinessWithoutIDNameFormProvider()()

  ".businessWithoutIDName" - {

    val fieldName = "businessWithoutIDName"
    val requiredKey = "businessWithoutIDName.error.businessName.required"
    val lengthKey = "businessWithoutIDName.error.businessName.length"
    val invalidKey = "businessWithoutIDName.error.businessName.invalid"
    val maxLength = 105
    val businessNameRegex = """^[A-Za-z0-9&\/\\'\s]*$"""

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validBusinessName
    )

    behave like fieldWithMaxLengthAndInvalid(
      form,
      fieldName,
      maxLength = maxLength,
      invalidError = FormError(fieldName, invalidKey, Seq(businessNameRegex)),
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
