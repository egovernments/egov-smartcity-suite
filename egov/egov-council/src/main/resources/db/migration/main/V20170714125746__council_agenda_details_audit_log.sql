INSERT INTO eg_roleaction (roleid, actionid)
SELECT (select id from eg_role where name='Council Clerk'), (select id from eg_action where name='View-CouncilPreamble')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Council Clerk') and actionid in 
 (select id from eg_action where name='View-CouncilPreamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO egcncl_committeetype(id, name, code, isactive, createddate, createdby, lastmodifieddate,lastmodifiedby, version)
    VALUES (nextval('seq_egcncl_committeetype'), 'Requisition Meeting', 'Requisition Meeting', true, now(), 1, now(), 1, 0);

alter table egcncl_agenda_details add column  createdby bigint;
alter table egcncl_agenda_details add column createddate timestamp without time zone;
alter table egcncl_agenda_details add column lastmodifieddate timestamp without time zone;
alter table egcncl_agenda_details add column  lastmodifiedby bigint;
alter table egcncl_agenda_details add column  version bigint;

CREATE TABLE egcncl_agenda_details_aud
(
  id bigint NOT NULL,
  rev integer NOT NULL,
  agenda bigint NOT NULL,
  itemnumber character varying(25) NOT NULL,
  order_id bigint,
  preamble bigint NOT NULL,
  revtype numeric,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  CONSTRAINT pk_egcncl_agenda_details_aud PRIMARY KEY (id, rev)
);
