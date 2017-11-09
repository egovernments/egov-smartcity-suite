CREATE sequence SEQ_EGSWTAX_SHSC_NUMBER;

--append existing records with 0
update egswtax_connection set shsc_number  = shsc_number || '0' where shsc_number is not null;

