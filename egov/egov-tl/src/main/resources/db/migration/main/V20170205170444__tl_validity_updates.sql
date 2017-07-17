delete from eg_feature_action  where action = (select id from eg_action where name='Create-Validity' and contextroot='tl');
delete from eg_roleaction where actionid = (select id from eg_action where name='Create-Validity' and contextroot='tl');
delete from eg_action where name='Create-Validity' and contextroot='tl';

update eg_action set name = 'Create-Validity', url='/validity/create' where name='New-Validity' and contextroot='tl';

update eg_feature_action  set action = (select id from eg_action where name='Search and Edit-Validity' and contextroot='tl') where action=(select id from eg_action where name='Search and View-Validity' and contextroot='tl') and feature=(select id from eg_feature where name='View License Validity');

delete from eg_feature_action  where action = (select id from eg_action where name='Search and View-Validity' and contextroot='tl');
delete from eg_roleaction where actionid = (select id from eg_action where name='Search and View-Validity' and contextroot='tl');
delete from eg_action where name='Search and View-Validity' and contextroot='tl';

update eg_action set name = 'Search-Validity', url='/validity/search', displayname='Search License Validity' where name='Search and Edit-Validity' and contextroot='tl';


delete from eg_feature_action  where action = (select id from eg_action where name='Edit-Validity' and contextroot='tl');
delete from eg_roleaction where actionid = (select id from eg_action where name='Edit-Validity' and contextroot='tl');
delete from eg_action where name='Edit-Validity' and contextroot='tl';

delete from eg_feature_action  where action = (select id from eg_action where name='Result-Validity' and contextroot='tl');
delete from eg_roleaction where actionid = (select id from eg_action where name='Result-Validity' and contextroot='tl');
delete from eg_action where name='Result-Validity' and contextroot='tl';

delete from eg_feature_action  where action = (select id from eg_action where name='Search and View Result-Validity' and contextroot='tl');
delete from eg_roleaction where actionid = (select id from eg_action where name='Search and View Result-Validity' and contextroot='tl');
delete from eg_action where name='Search and View Result-Validity' and contextroot='tl';


delete from eg_feature_action  where action = (select id from eg_action where name='Search and Edit Result-Validity' and contextroot='tl');
delete from eg_roleaction where actionid = (select id from eg_action where name='Search and Edit Result-Validity' and contextroot='tl');
delete from eg_action where name='Search and Edit Result-Validity' and contextroot='tl';

insert into eg_feature_action(action,feature) values((select id from eg_action where name='View-Validity' and contextroot='tl') , (select id from eg_feature where name = 'Modify License Validity'));

