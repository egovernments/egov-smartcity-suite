update eg_modules set name='Water Charges' where description='Water Tax Module' and name='Water Tax';
update eg_applicationindex set modulename='Water Charges' where modulename='Water Tax';
update egcl_servicedetails set name = 'Water Charges' where name='Water Tax' and code='WT'; 
update eg_collectionindex set billingservice='Water Charges' where billingservice='Water Tax';