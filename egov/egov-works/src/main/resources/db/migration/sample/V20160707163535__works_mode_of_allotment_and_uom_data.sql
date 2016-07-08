-----------Start-----------
------------sample data for Mode of Allotment table--------------------
Insert into egw_mode_of_allotment (ID,NAME) values (NEXTVAL('SEQ_EGW_MODE_OF_ALLOTMENT'),'E-Procurement (For above 1 Lakh)');
Insert into egw_mode_of_allotment (ID,NAME) values (NEXTVAL('SEQ_EGW_MODE_OF_ALLOTMENT'),'Tendering (For below 1 Lakh)');
Insert into egw_mode_of_allotment (ID,NAME) values (NEXTVAL('SEQ_EGW_MODE_OF_ALLOTMENT'),'Nomination');

------------sample data for Line Estimate UOM table--------------------
Insert into egw_lineestimate_uom (ID,NAME) values (NEXTVAL('SEQ_EGW_LINEESTIMATE_UOM'),'Sq.metres');
Insert into egw_lineestimate_uom (ID,NAME) values (NEXTVAL('SEQ_EGW_LINEESTIMATE_UOM'),'Cubic Metres');
Insert into egw_lineestimate_uom (ID,NAME) values (NEXTVAL('SEQ_EGW_LINEESTIMATE_UOM'),'Running Metres');
Insert into egw_lineestimate_uom (ID,NAME) values (NEXTVAL('SEQ_EGW_LINEESTIMATE_UOM'),'Numbers');
Insert into egw_lineestimate_uom (ID,NAME) values (NEXTVAL('SEQ_EGW_LINEESTIMATE_UOM'),'Each');
Insert into egw_lineestimate_uom (ID,NAME) values (NEXTVAL('SEQ_EGW_LINEESTIMATE_UOM'),'Others');

--rollback delete from egw_lineestimate_uom;
--rollback delete from egw_mode_of_allotment;

-----------END----------------