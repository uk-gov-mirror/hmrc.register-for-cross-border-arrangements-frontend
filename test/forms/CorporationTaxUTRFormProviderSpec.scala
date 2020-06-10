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

class CorporationTaxUTRFormProviderSpec extends StringFieldBehaviours {

  val form = new CorporationTaxUTRFormProvider()()

  ".corporationTaxUTRReference" - {

    val fieldName = "corporationTaxUTR"
    val requiredKey = "corporationTaxUTR.error.required"
    val lengthKey = "corporationTaxUTR.error.length"
    val invalidKey = "corporationTaxUTR.error.invalid"
    val utrRegex = "^[0-9]{10}$"

    val maxLength = 10
    val minLength = 10

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLengthAndInvalid(
      form,
      fieldName,
      maxLength = maxLength,
      invalidError = FormError(fieldName, invalidKey, Seq(utrRegex)),
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like fieldWithMinLengthAndInvalid(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey, Seq(utrRegex)),
      minLength = minLength,
      lengthError = FormError(fieldName, lengthKey, Seq(minLength))
    )


    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
