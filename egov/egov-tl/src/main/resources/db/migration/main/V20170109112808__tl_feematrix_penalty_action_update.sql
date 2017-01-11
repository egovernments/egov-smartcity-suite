
DELETE FROM eg_roleaction WHERE actionid IN (select id FROM eg_action WHERE name ='tradeLicenseLocalityAjax');

DELETE FROM eg_action WHERE name ='tradeLicenseLocalityAjax';

UPDATE eg_action SET url ='/search/tradeLicense' WHERE name ='SearchAjax-PopulateData';
 
UPDATE eg_action SET url ='/feematrix/deleterow' WHERE name ='Delete Fee Matrix';

UPDATE eg_action SET url ='/penaltyRates/deleterow' WHERE name ='deletepenaltyrate';