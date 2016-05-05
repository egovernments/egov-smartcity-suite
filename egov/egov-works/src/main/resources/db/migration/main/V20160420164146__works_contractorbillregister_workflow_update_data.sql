------ START : Contractor Bill Register workflow update ---
UPDATE EG_WF_MATRIX SET nextstate = 'Approved' WHERE objecttype = 'ContractorBillRegister' AND currentstate = 'Created';