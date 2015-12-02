----------------------START----------------------------
create or replace FUNCTION getAadharno(v_basicpropid IN BIGINT)  
RETURNS VARCHAR  as  $$
declare
	v_owneraadhar VARCHAR(3072);  
	owners eg_user%ROWTYPE;
BEGIN  
	for owners in (select u.* from egpt_property_owner_info po, eg_user u where po.owner=u.id and po.basicproperty = v_basicpropid)
	loop 
		begin
			IF v_owneraadhar <> '' THEN
			v_owneraadhar := v_owneraadhar || ',' || owners.aadhaarnumber;
			ELSE
			v_owneraadhar := owners.aadhaarnumber;
			END IF;
		EXCEPTION
		WHEN NO_DATA_FOUND THEN
		NULL;   
		END;
	END LOOP;
	return V_owneraadhar;   
END; 
$$ LANGUAGE plpgsql;
----------------------END----------------------------
