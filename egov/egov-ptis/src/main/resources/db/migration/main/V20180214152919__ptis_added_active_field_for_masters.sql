-- Adding active field to master tables
ALTER TABLE egpt_apartment
  ADD column active boolean DEFAULT true;
ALTER TABLE egpt_floor_type 
  ADD column active boolean DEFAULT true;
ALTER TABLE egpt_roof_type
  ADD column active boolean DEFAULT true;
ALTER TABLE egpt_wall_type
  ADD column active boolean DEFAULT true;
ALTER TABLE egpt_wood_type
  ADD column active boolean DEFAULT true;

-- Updating display names for more clarity

update eg_action set displayname ='View Floor Type' where name='Search Floor Type' and contextroot='ptis';
update eg_action set displayname ='Add Floor Type' where name='Create Floor Type' and contextroot='ptis';
update eg_action set displayname ='View Roof Type' where name='Search Roof Type' and contextroot='ptis';
update eg_action set displayname ='Add Roof Type' where name='Create Roof Type' and contextroot='ptis';
update eg_action set displayname ='View Wall Type' where name='Search Wall Type' and contextroot='ptis';
update eg_action set displayname ='Add Wall Type' where name='Create Wall Type' and contextroot='ptis';
update eg_action set displayname ='View Wall Type' where name='Search Wall Type' and contextroot='ptis';
update eg_action set displayname ='Add Wall Type' where name='Create Wall Type' and contextroot='ptis';
