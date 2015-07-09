update eg_boundary_type set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;
update eg_boundary set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;
update eg_user set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;
update eg_city_website set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;
update eg_department set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;
update eg_hierarchy_type set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;
update eg_role set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;

update pgr_complainttype set createdby = 1,createddate='01-01-2010',lastmodifiedby=1,lastmodifieddate='01-01-2015',"version"=0;
ALTER TABLE pgr_complainttype RENAME TO egpgr_complainttype;
ALTER SEQUENCE seq_pgr_complainttype RENAME TO seq_egpgr_complainttype;
ALTER TABLE eg_module ALTER contextroot type varchar(10);
DROP VIEW VIEW_EG_MENULINK;
ALTER TABLE eg_module ALTER displayname type varchar(50);
CREATE OR REPLACE VIEW VIEW_EG_MENULINK AS 
 SELECT m.id AS module_id,
    m.displayName AS module_name,
    m.parentModule AS parent_id,
    NULL::bigint AS action_id,
    NULL::character varying AS action_name,
    NULL::text AS action_url,
    m.orderNumber AS order_number,
    'M'::text AS typeflag,
    m.enabled AS is_enabled,
    NULL::character varying AS context_root
   FROM eg_module m
UNION
 SELECT NULL::bigint AS module_id,
    NULL::character varying AS module_name,
    a.module_id AS parent_id,
    a.id AS action_id,
    a.display_name AS action_name,
    a.url::text ||
        CASE
            WHEN a.queryparams IS NULL THEN ''::text
            ELSE '?'::text || a.queryparams::text
        END AS action_url,
    a.order_number,
    'A'::text AS typeflag,
    a.is_enabled,
    a.context_root
   FROM eg_action a;

DROP TABLE eg_audit_event; 
DROP SEQUENCE eg_audit_event_seq; 
DROP TABLE eg_address_type_master CASCADE;
ALTER TABLE EGEIS_EMPLOYEE ADD COLUMN "version" numeric default 0;