DROP VIEW IF EXISTS view_eg_menulink;

CREATE MATERIALIZED VIEW view_eg_menulink AS
 SELECT m.id AS module_id,
    m.displayname AS module_name,
    m.parentmodule AS parent_id,
    NULL::bigint AS action_id,
    NULL::character varying AS action_name,
    NULL::text AS action_url,
    m.ordernumber AS order_number,
    'M'::text AS typeflag,
    m.enabled AS is_enabled,
    NULL::character varying AS context_root
   FROM eg_module m
UNION
 SELECT NULL::bigint AS module_id,
    NULL::character varying AS module_name,
    a.parentmodule AS parent_id,
    a.id AS action_id,
    a.displayname AS action_name,
    ((a.url)::text ||
        CASE
            WHEN (a.queryparams IS NULL) THEN ''::text
            ELSE ('?'::text || (a.queryparams)::text)
        END) AS action_url,
    a.ordernumber AS order_number,
    'A'::text AS typeflag,
    a.enabled AS is_enabled,
    a.contextroot AS context_root
   FROM eg_action a;
