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

package controllers.auth

import com.google.inject.{ImplementedBy, Inject}
import config.FrontendAppConfig
import controllers.{auth, routes}
import play.api.mvc.Results.Redirect
import play.api.mvc._
import uk.gov.hmrc.auth.core.AuthProvider.GovernmentGateway
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals._
import uk.gov.hmrc.http.{HeaderCarrier, UnauthorizedException}

import scala.concurrent.{ExecutionContext, Future}

class AuthActionImpl @Inject()(override val authConnector: AuthConnector,
                               config: FrontendAppConfig,
                               mcc: MessagesControllerComponents)
                              (implicit ec: ExecutionContext, hc:HeaderCarrier) extends AuthAction with AuthorisedFunctions {

  override def parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser

  override protected def executionContext: ExecutionContext = mcc.executionContext

  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] = {
    authorised(AuthProviders(GovernmentGateway) and ConfidenceLevel.L50)
      .retrieve(allEnrolments) {

        case Enrolments(enrolments) =>
          val authenticatedRequest =  auth.AuthenticatedRequest[A](
            enrolments,
            request
          )

         block(authenticatedRequest)

        case _ => throw new UnauthorizedException("Unable to retrieve details")
      } recover authException(request)
  }

  private def authException[A](request: Request[A]): PartialFunction[Throwable, Result] = {

    case _: NoActiveSession =>

      val continueUrl = request.uri

      val url = if (request.rawQueryString.nonEmpty) {
        s"$continueUrl?${request.rawQueryString}"
      } else {
        continueUrl
      }
      Redirect(config.loginUrl, Map("continue" -> Seq(url)))
    case _: InsufficientEnrolments => Redirect(routes.UnauthorisedController.onPageLoad())
    case _: InsufficientConfidenceLevel => Redirect(routes.UnauthorisedController.onPageLoad())
    case _: UnsupportedAuthProvider => Redirect(routes.UnauthorisedController.onPageLoad())
    case _: UnauthorizedException => Redirect(routes.UnauthorisedController.onPageLoad())
  }
}

@ImplementedBy(classOf[AuthActionImpl])
trait AuthAction extends ActionBuilder[AuthenticatedRequest, AnyContent] with ActionFunction[Request, AuthenticatedRequest]
