delete from eg_feature_action where action in (select id from eg_action where name in ('save-demand-generate', 'demand-regenerate'));

delete from eg_roleaction where actionid in (select id from eg_action where name in ('save-demand-generate', 'demand-regenerate'));

delete from eg_action where id in (select id from eg_action where name in ('save-demand-generate', 'demand-regenerate'));

update eg_action set name='demand-generate' where name='demand-generate-create' and contextroot='tl';