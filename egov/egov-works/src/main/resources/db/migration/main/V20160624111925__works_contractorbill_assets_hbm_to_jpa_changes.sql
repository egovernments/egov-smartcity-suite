alter table egw_contractorbill_assets rename column created_by TO createdby;

alter table egw_contractorbill_assets rename column created_date TO createddate;

alter table egw_contractorbill_assets rename column modified_by TO lastmodifiedby;

alter table egw_contractorbill_assets rename column modified_date TO lastmodifieddate;

alter table egw_contractorbill_assets add column version bigint default 0 ;

alter table egw_contractorbill_assets drop column bill_asset_index ; 


--rollback alter table egw_contractorbill_assets rename column createdby  TO created_by;

--rollback alter table egw_contractorbill_assets rename column createddate  TO created_date;

--rollback alter table egw_contractorbill_assets rename column lastmodifiedby  TO modified_by;

--rollback alter table egw_contractorbill_assets rename column lastmodifieddate  TO modified_date;

--rollback alter table egw_contractorbill_assets   drop column version ;

--rollback alter table egw_contractorbill_assets add column bill_asset_index bigint default 0 ;

