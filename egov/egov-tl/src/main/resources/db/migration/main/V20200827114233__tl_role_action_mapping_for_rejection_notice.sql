Insert into EG_ROLEACTION values ((select id from eg_role where name like 'TL VIEW ACCESS'),(select id from eg_action where name='Search Rejection Notice' and contextroot='tl'));

