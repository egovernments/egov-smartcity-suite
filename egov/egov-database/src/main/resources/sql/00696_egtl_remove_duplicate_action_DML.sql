
delete from eg_roleaction where actionid in (Select id from eg_action where url='/newtradelicense/newTradeLicense-create.action' and displayname is null);
delete from eg_action where url='/newtradelicense/newTradeLicense-create.action' and displayname is null;