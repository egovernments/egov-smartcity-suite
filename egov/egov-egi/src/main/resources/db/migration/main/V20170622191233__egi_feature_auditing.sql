create table eg_feature_aud(
id numeric,
rev integer,
revtype numeric
);

create table eg_feature_role_aud(
feature numeric,
role numeric,
rev integer,
revtype numeric
);

alter table eg_user_aud add column password varchar(64);