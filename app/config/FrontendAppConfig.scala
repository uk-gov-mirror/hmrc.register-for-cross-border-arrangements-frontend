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

package config

import com.google.inject.{Inject, Singleton}
import controllers.routes
import play.api.Configuration
import play.api.i18n.Lang
import play.api.mvc.Call

@Singleton
class FrontendAppConfig @Inject() (configuration: Configuration) {

  lazy val appName: String = configuration.get[String]("appName")

  private val contactHost = configuration.get[String]("contact-frontend.host")
  private val contactFormServiceIdentifier = "DAC6"

  val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUrl = s"$contactHost/contact/beta-feedback?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUnauthenticatedUrl = s"$contactHost/contact/beta-feedback-unauthenticated?service=$contactFormServiceIdentifier"

  lazy val countryCodeJson: String = configuration.get[String]("json.countries")
  val analyticsToken: String = configuration.get[String](s"google-analytics.token")
  val analyticsHost: String = configuration.get[String](s"google-analytics.host")
  val signOutUrl: String             = configuration.get[String]("urls.logout")

  lazy val authUrl: String = configuration.get[Service]("auth").baseUrl
  lazy val loginUrl: String = configuration.get[String]("urls.login")
  lazy val loginContinueUrl: String = configuration.get[String]("urls.loginContinue")
  lazy val businessMatchingUrl: String = s"${configuration.get[Service]("microservice.services.business-matching").baseUrl}${configuration.get[String]("microservice.services.business-matching.startUrl")}"
  lazy val crossBorderArrangementsUrl: String = configuration.get[Service]("microservice.services.cross-border-arrangements").baseUrl
  lazy val addressLookUpUrl: String = configuration.get[Service]("microservice.services.address-lookup").baseUrl
  lazy val sendEmailUrl: String = configuration.get[Service]("microservice.services.email").baseUrl

  lazy val dacSubmissionsUrl: String = s"${configuration.get[String]("urls.dac-submissions.host")}${configuration.get[String]("urls.dac-submissions.startUrl")}"
  lazy val lostUTRUrl: String = "https://www.gov.uk/find-lost-utr-number"

  lazy val languageTranslationEnabled: Boolean =
    configuration.get[Boolean]("microservice.services.features.welsh-translation")

  lazy val timeoutSeconds: String = configuration.get[String]("session.timeoutSeconds")
  lazy val countdownSeconds: String = configuration.get[String]("session.countdownSeconds")

  //Toggles
  lazy val addressLookupToggle: Boolean = configuration.get[String]("addressLookupToggle").toBoolean
  lazy val recruitmentBannerToggle: Boolean = configuration.get[Boolean]("recruitmentBannerToggle")

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  def routeToSwitchLanguage: String => Call =
    (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)
}
