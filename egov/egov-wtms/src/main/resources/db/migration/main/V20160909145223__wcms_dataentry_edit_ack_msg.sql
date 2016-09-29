delete from eg_roleaction where actionid =(select id from eg_action where name='dataeditentrymessage' and contextroot ='wtms');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='dataeditentrymessage' and contextroot ='wtms') );
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='dataeditentrymessage' and contextroot ='wtms'));
