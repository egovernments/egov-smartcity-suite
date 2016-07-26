delete from egw_status where moduletype='LCMS' and code='Hearing';


----Insert into egw status for Hearing
Insert into egw_status (id,moduletype,description,lastmodifieddate,code,order_id )
values(nextval('seq_egw_status'),'Legal Case','Created Hearing',now(),'IN PROGRESS',3);