
DELETE FROM eg_roleaction WHERE actionid in (select id from eg_action where parentmodule=(select id from eg_module where name='Advertisement Tax Penalty Rates'));

DELETE FROM eg_action WHERE parentmodule=(select id from eg_module where name='Advertisement Tax Penalty Rates');

DELETE FROM eg_module WHERE name='Advertisement Tax Penalty Rates';

DROP TABLE  if exists egadtax_mstr_penaltyrates_aud;

INSERT INTO  EG_MODULE VALUES(nextval('SEQ_EG_MODULE'), 'Advertisement Tax Penalty Rates', true, 'adtax',(select id from eg_module where name='AdvertisementTaxMasters'),'Penalty Rates',null);

INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Change Penalty Rates','/penalty/change',null,(select id from eg_module where name='Advertisement Tax Penalty Rates'),1,'Change Penalty Rates',true,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Super User'),(select id from eg_action where name='Change Penalty Rates'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='Change Penalty Rates'));

INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Create Penalty Rates','/penalty/create',null,(select id from eg_module where name='Advertisement Tax Penalty Rates'),2,'Create Penalty Rates',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Super User'),(select id from eg_action where name='Create Penalty Rates'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='Create Penalty Rates'));

CREATE TABLE egadtax_mstr_penaltyrates_aud
(
  id integer NOT NULL,
  rev integer NOT NULL,
  revtype numeric,
  rangefrom double precision,
  rangeto double precision,
  percentage double precision
);

ALTER TABLE ONLY egadtax_mstr_penaltyrates_aud ADD CONSTRAINT pk_egadtax_mstr_penaltyrates_aud PRIMARY KEY (id, rev);

UPDATE egadtax_mstr_penaltyrates SET rangefrom=-999 WHERE rangefrom=-999999;
