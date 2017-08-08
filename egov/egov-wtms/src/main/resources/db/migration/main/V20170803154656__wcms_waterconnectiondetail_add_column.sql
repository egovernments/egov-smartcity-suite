ALTER TABLE egwtr_connectiondetails  ADD COLUMN closurefilestoreid bigint;
ALTER TABLE egwtr_connectiondetails  ADD COLUMN reconnectionfilestoreid bigint;

UPDATE eg_wf_matrix SET nextaction ='Acknowledgemnt pending' where currentdesignation  ='Commissioner' and additionalrule='CLOSECONNECTION' and currentstate='Closure Approved By Commissioner' and objecttype='WaterConnectionDetails';

