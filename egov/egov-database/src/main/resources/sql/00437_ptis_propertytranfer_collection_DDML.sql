select  nextval('seq_egcl_servicedetails');
Update egcl_servicecategory set name ='Property Tax', code='PT' where code='TEMP';
insert into egcl_servicedetails values(nextval('seq_egcl_servicedetails'), 'PT Mutation Fee', '/../ptis/view/viewDCBProperty!displayPropInfo.action?propertyId=',true,'/receipts/receipt-create.action','B','PTMF',1,null,null,false,null,null,(select id from egcl_servicecategory where code='PT'),false,now(),1,now(),1,now());
