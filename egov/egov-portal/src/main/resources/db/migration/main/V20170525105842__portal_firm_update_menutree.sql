INSERT into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Portal Firm','true','portal',(select id from eg_module where name='Portal Masters'),'Firm', 2);

update eg_action set parentmodule = (select id from EG_MODULE where name ='Portal Firm') where name = 'CreateFirm';
update eg_action set parentmodule = (select id from EG_MODULE where name ='Portal Firm') where name = 'SearchFirmToModify';
update eg_action set parentmodule = (select id from EG_MODULE where name ='Portal Firm') where name = 'searchFirm';

update eg_action set displayname = 'Search Firm' where name = 'searchFirm';