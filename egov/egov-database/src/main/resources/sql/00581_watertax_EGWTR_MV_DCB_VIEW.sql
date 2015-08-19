create view EGWTR_MV_DCB_VIEW as (SELECT bp.propertyid,
  bpaddr.housenobldgapt
  ||bpaddr.subnumber
  ||bpaddr.address as address,
  con.consumercode as hscno,
  usr.name as username,
  propid.zone_num as zoneid,
  propid.WARD_ADM_ID as wardid,
  propid.ADM1 as block,
  propid.ADM2 as locality ,
  propid.ADM3 as street,
  cond.connectiontype as connectiontype,
  currdemd.amount        AS curr_demand,
  currdemd.amt_collected AS curr_coll,
  currdemd.amount -currdemd.amt_collected    AS curr_balance,
  arrdemd.amount           AS arr_demand,
  arrdemd.amt_collected    AS arr_coll,
  arrdemd.amount -arrdemd.amt_collected        AS arr_balance
FROM egwtr_connection con,
  egwtr_connectiondetails cond,
  egpt_basic_property bp,
  eg_demand dem,
  eg_demand_details currdemd,
  eg_demand_details arrdemd,
  egpt_mv_bp_address bpaddr,
  egpt_property_owner_info propinfo,
  eg_user usr,
  egpt_propertyid propid
WHERE  bp.id_propertyid=propid.id and propinfo.basicproperty=bp.id
AND propinfo.owner          =usr.id
AND cond.connection         =con.id
AND cond.demand             =dem.id
AND cond.connectionstatus   = 'ACTIVE'
AND cond.state_id           =
  (SELECT id FROM eg_wf_states WHERE value='ACTIVE'
  )
AND con.propertyidentifier       =bp.propertyid
AND bpaddr.id_basic_property     =bp.id
AND currdemd.id_demand         =dem.id
AND currdemd.id_demand_reason IN
  (SELECT id
  FROM eg_demand_reason
  WHERE id_demand_reason_master =
    (SELECT id
    FROM eg_demand_reason_master
    WHERE code='WTAXCHARGES'
    )
  AND id_installment=((
    (SELECT id_installment
    FROM eg_installment_master
    WHERE eg_installment_master.start_date <= now()
    AND eg_installment_master.end_date     >= now()
    AND eg_installment_master.id_module     = (
      (SELECT eg_module.id
      FROM eg_module
      WHERE eg_module.name::text = 'Water Tax Management'::text
      ))
    )))
  )
AND arrdemd.id_demand         =dem.id
AND arrdemd.id_demand_reason IN
  (SELECT id
  FROM eg_demand_reason
  WHERE id_demand_reason_master =
    (SELECT id
    FROM eg_demand_reason_master
    WHERE code='WTAXCHARGES'
    )
  AND id_installment NOT IN ((
    (SELECT id_installment
    FROM eg_installment_master
    WHERE eg_installment_master.start_date <= now()
    AND eg_installment_master.end_date     >= now()
    AND eg_installment_master.id_module     = (
      (SELECT eg_module.id
      FROM eg_module
      WHERE eg_module.name::text = 'Water Tax Management'::text
      ))
    )))
  )
  and
  cond.connectiontype='METERED')
  union
  (
  SELECT bp.propertyid,
  bpaddr.housenobldgapt
  ||bpaddr.subnumber
  ||bpaddr.address as address,
  con.consumercode as hscno,
  usr.name as username,
  propid.zone_num as zoneid,
  propid.WARD_ADM_ID as wardid,
  propid.ADM1 as block,
  propid.ADM2 as locality ,
  propid.ADM3 as street,
  cond.connectiontype as connectiontype,
  currdemd.amount        AS curr_demand,
  currdemd.amt_collected AS curr_coll,
  currdemd.amount -currdemd.amt_collected    AS curr_balance,
  arrdemd.amount           AS arr_demand,
  arrdemd.amt_collected    AS arr_coll,
  arrdemd.amount -arrdemd.amt_collected        AS arr_balance
FROM egwtr_connection con,
  egwtr_connectiondetails cond,
  egpt_basic_property bp,
  eg_demand dem,
  eg_demand_details currdemd,
  eg_demand_details arrdemd,
  egpt_mv_bp_address bpaddr,
  egpt_property_owner_info propinfo,
  eg_user usr,
  egpt_propertyid propid
WHERE  bp.id_propertyid=propid.id and propinfo.basicproperty=bp.id
AND propinfo.owner          =usr.id
AND cond.connection         =con.id
AND cond.demand             =dem.id
AND cond.connectionstatus   = 'ACTIVE'
AND cond.state_id           =
  (SELECT id FROM eg_wf_states WHERE value='ACTIVE'
  )
AND con.propertyidentifier       =bp.propertyid
AND bpaddr.id_basic_property     =bp.id
AND currdemd.id_demand         =dem.id
AND currdemd.id_demand_reason IN
  (SELECT id
  FROM eg_demand_reason
  WHERE id_demand_reason_master =
    (SELECT id
    FROM eg_demand_reason_master
    WHERE code='WTAXCHARGES'
    )
  AND id_installment=((
    (SELECT id_installment
    FROM eg_installment_master
    WHERE eg_installment_master.start_date <= now()
    AND eg_installment_master.end_date     >= now()
    AND eg_installment_master.id_module     = (
      (SELECT eg_module.id
      FROM eg_module
      WHERE eg_module.name::text = 'Property Tax'::text
      ))
    )))
  )
AND arrdemd.id_demand         =dem.id
AND arrdemd.id_demand_reason IN
  (SELECT id
  FROM eg_demand_reason
  WHERE id_demand_reason_master =
    (SELECT id
    FROM eg_demand_reason_master
    WHERE code='WTAXCHARGES'
    )
  AND id_installment NOT IN ((
    (SELECT id_installment
    FROM eg_installment_master
    WHERE eg_installment_master.start_date <= now()
    AND eg_installment_master.end_date     >= now()
    AND eg_installment_master.id_module     = (
      (SELECT eg_module.id
      FROM eg_module
      WHERE eg_module.name::text = 'Property Tax'::text
      ))
    )))

 and cond.connectiontype='NON_METERED')

  ); 
  
--  drop view EGWTR_MV_DCB_VIEW;
