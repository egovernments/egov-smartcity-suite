update eg_module set ordernumber=1 where "name" = 'Administration';
update eg_module set ordernumber=2 where "name" = 'EIS';
update eg_module set ordernumber=3 where "name" = 'PGR';
update eg_module set ordernumber=4 where "name" = 'EGF';
update eg_module set ordernumber=5 where "name" = 'Collection';
update eg_module set ordernumber=6 where "name" = 'Property Tax';
update eg_module set ordernumber=7 where "name" = 'Water Tax Management';
update eg_module set ordernumber=8 where "name" = 'Asset Management';
update eg_module set ordernumber=9 where "name" = 'BPA';
delete from eg_module where "name"='BpaCitizenPortal';

update eg_module set enabled=false where "name" in ('BPA','Asset Management');
update eg_module set ordernumber = 4 where "name"='Department';
update eg_module set ordernumber = 2 where "name"='User Module';
update eg_module set ordernumber = 3 where "name"='Configuration';
update eg_module set ordernumber = 1 where "name"='Boundary Module';

update eg_module set ordernumber = 2 where "name"='Boundary Type';
update eg_module set ordernumber = 1 where "name"='Hierarchy Type';
update eg_module set ordernumber = 3 where "name"='Boundary';