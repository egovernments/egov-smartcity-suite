alter table  egcncl_meeting_mom add column meeting bigint not null;

ALTER TABLE egcncl_meeting_mom ADD CONSTRAINT fk_egcncl_mngmom_mtng FOREIGN KEY (meeting)  REFERENCES egcncl_meeting (id) ;