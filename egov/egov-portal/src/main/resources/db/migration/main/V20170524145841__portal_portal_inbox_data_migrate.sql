alter table egp_inbox add column userid bigint;

INSERT INTO egp_inbox(
            id, moduleid, servicetype, applicationnumber,  
            headermessage, detailedmessage, link, read,  
            applicationdate,  state_id, status, createdby, createddate, 
            lastmodifieddate, lastmodifiedby, version, priority, 
            userid,resolved,resolveddate) select nextval('seq_egp_inbox'),module_id,'Complaint',identifier,header_msg
            ,detailed_msg,link,read,msg_date,state_id,status,createdby,createddate,lastmodifieddate
            ,lastmodifiedby,version, priority,assigned_to_user,case when status in ('COMPLETED','REJECTED','WITHDRAWN') then true else false end,case when status in ('COMPLETED','REJECTED','WITHDRAWN') then lastmodifieddate else null end
             from egp_citizeninbox  where id in (select max(id) from egp_citizeninbox  group by identifier);

INSERT INTO egp_inboxusers(
            id, userid, portalinbox)
    select nextval('SEQ_EGP_INBOXUSERS'),userid,id from egp_inbox;

alter table egp_inbox drop column userid;