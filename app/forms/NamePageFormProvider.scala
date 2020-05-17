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
import play.api.data.Forms._

class NamePageFormProvider @Inject() extends Mappings {

  def apply(): Form[Name] =
    Form(
      mapping(
      "firstName" -> text("name.error.firstName.required")
        .verifying(maxLength(50, "name.error.firstName.length")),
      "secondName" -> text("name.error.secondName.required")
        .verifying(maxLength(50, "name.error.secondName.length"))
      )(Name.apply)(Name.unapply)
    )
}
