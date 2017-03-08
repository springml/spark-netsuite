# Spark NetSuite Library

Spark Connector for NetSuite is a SOAP web service wrapper around the NetSuite web service published [here](https://webservices.netsuite.com/wsdl/v2016_1_0/netsuite.wsdl).  

## Requirements

This library requires Spark 2.x+

For Spark 1.x support, please check [spark1.x](https://github.com/springml/spark-netsuite/tree/spark1.x) branch.


## Linking
You can link against this library in your program at the following ways:

### Maven Dependency
```
<dependency>
    <groupId>com.springml</groupId>
    <artifactId>spark-netsuite_2.11</artifactId>
    <version>1.1.0</version>
</dependency>
```

### SBT Dependency
```
libraryDependencies += "com.springml" % "spark-netsuite_2.11" % "1.1.0"
```

## Using with Spark shell
This package can be added to Spark using the `--packages` command line option.  For example, to include it when starting the spark shell:

```
$ bin/spark-shell --packages com.springml:spark-netsuite_2.11:1.1.0
```

## Feature
* **Construct Spark Dataframe using NetSuite data** - User has to provide NetSuite web service request and list of XPath to read data from NetSuite. The XPath will be evaluated against NetSuite web service response and dataframe will be constructed based on that. Records will be searched based on the user provided request and further records will be fetched using searchMoreWithId

### Options
* `email`: NetSuite account user Id
* `password`: NetSuite account passsword
* `account`: NetSuite account Id
* `applicationId`: NetSuite application Id
* `role`: (Optional) NetSuite Role Id. Default value is `3`
* `pageSize`: (Optional) Number of records to pulled in a single request. Max pageSze is 1000. Default value is `100`.
* `request`: NetSuite Web Service search request. This request will be used to search for records from NetSuite. Sample request is present over [here](https://raw.githubusercontent.com/springml/spark-netsuite/master/src/test/resources/Search_Customers_Sample_Request.xml)
* `recordTagPath`: (Optional) XPath of the response element which should be considered as record. Default value is `//platformCore:record`
* `xpathMap`: Location of CSV file which should contain fieldName and its XPath. Sample file is present over [here](https://raw.githubusercontent.com/springml/spark-netsuite/master/src/test/resources/xpath.csv)
* `namespacePrefixMap`: Location of CSV file which should contain prefix and its corresponding namespace. Sample file is present over [here](https://raw.githubusercontent.com/springml/spark-netsuite/master/src/test/resources/namespaces.csv)
* `schema`: (Optional) Schema to be used for constructing dataframes. If not provided all fields will be of type String

### Scala API
```scala
import org.apache.spark.sql.SQLContext

// Construct Dataframe from NetSuite records
// Search request to be executed against NetSuite Web Service
// Here Customers are fetched
val request = """
<search xmlns="urn:messages_2016_1.platform.webservices.netsuite.com" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <searchRecord xmlns:ns7="urn:relationships_2016_1.lists.webservices.netsuite.com" xsi:type="ns7:CustomerSearch">
        <ns7:basic xmlns:ns8="urn:common_2016_1.platform.webservices.netsuite.com" xsi:type="ns8:CustomerSearchBasic"></ns7:basic>
    </searchRecord>
</search>"""

// Below constructs dataframe by executing search and searchMoreWithId operations 
val df = spark.read.
    format("com.springml.spark.netsuite").
    option("email", "netsuite_email").
    option("password", "netsuite_password").
    option("account", "netsuite_account").
    option("applicationId", "netsuite_application_id").
    option("request", request).
    option("xpathMap","/home/xpath.csv").
    option("namespacePrefixMap","/home/namespaces.csv").
    load() 

```


### R API
```r
# Search request to be executed against NetSuite Web Service
# Here Customers are fetched
netsuite_request <- "<search xmlns=\"urn:messages_2016_1.platform.webservices.netsuite.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><searchRecord xmlns:ns7=\"urn:relationships_2016_1.lists.webservices.netsuite.com\" xsi:type=\"ns7:CustomerSearch\"><ns7:basic xmlns:ns8=\"urn:common_2016_1.platform.webservices.netsuite.com\" xsi:type=\"ns8:CustomerSearchBasic\"></ns7:basic></searchRecord></search>"

// Below constructs dataframe by executing search and searchMoreWithId operations 
df <- read.df(source="com.springml.spark.netsuite",
      email="netsuite_email",
      password="netsuite_password",
      account="netsuite_account",
      applicationId="netsuite_application_id",
      role="netsuite_user_role",
      pageSize="page_size",
      request=netsuite_request,
      recordTagPath="//platformCore:record",
      xpathMap="/home/xpath.csv",
      namespacePrefixMap="/home/namespace.csv")

```


## Building From Source
This library is built with [SBT](http://www.scala-sbt.org/0.13/docs/Command-Line-Reference.html), which is automatically downloaded by the included shell script. To build a JAR file simply run `sbt/sbt package` from the project root.
