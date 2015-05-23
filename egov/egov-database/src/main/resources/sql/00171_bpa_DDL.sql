
CREATE TABLE egbpaextnd_address (
    id bigint NOT NULL,
    addresstypeid bigint NOT NULL,
    registrationid bigint NOT NULL,
    plotdoornumber character varying(32),
    plotlandmark character varying(128),
    plotnumber character varying(32),
    plotsurveynumber character varying(256),
    plotblocknumber character varying(512),
    streetaddress1 character varying(512),
    streetaddress2 character varying(512),
    citytown character varying(512),
    villageid bigint,
    blocknumber character varying(512),
    stateid character varying(32),
    pincode bigint
);


-- Need more fields in this table.
CREATE TABLE egbpaextnd_apprd_bldgdetails (
    id bigint NOT NULL,
    registrationid bigint,
    unit_classification character varying(64),
    unit_count bigint,
    floor_count bigint,
    isbasementunit character(1) DEFAULT 0,
    building_height bigint,
    total_floorarea bigint
);

CREATE TABLE egbpaextnd_apprd_bldgfloordtls (
    id bigint NOT NULL,
    approvedbldgid bigint,
    exist_bldg_area bigint,
    proposed_bldg_area bigint,
    exist_bldg_usage bigint,
    proposed_bldg_usage bigint,
    floor_num bigint
);

CREATE TABLE egbpaextnd_autodcr (
    id bigint NOT NULL,
    autodcr_num character varying(64) NOT NULL,
    applicant_name character varying(64),
    address character varying(256),
    email_id character varying(64),
    mobile_no bigint,
    zone character varying(64),
    ward character varying(64),
    door_no character varying(512),
    plotnumber character varying(32),
    survey_no character varying(256),
    village character varying(512),
    blocknumber character varying(512),
    plotarea bigint,
    floor_count bigint
);

CREATE TABLE egbpaextnd_autodcr_floordetail (
    id bigint NOT NULL,
    autodcr_id bigint NOT NULL,
    existing_bldg_area bigint,
    existing_bldg_usage bigint,
    proposed_bldg_area bigint,
    proposed_bldg_usage bigint,
    floor_num bigint
);

CREATE TABLE egbpaextnd_ddfee_details (
    id bigint NOT NULL,
    ddamount bigint,
    ddno character varying(64),
    dddate timestamp without time zone,
    ddtype character varying(126),
    registrationid bigint,
    ddbank bigint
);


CREATE TABLE egbpaextnd_docket (
    id bigint NOT NULL,
    statusofapplicant character varying(20),
    existingusage character varying(52),
    proposedactivitypermissible character varying(52),
    old_proptax_paidrecpt_enclosd character varying(5),
    abuttingroad_width bigint,
    abuttingroad_publicorprivate character varying(7),
    abuttingroad_takenupforimpmnt character varying(5),
    abuttingroad_gainaccessthrpsg character varying(5),
    abuttingroad_gainwidth bigint,
    abuttingroad_gainpuborprivate character varying(7),
    plancomplieswithsidecondition character varying(5),
    remarks character varying(512),
    aeeinspectionreport character varying(512),
    createdby bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    totalfloor_count bigint,
    length_of_compoundwall bigint,
    diameter_of_well bigint,
    seperatelat_tank bigint,
    terraced bigint,
    tiled_roof bigint,
    plotwidth_rear bigint DEFAULT 0,
    constructionwidth_rear bigint DEFAULT 0,
    constructionheight_rear bigint DEFAULT 0
);

CREATE TABLE egbpaextnd_docket_constrnstage (
    id bigint NOT NULL,
    checklistdetailid bigint NOT NULL,
    docketid bigint NOT NULL,
    value character varying(5),
    remarks character varying(256)
);

CREATE TABLE egbpaextnd_docket_documentdtl (
    id bigint NOT NULL,
    checklistdetailid bigint NOT NULL,
    docketid bigint NOT NULL,
    value character varying(256),
    remarks character varying(256)
);

CREATE TABLE egbpaextnd_docket_floordetails (
    id bigint NOT NULL,
    docketid bigint NOT NULL,
    floor_num bigint,
    floor_value bigint
);

CREATE TABLE egbpaextnd_docket_violationdtl (
    id bigint NOT NULL,
    checklistdetailid bigint NOT NULL,
    docketid bigint NOT NULL,
    value character varying(7),
    required character varying(20),
    provided character varying(20),
    extentofviolation character varying(20),
    percentageofviolation character varying(20),
    remarks character varying(256)
);


 CREATE TABLE EGBPAEXTND_DOCUMENTHISTORY 
   (	ID bigint NOT NULL , 
	REGISTRATIONID bigint NOT NULL , 
	CREATED_USERID bigint NOT NULL , 
	DOC_ENCLOSED_bigint character varying(256), 
	DOC_ENCLOSED_DATE timestamp without time zone, 
	DOC_ENCLOSED_EXTENT_AREASQMT bigint, 
	DOC_LAYOUT_EXTENT_AREASQMT bigint, 
	WHEATHER_PLOT_DEVELOPEDBY character varying(256), 
	WHEATHER_DOCUMENT_ENCLOSEDBY character(1), 
	WHEATHER_PART_OF_LAYOUT character(1), 
	WHEATHER_FMSSKETCH_COPYOFREG character(1), 
	MODIFIEDDATE timestamp without time zone NOT NULL , 
	PLOT_DEVELOPEDBY character(1));

CREATE TABLE egbpaextnd_insp_checklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1) DEFAULT 0,
    inspectiondtlsid bigint
);

CREATE TABLE egbpaextnd_inspect_measuredtls (
    id bigint NOT NULL,
    inspectiondtlsid bigint,
    inspectionsourceid bigint,
    fsb bigint,
    rsb bigint,
    ssb1 bigint,
    ssb2 bigint,
    pass_width bigint,
    passage_length bigint,
    surroundedbynorth bigint,
    surroundedbysouth bigint,
    surroundedbyeast bigint,
    surroundedbywest bigint
);

CREATE TABLE egbpaextnd_inspection (
    id bigint NOT NULL,
    inspection_num character varying(64),
    inspection_date timestamp without time zone NOT NULL,
    inspectedby bigint NOT NULL,
    registrationid bigint,
    parent bigint,
    isinspected character(1) DEFAULT 0,
    ispostponeddate character(1) DEFAULT 0,
    postponementreason character varying(256),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    inspection_remarks character varying(256),
    land_zoning character varying(400),
    lnd_layout_type bigint,
    lnd_min_plotextent bigint,
    lnd_proposed_plotextent bigint,
    lnd_osr_landextent bigint,
    lnd_guidelinevalue bigint,
    building_zoning character varying(400),
    bldg_builduparea bigint,
    bldg_proposed_plotfrontage bigint,
    bldg_roadwidth bigint,
    bldg_proposed_buildingarea bigint,
    bldg_gfloor_othertypes bigint,
    bldg_firstfloor_totalarea bigint,
    bldg_compoundwall bigint,
    bldg_welloht_sumptankarea bigint,
    lnd_usage bigint,
    building_type bigint,
    bldg_commercial bigint,
    bldg_residential bigint,
    bldg_gfloor_tiledfloor bigint,
    bldg_stormwaterdrain bigint,
    lnd_regularizationarea bigint,
    lnd_penaltyperiod_halfyears bigint,
    bldg_isimprovementcharges character(1) DEFAULT 1,
    bldg_isregularisationcharges character(1) DEFAULT 1,
    land_isregularisationcharges character(1) DEFAULT 1,
    docketid bigint,
    dwellingunit bigint
);

CREATE TABLE egbpaextnd_inspection_details (
    id bigint NOT NULL,
    conststagesid bigint,
    building_extent character varying(256),
    num_of_plots bigint,
    remarks character varying(1064)
);

CREATE TABLE egbpaextnd_land_buildingtypes (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    description character varying(126),
    type character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);

CREATE TABLE egbpaextnd_lettertoparty (
    id bigint NOT NULL,
    lp_num character varying(128),
    acknowledgementnumber character varying(128),
    inspectionid bigint,
    registrationid bigint,
    letterdate timestamp without time zone NOT NULL,
    lp_reasonid bigint NOT NULL,
    sent_date timestamp without time zone,
    reply_date timestamp without time zone,
    lpremarks character varying(1024),
    lpreplyremarks character varying(1024),
    lpdesc character varying(1024),
    lpreplydesc character varying(1024),
    is_history character(1),
    documentid character varying(256),
    createdby bigint
);

CREATE TABLE egbpaextnd_lpchecklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1),
    lp_checklist_type character varying(64),
    lpid bigint,
    remarks character varying(300)
);

CREATE TABLE egbpaextnd_mstr_bldgcategory (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);


CREATE TABLE egbpaextnd_mstr_bpafee (
    id bigint NOT NULL,
    servicetypeid bigint NOT NULL,
    fundid bigint NOT NULL,
    functionid bigint NOT NULL,
    fee_type character varying(64) NOT NULL,
    fee_code character varying(3) NOT NULL,
    fee_description character varying(64) NOT NULL,
    glcodeid bigint NOT NULL,
    isfixedamount character(1) DEFAULT 0,
    isactive character(1) DEFAULT 1 NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    ismandatory smallint DEFAULT 0 NOT NULL,
    fee_description_local character varying(256),
    order_num bigint,
    isplanningpermitfee character(1) DEFAULT 1 NOT NULL,
    feegroup character varying(128)
);
CREATE TABLE egbpaextnd_mstr_bpafeedetail (
    id bigint NOT NULL,
    feeid bigint NOT NULL,
    from_areasqmt bigint,
    to_areasqmt bigint,
    amount bigint NOT NULL,
    subtype character varying(25),
    landusezone character varying(25),
    floornumber bigint,
    usagetypeid bigint,
    startdate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    enddate timestamp without time zone,
    additionaltype character varying(25)
);

CREATE TABLE egbpaextnd_mstr_buildingusage (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);
CREATE TABLE egbpaextnd_mstr_changeofusage (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);

CREATE TABLE egbpaextnd_mstr_checklist (
    id bigint NOT NULL,
    checklisttype character varying(128) NOT NULL,
    servicetypeid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);

CREATE TABLE egbpaextnd_mstr_chklistdetail (
    id bigint NOT NULL,
    checklistid bigint NOT NULL,
    code character varying(10) NOT NULL,
    description character varying(256) NOT NULL,
    ismandatory character(1) DEFAULT 0,
    isactive character(1) DEFAULT 1 NOT NULL
);

CREATE TABLE egbpaextnd_mstr_const_stages (
    id bigint NOT NULL,
    const_stage character varying(256) NOT NULL,
    description character varying(64),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);

CREATE TABLE egbpaextnd_mstr_inspsource (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);

CREATE TABLE egbpaextnd_mstr_layout (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    description character varying(126),
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);


CREATE TABLE egbpaextnd_mstr_lpreason (
    id bigint NOT NULL,
    reason character varying(1024) NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);

CREATE TABLE egbpaextnd_mstr_roadwidth (
    id bigint NOT NULL,
    range character varying(126) NOT NULL,
    rate bigint DEFAULT 0,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);

CREATE TABLE egbpaextnd_mstr_servicetype (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    iscmdatype character(1) DEFAULT 0,
    isinspectionfeerequired character(1) DEFAULT 0 NOT NULL,
    isscrutinyfeerequired character(1) DEFAULT 0 NOT NULL,
    isptisnumberrequired character(1) DEFAULT 0 NOT NULL,
    isautodcrnumberrequired character(1) DEFAULT 0 NOT NULL,
    servicenumberprefix character varying(10) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL,
    description_local character varying(256),
    isdocuploadrequiredforcitizen character(1) DEFAULT 0
);

CREATE TABLE egbpaextnd_mstr_surnbldgdtls (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);

CREATE TABLE egbpaextnd_mstr_surveyorname (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    regnnum character varying(64),
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);

CREATE TABLE egbpaextnd_mstr_village (
    id bigint NOT NULL,
    villagename character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);

CREATE TABLE egbpaextnd_registration (
    id bigint NOT NULL,
    ba_num character varying(128),
    ba_order_date timestamp without time zone,
    psn_num character varying(128) NOT NULL,
    psn_date timestamp without time zone NOT NULL,
    cmda_num character varying(128),
    cmda_ref_date timestamp without time zone,
    app_type character varying(128) NOT NULL,
    propertyid character varying(128),
    parent bigint,
    servicetypeid bigint NOT NULL,
    demandid bigint,
    stateid bigint,
    statusid bigint NOT NULL,
    ownerid bigint NOT NULL,
    surveyorid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    adminboundaryid bigint NOT NULL,
    locboundaryid bigint,
    documentid character varying(256),
    rejectionid bigint,
    approverid bigint,
    ppa_num character varying(256),
    issanctionfeeraised bigint DEFAULT 0,
    feeremarks character varying(1024),
    externalfeecollecteddate timestamp without time zone,
    securitykey character varying(64),
    exist_ppanum character varying(128),
    exist_banum character varying(128),
    app_mode character varying(128) DEFAULT 'General'::character varying,
    request_number character varying(50),
    servicereqregistryid bigint,
    serviceregistryid bigint
);

CREATE TABLE egbpaextnd_registrationfee (
    id bigint NOT NULL,
    feedate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    feeremarks character varying(1024),
    stateid bigint,
    statusid bigint NOT NULL,
    isrevised bigint DEFAULT 0,
    registrationid bigint NOT NULL,
    challannumber character varying(128),
    createdby bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);

CREATE TABLE egbpaextnd_regn_approvalinfo (
    id bigint NOT NULL,
    approval_type bigint NOT NULL,
    comm_approved_date timestamp without time zone NOT NULL,
    usage_from bigint NOT NULL,
    registrationid bigint NOT NULL,
    usage_to bigint NOT NULL,
    isforward_to_cmda character(1) NOT NULL,
    date_of_forward timestamp without time zone,
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);

CREATE TABLE egbpaextnd_regn_autodcr (
    id bigint NOT NULL,
    lpid bigint,
    registrationid bigint NOT NULL,
    autodcr_num character varying(64) NOT NULL,
    isactive character(1) DEFAULT 0
);

CREATE TABLE egbpaextnd_regn_checklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1) DEFAULT 0,
    registrationid bigint,
    checklistremarks character varying(256),
    DOCUMENTUPLOADID bigint
);

CREATE TABLE egbpaextnd_regn_details (
    id bigint NOT NULL,
    registrationid bigint NOT NULL,
    exist_bldgcatgid bigint,
    proposed_bldgcatgid bigint,
    sital_areasqmt bigint,
    sital_areasqft bigint,
    remarks character varying(256)
);

CREATE TABLE egbpaextnd_regnfeedetail (
    id bigint NOT NULL,
    registrationfeeid bigint NOT NULL,
    bpafeeid bigint NOT NULL,
    amount bigint DEFAULT 0
);

CREATE TABLE egbpaextnd_regnstatus_details (
    id bigint NOT NULL,
    registrationid bigint NOT NULL,
    statusdate timestamp without time zone,
    remarks character varying(1064),
    statusid bigint NOT NULL
);

CREATE TABLE egbpaextnd_rejection (
    id bigint NOT NULL,
    rejectiondate timestamp without time zone NOT NULL,
    remarks character varying(256),
    rejectionnumber character varying(32)
);

CREATE TABLE egbpaextnd_rejection_checklist (
    id bigint NOT NULL,
    rejectionid bigint,
    checklistdetailid bigint,
    ischecked character(1),
    remarks character varying(300)
);
CREATE TABLE egbpaextnd_stormwaterdrain (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    dimension character varying(126) NOT NULL,
    description character varying(126),
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);



CREATE TABLE EGBPAEXTND_AUTODCR_DETAILS 
   (	ID bigint NOT NULL , 
	AUTODCR_NUM character varying(64 ) NOT NULL , 
	FILENUMBER character varying(64 ), 
	FILE_CASETYPE character varying(64 ), 
	FILE_BUILDINGCATEGORY character varying(64 ), 
	FILE_LANDUSEZONE character varying(64 ), 
	FILE_PROPOSLATYPE character varying(64 ), 
	FILE_INWARDDATE character varying(64 ), 
	FILE_ZONE character varying(64 ), 
	FILE_DIVISION character varying(64 ), 
	FILE_PLOTNUMBER character varying(64 ), 
	FILE_ROADNAME character varying(64 ), 
	FILE_DOORNUMBER character varying(64 ), 
	FILE_SURVEYNUMBER character varying(64 ), 
	FILE_REVENUEVILLAGE character varying(64 ), 
	FILE_BLOCKNUMBER character varying(64 ), 
	FILE_MOBILENUMBER character varying(64 ), 
	FILE_EMAILID character varying(64 ), 
	FILE_UNIQUEID character varying(64 ), 
	FILE_PATTAPLOTAREA character varying(64 ), 
	FILE_DOCUMENTPLOTAREA character varying(64 ), 
	FILE_SITEPLOTAREA character varying(64 ), 
	FILE_STATUS character varying(64 ), 
	PLOTUSE character varying(64 ), 
	PLOT_GROSSPLOTAREA character varying(64 ), 
	PLOT_TOTALBUILDUPAREA character varying(64 ), 
	PLOT_CONSUMEDFSI character varying(64 ), 
	PLOT_COVERAGEPERCENTAGE character varying(64 ), 
	PLOT_NETPLOTAREA character varying(64 ), 
	PLOTWIDTH character varying(64 ), 
	PLOT_ABUTTINGROAD character varying(64 ), 
	PLOTFRONTAGE character varying(64 ), 
	PLOT_COMPOUNDWALLAREA character varying(64 ), 
	PLOT_WELLOHTSUMPTANKAREA character varying(64 ), 
	BLDG_BUILDINGNAME character varying(64 ), 
	BLDG_BUILDINGHEIGHT character varying(64 ), 
	BLDG_MARGINFRONTSIDE character varying(64 ), 
	BLDG_MARGINREARSIDE character varying(64 ), 
	BLDG_MARGINSIDE1 character varying(64 ), 
	BLDG_MARGINSIDE2 character varying(64 ), 
	FILE_APPLICANTNAME character varying(64 ), 
	LOGICAL_PATH character varying(256 ));

 CREATE TABLE  EGBPAEXTND_AUTODCR_FLOORDTLS  
   (	 ID  bigint NOT NULL , 
	 AUTODCRDETAILSID   bigint NOT NULL , 
	 FLOORNAME   character varying(64 ), 
	 TOTALCARPETAREA   character varying(64 ), 
	 TOTALBUILDUPAREA   character varying(64 ), 
	 TOTALSLAB   character varying(64 )); 

CREATE TABLE EGBPAEXTND_AUTODCRDTLS_REGN 
   (	ID bigint NOT NULL , 
	AUTODCR_NUM character varying(64 ) NOT NULL , 
	LPID bigint, 
	REGISTRATIONID bigint NOT NULL , 
	ISACTIVE CHAR(1 ), 
	AUTODCRDETAILSID bigint NOT NULL );

  CREATE TABLE EGBPAEXTND_CMDA_LETTERTOPARTY 
   (	ID  bigint NOT NULL , 
	LP_NUM  character varying(128), 
	LP_REASON  character varying(1024), 
	LP_REMARKS  character varying(1024), 
	DOCUMENTID  character varying(256), 
	LPREPLY_DESCRIPTION  character varying(1024), 
	LPREPLY_REMARKS  character varying(1024),
	IS_HISTORY character(1),
	LPID  bigint NOT NULL , 
	REGISTRATIONID  bigint NOT NULL , 
	CREATEDDATE timestamp without time zone NOT NULL,
   	MODIFIEDDATE timestamp without time zone,
	CREATEDBY  bigint NOT NULL , 
	MODIFIEDBY  bigint, 
	ACKNOWLEDGEMENTNUMBER  character varying(256), 
	REPLY_DATE timestamp without time zone); 

 CREATE TABLE EGBPAEXTND_DOCHIST_DETAIL 
   (	ID bigint NOT NULL , 
	DOCHISTORYID bigint NOT NULL , 
	EXTENT_AREASQMT bigint, 
	SCHEDULEPROPERTY character varying(256), 
	VENDOR character varying(256), 
	PURCHASER character varying(256), 
	NATURE_OF_DEED character varying(256), 
	REGISTARTION_DATE timestamp without time zone, 
	REFERENCE_NUMBER character varying(256), 
	NORTH_BOUNDARY character varying(256), 
	SOUTH_BOUNDARY character varying(256), 
	EAST_BOUNDARY character varying(256), 
	WEST_BOUNDARY character varying(256), 
	DOC_REMARKS character varying(256)); 

 CREATE TABLE EGBPAEXTND_DOCUPLOAD 
   (	ID bigint NOT NULL , 
	REFERENCEID bigint NOT NULL , 
	OBJECTTYPE character varying(256) NOT NULL , 
	CREATEDBY bigint NOT NULL , 
	CREATEDDATE timestamp without time zone); 

  CREATE TABLE EGBPAEXTND_DOCUPLOAD_DETAILS 
   (	ID bigint NOT NULL , 
	DOCUPLOADID bigint NOT NULL , 
	COL_INDEX bigint, 
	CONTENTTYPE character varying(256), 
	FILENAME character varying(256), 
	IMAGE_DOCUMENT bytea);

  CREATE TABLE EGBPAEXTND_OFFICIALACTIONS 
   (	ID bigint NOT NULL , 
	VIEWED_SURVEYOR_INSPECTION character(1)  DEFAULT 0, 
	VIEWED_AE_AEE_INSPECTION character(1) DEFAULT 0, 
	VIEWED_SURVEYOR_DOCDETAILS character(1) DEFAULT 0, 
	VIEWED_AE_AEE_DOCDETAILS character(1) DEFAULT 0, 
	VIEWED_AUTODCRDETAILS character(1) DEFAULT 0, 
	VIEWED_DOCKETSHEET character(1) DEFAULT 0, 
	REGISTRATIONID bigint NOT NULL , 
	CREATEDDATE timestamp without time zone NOT NULL , 
	MODIFIEDDATE timestamp without time zone, 
	CREATEDBY bigint NOT NULL , 
	MODIFIEDBY bigint);
