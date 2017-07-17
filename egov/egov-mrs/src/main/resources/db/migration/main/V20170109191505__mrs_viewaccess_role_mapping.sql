-- Create Role Marriage_VIEW_ACCESS_ROLE and role-action mappings
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('SEQ_EG_ROLE'), 'MARRIAGE_VIEW_ACCESS_ROLE', 'user has access to view masters, reports, transactional data, etc', now(), 1, 1, now(), 0);

--Marriage Religion Master view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='MARRIAGE_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search And View Religion','View-Religion','Religion Search Result') and application=(select id from eg_module where name='Marriage Registration'));

--Marriage Fee Master view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='MARRIAGE_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('View-Fee','Search And View Result Fee','Search And View Fee') and application=(select id from eg_module where name='Marriage Registration'));

--Marriage Registration Unit Master view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='MARRIAGE_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search and View-Registration unit','View-Registration unit','Search and View Result Registration unit') and application=(select id from eg_module where name='Marriage Registration'));

--Marriage Act Master view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='MARRIAGE_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search And View Act','Act Search Result','View-Act') and application=(select id from eg_module where name='Marriage Registration'));

--Marriage Registration transaction view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='MARRIAGE_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('SearchRegistration','View Marriage Registration') and application=(select id from eg_module where name='Marriage Registration'));

--Marriage Registration Report view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='MARRIAGE_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('RegistrationStatusReport','Marriage Registration Age Wise Report','Age Wise Report View Details',
'Marriage Registration Certificate Details Report','Status at the time Marriage  Report',
'RegistrationdatewiseReport','RegistrationmonthwiseReport','RegistrationreligionwiseReport','Act Wise Report View Details',
'Status at the time Marriage Report View Details','Marriage Registration Ageing Report','Ageing Report View Details',
'Monthly Marriage Applications Count','Show Marriage Applications Details','Marriage Applications Status Count Details',
'Religion Wise Registrations Report','Print Religion Wise Registrations Report','Hanidcapped Marriage Registration Report',
'Hanidcapped Marriage Registration Report Search','RegistrationactwiseReport','MarriageFundcollectionReport','MRFundCollectionReport') and application=(select id from eg_module where name='Marriage Registration'));