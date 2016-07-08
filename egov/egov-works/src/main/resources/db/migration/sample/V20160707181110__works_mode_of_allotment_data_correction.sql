----------Update script to update mode of allotment data-------------
update egw_lineestimate set modeofallotment = 'E-Procurement (For above 1 Lakh)' where modeofallotment = 'ePROCUREMENT';
update egw_lineestimate set modeofallotment = 'Tendering (For below 1 Lakh)' where modeofallotment = 'TENDERING';