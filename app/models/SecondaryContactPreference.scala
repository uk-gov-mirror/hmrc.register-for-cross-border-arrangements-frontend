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

package models

import play.api.data.Form
import uk.gov.hmrc.viewmodels._

sealed trait SecondaryContactPreference

object SecondaryContactPreference extends Enumerable.Implicits {

  case object Email extends WithName("email") with SecondaryContactPreference
  case object Telephone extends WithName("telephone") with SecondaryContactPreference

  val values: Seq[SecondaryContactPreference] = Seq(
    Email,
    Telephone
  )

  def checkboxes(form: Form[_]): Seq[Checkboxes.Item] = {

    val field = form("value")
    val items = Seq(
      Checkboxes.Checkbox(msg"secondaryContactPreference.email", Email.toString),
      Checkboxes.Checkbox(msg"secondaryContactPreference.telephone", Telephone.toString)
    )

    Checkboxes.set(field, items)
  }

  implicit val enumerable: Enumerable[SecondaryContactPreference] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
