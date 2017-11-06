SELECT setval('SEQ_EG_OWNERGROUP', (select max(id) from eg_position));
