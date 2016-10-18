package com.springml.spark.netsuite.model

/**
  * Created by sam on 20/9/16.
  */
class NetSuiteInput(
              val username : String,
              val password : String,
              val wssEndpoint : String,
              var request : String
              ) {

}
