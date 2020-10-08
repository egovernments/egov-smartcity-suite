FROM egovio/alpine-maven-builder-jdk-8:gcp AS build

ARG WORK_DIR
ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD
ARG CI_Database
ARG CI_DBNAME
ARG CI_DB_USER
ARG CI_DB_PWD

WORKDIR /app

# copy the project files & do erp build
COPY ${WORK_DIR} ./${WORK_DIR}
RUN cd ${WORK_DIR} \
    && mvn clean deploy -U -s settings.xml -Dbuild.number=${BUILD_NUMBER} \
    -Ddb.url=jdbc:postgresql://${CI_Database}:5432/${CI_DBNAME} -Ddb.password=${CI_DB_PWD} -Ddb.user=${CI_DB_USER} -Ddb.driver=org.postgresql.Driver \
    -Dnexus.user=${NEXUS_USERNAME} -Dnexus.password=${NEXUS_PASSWORD} \
    -Dmaven.javadoc.skip=true