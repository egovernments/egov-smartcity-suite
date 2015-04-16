# eGov Opensource
[![Build Status](http://ci.egovernments.org/job/eGov-Github-Master/badge/icon)](http://ci.egovernments.org/job/eGov-Github-Master/)
## Repository Structure

* `egov` - folder contains all the source code of eGov opensource projects

## Prerequisites

* Install [maven >= v3.2.x](http://maven.apache.org/download.cgi)
* Install your favorite IDE for java project. Recommended Eclipse or IntelliJ
* Install [PostgreSQL >= v9.3 ](http://www.postgresql.org/download/)
* Install [Elastic Search >= v1.4.2](https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.4.2.zip)
* Install [Jboss Wildfly v8.2.x](http://182.74.137.193/downloads/DevTools/phoenix/wildfly-8.2.0.Final-v1.zip)

__Note__: Please check in [downlods repository](http://182.74.137.193/downloads/) for any of the above software installables before downloading from internet.

## Building Source

* Change your directory on command prompt to `<CLONED_REPO_DIR>/egov`
* Run the following commands

```bash
mvn -s settings.xml clean compile ## Cleans your build directory and compiles your java code
mvn -s settings.xml clean test    ## Cleans, compiles and runs unit, integration tests
mvn -s settings.xml package       ## Cleans, compiles, tests and generates ear artifact along with jars and wars approproiately
```
#### Building Database

Database migration and artifact creation is part of maven package. This will be enabled only when maven profile 'db' is provided.

```bash
mvn -s settings.xml package -Pdb  ## Cleans, compiles, tests, migrates database and generates ear artifact along with jars and wars approproiately
```

To skip migration use -Dliquibase.should.run=false

#### Database sql files

* All sql files should be added under directory `<CLONED_REPO_DIR>/egov/egov-database/src/main/resources/sql`
* Uses the database properties from `<CLONED_REPO_DIR>/egov/egov-database/src/main/resources/liquibase.properties` for migration
* All sql scripts should be named with incremental number prefix and .sql suffix
* Format `<sequence>_<module>_<description>_<database-statement-type>.sql`

##### Examples

```
1_egi_create-deparment_DDL.sql
2_eis_add-employee-role_DML.sql
```

For More details refer [liquibase](http://www.liquibase.org/documentation/index.html)

## Deploying

#### 1. Configuring JBoss Wildfly

##### Spring Profiles

Application uses [spring environment profiles](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-environment) to manage production and test related bean definitions. To run your application on wildfly make sure the `production` spring profile is enabled by setting a system property `spring.profiles.active=production`

This can be set in `standalone.conf` by suffixing it to `JAVA_OPTS` as shown below:

```bash
JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=production"
```

##### Application Settings

* Unzip the `<CLONED_REPO_DIR>/deployment/modules/egov-settings.zip` in to `<JBOSS_HOME>/modules/system/layers/base/` directory.
* After the above step you should have the following folders created in your JBOSS module system

```
<JBOSS_HOME>/modules/system/layers/base/

org
└── egov
	└── settings
		└── main
	        ├── config
	        │   └── egov-erp-override.properties
	        └── module.xml
```

###### Settings to Override in your local environment

* __Database__: URL, username and password

```properties
db.url=jdbc:postgresql://localhost:5432/postgres  ## Change the database name to whatever you wanna point to
db.username=erp_owner
db.password=erp_owner
```

* __Search Server__: Clustername

```properties
search.clusterName=elasticsearch-<username> ## Your local elasticsearch clustername, DO NOT use default clustername
```


##### Datasource

* TODO - List out steps to configure JNDI for datasource

#### 2. Manual Standalone Deployment

* Copy the generated exploded ear `<CLONED_REPO_DIR>/egov/egov-ear/target/egov-ear-1.0-SNAPSHOT` in to your JBoss deployment folder `<JBOSS_HOME>/standalone/deployments`
* Rename the copied folder `egov-ear-1.0-SNAPSHOT` to `egov-ear-1.0-SNAPSHOT.ear`
* Create or touch a file named `egov-ear-1.0-SNAPSHOT.ear.dodeploy` to make sure JBoss picks it up for auto deployment
* Monitor the logs and in case of successful deployment, just hit `http://localhost:8080/egi` in your favorite browser

Alternatively you could also run `<CLONED_REPO_DIR>/egov/deploy-local.sh` which deploys the already built ear in to your wildfly as configured.

#### 3. Eclipse Deployment

* Import the cloned git repo using maven Import Existing Project.
* Install Jboss Tools and configure Wildfly Server.
* Since jasperreport related jar's are not available in maven central, we have to tell eclipse to find jar's in alternative place for that navigate to `Windows -> Preference -> Maven -> User Settings -> Browse Global Settings` and point settings.xml available under egov-erp/
* Now add your EAR project into the configured Wildfly server.
* Start Wildfly in debug mode, this will enable hot deployment.

#### 4. Intellij Deployment

* TODO - Contribute
