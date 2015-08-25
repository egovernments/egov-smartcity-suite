INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='createPropertyAckPrint' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='modifyPropertyAckPrint' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='generateSpecialNotice' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));