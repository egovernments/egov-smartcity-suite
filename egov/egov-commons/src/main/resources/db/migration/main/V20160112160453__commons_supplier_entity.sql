delete from accountdetailtype where name='Supplier';

SELECT setval('"seq_accountdetailtype"',10);

Insert into ACCOUNTDETAILTYPE (ID,NAME,DESCRIPTION,TABLENAME,COLUMNNAME,ATTRIBUTENAME,NBROFLEVELS,ISACTIVE,CREATED,LASTMODIFIED,MODIFIEDBY ,FULL_QUALIFIED_NAME)
 values (nextval('seq_accountdetailtype'),'Supplier','Supplier','relation','id','relation_id',1,1,current_date,current_date,null,'org.egov.commons.Relation');

