package com.springml.spark.netsuite.model

/**
  * Created by sam on 27/9/16.
  */
class XPathInput(
                val recordTag : String
                ) {

  var xpathMap : Map[String, String] = Map.empty
  var namespaceMap : Map[String, String] = Map.empty
}
