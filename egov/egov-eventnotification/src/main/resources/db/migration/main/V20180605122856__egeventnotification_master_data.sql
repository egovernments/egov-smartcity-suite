INSERT INTO egevntnotification_drafttype(id, name) VALUES (nextval('seq_egevntnotification_drafttype'), 'General');
INSERT INTO egevntnotification_drafttype(id, name) VALUES (nextval('seq_egevntnotification_drafttype'), 'Business');

INSERT INTO egevntnotification_eventtype(id, name) VALUES (nextval('seq_egevntnotification_eventtype'), 'Business');
INSERT INTO egevntnotification_eventtype(id, name) VALUES (nextval('seq_egevntnotification_eventtype'), 'Exhibition');
INSERT INTO egevntnotification_eventtype(id, name) VALUES (nextval('seq_egevntnotification_eventtype'), 'Cultural');
INSERT INTO egevntnotification_eventtype(id, name) VALUES (nextval('seq_egevntnotification_eventtype'), 'Drama');

INSERT INTO egevntnotification_schedulerepeat(id, name) VALUES (nextval('seq_egevntnotification_schedulerepeat'), 'Month');
INSERT INTO egevntnotification_schedulerepeat(id, name) VALUES (nextval('seq_egevntnotification_schedulerepeat'), 'Day');
INSERT INTO egevntnotification_schedulerepeat(id, name) VALUES (nextval('seq_egevntnotification_schedulerepeat'), 'Year');
INSERT INTO egevntnotification_schedulerepeat(id, name) VALUES (nextval('seq_egevntnotification_schedulerepeat'), 'One Time');



