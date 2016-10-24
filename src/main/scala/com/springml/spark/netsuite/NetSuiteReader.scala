package com.springml.spark.netsuite

import com.springml.spark.netsuite.model.{NetSuiteInput, XPathInput}
import com.springml.spark.netsuite.util.{XPathHelper, XercesWarningFilter}
import com.springml.spark.netsuite.ws.NetSuiteClient
import org.apache.log4j.Logger

import scala.collection.mutable

/**
  * Created by sam on 20/10/16.
  */
class NetSuiteReader(
                    netSuiteInput: NetSuiteInput,
                    xPathInput: XPathInput
                    ) {

  @transient val logger = Logger.getLogger(classOf[NetSuiteReader])
  val xPathHelper = new XPathHelper(xPathInput.namespaceMap, null)

  def read() : List[mutable.Map[String, String]] = {
    XercesWarningFilter.start()
    var records :List[mutable.Map[String, String]] = List.empty

    var response : String = ""
    do {
      response = new NetSuiteClient(netSuiteInput) search()
      logger.debug("Response from NetSuite " + response)

      if (response != null && !response.isEmpty) {
        val xmlRecords = xPathHelper.evaluate(xPathInput.recordTag, response)

        if (!xmlRecords.isEmpty) {
          records ++= read(xmlRecords)
        }
      }
    }
    while (false)

    records
  }

  private def read(xmlRecords : List[String]) : List[scala.collection.mutable.Map[String, String]] = {
    xmlRecords.map(row => read(row))
  }

  private def read(row: String): scala.collection.mutable.Map[String, String] = {
    logger.debug("Row : " + row)
    val record = scala.collection.mutable.Map[String, String]()
    for ((column, xpath) <- xPathInput.xpathMap) {
      val result = xPathHelper.evaluateToString(xpath, row)
      logger.debug("Xpath evaluation response for xpath " + xpath + " \n" + result)
      record(column) = result
    }

    record
  }

}
