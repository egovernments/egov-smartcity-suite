---Auditing table 
CREATE TABLE eglc_advocate_master_aud
(
  id integer NOT NULL,
  rev integer NOT NULL,
  salutation  character varying(10) ,
  name character varying (250) ,
  address character varying(256),
  contactphone character varying(256),
  specilization character varying(256),
  mobilenumber character varying(10),
  email  character varying(50),
  monthlyrenumeration numeric(15,2),
  isretaineradvocate boolean  ,
  firmname  character varying(50),
  pannumber character varying(20),
  paymentmode character varying(20),
  bankaccount character varying(50) ,
  ifsccode character varying(11),
  tinumber character varying(11),
  fee double precision,
  isactive  boolean  ,
  issenioradvocate boolean ,
  remarks  character varying(256),
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  revtype numeric
);
ALTER TABLE ONLY eglc_advocate_master_aud ADD CONSTRAINT pk_eglc_advocate_master_aud PRIMARY KEY (id, rev);

COMMENT ON TABLE eglc_advocate_master_aud IS 'Advocate Master Auditing table';
COMMENT ON COLUMN eglc_advocate_master_aud.id IS 'Primary Key';
COMMENT ON COLUMN eglc_advocate_master_aud.rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_advocate_master_aud.salutation IS 'Mr./Mrs./Shri ';
COMMENT ON COLUMN eglc_advocate_master_aud.name IS 'Name of Advocate';
COMMENT ON COLUMN eglc_advocate_master_aud.address IS 'Address of Advocate';
COMMENT ON COLUMN eglc_advocate_master_aud.contactphone IS 'Phone Number';
COMMENT ON COLUMN eglc_advocate_master_aud.specilization IS 'Advocate Speciality';
COMMENT ON COLUMN eglc_advocate_master_aud.mobilenumber IS 'Mobile Number';
COMMENT ON COLUMN eglc_advocate_master_aud.email IS 'Email';
COMMENT ON COLUMN eglc_advocate_master_aud.monthlyrenumeration IS 'Monthly Fees';
COMMENT ON COLUMN eglc_advocate_master_aud.isretaineradvocate IS 'Is Retainer Advocate?';
COMMENT ON COLUMN eglc_advocate_master_aud.firmname IS 'FIR Name';
COMMENT ON COLUMN eglc_advocate_master_aud.pannumber IS 'PAN Number';
COMMENT ON COLUMN eglc_advocate_master_aud.isactive IS 'Is Active?';
COMMENT ON COLUMN eglc_advocate_master_aud.issenioradvocate IS 'Is Senior Advocate?';
COMMENT ON COLUMN eglc_advocate_master_aud.paymentmode IS 'Payment Mode';
COMMENT ON COLUMN eglc_advocate_master_aud.bankaccount IS 'Bank Account Number';
COMMENT ON COLUMN eglc_advocate_master_aud.ifsccode IS 'IFSC Code of Bank for Bank to Bank Payment.';
COMMENT ON COLUMN eglc_advocate_master_aud.tinumber IS 'TIN Number of the Legal Firm(if any)';
COMMENT ON COLUMN eglc_advocate_master_aud.fee IS 'Fee';
COMMENT ON COLUMN eglc_advocate_master_aud. remarks IS 'Remarks';
COMMENT ON COLUMN eglc_advocate_master_aud.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_advocate_master_aud.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_advocate_master_aud.revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';

-----Inserting Accountdetailtype
INSERT INTO accountdetailtype (id, name, description, tablename, columnname, attributename, nbroflevels, isactive, created, lastmodified, modifiedby, full_qualified_name,version) 
VALUES (nextval('seq_accountdetailtype'), 'lawyer', 'Standing Counsel', 'eglc_advocate_master', 'id', 'advocate_master_id', 
1, 'TRUE', 'now()', 'now()', null, 'org.egov.lcms.masters.entity.AdvocateMaster',0);

----Inserting eg_module 

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'StandingcounselMaster', true, NULL,(select id from eg_module where name='LCMS Masters'), 'Standing Counsel Master', 8);

-----Inserting eg_action
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-AdvocateMaster','/advocatemaster/new',(select id from eg_module where name='StandingcounselMaster'),1,' Create Standing Counsel',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-AdvocateMaster','/advocatemaster/create',(select id from eg_module where name='StandingcounselMaster'),1,'Create-AdvocateMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-AdvocateMaster','/advocatemaster/update',(select id from eg_module where name='StandingcounselMaster'),1,'Update-AdvocateMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-AdvocateMaster','/advocatemaster/view',(select id from eg_module where name='StandingcounselMaster'),1,'View-AdvocateMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-AdvocateMaster','/advocatemaster/edit',(select id from eg_module where name='StandingcounselMaster' ),1,'Edit-AdvocateMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-AdvocateMaster','/advocatemaster/result',(select id from eg_module where name='StandingcounselMaster'),1,'Result-AdvocateMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-AdvocateMaster','/advocatemaster/search/view',(select id from eg_module where name='StandingcounselMaster' ),2,' View Standing Counsel',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-AdvocateMaster','/advocatemaster/search/edit',(select id from eg_module where name='StandingcounselMaster' ),3,'Modify Standing Counsel',true,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-AdvocateMaster','/advocatemaster/ajaxsearch/view',(select id from eg_module where name='StandingcounselMaster'),1,'Search and View Result-AdvocateMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-AdvocateMaster'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-AdvocateMaster','/advocatemaster/ajaxsearch/edit',(select id from eg_module where name='StandingcounselMaster'),1,'Search and Edit Result-AdvocateMaster',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-AdvocateMaster'));

------alter add column--
ALTER TABLE eglc_advocate_master ADD remarks character varying(256);

----------------------getAllBankBranchesByBank-----
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BankBranchsByBankAjax','/ajax-getAllBankBranchsByBank',
(select id from eg_module where name='StandingcounselMaster'),1,'BankBranchsByBankAjax',false,'lcms',(select id from eg_module where name='LCMS' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BankBranchsByBankAjax'));
