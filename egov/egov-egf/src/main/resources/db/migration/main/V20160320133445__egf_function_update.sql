


ALTER TABLE function ADD version bigint ;
ALTER TABLE function RENAME COLUMN created to createddate;
ALTER TABLE function RENAME COLUMN lastmodified to lastModifieddate ;
ALTER TABLE function RENAME COLUMN modifiedby to lastModifiedby;

update eg_action SET enabled ='f' where displayname ='View Function';
update eg_action SET enabled ='f' where displayname ='Create Function';
update eg_action SET enabled ='f' where displayname ='Modify Function';

