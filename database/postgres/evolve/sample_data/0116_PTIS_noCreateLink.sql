#UP

delete from eg_roleaction_map where actionid in(
select id from eg_action where url in ('/property/beforeCreateProperty.do','/property/createPropertyConfirm.do','/property/createPropertySubmit.do'));
update eg_action set is_enabled = 0 where url in ('/property/beforeCreateProperty.do','/property/createPropertyConfirm.do','/property/createPropertySubmit.do');

#DOWN





