INSERT INTO eg_bill_type(id, name, code, create_date, modified_date) VALUES(nextval('seq_eg_bill_type'), 'AUTO', 'AUTO', current_timestamp, current_timestamp);

INSERT INTO egcl_servicedetails (ID,NAME,SERVICEURL,ISENABLED,CALLBACKURL,SERVICETYPE,CODE,FUND,FUNDSOURCE,FUNCTIONARY, VOUCHERCREATION,SCHEME,SUBSCHEME,SERVICECATEGORY,ISVOUCHERAPPROVED,VOUCHERCUTOFFDATE,CREATED_BY,created_date, modified_by,modified_date) 
VALUES (2,'Property Tax','/../ptis/view/viewDCBProperty!displayPropInfo.action?propertyId=',true,'/receipts/receipt-create.action', 'B','PT',1,null,null,true,null,null,3,true,to_date('11-07-12','DD-MM-RR'),1,current_timestamp,1,current_timestamp);

INSERT INTO egcl_servicecategory(id, name, code, isactive, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES(3, 'temp', 'TEMP', true, 0, 1, current_timestamp, 1, current_timestamp);

INSERT INTO EGF_INSTRUMENTTYPE (ID,TYPE,ISACTIVE,CREATEDBY,LASTMODIFIEDBY,CREATEDDATE,LASTMODIFIEDDATE) values (2,'cash','1',1,1, current_timestamp, current_timestamp);

INSERT INTO EG_LOCATION (ID,NAME,DESCRIPTION,LOCATIONID,CREATEDDATE,LASTMODIFIEDDATE,ISACTIVE,ISLOCATION) values (2,'Z01C1','Zone 1 Counter 1',86, current_timestamp, current_timestamp, 1, 0);
