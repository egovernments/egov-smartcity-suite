#UP

insert into eg_roleaction_map (actionid,roleid) values ((select id from eg_action where name = 'COCPTIS' and url = '/eGovPtis.jsp'),(select id_role from eg_roles where role_name = 'SuperUser'));

insert into eg_roleaction_map (actionid,roleid) values ((select id from eg_action where url = '/reports/coc/boundaryWisePropUsgeDemand!execute.action' and name = 'zone Wise Prop Usage Demand print'),(select id_role from eg_roles where role_name='ASSISTANT'));
insert into eg_roleaction_map (actionid,roleid) values ((select id from eg_action where url = '/reports/coc/boundaryWisePropUsgeDemand!execute.action' and name = 'zone Wise Prop Usage Demand print'),(select id_role from eg_roles where role_name='PROPERTY_TAX_USER'));

#DOWN
