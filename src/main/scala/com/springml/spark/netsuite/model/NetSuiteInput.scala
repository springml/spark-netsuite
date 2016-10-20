package com.springml.spark.netsuite.model

/**
  * Created by sam on 20/9/16.
  */
class NetSuiteInput(
              val email : String,
              val password : String,
              val account : String,
              val role : String,
              val applicationId : String,
              val request : String,
              val pageSize : Integer
              ) {

}
