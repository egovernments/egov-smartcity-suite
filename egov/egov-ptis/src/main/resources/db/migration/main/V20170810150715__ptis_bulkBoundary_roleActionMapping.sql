UPDATE eg_action SET enabled=false WHERE name='bulkBoundaryUpdation-Search';

INSERT into EGPT_STATUS (id,status_name,created_date,is_active,code)
VALUES
(nextval('SEQ_EGPT_STATUS'),"Bulk Boundary Updation",now(),'Y',"BULK BOUNDARY UPDATION");
