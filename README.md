# eGov ERP System
[![Build Status](http://192.168.1.58:8080/job/Phoenix-Master-Build/badge/icon)](http://192.168.1.58:8080/job/Phoenix-Master-Build/)
## Repository Structure

* `source` - folder contains all the source code of erp

## Prerequisites

* Install [maven](http://maven.apache.org/download.cgi)
* Install your favorite IDE for java project. Recommended Eclipse or IntelliJ
* Install [PostgreSQL](http://www.postgresql.org/download/)
* Install [Jboss Wildfly](http://download.jboss.org/wildfly/8.2.0.Final/wildfly-8.2.0.Final.zip)

__Note__: Please check in [downlods repository](http://192.168.1.3/downloads/) for any of the above software installables before downloading from internet.

## Building Source

* Change your directory on command prompt to `<CHECKOUT_DIR>/source/egov-erp`
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

* All sql files should be added under directory `<CHECKOUT_DIR>/source/egov-erp/egov-database/src/main/resources/sql`
* Uses the database properties from `<CHECKOUT_DIR>/source/egov-erp/egov-database/src/main/resources/liquibase.properties` for migration
* All sql scripts should be named with incremental number prefix and .sql suffix
* Format `<sequence>_<module>_<description>_<database-statement-type>.sql`

##### Examples

```
1_egi_create-deparment_DDL.sql
2_eis_add-employee-role_DML.sql
```

For More details refer [liquibase](http://www.liquibase.org/documentation/index.html)

## Deploying

#### 1. Configuring JBoss

##### Spring Profiles

Application uses [spring environment profiles](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-environment) to manage production and test related bean definitions. To run your application on wildfly make sure the `production` spring profile is enabled by setting a system property `spring.profiles.active=production`

This can be set in `standalone.conf` by suffixing it to `JAVA_OPTS` as shown below:

```bash
JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=production"
```

##### Datasource

* TODO - List out steps to configure JNDI for datasource

#### 2. Manual Standalone Deployment

* Copy the generated exploded ear `<CHECKOUT_DIR>/source/egov-erp/egov-ear/target/egov-ear-1.0-SNAPSHOT` in to your JBoss deployment folder `<JBOSS_HOME>/standalone/deployments`
* Rename the copied folder `egov-ear-1.0-SNAPSHOT` to `egov-ear-1.0-SNAPSHOT.ear`
* Create or touch a file named `egov-ear-1.0-SNAPSHOT.ear.dodeploy` to make sure JBoss picks it up for auto deployment
* Monitor the logs and in case of successful deployment, just hit `http://localhost:8080/egi` in your favorite browser

Alternatively you could also run `<CHECKOUT_DIR>/source/egov-erp/deploy-local.sh` which deploys the already built ear in to your wildfly as configured.

#### 3. Eclipse Deployment

* Import the cloned git repo using maven Import Existing Project (or use SCM project import).
* Install Jboss Tools and configure Wildfly Server.
* When we wrote this, m2e plugin does not have full support for EAR dependency management for deployment. Please follow the below tweaking tips to manage deployment in eclipse.

    1. Right click on ear project and select `Properties -> Deployment Assembly` and add `target/egov-ear-XYZ` folder from EAR project.
    2. Right click on war project and select `Properties -> Maven -> Java EE Integration`, Select `Enable Project Specific Settings` and deselect all items below it. Repeat the same for all Web Projects.
    3. Right click on war Project and select `Properties -> Deployment Assembly` and remove maven dependency and all other java project dependencies. Repeat the same for other Web Projects.

* Now add your EAR project into the configured Wildfly server.
* Start Wildfly in debug mode, this will enable hot deployment.

#### 4. Intellij Deployment

* TODO - Contribute
