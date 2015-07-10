ALTER TABLE egeis_jurisdiction DROP CONSTRAINT UNQ_EGEIS_JURISDICTION_EMP_BNDRYTYPE;
ALTER TABLE egeis_jurisdiction ADD COLUMN boundary BIGINT;
ALTER TABLE egeis_jurisdiction ADD CONSTRAINT FK_EGEIS_JURISDICTION_BOUNDARY FOREIGN KEY(boundary) REFERENCES eg_boundary(id);
ALTER TABLE egeis_jurisdiction ADD CONSTRAINT UNQ_EGEIS_JRDN_EMP_BNDTYPE_BND UNIQUE(employee,boundarytype,boundary);
DROP TABLE egeis_jurisdiction_details  ;

