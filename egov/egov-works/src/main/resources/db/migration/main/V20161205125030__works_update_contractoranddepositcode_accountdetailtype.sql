
update ACCOUNTDETAILTYPE set FULL_QUALIFIED_NAME='org.egov.works.masters.entity.Contractor' where NAME='contractor';
update ACCOUNTDETAILTYPE set FULL_QUALIFIED_NAME='org.egov.works.masters.entity.DepositCode' where NAME='DEPOSITCODE';

--rollback update ACCOUNTDETAILTYPE set FULL_QUALIFIED_NAME='org.egov.works.models.masters.DepositCode' where NAME='DEPOSITCODE';
--rollback update ACCOUNTDETAILTYPE set FULL_QUALIFIED_NAME='org.egov.works.models.masters.Contractor' where NAME='contractor';