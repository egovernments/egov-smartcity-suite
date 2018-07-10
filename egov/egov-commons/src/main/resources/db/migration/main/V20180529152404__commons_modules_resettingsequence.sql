SELECT setval('seq_eg_modules', (select max(id) from eg_modules));
