insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'License Validity','t','tl',(select id from eg_module where name = 'Trade License Masters'),'License Validity', null);

update eg_action set parentmodule=(select id from eg_module where name='License Validity'), displayname='Create License Validity',ordernumber=1 where name='New-Validity' and contextroot='tl';

update eg_action set parentmodule=(select id from eg_module where name='License Validity'), displayname='Update License Validity',ordernumber=2 where name='Search and Edit-Validity' and contextroot='tl';

update eg_action set parentmodule=(select id from eg_module where name='License Validity'), displayname='View License Validity',ordernumber=3 where name='Search and View-Validity' and contextroot='tl';

alter table egtl_validity ALTER COLUMN licensecategory DROP NOT NULL;