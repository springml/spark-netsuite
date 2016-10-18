package com.springml.spark.netsuite.model

/**
  * Created by sam on 27/9/16.
  */
class XPathInput(
                val objectTag : String,
                val detailTag : String
                ) {

  var headersMap : Map[String, String] = Map.empty
  var detailsMap : Map[String, String] = Map.empty
  var namespaceMap : Map[String, String] = Map.empty
}
