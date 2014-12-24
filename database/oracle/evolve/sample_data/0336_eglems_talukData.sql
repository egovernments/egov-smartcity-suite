#UP
 Insert into EG_DISTRICT
    (DISTRICTID, DISTRICTNAME, DISTRICTCONST, STATEID)
   Values
   (SEQ_EG_DISTRICT.nextval, 'CHENNAI', 'CHENNAI', (select stateid from EG_STATE where statename='TAMILNADU'));
   
Insert into EG_TALUK
   (TALUKID, TALUKCONST, TALUKNAME, DISTRICTID, STATEID)
 Values
   (SEQ_EG_TALUK.nextval,'Tondiarpet', '01-Fort Tondiarpet', (select districtid from EG_DISTRICT where stateid in (select stateid from eg_state where statename='TAMILNADU')), (select stateid from EG_STATE where statename='TAMILNADU'));
 
 Insert into EG_TALUK
   (TALUKID, TALUKCONST, TALUKNAME, DISTRICTID, STATEID)
 Values
   (SEQ_EG_TALUK.nextval,'Nungambakkam', '02-Egmore Nungambakkam', (select districtid from EG_DISTRICT where stateid in (select stateid from eg_state where statename='TAMILNADU')), (select stateid from EG_STATE where statename='TAMILNADU'));
  
Insert into EG_TALUK
   (TALUKID, TALUKCONST, TALUKNAME, DISTRICTID, STATEID)
 Values
   (SEQ_EG_TALUK.nextval,'Purasavakkam', '03-Perambur-Purasavakkam', (select districtid from EG_DISTRICT where stateid in (select stateid from eg_state where statename='TAMILNADU')), (select stateid from EG_STATE where statename='TAMILNADU'));
 
 Insert into EG_TALUK
   (TALUKID, TALUKCONST, TALUKNAME, DISTRICTID, STATEID)
 Values
   (SEQ_EG_TALUK.nextval,'Triplicane', '04-Mylapore-Triplicane', (select districtid from EG_DISTRICT where stateid in (select stateid from eg_state where statename='TAMILNADU')), (select stateid from EG_STATE where statename='TAMILNADU'));
  
Insert into EG_TALUK
   (TALUKID, TALUKCONST, TALUKNAME, DISTRICTID, STATEID)
 Values
   (SEQ_EG_TALUK.nextval,'Guindy', '05-Mambalam-Guindy', (select districtid from EG_DISTRICT where stateid in (select stateid from eg_state where statename='TAMILNADU')), (select stateid from EG_STATE where statename='TAMILNADU'));

   
INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
SEQ_EG_APPCONFIG.NEXTVAL, 'DISTRICTNAMEFORLANDESTATEMODULE', 'District name used to show taluks in land register', 'LandAndEstate'); 

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DISTRICTNAMEFORLANDESTATEMODULE'),  SYSDATE-1, 'CHENNAI');

#DOWN

delete from eg_taluk where  districtid in (select districtid from EG_DISTRICT where stateid in (select stateid from eg_state where statename='TAMILNADU'));
delete from EG_DISTRICT where  STATEID in (select stateid from EG_STATE where statename='TAMILNADU');

DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DISTRICTNAMEFORLANDESTATEMODULE');
DELETE FROM EG_APPCONFIG WHERE KEY_NAME='DISTRICTNAMEFORLANDESTATEMODULE';
