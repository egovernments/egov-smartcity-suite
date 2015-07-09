alter table eg_user drop column "version";

alter table eg_citizen drop column "version";
alter table eg_citizen drop column lastModifiedDate;
alter table eg_citizen drop column lastModifiedby;
alter table eg_citizen drop column createdDate;
alter table eg_citizen drop column createdBy;

alter table egeis_employee drop column "version";
alter table egeis_employee drop column lastModifiedDate;
alter table egeis_employee drop column lastModifiedby;
alter table egeis_employee drop column createdDate;
alter table egeis_employee drop column createdBy;

alter table eg_address rename column "user" to userid;
alter table eg_address drop column id;
alter table eg_address drop column "version";

--alter table eg_address rename column userid to "user";
--alter table eg_address add column id numeric;
--alter table eg_address add column "version" numeric;