#UP

INSERT INTO ACCOUNTDETAILKEY
(ID, groupid, detailtypeid, detailname, detailkey)
SELECT seq_accountdetailkey.NEXTVAL, 1, (SELECT id FROM ACCOUNTDETAILTYPE WHERE UPPER (NAME) = UPPER ('Creditor')) AS detailtypeid , 'relation_id', a.id
 FROM RELATION a  WHERE a.NAME ='Teja' ; 
 
#DOWN

