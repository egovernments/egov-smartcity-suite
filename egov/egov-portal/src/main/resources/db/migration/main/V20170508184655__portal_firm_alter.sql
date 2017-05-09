alter table egp_firmusers rename to egp_firmuser;
alter table egp_inbox rename COLUMN header_msg to headermessage;
alter table egp_inbox rename COLUMN isresolved to resolved;

--rollback alter table egp_firmuser rename to egp_firmusers;