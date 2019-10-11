UPDATE eg_action set displayname ='Create Write-Off' where name='Write Off' and contextroot='ptis';

----------Council Role Action Mapping----------------------------

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name in ('CouncilResolutionDetails')), id from eg_role where name in ('Write Off Initiator','Write Off Approver');

----------Sewage Role Action Mapping----------------------------
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name in ('Rest SewerageTax Dues')), id from eg_role where name in ('Write Off Initiator','Write Off Approver');

