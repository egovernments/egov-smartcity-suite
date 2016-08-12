alter table EGW_MV_WORK_PROGRESS_REGISTER add column woOfflineStatusCode character varying(50);
alter table EGW_MV_WORK_PROGRESS_REGISTER add column contractorName character varying(100);
alter table EGW_MV_WORK_PROGRESS_REGISTER add column contractorCode character varying(50);
alter table EGW_MV_WORK_PROGRESS_REGISTER add column boundaryNum bigint;

--rollback alter table EGW_MV_WORK_PROGRESS_REGISTER drop column woOfflineStatusCode;
--rollback alter table EGW_MV_WORK_PROGRESS_REGISTER drop column contractorName;
--rollback alter table EGW_MV_WORK_PROGRESS_REGISTER drop column contractorCode;
--rollback alter table EGW_MV_WORK_PROGRESS_REGISTER drop column boundaryNum;