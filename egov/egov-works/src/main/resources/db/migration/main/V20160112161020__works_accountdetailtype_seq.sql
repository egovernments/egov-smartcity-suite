SELECT setval('"seq_accountdetailtype"',
(SELECT MAX(ID) FROM accountdetailtype ));