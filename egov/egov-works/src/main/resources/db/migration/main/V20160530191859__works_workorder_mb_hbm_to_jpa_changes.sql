alter table egw_workorder add column version bigint  default 0 ;

alter table egw_revision_workorder add column version bigint default 0 ;

alter table egw_offline_status add column version bigint default 0 ;

alter table egw_contractor add column version bigint default 0 ;

alter table egw_contractor_detail add column version bigint default 0 ;

alter table egw_contractor_grade add column version bigint default 0 ;

alter table egw_workorder_estimate add column version bigint default 0 ;

alter table egw_workorder_activity add column version bigint default 0 ;

alter table egw_workorder_assets  add column version bigint default 0 ;

alter table egw_mb_header  add column version bigint default 0 ;

alter table egw_mb_details   add column version bigint default 0 ;



alter table egw_workorder rename column modifiedby TO lastmodifiedby;

alter table egw_workorder rename column modifieddate TO lastmodifieddate;

alter table egw_offline_status rename column created_by TO createdby;

alter table egw_offline_status rename column created_date TO createddate;

alter table egw_offline_status rename column modified_by TO lastmodifiedby;

alter table egw_offline_status rename column modified_date TO lastmodifieddate;

alter table egw_contractor rename column modifiedby TO lastmodifiedby;

alter table egw_contractor rename column modifieddate TO lastmodifieddate;

alter table egw_contractor_detail rename column modifiedby TO lastmodifiedby;

alter table egw_contractor_detail rename column modifieddate TO lastmodifieddate;

alter table egw_contractor_grade rename column modifiedby TO lastmodifiedby;

alter table egw_contractor_grade rename column modifieddate TO lastmodifieddate;

alter table egw_workorder_estimate rename column modifiedby TO lastmodifiedby;

alter table egw_workorder_estimate rename column modifieddate TO lastmodifieddate;

alter table egw_workorder_activity rename column modifiedby TO lastmodifiedby;

alter table egw_workorder_activity rename column modifieddate TO lastmodifieddate;

alter table egw_workorder_assets rename column created_by TO createdby;

alter table egw_workorder_assets rename column created_date TO createddate;

alter table egw_workorder_assets rename column modified_by TO lastmodifiedby;

alter table egw_workorder_assets rename column modified_date TO lastmodifieddate;

alter table egw_mb_header rename column created_by TO createdby;

alter table egw_mb_header rename column created_date TO createddate;

alter table egw_mb_header rename column modified_by TO lastmodifiedby;

alter table egw_mb_header rename column modified_date TO lastmodifieddate;

alter table egw_mb_details rename column created_by TO createdby;

alter table egw_mb_details rename column created_date TO createddate;

alter table egw_mb_details rename column modified_by TO lastmodifiedby;

alter table egw_mb_details rename column modified_date TO lastmodifieddate;


alter table egw_contractor_detail  drop column MY_CONTRACTOR_INDEX restrict;

alter table egw_mb_details drop column MBD_MBH_INDEX restrict;

alter table egw_workorder_estimate drop column WORKORDER_ESTIMATE_INDEX restrict;

alter table egw_workorder_assets  drop column wo_est_asset_index restrict;


 
 
