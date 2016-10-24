package com.springml.spark.netsuite.ws

import java.io.{StringReader, StringWriter}
import javax.xml.transform.stream.{StreamResult, StreamSource}

import com.springml.spark.netsuite.model.NetSuiteInput
import org.apache.log4j.Logger
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory

/**
  * Created by sam on 20/9/16.
  */
class NetSuiteClient(
                    val netSuiteInput: NetSuiteInput
                     ) {

  @transient val logger = Logger.getLogger(classOf[NetSuiteClient])

  private val netSuiteEndpoint = "https://webservices.netsuite.com/services/NetSuitePort_2016_1"
//  private val netSuiteEndpoint = "http://localhost:3031/netsuite/service"
  private val webServiceTemplate = createWebServiceTemplate

  def search() : String = {
    execute(soapHeaderHandler("search"))
  }

  def searchMoreWithId() : String = {
    execute(soapHeaderHandler("searchMoreWithId"))
  }

  private def execute(soapHeaderHandler: SoapHeaderHandler) : String = {
    logger.info("Request : " + netSuiteInput.request)
    val source = new StreamSource(new StringReader(netSuiteInput.request))
    val writer = new StringWriter
    val streamResult = new StreamResult(writer)
    webServiceTemplate.sendSourceAndReceiveToResult(source, soapHeaderHandler, streamResult)

    val response = writer.toString
    logger.info("Response : " + response)

    return response
  }

  private def createWebServiceTemplate : WebServiceTemplate = {
    val wsTemplate = new WebServiceTemplate(new AxiomSoapMessageFactory)
    wsTemplate.setDefaultUri(netSuiteEndpoint)

    wsTemplate
  }

  private def soapHeaderHandler(soapHeader : String) : SoapHeaderHandler = {
    new SoapHeaderHandler(netSuiteInput, soapHeader)
  }
}
