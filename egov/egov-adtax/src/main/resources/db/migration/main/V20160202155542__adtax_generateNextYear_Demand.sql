
-------- Start ---------
create table EGADTAX_BATCHDEMANDGENERATE (
    id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL,
    totalRecords bigint DEFAULT 0 NOT NULL,
    jobName character varying(120) NOT NULL,
    financialyear bigint NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    version bigint DEFAULT 0
   );

CREATE INDEX idx_adtax_batchDmdGen_financialyear ON EGADTAX_BATCHDEMANDGENERATE USING btree (financialyear);


ALTER TABLE ONLY EGADTAX_BATCHDEMANDGENERATE
ADD CONSTRAINT fk_adtax_batchDmdGen_finYear FOREIGN KEY (financialyear) REFERENCES financialyear(id);
    
CREATE SEQUENCE SEQ_BATCHDEMANDGENERATE;



INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 
'Number of Records used in next year demand generation', 
'Number of Records used in next year demand generation',0, (select id from eg_module where name='Advertisement Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Number of Records used in next year demand generation' AND 
 MODULE =(select id from eg_module where name='Advertisement Tax')),current_date,
  '300',0);

update eg_action set enabled= true where name='generateDemandAdvertisementTax' and contextroot='adtax';
update eg_action set displayname= 'Auto Generate Demand For Advertisements' where name='generateDemandAdvertisementTax' and contextroot='adtax';

-------------end --------
