Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_Action where name='Search Property' and contextroot='ptis'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_Action where name='Search Property By Bndry' and contextroot='ptis'));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_Action where name='Populate Wards' and contextroot='ptis'));
