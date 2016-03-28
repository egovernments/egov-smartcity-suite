ALTER TABLE financialyear ADD COLUMN createddate timestamp without time zone;

ALTER TABLE financialyear ADD COLUMN lastmodifiedby bigint;

ALTER TABLE financialyear ADD COLUMN lastmodifieddate timestamp without time zone;

ALTER TABLE financialyear  ADD COLUMN version numeric ;

ALTER TABLE financialyear ADD COLUMN createdby bigint;


ALTER TABLE fiscalperiod ADD COLUMN createddate timestamp without time zone;

ALTER TABLE fiscalperiod ADD COLUMN lastmodifiedby bigint;

ALTER TABLE fiscalperiod ADD COLUMN lastmodifieddate timestamp without time zone;

ALTER TABLE fiscalperiod  ADD COLUMN version numeric ;

ALTER TABLE fiscalperiod ADD COLUMN createdby bigint;

ALTER TABLE fiscalperiod ADD COLUMN isactive boolean;

ALTER TABLE fiscalperiod  ADD COLUMN isactiveforposting boolean;

ALTER TABLE fiscalperiod ADD COLUMN isclosed boolean;
