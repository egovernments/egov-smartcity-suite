## Repository Structure

* `source` - folder contains all the source code of erp
* `build` - folder contains database build and related scripts

## Prerequisites

* Install [maven](http://maven.apache.org/download.cgi)
* Install your favorite IDE for java project. Recommended Eclipse or IntelliJ
* Install [PostgreSQL](http://www.postgresql.org/download/) 
* Install [JBoss]()

__Note__: Please check in [downlods repository](http://192.168.1.3/downloads/) for any of the above software installables before downloading from internet.

## Building Source

* Change your directory on command prompt to `<CHECKOUT_DIR>/source/egov-erp`
* Run the following commands 

```bash
mvn clean compile ## Cleans your build directory and compiles your java code
mvn clean test    ## Cleans, compiles and runs unit, integration tests
mvn package       ## Cleans, compiles, tests and generates ear artifact alsong with jars and wars approproiately
```

## Deploying

#### Configuring JBoss

* TODO - List out steps to configure JNDI for datasource

#### Manual Standalone Deployment

* Copy the generated exploded ear `<CHECKOUT_DIR>/source/egov-erp/egov-ear/target/egov-ear-1.0-SNAPSHOT` in to your JBoss deployment folder `<JBOSS_HOME>/standalone/deployments` 
* Rename the copied folder `egov-ear-1.0-SNAPSHOT` to `egov-ear-1.0-SNAPSHOT.ear`
* Create or touch a file named `egov-ear-1.0-SNAPSHOT.ear.dodeploy` to make sure JBoss picks it up for auto deployment
* Monitor the logs and in case of successful deployment, just hit `http://localhost:8080/egi` in your favorite browser

## Notes

This is used for JBOSS 7 Deployment
The Project Code base from http://192.168.1.3/erpbuild/nmc/trunk
/jboss dir contains files that to be copied to JBoss 7 at build process.

##### Change Log

1) Removed all EJB's and used POJO injection using Spring Framework.
2) Jboss AS 7 Community Edition compatible code base [No backward compatibilty].
3) Changed DB from Oracle to Postgresql.
4) Migrated to Struts2 latest.
5) Migrated to Spring3 latest.
6) Migrated to Hibernate4 latest.

Please read migration guide for more details....

 