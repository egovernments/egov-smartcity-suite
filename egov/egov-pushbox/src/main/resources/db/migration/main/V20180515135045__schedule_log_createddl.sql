CREATE SEQUENCE seq_egevntnotification_schedule_log;
CREATE TABLE egevntnotification_schedule_log
(
  id bigint NOT NULL,
  filestore bigint NOT NULL,
  createddate bigint NOT NULL,
  CONSTRAINT egevntnotification_schedule_log_pkey PRIMARY KEY (id)
);
update eg_action set displayname='Event' where name='Event'
