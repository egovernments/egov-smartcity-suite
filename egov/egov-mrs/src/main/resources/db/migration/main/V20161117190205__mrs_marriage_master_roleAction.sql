-----------------------------------------------------------ADDING role actions-------------------------------------------------------------
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) 
select (select id from eg_role where name='Marriage Registration Admin'), id from eg_action where name in ('CreateReligion','Search And View Religion','Religion Success Result','Religion Search Result','Search And Show Edit  Religion','Edit Religion','Update Religion','Create Act','Search And View Act','Act Success Result','Act Search Result','Search And Show Edit  Act','Edit Act','Update Act','Search And Show Marriage Registration Results','Update Marriage Registration Record');

INSERT into EG_ROLEACTION(ROLEID,ACTIONID)
select (select id from eg_role where name='Marriage Registration Creator'), id from eg_action where name in ('View Marriage Registration','SearchRegistration','Search Registration Certificates','Print Marriage Certificates');
