-------------------------document table for Revision Petition--------------
CREATE TABLE egpt_objection_docs
(
  objection bigint,
  document bigint
);

COMMENT ON TABLE egpt_objection_docs IS 'Docs for revision petition';

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Others',false,null,'OBJECTION',(select id from egpt_application_type where code='REVISION_PETETION'));
