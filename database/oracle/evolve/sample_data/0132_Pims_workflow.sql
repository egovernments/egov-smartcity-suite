#UP

/*********** Object Heirarchy ******************************/

INSERT INTO  EG_POSITION_HIR (id,position_from,position_to,object_type_id)VALUES
	   (SEQ_POSITION_HIR.NEXTVAL,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=100) ,(SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=101),
	   (SELECT o.ID FROM EG_OBJECT_TYPE o WHERE o.TYPE LIKE'Leave'));




#DOWN

