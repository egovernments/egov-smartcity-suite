INSERT into eg_role values(nextval('seq_eg_role'),'Employee Admin','Employee Admin',current_date,1,1,current_date,0);

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Employee'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Employee'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Position'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Position'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ViewuserRoleForm'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Employee Data'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Official Home Page'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Inbox'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'InboxDraft'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'InboxHistory'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'File Download'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'RemoveFavourite'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AddFavourite'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Role View'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Role Update'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'OfficialsProfileEdit'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'OfficialSentFeedBack'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'OfficialChangePassword'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Open inbox'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'EmpDesigAutoComplete'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'EmpPosAutoComplete'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Position count in Search Position'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Ajax Call in Search Position'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Update Position'), id from eg_role where name in ('Employee Admin');

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AjaxLoadBoundarys'), id from eg_role where name in ('Employee Admin');
