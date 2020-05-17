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

class PostCodeFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "postCode.error.required"
  val lengthKey = "postCode.error.length"
  val invalidKey = "postCode.error.invalid"
  val regexPostCode = """^[A-Za-z]{1,2}[0-9Rr][0-9A-Za-z]?\s?[0-9][ABD-HJLNP-UW-Zabd-hjlnp-uw-z]{2}$"""
  val maxLength = 10

  val form = new PostCodeFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validPostCodes
    )

    behave like fieldWithMaxLengthAndInvalid(
      form,
      fieldName,
      maxLength = maxLength,
      invalidError = FormError(fieldName, invalidKey, Seq(regexPostCode)),
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
