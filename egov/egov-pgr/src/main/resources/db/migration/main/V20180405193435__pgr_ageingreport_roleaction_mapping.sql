
--role action mapping for download/grand-total in ageing report
delete FROM eg_roleaction  where actionid  in(select id from eg_action  where name in('Ageing report Grand Total','Ageing report Download'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name ='Ageing report Grand Total'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='Boundarywise Ageing Report');

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Ageing report Download'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='Boundarywise Ageing Report');
