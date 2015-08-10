
update egeis_assignment set designation=
 (select id from eg_designation where name='Assistant engineer') ,
 department =(select id from eg_department where name='Revenue') where position in(select id from eg_position where 
 name='R-ASSISTANT-3');
 
ALTER TABLE egwtr_connectiondetails ADD COLUMN statusid bigint;

ALTER TABLE egwtr_connectiondetails ADD  CONSTRAINT fk_conndtls_status FOREIGN KEY (statusid)
      REFERENCES egw_status (id);



update  egwtr_connectiondetails set statusid =(select id from egw_status  where moduletype='WATERTAXAPPLICATION' and code='Approved') 
where statusid is null and connection in (select id from egwtr_connection where consumercode is not null);

update  egwtr_connectiondetails set statusid =(select id from egw_status  where moduletype='WATERTAXAPPLICATION' and
 code='Created') 
where statusid is null and connection in (select id from egwtr_connection where consumercode is  null);


ALTER TABLE egwtr_connectiondetails ALTER COLUMN statusid SET NOT NULL;