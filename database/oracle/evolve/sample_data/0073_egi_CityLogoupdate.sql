#UP
update eg_city_website ecw
set logo='chennaicmc.jpg'
where
ecw.BNDRYID in(select eb.ID_BNDRY
from eg_boundary eb, eg_boundary_type ebt,eg_heirarchy_type eht
where
eb.ID_BNDRY_TYPE=ebt.ID_BNDRY_TYPE
and ebt.ID_HEIRARCHY_TYPE=eht.ID_HEIRARCHY_TYPE
and ebt.NAME in('city','City'))
and cityname='Corporation of Chennai';
#DOWN
