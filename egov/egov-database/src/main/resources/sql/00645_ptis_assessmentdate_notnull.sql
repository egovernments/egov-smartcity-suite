CREATE OR REPLACE VIEW  egpt_mv_bp_curr_demand AS 
SELECT bp.ID AS id_basic_property,
	   admd.id_demand
FROM egpt_mv_bp_inst_active_demand admd, egpt_basic_property bp  
WHERE admd.id_basic_property = bp.id
and admd.id_installment = (SELECT id
	          FROM eg_installment_master
              WHERE start_date <= now()
              AND end_date     >= now()
              AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'));

update egpt_basic_property set assessmentdate = lastmodifieddate where assessmentdate is null;
alter table egpt_basic_property alter column assessmentdate set not null;

--rollback ALTER TABLE egpt_basic_property ALTER COLUMN assessmentdate DROP NOT NULL;
/*rollback CREATE OR REPLACE VIEW  egpt_mv_bp_curr_demand AS 
SELECT bp.ID AS id_basic_property,
	   admd.id_demand
FROM egpt_mv_bp_inst_active_demand admd, egpt_basic_property bp  
WHERE admd.id_basic_property = bp.id
and admd.id_installment = (SELECT id
	          FROM eg_installment_master
              WHERE start_date <= now()
              AND end_date     >= now()
              AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'));*/
