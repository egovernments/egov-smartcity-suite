insert into eg_roleaction values((select id from eg_role where name='TLCreator'),
(select id from eg_action where url='/newtradelicense/newTradeLicense-approve.action' and contextroot='tl'));
