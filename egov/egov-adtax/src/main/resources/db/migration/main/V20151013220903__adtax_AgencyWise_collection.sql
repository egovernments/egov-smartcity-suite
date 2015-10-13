------------------START------------------

CREATE SEQUENCE SEQ_EGADTAX_AGENCYWISECOLLECTION;
CREATE SEQUENCE SEQ_EGADTAX_AGENCYWISECOLLECTIONDTL;

CREATE TABLE EGADTAX_AGENCYWISECOLLECTION 
(	ID bigint NOT NULL primary key, 
	AGENCY bigint  NOT NULL,  
	billNumber VARCHAR(25) NOT NULL, 
	createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
	lastmodifieddate timestamp without time zone,
	createdby bigint NOT NULL,
	agencyWiseDemand  bigint not null,
	lastmodifiedby bigint,
	demandUpdated boolean DEFAULT false NOT NULL,
	amountCollected boolean DEFAULT false NOT NULL,
	version bigint DEFAULT 0,
	totalAmount double precision
);


CREATE INDEX idx_adtax_agencycollection_agency ON EGADTAX_AGENCYWISECOLLECTION USING btree (agency);
CREATE INDEX idx_adtax_agencycollection_billno ON EGADTAX_AGENCYWISECOLLECTION USING btree (billNumber);
CREATE INDEX idx_adtax_agencycollection_demandUpd ON EGADTAX_AGENCYWISECOLLECTION USING btree (demandUpdated);
ALTER TABLE ONLY EGADTAX_AGENCYWISECOLLECTION  ADD CONSTRAINT fk_adtax_agencycollection_agency FOREIGN KEY (agency) REFERENCES egadtax_agency(id);
ALTER TABLE ONLY EGADTAX_AGENCYWISECOLLECTION  ADD CONSTRAINT fk_adtax_agencycollection_demand FOREIGN KEY (agencyWiseDemand) REFERENCES eg_demand(id);
  

CREATE TABLE EGADTAX_AGENCYWISECOLLECTION_DETAIL 
( ID bigint NOT NULL primary key, 
  agencywisecollection bigint not null,
  demand bigint not null,
  demandDetail bigint ,
  amount bigint not null ,
  demandreason bigint not null,
  version bigint DEFAULT 0
 );
 
 
CREATE INDEX idx_adtax_agencycolDtl_agencyWiseColl ON EGADTAX_AGENCYWISECOLLECTION_DETAIL USING btree (agencywisecollection);
CREATE INDEX idx_adtax_agencycolDtl_demandDel ON EGADTAX_AGENCYWISECOLLECTION_DETAIL USING btree (demandDetail);
CREATE INDEX idx_adtax_agencycolDtl_demand ON EGADTAX_AGENCYWISECOLLECTION_DETAIL USING btree (demand);
CREATE INDEX idx_adtax_agencycolDtl_demandreason ON EGADTAX_AGENCYWISECOLLECTION_DETAIL USING btree (demandreason);

ALTER TABLE ONLY EGADTAX_AGENCYWISECOLLECTION_DETAIL    ADD CONSTRAINT fk_adtax_agencycolDtl_demandDel FOREIGN KEY (demandDetail) REFERENCES eg_demand_details(id);
ALTER TABLE ONLY EGADTAX_AGENCYWISECOLLECTION_DETAIL    ADD CONSTRAINT fk_adtax_agencycolDtl_demand FOREIGN KEY (demand) REFERENCES eg_demand(id);
ALTER TABLE ONLY EGADTAX_AGENCYWISECOLLECTION_DETAIL    ADD CONSTRAINT fk_adtax_agencycolDtl_agencyWiseColl FOREIGN KEY (agencywisecollection) REFERENCES EGADTAX_AGENCYWISECOLLECTION(id);
ALTER TABLE ONLY EGADTAX_AGENCYWISECOLLECTION_DETAIL    ADD CONSTRAINT fk_adtax_agencycolDtl_demandreason FOREIGN KEY (demandreason) REFERENCES eg_demand_reason(id);
        
-------------------END-------------------


------------------START------------------
insert INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)   VALUES (nextval('seq_eg_action'), 'collectTaxByAgency', '/hoarding/collectTaxByAgency', null, (select id from eg_module where name='ADTAX-COMMON'), 1, 'Collect tax by agency', false, 'adtax', 0, 1, now(), 1, now(),    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'collectTaxByAgency'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'CollectAdvertisementTax')); 
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AgencyAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'SubcategoryAjaxDropdown'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'generateBillForCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator'),(select id FROM eg_action  WHERE NAME = 'CreateReceipt' and CONTEXTROOT='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator') , (select id FROM eg_action  WHERE name = 'collectTaxByAgency'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'searchHoardingResult'));

------------------END------------------