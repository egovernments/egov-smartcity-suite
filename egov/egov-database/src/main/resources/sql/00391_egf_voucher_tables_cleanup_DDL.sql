ALTER TABLE voucherheader 
DROP COLUMN cgdate, 
DROP COLUMN departmentid,
DROP COLUMN fundsourceid,
DROP COLUMN functionid;

ALTER TABLE vouchermis 
DROP COLUMN schemename, 
DROP COLUMN accountcode,
DROP COLUMN accounthead,
DROP COLUMN contractamt,
DROP COLUMN cashbook, 
DROP COLUMN natureofwork,
DROP COLUMN assetdesc,
DROP COLUMN userdept,
DROP COLUMN demandno,
DROP COLUMN narration,
DROP COLUMN currentyear,
DROP COLUMN deptacchead,
DROP COLUMN subaccounthead,
DROP COLUMN projectcode,
DROP COLUMN concurrance_pn,
DROP COLUMN zonecode,
DROP COLUMN wardcode,
DROP COLUMN divisioncode,
DROP COLUMN month,
DROP COLUMN grossded,
DROP COLUMN emd_security,
DROP COLUMN netdeduction,
DROP COLUMN netamt,
DROP COLUMN totexpenditure,
DROP COLUMN billregisterid,
DROP COLUMN acount_department,
DROP COLUMN projectfund,
DROP COLUMN concurrance_sn,
DROP COLUMN segmentid,
DROP COLUMN sub_segmentid,
DROP COLUMN updatedtimestamp,
DROP COLUMN createtimestamp,
DROP COLUMN iut_status,
DROP COLUMN iut_number;

