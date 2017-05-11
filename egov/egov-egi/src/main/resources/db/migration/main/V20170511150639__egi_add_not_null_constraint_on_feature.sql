delete from eg_feature_action where feature is null or action is null;
delete from eg_feature_role where feature is null or role is null;
alter table eg_feature_action alter column feature set not null;
alter table eg_feature_action alter column action set not null;
alter table eg_feature_role alter column feature set not null;
alter table eg_feature_role alter column role set not null;
