package com.springml.spark.netsuite

import com.springml.spark.netsuite.model.{NetSuiteInput, XPathInput}
import com.springml.spark.netsuite.util.CSVUtil
import org.apache.log4j.Logger
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

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
    val role = parameters.getOrElse("role", "3")
    val applicationId = param(parameters, "applicationId")
    val request = param(parameters, "request")
    val recordTagPath = parameters.getOrElse("recordTagPath", "//platformCore:record")
    val xpath = param(parameters, "xpathMap")
    val namespacePrefix = parameters.get("namespacePrefixMap")
    val pageSizeParam = parameters.getOrElse("pageSize", "100")
    val pageSize = pageSizeParam.toInt

    if (pageSize > 1000) {
      sys.error("Invalid pageSize option. Maximum supported pageSize is 1000")
    }

    val netSuiteInput = new NetSuiteInput(username, password, account, role, applicationId, request, pageSize)
    val xPathInput = new XPathInput(recordTagPath)
    xPathInput.xpathMap = CSVUtil.readCSV(xpath)
    xPathInput.namespaceMap = CSVUtil.readCSV(namespacePrefix.get)
    logger.debug("Namespace Map" + xPathInput.namespaceMap)

    val records = new NetSuiteReader(netSuiteInput, xPathInput) read()
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
      sys.error(s"""'$paramName' must be specified for Spark NetSuite package"""))

    if ("password".equals(paramName)) {
      logger.debug("Param " + paramName + " value " + paramValue)
    }

    paramValue
  }

}
