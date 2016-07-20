-----------Updating description to Re-Submitted status to Line Estimate---------------
UPDATE EGW_STATUS SET DESCRIPTION = 'Re-Submitted' where MODULETYPE = 'LINEESTIMATE' and CODE = 'RESUBMITTED';

-----------Updating description to Re-Submitted status to MB---------------
UPDATE EGW_STATUS SET DESCRIPTION = 'Re-Submitted' where MODULETYPE = 'MBHeader' and CODE = 'RESUBMITTED';

-----------Updating description to Re-Submitted status to LOA---------------
UPDATE EGW_STATUS SET DESCRIPTION = 'Re-Submitted' where MODULETYPE = 'WorkOrder' and CODE = 'RESUBMITTED';

--rollback UPDATE EGW_STATUS SET DESCRIPTION = 'Resubmitted' where MODULETYPE = 'WorkOrder' and CODE = 'RESUBMITTED';
--rollback UPDATE EGW_STATUS SET DESCRIPTION = 'Resubmitted' where MODULETYPE = 'MBHeader' and CODE = 'RESUBMITTED';
--rollback UPDATE EGW_STATUS SET DESCRIPTION = 'Resubmitted' where MODULETYPE = 'LINEESTIMATE' and CODE = 'RESUBMITTED';