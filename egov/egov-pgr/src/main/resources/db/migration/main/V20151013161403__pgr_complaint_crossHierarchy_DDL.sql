--DDL
ALTER TABLE EGPGR_COMPLAINT ADD COLUMN childlocation bigint;

ALTER TABLE EGPGR_COMPLAINT ADD CONSTRAINT fk_complaint_childlocation FOREIGN KEY (childlocation) REFERENCES eg_boundary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

--ajax load child locations

update eg_action set url='/ajax-getChildLocation',name='loadchildlocations' where name='load Wards' and contextroot='pgr';



