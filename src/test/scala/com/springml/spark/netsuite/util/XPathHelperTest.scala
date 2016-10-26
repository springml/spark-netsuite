package com.springml.spark.netsuite.util

import com.springml.spark.netsuite.model.XPathInput
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.mock.MockitoSugar

/**
  * Created by sam on 26/10/16.
  */
class XPathHelperTest extends FunSuite with MockitoSugar with BeforeAndAfterEach {
  test ("Test evaluate") {
    val recordTag = "//platformCore:record"
    val namespaceFile = getClass.getResource("/namespaces.csv").getPath
    val helper = new XPathHelper(CSVUtil.readCSV(namespaceFile), null)

    val responseXMLStream = getClass().getResourceAsStream("/Search_Customers_Sample_Response.xml")
    val xmlContent = scala.io.Source.fromInputStream(responseXMLStream).mkString
    val evaluateResult = helper.evaluate(recordTag, xmlContent)
    assert(evaluateResult != null)
    assert(evaluateResult.size == 10)
  }

  test ("Test Invalid ObjectTag") {
    val invalidRecordTag = "invalid_tag"
    val namespaceFile = getClass.getResource("/namespaces.csv").getPath
    val helper = new XPathHelper(CSVUtil.readCSV(namespaceFile), null)

    val responseXMLStream = getClass().getResourceAsStream("/Search_Customers_Sample_Response.xml")
    val xmlContent = scala.io.Source.fromInputStream(responseXMLStream).mkString
    val evaluateResult = helper.evaluate(invalidRecordTag, xmlContent)
    assert(evaluateResult != null)
    assert(evaluateResult.size == 0)
  }

  test ("Test evaluate as String") {
    val recordTag = "//platformCore:record"
    val namespaceFile = getClass.getResource("/namespaces.csv").getPath
    val helper = new XPathHelper(CSVUtil.readCSV(namespaceFile), null)

    val responseXMLStream = getClass().getResourceAsStream("/Search_Customers_Sample_Response.xml")
    val xmlContent = scala.io.Source.fromInputStream(responseXMLStream).mkString
    val evaluateResult = helper.evaluate(recordTag, xmlContent)
    assert(evaluateResult != null)
    assert(evaluateResult.size == 10)

    val idXPath = "/platformCore:record/listRel:entityId/text()"
    val id = helper.evaluateToString(idXPath, evaluateResult(0))
    assert(id != null)
    assert(id.equals("Alex Wolfe"))
  }

  test ("Test evaluate attribute") {
    val recordTag = "//platformCore:record"
    val xpathInput = new XPathInput(recordTag)

    val namespaceFile = getClass.getResource("/namespaces.csv").getPath
    val helper = new XPathHelper(CSVUtil.readCSV(namespaceFile), null)

    val responseXMLStream = getClass().getResourceAsStream("/Search_Customers_Sample_Response.xml")
    val xmlContent = scala.io.Source.fromInputStream(responseXMLStream).mkString
    val evaluateResult = helper.evaluate(recordTag, xmlContent)
    assert(evaluateResult != null)
    assert(evaluateResult.size == 10)

    val internalIdXPath = "string(/platformCore:record/@internalId)"
    val internalId = helper.evaluateToString(internalIdXPath, evaluateResult(0))
    assert(internalId != null)
    println("internalId : " + internalId)
    assert(internalId.equals("-5"))
  }
}
