#UP
insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (seq_eg_appconfig.nextval,'budget_toplevel_approver_designation','budget_toplevel_approver_designation','EGF');
insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (seq_eg_appconfig.nextval,'budget_secondlevel_approver_designation','budget_secondlevel_approver_designation','EGF');

insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (seq_eg_appconfig_values.nextval,(select id from eg_appconfig where key_name='budget_toplevel_approver_designation'),'01-Apr-2000','Commissioner');
insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (seq_eg_appconfig_values.nextval,(select id from eg_appconfig where key_name='budget_secondlevel_approver_designation'),'01-Apr-2000','DEPUTY COMMISSIONER');

#DOWN

delete from eg_appconfig_values where key_id=(select id from eg_appconfig where key_name='budget_toplevel_approver_designation') and value='Commissioner';
delete from eg_appconfig_values where key_id=(select id from eg_appconfig where key_name='budget_secondlevel_approver_designation') and value='DEPUTY COMMISSIONER';


