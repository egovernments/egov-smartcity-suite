alter table AccountEntityMaster drop column lastmodified;
alter table AccountEntityMaster drop column modifiedby;
alter table AccountEntityMaster drop column created;

ALTER TABLE AccountEntityMaster    ADD COLUMN createdby bigint;
ALTER TABLE AccountEntityMaster    ADD COLUMN createdDate timestamp;
ALTER TABLE AccountEntityMaster    ADD COLUMN lastModifiedBy bigint;
ALTER TABLE AccountEntityMaster    ADD COLUMN lastModifiedDate timestamp; 
ALTER TABLE AccountEntityMaster    ADD COLUMN version numeric; 

update AccountEntityMaster    set version=0 where version is null;

ALTER TABLE  AccountEntityMaster add  CONSTRAINT fk_aem_adt FOREIGN KEY (detailtypeid)
      REFERENCES accountdetailtype (id);
      
ALTER TABLE AccountEntityMaster add  CONSTRAINT fk_aem_user FOREIGN KEY (lastModifiedBy)
      REFERENCES eg_user (id);

ALTER TABLE AccountEntityMaster add  CONSTRAINT fk_aem_modified_user FOREIGN KEY (createdby)
      REFERENCES eg_user (id);   