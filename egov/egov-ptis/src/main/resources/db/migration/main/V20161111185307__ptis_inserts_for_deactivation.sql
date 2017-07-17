------update and insert for deactivation reasons----
update egpt_mutation_master set type='DEACT' where type='DEACTIVATE';

insert into egpt_mutation_master values(nextval('seq_egpt_mutation_master'),'AMALGAMATED PROPERTY','Amalgamated Property','DEACTIVATE','AMALGA',9);
insert into egpt_mutation_master values(nextval('seq_egpt_mutation_master'),'BOGUS PROPERTY','Bogus Property','DEACTIVATE','BOGUS',9);
insert into egpt_mutation_master values(nextval('seq_egpt_mutation_master'),'CONVERTED TO HOUSE','Converted To House','DEACTIVATE','CONV_TO_HOUSE',9);
insert into egpt_mutation_master values(nextval('seq_egpt_mutation_master'),'DEMOLISHED PROPERTY','Demolished Property','DEACTIVATE','DEMOLISH',9);
insert into egpt_mutation_master values(nextval('seq_egpt_mutation_master'),'DUPLICATE PROPERTY','Duplicate Property','DEACTIVATE','DUPLIC',9);

-----insert into application type----
insert into egpt_application_type values(nextval('seq_egpt_application_type'),'DEACTIVATE','DEACTIVATE',7,'Property Deactivation',now(),null,1,null,null);

-----insert into document type-----
insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Council Resolution Document',true,null,'DEACTIVATE',(select id from egpt_application_type where code='DEACTIVATE'));

----insert into ststus value-------
insert into egpt_status values(nextval('seq_egpt_status'),'PROPERTY DEACTIVATED','2016-04-28','Y','DEACTIVATE');







