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

package models

import models.RegistrationType.Business
import pages._
import play.api.libs.json._


case class NoIdOrganisation(organisationName: String)

object NoIdOrganisation {
  implicit val format: OFormat[NoIdOrganisation] = Json.format[NoIdOrganisation]
}

//ToDo This is different to one we are collecting and validating on the form this needs looked at
case class AddressNoId(addressLine1: String, addressLine2:Option[String], addressLine3: String, addressLine4: Option[String], postalCode: Option[String], countryCode: String)

object AddressNoId {
  def apply(address: Address): Option[AddressNoId] =
    Some(AddressNoId(
      addressLine1 = address.addressLine1,
      addressLine2 = Some(address.addressLine2),
      addressLine3 = address.addressLine3.get,
      addressLine4 = address.addressLine4,
      postalCode = address.postCode,
      countryCode = address.country.code
    ))


  implicit lazy val writes: OWrites[AddressNoId] = OWrites[AddressNoId] {
    address =>
      Json.obj(
        "addressLine1" -> address.addressLine1,
        "addressLine2" -> address.addressLine2,
        "addressLine3" -> address.addressLine3,
        "addressLine4" -> address.addressLine4,
        "postalCode" -> address.postalCode,
        "countryCode" -> address.countryCode
      )
  }

  implicit lazy val reads: Reads[AddressNoId] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "addressLine1").read[String] and
        (__ \ "addressLine2").readNullable[String] and
        (__ \ "addressLine3").read[String] and
        (__ \ "addressLine4").readNullable[String] and
        (__ \ "postalCode").readNullable[String] and
        (__ \ "countryCode").read[String]
      )((a1, a2,a3, a4, pc, cc) => AddressNoId(a1, a2,a3, a4, pc, cc))
  }
}

case class ContactDetails(phoneNumber: Option[String], mobileNumber: Option[String], faxNumber: Option[String], emailAddress: Option[String])

object ContactDetails {
  implicit lazy val writes: OWrites[ContactDetails] = OWrites[ContactDetails] {
    details =>
      Json.obj(
        "phoneNumber" -> details.phoneNumber,
        "mobileNumber" -> details.mobileNumber,
        "faxNumber" -> details.faxNumber,
        "emailAddress" -> details.emailAddress
      )
  }

  implicit lazy val reads: Reads[ContactDetails] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "phoneNumber").readNullable[String] and
        (__ \ "mobileNumber").readNullable[String] and
        (__ \ "faxNumber").readNullable[String] and
        (__ \ "emailAddress").readNullable[String]
      )((phone, mobile,fax, email) => ContactDetails(phone, mobile,fax, email))
  }
}

case class Identification(idNumber: String, issuingInstitution: String, issuingCountryCode: String)

object Identification {
  implicit lazy val writes: OWrites[Identification] = OWrites[Identification] {
    id =>
      Json.obj(
        "idNumber" -> id.idNumber,
        "issuingInstitution" -> id.issuingInstitution,
        "issuingCountryCode" -> id.issuingCountryCode
      )
  }

  implicit lazy val reads: Reads[Identification] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "idNumber").read[String] and
        (__ \ "issuingInstitution").read[String] and
        (__ \ "issuingCountryCode").read[String]
      )((number, institution, cc) => Identification(number, institution, cc))
  }
}

case class RequestCommon(receiptDate: String, regime:String, acknowledgementRef: String, parameters: Option[String])
object RequestCommon {
  implicit lazy val writes: OWrites[RequestCommon] = OWrites[RequestCommon] {
    request =>
      Json.obj(
        "receiptDate" -> request.receiptDate,
        "regime" -> "DAC",
        "acknowledgementReference" -> request.acknowledgementRef,
        "parameters" -> request.parameters
      )
  }

  implicit lazy val reads: Reads[RequestCommon] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "receiptDate").read[String] and
        (__ \ "regime").read[String] and
        (__ \ "acknowledgementReference").read[String] and
        (__ \ "parameters").readNullable[String]
      ) ((receipt, regime, ar, param) => RequestCommon(receipt, regime, ar, param))
  }

case class RequestParameters(paramName: String, paramValue: String)

object RequestParameters {
  implicit val formats = Json.format[RequestParameters]
  }
}



case class RequestDetails(organisation: Option[NoIdOrganisation], individual: Option[Individual], address: AddressNoId, contactDetails: ContactDetails,
                          identification: Option[Identification])

object RequestDetails {
  implicit val formats = Json.format[RequestDetails]
}

object Registration{
  def apply(userAnswers: UserAnswers): Option[RequestDetails] = userAnswers.get(RegistrationTypePage) match {
    case Some(models.RegistrationType.Individual) => IndRegistration.apply(userAnswers)
    case Some(Business) => OrgRegistration.apply(userAnswers)
    case _ => throw new Exception("Cannot retrieve registration type")
  }
}


object OrgRegistration  {

  def apply(userAnswers: UserAnswers): Option[RequestDetails] =
    for {
      organisationName <- getBusinessName(userAnswers)
      addressBusiness <- userAnswers.get(BusinessAddressPage)
      address <- AddressNoId(addressBusiness)
    } yield {
      RequestDetails(Some(NoIdOrganisation(organisationName)), None, address,
        ContactDetails(userAnswers.get(ContactTelephoneNumberPage), None, None, userAnswers.get(ContactEmailAddressPage)), None)
    }

  private def getBusinessName(userAnswers: UserAnswers): Option[String] = {
    userAnswers.get(BusinessTypePage) match {
      case Some(BusinessType.NotSpecified) => {
        userAnswers.get(SoleTraderNamePage).map { name => s"${name.firstName} ${name.secondName}"}
      }
      case _ =>  userAnswers.get(BusinessWithoutIDNamePage)
    }
  }
}

object IndRegistration {
  def apply(userAnswers: UserAnswers): Option[RequestDetails] = for {
    name <- userAnswers.get(NonUkNamePage)
    dob <- userAnswers.get(DateOfBirthPage)
    addressInd <- getAddress(userAnswers)
    address <- AddressNoId(addressInd)
  } yield {
    RequestDetails(None, Some(Individual(name, dob)), address,
      ContactDetails(userAnswers.get(ContactTelephoneNumberPage), None, None, userAnswers.get(ContactEmailAddressPage)), None)
  }

  private def getAddress(userAnswers: UserAnswers): Option[Address] = {
    (userAnswers.get(DoYouLiveInTheUKPage), userAnswers.get(WhatIsYourAddressUkPage).isDefined) match {
      case (Some(true), true) => userAnswers.get(WhatIsYourAddressUkPage)
      case (Some(true), false) => toAddress(userAnswers)
      case (Some(false), false) => userAnswers.get(WhatIsYourAddressPage)
      case _ => throw new Exception("Cannot get address")
    }
  }

  private def toAddress(userAnswers: UserAnswers) =
    userAnswers.get(SelectedAddressLookupPage) map { lookUp =>
      Address(lookUp.addressLine1.getOrElse(""),
        lookUp.addressLine2.getOrElse(""),
        lookUp.addressLine3,
        lookUp.addressLine4,
        Some(lookUp.postcode),
        Country("valid", "UK", "United Kingdom"))
    }
}

case class RegisterWithoutIDRequest(requestCommon: RequestCommon, requestDetail: RequestDetails)

object RegisterWithoutIDRequest{
  implicit val format = Json.format[RegisterWithoutIDRequest]
}

case class Register(
                         registerWithoutIDRequest: RegisterWithoutIDRequest
                       )

object Register {
  implicit val format = Json.format[Register]
}