#UP
/******Sample data for CPF calculation in pfheader table ******/

INSERT INTO EGPAY_PFHEADER ( ID, PFACCOUNTID, PFINTEXPACCOUNTID, FREQUENCY, TDS_ID, ID_WF_ACTION,
PF_TYPE ) VALUES ( 
SEQ_PFHEADER.nextval, NULL, (select id from chartofaccounts where glcode='2405000'), NULL, (select id from tds where type='CPF' ), (select wfa.id from eg_wf_actions wfa where wfa.type='CPFHeader' and wfa.name='PctBasic'), 'CPF'); 

commit;

#DOWN