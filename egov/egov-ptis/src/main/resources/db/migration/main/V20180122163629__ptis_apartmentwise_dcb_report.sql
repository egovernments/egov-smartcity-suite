INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'ApartmentDCBReport','/report/apartmentdcbreport',null,(select id from eg_module where name='PTIS-Reports'),3,'Apartment DCB Report',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ApartmentDCBReport'),id from eg_role where name in ('Super User','ERP Report Viewer');

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'ApartmentDCBReportResult','/report/apartmentdcbreport/result',null,(select id from eg_module where name='PTIS-Reports'),1,'Apartment DCB Report Result',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ApartmentDCBReportResult'),id from eg_role where name in ('Super User','ERP Report Viewer');


