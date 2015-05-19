CREATE TABLE eg_remittance (
    id bigint NOT NULL,
    tdsid bigint NOT NULL,
    fundid bigint NOT NULL,
    fyid bigint NOT NULL,
    month bigint NOT NULL,
    paymentvhid bigint NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    asondate timestamp without time zone
);


CREATE TABLE eg_remittance_detail (
    id bigint NOT NULL,
    remittanceid bigint NOT NULL,
    remittancegldtlid bigint,
    lastmodifieddate timestamp without time zone,
    remittedamt double precision,
    generalledgerid bigint
);



CREATE TABLE eg_remittance_gldtl (
    id bigint NOT NULL,
    gldtlid bigint NOT NULL,
    gldtlamt double precision,
    lastmodifieddate timestamp without time zone,
    remittedamt double precision,
    tdsid bigint
);
CREATE TABLE generalledgerdetail (
    id bigint NOT NULL,
    generalledgerid bigint NOT NULL,
    detailkeyid bigint NOT NULL,
    detailtypeid bigint NOT NULL,
    amount double precision
);
CREATE TABLE egf_ebconsumer (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(96) NOT NULL,
    region character varying(64) NOT NULL,
    oddorevenbilling character varying(32) NOT NULL,
    wardid bigint,
    location character varying(64),
    address character varying(256),
    isactive bigint DEFAULT 0,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone
);
CREATE TABLE egf_ebdetails (
    id bigint NOT NULL,
    consumerno bigint NOT NULL,
    billno character varying(32),
    billamount double precision NOT NULL,
    month bigint NOT NULL,
    prevbillamount double precision,
    comments character varying(1024),
    billid bigint,
    billdate timestamp without time zone,
    status bigint NOT NULL,
    receiptno character varying(32),
    recieptdate timestamp without time zone,
    duedate timestamp without time zone,
    stateid bigint,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    wardid bigint,
    target_area_id bigint,
    position_id bigint,
    region character varying(64),
    financialyearid bigint,
    variance bigint
);
CREATE TABLE egf_target_area (
    id bigint NOT NULL,
    name character varying(32) NOT NULL,
    code character varying(32) NOT NULL,
    isactive bigint DEFAULT 0,
    positionid bigint,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone
);


CREATE TABLE egf_wardtargetarea_mapping (
    id bigint NOT NULL,
    boundaryid bigint,
    targetareaid bigint NOT NULL
);

CREATE TABLE egf_ebschedulerlog (
    id bigint NOT NULL,
    starttime timestamp without time zone,
    endtime timestamp without time zone,
    noofpendingbills bigint,
    noofbillsprocessed bigint,
    noofbillscreated bigint,
    schedulerstatus character varying(16),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    oddorevenbilling character varying(16),
    noofbillsfailed bigint
);

CREATE TABLE egf_ebschedulerlogdetails (
    id bigint NOT NULL,
    schedulerlogid bigint,
    consumer bigint,
    consumerno character varying(16),
    duedate character varying(16),
    amount character varying(16),
    message character varying(128),
    status character varying(16)
);

CREATE TABLE egf_cbill (
    id bigint
);
CREATE TABLE egf_fundingagency (
    id bigint NOT NULL,
    code character varying(16) NOT NULL,
    address character varying(256),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    remarks character varying(200),
    name character varying(100),
    isactive smallint
);

CREATE TABLE egf_loangrantdetail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    agencyid bigint NOT NULL,
    loanamount double precision,
    grantamount double precision,
    percentage real,
    agencyschemeno character varying(48),
    councilresno character varying(48),
    loansanctionno character varying(48),
    agreementdate timestamp without time zone,
    commorderno character varying(48),
    docid bigint,
    type character varying(16) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

CREATE TABLE egf_loangrantheader (
    id bigint NOT NULL,
    subschemeid bigint,
    councilresno character varying(48),
    govtorderno character varying(48),
    amendmentno character varying(48),
    projectcost double precision,
    sanctionedcost double precision,
    revisedcost double precision,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    amendmentdate timestamp without time zone,
    councilresdate timestamp without time zone,
    govtorderdate timestamp without time zone
);

CREATE TABLE egf_loangrantreceiptdetail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    agencyid bigint,
    amount double precision,
    description character varying(1024),
    voucherheaderid bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    instrumentheaderid bigint,
    bankaccountid bigint
);
CREATE TABLE egf_scheme_bankaccount (
    id bigint NOT NULL,
    schemeid bigint,
    subschemeid bigint,
    bankaccountid bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);


CREATE TABLE egf_subscheme_project (
    id bigint NOT NULL,
    subschemeid bigint NOT NULL,
    projectcodeid bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

CREATE TABLE egf_fixeddeposit (
    id bigint NOT NULL,
    fileno character varying(48),
    amount double precision,
    depositdate timestamp without time zone NOT NULL,
    bankbranchid bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    interestrate double precision NOT NULL,
    period character varying(64),
    serialnumber character varying(32) NOT NULL,
    outflowgjvid bigint,
    gjvamount double precision,
    maturityamount double precision,
    maturitydate timestamp without time zone,
    withdrawaldate timestamp without time zone,
    inflowgjvid bigint,
    challanreceiptvhid bigint,
    instrumentheaderid bigint,
    receiptamount double precision,
    remarks character varying(512),
    parentid bigint,
    extend smallint DEFAULT 0,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

CREATE TABLE egf_grant (
    id bigint NOT NULL,
    deptid bigint,
    financialyearid bigint NOT NULL,
    period character varying(10) NOT NULL,
    proceedingsno character varying(48) NOT NULL,
    proceedingsdate timestamp without time zone NOT NULL,
    accrualvoucherid bigint NOT NULL,
    accrualamount double precision,
    grantvoucherid bigint,
    receiptvoucherid bigint,
    grantamount double precision,
    instrumentheaderid bigint,
    remarks character varying(512),
    granttype character varying(48),
    commtaxofficer character varying(48),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);
CREATE TABLE eg_advancereqpayeedetails (
    id bigint NOT NULL,
    advancerequisitiondetailid bigint NOT NULL,
    accountdetailtypeid bigint NOT NULL,
    accountdetailkeyid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    tdsid bigint,
    narration character varying(250)
);
CREATE TABLE eg_advancerequisition (
    id bigint NOT NULL,
    advancerequisitionnumber character varying(100) NOT NULL,
    advancerequisitiondate timestamp without time zone NOT NULL,
    advancerequisitionamount double precision NOT NULL,
    narration character varying(512),
    arftype character varying(50) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone NOT NULL,
    statusid bigint NOT NULL,
    state_id bigint
);

CREATE TABLE eg_advancerequisitiondetails (
    id bigint NOT NULL,
    advancerequisitionid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    creditamount double precision,
    debitamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    narration character varying(256),
    functionid bigint
);
CREATE TABLE eg_advancerequisitionmis (
    id bigint NOT NULL,
    advancerequisitionid bigint NOT NULL,
    fundid bigint,
    fieldid bigint,
    subfieldid bigint,
    functionaryid bigint,
    lastupdatedtime timestamp without time zone NOT NULL,
    departmentid bigint,
    fundsourceid bigint,
    payto character varying(250),
    paybydate timestamp without time zone,
    schemeid bigint,
    subschemeid bigint,
    voucherheaderid bigint,
    sourcepath character varying(256),
    partybillnumber character varying(50),
    partybilldate timestamp without time zone,
    referencenumber character varying(50),
    id_function bigint
);

CREATE TABLE eg_billdetails (
    id bigint NOT NULL,
    billid bigint NOT NULL,
    functionid bigint,
    glcodeid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    narration character varying(250)
);
CREATE TABLE eg_billpayeedetails (
    id bigint NOT NULL,
    billdetailid bigint NOT NULL,
    accountdetailtypeid bigint NOT NULL,
    accountdetailkeyid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    tdsid bigint,
    narration character varying(250),
    pc_department bigint
);
CREATE TABLE eg_billregister (
    id bigint NOT NULL,
    billnumber character varying(50) NOT NULL,
    billdate timestamp without time zone NOT NULL,
    billamount double precision NOT NULL,
    fieldid bigint,
    worksdetailid bigint,
    billstatus character varying(50) NOT NULL,
    narration character varying(1024),
    passedamount double precision,
    billtype character varying(50),
    expendituretype character varying(20) NOT NULL,
    advanceadjusted double precision,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    statusid bigint,
    workorderdate timestamp without time zone,
    zone character varying(20),
    division character varying(50),
    workordernumber character varying(50),
    billpasseddate timestamp without time zone,
    isactive smallint,
    billapprovalstatus character varying(50),
    po character varying(50),
    state_id bigint
);

CREATE TABLE eg_billregistermis (
    id bigint,
    billid bigint NOT NULL,
    fundid bigint,
    segmentid bigint,
    subsegmentid bigint,
    fieldid bigint,
    subfieldid bigint,
    functionaryid bigint,
    sanctionedby character varying(30),
    sanctiondate timestamp without time zone,
    sanctiondetail character varying(200),
    narration character varying(300),
    lastupdatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    disbursementtype character varying(30),
    escalation bigint,
    advancepayments bigint,
    securedadvances bigint,
    deductamountwitheld bigint,
    departmentid bigint,
    month bigint,
    financialyearid bigint,
    fundsourceid bigint,
    rebate real,
    billtype character varying(50),
    payto character varying(250),
    paybydate timestamp without time zone,
    mbrefno character varying(200),
    schemeid bigint,
    subschemeid bigint,
    voucherheaderid bigint,
    sourcepath character varying(150),
    partybillnumber character varying(50),
    partybilldate timestamp without time zone,
    inwardserialnumber character varying(50),
    billsubtype bigint,
    budgetary_appnumber character varying(30),
    budgetcheckreq smallint,
    functionid bigint
);


CREATE TABLE eg_bill_subtype (
    id bigint NOT NULL,
    name character varying(120) NOT NULL,
    expenditure_type character varying(50) NOT NULL
);
CREATE TABLE miscbilldetail (
    id bigint NOT NULL,
    billnumber character varying(50),
    billdate timestamp without time zone,
    amount double precision NOT NULL,
    passedamount double precision NOT NULL,
    paidto character varying(250) NOT NULL,
    paidbyid bigint,
    billvhid bigint,
    payvhid bigint,
    paidamount double precision
);
CREATE TABLE egf_budget (
    id bigint NOT NULL,
    name character varying(50),
    description character varying(250),
    financialyearid bigint NOT NULL,
    state_id bigint,
    parent bigint,
    isactivebudget bigint,
    updatedtimestamp timestamp without time zone NOT NULL,
    isprimarybudget bigint NOT NULL,
    createdby bigint,
    lastmodifiedby bigint,
    isbere character varying(20),
    as_on_date timestamp without time zone,
    materializedpath character varying(10),
    reference_budget bigint,
    document_number bigint
);
CREATE TABLE egf_budgetdetail (
    id bigint NOT NULL,
    using_department bigint,
    executing_department bigint,
    function bigint,
    budget bigint NOT NULL,
    budgetgroup bigint NOT NULL,
    originalamount double precision,
    approvedamount double precision,
    anticipatory_amount double precision,
    budgetavailable double precision,
    scheme bigint,
    subscheme bigint,
    functionary bigint,
    boundary bigint,
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint,
    state_id bigint,
    fund bigint,
    materializedpath character varying(10),
    document_number bigint,
    uniqueno character varying(32),
    planningpercent real
);

CREATE TABLE egf_budgetgroup (
    id bigint NOT NULL,
    majorcode bigint,
    maxcode bigint,
    mincode bigint,
    name character varying(250),
    description character varying(250),
    budgetingtype character varying(250),
    accounttype character varying(250),
    isactive smallint,
    updatedtimestamp timestamp without time zone NOT NULL
);

CREATE TABLE egf_budget_reappropriation (
    id bigint NOT NULL,
    budgetdetail bigint NOT NULL,
    anticipatory_amount bigint,
    addition_amount bigint,
    deduction_amount bigint,
    state_id bigint,
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint,
    reappropriation_misc bigint,
    original_addition_amount bigint,
    original_deduction_amount bigint,
    status bigint
);
CREATE TABLE egf_reappropriation_misc (
    id bigint NOT NULL,
    sequence_number character varying(1024),
    reappropriation_date timestamp without time zone,
    remarks character varying(1024),
    state_id bigint,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone
);
CREATE TABLE egf_budget_usage (
    id bigint NOT NULL,
    financialyearid bigint NOT NULL,
    moduleid bigint,
    referencenumber character varying(50) NOT NULL,
    createdby bigint,
    consumedamt double precision,
    releasedamt double precision,
    updatedtime timestamp without time zone NOT NULL,
    budgetdetailid bigint NOT NULL,
    appropriationnumber character varying(32)
);

CREATE TABLE eg_dept_functionmap (
    id bigint NOT NULL,
    departmentid bigint,
    functionid bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE TABLE egf_account_cheques (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    fromchequenumber character varying(50) NOT NULL,
    tochequenumber character varying(50) NOT NULL,
    receiveddate timestamp without time zone NOT NULL,
    isexhausted bigint,
    nextchqno character varying(50),
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    serialno character varying(16)
);

CREATE TABLE contrajournalvoucher (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    frombankaccountid bigint,
    tobankaccountid bigint,
    instrumentheaderid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE TABLE transactionsummary (
    id bigint NOT NULL,
    glcodeid bigint NOT NULL,
    openingdebitbalance double precision NOT NULL,
    openingcreditbalance double precision NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    accountdetailtypeid bigint,
    accountdetailkey bigint,
    financialyearid bigint NOT NULL,
    fundid bigint,
    fundsourceid bigint,
    narration character varying(300),
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    departmentid bigint,
    functionaryid bigint,
    functionid smallint,
    divisionid bigint
);

CREATE TABLE eg_dept_do_mapping (
    id bigint NOT NULL,
    department_id bigint NOT NULL,
    drawingofficer_id bigint NOT NULL
);
CREATE TABLE egf_dishonorcheque (
    id bigint NOT NULL,
    instrumentheaderid bigint NOT NULL,
    originalvhid bigint NOT NULL,
    statusid bigint NOT NULL,
    bankcharges bigint,
    transactiondate timestamp without time zone NOT NULL,
    bankchargeglcodeid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    bankreferencenumber character varying(20),
    reversalvhid bigint,
    bankchargesvhid bigint,
    stateid bigint NOT NULL,
    bankreason character varying(50),
    instrumentdishonorreason character varying(50)
);
CREATE TABLE egf_dishonorcheque_detail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    debitamt bigint,
    creditamt bigint,
    detailkey bigint,
    detailtype bigint,
    functionid bigint
);
CREATE TABLE egf_instrumentaccountcodes (
    id bigint NOT NULL,
    typeid bigint,
    glcodeid bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);
CREATE TABLE egf_instrumentheader (
    id bigint NOT NULL,
    instrumentnumber character varying(20),
    instrumentdate timestamp without time zone,
    instrumentamount double precision NOT NULL,
    id_status bigint NOT NULL,
    bankaccountid bigint,
    payto character varying(250),
    ispaycheque character(1),
    instrumenttype character varying(20),
    bankid bigint,
    detailkeyid bigint,
    detailtypeid bigint,
    transactionnumber character varying(50),
    transactiondate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    payee character varying(50),
    bankbranchname character varying(50),
    surrendarreason character varying(100),
    serialno character varying(16),
    ecstype bigint
);
CREATE TABLE egf_instrumentotherdetails (
    id bigint,
    instrumentheaderid bigint,
    payinslipid bigint,
    instrumentstatusdate timestamp without time zone,
    reconciledamount double precision,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    reconciledon timestamp without time zone,
    dishonorbankrefno character varying(20)
);

CREATE TABLE egf_instrumenttype (
    id bigint NOT NULL,
    type character varying(50),
    isactive character varying(1),
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);

CREATE TABLE egf_instrumentvoucher (
    id bigint,
    instrumentheaderid bigint,
    voucherheaderid bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);

CREATE TABLE paymentheader (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    type character varying(50) NOT NULL,
    bankaccountnumberid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint,
    paymentamount double precision,
    concurrencedate timestamp without time zone,
    drawingofficer_id bigint
);


CREATE TABLE egf_receipt_voucher (
    id bigint NOT NULL,
    voucherheaderid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE TABLE eg_deduction_details (
    id bigint NOT NULL,
    tdsid bigint NOT NULL,
    partytypeid bigint,
    doctypeid bigint,
    docsubtypeid bigint,
    datefrom timestamp without time zone,
    dateto timestamp without time zone,
    lowlimit double precision NOT NULL,
    highlimit double precision,
    incometax real,
    surcharge real,
    education real,
    lastmodifieddate timestamp without time zone,
    amount double precision,
    cumulativehighlimit double precision,
    cumulativelowlimit double precision
);

CREATE TABLE tds (
    id bigint NOT NULL,
    type character varying(20),
    ispaid smallint,
    glcodeid bigint,
    isactive smallint,
    lastmodified timestamp without time zone,
    created timestamp without time zone,
    modifiedby bigint,
    rate double precision,
    effectivefrom timestamp without time zone,
    createdby bigint NOT NULL,
    remitted character varying(100),
    bsrcode character varying(20),
    description character varying(200),
    partytypeid bigint,
    bankid bigint,
    caplimit double precision,
    isearning character varying(1),
    recoveryname character varying(50),
    calculationtype character varying(50),
    section character varying(50),
    recovery_mode character(1) DEFAULT 'M'::bpchar NOT NULL,
    remittance_mode character(1) DEFAULT 'M'::bpchar,
    ifsccode character varying(16),
    accountnumber character varying(32),
    CONSTRAINT tds_ma CHECK ((recovery_mode = ANY (ARRAY['M'::bpchar, 'A'::bpchar])))
);

CREATE TABLE egf_recovery_bankdetails (
    id bigint NOT NULL,
    tds_id bigint NOT NULL,
    fund_id bigint NOT NULL,
    bank_id bigint NOT NULL,
    branch_id bigint NOT NULL,
    bankaccount_id bigint NOT NULL
);
CREATE TABLE egf_remittance_schd_payment (
    id bigint NOT NULL,
    schd_id bigint NOT NULL,
    vhid bigint NOT NULL
);
CREATE TABLE egf_remittance_scheduler (
    id bigint NOT NULL,
    sch_job_name character varying(200) NOT NULL,
    lastrundate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    sch_type character(1) DEFAULT 'S'::bpchar NOT NULL,
    remarks character varying(4000),
    status character varying(50) NOT NULL,
    glcode character varying(16)
);
CREATE TABLE egf_fundflow (
    id bigint NOT NULL,
    reportdate timestamp without time zone NOT NULL,
    bankaccountid bigint,
    openingbalance double precision,
    currentreceipt double precision,
    createdby bigint,
    lastmodifiedby bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone
);

CREATE TABLE generalledger (
    id bigint NOT NULL,
    voucherlineid bigint NOT NULL,
    effectivedate timestamp without time zone NOT NULL,
    glcodeid bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    description character varying(250),
    voucherheaderid bigint,
    functionid bigint,
    remittancedate timestamp without time zone
);
CREATE TABLE egf_ecstype (
    id bigint NOT NULL,
    type character varying(30) NOT NULL,
    isactive bigint NOT NULL
);
CREATE TABLE egw_typeofwork (
    id bigint NOT NULL,
    code character varying(20) NOT NULL,
    parentid bigint,
    description character varying(1000) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    partytypeid bigint
);
