-------------- Updating financial year --------------------

update eg_installment_master set financial_year = (substring(description from 1 for 5)||substring(description from 8 for 2)) where id_module = (select id from eg_module where name = 'Property Tax');

