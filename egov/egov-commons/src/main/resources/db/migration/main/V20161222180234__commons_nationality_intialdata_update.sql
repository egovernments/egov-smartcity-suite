delete from eg_nationality  where name='Afghans';

update eg_nationality set name='Indian', description='Indian'  where name='Indians';

INSERT INTO eg_nationality(id, name, description, version, createdby, createddate, lastmodifiedby, lastmodifieddate)  VALUES (nextval('SEQ_EG_NATIONALITY'),'Afghans','Afghans', 0, 1, current_timestamp, 1, current_timestamp);




