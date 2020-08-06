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

import javax.inject.Inject
import forms.mappings.Mappings
import models.Name
import play.api.data.Form
import play.api.data.Forms.mapping
import utils.RegexConstants

class ContactNameFormProvider @Inject() extends Mappings with RegexConstants {
  val maxLength: Int = 35

  def apply(): Form[Name] =
    Form(
      mapping(
        "firstName" -> validatedText(
          "contactName.error.required.firstName",
            "contactName.error.invalid.firstName",
            "contactName.error.length.firstName", apiNameRegex, maxLength),
        "lastName" -> validatedText(
          "contactName.error.required.lastName",
            "contactName.error.invalid.lastName",
            "contactName.error.length.lastName", apiNameRegex, maxLength)
      )(Name.apply)(Name.unapply)
    )
}
