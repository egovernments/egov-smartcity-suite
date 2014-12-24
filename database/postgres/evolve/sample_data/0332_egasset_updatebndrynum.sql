#UP
update EG_BOUNDARY set bndry_num= id_bndry where bndry_num is null;
#DOWN
update EG_BOUNDARY set bndry_num = null where bndry_num = id_bndry;