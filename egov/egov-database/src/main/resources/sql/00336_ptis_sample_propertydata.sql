insert into egpt_propertyid (id, zone_num, ward_adm_id, adm1, adm2, created_date, modified_date, created_by, modified_by) values (nextval('SEQ_EGPT_PROPERTYID'), (select id from eg_boundary where name='Zone-1'), (select id from eg_boundary where name='Revenue Ward No 1'), (select id from eg_boundary where name='Block No 1'), (select id from eg_boundary where name='Kotta Peta Super Structure'), current_timestamp, current_timestamp, 1, 1);

insert into eg_address (housenobldgapt, landmark, userid, type, pincode, id) values ('001', 'Bank Road', 1, 'PROPERTYADDRESS', '532001', nextval('seq_eg_address'));

insert into egpt_address (id) values ((select id from eg_address where housenobldgapt='001'));

insert into egpt_basic_property (id, createdby, propertyid, isactive, addressid, createddate, lastmodifieddate, id_propertyid,id_prop_create_reason, status, modifiedby, ismigrated) values (nextval('SEQ_EGPT_BASIC_PROPERTY'), 1, '1085000001', TRUE, (select id from eg_address where housenobldgapt='001'), current_timestamp, current_timestamp, (select id from egpt_propertyid where zone_num=(select id from eg_boundary where name='Zone-1') and ward_adm_id=(select id from eg_boundary where name='Revenue Ward No 1') and adm1=(select id from eg_boundary where name='Block No 1') and adm2=(select id from eg_boundary where name='Kotta Peta Super Structure')), (select id from egpt_mutation_master where type='CREATE' and code='NEW'), (select id from egpt_status where code='ASSESSED'), 1, 'N');

insert into egpt_property (id, created_date, modified_date, created_by, id_source, id_basic_property, is_default_property, status, id_installment, modified_by, effective_date) values (nextval('SEQ_EGPT_PROPERTY'), current_timestamp, current_timestamp, 1, (select id from EGPT_SRC_OF_INFO), (select id from egpt_basic_property where propertyid='1085000001'), 'Y', 'A', (SELECT id FROM eg_installment_master WHERE start_date <= current_timestamp AND end_date >= current_timestamp AND id_module = (SELECT id FROM eg_module WHERE name='Property Tax')), 1, current_timestamp);

insert into egpt_property_detail(id, id_property, sital_area, total_builtup_area, num_floors, id_usg_mstr, property_type, id_propertytypemaster, id_mutation, updt_timestamp, lift, toilets, watertap, structure, drainage, electricity, attachedbathroom, waterharvesting, cable, extentsite, extent_appurtenant_land, floortype, rooftype, walltype, woodtype)
values (nextval('SEQ_EGPT_PROPERTY_DETAIL'), (select id from egpt_property where id_basic_property=(select id from egpt_basic_property where propertyid='1085000001')), 1600, 1400, 1, (select id from egpt_property_usage_master where code='RESD'), 'BuiltUpProperty', (select id from egpt_property_type_master where code='RESIDENTIAL'), (select id from egpt_mutation_master where code='NEW'), current_timestamp, FALSE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, 500, 500, (select id from egpt_floor_type where name='Cement'), (select id from egpt_roof_type where name='Ordinary RCC'), (select id from egpt_wall_type where name='Bricks With Cement'), (select id from egpt_wood_type where name='Teak Wood'));

insert into egpt_floor_detail(id, id_property_detail, floor_no, created_date, builtup_area, id_struc_cl, id_occpn_mstr, id_usg_mstr, capitalvalue, planapproved, modified_date)
values (nextval('SEQ_EGPT_FLOOR_DETAIL'), (select id from egpt_property_detail where id_property = (select id_property  from egpt_property where id_basic_property=(select id from egpt_basic_property where propertyid='1085000001'))), 0, current_timestamp, 1400, (select id from egpt_struc_cl where constr_type='Rcc Ordinary'), (select id from egpt_occupation_type_master where occupation='Owner'), (select id from egpt_property_usage_master where code='RESD'), 100000, TRUE, current_timestamp);

insert into eg_user(id, salutation, username, password, pwdexpirydate, mobilenumber, emailid, createddate, lastmodifieddate, createdby, lastmodifiedby, active, name, gender, aadhaarnumber, type, version)
values (nextval('SEQ_EG_USER'), 'Mr.', '8888888888', '$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK', to_date('31/12/2099','dd/MM/yyyy'), '8888888888', 'sudarshan@test.com', current_timestamp, current_timestamp , 1, 1, TRUE, 'sudarshan', 1, '123123123', 'CITIZEN', 0);

insert into EGPT_PROPERTY_OWNER_INFO (id, id_property, id_source, orderno, owner)
values (nextval('SEQ_EGPT_PROPERTY_OWNER_INFO'), (select id from egpt_property where id_basic_property=(select id from egpt_basic_property where propertyid='1085000001')), (select id from EGPT_SRC_OF_INFO), 1, (select id from eg_user where username='8888888888'));

insert into eg_demand(id, id_installment, base_demand, is_history, create_date, modified_date, amt_collected, status)
values (nextval('SEQ_EG_DEMAND'), (SELECT id FROM eg_installment_master WHERE start_date <= current_timestamp AND end_date >= current_timestamp AND id_module = (SELECT id FROM eg_module WHERE name='Property Tax')), 505, 'N', current_timestamp, current_timestamp , 0, 'A');

insert into egpt_ptdemand (id_property, id_demand) values ((select id from egpt_property where id_basic_property=(select id from egpt_basic_property where propertyid='1085000001')),(select id from eg_demand where base_demand=505));

insert into eg_demand_details (id, id_demand, id_demand_reason, amount, modified_date, create_date, amt_collected)
values (nextval('SEQ_EG_DEMAND_DETAILS'), (select id from eg_demand), (select id from eg_demand_reason where id_installment=(SELECT id FROM eg_installment_master WHERE start_date <= current_timestamp AND end_date >= current_timestamp AND id_module = (SELECT id FROM eg_module WHERE name='Property Tax'))
and id_demand_reason_master = (select id from eg_demand_reason_master where module=(SELECT id FROM eg_module WHERE name='Property Tax') and code='GEN_TAX')), 435, current_timestamp, current_timestamp, 0);

insert into eg_demand_details (id, id_demand, id_demand_reason, amount, modified_date, create_date, amt_collected)
values (nextval('SEQ_EG_DEMAND_DETAILS'), (select id from eg_demand), (select id from eg_demand_reason where id_installment=(SELECT id FROM eg_installment_master WHERE start_date <= current_timestamp AND end_date >= current_timestamp AND id_module = (SELECT id FROM eg_module WHERE name='Property Tax'))
and id_demand_reason_master = (select id from eg_demand_reason_master where module=(SELECT id FROM eg_module WHERE name='Property Tax') and code='LIB_CESS')), 35, current_timestamp, current_timestamp, 0);

insert into eg_demand_details (id, id_demand, id_demand_reason, amount, modified_date, create_date, amt_collected)
values (nextval('SEQ_EG_DEMAND_DETAILS'), (select id from eg_demand), (select id from eg_demand_reason where id_installment=(SELECT id FROM eg_installment_master WHERE start_date <= current_timestamp AND end_date >= current_timestamp AND id_module = (SELECT id FROM eg_module WHERE name='Property Tax'))
and id_demand_reason_master = (select id from eg_demand_reason_master where module=(SELECT id FROM eg_module WHERE name='Property Tax') and code='EDU_CESS')), 35, current_timestamp, current_timestamp, 0);
