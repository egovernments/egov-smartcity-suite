--renaming marketvalue as circlerate, added new marketvalue column
alter table egpt_property_detail rename column marketvalue to circlerate;
alter table egpt_property_detail add column marketvalue double precision;

--updating new marketvalue column with circlerate for existing properties
update egpt_property_detail set marketvalue=current_capitalvalue where current_capitalvalue is not null and marketvalue is null;
