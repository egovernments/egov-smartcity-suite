
INSERT INTO eg_wf_types (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, renderyn, groupyn, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), 2, 'AdvertisementPermitDetail', '/adtax/advertisement/update/:ID', 1, '2015-08-28 10:45:18.201078', 1, '2015-08-28 10:45:18.201078', 'Y', 'N', 'org.egov.adtax.entity.AdvertisementPermitDetail', 'Advertisement', 0);

Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEMENT','CREATED',now(),'CREATED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEMENT','Approved',now(),'APPROVED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEMENT','AdTax Amount Paid',now(),'ADTAXAMOUNTPAID',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEMENT','AdTax Permit Generated',now(),'ADTAXPERMITGENERATED',1);

