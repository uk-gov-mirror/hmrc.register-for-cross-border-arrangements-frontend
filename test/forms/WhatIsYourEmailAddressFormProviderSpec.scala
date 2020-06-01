package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class WhatIsYourEmailAddressFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "whatIsYourEmailAddress.error.required"
  val lengthKey = "whatIsYourEmailAddress.error.length"
  val maxLength = 254

  val form = new WhatIsYourEmailAddressFormProvider()()

  ".value" - {

    val fieldName = "value"

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
}
