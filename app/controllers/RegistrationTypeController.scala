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

import controllers.actions._
import forms.RegistrationTypeFormProvider
import helpers.JourneyHelpers.redirectToSummary

import javax.inject.Inject
import models.{Mode, RegistrationType, UserAnswersHelper}
import navigation.Navigator
import pages.RegistrationTypePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import renderer.Renderer
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import uk.gov.hmrc.viewmodels.NunjucksSupport

import scala.concurrent.{ExecutionContext, Future}

class RegistrationTypeController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            sessionRepository: SessionRepository,
                                            navigator: Navigator,
                                            identify: IdentifierAction,
                                            notEnrolled: NotEnrolledForDAC6Action,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            formProvider: RegistrationTypeFormProvider,
                                            val controllerComponents: MessagesControllerComponents,
                                            renderer: Renderer
                                          )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with NunjucksSupport {

  private val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen notEnrolled andThen getData andThen requireData).async {
    implicit request =>

      val preparedForm = request.userAnswers.get(RegistrationTypePage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      val json = Json.obj(
        "form"   ->  preparedForm,
        "mode"   -> mode,
        "radios"  -> RegistrationType.radios(preparedForm)
      )

      renderer.render("registrationType.njk", json).map(Ok(_))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen notEnrolled andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {

          val json = Json.obj(
            "form"   -> formWithErrors,
            "mode"   -> mode,
            "radios" -> RegistrationType.radios(formWithErrors)
          )

          renderer.render("registrationType.njk", json).map(BadRequest(_))
        },
        registrationType => {
          val redirectUsers = redirectToSummary(registrationType, RegistrationTypePage, mode, request.userAnswers)

          for {
            updatedAnswers <- UserAnswersHelper.updateUserAnswersIfValueChanged(request.userAnswers, RegistrationTypePage, registrationType)
            _              <- sessionRepository.set(updatedAnswers)
          } yield {
            if (redirectUsers) {
              Redirect(routes.CheckYourAnswersController.onPageLoad())
            } else {
              Redirect(navigator.nextPage(RegistrationTypePage, mode, updatedAnswers))
            }
          }
        }
      )
  }
}
