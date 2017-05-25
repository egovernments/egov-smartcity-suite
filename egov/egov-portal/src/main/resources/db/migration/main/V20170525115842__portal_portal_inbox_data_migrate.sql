delete from egp_inboxusers;
delete from egp_inbox;

INSERT INTO egp_inbox(
            id, moduleid, servicetype, applicationnumber  
            ,headermessage, detailedmessage, link, read  
            ,applicationdate,  state_id, status, createdby, createddate 
            ,lastmodifieddate, lastmodifiedby, version, priority 
            ,resolved,resolveddate) select nextval('seq_egp_inbox'),module_id,'Complaint',identifier,header_msg
            ,detailed_msg,link,read,msg_date,state_id,status,createdby,createddate,lastmodifieddate
            ,lastmodifiedby,version, priority,case when status in ('COMPLETED','REJECTED','WITHDRAWN') then true else false end,case when status in ('COMPLETED','REJECTED','WITHDRAWN') then lastmodifieddate else null end
             from egp_citizeninbox  where id in (select max(id) from egp_citizeninbox  where createdby in (select id from eg_user where type = 'CITIZEN') group by identifier); 

INSERT INTO egp_inboxusers(
            id, userid, portalinbox)
    select nextval('SEQ_EGP_INBOXUSERS'),createdby,id from egp_inbox;
