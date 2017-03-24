---enable normal search screen
update eg_action set enabled=true where contextroot='lcms' and name='searchlegalcase' and enabled=false;

-----delete elastic search screen
delete from eg_feature_action where action =(select id FROM eg_action  WHERE name = 'elasticsearchlegalcase');
delete from eg_roleaction where actionid =(select id from eg_action where name='elasticsearchlegalcase');
delete from eg_action where name ='elasticsearchlegalcase' and contextroot='lcms'; 