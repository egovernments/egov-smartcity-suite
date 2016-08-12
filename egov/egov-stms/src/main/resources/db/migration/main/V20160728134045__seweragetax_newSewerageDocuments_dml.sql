update egswtax_document_type_master set isactive='f' where description in ('Property Tax Paid Receipt','Water Tax Paid Receipt') and
applicationtype = (select id from egswtax_application_type  where code  ='NEWSEWERAGECONNECTION');
