package pages

import models.BusinessName
import pages.behaviours.PageBehaviours

class BusinessNamePageSpec extends PageBehaviours {

  "BusinessNamePage" - {

    beRetrievable[BusinessName](BusinessNamePage)

    beSettable[BusinessName](BusinessNamePage)

    beRemovable[BusinessName](BusinessNamePage)
  }
}
