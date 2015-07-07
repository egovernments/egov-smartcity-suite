
CREATE TABLE vouchermis (
    id bigint NOT NULL,
    billnumber bigint,
    divisionid bigint,
    schemename character varying(25),
    accountcode character varying(25),
    accounthead character varying(70),
    contractamt character varying(25),
    cashbook character varying(25),
    natureofwork character varying(25),
    assetdesc character varying(25),
    userdept character varying(25),
    demandno character varying(25),
    narration character varying(250),
    currentyear character varying(25),
    departmentid bigint,
    deptacchead character varying(25),
    subaccounthead character varying(25),
    projectcode bigint,
    concurrance_pn character varying(25),
    zonecode bigint,
    wardcode bigint,
    divisioncode bigint,
    month bigint,
    grossded character varying(25),
    emd_security character varying(25),
    netdeduction character varying(25),
    netamt character varying(25),
    totexpenditure character varying(25),
    voucherheaderid bigint,
    billregisterid character varying(20),
    acount_department bigint,
    projectfund bigint,
    concurrance_sn smallint,
    segmentid bigint,
    sub_segmentid bigint,
    updatedtimestamp timestamp without time zone,
    createtimestamp timestamp without time zone,
    iut_status character varying(20),
    iut_number character varying(30),
    fundsourceid bigint,
    schemeid bigint,
    subschemeid bigint,
    functionaryid bigint,
    sourcepath character varying(250),
    budgetary_appnumber character varying(30),
    budgetcheckreq smallint,
    functionid bigint
);

CREATE SEQUENCE seq_vouchermis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


CREATE INDEX idx_vmis_dept ON vouchermis USING btree (departmentid);

CREATE INDEX indx_vmis_schemeid ON vouchermis USING btree (schemeid);



CREATE INDEX indx_vmis_segmentid ON vouchermis USING btree (segmentid);



CREATE INDEX indx_vmis_subschemeid ON vouchermis USING btree (subschemeid);



CREATE INDEX indx_vmis_subsegmentid ON vouchermis USING btree (sub_segmentid);



CREATE INDEX indx_vmis_vhid ON vouchermis USING btree (voucherheaderid);


ALTER TABLE ONLY vouchermis
    ADD CONSTRAINT fk_vmis_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id);



ALTER TABLE ONLY vouchermis
    ADD CONSTRAINT fk_vmis_schemeid FOREIGN KEY (schemeid) REFERENCES scheme(id);



ALTER TABLE ONLY vouchermis
    ADD CONSTRAINT fk_vmis_sementidpk FOREIGN KEY (segmentid) REFERENCES fund(id);



ALTER TABLE ONLY vouchermis
    ADD CONSTRAINT fk_vmis_subschemeidpk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY vouchermis
    ADD CONSTRAINT fk_vmis_subsegmentidpk FOREIGN KEY (sub_segmentid) REFERENCES fund(id);



ALTER TABLE ONLY vouchermis
    ADD CONSTRAINT fk_vmis_vhidpk FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);

ALTER TABLE vouchermis ALTER COLUMN budgetcheckreq TYPE boolean USING CASE WHEN budgetcheckreq = 0 THEN FALSE WHEN budgetcheckreq = 1 THEN TRUE ELSE NULL END;



CREATE SEQUENCE seq_voucherdetail
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE TABLE voucherdetail (
    id bigint NOT NULL,
    lineid bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    accountname character varying(200) NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    narration character varying(250)
);

ALTER TABLE ONLY voucherdetail
    ADD CONSTRAINT voucherdetail_pkey PRIMARY KEY (id);

CREATE INDEX indx_vd_vhid ON voucherdetail USING btree (voucherheaderid);


ALTER TABLE ONLY voucherdetail
    ADD CONSTRAINT fk_coa_vd FOREIGN KEY (glcode) REFERENCES chartofaccounts(glcode);

ALTER TABLE ONLY voucherdetail
    ADD CONSTRAINT fk_voucherheader_vd FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);

CREATE TABLE closedperiods (
    id bigint NOT NULL,
    startingdate timestamp without time zone NOT NULL,
    endingdate timestamp without time zone NOT NULL,
    isclosed bigint NOT NULL
);

