
update egmrs_religion set name='Hinduism',description='Hinduism' where name='Hindu';
update egmrs_religion set name='Christianity',description='Christianity' where name='Christian';
update egmrs_religion set name='Islam',description='Islam' where name='Muslim';
update egmrs_religion set name='Zorastrainism',description='Zorastrainism' where name='Parsi';

INSERT INTO egmrs_religion(id, name, description, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGMRS_RELIGION'),'Sikhism','Sikhism', 0, 1, current_timestamp, 1, current_timestamp);

INSERT INTO egmrs_religion(id, name, description, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGMRS_RELIGION'),'Buddhism','Buddhism', 0, 1, current_timestamp, 1, current_timestamp);

INSERT INTO egmrs_religion(id, name, description, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGMRS_RELIGION'),'Jainism','Jainism', 0, 1, current_timestamp, 1, current_timestamp);

INSERT INTO egmrs_religion(id, name, description, version, createdby, createddate, lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EGMRS_RELIGION'),'Judaism','Judaism', 0, 1, current_timestamp, 1, current_timestamp);


