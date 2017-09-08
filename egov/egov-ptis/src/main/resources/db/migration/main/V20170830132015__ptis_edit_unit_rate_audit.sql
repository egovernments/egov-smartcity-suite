create sequence SEQ_EGPT_MSTR_CATEGORY_AUDIT;
CREATE TABLE EGPT_MSTR_CATEGORY_AUDIT
(
  id bigint PRIMARY KEY,
  category_name  character varying(32),
  category_amnt bigint, 
  id_category bigint NOT NULL,
  id_usage  bigint,
  id_struct_cl  bigint,
  action character varying(32),
  createdby bigint,                       
  createddate timestamp without time zone,             
  lastmodifiedby bigint,         
  lastmodifieddate timestamp without time zone,         
  version bigint               
);
COMMENT ON COLUMN EGPT_MSTR_CATEGORY_AUDIT.id_category IS 'id of egpt_mstr_category';

---------feature for nature of usage

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Usage Master','Master screen to edit property Nature Of Usage',
(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_feature_action (ACTION, FEATURE)
VALUES
((select id FROM eg_action  WHERE name = 'Modify Usage Master'),(select id FROM eg_feature  WHERE name = 'Modify Usage Master'));
