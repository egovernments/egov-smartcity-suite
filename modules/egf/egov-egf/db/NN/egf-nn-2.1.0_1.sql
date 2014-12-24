-- chequenumber for egf_account_cheques table (cheque master table) is changed to varchar for all other clients.
ALTER TABLE egf_account_cheques DROP CONSTRAINT from_cst;
ALTER TABLE egf_account_cheques DROP CONSTRAINT to_cst;
DROP INDEX from_cst;
DROP INDEX to_cst;
ALTER TABLE egf_account_cheques MODIFY(fromchequenumber  NULL);
ALTER TABLE egf_account_cheques MODIFY(tochequenumber  NULL);
ALTER TABLE EGF_ACCOUNT_CHEQUES ADD (tochequenumber1 VARCHAR(50));
ALTER TABLE EGF_ACCOUNT_CHEQUES ADD (fromchequenumber1 VARCHAR(50));
UPDATE EGF_ACCOUNT_CHEQUES SET fromchequenumber1=fromchequenumber ;
UPDATE EGF_ACCOUNT_CHEQUES SET tochequenumber1=tochequenumber ;
UPDATE EGF_ACCOUNT_CHEQUES SET tochequenumber=NULL;
UPDATE EGF_ACCOUNT_CHEQUES SET fromchequenumber=NULL;
ALTER TABLE EGF_ACCOUNT_CHEQUES MODIFY (tochequenumber VARCHAR(50) );
ALTER TABLE EGF_ACCOUNT_CHEQUES MODIFY (fromchequenumber VARCHAR(50) );
UPDATE EGF_ACCOUNT_CHEQUES SET fromchequenumber =fromchequenumber1 ;
UPDATE EGF_ACCOUNT_CHEQUES SET tochequenumber =tochequenumber1 ;
ALTER TABLE EGF_ACCOUNT_CHEQUES MODIFY (tochequenumber NOT NULL );
ALTER TABLE EGF_ACCOUNT_CHEQUES MODIFY (fromchequenumber NOT NULL );
ALTER TABLE EGF_ACCOUNT_CHEQUES DROP (tochequenumber1) ;
ALTER TABLE EGF_ACCOUNT_CHEQUES DROP (fromchequenumber1) ;
CREATE UNIQUE INDEX FROM_CST ON EGF_ACCOUNT_CHEQUES
(BANKACCOUNTID, FROMCHEQUENUMBER);
CREATE UNIQUE INDEX TO_CST ON EGF_ACCOUNT_CHEQUES
(BANKACCOUNTID, TOCHEQUENUMBER);


update chartofaccounts coa set coa.parentid=(select id from chartofaccounts where glcode=substr(coa.glcode,1,4))
where coa.parentid is null and length(coa.glcode)=6;

commit;
