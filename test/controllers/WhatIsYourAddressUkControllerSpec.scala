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

import base.SpecBase
import config.FrontendAppConfig
import forms.WhatIsYourAddressUkFormProvider
import matchers.JsonMatchers
import models.{Address, CheckMode, Country, NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.{IndividualUKPostcodePage, WhatIsYourAddressUkPage}
import play.api.data.Form
import play.api.inject.bind
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.Html
import repositories.SessionRepository
import uk.gov.hmrc.viewmodels.NunjucksSupport
import utils.CountryListFactory

import scala.concurrent.Future

class WhatIsYourAddressUkControllerSpec extends SpecBase with MockitoSugar with NunjucksSupport with JsonMatchers {

  def onwardRoute: Call = Call("GET", "/foo")
  val mockSessionRepository: SessionRepository = mock[SessionRepository]
  val mockFrontendAppConfig: FrontendAppConfig = mock[FrontendAppConfig]

  val mockCountryFactory: CountryListFactory = mock[CountryListFactory]

  val country = Country("valid","GB","United Kingdom")
  val address = Address("value 1", Some("value 2"), "value 3", Some("value 4"), Some("XX9 9XX"), country)

  val formProvider = new WhatIsYourAddressUkFormProvider()
  val form: Form[Address] = formProvider(Seq(country))

  lazy val whatIsYourAddressUkRoute: String = routes.WhatIsYourAddressUkController.onPageLoad(NormalMode).url

  "WhatIsYourAddressUk Controller" - {

    "must return OK and the correct view for a GET" in {

      when(mockRenderer.render(any(), any())(any()))
        .thenReturn(Future.successful(Html("")))

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
      val request = FakeRequest(GET, whatIsYourAddressUkRoute)
      val templateCaptor = ArgumentCaptor.forClass(classOf[String])
      val jsonCaptor = ArgumentCaptor.forClass(classOf[JsObject])

      val result = route(application, request).value

      status(result) mustEqual OK

      verify(mockRenderer, times(1)).render(templateCaptor.capture(), jsonCaptor.capture())(any())

      val expectedJson = Json.obj(
        "form" -> form,
        "mode" -> NormalMode
      )

      templateCaptor.getValue mustEqual "whatIsYourAddressUk.njk"
      jsonCaptor.getValue must containJson(expectedJson)

      application.stop()
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      when(mockRenderer.render(any(), any())(any()))
        .thenReturn(Future.successful(Html("")))

      when(mockCountryFactory.uk).thenReturn(country)

      val userAnswers = UserAnswers(userAnswersId).set(WhatIsYourAddressUkPage, address).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).overrides(bind[CountryListFactory].toInstance(mockCountryFactory)).build()
      val request = FakeRequest(GET, whatIsYourAddressUkRoute)
      val templateCaptor = ArgumentCaptor.forClass(classOf[String])
      val jsonCaptor = ArgumentCaptor.forClass(classOf[JsObject])

      val result = route(application, request).value

      status(result) mustEqual OK

      verify(mockRenderer, times(1)).render(templateCaptor.capture(), jsonCaptor.capture())(any())

      val filledForm = form.bind(
        Map(
          "addressLine1" -> "value 1",
          "addressLine2" -> "value 2",
          "addressLine3" -> "value 3",
          "addressLine4" -> "value 4",
          "postCode" -> "XX9 9XX",
          "country" -> "GB"
        )
      )

      val expectedJson = Json.obj(
        "form" -> filledForm,
        "mode" -> NormalMode
      )

      templateCaptor.getValue mustEqual "whatIsYourAddressUk.njk"
      jsonCaptor.getValue must containJson(expectedJson)

      application.stop()
    }

    "must populate the view with the postcode on a GET if postcode lookup failed previously" in {

      when(mockRenderer.render(any(), any())(any()))
        .thenReturn(Future.successful(Html("")))

      when(mockCountryFactory.uk).thenReturn(country)

      val userAnswers = UserAnswers(userAnswersId)
        .set(IndividualUKPostcodePage, "XX9 9XX").success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers))
        .overrides(bind[CountryListFactory].toInstance(mockCountryFactory)).build()
      val request = FakeRequest(GET, whatIsYourAddressUkRoute)
      val templateCaptor = ArgumentCaptor.forClass(classOf[String])
      val jsonCaptor = ArgumentCaptor.forClass(classOf[JsObject])

      val result = route(application, request).value

      status(result) mustEqual OK

      verify(mockRenderer, times(1)).render(templateCaptor.capture(), jsonCaptor.capture())(any())

      val filledForm = form.fill(Address("", None, "", None, Some("XX9 9XX"), country))

      val expectedJson = Json.obj(
        "form" -> filledForm,
        "mode" -> NormalMode
      )

      templateCaptor.getValue mustEqual "whatIsYourAddressUk.njk"
      jsonCaptor.getValue must containJson(expectedJson)

      application.stop()
    }

    "must redirect to the next page when valid data is submitted" in {

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      when(mockCountryFactory.getCountryList).thenReturn(Some(Seq(country,Country("valid","ES","Spain"))))

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute, appConfig = mockFrontendAppConfig)),
            bind[SessionRepository].toInstance(mockSessionRepository),
            bind[CountryListFactory].toInstance(mockCountryFactory)
          )
          .build()

      val request =
        FakeRequest(POST, whatIsYourAddressUkRoute)
          .withFormUrlEncodedBody(("addressLine1", "value 1"), ("addressLine2", "value 2"),("addressLine3", "value 3"), ("addressLine4", "value 4"),
            ("postCode", "XX9 9XX"),("country", "GB"))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      when(mockRenderer.render(any(), any())(any()))
        .thenReturn(Future.successful(Html("")))

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
      val request = FakeRequest(POST, whatIsYourAddressUkRoute).withFormUrlEncodedBody(("value", ""))
      val boundForm = form.bind(Map("value" -> ""))
      val templateCaptor = ArgumentCaptor.forClass(classOf[String])
      val jsonCaptor = ArgumentCaptor.forClass(classOf[JsObject])

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      verify(mockRenderer, times(1)).render(templateCaptor.capture(), jsonCaptor.capture())(any())

      val expectedJson = Json.obj(
        "form" -> boundForm,
        "mode" -> NormalMode
      )

      templateCaptor.getValue mustEqual "whatIsYourAddressUk.njk"
      jsonCaptor.getValue must containJson(expectedJson)

      application.stop()
    }

    "must redirect to the Check your answers page when user doesn't change their answer" in {
      val whatIsYourAddressUkRoute: String = routes.WhatIsYourAddressUkController.onPageLoad(CheckMode).url
      val userAnswers = UserAnswers(userAnswersId).set(WhatIsYourAddressUkPage, address).success.value

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)
      when(mockCountryFactory.getCountryList).thenReturn(Some(Seq(country, Country("valid","ES","Spain"))))

      val application =
        applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute, appConfig = mockFrontendAppConfig)),
            bind[SessionRepository].toInstance(mockSessionRepository),
            bind[CountryListFactory].toInstance(mockCountryFactory)
          )
          .build()

      val request =
        FakeRequest(POST, whatIsYourAddressUkRoute)
          .withFormUrlEncodedBody(
            ("addressLine1", "value 1"),
            ("addressLine2", "value 2"),
            ("addressLine3", "value 3"),
            ("addressLine4", "value 4"),
            ("postCode", "XX9 9XX"),
            ("country", "GB")
          )

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.CheckYourAnswersController.onPageLoad().url

      application.stop()
    }

    "must redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, whatIsYourAddressUkRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "must redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, whatIsYourAddressUkRoute)
          .withFormUrlEncodedBody(("value", "answer"))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
