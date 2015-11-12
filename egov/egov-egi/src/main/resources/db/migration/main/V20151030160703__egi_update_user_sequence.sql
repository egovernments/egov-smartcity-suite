SELECT setval('"seq_eg_user"',
       (SELECT MAX(ID) FROM eg_user ));