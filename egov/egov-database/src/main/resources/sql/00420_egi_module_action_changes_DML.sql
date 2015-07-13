UPDATE eg_action SET parentmodule = null WHERE parentmodule NOT IN(SELECT id FROM eg_module);
ALTER TABLE eg_action ADD CONSTRAINT FK_EG_ACTION_PARENTMODULE FOREIGN KEY(parentmodule) REFERENCES eg_module(id);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE contextroot='eis' AND parentmodule IS NULL),enabled=false WHERE contextroot='eis' AND parentmodule IS NULL;
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE contextroot='egf' AND parentmodule IS NULL),enabled=false WHERE contextroot='EGF' AND parentmodule IS NULL;
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE contextroot='collection' AND parentmodule IS NULL),enabled=false WHERE contextroot='collection' AND parentmodule IS NULL;
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE contextroot='ptis' AND parentmodule IS NULL),enabled=false WHERE contextroot='ptis' AND parentmodule IS NULL;
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE contextroot='bpa' AND parentmodule IS NULL),enabled=false WHERE contextroot='bpa' AND parentmodule IS NULL;
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE contextroot='egi' AND parentmodule IS NULL),enabled=false WHERE contextroot='egi' AND parentmodule IS NULL;
INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'Portal',false,'portal',null,'Portal',10);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE contextroot='portal' AND parentmodule IS NULL),enabled=false WHERE contextroot='portal' AND parentmodule IS NULL;
ALTER TABLE eg_action ALTER COLUMN parentmodule SET NOT NULL;
ALTER TABLE eg_module ADD CONSTRAINT FK_EG_MODULE_PARENTMODULE FOREIGN KEY(parentmodule) REFERENCES eg_module(id);
