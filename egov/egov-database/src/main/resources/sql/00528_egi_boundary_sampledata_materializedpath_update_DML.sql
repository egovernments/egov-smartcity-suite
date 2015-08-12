UPDATE eg_boundary
SET materializedpath = (SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where  parent is null
    ) SubQuery
where SubQuery.id =eg_boundary.id ;

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Srikakulam  Municipality' and 
	boundarytype in(select id from eg_boundary_type where name='City' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Srikakulam  Municipality' and boundarytype 
		in(select id from eg_boundary_type where name='City' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Zone-1' and 
	boundarytype in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Zone-1' and boundarytype 
		in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Zone-2' and 
	boundarytype in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Zone-2' and boundarytype 
		in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Zone-3' and 
	boundarytype in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Zone-3' and boundarytype 
		in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Zone-4' and 
	boundarytype in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Zone-4' and boundarytype 
		in(select id from eg_boundary_type where name='Zone' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));					

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Revenue Ward No 1' and 
	boundarytype in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Revenue Ward No 1' and boundarytype 
		in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Revenue Ward No 2' and 
	boundarytype in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Revenue Ward No 2' and boundarytype 
		in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));				

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Revenue Ward No 3' and 
	boundarytype in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Revenue Ward No 3' and boundarytype 
		in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Revenue Ward No 4' and 
	boundarytype in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Revenue Ward No 4' and boundarytype 
		in(select id from eg_boundary_type where name='Ward' and hierarchytype in(select id from eg_hierarchy_type where name='ADMINISTRATION')));				

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Srikakulam  Municipality' and 
	boundarytype in(select id from eg_boundary_type where name='City' and hierarchytype in(select id from eg_hierarchy_type where name='LOCATION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Srikakulam  Municipality' and boundarytype 
		in(select id from eg_boundary_type where name='City' and hierarchytype in(select id from eg_hierarchy_type where name='LOCATION')));

UPDATE eg_boundary
SET materializedpath = (select distinct(materializedpath) from eg_boundary where id=SubQuery.parent)||'.'||(SubQuery.Sort_Order)
FROM
    (
    SELECT id,parent, Row_Number() OVER (ORDER BY id) as SORT_ORDER
    FROM eg_boundary where parent in(select id from eg_boundary where name='Srikakulam  Municipality' and 
	boundarytype in(select id from eg_boundary_type where name='City' and hierarchytype in(select id from eg_hierarchy_type where name='ELECTION')))
    ) SubQuery
where SubQuery.id =eg_boundary.id and eg_boundary.parent 
	in(select id from eg_boundary where name='Srikakulam  Municipality' and boundarytype 
		in(select id from eg_boundary_type where name='City' and hierarchytype in(select id from eg_hierarchy_type where name='ELECTION')));				



