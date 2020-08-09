# cerebro
Cerebro is Search As a Service which anyone can use out of the box to enable free text search on their website/project/application.

Cerebro allows anyone who wants to have search functionality enabled but does not want to invest time in knowing how it works, which technology to use, or go in the inner details. It is very similar to Algolia wherein you just give it some data, and it indexes it automatically and is ready to use.

### Requirements
* Java 8
* Gradle
* ElasticSearch 7.6.0

A Dynamo DB table `index_settings` with `index_name` being the hash key

### ENV Variables
* AWS_ACCESS_KEY
* AWS_SECRET_KEY
* AWS_REGION
* ELASTIC_SEARCH_HOST
* ELASTIC_SEARCH_PORT
* ELASTIC_SEARCH_SCHEME

AWS Keys should have access to table `index_settings`

### Running
./gradlew bootRun


### Usage
Refer to [WIKI](https://github.com/sidhant92/cerebro/wiki)
