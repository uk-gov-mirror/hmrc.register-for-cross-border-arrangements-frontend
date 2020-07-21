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

import forms.mappings.Mappings
import javax.inject.Inject
import models.{Address, Country}
import play.api.data.Form
import play.api.data.Forms._
import utils.RegexConstants

class BusinessAddressFormProvider @Inject() extends Mappings with RegexConstants {

  val addressLineLength = 35
  val postCodeLength = 10
  def apply(countryList: Seq[Country]): Form[Address] = Form(
    mapping(
      "addressLine1" ->  validatedText("businessAddress.error.addressLine1.required",
        "businessAddress.error.addressLine1.invalid",
        "businessAddress.error.addressLine1.length",
        apiAddressRegex,
        addressLineLength
      ),
      "addressLine2" ->  validatedText("businessAddress.error.addressLine2.required",
        "businessAddress.error.addressLine2.invalid",
        "businessAddress.error.addressLine2.length",
        apiAddressRegex,
        addressLineLength
      ),
      "addressLine3" -> validatedOptionalText("businessAddress.error.addressLine3.invalid",
        "businessAddress.error.addressLine3.length",
        apiAddressRegex,
        addressLineLength),
      "addressLine4" -> validatedOptionalText("businessAddress.error.addressLine4.invalid",
        "businessAddress.error.addressLine4.length",
        apiAddressRegex,
        addressLineLength),
      "postCode" -> optionalText().verifying(maxLength(postCodeLength,"businessAddress.error.postcode.length")),
      "country" ->  text("businessAddress.error.country.required")
        .verifying("businessAddress.error.country.required", value => countryList.exists(_.code == value))
        .transform[Country](value => countryList.find(_.code == value).get, _.code)
    )(Address.apply)(Address.unapply)
  )

}
