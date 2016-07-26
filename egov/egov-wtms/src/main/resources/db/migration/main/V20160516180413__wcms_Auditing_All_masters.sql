----Connection category master ------

CREATE TABLE egwtr_category_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(100),
name character varying(50),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_category_aud ADD CONSTRAINT pk_egwtr_category_aud PRIMARY KEY (id, rev);

-----Usage type master------

CREATE TABLE egwtr_usage_type_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(100),
name character varying(50),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_usage_type_aud ADD CONSTRAINT pk_egwtr_usage_type_aud PRIMARY KEY (id, rev);


------Pipe Size master-------

CREATE TABLE egwtr_pipesize_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(100),
sizeinmilimeter double precision,
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_pipesize_aud ADD CONSTRAINT pk_egwtr_pipesize_aud PRIMARY KEY (id, rev);

---------Water Source master------

CREATE TABLE egwtr_water_source_aud(
id integer NOT NULL,
rev integer NOT NULL,
code character varying(25),
watersourcetype character varying(100),
description character varying(255),
active boolean,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
revtype numeric
);

ALTER TABLE ONLY egwtr_water_source_aud ADD CONSTRAINT pk_egwtr_water_source_aud PRIMARY KEY (id, rev);
