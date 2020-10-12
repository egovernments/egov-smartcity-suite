FROM egovio/alpine-maven-builder-jdk-8:gcp AS build

ARG WORK_DIR
ARG nexusUsername
ARG nexusPassword
ARG ciDbUsername
ARG ciDbpassword
ENV BUILD_NUMBER=${BUILD_NUMBER}

WORKDIR /app

# copy the project files & do erp build
COPY ${WORK_DIR} ./${WORK_DIR}

RUN cd ${WORK_DIR} \
    && mvn clean deploy -U -s settings.xml -Dbuild.number=${BUILD_NUMBER} \
    -Ddb.url=jdbc:postgresql://postgres.jenkins:5432/ci_database -Ddb.user=${ciDbUsername} -Ddb.password=${ciDbpassword} -Ddb.driver=org.postgresql.Driver \
    -Dnexus.user=${nexusUsername} -Dnexus.password=${nexusPassword} \
    -Dmaven.javadoc.skip=true