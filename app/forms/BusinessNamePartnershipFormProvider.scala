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
import play.api.data.Form

class BusinessNamePartnershipFormProvider @Inject() extends Mappings {
  private val nameRegex = "^[a-zA-Z0-9 '&\\/]{1,105}$"

  def apply(): Form[String] =
    Form(
      "value" ->  textNonWhitespaceOnly("businessName.partnership.error.required")
        .verifying(regexp(nameRegex,"businessName.partnership.error.invalid"))
        .verifying(maxLength(105, "businessName.partnership.error.length"))
    )
}
