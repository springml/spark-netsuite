package com.springml.spark.netsuite.ws

import javax.xml.transform.TransformerFactory

import com.springml.spark.netsuite.model.NetSuiteInput
import org.apache.log4j.Logger
import org.springframework.ws.WebServiceMessage
import org.springframework.ws.client.core.WebServiceMessageCallback
import org.springframework.ws.soap.SoapMessage
import org.springframework.xml.transform.StringSource

/**
  * Created by sam on 20/10/16.
  */
class SoapHeaderHandler(
                         val netSuiteInput: NetSuiteInput,
                         val soapAction : String
                       ) extends WebServiceMessageCallback {
  @transient val logger = Logger.getLogger(classOf[SoapHeaderHandler])

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

  override def doWithMessage(message: WebServiceMessage): Unit = {
    val soapMessage = message.asInstanceOf[SoapMessage]
    soapMessage.setSoapAction(soapAction)

    val soapHeader = soapMessage.getSoapHeader
    val transformer = TransformerFactory.newInstance().newTransformer()
    transformer.transform(new StringSource(searchPreferenceElement), soapHeader.getResult())
    transformer.transform(new StringSource(applicationInfoElement), soapHeader.getResult())
    transformer.transform(new StringSource(passportElement), soapHeader.getResult())
  }

  private def applicationInfoElement() : String = {
    val applicationId = NetSuiteElement(prefixMsg2016, applicationIdElementName,
                                        namespaceMsg2016, null, netSuiteInput.applicationId, null)
    val applicationInfo = NetSuiteElement(prefixMsg2016, applicationInfoElementName, namespaceMsg2016,
                                        null, null, List(applicationId))

    applicationInfo.toString()
  }

  private def passportElement() : String = {
    val emailElem = NetSuiteElement(prefixCore2016, emailElementName,
                          namespaceCore2016, null, netSuiteInput.email, null)
    val passwordElem = NetSuiteElement(prefixCore2016, passwordElementName,
                          namespaceCore2016, null, netSuiteInput.password, null)
    val accountElem = NetSuiteElement(prefixCore2016, accountElementName,
                          namespaceCore2016, null, netSuiteInput.account, null)

    val internalIdAttr = NetSuiteAttribute(internalIdAttrName, netSuiteInput.role)
    val roleElem = NetSuiteElement(prefixCore2016, roleElementName,
                          namespaceCore2016, List(internalIdAttr), null, null)

    val passportElem = NetSuiteElement(prefixMsg2016, passportElementName, namespaceMsg2016,
                          null, null, List(emailElem, passwordElem, accountElem, roleElem))

    passportElem.toString()
  }

  private def searchPreferenceElement() : String = {
    // Getting NullPointerException at
    // at scala.xml.NamespaceBinding.buildString(NamespaceBinding.scala:62) when scala XML used
    // Hence Scala xml is not used
    val pageSizeElement = NetSuiteElement(prefixMsg2016, pageSizeElementName, namespaceMsg2016,
                                          null, netSuiteInput.pageSize.toString, null)
    val bodyFieldsOnlyElement = NetSuiteElement(prefixMsg2016, bodyFieldsOnlyElementName,
                                          namespaceMsg2016, null, "false", null)

    val searchPreElement = NetSuiteElement(prefixMsg2016, searchPrefElementName, namespaceMsg2016,
                                            null, null, List(bodyFieldsOnlyElement, pageSizeElement))
    searchPreElement.toString()
  }
}
