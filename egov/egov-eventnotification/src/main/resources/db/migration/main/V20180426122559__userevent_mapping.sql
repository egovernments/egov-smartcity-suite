CREATE SEQUENCE seq_eg_userevent;

CREATE TABLE eg_userevent
(
  id bigint NOT NULL PRIMARY KEY,
  userid bigint NOT NULL,
  eventid bigint NOT NULL,
  version bigint
)
