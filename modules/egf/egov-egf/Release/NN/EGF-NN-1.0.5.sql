CREATE OR REPLACE PROCEDURE VOUCHEMIS_UPDATE
AS
  v_VHID		 NUMBER;
  v_date	DATE;
  v_fs	  		NUMBER DEFAULT NULL;
 
 
  CURSOR C1
	IS
	SELECT * FROM VOUCHERHEADER
	WHERE ID NOT IN(SELECT voucherheaderid FROM VOUCHERMIS);		
	  
BEGIN
	 DBMS_OUTPUT.PUT_LINE('Getting into the loop');
	
	 FOR VOUCHERHD_cursor IN C1
	 LOOP
		 v_VHID := VOUCHERHD_cursor.ID;
	 	 SELECT cgdate,fundsourceid INTO v_date,v_fs FROM VOUCHERHEADER WHERE ID= v_VHID;
		
		 
  		 INSERT INTO VOUCHERMIS (ID,VOUCHERHEADERID,CREATETIMESTAMP,FUNDSOURCEID)
  		  VALUES
		   (seq_VOUCHERMIS.NEXTVAL,v_VHID,v_date,v_fs);
		 
		 DBMS_OUTPUT.PUT_LINE ('Successfully inserted into Vouchermis for ->'||v_VHID);
	END LOOP;	
	
	DBMS_OUTPUT.PUT_LINE ('Finished!!');
END;
/

