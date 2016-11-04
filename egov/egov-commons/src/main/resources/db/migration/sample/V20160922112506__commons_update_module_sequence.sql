SELECT setval('"seq_eg_module"',(SELECT MAX(ID) FROM eg_module ));
