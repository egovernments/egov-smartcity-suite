update eg_action set url = '/firm/create' where name= 'CreateFirm' and contextroot = 'portal';
update eg_action set url = '/firm/update' where name= 'UpdateFirm' and contextroot = 'portal';
update eg_action set url = '/firm/search' where name= 'SearchFirmToModify' and contextroot = 'portal';
update eg_action set url = '/firm/search' where name= 'searchFirm' and contextroot = 'portal';
update eg_action set url = '/firm/view' where name= 'VIEWFIRM' and contextroot = 'portal';

delete from eg_roleaction  where actionid = (select id from eg_action where name='SaveFirm' and contextroot = 'portal');
delete from eg_action where name='SaveFirm' and contextroot = 'portal';

delete from eg_roleaction  where actionid = (select id from eg_action where name='AjaxSearchFirm' and contextroot = 'portal');
delete from eg_action where name='AjaxSearchFirm' and contextroot = 'portal';