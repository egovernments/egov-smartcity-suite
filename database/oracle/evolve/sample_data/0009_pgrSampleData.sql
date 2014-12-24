#UP


-- Deleted Start status

INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
2, 'REGISTERED'); 
INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
3, 'ASSIGNED'); 
INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
4, 'PROCESSING'); 
INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
5, 'COMPLETED'); 
INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
6, 'REJECTED'); 
INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
7, 'NOTCOMPLETED'); 
INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
8, 'WITHDRAWN'); 
INSERT INTO EGGR_COMPLAINTSTATUS ( COMPLAINTSTATUSID, STATUSNAME ) VALUES ( 
9, 'REOPENED'); 


INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'GRIEVANCE_OFFICER'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REGISTERED'), 2); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'GRIEVANCE_OFFICER'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'ASSIGNED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'GRIEVANCE_OFFICER'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'PROCESSING'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'GRIEVANCE_OFFICER'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'NOTCOMPLETED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'GRIEVANCE_OFFICER'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REOPENED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'GRIEVANCE_OFFICER'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REJECTED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'GRIEVANCE_OFFICER'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'COMPLETED'), 3); 

INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_Officer'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REGISTERED'), 2); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_Officer'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'ASSIGNED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_Officer'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'PROCESSING'), 3);  
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_Officer'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'NOTCOMPLETED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_Officer'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REOPENED'), 3);
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_Officer'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REJECTED'), 3);
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_Officer'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'COMPLETED'), 3); 

INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'COMPLAINANT'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REGISTERED'), 2); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'COMPLAINANT'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REOPENED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'COMPLAINANT'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'WITHDRAWN'), 3);

INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_ADMINISTRATOR'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REGISTERED'), 2); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_ADMINISTRATOR'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'ASSIGNED'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_ADMINISTRATOR'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'PROCESSING'), 3); 
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_ADMINISTRATOR'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'NOTCOMPLETED'), 3);  
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_ADMINISTRATOR'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REOPENED'), 3);
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_ADMINISTRATOR'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'REJECTED'), 3);
INSERT INTO EGGR_STATUS_ROLES ( ROLE, STATUS, STATE_ORDER ) VALUES ( 
(select ID_ROLE from eg_roles where ROLE_NAME like 'PGR_ADMINISTRATOR'), (SELECT COMPLAINTSTATUSID FROM EGGR_COMPLAINTSTATUS WHERE STATUSNAME LIKE 'COMPLETED'), 3); 
 
UPDATE eggr_complaintstatus set  STATUSNAME='FORWARDED' WHERE STATUSNAME LIKE 'ASSIGNED';


Insert into eggr_complaintgroup
   (ID_COMPLAINTGROUP, COMPLAINTGROUPNAME)
 Values
   (seq_complaintgroup.nextVal, 'Health');
Insert into eggr_complaintgroup
   (ID_COMPLAINTGROUP, COMPLAINTGROUPNAME)
 Values
   (seq_complaintgroup.nextVal, 'General');
Insert into eggr_complaintgroup
   (ID_COMPLAINTGROUP, COMPLAINTGROUPNAME)
 Values
   (seq_complaintgroup.nextVal, 'Engineering');
Insert into eggr_complaintgroup
   (ID_COMPLAINTGROUP, COMPLAINTGROUPNAME)
 Values
   (seq_complaintgroup.nextVal, 'Revenue');
Insert into eggr_complaintgroup
   (ID_COMPLAINTGROUP, COMPLAINTGROUPNAME)
 Values
   (seq_complaintgroup.nextVal, 'Solid Waste Management');

--eggr_complainttypes
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Removal of garbage', 'F¥ig mf¿whik', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Over flowing of garbage bins ', 'F¥ig¤ bjh£ofë¿ F¥ig ãu«Ã têj¿', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Shifting of garbage bin', 'F¥il¤ bjh£oik mf¿Wj¿ / Ïl« kh¿Wj¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Provision of garbage bin', 'F¥ig¤ bjh£o mik¤j¿', 3, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Absenteesim of sweepers', 'J¥òuÎ gâahs®fY gâ¡F tuhik', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Absenteesim of door to door garbage collector', 'ÅL Ålhf F¥ig nrfç¡F« gâahs®fY gâ¡F tuhik', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Removal of Debris', 'f£ol ÏoghL¡s mf¿Wj¿', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Nuisance by garbage tractors or trucks', 'F¥ig thfd§fY jU« Ï¿d¿fY', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Bio Medical waste/Health hazard waste removal', 'cæça¿ kU¤Jt¡fêÎ k¿W« Rfhj`hu¢ Ó®nf£L¡fêÎ mf¿Wj¿', 3, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Obstruction of Trees', 'ku§fëdh¿ V¿g£l jil r«gªjkhd òfh®fY', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding burial ground', 'RLfhL / ÏLfhL r«gªjkhd òfh®fY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding public toilets', 'bghJ¡ fê¥Ãl§fY r«gªjkhd òfh®fY mDkÂa¿w cztf§fY r«gªjkhd òfh®fY ', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding unauthorised restaurants', 'mDkÂa¿w cztf§fY r«gªjkh òfh®fY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding  Dispensary ', 'kUªjf§fY r«gªjkhd òfh®fY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 7, 'Complaints regarding CWC', 'Fhªij ey ika« r«gªjkhd òfh®fY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding CDH', 'bjh¿W neh¿ kU¤Jtkid r«gªjkhd òfh®fY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Public Health/Dengue/Malaria/Gastro-enteritis', 'bghJ Rfhjhu« / bl§F / knyçah /             r«gªjkhd òfh®fY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Mosquito menace ', 'bfhR¤ bjh¿i', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Dog menace ', 'bjU eh¿¤ bjh¿iy', 5, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Burning of garbage', 'F¥ig Ts§fis vç¤j¿', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Issue of Birth and Death Certificate', 'Ãw¥ò Ïw¥ò rh¿Â¿fY tH§FtJ r«gªjkhd òfh®fY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Unsanitary conditions on the road', 'rhiyfë¿ cYs Rfhjhu¡ nfLfY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Slaughter House', 'Ïiw¢Á¡fhf fh¿eilfis tu« br¿Í« Ïl« r«gªjkhd òfh®', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Unauthorised sale of meat and meat product', 'mDkÂa¿w Ïiw¢Á¡ Tl§fY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Stray cattle', 'j¿å¢irahf¤ ÂçÍ« fh¿eilfY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding  Cleanliness of Toilets in Theatre', 'Âiuau§fë¿ cYs fêtiwfis R¤jkhf it¤ÂU¥gJ r«gªjkhd òfh®fY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding  Cleanliness of Toilets in Shopping complex', 'tâf tshf§fë¿ fêtiwfis R¤jkhf ik¤ÂU¥gJ r«ge¤khd òfh®fY', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints regarding quality of food in Hotels', 'cztf¡ë¿ é¿f¥gL« czéfë¿ ju« r«gªjkhd òfh®fY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Illegal slaughtering', 'r£l énuhjkhf fh¿eilfis tj« br¿j¿', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Stray Pigs', 'j¿å¢irahf ÂçÍ« g¿¿fY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Death of Stray Animals', 'jYå¢irahf ÂçÍ« fh¿eilfY Ïw¥ò r«gªjkhd òfh®fY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Road Side Eateries', 'rhiynahu cztf§fY r«gªjkhd òfh®fY', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Unhygienic and improper transport of meat and livestock', 'Rfhjhuk¿w Kiwæ¿ Ïiw¢Á k¿W« fh¿eilfis bfh©L br¿Yj¿', 3, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Improper Sweeping', 'rçahf J¥òuÎ br¿ahik', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Broken Bin', 'cilªj F¥ig bjh£o', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Garbage lorry with out Net', 'F¥igyhç tiyæ¿yhk¿ F¥ig vL¥gJ', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Transfer Station Smell', 'F¥ig bfh £o ÂU«g vL¡F« Ïl¤ÂèUªJ nkhrkhd J®eh¿w«', 1, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Spilling of Garbage from lorry', 'F¥igfY yhçæ¿ vL¤J br¿Y«bghGJ têæ¿ éGtJ', 1, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 17, 'Burning of garbage at Kodungaiyur Dumping Ground', 'F¥igfis bfhL§ifô® F¥igbfh£L« Ïl¤Â¿ vç¤j¿', 7, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 17, 'Burning of garbage at Perungudi Dumping Ground', 'F¥igfis bgU§Fo F¥igbfh£L« Ïl¤Â¿ vç¤j¿', 7, 5);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 17, 'Flies menace at Kodungaiyur Dumping Ground', 'ó¢ÁfY bjhªjuÎ bfhL§ifô®', 7, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 17, 'Flies menace at Perungudi Dumping Ground', 'ó¢ÁfY bjhªjuÎ bgU§Fo', 7, 1);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 16, 'Complaints related to property tax ', 'brh¤J tç r«gªjkhd òfh®fY', 7, 4);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 11, 'Complaints related to issue of all types of license ', 'v¿yhéjkhd cçk§fY tH§FtJ r«gªjkhd òfh®fY', 3, 4);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 16, 'Complaints regarding Voter list', 'th¡fhs® g£oa¿ r«gªjkhd òfh®fY', 3, 4);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 16, 'Inclusion, delection of correction in the Voter list', 'th¡fhs® g£oaè¿ bga® nr®¤j¿, Ú¡Fj¿ k¿W« ÃiH¤ÂU¤j« br¿j¿ r«gªjkhd òfh®fY', 7, 4);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 8, 'Complaints regarding Schools', 'gYëfY g¿¿a òfh®fY', 3, 2);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 7, 'Sanction of financial assistance under Moovalur thirumana thittam', '_tÿ® Ïuhkhä®j« Âukz cjé¤Â£l¤Â¿ _y« ãÂcjé tH§Fj¿ g¿¿a òfh®fY', 3, 2);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Issue of Colour TV', 't©z¤bjhiy¡ fh£Á bg£o tH§Fj¿', 3, 2);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Issue of Voter ID', 'th¡fhs® milahs m£il tH§Fj¿', 3, 2);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Unauthorised Advt. Boards', 'mDkÂa¿w és«gu« gyiffY', 3, 2);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 9, 'Non Burning of Street Lights', 'bjU és¡F vçahik', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 9, 'Shifting of Street light Pole', 'bjU és¡F f«g« mf¿Wj¿ / Ïl« kh¿Wj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 9, 'New Street light', 'òÂa bjU és¡F nt©Lj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 9, 'Electric Shock due to street light', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 9, 'Damage to the  Electric Pole', 'bjU f«g« gGJ gh®¤j¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Request to relay the Road', 'rhiy Ûs¥nghLj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Pot hole fill up/Repairs to the damage surface', 'rhiy gYs§fY rçbr¿j¿ / rhiy gGJ gh®¤j¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Formation of New Road', 'òÂa rhiy mik¤j¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Request to provide footpath', 'eil ghij nt©Lj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Repairs to existing footpath', 'eil ghij gGJ gh®¤j¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Removal of shops in the footpath', 'eilghij¡ filfis mf¿Wj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 18, 'New Drain Construction', 'òÂjhf kiHÚ® tHfh¿th¿ f£Lj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Repairs to the SWD', 'kiHÚ® tofh¿th¿ gGJ gh®¤j¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Desilting of Drain', 'kiHÚ® tofh¿th¿ ö® thUj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Desilting of Canal', 'fh¿th¿ jh® thUj¿', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Obstruction of water flow', 'kiHÚ® toa jL¥ò mf¿Wj¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Cleaning of water table', 'rhiy Xu kiHÚ® XL ghij R¤j« br¿j¿', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Stagnation of water', 'kiHÚ® nj¡f«', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Disposal of removed silt on the Road', 'rhiynahu¤Â¿ Fé¡f¥g£oU¡F« ö® /  t©l¿k© mf¿Wj¿', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Removal of fallen trees', 'ÑnH éHªJYs ku« / ku¡»isfY mf¿Wj¿', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Covering of Man holes of SWD', 'kiHÚ® tofh¿th¿ kåj JiHthæ¿ _Lj¿', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Unauthorised / Illegal construction', 'mDkÂa¿w k¿W« r£l¤Â¿F òw«ghd f£Lkhd¥gâ', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Violation of DCR/Building bye laws', 'ts®¢Á éÂ k¿W« f£oa éÂ Ûw¿fY', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Encroachment on the public property', 'bghJ¢ brh¤J¡ë¿ M¡»uä¥ò', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Illegal draining of sewage to SWD/Open site', 'fêÎ Úiu kiHã® tofh¿thæ¿ éLj¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 16, 'Complaints regarding community hall', 'rKjha¡ Tl« r«gªjkhd òfh®fY', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Complaints regarding Park', 'ó§fh¡fY r«gªjkhd òfh®fY', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Complaints regarding Play grounds', 'éisah£L ikjhd§fY r«gªjkhd òfh®fY', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Complaints regarding any other COC Buildings', 'br¿id khefuh£Áæ¿ Ïju f£ol§fY r«gªjkhd òfh®fY', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, DEPTID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 2, 'Complaints regarding Bridges/Flyovers/Subways', 'ghy§fY / nk«ghy§fY / Ñ¿¥ghy§fY r«gªjkhd òfh®fY', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Complaints regarding Centre Median', 'rhiy ika¤jL¥òfY r«gªjkhd òfh®fY', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Complaints regarding Traffic Island', 'ngh¡Ftu¤J  ÔÎfY r«gªjkhd òfh®fY', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Unauthorised tree Cutting', 'mDkÂæ¿¿ ku« bt£Lj¿', 1, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Open Air Defunction', 'Âwªj btëæ¿ ky#y« fê¤j¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Parking Issue', 'thfd§fY ãW¤jtJ r«gªkhd òfh®fY', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Slow progress of work', 'bkJthf ntiyfY br¿j¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Poor quality of work', 'juk¿w ntiyfY br¿j¿', 3, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Building plan sanction', 'f£ol tiugl mDkÂ', 7, 3);
Insert into eggr_complainttypes
   (COMPLAINTTYPEID, COMPLAINTTYPENAME, COMPLAINTTYPENAME_LOCAL, NOOFDAYS, COMPLAINTGROUP_ID)
 Values
   (seq_complainttype.nextVal, 'Over head cable Wires running in Hapazard manner', 'xG§f¿w Kiwæ¿ f«Ã to§fY f«g§fY _y« Ïiz¤j¿', 3, 3);


--designation
Insert into eg_designation
   (DESIGNATIONID, DESIGNATION_NAME, DESIGNATION_DESCRIPTION)
 Values
   (SEQ_DESIGNATION.nextVal, 'EXECUTIVE ENGINEER', '60');

Insert into eg_designation
   (DESIGNATIONID, DESIGNATION_NAME, DESIGNATION_DESCRIPTION)
 Values
   (SEQ_DESIGNATION.nextVal, 'ASSISTANT HEALTH OFFICER', '92');

   Insert into eg_designation
   (DESIGNATIONID, DESIGNATION_NAME, DESIGNATION_DESCRIPTION)
 Values
   (SEQ_DESIGNATION.nextVal, 'ASSISTANT EXECUTIVE ENGINEER', '61');
  
      Insert into eg_designation
   (DESIGNATIONID, DESIGNATION_NAME, DESIGNATION_DESCRIPTION)
 Values
   (SEQ_DESIGNATION.nextVal, 'ASSISTANT REVENUE OFFICER', '88');
   
    Insert into eg_designation
   (DESIGNATIONID, DESIGNATION_NAME, DESIGNATION_DESCRIPTION)
 Values
   (SEQ_DESIGNATION.nextVal, 'Zonal Officer', '501');

  Insert into eg_designation
   (DESIGNATIONID, DESIGNATION_NAME, DESIGNATION_DESCRIPTION)
 Values
   (SEQ_DESIGNATION.nextVal, 'Commissioner', '500');
   
   --position
   INSERT INTO eg_position
      (POSITION_NAME, ID, DESIG_ID, EFFECTIVE_DATE)
    VALUES
      ('EXECUTIVE ENGINEER', SEQ_POS.NEXTVAL, (select  DESIGNATIONID from eg_designation where designation_name='EXECUTIVE ENGINEER'), TO_DATE('02/13/2009 14:19:24', 'MM/DD/YYYY HH24:MI:SS'));
   
     INSERT INTO eg_position
      (POSITION_NAME, ID, DESIG_ID, EFFECTIVE_DATE)
    VALUES
      ('ASSISTANT HEALTH OFFICER', SEQ_POS.NEXTVAL, (select  DESIGNATIONID from eg_designation where designation_name='ASSISTANT HEALTH OFFICER'), TO_DATE('02/13/2009 14:19:24', 'MM/DD/YYYY HH24:MI:SS'));
   
    INSERT INTO eg_position
      (POSITION_NAME, ID, DESIG_ID, EFFECTIVE_DATE)
    VALUES
      ('ASSISTANT EXECUTIVE ENGINEER', SEQ_POS.NEXTVAL, (select  DESIGNATIONID from eg_designation where designation_name='ASSISTANT EXECUTIVE ENGINEER'), TO_DATE('02/13/2009 14:19:24', 'MM/DD/YYYY HH24:MI:SS'));
   
    INSERT INTO eg_position
      (POSITION_NAME, ID, DESIG_ID, EFFECTIVE_DATE)
    VALUES
      ('ASSISTANT REVENUE OFFICER', SEQ_POS.NEXTVAL, (select  DESIGNATIONID from eg_designation where designation_name='ASSISTANT REVENUE OFFICER'), TO_DATE('02/13/2009 14:19:24', 'MM/DD/YYYY HH24:MI:SS'));
   
    INSERT INTO eg_position
      (POSITION_NAME, ID, DESIG_ID, EFFECTIVE_DATE)
    VALUES
      ('Zonal Officer', SEQ_POS.NEXTVAL, (select  DESIGNATIONID from eg_designation where designation_name='Zonal Officer'), TO_DATE('02/13/2009 14:19:24', 'MM/DD/YYYY HH24:MI:SS'));
   
    INSERT INTO eg_position
      (POSITION_NAME, ID, DESIG_ID, EFFECTIVE_DATE)
    VALUES
      ('Commissioner', SEQ_POS.NEXTVAL, (select  DESIGNATIONID from eg_designation where designation_name='Commissioner'), TO_DATE('02/13/2009 14:19:24', 'MM/DD/YYYY HH24:MI:SS'));

-----user
Insert into eg_user
   (ID_USER, FIRST_NAME, USER_NAME, PWD, UPDATETIME, EXTRAFIELD2, IS_SUSPENDED, ID_TOP_BNDRY, ISACTIVE, FROMDATE, TODATE)
 Values
   (seq_eg_user.nextVal, 'AEEU6', 'AEEU6', '-640vq9e24s79b', TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), ' ', 'N', 1, 1, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2999 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

Insert into eg_user
   (ID_USER, FIRST_NAME, USER_NAME, PWD, UPDATETIME, EXTRAFIELD2, IS_SUSPENDED, ID_TOP_BNDRY, ISACTIVE, FROMDATE, TODATE)
 Values
   (seq_eg_user.nextVal, 'EE1Z2', 'EE1Z2', '-640vq9e24s79b', TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), ' ', 'N', 1, 1, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2999 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));


Insert into eg_user
   (ID_USER, FIRST_NAME, USER_NAME, PWD, UPDATETIME, EXTRAFIELD2, IS_SUSPENDED, ID_TOP_BNDRY, ISACTIVE, FROMDATE, TODATE)
 Values
   (seq_eg_user.nextVal, 'AHO2', 'AHO2', '-640vq9e24s79b', TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), ' ', 'N', 1, 1, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2999 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

Insert into eg_user
   (ID_USER, FIRST_NAME, USER_NAME, PWD, UPDATETIME, EXTRAFIELD2, IS_SUSPENDED, ID_TOP_BNDRY, ISACTIVE, FROMDATE, TODATE)
 Values
   (seq_eg_user.nextVal, 'ARO2', 'ARO2', '-640vq9e24s79b', TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), ' ', 'N', 1, 1, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2999 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

Insert into eg_user
   (ID_USER, FIRST_NAME, USER_NAME, PWD, UPDATETIME, EXTRAFIELD2, IS_SUSPENDED, ID_TOP_BNDRY, ISACTIVE, FROMDATE, TODATE)
 Values
   (seq_eg_user.nextVal, 'ZOZ2', 'ZOZ2', '-640vq9e24s79b', TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), ' ', 'N', 1, 1, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2999 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));


Insert into eg_user
   (ID_USER, FIRST_NAME, USER_NAME, PWD, UPDATETIME, EXTRAFIELD2, IS_SUSPENDED, ID_TOP_BNDRY, ISACTIVE, FROMDATE, TODATE)
 Values
   (seq_eg_user.nextVal, 'Commissioner', 'Commissioner', '-640vq9e24s79b', TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), ' ', 'N', 1, 1, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2999 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));



Insert into eg_userrole
   (ID_ROLE, ID_USER, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_role from eg_roles where role_name='PGR_Officer'), (select id_user from eg_user where user_name='AEEU6'), seq_eg_userrole.nextVal, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('03/31/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');


Insert into eg_userrole
   (ID_ROLE, ID_USER, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_role from eg_roles where role_name='PGR_Officer'), (select id_user from eg_user where user_name='EE1Z2'), seq_eg_userrole.nextVal, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('03/31/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_userrole
   (ID_ROLE, ID_USER, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_role from eg_roles where role_name='PGR_Officer'), (select id_user from eg_user where user_name='AHO2'), seq_eg_userrole.nextVal, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('03/31/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');


Insert into eg_userrole
   (ID_ROLE, ID_USER, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_role from eg_roles where role_name='PGR_Officer'), (select id_user from eg_user where user_name='ZOZ2'), seq_eg_userrole.nextVal, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('03/31/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_userrole
   (ID_ROLE, ID_USER, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'), (select id_user from eg_user where user_name='ARO2'), seq_eg_userrole.nextVal, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('03/31/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_userrole
   (ID_ROLE, ID_USER, ID, FROMDATE, TODATE, IS_HISTORY)
 Values
   ((select id_role from eg_roles where role_name='PGR_Officer'), (select id_user from eg_user where user_name='Commissioner'), seq_eg_userrole.nextVal, TO_DATE('04/01/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('03/31/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

--employee
Insert into eg_employee
   (ID, ID_USER, ISACTIVE, EMP_FIRSTNAME, CODE, CREATEDTIME, STATUS, LASTMODIFIED_DATE,EMPCATMSTR_ID)
 Values
   (EGPIMS_PERSONAL_INFO_SEQ.nextVal,  (select id_user from eg_user where user_name='EE1Z2'), 1, 'EE1Z2', (select max(code)+1 from eg_employee ), TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('03/23/2009 11:24:35', 'MM/DD/YYYY HH24:MI:SS'),3);

Insert into eg_employee
   (ID, ID_USER, ISACTIVE, EMP_FIRSTNAME, CODE, CREATEDTIME, STATUS, LASTMODIFIED_DATE,EMPCATMSTR_ID)
 Values
   (EGPIMS_PERSONAL_INFO_SEQ.nextVal,  (select id_user from eg_user where user_name='AEEU6'), 1, 'AEEU6', (select max(code)+1 from eg_employee ), TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('03/23/2009 11:24:35', 'MM/DD/YYYY HH24:MI:SS'),3);

Insert into eg_employee
   (ID, ID_USER, ISACTIVE, EMP_FIRSTNAME, CODE, CREATEDTIME, STATUS, LASTMODIFIED_DATE,EMPCATMSTR_ID)
 Values
   (EGPIMS_PERSONAL_INFO_SEQ.nextVal,  (select id_user from eg_user where user_name='AHO2'), 1, 'AHO2', (select max(code)+1 from eg_employee ), TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('03/23/2009 11:24:35', 'MM/DD/YYYY HH24:MI:SS'),3);


Insert into eg_employee
   (ID, ID_USER, ISACTIVE, EMP_FIRSTNAME, CODE, CREATEDTIME, STATUS, LASTMODIFIED_DATE,EMPCATMSTR_ID)
 Values
   (EGPIMS_PERSONAL_INFO_SEQ.nextVal,  (select id_user from eg_user where user_name='ARO2'), 1, 'ARO2', (select max(code)+1 from eg_employee ), TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('03/23/2009 11:24:35', 'MM/DD/YYYY HH24:MI:SS'),3);

Insert into eg_employee
   (ID, ID_USER, ISACTIVE, EMP_FIRSTNAME, CODE, CREATEDTIME, STATUS, LASTMODIFIED_DATE,EMPCATMSTR_ID)
 Values
   (EGPIMS_PERSONAL_INFO_SEQ.nextVal,  (select id_user from eg_user where user_name='ZOZ2'), 1, 'ZOZ2', (select max(code)+1 from eg_employee ), TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('03/23/2009 11:24:35', 'MM/DD/YYYY HH24:MI:SS'),3);

Insert into eg_employee
   (ID, ID_USER, ISACTIVE, EMP_FIRSTNAME, CODE, CREATEDTIME, STATUS, LASTMODIFIED_DATE,EMPCATMSTR_ID)
 Values
   (EGPIMS_PERSONAL_INFO_SEQ.nextVal,  (select id_user from eg_user where user_name='Commissioner'), 1, 'Commissioner', (select max(code)+1 from eg_employee ), TO_DATE('02/13/2009 14:19:22', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('03/23/2009 11:24:35', 'MM/DD/YYYY HH24:MI:SS'),3);




Insert into eg_emp_assignment_prd
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values
   (seq_ass_prd.nextval, TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where emp_firstname='EE1Z2'));

Insert into eg_emp_assignment_prd
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values
   (seq_ass_prd.nextval, TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where emp_firstname='AEEU6'));

Insert into eg_emp_assignment_prd
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values
   (seq_ass_prd.nextval, TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where emp_firstname='AHO2'));

Insert into eg_emp_assignment_prd
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values
   (seq_ass_prd.nextval, TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where emp_firstname='ARO2'));

Insert into eg_emp_assignment_prd
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values
   (seq_ass_prd.nextval, TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where emp_firstname='ZOZ2'));

Insert into eg_emp_assignment_prd
   (ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values
   (seq_ass_prd.nextval, TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where emp_firstname='Commissioner'));






Insert into eg_emp_assignment
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values
   (seq_ass.nextVal, 8, 1,  (select  DESIGNATIONID from eg_designation where designation_name='EXECUTIVE ENGINEER')
   , 1, (select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='EE1Z2')) ), (select id_dept from eg_department where dept_name='FB-Zone 2'), 
   (select id from eg_position where position_name ='EXECUTIVE ENGINEER'));

 Insert into eg_emp_assignment
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values
   (seq_ass.nextVal, 8, 1,  (select  DESIGNATIONID from eg_designation where designation_name='ASSISTANT EXECUTIVE ENGINEER')
   , 1,  (select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='AEEU6')) ), (select id_dept from eg_department where dept_name='FB-Zone 2'), 
   (select id from eg_position where position_name ='ASSISTANT EXECUTIVE ENGINEER'));

   Insert into eg_emp_assignment
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values
   (seq_ass.nextVal, 8, 1,  (select  DESIGNATIONID from eg_designation where designation_name='ASSISTANT HEALTH OFFICER')
   , 1,  (select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='AHO2')) ), (select id_dept from eg_department where dept_name='FB-Zone 2'), 
   (select id from eg_position where position_name ='ASSISTANT HEALTH OFFICER'));

Insert into eg_emp_assignment
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values
   (seq_ass.nextVal, 8, 1,  (select  DESIGNATIONID from eg_designation where designation_name='ASSISTANT REVENUE OFFICER')
   , 1,  (select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='ARO2')) ), (select id_dept from eg_department where dept_name='FB-Zone 2'), 
   (select id from eg_position where position_name ='ASSISTANT REVENUE OFFICER'));

Insert into eg_emp_assignment
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values
   (seq_ass.nextVal, 8, 1,  (select  DESIGNATIONID from eg_designation where designation_name='Zonal Officer')
   , 1,  (select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='ZOZ2')) ), (select id_dept from eg_department where dept_name='FB-Zone 2'), 
   (select id from eg_position where position_name ='Zonal Officer'));

Insert into eg_emp_assignment
   (ID, ID_FUND, ID_FUNCTION, DESIGNATIONID, ID_FUNCTIONARY, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values
   (seq_ass.nextVal, 8, 1,  (select  DESIGNATIONID from eg_designation where designation_name='Commissioner')
   , 1,  (select ID from eg_emp_assignment_prd where id_employee=((select id from eg_employee where emp_firstname='Commissioner')) ), (select id_dept from eg_department where dept_name='FB-Zone 2'), 
   (select id from eg_position where position_name ='Commissioner'));



Insert into eg_position_hir
   (ID, POSITION_FROM, POSITION_TO, OBJECT_TYPE_ID)
 Values
   (SEQ_POSITION_HIR.nextVal, (select id from eg_position where position_name='EXECUTIVE ENGINEER'), 
   (select id from eg_position where position_name='Zonal Officer'), (select id from eg_object_type where type='Complaint'));

Insert into eg_position_hir
   (ID, POSITION_FROM, POSITION_TO, OBJECT_TYPE_ID)
 Values
   (SEQ_POSITION_HIR.nextVal, (select id from eg_position where position_name='ASSISTANT EXECUTIVE ENGINEER'), 
   (select id from eg_position where position_name='EXECUTIVE ENGINEER'), (select id from eg_object_type where type='Complaint'));

Insert into eg_position_hir
   (ID, POSITION_FROM, POSITION_TO, OBJECT_TYPE_ID)
 Values
   (SEQ_POSITION_HIR.nextVal, (select id from eg_position where position_name='ASSISTANT HEALTH OFFICER'), 
   (select id from eg_position where position_name='Zonal Officer'), (select id from eg_object_type where type='Complaint'));

Insert into eg_position_hir
   (ID, POSITION_FROM, POSITION_TO, OBJECT_TYPE_ID)
 Values
   (SEQ_POSITION_HIR.nextVal, (select id from eg_position where position_name='ASSISTANT REVENUE OFFICER'), 
   (select id from eg_position where position_name='Zonal Officer'), (select id from eg_object_type where type='Complaint'));


Insert into eg_position_hir
   (ID, POSITION_FROM, POSITION_TO, OBJECT_TYPE_ID)
 Values
   (SEQ_POSITION_HIR.nextVal, (select id from eg_position where position_name='Zonal Officer'), 
   (select id from eg_position where position_name='Commissioner'), (select id from eg_object_type where type='Complaint'));





--userjur values

INSERT INTO eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL,  (select id_user from eg_user where user_name='EE1Z2'), 3, TO_DATE('02/13/2009 14:19:21', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL, (select id_user from eg_user where user_name='AEEU6'), 3, TO_DATE('02/13/2009 14:19:21', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL,  (select id_user from eg_user where user_name='AHO2'), 3, TO_DATE('02/13/2009 14:19:21', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL, (select id_user from eg_user where user_name='ARO2'), 1, TO_DATE('02/13/2009 14:19:21', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL, (select id_user from eg_user where user_name='ZOZ2'), 3, TO_DATE('02/13/2009 14:19:21', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO eg_user_jurlevel
   (ID_USER_JURLEVEL, ID_USER, ID_BNDRY_TYPE, UPDATETIME)
 VALUES
   (SEQ_EG_USER_JURLEVEL.NEXTVAL,(select id_user from eg_user where user_name='Commissioner'), 3, TO_DATE('02/13/2009 14:19:21', 'MM/DD/YYYY HH24:MI:SS'));


 Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   (  (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user=(select id_user from eg_user where user_name='EE1Z2')), 3, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');


Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   ( (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user= (select id_user from eg_user where user_name='AHO2')), 3, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   ( (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user= (select id_user from eg_user where user_name='ARO2')), 1, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   (  (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user= (select id_user from eg_user where user_name='ZOZ2')), 3, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   ( (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user= (select id_user from eg_user where user_name='Commissioner')), 3, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   (  (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user=(select id_user from eg_user where user_name='AEEU6')),24, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   (  (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user=(select id_user from eg_user where user_name='AEEU6')), 25, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   ( (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user=(select id_user from eg_user where user_name='AEEU6')), 29, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');

Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   (  (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user=(select id_user from eg_user where user_name='AEEU6')), 30, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');
Insert into eg_user_jurvalues
   (ID_USER_JURLEVEL, ID_BNDRY, ID, FROMDATE, IS_HISTORY)
 Values
   (  (select ID_USER_JURLEVEL from eg_user_jurlevel where id_user=(select id_user from eg_user where user_name='AEEU6')), 31, SEQ_EG_USER_JURVALUES.nextVal, TO_DATE('04/01/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'N');
 


--router
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 24, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 25, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 26, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 27, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 28, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 29, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 30, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 31, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 32, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 33, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 34, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 35, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 36, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 37, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 38, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 39, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 40, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 41, 86);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 24, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 25, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 26, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 27, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 28, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 29, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 30, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 31, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 32, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 33, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 34, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 35, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 36, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 37, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 38, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 39, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 40, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 41, 87);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 11);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 12);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 13);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 14);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 24, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 25, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 26, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 27, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 28, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 29, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 30, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 31, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 32, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 33, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 34, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 35, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 36, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 37, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 38, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 39, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 40, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 41, 90);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 24, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 25, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)

 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 26, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 27, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 28, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 29, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 30, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 31, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 32, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 33, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 34, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 35, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 36, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 37, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 38, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 39, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 40, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 41, 51);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 24, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 25, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 26, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 27, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 28, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 29, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 30, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 31, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 32, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 33, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 34, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 35, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 36, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 37, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 38, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 39, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 40, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 41, 46);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 24, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 25, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 26, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 27, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 28, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 29, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 30, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 31, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 32, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 33, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 34, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 35, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 36, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 37, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 38, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 39, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 40, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 41, 49);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 24, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 25, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 26, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 27, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 28, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 29, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 30, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 31, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 32, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 33, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 34, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 35, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 36, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 37, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 38, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 39, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 40, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ZOZ2'), 41, 50);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 24, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 25, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 26, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 27, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 28, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 29, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 30, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 31, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 32, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 33, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 34, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 35, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 36, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 37, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 38, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 39, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 40, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 41, 88);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 24, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 25, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 26, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 27, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 28, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 29, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 30, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 31, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 32, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 33, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 34, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 35, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 36, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 37, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 38, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 39, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 40, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='EE1Z2'), 41, 89);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 24, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 25, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 26, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 27, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 28, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 29, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 30, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 31, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 32, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 33, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 34, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 35, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 36, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 37, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 38, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 39, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 40, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 41, 43);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 44);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 24, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 25, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 26, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 27, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 28, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 29, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 30, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 31, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 32, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 33, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 34, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 35, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 36, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 37, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 38, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 39, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 40, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 41, 45);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 18);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 21);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 22);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 24, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 25, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 26, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 27, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 28, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 29, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 30, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 31, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 32, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 33, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 34, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 35, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 36, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 37, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 38, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 39, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 40, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='ARO2'), 41, 77);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 26);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 25);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 27);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 28);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 29);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 23);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 33);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 24);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 30);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 24, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 25, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 26, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 27, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 28, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 29, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 30, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 31, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 32, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 33, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 34, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 35, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 36, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 37, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 38, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 39, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 40, 32);
Insert into eg_router
   (ID, PGRFLAG, USERID, BNDRYID, COMPLAINTTYPEID)
 Values
   (seq_eg_router.nextVal, 1, (select id_user from eg_user where user_name='AHO2'), 41, 32);


Insert into EGGR_COMPLAINTRECEIVINGCENTER 
   (CENTERID, CENTERNAME, BNDRYID)
 Values
   (1, 'Complaint Cell', 1);
Insert into EGGR_COMPLAINTRECEIVINGCENTER 
   (CENTERID, CENTERNAME, BNDRYID)
 Values
   (2, 'Mayor', 1);
Insert into EGGR_COMPLAINTRECEIVINGCENTER 
   (CENTERID, CENTERNAME, BNDRYID)
 Values
   (3, 'Zone Office', 1);
Insert into EGGR_COMPLAINTRECEIVINGCENTER 
   (CENTERID, CENTERNAME, BNDRYID)
 Values
   (4, 'Commissioner Office', 1);
Insert into EGGR_COMPLAINTRECEIVINGCENTER 
   (CENTERID, CENTERNAME, BNDRYID)
 Values
   (5, 'CM Office', 1);
COMMIT;



 

#DOWN
