CREATE TABLE egp_portallink (
    id integer NOT NULL,
    modulename character varying(50) NOT NULL,
    url character varying(250) NOT NULL,
    applicantname character varying(100) NOT NULL,
    consumerNo character varying(50) NOT NULL,
    userid integer NOT NULL,
    paymentURL character varying(250) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint NOT NULL,
    version bigint NOT NULL,
    isActive boolean default true
);
CREATE SEQUENCE seq_egp_link
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egp_portallink ADD CONSTRAINT pk_c_portallink PRIMARY KEY (id);
ALTER TABLE ONLY egp_portallink ADD CONSTRAINT fk_c_portallink_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_portallink ADD CONSTRAINT fk_c_portallink_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_portallink ADD CONSTRAINT fk_c_portallink_user FOREIGN KEY (userid) REFERENCES eg_user(id); 
INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'My Services', true, 'wtms', null, 'My Services', (select max(ordernumber)+1 from eg_module where parentmodule is null));


INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='My Services'),'Link Service',null,0,'/wtms/elastic/appSearch/','true','Link Service','true','true',null,1,now(),now(),1);

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby,createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),'portalLink', '/citizen/linkconnection/',NULL,(select id from eg_module where name='Portal Services'), 3, 'Portal Link', false, 'portal', 0, 1, now(), 1, now(), (select id from eg_module where name='Citizen Portal'));

