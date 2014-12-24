--To generate GRANT script for erp_ro 
GRANT USAGE on schema egoverp to erp_ro;
\o grant_ro.sql
select 'grant select on public.' || table_name || ' to erp_ro;' as qry from dba_tables where schema_name='public' order by table_name;

--To generate GRANT script for erp_ro sequences
\o grant_ro_seq.sql
select 'grant usage, select on sequence public.' ||sequence_name || ' to erp_ro;' as qry from dba_sequences where sequence_owner='ERP_OWNER' order by sequence_name;

--To generate GRANT script for erp_user 
GRANT USAGE on schema egoverp to erp_user;
\o grant_user.sql
select 'grant select, insert, update, delete on public' || table_name || ' to erp_user;' as qry from dba_tables where schema_name='public' order by table_name;

--To generate GRANT script for erp_user sequences
\o grant_user_seq.sql
select 'grant usage, select, update on sequence public.' ||sequence_name || ' to erp_user;' as qry from dba_sequences where sequence_owner='ERP_OWNER' order by sequence_name;


