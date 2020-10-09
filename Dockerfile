FROM egovio/alpine-maven-builder-jdk-8:gcp AS build

ARG WORK_DIR
ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD
ARG CI_DB_USER
ARG CI_DB_PWD
ENV BUILD_NUMBER=${BUILD_NUMBER}

WORKDIR /app

# copy the project files & do erp build
COPY ${WORK_DIR} ./${WORK_DIR}

RUN cd ${WORK_DIR} \
    && mvn clean deploy -U -s settings.xml -Dbuild.number=${BUILD_NUMBER} \
    -Ddb.url=jdbc:postgresql://postgres.jenkins:5432/ci_database -Ddb.user=${CI_DB_USER} -Ddb.password=${CI_DB_PWD} -Ddb.driver=org.postgresql.Driver \
    -Dnexus.user=${NEXUS_USERNAME} -Dnexus.password=${NEXUS_PASSWORD} \
    -Dmaven.javadoc.skip=true