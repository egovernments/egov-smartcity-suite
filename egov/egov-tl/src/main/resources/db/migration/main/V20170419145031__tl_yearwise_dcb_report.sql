CREATE OR REPLACE VIEW egtl_dcb_aggr_view AS 

SELECT lic.license_number AS licensenumber,
            lic.address AS licaddress,
            lic.id AS licenseid,
            licadd.applicant_name AS username,
            lic.id_parent_bndry AS wardid,
            lic.id_adm_bndry AS locality,
            currdd.amount AS curr_demand,
            currdd.amt_collected AS curr_coll,
            currdd.amount - currdd.amt_collected AS curr_balance,
            im.start_date as installment
           FROM egtl_license lic
             JOIN egtl_licensee licadd ON licadd.id_license = lic.id
             JOIN egtl_mstr_status licst ON licst.id = lic.id_status
             LEFT JOIN eg_demand_details currdd ON currdd.id_demand = lic.id_demand
             LEFT JOIN eg_demand_reason dr ON dr.id = currdd.id_demand_reason
             LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
             LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
             LEFT JOIN eg_module m ON m.id = im.id_module
          WHERE drm.isdemand = true and m.name::text = 'Trade License'::text 
          order by lic.id;


--Roleaction mapping
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'installmentwise-dcbreport','/installmentwise/dcbreport',null,(select id from EG_MODULE where name = 'Trade License Reports'),3,'YearWise DCB Report',true,'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='installmentwise-dcbreport'));

Insert into eg_roleaction values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='installmentwise-dcbreport'));

Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='installmentwise-dcbreport'));

Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='installmentwise-dcbreport'));

Insert into eg_roleaction values((select id from eg_role where name='TL VIEW ACCESS'),(select id from eg_action where name='installmentwise-dcbreport'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'installmentwise-dcbresult','/installmentwise/dcbresult',null,(select id from EG_MODULE where name = 'Trade License Reports'),3,'Installment Wise DCB Result',false,'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='installmentwise-dcbresult'));

Insert into eg_roleaction values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='installmentwise-dcbresult'));

Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='installmentwise-dcbresult'));

Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='installmentwise-dcbresult'));

Insert into eg_roleaction values((select id from eg_role where name='TL VIEW ACCESS'),(select id from eg_action where name='installmentwise-dcbresult'));

--Feature action/role mapping
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('SEQ_EG_ACTION'),'YearWise DCB Report','YearWise DCB Report',(select id from eg_module where name='Trade License'),0);

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='installmentwise-dcbreport'),(select id FROM EG_FEATURE
WHERE name  ='YearWise DCB Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='installmentwise-dcbresult'),(select id FROM EG_FEATURE
WHERE name  ='YearWise DCB Report'));

INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
WHERE name  ='YearWise DCB Report'));

INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='YearWise DCB Report'));

INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLCreator')),(select id FROM EG_FEATURE
WHERE name  ='YearWise DCB Report'));

INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLApprover')),(select id FROM EG_FEATURE
WHERE name  ='YearWise DCB Report'));
