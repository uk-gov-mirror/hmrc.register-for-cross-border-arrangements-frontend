package controllers

import base.SpecBase
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.FakeRequest
import play.api.test.Helpers._

class SignOutControllerSpec extends SpecBase with MockitoSugar {

  private def signOutRoute: String = controllers.routes.SignOutController.signOut().url
  private val application = applicationBuilder().build()

  "SignOut Controller"  - {

    "redirect to feedback survey page" in {

      when(mockAppConfig.signOutUrl).thenReturn(frontendAppConfig.signOutUrl)
      val result = route(application, FakeRequest(GET, signOutRoute)).value

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(frontendAppConfig.signOutUrl)
    }
  }
}
