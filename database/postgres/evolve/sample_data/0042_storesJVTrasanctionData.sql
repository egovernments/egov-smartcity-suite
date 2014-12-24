#UP



INSERT INTO ACCOUNTDETAILKEY
(ID, groupid, detailtypeid, detailname, detailkey)
SELECT seq_accountdetailkey.NEXTVAL, 1, (SELECT id FROM ACCOUNTDETAILTYPE WHERE UPPER (NAME) = UPPER ('Item')) AS detailtypeid , 'eg_item_id', (SELECT b.id FROM EG_ITEM  b WHERE a.id=b.id) AS detailkey 
 FROM EG_ITEM a ; 

INSERT INTO CHARTOFACCOUNTDETAIL(id,glcodeid,detailtypeid)
VALUES(SEQ_CHARTOFACCOUNTDETAIL.NEXTVAL, (SELECT id FROM CHARTOFACCOUNTS WHERE glcode IN(4604003)), 
(SELECT id FROM ACCOUNTDETAILTYPE WHERE name='Item'));
#DOWN
