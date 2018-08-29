alter table egpgr_receivingmode add column rcrequired boolean NOT NULL DEFAULT FALSE;
update egpgr_receivingmode set rcrequired =true where code='MANUAL';
