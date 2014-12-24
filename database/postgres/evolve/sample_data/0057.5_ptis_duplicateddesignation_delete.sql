#UP

update eg_position set desig_id = (select DESIGNATIONID from eg_designation where designation_name = 'Zonal Officer') where position_name like 'ZONAL_OFFICER%';
update EG_EMP_assignment set designationid = (select DESIGNATIONID from eg_designation where designation_name = 'Zonal Officer') where position_id in (select id from eg_position where position_name like 'ZONAL_OFFICER%');
delete eg_designation where designation_name = 'ZONAL OFFICER';

#DOWN