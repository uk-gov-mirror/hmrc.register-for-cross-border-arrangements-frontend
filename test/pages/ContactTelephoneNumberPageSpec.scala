package pages

import pages.behaviours.PageBehaviours


class ContactTelephoneNumberPageSpec extends PageBehaviours {

  "ContactTelephoneNumberPage" - {

    beRetrievable[String](ContactTelephoneNumberPage)

    beSettable[String](ContactTelephoneNumberPage)

    beRemovable[String](ContactTelephoneNumberPage)
  }
}
