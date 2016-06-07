------ START : Contractor Bill Register workflow update ---
UPDATE EG_WF_MATRIX SET currentdesignation = 'Work Inspector/ Technical Mastry', nextdesignation = 'Assistant engineer' WHERE objecttype = 'ContractorBillRegister' AND currentstate = 'NEW';
UPDATE EG_WF_MATRIX SET currentdesignation = 'Assistant engineer' WHERE objecttype = 'ContractorBillRegister' AND currentstate = 'Created';
UPDATE EG_WF_MATRIX SET currentdesignation = 'Work Inspector/ Technical Mastry', nextdesignation = 'Assistant engineer' WHERE objecttype = 'ContractorBillRegister' AND currentstate = 'Rejected';