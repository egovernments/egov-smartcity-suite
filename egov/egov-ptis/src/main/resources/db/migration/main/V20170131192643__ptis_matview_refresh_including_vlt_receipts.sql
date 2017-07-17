CREATE OR REPLACE FUNCTION egptUpdateMatView(noofdays integer)
  RETURNS void AS $$
declare
	v_basicpropertyid   		bigint;
	v_upicno    				character varying(32);
	v_ownersname    			character varying;
	v_aadharno    				character varying;
	v_houseno    				character varying(32);
	v_mobileno    				character varying;
	v_address    				text;
	v_proptymaster    			bigint;
	v_wardid    				bigint;
	v_zoneid    				bigint;
	v_streetid    				bigint;
	v_blockid    				bigint;
	v_localityid    			bigint;
	v_electionwardid    		bigint;
	v_source_id    				bigint;
	v_sital_area    			double precision;
	v_total_builtup_area    	double precision;
	v_latest_status    			bigint;
	v_aggregate_arrear_demand   double precision;
	v_arrearcollection    		double precision;
	v_pen_aggr_arrear_demand    double precision;
	v_pen_aggr_arr_coll    		double precision;
	v_aggregate_curr1st_demand  double precision;
	v_curr1st_collection    	double precision;
	v_aggregate_curr2nd_demand  double precision;
	v_curr2nd_collection    	double precision;
	v_pen_aggr_curr1st_demand   double precision;
	v_pen_aggr_curr1st_coll    	double precision;
	v_pen_aggr_curr2nd_demand   double precision;
	v_pen_aggr_curr2nd_coll    	double precision;
	v_gisrefno    				character varying(32);
	v_isexempted    			boolean;
	v_usage    					character varying(256);
	v_source    				character varying;
	v_alv    					double precision;	
	v_longitude    				double precision;
	v_latitude    				double precision;
	v_annualdemand    			double precision;
	v_annualcoll    			double precision;
	v_isactive    				boolean;
	v_regdocno					character varying(64);
	v_regdocdate				date;
	v_assessmentdate			date;
	v_capitalvalue				double precision;
	v_marketvalue				double precision;
	v_pattanumber				character varying(64);
	
	v_iddemand 					bigint;
	v_curr1stinst 				bigint;
	v_curr2ndinst 				bigint;
	v_addressid 				bigint;
	v_moduleid 					bigint;
	v_idproperty 				bigint;
	v_idpropdetail 				bigint;
	v_assessmentno 				character varying(64);
	v_finyearstartdate 			date;
	v_finyearenddate 			date;
	v_lastupdated				date;
	v_surveyno					character varying(64);
	v_courtcase    				boolean;
	v_period    				character varying;
	v_frominstallment			character varying;
	v_toinstallment				character varying;
	v_advance    				double precision default 0;
	v_totaldue    				double precision default 0;
	v_oldmunicipalno			character varying;
	modifiedrecords 			record;
BEGIN
	--raise notice 'egptUpdateMatView : view dropped';
	--adding newly created properties
	insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'NEW_ASSESSMENT' from egpt_basic_property where propertyid is not null and propertyid not in (select upicno from egpt_mv_propertyinfo);
	insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'NEW_ASSESSMENT' from egpt_basic_property where propertyid is not null and propertyid in (select propertyid from egpt_basic_property where propertyid is not null except select upicno from egpt_mv_propertyinfo);

	--adding missing demand props
	insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'MISSING' from egpt_basic_property where propertyid is not null and propertyid in (select upicno from egpt_mv_propertyinfo where source='D' and (annualdemand is null or annualdemand=0));

	--adding modified properties
	insert into egpt_mv_assessments (id, assessmentno, date_modified, type) SELECT nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'ASSESSMENT' FROM egpt_basic_property WHERE propertyid is not null and lastmodifieddate::date  >= NOW()::date - noofdays;

	--adding collected properties
	insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), consumercode, lastmodifieddate, 'COLLECTION' from egcl_collectionheader where servicedetails in (select id from egcl_servicedetails where upper(name) in ('PROPERTY TAX','PROPERTY TAX (ON LAND)')) and status in (select id from egw_status where moduletype='ReceiptHeader' and code in ('TO_BE_SUBMITTED','SUBMITTED','APPROVED','REMITTED')) and lastmodifieddate::date  >= NOW()::date - noofdays;

	--adding Edit Demands
	insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), assessmentno, lastmodifieddate, 'EDITDEMAND' from EGPT_DEMANDAUDIT where lastmodifieddate::date  >= NOW()::date - noofdays;

	--raise notice 'egptUpdateMatView : data logged';
	select date(startingdate), date(endingdate) into v_finyearstartdate, v_finyearenddate from financialyear where now() between startingdate and endingdate;
	select id into v_moduleid from eg_module where name='Property Tax';
	select id into v_curr1stinst from eg_installment_master where id_module=v_moduleid and date(start_date)=v_finyearstartdate;
	select id into v_curr2ndinst from eg_installment_master where id_module=v_moduleid and date(end_date)=v_finyearenddate;
	--raise notice 'egptUpdateMatView : v_moduleid, v_curr1stinst, v_curr2ndinst (% % %)', v_moduleid, v_curr1stinst, v_curr2ndinst;
	for modifiedrecords in (select distinct assessmentno from egpt_mv_assessments where isUpdated = false)
	loop
	begin
		v_assessmentno := modifiedrecords.assessmentno;
		--raise notice 'egptUpdateMatView : v_assessmentno (%)', v_assessmentno;
		DELETE from egpt_mv_propertyinfo where upicno = v_assessmentno;
		--raise notice 'egptUpdateMatView : deleted from mv and started getting latest data for assesssment (% %)', v_assessmentno , v_basicpropertyid;

		--getting property basic details
		SELECT prop.id, bp.id, bp.propertyid, ownername(bp.id), getAadharno(bp.id), mobilenumber(bp.id), bp.MUNICIAPL_NO_OLD, propdet.id_propertytypemaster, propid.ward_adm_id, propid.zone_num, propid.adm3, propid.adm1, propid.adm2, propid.elect_bndry, propdet.sital_area, propdet.total_builtup_area, bp.status, bp.gis_ref_no, prop.isexemptedfromtax, propdet.category_type, bp.source, bp.longitude, bp.latitude, bp.isactive, bp.addressid, bp.regd_doc_no, bp.regd_doc_date, bp.assessmentdate, propdet.current_capitalvalue, propdet.marketvalue, propdet.pattanumber, propdet.id, propdet.survey_num      
		into 
		v_idproperty, v_basicpropertyid, v_upicno, v_ownersname, v_aadharno, v_mobileno, v_oldmunicipalno, v_proptymaster, v_wardid, v_zoneid, v_streetid, v_blockid, v_localityid, v_electionwardid, v_sital_area, v_total_builtup_area, v_latest_status, v_gisrefno, v_isexempted, v_usage, v_source, v_longitude, v_latitude, v_isactive, v_addressid, v_regdocno, v_regdocdate, v_assessmentdate, v_capitalvalue, v_marketvalue, v_pattanumber, v_idpropdetail, v_surveyno
		FROM egpt_basic_property bp,
		egpt_property prop,
		egpt_property_detail propdet,
		egpt_propertyid propid
		WHERE prop.id_basic_property = bp.id AND propdet.id_property = prop.id AND propid.id = bp.id_propertyid and prop.status in ('I','A') and bp.propertyid = v_assessmentno;
		--raise notice 'egptUpdateMatView : got basic details';

		select (case when exists (select assessmentno from egpt_courtcases where assessmentno=v_assessmentno) then true else false end) into v_courtcase;

		--getting address details
		select addr.housenobldgapt, (CASE WHEN addr.housenobldgapt IS NOT NULL THEN addr.housenobldgapt ELSE '' END ||
		CASE WHEN addr.streetroadline IS NOT NULL THEN ', ' || addr.streetroadline ELSE '' END ||
		CASE WHEN addr.arealocalitysector IS NOT NULL THEN ', ' || addr.arealocalitysector ELSE '' END ||
		CASE WHEN addr.landmark IS NOT NULL THEN ', ' || addr.landmark ELSE '' END ||
		CASE WHEN addr.citytownvillage IS NOT NULL THEN ', ' || addr.citytownvillage ELSE '' END ||
		CASE WHEN addr.pincode IS NOT NULL THEN ' ' || addr.pincode ELSE '' END)
		into v_houseno, v_address
		from eg_address addr where addr.id=v_addressid;
		--raise notice 'egptUpdateMatView : got address details';

		--getting current demand id
		select d.id into v_iddemand from egpt_ptdemand ptd, eg_demand d where d.id=ptd.id_demand and d.id_installment=v_curr1stinst and ptd.id_property=v_idproperty;
		--raise notice 'egptUpdateMatView : got current demand %', v_iddemand;

		--getting alv
		 SELECT (CASE dmdcal.alv WHEN NULL::double precision THEN 0::double precision ELSE dmdcal.alv END) into v_alv FROM egpt_demandcalculations dmdcal WHERE dmdcal.id_demand = v_iddemand;
		--raise notice 'egptUpdateMatView : got alv';

		--getting Arrear tax details
		select sum(COALESCE(dd.amount, 0)), sum(COALESCE(dd.amt_collected, 0)) 
		into v_aggregate_arrear_demand, v_arrearcollection
		from eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm
		where dd.id_demand_reason=dr.id 
		and dr.id_demand_reason_master=drm.id 
		and drm.module=v_moduleid
		and drm.code::text not in ('CHQ_BUNC_PENALTY'::character varying, 'PENALTY_FINES'::character varying)
		and dr.id_installment not in (v_curr1stinst, v_curr2ndinst)
		and dd.id_demand=v_iddemand;
		--raise notice 'egptUpdateMatView got arrear taxes: v_aggregate_arrear_demand, v_arrearcollection (% %)', v_aggregate_arrear_demand, v_arrearcollection;

		--getting Arrear penalty details
		select sum(COALESCE(dd.amount, 0)), sum(COALESCE(dd.amt_collected, 0))  
		into v_pen_aggr_arrear_demand, v_pen_aggr_arr_coll
		from eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm
		where dd.id_demand_reason=dr.id 
		and dr.id_demand_reason_master=drm.id 
		and drm.module=v_moduleid
		and drm.code = 'PENALTY_FINES'
		and dr.id_installment not in (v_curr1stinst, v_curr2ndinst)
		and dd.id_demand=v_iddemand;
		--raise notice 'egptUpdateMatView : got arrear penalty tax, v_pen_aggr_arrear_demand, v_pen_aggr_arr_coll (% %)', v_pen_aggr_arrear_demand, v_pen_aggr_arr_coll;

		--getting Current 1st half tax details
		select sum(COALESCE(dd.amount, 0)), sum(COALESCE(dd.amt_collected, 0)) 
		into v_aggregate_curr1st_demand, v_curr1st_collection
		from eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm
		where dd.id_demand_reason=dr.id 
		and dr.id_demand_reason_master=drm.id 
		and drm.module=v_moduleid
		and drm.code::text not in ('CHQ_BUNC_PENALTY'::character varying, 'PENALTY_FINES'::character varying)
		and dr.id_installment=v_curr1stinst
		and dd.id_demand=v_iddemand;
		--raise notice 'egptUpdateMatView got Current 1st half tax details: v_aggregate_curr1st_demand, v_curr1st_collection (% %)', v_aggregate_curr1st_demand, v_curr1st_collection;

		--getting Current 2nd half tax details
		select sum(COALESCE(dd.amount, 0)), sum(COALESCE(dd.amt_collected, 0)) 
		into v_aggregate_curr2nd_demand, v_curr2nd_collection
		from eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm
		where dd.id_demand_reason=dr.id 
		and dr.id_demand_reason_master=drm.id 
		and drm.module=v_moduleid
		and drm.code::text not in ('CHQ_BUNC_PENALTY'::character varying, 'PENALTY_FINES'::character varying)
		and dr.id_installment=v_curr2ndinst
		and dd.id_demand=v_iddemand;
		--raise notice 'egptUpdateMatView got Current 2nd half tax details : v_aggregate_curr2nd_demand, v_curr2nd_collection (% %)', v_aggregate_curr2nd_demand, v_curr2nd_collection;

		--getting Current 1st penalty details
		select sum(COALESCE(dd.amount, 0)), sum(COALESCE(dd.amt_collected, 0)) 
		into v_pen_aggr_curr1st_demand, v_pen_aggr_curr1st_coll
		from eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm
		where dd.id_demand_reason=dr.id 
		and dr.id_demand_reason_master=drm.id 
		and drm.module=v_moduleid
		and drm.code = 'PENALTY_FINES'
		and dr.id_installment=v_curr1stinst
		and dd.id_demand=v_iddemand;
		--raise notice 'egptUpdateMatView got Current 1st penalty details : v_pen_aggr_curr1st_demand, v_pen_aggr_curr1st_coll (% %)', v_pen_aggr_curr1st_demand, v_pen_aggr_curr1st_coll;

		--getting Current 2nd penalty details
		select sum(COALESCE(dd.amount, 0)), sum(COALESCE(dd.amt_collected, 0)) 
		into v_pen_aggr_curr2nd_demand, v_pen_aggr_curr2nd_coll
		from eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm
		where dd.id_demand_reason=dr.id 
		and dr.id_demand_reason_master=drm.id 
		and drm.module=v_moduleid
		and drm.code = 'PENALTY_FINES'
		and dr.id_installment=v_curr2ndinst
		and dd.id_demand=v_iddemand;
		--raise notice 'egptUpdateMatView got Current 1st penalty details : v_pen_aggr_curr2nd_demand, v_pen_aggr_curr2nd_coll (% %)', v_pen_aggr_curr2nd_demand, v_pen_aggr_curr2nd_coll;

		--getting Advance Collection
		select COALESCE(sum(COALESCE(dd.amt_collected, 0)),0)
		into v_advance
		from eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm
		where dd.id_demand_reason=dr.id 
		and dr.id_demand_reason_master=drm.id 
		and drm.module=v_moduleid
		and drm.code = 'ADVANCE'
		and dr.id_installment=v_curr2ndinst
		and dd.id_demand=v_iddemand;
		--raise notice 'egptUpdateMatView got Advance collection details : v_advance (%)', v_advance;

		--calculating annual demand collection
		v_annualdemand := v_aggregate_curr1st_demand + v_aggregate_curr2nd_demand;
		v_annualcoll := v_curr1st_collection + v_curr2nd_collection;

		--raise notice 'egptUpdateMatView : latest data (% % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % %)', v_basicpropertyid, v_upicno, v_ownersname, v_aadharno, v_houseno, v_mobileno, v_address, v_proptymaster, v_wardid, v_zoneid, v_streetid, v_blockid, v_localityid, v_electionwardid, v_source_id, v_sital_area, v_total_builtup_area, v_latest_status, v_aggregate_curr1st_demand, v_aggregate_curr2nd_demand,  v_aggregate_arrear_demand, v_curr1st_collection, v_curr2nd_collection, v_arrearcollection, v_pen_aggr_curr1st_demand, v_pen_aggr_curr1st_coll, v_pen_aggr_curr2nd_demand, v_pen_aggr_curr2nd_coll, v_pen_aggr_arrear_demand, v_pen_aggr_arr_coll, v_gisrefno, v_isexempted, v_usage, v_source, v_alv, v_longitude, v_latitude, v_annualdemand, v_annualcoll, v_isactive, v_regdocno, v_regdocdate, v_assessmentdate, v_capitalvalue, v_marketvalue, v_pattanumber, v_advance;

		select max(date_modified) into v_lastupdated from egpt_mv_assessments where assessmentno=v_assessmentno;

		--updating installment wise taxes
		delete from egpt_mv_inst_dem_coll where id_basic_property=v_basicpropertyid;
		insert into egpt_mv_inst_dem_coll (id_basic_property, id_installment, createddate, generaltax, libcesstax, educesstax, unauthpenaltytax, penaltyfinestax, sewtax, vacantlandtax, pubserchrgtax, generaltaxcoll, libcesstaxcoll, educesstaxcoll, unauthpenaltytaxcoll, penaltyfinestaxcoll, sewtaxcoll, vacantlandtaxcoll, pubserchrgtaxcoll) SELECT dmndcoldtls.basicproperty,  dmndcoldtls.id_installment,
    max(dmndcoldtls.createddate) AS createddate,
    COALESCE(max(dmndcoldtls.generaltax), NULL::bigint, 0::bigint, max(dmndcoldtls.generaltax)) AS generaltax,
    COALESCE(max(dmndcoldtls.libcesstax), NULL::bigint, 0::bigint, max(dmndcoldtls.libcesstax)) AS libcesstax,
    COALESCE(max(dmndcoldtls.educesstax), NULL::bigint, 0::bigint, max(dmndcoldtls.educesstax)) AS educesstax,
    COALESCE(max(dmndcoldtls.unauthpenaltytax), NULL::bigint, 0::bigint, max(dmndcoldtls.unauthpenaltytax)) AS unauthpenaltytax,
    COALESCE(max(dmndcoldtls.penaltyfinestax), NULL::bigint, 0::bigint, max(dmndcoldtls.penaltyfinestax)) AS penaltyfinestax,
    COALESCE(max(dmndcoldtls.sewtax), NULL::bigint, 0::bigint, max(dmndcoldtls.sewtax)) AS sewtax,
    COALESCE(max(dmndcoldtls.vacantlandtax), NULL::bigint, 0::bigint, max(dmndcoldtls.vacantlandtax)) AS vacantlandtax,
    COALESCE(max(dmndcoldtls.pubserchrgtax), NULL::bigint, 0::bigint, max(dmndcoldtls.pubserchrgtax)) AS pubserchrgtax,
    COALESCE(max(dmndcoldtls.generaltaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.generaltaxcoll)) AS generaltaxcoll,
    COALESCE(max(dmndcoldtls.libcesstaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.libcesstaxcoll)) AS libcesstaxcoll,
    COALESCE(max(dmndcoldtls.educesstaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.educesstaxcoll)) AS educesstaxcoll,
    COALESCE(max(dmndcoldtls.unauthpenaltytaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.unauthpenaltytaxcoll)) AS unauthpenaltytaxcoll,
    COALESCE(max(dmndcoldtls.penaltyfinestaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.penaltyfinestaxcoll)) AS penaltyfinestaxcoll,
    COALESCE(max(dmndcoldtls.sewtaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.sewtaxcoll)) AS sewtaxcoll,
    COALESCE(max(dmndcoldtls.vacantlandtaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.vacantlandtaxcoll)) AS vacantlandtaxcoll,
    COALESCE(max(dmndcoldtls.pubserchrgtaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.pubserchrgtaxcoll)) AS pubserchrgtaxcoll
   FROM ( SELECT v_basicpropertyid as basicproperty, dr.id_installment,
            det.create_date AS createddate,
            CASE drm.code WHEN 'GEN_TAX'::text THEN det.amount ELSE NULL::bigint END AS generaltax,
            CASE drm.code WHEN 'LIB_CESS'::text THEN det.amount ELSE NULL::bigint END AS libcesstax,
            CASE drm.code WHEN 'EDU_CESS'::text THEN det.amount ELSE NULL::bigint END AS educesstax,
            CASE drm.code WHEN 'UNAUTH_PENALTY'::text THEN det.amount ELSE NULL::bigint END AS unauthpenaltytax,
            CASE drm.code WHEN 'PENALTY_FINES'::text THEN det.amount ELSE NULL::bigint END AS penaltyfinestax,
            CASE drm.code WHEN 'SEW_TAX'::text THEN det.amount ELSE NULL::bigint END AS sewtax,
            CASE drm.code WHEN 'VAC_LAND_TAX'::text THEN det.amount ELSE NULL::bigint END AS vacantlandtax,
            CASE drm.code WHEN 'PUB_SER_CHRG'::text THEN det.amount ELSE NULL::bigint END AS pubserchrgtax,
            CASE drm.code WHEN 'GEN_TAX'::text THEN det.amt_collected ELSE NULL::double precision END AS generaltaxcoll,
            CASE drm.code WHEN 'LIB_CESS'::text THEN det.amt_collected ELSE NULL::double precision END AS libcesstaxcoll,
            CASE drm.code WHEN 'EDU_CESS'::text THEN det.amt_collected ELSE NULL::double precision END AS educesstaxcoll,
            CASE drm.code WHEN 'UNAUTH_PENALTY'::text THEN det.amt_collected ELSE NULL::double precision END AS unauthpenaltytaxcoll,
            CASE drm.code WHEN 'PENALTY_FINES'::text THEN det.amt_collected ELSE NULL::double precision END AS penaltyfinestaxcoll,
            CASE drm.code WHEN 'SEW_TAX'::text THEN det.amt_collected ELSE NULL::double precision END AS sewtaxcoll,
            CASE drm.code WHEN 'VAC_LAND_TAX'::text THEN det.amt_collected ELSE NULL::double precision END AS vacantlandtaxcoll,
            CASE drm.code WHEN 'PUB_SER_CHRG'::text THEN det.amt_collected ELSE NULL::double precision END AS pubserchrgtaxcoll
           FROM eg_demand_details det,
            eg_demand_reason dr,
            eg_demand_reason_master drm
          WHERE det.id_demand = v_iddemand AND det.id_demand_reason = dr.id AND dr.id_demand_reason_master = drm.id) dmndcoldtls GROUP BY basicproperty, dmndcoldtls.id_installment;

		--raise notice 'egptUpdateMatView : latest data (% % % % % % % % % %)',v_aggregate_arrear_demand,v_annualdemand,v_pen_aggr_arrear_demand,v_pen_aggr_curr1st_demand,v_pen_aggr_curr2nd_demand, v_arrearcollection,v_annualcoll,v_pen_aggr_arr_coll,v_pen_aggr_curr1st_coll,v_pen_aggr_curr2nd_coll;
		--Calculate total due
		v_totaldue := COALESCE(((COALESCE(v_aggregate_arrear_demand,0)+COALESCE(v_annualdemand,0)+COALESCE(v_pen_aggr_arrear_demand,0)+COALESCE(v_pen_aggr_curr1st_demand,0)+COALESCE(v_pen_aggr_curr2nd_demand,0))-(COALESCE(v_arrearcollection,0)+COALESCE(v_annualcoll,0)+COALESCE(v_pen_aggr_arr_coll,0)+COALESCE(v_pen_aggr_curr1st_coll,0)+COALESCE(v_pen_aggr_curr2nd_coll,0))), 0);
		--raise notice 'egptUpdateMatView v_totaldue : (%)', v_totaldue;

		if (v_advance = 0 and v_totaldue > 0) then
			select inst.description into v_frominstallment from egpt_mv_inst_dem_coll idc, eg_installment_master inst where idc.id_installment = inst.id and idc.id_basic_property = v_basicpropertyid and ((idc.generaltax+idc.libcesstax+idc.educesstax+idc.unauthPenaltyTax+idc.penaltyFinesTax+idc.sewtax+idc.vacantLandTax+idc.pubSerChrgTax)-(idc.GENERALTAXCOLL+idc.libCessTaxColl+idc.eduCessTaxColl+idc.unauthPenaltyTaxColl+idc.penaltyFinesTaxColl+idc.sewTaxColl+idc.vacantLandTaxColl+idc.pubSerChrgTaxColl))>0 order by inst.start_date asc limit 1;
	
			select description into v_toinstallment from eg_installment_master where now() between start_date and end_date and id_module = (SELECT id FROM eg_module WHERE name = 'Property Tax');
		
			v_period := v_frominstallment || ' to ' || v_toinstallment;
		else
			v_period := '';
		end if;
		--raise notice 'egptUpdateMatView balance period : (%)', v_period;

		insert into egpt_mv_propertyinfo (basicpropertyid, upicno, ownersname, aadharno, houseno, mobileno, address, proptymaster, wardid, zoneid, streetid, blockid, localityid, electionwardid, source_id, sital_area, total_builtup_area, latest_status, aggregate_arrear_demand, arrearcollection, pen_aggr_arrear_demand, pen_aggr_arr_coll, aggregate_current_firsthalf_demand, aggregate_current_secondhalf_demand, current_firsthalf_collection, current_secondhalf_collection,  pen_aggr_current_firsthalf_demand, pen_aggr_current_secondhalf_demand, pen_aggr_current_firsthalf_coll, pen_aggr_current_secondhalf_coll, gisrefno, isexempted, usage, source, alv, longitude, latitude, annualdemand, annualcoll, isactive, regd_doc_no, regd_doc_date, assessmentdate, capitalvalue, pattano, marketvalue, lastupdated, surveyNo, is_under_courtcase, duePeriod, advance, oldmuncipalnumber) values (v_basicpropertyid, v_upicno, v_ownersname, v_aadharno, v_houseno, v_mobileno, v_address, v_proptymaster, v_wardid, v_zoneid, v_streetid, v_blockid, v_localityid, v_electionwardid, 1, v_sital_area, v_total_builtup_area, v_latest_status, coalesce(v_aggregate_arrear_demand, 0), coalesce(v_arrearcollection, 0), coalesce(v_pen_aggr_arrear_demand, 0), coalesce(v_pen_aggr_arr_coll, 0), coalesce(v_aggregate_curr1st_demand, 0), coalesce(v_aggregate_curr2nd_demand, 0), coalesce(v_curr1st_collection, 0), coalesce(v_curr2nd_collection, 0), coalesce(v_pen_aggr_curr1st_demand, 0), coalesce(v_pen_aggr_curr2nd_demand, 0), coalesce(v_pen_aggr_curr1st_coll, 0), coalesce(v_pen_aggr_curr2nd_coll, 0), v_gisrefno, v_isexempted, v_usage, v_source, coalesce(v_alv,0), v_longitude, v_latitude, coalesce(v_annualdemand, 0), coalesce(v_annualcoll, 0), v_isactive, v_regdocno, v_regdocdate, v_assessmentdate, v_capitalvalue, v_pattanumber, v_marketvalue, v_lastupdated, v_surveyno, v_courtcase, v_period, v_advance, v_oldmunicipalno);

		--updating floor details
		delete from egpt_mv_current_floor_detail where basicpropertyid = v_basicpropertyid;
		insert into egpt_mv_current_floor_detail (basicpropertyid, propertyid, natureofusage, propertytype, floorno, builtuparea, plintharea, classification, occupation, floorid) SELECT v_basicpropertyid, v_idproperty, usage.usg_name, v_proptymaster, floordet.floor_no, floordet.builtup_area, v_sital_area, strut.constr_type, floordet.id_occpn_mstr, floordet.id FROM egpt_property_usage_master usage, egpt_floor_detail floordet, egpt_struc_cl strut WHERE floordet.id_property_detail = v_idpropdetail AND floordet.id_struc_cl = strut.id AND floordet.id_usg_mstr = usage.id;

		--raise notice 'egptUpdateMatView : updated mv for v_upicno (%)', v_upicno;
		update egpt_mv_assessments set isUpdated=TRUE where assessmentno=v_assessmentno;
		EXCEPTION
		WHEN OTHERS THEN
		raise notice 'egptUpdateMatView : % %', SQLERRM, SQLSTATE;
		END;
	END LOOP;

END;
$$ LANGUAGE plpgsql;


