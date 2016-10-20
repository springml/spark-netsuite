package com.springml.spark.netsuite

import com.springml.spark.netsuite.model.{NetSuiteInput, XPathInput}
import com.springml.spark.netsuite.util.CSVUtil
import org.apache.log4j.Logger
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

import scala.collection.mutable

/**
  * Created by sam on 20/9/16.
  */
class DefaultSource extends RelationProvider with SchemaRelationProvider with CreatableRelationProvider {
  @transient val logger = Logger.getLogger(classOf[DefaultSource])

  override def createRelation(sqlContext: SQLContext,
                              parameters: Map[String, String]): BaseRelation = {
    createRelation(sqlContext, parameters, null)
  }

  override def createRelation(sqlContext: SQLContext,
                              parameters: Map[String, String],
                              schema: StructType): BaseRelation = {
    val username = param(parameters, "email")
    val password = param(parameters, "password")
    val account = param(parameters, "account")
    val role = param(parameters, "role")
    val applicationId = param(parameters, "applicationId")
    val request = param(parameters, "request")
    val recordTagPath = param(parameters, "recordTagPath")
    val xpath = param(parameters, "xpathMap")
    val namespacePrefix = parameters.get("namespacePrefixMap")
    val pageSize = parameters.get("pageSize")

    val wwsInput = new NetSuiteInput(username, password, account, role, applicationId, request, getPageSize(pageSize))
    val xPathInput = new XPathInput(recordTagPath)
    CSVUtil.populateXPathInput(xpath, xPathInput)
    xPathInput.namespaceMap = CSVUtil.readCSV(namespacePrefix.get)
    logger.debug("Namespace Map" + xPathInput.namespaceMap)

    //val records = new NetSuiteReader(wwsInput, xPathInput) read()
    var records : List[mutable.Map[String, String]] = null
    var sparkSqlContext : SQLContext = null
    var userSchema : StructType = null
    new DatasetRelation(records, sqlContext, schema)
  }

  override def createRelation(sqlContext: SQLContext,
                              mode: SaveMode,
                              parameters: Map[String, String],
                              data: DataFrame): BaseRelation = {
    logger.error("Save not supported by netsuite connector")
    throw new UnsupportedOperationException
  }

  private def param(parameters: Map[String, String],
                    paramName: String) : String = {
    val paramValue = parameters.getOrElse(paramName,
      sys.error(s"""'$paramName' must be specified for Spark Workday package"""))

    if ("password".equals(paramName)) {
      logger.debug("Param " + paramName + " value " + paramValue)
    }

    paramValue
  }

  private def getPageSize(pageSizeOption : Option[String]) : Integer = {
    var pageSize = 100
    if (pageSizeOption != null && pageSizeOption.isDefined) {
      pageSize = pageSizeOption.get.toInt
    }

    pageSize
  }
}
