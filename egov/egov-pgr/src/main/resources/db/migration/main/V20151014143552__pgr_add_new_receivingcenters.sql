update egpgr_receiving_center set orderno  =6 where name='Commissioner Office';
update egpgr_receiving_center set orderno  =7 where name='Zonal Office';
update egpgr_receiving_center set orderno  =8 where name='Complaint Cell';
update egpgr_receiving_center set orderno  =9 where name='Field visits';
update egpgr_receiving_center set orderno  =10 where name='Adverse Items(Paper/news)';

INSERT INTO egpgr_receiving_center (id, name, iscrnrequired, orderno, version) VALUES (9,'Office of CDMA',true,4,0);
INSERT INTO egpgr_receiving_center (id, name, iscrnrequired, orderno, version) VALUES (10,'Office of RDMA',true,5,0);
