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
import models.Country
import play.api.data.FormError

class WhatIsYourAddressUkFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "whatIsYourAddressUk.error.required"
  val lengthKey = "whatIsYourAddressUk.error.length"
  val maxLength = 35


  val countries = Seq(Country("valid", "AD", "Andorra"))
  val form = new WhatIsYourAddressUkFormProvider()(countries)

  ".addressLine1" - {

    val fieldName = "addressLine1"
    val requiredKey = "whatIsYourUkAddress.error.addressLine1.required"
    val lengthKey = "whatIsYourUkAddress.error.addressLine1.length"
    val maxLength = 35

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }

  ".addressLine2" - {

    val fieldName = "addressLine2"
    val requiredKey = "whatIsYourUkAddress.error.addressLine2.required"
    val lengthKey = "whatIsYourUkAddress.error.addressLine2.length"
    val maxLength = 35

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }

  ".postCode" - {

    val fieldName = "postCode"
    val requiredKey = "whatIsYourUkAddress.error.postcode.required"
    val lengthKey = "whatIsYourUkAddress.error.postcode.length"
    val invalidKey = "whatIsYourUkAddress.error.postcode.invalid"
    val maxLength = 8
    val regexPostCode = """^[A-Za-z]{1,2}[0-9Rr][0-9A-Za-z]?\s?[0-9][ABD-HJLNP-UW-Zabd-hjlnp-uw-z]{2}$"""

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validPostCodes
    )

    behave like fieldWithValidatedRegex(
      form,
      fieldName,
      maxLength = maxLength,
      invalidError = FormError(fieldName, Seq(invalidKey))
    )

   behave like mandatoryField(
     form,
     fieldName,
     requiredError = FormError(fieldName, requiredKey)
   )
  }
}
