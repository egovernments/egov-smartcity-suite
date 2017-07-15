CREATE TABLE egcncl_meeting_document
(
  filestoreid bigint NOT NULL,
  meetingid bigint NOT NULL,
  CONSTRAINT fk_egcncl_meeting FOREIGN KEY (meetingid)
      REFERENCES egcncl_meeting (id)
);
