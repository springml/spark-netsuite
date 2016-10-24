package com.springml.spark.netsuite.ws

/**
  * Created by sam on 21/10/16.
  */
case class NetSuiteAttribute(
                            localName : String,
                            value : String
                            ) {

  override def toString: String = {
    val sb = new StringBuilder()
    sb.append(localName)
    sb.append("=")
    if (value != null) {
      sb.append("\"").append(value).append("\"")
    }

    sb.toString()
  }

}
