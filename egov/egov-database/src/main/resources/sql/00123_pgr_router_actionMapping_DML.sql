
update eg_action set is_enabled=0 where name ='AjaxRouterBoundariesbyType';
--rollback update eg_action set is_enabled=1 where name ='AjaxRouterBoundariesbyType';
update eg_action set is_enabled=0 where name ='AjaxRouterPosition';
--rollback update eg_action set is_enabled=1 where name ='AjaxRouterPosition';
