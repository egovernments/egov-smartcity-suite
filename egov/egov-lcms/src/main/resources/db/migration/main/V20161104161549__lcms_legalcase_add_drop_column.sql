----alter add column
ALTER TABLE eglc_legalcase ADD oldreferencenumber character varying(4); 

ALTER TABLE eglc_legalcase_aud ADD oldreferencenumber character varying(4); 

---alter drop column
ALTER TABLE eglc_legalcase DROP COLUMN lcnumbertype;

ALTER TABLE eglc_legalcase_aud DROP COLUMN lcnumbertype;