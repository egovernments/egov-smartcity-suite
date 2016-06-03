ALTER TABLE EGW_PROJECTCODE RENAME COLUMN modified_by TO lastModifiedBy;
ALTER TABLE EGW_PROJECTCODE RENAME COLUMN created_by TO createdBy;
ALTER TABLE EGW_PROJECTCODE RENAME COLUMN created_date TO createdDate;
ALTER TABLE EGW_PROJECTCODE RENAME COLUMN modified_date TO lastModifiedDate;
ALTER TABLE EGW_PROJECTCODE add column version bigint default 0 ;

UPDATE eg_action set url='/abstractestimate/create' where name='CreateAbstractEstimateForm' and contextroot='egworks';

--rollback update eg_action set url='/abstractestimate/newform' where name='CreateAbstractEstimateForm' and contextroot='egworks';