package com.springml.spark.netsuite.ws

/**
  * Created by sam on 21/10/16.
  */
case class NetSuiteElement(
                            prefix : String,
                            localName : String,
                            namespace : String,
                            attributes: List[NetSuiteAttribute],
                            value : String,
                            childs : List[NetSuiteElement]
                          ) {

  override def toString: String = {
    val sb = new StringBuilder()
    sb.append("<")
    if (prefix != null) {
      sb.append(prefix)
      sb.append(":")
    }

    sb.append(localName)
    sb.append(" ")
    if (namespace != null) {
      sb.append("xmlns:").append(prefix).append("=")
      sb.append("\"").append(namespace).append("\"")
    }

    if (attributes != null) {
      sb.append(" ")
      sb.append(attributes.mkString(" "))
    }

    sb.append(">")
    if (childs != null && childs.size > 0) {
      val childXml = childs.mkString("")
      if (childXml != null) {
        sb.append(childXml)
      }
    }

    if (value != null) {
      sb.append(value)
    }

    sb.append("</")
    if (prefix != null) {
      sb.append(prefix)
      sb.append(":")
    }

    sb.append(localName)
    sb.append(">")

    sb.toString()
  }

}
