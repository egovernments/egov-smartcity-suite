insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'01','New Construction',true,0,1,now(),null,now(),true,true,true,false,false,false,'PPA',null,null);

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'02','Demolition',false,0,1,now(),null,now(),false,false,true,false,false,false,'DA',null,null); 

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'03','Reconstruction',true,0,1,now(),null,now(),true,true,true,false,false,false,'PPA',null,null);  

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'04','Division Of Plot',false,0,1,now(),null,now(),false,false,true,false,false,false,'SD',null,null); 

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'05','Alteration',true,0,1,now(),null,now(),true,true,true,false,false,false,'PPA',null,null); 

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'06','Additional or Extension',true,0,1,now(),null,now(),true,true,true,false,false,false,'PPA',null,null); 

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'07','Digging of Well',false,0,1,now(),null,now(),false,false,true,false,false,false,'DW',null,null); 

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'08','Change in occupancy',false,0,1,now(),null,now(),false,false,true,false,false,false,'CO',null,null); 

insert into egbpa_mstr_servicetype ( id,code,description,isactive, version,createdby,createddate,lastmodifiedby,lastmodifieddate,buildingplanapproval,siteapproval, isapplicationfeerequired,isptisnumberrequired,isautodcrnumberrequired, isdocuploadforcitizen,servicenumberprefix,descriptionlocal,sla )
values (nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'09','Erection of Telecommunication tower or other structure',false,0,1,now(),null,now(),false,false,true,false,false,false,'ET',null,null);

-- end of servicetype ----

-- checklist for stakeholder documents ----


INSERT INTO egbpa_mstr_checklist(id, checklisttype, servicetype, version, createdby, createddate,lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGBPA_MSTR_CHECKLIST'), 'STAKEHOLDERDOCUMENT', null, 0, 1, now(), 1, now());

INSERT INTO egbpa_mstr_chklistdetail(id, code, description, isactive, ismandatory, checklist, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL'), 'EDUCER', 'Edcational Certificates', true, false, (select id from egbpa_mstr_checklist where checklisttype='STAKEHOLDERDOCUMENT'), 0, 1, now(), 1, now());

INSERT INTO egbpa_mstr_chklistdetail(id, code, description, isactive, ismandatory, checklist, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL'), 'EXPCER', 'Experience Certificate', true, false,(select id from egbpa_mstr_checklist where checklisttype='STAKEHOLDERDOCUMENT'), 0, 1, now(), 1, now());

INSERT INTO egbpa_mstr_chklistdetail(id, code, description, isactive, ismandatory, checklist, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL'), 'SCN', 'Details of show cause notice received/cancellations of licenses if any', true, false, (select id from egbpa_mstr_checklist where checklisttype='STAKEHOLDERDOCUMENT'), 0, 1, now(), 1, now());

--- document list - by service --


insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description='New Construction'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'101','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'102','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'103','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'104','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'105','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'106','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'107','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'108','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'109','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'110','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'111','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'112','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'113','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'114','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description='New Construction')), 0,1,now());

--Demolition service

insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description= 'Demolition'),0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'201','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'202','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'203','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'204','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'205','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'206','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'207','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'208','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'209','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'210','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'211','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'212','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'213','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'214','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description= 'Demolition')), 0,1,now());

insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description=  'Reconstruction'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'301','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'302','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'303','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'304','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'305','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'306','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'307','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'308','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'309','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'310','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'311','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'312','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'313','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'314','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Reconstruction')), 0,1,now());



insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description=  'Division Of Plot'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'401','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'402','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'403','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'404','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'405','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'406','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'407','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'408','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'409','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'410','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'411','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'412','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'413','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'414','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Division Of Plot')), 0,1,now());


insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description=  'Alteration'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'501','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'502','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'503','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'504','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'505','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'506','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'507','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'508','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'509','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'510','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'511','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'512','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'513','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'514','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Alteration')), 0,1,now());



insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description=  'Additional or Extension'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'601','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'602','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'603','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'604','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'605','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'606','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'607','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'608','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'609','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'610','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'611','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'612','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'613','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'614','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Additional or Extension')), 0,1,now());



insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description=  'Digging of Well'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'701','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'702','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'703','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'704','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'705','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'706','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'707','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'708','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'709','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'710','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'711','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'712','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'713','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'714','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Digging of Well')), 0,1,now());


insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description=  'Change in occupancy'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'801','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'802','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'803','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'804','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'805','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'806','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'807','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'808','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'809','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'810','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'811','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'812','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'813','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'814','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Change in occupancy')), 0,1,now());



insert into EGBPA_MSTR_CHECKLIST(id,checklisttype,servicetype,version,createdBy,createdDate)
values(nextval('SEQ_EGBPA_MSTR_CHECKLIST'),'DOCUMENTATION', (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure'),
0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'901','A Register Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'902','Adangal/ Chitta/ FMB Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());


insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'903','Approved Layout Plan Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'904','Approved Sub-Division Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'905','Court Orders',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'906','Death Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'907','Documents as per the History Sheet',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL (id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'908','Encumbrance Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'909','Enjoyment Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'910','FMB Sketch Prior to 05/08/1975',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'911','Legal Heir Certificate',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'912','Order of the Settlement officers',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'913','Other Documents',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());

insert into EGBPA_MSTR_CHKLISTDETAIL(id ,code,description,isactive,ismandatory,checklist,version,createdBy,createdDate)
values((nextval('SEQ_EGBPA_MSTR_CHKLISTDETAIL')),'914','Patta Copy',true,false,(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION' and servicetype= (select id from egbpa_mstr_servicetype where description=  'Erection of Telecommunication tower or other structure')), 0,1,now());


-- Admission fee

INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'New Construction'),'001','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='001'),null,null,50,null,now(),1,now()  );

INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Demolition'),'002','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='002'),null,null,50,null,now(),1,now()  );

INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Reconstruction'),'003','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='003'),null,null,50,null,now(),1,now()  );


INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Division Of Plot'),'004','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='004'),null,null,50,null,now(),1,now()  );


INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Alteration'),'005','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='005'),null,null,50,null,now(),1,now()  );


INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Additional or Extension'),'006','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='006'),null,null,50,null,now(),1,now()  );


INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Digging of Well'),'007','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='007'),null,null,15,null,now(),1,now()  );


INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Change in occupancy'),'008','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='008'),null,null,50,null,now(),1,now()  );


INSERT INTO egbpa_mstr_bpafee (ID, feetype, SERVICETYPE, code, description, isfixedamount,isactive,ismandatory,CREATEDBY, createddate)
 VALUES ((nextval('SEQ_EGBPA_MSTR_BPAFEE'))  , 'AdmissionFee',(select id from egbpa_mstr_servicetype where  description=  'Erection of Telecommunication tower or other structure'),'009','Admission Fees',true,true,false,1,now());

insert into egbpa_mstr_bpafeedetail (id,bpafee,fromareasqmt,toareasqmt,amount,subtype,startdate,CREATEDBY, createddate) values (
nextval('SEQ_EGBPA_MSTR_BPAFEEDETAIL'), (select id from egbpa_mstr_bpafee where code='009'),null,null,20,null,now(),1,now()  );



---------building category

INSERT INTO egbpa_mstr_bldgcategory (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BLDGCATEGORY'),'Residential','Residential',true,0,1,now());
INSERT INTO egbpa_mstr_bldgcategory (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BLDGCATEGORY'),'Non Residential','Non Residential',true,0,1,now());

----- building usage

INSERT INTO egbpa_mstr_buildingusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BUILDINGUSAGE'),'Residential','Residential',true,0,1,now());
INSERT INTO egbpa_mstr_buildingusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BUILDINGUSAGE'),'Commercial','Commercial',true,0,1,now());
INSERT INTO egbpa_mstr_buildingusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BUILDINGUSAGE'),'Industrial','Industrial',true,0,1,now());
INSERT INTO egbpa_mstr_buildingusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BUILDINGUSAGE'),'Institutional','Institutional',true,0,1,now());
INSERT INTO egbpa_mstr_buildingusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BUILDINGUSAGE'),'Multiple','Multiple',true,0,1,now());
INSERT INTO egbpa_mstr_buildingusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_BUILDINGUSAGE'),'NONRESD','Non - Residential',true,0,1,now());

------ change in usage 

alter table egbpa_mstr_changeofusage drop column name;

INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Agriculture','Agriculture',true,0,1,now());
INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Industrial','Industrial',true,0,1,now());
INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Mixed Residential','Mixed Residential',true,0,1,now());
INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Primary Residential','Primary Residential',true,0,1,now());
INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Commercial','Commercial',true,0,1,now());
INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Institutional','Institutional',true,0,1,now());
INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Open space Reservation','Open space Reservation',true,0,1,now());
INSERT INTO egbpa_mstr_changeofusage (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CHANGEOFUSAGE'),'Others','Others',true,0,1,now());

------- construction stages

INSERT INTO egbpa_mstr_const_stages (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CONST_STAGES'),'Started','Started',true,0,1,now());
INSERT INTO egbpa_mstr_const_stages (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_CONST_STAGES'),'NotStarted','NotStarted',true,0,1,now());


--lp reasons

INSERT INTO egbpa_mstr_lpreason (ID,CODE,DESCRIPTION,reason,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_LPREASON'),'Document Clarification','Document Clarification','Document Clarification',true,0,1,now());
INSERT INTO egbpa_mstr_lpreason (ID,CODE,DESCRIPTION,reason, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_LPREASON'),'Deviation','Deviation','Deviation',true,0,1,now());

-- surrounding building details

INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION,ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Residential','Residential',true,0,1,now());
INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Office','Office',true,0,1,now());
INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Shops','Shops',true,0,1,now());
INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Commercial Establishment','Commercial Establishment',true,0,1,now());
INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Industries','Industries',true,0,1,now());
INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Institution Activities','Institution Activities',true,0,1,now());
INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Agricultural','Agricultural',true,0,1,now());
INSERT INTO egbpa_mstr_surnbldgdtl (ID,CODE,DESCRIPTION, ISACTIVE,VERSION,CREATEDBY,CREATEDDATE) VALUES (nextval('SEQ_EGBPA_MSTR_SURNBLDGDTL'),'Others','Others',true,0,1,now());


--egcl_servicecategory 
delete from egcl_servicedetails where name='BPA AdmissionFee';
delete from egcl_servicecategory where name='Bpa Fees';
Insert into egcl_servicecategory (id,name,code,isactive,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('seq_egcl_servicecategory'),'Building Plan Approval','BPA',true,0,1,now(),1,now());

Insert into egcl_servicedetails (id,name,serviceurl,isenabled,callbackurl,servicetype,code,fund,fundsource,functionary,vouchercreation,scheme,subscheme,servicecategory,isvoucherapproved,vouchercutoffdate,created_by,created_date,modified_by,modified_date,ordernumber) values (nextval('seq_egcl_servicedetails'), 'Building Plan Charges', '/../bpa/collection/bill', true, '/receipts/receipt-create.action', 'B', 'BPA', (select id from fund where code='01'), null, null, true, null, null, (select id from egcl_servicecategory where code='BPA'), true, now(), 1, now(), 1, now(),null);

-- demand reason 

 delete from eg_demand_reason where id_demand_reason_master in ( select id from eg_demand_reason_master  where reasonmaster='ADMISSION FEES' and module=(select id from eg_module where name='BPA'));
 
-- demand reason master

delete from EG_DEMAND_REASON_MASTER  where reasonmaster='ADMISSION FEES' and module in (select id from eg_module where name = 'BPA' and parentmodule is null);
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Admission Fee', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='BPA' and parentmodule is null), 'ADMISSIONFEES', 1, current_timestamp, current_timestamp,'t');



--- installment
delete from eg_installment_master  where description='BPATAX/1718';

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) 
values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042017,to_date('01-04-17','DD-MM-YY'),to_date('01-04-17','DD-MM-YY'),to_date('31-03-18','DD-MM-YY'),(select id from eg_module where name = 'BPA' and parentmodule is null),current_timestamp,'BPA/17-18','Yearly');

-- demand reason

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Admission Fee' and module=(select id from eg_module where name='BPA' and parentmodule is null)), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where 
inst.id_module=(select id from eg_module where name='BPA' and parentmodule is null));