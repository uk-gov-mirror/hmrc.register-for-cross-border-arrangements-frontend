package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class BusinessNameFormProviderSpec extends StringFieldBehaviours {

  val form = new BusinessNameFormProvider()()

  ".businessName" - {

    val fieldName = "businessName"
    val requiredKey = "businessName.error.businessName.required"
    val lengthKey = "businessName.error.businessName.length"
    val maxLength = 105

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

  ".field2" - {

    val fieldName = "field2"
    val requiredKey = "businessName.error.field2.required"
    val lengthKey = "businessName.error.field2.length"
    val maxLength = 100

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
