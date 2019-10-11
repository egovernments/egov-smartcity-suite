-----Application Type COURTVERDICT
INSERT into EGPT_APPLICATION_TYPE (id,code,name,resolutiontime,description,createddate,lastmodifieddate,createdby,lastmodifiedby,version)
 values (nextval('seq_egpt_application_type'),'COURTVERDICT','COURT VERDICT',365,'COURT VERDICT',now(),null,1,null,null);

-----Update For Write Off Application Type
UPDATE EGPT_APPLICATION_TYPE SET resolutiontime =365 where code = 'WRITEOFF';
