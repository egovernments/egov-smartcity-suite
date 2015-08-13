  
INSERT INTO egwtr_road_category(id, name) VALUES (nextval('SEQ_EGWTR_ROAD_CATEGORY'), 'Municipal Road');
INSERT INTO egwtr_road_category(id, name) VALUES (nextval('SEQ_EGWTR_ROAD_CATEGORY'), 'CC Road');
INSERT INTO egwtr_road_category(id, name) VALUES (nextval('SEQ_EGWTR_ROAD_CATEGORY'), 'R & B Road');
INSERT INTO egwtr_road_category(id, name) VALUES (nextval('SEQ_EGWTR_ROAD_CATEGORY'), 'BT Road');

--rollback delete from egwtr_road_category;