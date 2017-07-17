alter table egtl_feematrix  add column effectivefrom timestamp without time zone;
alter table egtl_feematrix  add column effectiveto timestamp without time zone;
alter table egtl_feematrix  add column sameForPermanentAndTemporary boolean default false;
alter table egtl_feematrix  add column sameForNewAndRenew boolean default false;

update egtl_feematrix set  effectivefrom = (select startingdate from financialyear where id=egtl_feematrix.financialyear), effectiveto = (select endingdate from financialyear where id=egtl_feematrix.financialyear);

alter table egtl_feematrix_detail drop column percentage, drop column fromdate, drop column todate;

delete from eg_roleaction  where actionid = (select id from eg_action where name='Create-FeeMatrix' and contextroot='tl');
delete from eg_feature_action where action = (select id from eg_action where name='Create-FeeMatrix' and contextroot='tl');
delete from eg_action where name='Create-FeeMatrix' and contextroot='tl';
update eg_action set url='/feematrix/create', displayname='Create Fee Matrix' where name='Create-License FeeMatrix' and contextroot='tl';

update eg_action set name='Update-FeeMatrix', url='/feematrix/update', displayname='Update Fee Matrix',
parentmodule=(select id from eg_module where name='Trade License Fee Matrix') where name='Delete Fee Matrix' and contextroot='tl';

update eg_action set parentmodule=(select id from eg_module where name='Trade License Fee Matrix') where name='Update-FeeMatrix' and contextroot='tl';

update eg_action set url ='/feematrix/view', name='View-FeeMatrix', enabled=false where name='feematrix-view' and contextroot='tl';

update eg_action set enabled=true, displayname='Search Fee Matrix', parentmodule=(select id from eg_module where name='Trade License Fee Matrix') where name='Search-FeeMatrix' and contextroot='tl';

delete from eg_roleaction  where actionid = (select id from eg_action where name='feematrix-resultview' and contextroot='tl');
delete from eg_feature_action where action = (select id from eg_action where name='feematrix-resultview' and contextroot='tl');
delete from eg_action where name='feematrix-resultview' and contextroot='tl';
