ALTER TABLE egcncl_members ALTER COLUMN designation TYPE bigint USING designation::bigint;

ALTER TABLE egcncl_members ALTER COLUMN electionward TYPE bigint USING electionward::bigint;

ALTER TABLE egcncl_members ALTER COLUMN qualification TYPE bigint USING qualification::bigint;

ALTER TABLE egcncl_members ALTER COLUMN caste TYPE bigint USING caste::bigint ;

ALTER TABLE egcncl_members ALTER COLUMN partyaffiliation TYPE bigint USING partyaffiliation::bigint ;

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Download-Photo','/councilmember/downloadfile',(select id from eg_module where name='Council Management Master' and parentmodule=(select id from eg_module where name='Council Management' and parentmodule is null)),1,'Download-Photo',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Download-Photo'));

ALTER TABLE egcncl_qualification ADD COLUMN description character varying(100);

ALTER TABLE egcncl_members ADD COLUMN filestoreid bigint NOT NULL;