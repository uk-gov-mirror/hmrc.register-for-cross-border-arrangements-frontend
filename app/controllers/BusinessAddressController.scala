/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers

import controllers.actions.{NotEnrolledForDAC6Action, _}
import forms.BusinessAddressFormProvider
import helpers.JourneyHelpers.redirectToSummary
import javax.inject.Inject
import models.{Country, Mode}
import navigation.Navigator
import pages.BusinessAddressPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import renderer.Renderer
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import uk.gov.hmrc.viewmodels.NunjucksSupport
import utils.CountryListFactory

import scala.concurrent.{ExecutionContext, Future}

class BusinessAddressController @Inject()(override val messagesApi: MessagesApi,
                                          countryListFactory: CountryListFactory,
                                          sessionRepository: SessionRepository,
                                          navigator: Navigator,
                                          identify: IdentifierAction,
                                          notEnrolled: NotEnrolledForDAC6Action,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: BusinessAddressFormProvider,
                                          val controllerComponents: MessagesControllerComponents,
                                          renderer: Renderer
                                         )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with NunjucksSupport {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen notEnrolled andThen getData andThen requireData).async {
    implicit request =>

      val countries = countryListFactory.getCountryList.getOrElse(throw new Exception("Cannot retrieve country list"))
      val form = formProvider(countries)

      val preparedForm = request.userAnswers.get(BusinessAddressPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      val json = Json.obj(
        "form"   -> preparedForm,
        "mode"   -> mode,
        "countries" -> countryJsonList(preparedForm.data, countries)
      )

      renderer.render("businessAddress.njk", json).map(Ok(_))
  }

  private def countryJsonList(value: Map[String, String], countries: Seq[Country]): Seq[JsObject] = {
    def containsCountry(country: Country): Boolean =
      value.get("country") match {
        case Some(countrycode) => countrycode == country.code
        case _ => false
      }

    val countryJsonList = countries.map {
      country =>
        Json.obj("text" -> country.description, "value" -> country.code, "selected" -> containsCountry(country))
    }

    Json.obj("value" -> "", "text" -> "") +: countryJsonList
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen notEnrolled andThen getData andThen requireData).async {
    implicit request =>

      val countries = countryListFactory.getCountryList.getOrElse(throw new Exception("Cannot retrieve country list"))
      val form = formProvider(countries)

      form.bindFromRequest().fold(
        formWithErrors => {

          val json = Json.obj(
            "form"   -> formWithErrors,
            "mode"   -> mode,
            "countries" -> countryJsonList(formWithErrors.data, countries)
          )

          renderer.render("businessAddress.njk", json).map(BadRequest(_))
        },
        value => {
          val redirectUsers = redirectToSummary(value, BusinessAddressPage, mode, request.userAnswers)

          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(BusinessAddressPage, value))
            _ <- sessionRepository.set(updatedAnswers)
          } yield {
            if (redirectUsers) {
              Redirect(routes.CheckYourAnswersController.onPageLoad())
            } else {
              Redirect(navigator.nextPage(BusinessAddressPage, mode, updatedAnswers))
            }
          }
        }
      )
  }
}
