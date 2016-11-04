insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)values (NEXTVAL('SEQ_EG_ACTION'),'ValidateMileStonePercentage','/milestone/validate-milestonepercentagetocreatecontractorbill',null,
(select id from EG_MODULE where name = 'WorksContractorBill'),0,'Validate MileStone Percentage To create contractor bill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'ValidateMileStonePercentage' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ValidateMileStonePercentage' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name='ValidateMileStonePercentage') and roleid in(select id from eg_role where name in('Works Creator','Super User'));
--rollback delete from eg_action where name='ValidateMileStonePercentage' and contextroot='egworks';