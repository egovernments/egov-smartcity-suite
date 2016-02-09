insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover')
, id from eg_action where name='tradeLicenseSubCategoryAjax' 
and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Collection Operator')
, id from eg_action where name='tradeLicenseSubCategoryAjax' 
and contextroot = 'tl';



insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator')
, id from eg_action where name='tradeLicenseSubCategoryAjax' 
and contextroot = 'tl';