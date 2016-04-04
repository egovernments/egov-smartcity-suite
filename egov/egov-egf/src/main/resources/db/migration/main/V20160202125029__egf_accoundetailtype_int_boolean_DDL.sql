alter table accountdetailtype drop column isactive;
alter table accountdetailtype add column isactive boolean;
update accountdetailtype set isactive=true where isactive is null;
update accountdetailtype set version=0 where version  is null;
