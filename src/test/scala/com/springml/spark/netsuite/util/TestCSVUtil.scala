package com.springml.spark.netsuite.util

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.mock.MockitoSugar

/**
  * Created by sam on 26/10/16.
  */
class TestCSVUtil extends FunSuite with MockitoSugar with BeforeAndAfterEach {
  test("Successfully reading CSV file") {
    val csvURL= getClass.getResource("/xpath.csv")
    val csvContent = CSVUtil.readCSV(csvURL.getPath)
    assert(csvContent.size == 6)
    assert(csvContent("Id").equals("string(/platformCore:record/@internalId)"))
    assert(csvContent("Entity").equals("/platformCore:record/listRel:entityId/text()"))
    assert(csvContent("Status").equals("/platformCore:record/listRel:entityStatus[@internalId=\"13\"]/platformCore:name/text()"))
  }
}
