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

package generators

import java.time.LocalDate

import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import uk.gov.hmrc.domain.Nino

trait ModelGenerators {

  self: Generators =>

  val regime = "DACSIX"

  implicit lazy val arbitraryCountry: Arbitrary[Country] = {
    Arbitrary {
      for {
        state <- Gen.oneOf(Seq("Valid", "Invalid"))
        code  <- Gen.pick(2, 'A' to 'Z')
        name  <- arbitrary[String]
      } yield Country(state, code.mkString, name)
    }
  }

  implicit lazy val arbitraryWhatIsYourAddress: Arbitrary[Address] =
      Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[String]
        addressLine3 <- arbitrary[Option[String]]
        addressLine4 <- arbitrary[Option[String]]
        postCode <- arbitrary[Option[String]]
        country <- arbitrary[Country]
      } yield Address(addressLine1, addressLine2, addressLine3, addressLine4, postCode, country)
    }


  //TODO: Pull out length
  implicit val arbitraryName: Arbitrary[Name] = Arbitrary {
    for {
      firstName <- stringsWithMaxLength(50)
      secondName <- stringsWithMaxLength(50)
    } yield Name(firstName, secondName)
  }

  implicit val arbitraryNino: Arbitrary[Nino] = Arbitrary{
    for {
      prefix <- Gen.oneOf(Nino.validPrefixes)
      number <- Gen.choose(0, 999999)
      suffix <- Gen.oneOf(Nino.validSuffixes)
    } yield Nino(f"$prefix$number%06d$suffix")
  }

  implicit val arbitraryIndividualMatchingSubmission: Arbitrary[IndividualMatchingSubmission] = Arbitrary {
    for {
      name <- arbitrary[Name]
      dob <- arbitrary[LocalDate]
    } yield
      IndividualMatchingSubmission(regime,
        requiresNameMatch = true,
        isAnAgent = false,
        Individual(name, dob))
  }

  implicit val arbitraryBusinessMatchingSubmission: Arbitrary[BusinessMatchingSubmission] = Arbitrary {
    for {
      businessName <- arbitrary[String]
      businessType <- arbitraryBusinessType.arbitrary
    } yield
      BusinessMatchingSubmission(regime,
        requiresNameMatch = true,
        isAnAgent = false,
        Organisation(businessName, businessType))
  }

  implicit lazy val arbitraryUniqueTaxpayerReference: Arbitrary[UniqueTaxpayerReference] =
    Arbitrary {
      for {
        uniqueTaxPayerReference <- arbitrary[String]
      } yield UniqueTaxpayerReference(uniqueTaxPayerReference)
    }

  implicit lazy val arbitraryBusinessType: Arbitrary[BusinessType] =
    Arbitrary {
      Gen.oneOf(BusinessType.values.toSeq)
    }

  implicit lazy val arbitrarySecondaryContactPreference: Arbitrary[SecondaryContactPreference] =
    Arbitrary {
      Gen.oneOf(SecondaryContactPreference.values.toSeq)
    }

}
