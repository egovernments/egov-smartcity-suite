--drope egpt_apartment_house
drop table egpt_apartmenthouse; 

--alter egpt_apartent
alter table egpt_apartment add column type character varying(100);
alter table egpt_apartment drop column isresidential;
alter table egpt_apartment alter column name type character varying(250);
alter table egpt_apartment alter column code type character varying(50);
