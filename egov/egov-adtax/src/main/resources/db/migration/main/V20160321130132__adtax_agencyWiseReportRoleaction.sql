INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='Agency Wise DCB Report'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='Agency Wise DCB Report'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='Agency Wise DCB Report'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='Agency Wise DCB Report'));


update egadtax_rates_class set version=0 where version is null;