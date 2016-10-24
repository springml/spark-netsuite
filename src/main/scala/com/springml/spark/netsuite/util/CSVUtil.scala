package com.springml.spark.netsuite.util

import com.springml.spark.netsuite.model.XPathInput
import org.apache.log4j.Logger

/**
  * Created by sam on 20/9/16.
  */
object CSVUtil {
  @transient val logger = Logger.getLogger(this.getClass.getName)

  def readCSV(csvLocation : String) : Map[String, String] = {
    var resultMap : Map[String, String] = Map.empty

    if (csvLocation != null) {
      val bufferedSource = scala.io.Source.fromFile(csvLocation)

      for (line <- bufferedSource.getLines) {
        if (!line.startsWith("#")) {
          val cols = line.split(",").map(_.trim)
          if (cols.length != 2) {
            throw new Exception("Invalid Row : " + line + "\n Please make sure rows are specified as 'Prefix','namespace' in " + csvLocation)
          }

          resultMap += cols(0) -> cols(1)
        } else {
          logger.info("Ignoring commented line " + line)
        }
      }
      bufferedSource.close()
    }

    resultMap
  }

}
