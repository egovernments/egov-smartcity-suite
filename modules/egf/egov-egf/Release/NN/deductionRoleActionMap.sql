create or replace procedure actionRoleMapForULBADMIN
is
roleid number;
begin
	 select id_role into roleid  from eg_roles where 	upper(role_name)='ULB ADMIN';

		
	INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,265); 
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,266); 
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,267);
	
	INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,268);
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,269);
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,270);
    
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,271);
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,272);
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,273);
    
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,274);
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,275);
    INSERT INTO EG_ROLEACTION_MAP VALUES(roleid,276);
DBMS_OUTPUT.PUT_LINE(' PROCEDURE CREATED');
	commit;
end;

/

exec actionRoleMapForULBADMIN;
select 'procedure executed' from dual;
exit;