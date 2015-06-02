DROP VIEW v_eg_role_action_module_map;
alter table eg_module rename column id_module to id;
alter table eg_module rename column module_name to "name";
alter table eg_module rename column baseurl to contextRoot;
alter table eg_module drop column lastupdatedtimestamp;
alter table eg_module rename column isenabled to enabled;
alter table eg_module alter enabled TYPE bool USING CASE WHEN enabled=0 THEN FALSE ELSE TRUE END;
alter table eg_module drop column module_namelocal;
alter table eg_module rename column parentid to parentmodule;
alter table eg_module rename column module_desc to displayName;
alter table eg_module rename column order_num to orderNumber;
alter table eg_module add column "version" bigint;
alter table eg_action alter is_enabled TYPE bool USING CASE WHEN is_enabled=0 THEN FALSE ELSE TRUE END;
update eg_module set "version"=0;

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