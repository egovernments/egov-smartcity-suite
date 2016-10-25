update eg_module set displayname = 'Abstract/Detailed Estimate' where name = 'WorksAbstractEstimate' and parentmodule = (select id from eg_module where name = 'Works Management');

--rollback update eg_module set displayname = 'Abstract Estimate' where name = 'WorksAbstractEstimate' and parentmodule = (select id from eg_module where name = 'Works Management');