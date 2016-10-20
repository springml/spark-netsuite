package com.springml.spark.netsuite.ws

import javax.xml.namespace.QName

import com.springml.spark.netsuite.model.NetSuiteInput
import org.springframework.ws.WebServiceMessage
import org.springframework.ws.client.core.WebServiceMessageCallback
import org.springframework.ws.soap.SoapMessage

/**
  * Created by sam on 20/10/16.
  */
class SoapHeaderHandler(val netSuiteInput: NetSuiteInput) extends WebServiceMessageCallback {
  override def doWithMessage(message: WebServiceMessage): Unit = {
    val soapMessage = message.asInstanceOf[SoapMessage]

    val soapHeader = soapMessage.getSoapHeader
    val searchPrefElement = soapHeader.addHeaderElement(
        new QName("urn:messages_2016_1.platform.webservices.netsuite.com", "searchPreferences"))
//    searchPrefElement.
  }
}
