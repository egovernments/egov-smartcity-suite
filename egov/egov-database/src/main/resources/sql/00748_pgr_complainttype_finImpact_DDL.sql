alter table egpgr_complainttype add column hasfinancialimpact boolean;
update egpgr_complainttype  SET hasfinancialimpact = false;
