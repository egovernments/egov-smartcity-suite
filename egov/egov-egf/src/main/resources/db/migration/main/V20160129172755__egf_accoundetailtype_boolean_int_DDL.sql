alter table accountdetailtype drop column isactive;
alter table accountdetailtype add column isactive smallint;
update accountdetailtype set isactive=1 where isactive is null;
