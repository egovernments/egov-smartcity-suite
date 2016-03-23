SELECT setval('SEQ_EGWTR_USAGE_TYPE', COALESCE((SELECT MAX(id)+1 FROM egwtr_usage_type), 1), false);
SELECT setval('SEQ_EGWTR_PROPERTY_TYPE', COALESCE((SELECT MAX(id)+1 FROM egwtr_property_type), 1), false);
SELECT setval('SEQ_EGWTR_CATEGORY', COALESCE((SELECT MAX(id)+1 FROM egwtr_category), 1), false);
SELECT setval('SEQ_EGWTR_PIPESIZE', COALESCE((SELECT MAX(id)+1 FROM egwtr_pipesize), 1), false);
SELECT setval('SEQ_EGWTR_PROPERTY_USAGE', COALESCE((SELECT MAX(id)+1 FROM egwtr_property_usage), 1), false);
SELECT setval('SEQ_EGWTR_PROPERTY_CATEGORY', COALESCE((SELECT MAX(id)+1 FROM egwtr_property_category), 1), false);
SELECT setval('SEQ_EGWTR_PROPERTY_PIPESIZE', COALESCE((SELECT MAX(id)+1 FROM egwtr_property_pipe_size), 1), false);