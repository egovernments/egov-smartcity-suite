alter table accountdetailtype add column version numeric;
alter table accountdetailtype drop column isactive;
alter table accountdetailtype add column isactive Boolean;
update accountdetailtype set isactive=true where isactive is null;
