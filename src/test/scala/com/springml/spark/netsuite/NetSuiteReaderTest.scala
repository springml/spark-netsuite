package com.springml.spark.netsuite

import com.springml.spark.netsuite.model.XPathInput
import com.springml.spark.netsuite.util.CSVUtil
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FunSuite}

import scala.collection.mutable

/**
  * Created by sam on 26/10/16.
  */
class NetSuiteReaderTest extends FunSuite with MockitoSugar with BeforeAndAfterEach {

  test ("Test Fetch Search Id") {
    val reader = new NetSuiteReader(netSuiteInput = null, xPathInput = xpathInput)
    val xmlContent = getResourceContent("/Search_Customers_Sample_Response.xml")

    val searchId = reader.getSearchId(xmlContent)
    val expectedSearchId = "WEBSERVICES_TSTDRV1555103_10232016178664587641298967_840eef2"

    assert(expectedSearchId.equals(searchId))
  }

  test ("Test More to Read") {
    val reader = new NetSuiteReader(netSuiteInput = null, xPathInput = xpathInput)
    val xmlContent = getResourceContent("/Search_Customers_Sample_Response.xml")

    assert(reader.moreToRead(xmlContent))
  }

  test ("Test More to Read to return false") {
    val reader = new NetSuiteReader(netSuiteInput = null, xPathInput = xpathInput)
    val xmlContent = getResourceContent("/Search_Customers_Sample_Response.xml")

    reader.currentPage = 108
    assert(!reader.moreToRead(xmlContent))
  }

  test ("Test Read records") {
    val reader = new NetSuiteReader(netSuiteInput = null, xPathInput = xpathInput)
    val xmlContent = getResourceContent("/Search_Customers_Sample_Response.xml")

    val records = reader.readRecords(xmlContent)

    // 10 records are present in Search_Customers_Sample_Response
    assert(records.length == 10)
    val firstRow = records(0)
    verifyRecord(firstRow)
  }

  test ("Test Read records with few invalid XPath") {
    val modXPathInput: XPathInput = xpathInput
    // Adding invalid xpaths to existing
    modXPathInput.xpathMap += ("Invalid_Id" -> "//invalid_path", "Invalid_Status" -> "//another_invalid_path")

    val reader = new NetSuiteReader(netSuiteInput = null, xPathInput = modXPathInput)
    val xmlContent = getResourceContent("/Search_Customers_Sample_Response.xml")

    val records = reader.readRecords(xmlContent)

    // 10 records are present in Search_Customers_Sample_Response
    assert(records.length == 10)
    val firstRow = records(0)
    verifyRecord(firstRow)

    // No values should be present for invalid xpath
//    assert("".equals(firstRow("Invalid_Id")))
//    assert("".equals(firstRow("Invalid_Status")))
    intercept[NoSuchElementException] {
      firstRow("Invalid_Id")
    }
    intercept[NoSuchElementException] {
      firstRow("Invalid_Status")
    }

  }

  test ("Test Read records with invalid XPath for all elements") {
    val modXPathInput: XPathInput = xpathInput
    modXPathInput.xpathMap = Map("Id" -> "//invalid_path", "Status" -> "//another_invalid_path")

    val reader = new NetSuiteReader(netSuiteInput = null, xPathInput = modXPathInput)
    val xmlContent = getResourceContent("/Search_Customers_Sample_Response.xml")

    val records = reader.readRecords(xmlContent)

    // No records should be created as none of the XPaths are valid
    assert(records.length == 0)
  }

  private def getResourceContent(path: String): String = {
    val resStream = getClass().getResourceAsStream(path)
    scala.io.Source.fromInputStream(resStream).mkString
  }

  private def xpathInput: XPathInput = {
    val recordTagPath = "//platformCore:record"
    val xPathInput = new XPathInput(recordTagPath)

    val xpathFileLocation = getClass.getResource("/xpath.csv").getPath
    xPathInput.xpathMap = CSVUtil.readCSV(xpathFileLocation)

    val namespacesFileLocation = getClass.getResource("/namespaces.csv").getPath
    xPathInput.namespaceMap = CSVUtil.readCSV(namespacesFileLocation)

    xPathInput
  }

  private def verifyRecord(record: mutable.Map[String, String]) {
    val expectedId = "-5"
    assert(expectedId.equals(record("Id")))

    val expectedEntity = "Alex Wolfe"
    assert(expectedEntity.equals(record("Entity")))

    val expectedEmail = "user@springml.com"
    assert(expectedEmail.equals(record("Email")))

    val expectedBalance = "-32.3"
    assert(expectedBalance.equals(record("Balance")))

    val expectedIsPerson = "true"
    assert(expectedIsPerson.equals(record("IsPerson")))

    val expectedStatus = "CUSTOMER-Closed Won"
    assert(expectedStatus.equals(record("Status")))
  }

}
