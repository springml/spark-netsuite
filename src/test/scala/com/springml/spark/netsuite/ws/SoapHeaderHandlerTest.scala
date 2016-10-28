package com.springml.spark.netsuite.ws

import com.springml.spark.netsuite.model.NetSuiteInput
import org.apache.axiom.om.OMElement
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.springframework.ws.WebServiceMessage
import org.springframework.ws.soap.SoapMessage
import org.springframework.ws.soap.axiom.{AxiomSoapMessage, AxiomSoapMessageFactory}

/**
  * Created by sam on 28/10/16.
  */
class SoapHeaderHandlerTest extends FunSuite with MockitoSugar with BeforeAndAfterEach {
  test ("Test SoapHeaderHandler") {
    val webServiceMessage = new AxiomSoapMessageFactory().createWebServiceMessage()

    val netSuiteInput = new NetSuiteInput("test@springml.com", "password", "account", "3",
      "application-id", "dummy-request", 10)
    val soapHeaderHandler = new SoapHeaderHandler(netSuiteInput, "search")

    soapHeaderHandler.doWithMessage(webServiceMessage)

    verifyHeaders(webServiceMessage)
    val soapAction = webServiceMessage.asInstanceOf[SoapMessage].getSoapAction
    val expectedSoapAction : java.lang.String = "\"search\""
    assert(soapAction.equals(expectedSoapAction))
  }

  private def verifyHeaders(message: WebServiceMessage): Unit = {
    val soapMessage = message.asInstanceOf[AxiomSoapMessage]
    val header = soapMessage.getAxiomMessage.getSOAPEnvelope.getHeader
    val headerElements = header.getChildElements
    val headerNameList = List("searchPreferences", "applicationInfo", "passport")
    var count = 0
    while(headerElements.hasNext) {
      val headerElement : OMElement = headerElements.next().asInstanceOf[OMElement]
      if (headerNameList.contains(headerElement.getLocalName)) {
        count += 1
      }
    }

    assert(count == headerNameList.size)
  }

//  private def createAxiomSoapMessage(): Unit = {
//    val webServiceMessage = new AxiomSoapMessageFactory().createWebServiceMessage()
//  }

//  private def createSoapMessage() : SOAPMessage = {
//    val soapFactory = OMAbstractFactory.getSOAP12Factory
//    MessageFactory mf = MessageFactory.newInstance();
//    val soapBody = soapFactory.createSOAPBody()
//    soapBody.addChild()
//    val factory = MessageFactory.newInstance()
//    val soapMessage = factory.createMessage()
//    val soapPart = soapMessage.getSOAPPart
//    val soapEnvelope = soapPart.getEnvelope
//    val soapBody = soapEnvelope.getBody
//
//    val searchElement = soapBody.addBodyElement(soapEnvelope.createName("search", "",
//      "urn:messages_2016_1.platform.webservices.netsuite.com"))
//    searchElement.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance")
//
//    val searchRecord = searchElement.addChildElement("searchRecord")
//    searchRecord.addNamespaceDeclaration("ns7", "urn:relationships_2016_1.lists.webservices.netsuite.com")
//    val typeQname = new QName("http://www.w3.org/2001/XMLSchema-instance", "type")
//    searchRecord.addAttribute(typeQname, "ns7:CustomerSearch")
//
//    soapMessage
//  }
}
