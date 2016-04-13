------ START : Update Contractor Bill Register workflow  ---
UPDATE EG_WF_MATRIX SET additionalrule = null WHERE objecttype = 'ContractorBillRegister' and additionalrule = 'NEWCONTRACTORBILLREGISTER';
------ END : Update Contractor Bill Register workflow  ---

------ START : Update Line Estimate workflow  ---
UPDATE EG_WF_MATRIX SET additionalrule = null WHERE objecttype = 'LineEstimate' and additionalrule = 'NEWLINEESTIMATE';
------ END : Update Line Estimate workflow  ---

------ START : Update Application types for workflow ---
UPDATE eg_wf_types SET displayname = 'Contractor Bill' WHERE type = 'ContractorBillRegister';
------ END : Update Application types for workflow ---

--rollback UPDATE EG_WF_MATRIX SET additionalrule = 'NEWCONTRACTORBILLREGISTER' WHERE objecttype = 'ContractorBillRegister' and additionalrule = null;
--rollback UPDATE EG_WF_MATRIX SET additionalrule = 'NEWLINEESTIMATE' WHERE objecttype = 'LineEstimate' and additionalrule = null;
--rollback UPDATE EG_WF_TYPES SET displayname = 'Contractor Bill Register' WHERE type = 'ContractorBillRegister';