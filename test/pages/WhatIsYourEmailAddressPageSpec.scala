package pages

import pages.behaviours.PageBehaviours


class WhatIsYourEmailAddressPageSpec extends PageBehaviours {

  "WhatIsYourEmailAddressPage" - {

    beRetrievable[String](WhatIsYourEmailAddressPage)

    beSettable[String](WhatIsYourEmailAddressPage)

    beRemovable[String](WhatIsYourEmailAddressPage)
  }
}
