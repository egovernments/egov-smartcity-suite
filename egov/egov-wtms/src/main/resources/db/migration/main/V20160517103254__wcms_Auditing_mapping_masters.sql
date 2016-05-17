-------Property Usage master------

CREATE TABLE egwtr_property_usage_aud(
id integer NOT NULL,
rev integer NOT NULL,
usagetype bigint,
propertytype bigint,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_property_usage_aud ADD CONSTRAINT pk_egwtr_property_usage_aud PRIMARY KEY (id, rev);

-------Property Category master------

CREATE TABLE egwtr_property_category_aud(
id integer NOT NULL,
rev integer NOT NULL,
categorytype bigint,
propertytype bigint,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_property_category_aud ADD CONSTRAINT pk_egwtr_property_category_aud PRIMARY KEY (id, rev);

------Property Pipe Size---------

CREATE TABLE egwtr_property_pipe_size_aud(
id integer NOT NULL,
rev integer NOT NULL,
pipesize bigint,
propertytype bigint,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_property_pipe_size_aud ADD CONSTRAINT pk_egwtr_property_pipe_size_aud PRIMARY KEY (id, rev);

-----Meter Cost master------

CREATE TABLE egwtr_metercost_aud(
id integer NOT NULL,
rev integer NOT NULL,
pipesize bigint,
metermake character varying(50),
amount double precision,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_metercost_aud ADD CONSTRAINT pk_egwtr_metercost_aud PRIMARY KEY (id, rev);


------Application Process Time master------

CREATE TABLE egwtr_application_process_time_aud(
id integer NOT NULL,
rev integer NOT NULL,
applicationtype bigint,
category bigint,
processingtime numeric,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_application_process_time_aud ADD CONSTRAINT pk_egwtr_application_process_time_aud PRIMARY KEY (id, rev);

----- Document Master------

CREATE TABLE egwtr_document_names_aud(
id integer NOT NULL,
rev integer NOT NULL,
applicationtype bigint,
documentname character varying(100) ,
required boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_document_names_aud ADD CONSTRAINT pk_egwtr_document_names_aud PRIMARY KEY (id, rev);

------Application Type master-------

CREATE TABLE egwtr_application_type_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(25),
name character varying(50),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_application_type_aud ADD CONSTRAINT pk_egwtr_application_type_aud PRIMARY KEY (id, rev);

-----Property Type master------

CREATE TABLE egwtr_property_type_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(25),
name character varying(50),
connectioneligibility character(1),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_property_type_aud ADD CONSTRAINT pk_egwtr_property_type_aud PRIMARY KEY (id, rev);
