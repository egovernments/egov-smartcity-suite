drop index IF EXISTS idx_pgr_complaint_receivingcenter;
alter table egpgr_complaint drop constraint IF EXISTS uk_complaint_crn;
