 CREATE OR REPLACE PACKAGE emp_paymasters AS
  function get_token(the_list  varchar2,the_index number,delim  varchar2 := ',') return varchar2;
  function occurs(mainstring varchar2,stringtosearch varchar2) return number;
  PROCEDURE load_salarycodes;
  PROCEDURE CREATE_CONTROLCODE;
  PROCEDURE knn_emp_payscale;
  function createglcode(vglcode varchar2) return number;

END emp_paymasters;
/

CREATE OR REPLACE PACKAGE BODY emp_paymasters AS
 
 function createglcode(vglcode varchar2) return number as
 
 vglcodeid number default 0;
 vparentid number default 0;
 vskip number default 0;
 vdes varchar2(100);
 vtype varchar2(2);
 
 begin
 
 begin
 select id into vglcodeid from chartofaccounts where glcode=vglcode;
 if substr(vglcode,1,1)=1 then
 vtype:='I';
 elsif substr(vglcode,1,1)=2 then
 vtype:='E';
 elsif substr(vglcode,1,1)=3 then
 vtype:='L';
 elsif substr(vglcode,1,1)=4 then
 vtype:='A';
 end if;
 exception
 when no_data_found then
 select seq_chartofaccounts.nextval into vglcodeid from dual;
 begin
 select id into vparentid from chartofaccounts where glcode = substr(glcode,1,5);
 exception when no_data_found then
 vskip:=1;
 dbms_output.put_line('Parent not found in COA ->'||vglcode);
 end;
 if vskip=0 then
 select description into vdes from tmp_salarycodes where glcode=vglcode;
 insert into chartofaccounts (ID,GLCODE,NAME,DESCRIPTION,ISACTIVEFORPOSTING,PARENTID,ISACTIVE,MODIFIEDBY,CREATED,CLASSIFICATION,GROUPID,TYPE)
 values (vglcodeid,vglcode,vdes,vdes,1,vparentid,1,2,sysdate,4,1,vtype);
 end if;
 end;
 
 if vskip=1 then
 return (-1);
 else
 return (vglcodeid);
 end if;
 
 end createglcode;

FUNCTION occurs (mainstring VARCHAR2, stringtosearch VARCHAR2)
      RETURN NUMBER
   AS
      vct    NUMBER DEFAULT 0;
      vpos   NUMBER DEFAULT 0;
      temp   NUMBER DEFAULT 0;
   BEGIN
      vpos := NVL (INSTR (mainstring, stringtosearch, 1), 0);
      temp := vpos;

      WHILE vpos > 0
      LOOP
         vpos := NVL (INSTR (mainstring, stringtosearch, vpos + 1), 0);

         IF vpos != (temp + 1)
         THEN
            vct := vct + 1;
         END IF;

         temp := vpos;
      END LOOP;

      /* DBMS_OUTPUT.put_line (vct); */
      RETURN (vct);
   END occurs;
   
function get_token(
    the_list  varchar2,
    the_index number,
    delim     varchar2 := ','
 )
    return    varchar2
 is
    start_pos number;
    end_pos   number;
 begin
    if the_index = 1 then
        start_pos := 1;
    else
        start_pos := instr(the_list, delim, 1, the_index - 1);
        if start_pos = 0 then
            return null;
        else
            start_pos := start_pos + length(delim);
        end if;
    end if;
 
    end_pos := instr(the_list, delim, start_pos, 1);
 
    if end_pos = 0 then
        return substr(the_list, start_pos);
    else
        return substr(the_list, start_pos, end_pos - start_pos);
    end if;
 
 end get_token;
 
 PROCEDURE load_salarycodes
   AS
   ecode   NUMBER;
   emesg   VARCHAR2 (200);
   v_glcodeid NUMBER;
   v_basicid NUMBER;
   v_tdsid NUMBER;
   v_empparty_typeid NUMBER;
   
   CURSOR tmpSalCodes
         IS
            SELECT tmp.head as head, tmp.category as category, tmp.description as description, tmp.type as type, tmp.cal_type as cal_type, tmp.glcode as glcode, cat.id as categoryid, tmp.IS_RECOVERY as IS_RECOVERY
           FROM tmp_salarycodes tmp, egpay_category_master cat where tmp.category=cat.name order by tmp.id asc;
 BEGIN
 
 	 BEGIN
	      	SELECT id into v_empparty_typeid from eg_partytype where upper(code) like 'EMPLOYEE%' and parentid is null;
	      	EXCEPTION
	   		 WHEN NO_DATA_FOUND
	   		 THEN
	   		  DBMS_OUTPUT.put_line (   'No party type for Employee..Creating one' );
	   		  SELECT SEQ_EG_PARTYTYPE.nextval into v_empparty_typeid from dual;
	   		  INSERT INTO EG_PARTYTYPE (ID, CODE, PARENTID,DESCRIPTION, CREATEDBY, CREATEDDATE,LASTMODIFIEDBY, LASTMODIFIEDDATE) 
			  VALUES ( v_empparty_typeid, 'Employee',null , 'Employee', 1,sysdate , 1, sysdate);
	   
	END;
 
 	FOR tmpSal in tmpSalCodes LOOP
 		BEGIN
 		  v_glcodeid := null;
 		  SELECT ID into v_glcodeid from chartofaccounts where glcode like tmpSal.glcode;
 		  EXCEPTION
		       WHEN NO_DATA_FOUND
		       THEN
			  DBMS_OUTPUT.put_line (   'No matching glcodeid for'
						|| tmpSal.glcode
					       );
			
            	END; 
            	if v_glcodeid = null then
            		v_glcodeid:= createglcode(tmpSal.glcode);
            	end if;
 	
 		INSERT INTO EGPAY_SALARYCODES (ID, HEAD, CATEGORYID,CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, 
		   LASTMODIFIEDDATE, DESCRIPTION, IS_TAXABLE, CAL_TYPE, GLCODEID, PCT_BASIS, ORDER_ID, TDS_ID, LOCAL_LANG_DESC, 
		   INTEREST_GLCODEID, ISATTENDANCEBASED, ISRECOMPUTED, ISRECURRING) 
		VALUES (EISPAYROLL_SALARYCODES_SEQ.nextval ,tmpSal.head ,tmpSal.categoryid ,1 ,sysdate ,1 ,sysdate ,tmpSal.description , 'N',tmpSal.cal_type ,
			v_glcodeid, null,1  ,null ,tmpSal.description ,null,'Y' ,'Y' , 'Y');
 	
 		/* create recoveries for all isRecovery='Y' */
 		IF tmpSal.IS_RECOVERY = 'Y' and tmpSal.category like 'Deduction%' THEN
 			v_tdsid := null;
 			SELECT SEQ_tds.nextVal into v_tdsid from dual;
 			Insert into tds
			   (ID, TYPE, GLCODEID, ISACTIVE, CREATED, CREATEDBY, REMITTED, DESCRIPTION, PARTYTYPEID, ISEARNING)
			 Values
			   (v_tdsid, tmpSal.head, v_glcodeid, 1, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1,
			   'State Government', tmpSal.description, v_empparty_typeid, '0');
			
			UPDATE EGPAY_SALARYCODES
			SET TDS_ID = v_tdsid
			where UPPER(HEAD) like  upper(tmpSal.head);

 		END IF;
 	
 	END LOOP;
 	
 	SELECT id into v_basicid FROM EGPAY_SALARYCODES where UPPER(HEAD) LIKE 'BASIC';
 	
 	UPDATE EGPAY_SALARYCODES 
	 	SET ORDER_ID = id -(select min(sal.id) from egpay_salarycodes sal) + 1
	where id > 0;
 	
 	UPDATE EGPAY_SALARYCODES SET PCT_BASIS=v_basicid
 	where CATEGORYID=2 and CAL_TYPE='ComputedValue';

	/* create tds for HRA */
	Insert into tds
	   (ID, TYPE, GLCODEID, ISACTIVE, CREATED, CREATEDBY, REMITTED, DESCRIPTION, PARTYTYPEID, ISEARNING)
	 Values
	   (SEQ_tds.nextVal, 'HRA', (select id from chartofaccounts where glcode = 2101004), 1, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Self', 'HRA', (select id from eg_partytype where upper(code) = 'EMPLOYEE'), '1');

	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   0, 2549, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   2550, 3049, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 380);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   3050, 3799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 455);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   3800, 4549, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 570);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   4550, 5299, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 680);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   5300, 6049, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 795);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   6050, 6799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 905);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   6800, 7549, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1020);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   7500, 8299, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1130);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   8300, 9049, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1245);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   9050, 9799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1355);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   9800, 10799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1470);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   10800, 11799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1620);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   11800, 12799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1770);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   12800, 13799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1920);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   13800, 14799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2070);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   14800, 15799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2220);	
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   15800, 16799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2370);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   16800, 17799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2520);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   17800, 18799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2670);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   18800, 19799, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2820);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
	   19800, 21299, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2970);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='HRA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 
	   21300, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 3120);

	UPDATE EGPAY_SALARYCODES
	SET TDS_ID = (select id from tds where type='HRA'),
	CAL_TYPE = 'SlabBased'
	where UPPER(HEAD) LIKE 'HRA';
 	
 	/* create tds for cca */
	Insert into tds
	   (ID, TYPE, GLCODEID, ISACTIVE, CREATED, CREATEDBY, REMITTED, DESCRIPTION, PARTYTYPEID, ISEARNING)
	 Values
	   (SEQ_tds.nextVal, 'CCA', (select id from chartofaccounts where glcode = 2101004), 1, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'Self', 'CCA', (select id from eg_partytype where upper(code) = 'EMPLOYEE'), '1');

	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 2549, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2550, 2999, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 55);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 3000, 5499, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 80);
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, HIGHLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 5500, 7999, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 120);	
	Insert into eg_deduction_details
	   (ID, TDSID, DATEFROM, DATETO, LOWLIMIT, LASTMODIFIEDDATE, AMOUNT)
	 Values
	   (SEQ_EG_DEDUCTION_DETAILS.nextVal, (select id from tds where type='CCA'), TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('04/01/2090 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 8000, TO_DATE('10/17/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 150);

	UPDATE EGPAY_SALARYCODES
	SET TDS_ID = (select id from tds where type='CCA'),
	CAL_TYPE = 'SlabBased'
	where UPPER(HEAD) LIKE 'CCA';

 END load_salarycodes;
 
 
procedure CREATE_CONTROLCODE AS
 
 cursor salarycodes is select egps.glcodeid from egpay_salarycodes egps, egpay_category_master ecm
 where egps.categoryid=ecm.id and ecm.cat_type='D';
 
 coadetid number default 0;
 dettypeid number default 0;
 vinsert number default 0;
 
 begin
 
 select id into dettypeid from accountdetailtype where upper(name)=upper('Employee');
 
 for scs in salarycodes loop
 vinsert:=0;
 begin
 select id into coadetid from chartofaccountdetail where glcodeid=scs.glcodeid and detailtypeid = dettypeid;
 exception when no_data_found then
 vinsert:=1;
 dbms_output.put_line('Inserting ->'||scs.glcodeid);
 end;
 
 if vinsert=1 then
 insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID,ISCONTROLCODE)
 values
 (seq_chartofaccountdetail.nextval,scs.glcodeid,dettypeid,1);
 end if;
 
 end loop;
 /* creating for salary payable */
 
  begin
  select id into coadetid from chartofaccountdetail where glcodeid=(select id from chartofaccounts where name like 'Salary Payable') and detailtypeid = dettypeid;
  exception when no_data_found then
	  insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID,ISCONTROLCODE)
	    values
	  (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where name like 'Salary Payable'),dettypeid,1);
  	dbms_output.put_line('Inserting -> Salary Payable');
  end;
  
 
 end CREATE_CONTROLCODE;


 
 procedure knn_emp_payscale as
 
 egpscaleid number default 0;
 v_basicid NUMBER;
 v_daid NUMBER;
 v_sca_from_amount NUMBER;
 v_sca_to_amount NUMBER;
 v_incr_amount NUMBER;
 vcount NUMBER;
 i NUMBER;
 cursor payscale is select distinct scale from knn_payroll;
 
 begin
 SELECT id into v_basicid FROM EGPAY_SALARYCODES where UPPER(HEAD) LIKE 'BASIC';
 SELECT id into v_daid FROM EGPAY_SALARYCODES where UPPER(HEAD) LIKE 'DA';
 for pscl in payscale loop
 
 begin
 select id into egpscaleid from egpay_payscale_header where name=pscl.scale;
 exception
 when no_data_found then
 	select seq_payheader.nextval into egpscaleid from dual;
 	dbms_output.put_line('scale ->'|| pscl.scale);
 	vcount := occurs (pscl.scale, '-');
 	IF vcount >= 2 THEN
 		v_sca_from_amount := get_token(pscl.scale,1,'-');
 		v_sca_to_amount := get_token(pscl.scale,vcount+1,'-');
 		v_incr_amount := get_token(pscl.scale,2,'-');
 	ELSE
 		v_sca_from_amount := get_token(pscl.scale,1,'-');
		 v_incr_amount := 0;
 		v_sca_to_amount := get_token(pscl.scale,2,'-');
 	END IF;
 	--v_sca_from_amount := substr(pscl.scale,1,instr(pscl.scale,'-',1,1)-1);
 	--v_sca_to_amount := substr(pscl.scale,instr(pscl.scale,'-',1,1)+1,len(pscl.scale));
 	dbms_output.put_line('v_sca_from_amount for scale ->'||v_sca_from_amount || ' for ' || pscl.scale);
 	dbms_output.put_line('v_sca_to_amount ->'||v_sca_to_amount || ' for ' || pscl.scale);
 	dbms_output.put_line('v_incr_amount for scale ->'||v_incr_amount || ' for ' || pscl.scale);
 	INSERT INTO egpay_payscale_header
 		(ID,NAME,PAYCOMMISION,EFFECTIVEFROM,AMOUNTFROM,AMOUNTTO)
 	VALUES
 		(egpscaleid,pscl.scale,'',to_date('01-Apr-2008'),v_sca_from_amount,v_sca_to_amount);
 	
 	/* INSERT into increment */ 
 	/* need to insert subslabs if that information is in the payscale, for instance 2500-30-3500-60-4500  */
 	IF vcount <= 2 THEN
		INSERT INTO EGPAY_PAYSCALE_INCRDETAILS
		    (ID,incSlabAmt,incSlabFrmAmt,incSlabToAmt,ID_PAYHEADER)			    
		    VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,v_incr_amount,v_sca_from_amount,v_sca_to_amount,egpscaleid);
	ELSE
		i:=1;
		LOOP
		 v_sca_from_amount := get_token(pscl.scale,i,'-');
		 v_incr_amount := get_token(pscl.scale,i+1,'-');
 		 v_sca_to_amount := get_token(pscl.scale,i+2,'-');
 		 IF i > 1 THEN
 		 	v_sca_from_amount := v_sca_from_amount +1;
 		 END IF;
		 dbms_output.put_line(i || ' v_sca_from_amount in incr slab ->'||v_sca_from_amount );
		 dbms_output.put_line(i || ' v_incr_amount in incr slab ->'||v_incr_amount );
		 dbms_output.put_line(i || ' v_sca_to_amount in incr slab ->'||v_sca_to_amount );
		 INSERT INTO EGPAY_PAYSCALE_INCRDETAILS
		 		    (ID,incSlabAmt,incSlabFrmAmt,incSlabToAmt,ID_PAYHEADER)			    
		    VALUES (SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,v_incr_amount,v_sca_from_amount,v_sca_to_amount,egpscaleid);
		 i := i+2;
		 EXIT WHEN i>vcount;
		
		END LOOP;
	
	END IF;
 
 	/* INSERT BASIC */
 	INSERT INTO egpay_payscale_details
 		(ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
 		VALUES (SEQ_PAYHEADER_DETAILS.nextval,egpscaleid,v_basicid,0,v_sca_from_amount);
 	
 	/* INSERT DA */
	INSERT INTO egpay_payscale_details
		(ID,ID_PAYHEADER,ID_SALARYCODES,PCT,AMOUNT)
	VALUES (SEQ_PAYHEADER_DETAILS.nextval,egpscaleid,v_daid,70.5,(v_sca_from_amount*70.5)/100);
 
 end;
 
 end loop;
 
end knn_emp_payscale;
   
   END emp_paymasters;
   /