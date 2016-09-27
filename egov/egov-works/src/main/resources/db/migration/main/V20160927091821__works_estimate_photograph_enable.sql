update eg_module set enabled = 'true' where name = 'WorksEstimatePhotograph' and parentmodule = (select id from eg_module where name = 'Works Management');

--rollback update eg_module set enabled = 'false' where name = 'WorksEstimatePhotograph' and parentmodule = (select id from eg_module where name = 'Works Management');