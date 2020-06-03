package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class ContactTelephoneNumberFormProvider @Inject() extends Mappings {

  def apply(): Form[String] =
    Form(
      "value" -> text("contactTelephoneNumber.error.required")
        .verifying(maxLength(50, "contactTelephoneNumber.error.length"))
    )
}
