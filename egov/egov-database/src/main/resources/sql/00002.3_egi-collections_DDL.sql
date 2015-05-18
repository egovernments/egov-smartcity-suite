CREATE TABLE eg_service_accountdetails (
    id bigint NOT NULL,
    id_servicedetails bigint NOT NULL,
    glcodeid bigint NOT NULL,
    amount double precision,
    functionid bigint
);

CREATE TABLE eg_service_dept_mapping (
    id_department bigint NOT NULL,
    id_servicedetails bigint NOT NULL
);

CREATE TABLE eg_service_subledgerinfo (
    id bigint NOT NULL,
    id_accountdetailtype bigint NOT NULL,
    id_accountdetailkey bigint,
    amount double precision,
    id_serviceaccountdetail bigint NOT NULL
);


CREATE TABLE eg_servicecategory (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    code character varying(50) NOT NULL,
    isactive character(1) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL
);

CREATE TABLE eg_servicedetails (
    id bigint NOT NULL,
    servicename character varying(100) NOT NULL,
    serviceurl character varying(256),
    isenabled smallint,
    callbackurl character varying(256),
    servicetype character(1),
    code character varying(12) NOT NULL,
    fundid bigint,
    fundsourceid bigint,
    functionaryid bigint,
    voucher_creation smallint,
    schemeid bigint,
    subschemeid bigint,
    id_service_category bigint,
    isvoucherapproved smallint,
    vouchercutoffdate timestamp without time zone
);


CREATE SEQUENCE seq_eg_service_subledgerinfo;

CREATE SEQUENCE seq_eg_service_accountdetails;

CREATE SEQUENCE seq_eg_servicecategory;

CREATE SEQUENCE seq_eg_servicedetails;

CREATE SEQUENCE seq_service_history;

