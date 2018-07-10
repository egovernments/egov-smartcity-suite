INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Marriage Registrar', 'Marriage Registrar', now(), 1, 1, now(), 0);

INSERT INTO eg_roleaction(roleid,actionid)  VALUES((select id from eg_role  where name ='Marriage Registrar'),(select id from eg_action where name ='View Marriage Certificate'));