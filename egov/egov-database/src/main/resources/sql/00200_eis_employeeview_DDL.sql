CREATE OR REPLACE VIEW view_egeis_employee("id","employee","code","name","username","useractive","dateofappointment","assignment","fromdate",
"todate","department","designation","position","functionary","function","fund","isprimary") AS
SELECT EA.id,EE.id,EE.code,EU.name,EU.username,EU.active AS useractive,EE.dateofappointment,
EA.id AS assignemnt,EA.fromdate,EA.todate,EA.department,EA.designation,EA.position,
EA.functionary,EA.function,EA.fund,EA.isprimary
FROM eg_user EU INNER JOIN egeis_employee EE ON EU.id=EE.id
INNER JOIN egeis_assignment EA ON EA.employee=EE.id;

--rollback DROP VIEW view_egeis_employee;
