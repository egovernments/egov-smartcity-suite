
Insert into EG_UOMCATEGORY (id,category,narration,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uomcategory'),'AREA','area',now(),now(),1,1);
Insert into EG_UOMCATEGORY (id,category,narration,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uomcategory'),'Length','Length',now(),now(),1,1);
Insert into EG_UOMCATEGORY (id,category,narration,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uomcategory'),'Quantity','Quantity',now(),now(),1,1);
Insert into EG_UOMCATEGORY (id,category,narration,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uomcategory'),'Numbers','Numbers',now(),now(),1,1);
Insert into EG_UOMCATEGORY (id,category,narration,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uomcategory'),'Volume','Volume',now(),now(),1,1);
Insert into EG_UOMCATEGORY (id,category,narration,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uomcategory'),'Weight','Weight',now(),now(),1,1);


Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Length'),'MTR','MTR',1,'1',now(),now(),1,1);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Length'),'CENTIMETER','centimeter',100,'0',now(),now(),1,null);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Length'),'30 MTR','30 METER',30,'0',now(),now(),1,1);

Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Weight'),'SQM','square meter',1,'0',now(),now(),1,1);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Weight'),'10 SQM','10 SQM',10,'0',now(),now(),1,1);

Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Volume'),'CUM','cubic meter',1,'0',now(),now(),1,1);

Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Quantity'),'GRM','GRM',1,'1',now(),now(),1,1);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Quantity'),'KGS','KGS',1000,'0',now(),now(),1,1);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Quantity'),'TON','TON',1000000,'0',now(),now(),1,1);

Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Quantity'),'BAG','BAG',12,'0',now(),now(),1,1);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Quantity'),'BOX','BOX1',12,'0',now(),now(),1,1);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Quantity'),'DOZ','DOZ',12,'0',now(),now(),1,1);

Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Numbers'),'Each','each',1,'0',now(),now(),1,1);
Insert into EG_UOM (id,uomcategoryid,uom,narration,conv_factor,baseuom,lastmodified,createddate,createdby,lastmodifiedby) values (nextval('seq_eg_uom'),(select id from EG_UOMCATEGORY where category='Numbers'),'No','number',1,'1',now(),now(),1,1);

--rollback delete from EG_UOM;
--rollback delete from EG_UOMCATEGORY;
