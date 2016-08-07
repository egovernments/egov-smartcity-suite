---added columns
ALTER TABLE eglc_legalcase ADD officerincharge character varying(50); 
ALTER TABLE eglc_legalcase ADD noticedate date ;
UPDATE eglc_legalcase set noticedate='2016-06-15';
ALTER TABLE eglc_legalcase ALTER COLUMN noticedate SET NOT NULL ; 