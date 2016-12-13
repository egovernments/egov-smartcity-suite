------------DDL for VR documents-----------
CREATE TABLE egpt_vacancy_remission_docs
(
vacancyremission bigint,
document    bigint
);

----inerts into application type------------
insert into egpt_application_type values(nextval('seq_egpt_application_type'),'VACANCY_REMISSION','VACANCYREMISSION',7,'Vacancy Remission',now(),null,1,null,null);

-----insert into document type---------------
insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Electicity Bill',true,null,'VACANCYREMISSION',(select id from egpt_application_type where code='VACANCY_REMISSION'));
insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Property Photocopy',true,null,'VACANCYREMISSION',(select id from egpt_application_type where code='VACANCY_REMISSION'));
insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Other Documents',false,null,'VACANCYREMISSION',(select id from egpt_application_type where code='VACANCY_REMISSION'));
