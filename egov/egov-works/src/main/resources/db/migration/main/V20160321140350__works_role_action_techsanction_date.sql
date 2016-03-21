-----------------Role action mappings to validate technical sanction date----------------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxValidateTechnicalSanction','/lineestimate/ajaxvalidate-technicalsanction-date',null,(select id from EG_MODULE where name = 'WorksLineEstimate'),1,'Ajax Validate Technical Sanction','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxValidateTechnicalSanction' and contextroot = 'egworks'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'AjaxValidateTechnicalSanction' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Works Creator');
--rollback delete from eg_roleaction where roleid = (select id from eg_role where name = 'Works Approver');
--rollback delete from EG_ACTION where name = 'AjaxValidateTechnicalSanction' and contextroot = 'egworks';