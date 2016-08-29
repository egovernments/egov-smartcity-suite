ALTER TABLE eg_wf_types  RENAME renderYN to enabled;
ALTER TABLE eg_wf_types ALTER enabled TYPE boolean USING CASE enabled WHEN 'Y' THEN TRUE ELSE FALSE END;
ALTER TABLE eg_wf_types  RENAME groupYN to grouped;
ALTER TABLE eg_wf_types ALTER grouped TYPE boolean USING CASE grouped WHEN 'Y' THEN TRUE ELSE FALSE END;
UPDATE eg_wf_types SET createdby=1,createddate=now(),lastmodifiedby=1,lastmodifieddate=now(),version=0;