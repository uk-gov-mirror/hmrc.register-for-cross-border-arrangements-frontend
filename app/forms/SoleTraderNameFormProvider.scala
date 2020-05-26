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
import models.Name
import play.api.data.Form
import play.api.data.Forms._

class SoleTraderNameFormProvider @Inject() extends Mappings {
    val firstNameRegex = "^[a-zA-Z &`\\-\\'^]{1,35}$"
    val lastNameRegex =  "^[a-zA-Z &`\\-\\'^]{1,35}$"


  def apply(): Form[Name] =
    Form(
      mapping(
      "firstName" -> textNonWhitespaceOnly("soleTraderName.error.firstName.required")
        .verifying(regexp(firstNameRegex,"soleTraderName.error.firstName.invalid"))
        .verifying(maxLength(35, "soleTraderName.error.firstName.length")),
      "secondName" -> textNonWhitespaceOnly("soleTraderName.error.secondName.required")
        .verifying(regexp(lastNameRegex,"soleTraderName.error.secondName.invalid"))
        .verifying(maxLength(35, "soleTraderName.error.secondName.length"))
      )(Name.apply)(Name.unapply)
    )
}
