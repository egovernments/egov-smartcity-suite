alter table eg_applicationindex add column citycode character varying(4);
alter table eg_applicationindex add column citygrade character varying(50);
alter table eg_applicationindex add column regionname character varying(50);
alter table eg_applicationindex add column isclosed integer;
--alter table eg_applicationindex drop column citycode;
--alter table eg_applicationindex drop column citygrade;
--alter table eg_applicationindex drop column regionname;
--alter table eg_applicationindex drop column isclosed;
