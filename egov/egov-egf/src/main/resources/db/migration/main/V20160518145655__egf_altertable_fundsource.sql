ALTER TABLE fundsource RENAME COLUMN created TO createddate;

ALTER TABLE fundsource  ADD COLUMN version numeric ;

update fundsource set version=0 where version is null;

update fundsource set createddate = current_date where createddate is null;

update fundsource set lastmodifieddate = current_date where lastmodifieddate is null;

update fundsource set createdby = 1 where createdby is null;

update fundsource set lastmodifiedby = 1 where lastmodifiedby is null;