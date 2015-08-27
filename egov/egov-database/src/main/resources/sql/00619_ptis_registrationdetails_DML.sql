UPDATE egpt_basic_property SET regd_doc_date = '01-02-2015' WHERE regd_doc_date is null;
UPDATE egpt_basic_property SET regd_doc_no = '100' WHERE regd_doc_no is null OR regd_doc_no = '';