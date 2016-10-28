package com.springml.spark.netsuite.xml

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.mock.MockitoSugar

import scala.xml.XML

/**
  * Created by sam on 28/10/16.
  */
class NetSuiteElementTest extends FunSuite with MockitoSugar with BeforeAndAfterEach {
  private val prefixMsg2016 = "smlm"
  private val prefixCore2016 = "smlc"
  private val namespaceMsg2016 = "urn:messages_2016_1.platform.webservices.netsuite.com"
  private val namespaceCore2016 = "urn:core_2016_1.platform.webservices.netsuite.com"

  private val pageSizeElementName = "pageSize"
  private val bodyFieldsOnlyElementName = "bodyFieldsOnly"
  private val searchPrefElementName = "searchPreferences"
  private val applicationIdElementName = "applicationId"
  private val applicationInfoElementName = "applicationInfo"
  private val emailElementName = "email"
  private val passwordElementName = "password"
  private val accountElementName = "account"
  private val roleElementName = "role"
  private val passportElementName = "passport"
  private val internalIdAttrName = "internalId"

  test ("Test Element Construction") {
    val email = "test@springml.com"
    val emailElem = NetSuiteElement(prefixCore2016, emailElementName,
      namespaceCore2016, null, email, null)

    val emailXML = XML.loadString(emailElem.toString)
    assert(email.equals(emailXML.text))
  }

  test ("Test Element With Attribute") {
    val internalId = "3"
    val email = "test@springml.com"

    val internalIdAttr = NetSuiteAttribute(internalIdAttrName, internalId)
    val emailElem = NetSuiteElement(prefixCore2016, emailElementName,
      namespaceCore2016, List(internalIdAttr), email, null)

    val emailXML = XML.loadString(emailElem.toString)
    assert(email.equals(emailXML.text))
    assert(internalId.equals((emailXML \ "@internalId").text))
  }

  test ("Test Complex Element") {
    val applicationId = "33333-444344-434343-43434"
    val applicationIdElem = NetSuiteElement(prefixMsg2016, applicationIdElementName,
      namespaceMsg2016, null, applicationId, null)
    val applicationInfoElem = NetSuiteElement(prefixMsg2016, applicationInfoElementName, namespaceMsg2016,
      null, null, List(applicationIdElem))

    val applicationInfoXML = XML.loadString(applicationInfoElem.toString)
    assert(applicationId.equals((applicationInfoXML \ applicationIdElementName).text))
  }

  test ("Test Element namespace") {
    val applicationId = "33333-444344-434343-43434"
    val applicationIdElem = NetSuiteElement(prefixMsg2016, applicationIdElementName,
      namespaceMsg2016, null, applicationId, null)
    val applicationInfoElem = NetSuiteElement(prefixMsg2016, applicationInfoElementName, namespaceMsg2016,
      null, null, List(applicationIdElem))

    val namespaceDec = "xmlns:" + prefixMsg2016 + "=\"" + namespaceMsg2016 + "\""
    assert(applicationInfoElem.toString.contains(namespaceDec))
  }

}
