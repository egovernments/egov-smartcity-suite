/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.constants;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.AUGUST;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.JULY;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.MAY;
import static java.util.Calendar.NOVEMBER;
import static java.util.Calendar.OCTOBER;
import static java.util.Calendar.SEPTEMBER;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface PropertyTaxConstants {
	// General constants used across Clients
	public static final String PTMODULENAME = "Property Tax";
	public static final String PTISCONFIGFILE = "ptis_egov_config.xml";
	public static final String DEACTIVATION = "DEACTIVATION";
	public static final String DATE_FORMAT_DDMMYYY = "dd/MM/yyyy";
	public static final String SESSIONLOGINID = "userid";
	public static final String SESSION_VAR_LOGIN_USER_NAME = "username";
	public static final String CITIZENUSER = "9999999999";
	public static final String PROP_ADDR_TYPE = "PROPERTY";
	public static final String OWNER_ADDR_TYPE = "OWNER";
	public static final String TRANSFER = "TRANSFER";
	
	//Workflow statuses
	public static final String PROPERTY_STATUS_WORKFLOW = "WORKFLOW";
	public static final String PROPERTY_STATUS_APPROVEL_PENDING = "APPROVAL PENDING";
	public static final String PROPERTY_STATUS_APPROVED = "APPROVED";

	// Demand Category Constants used across Clients
	public static final String REASON_CATEGORY_CODE_TAX = "TAX";
	public static final String REASON_CATEGORY_CODE_PENALTY = "PENALTY";
	public static final String REASON_CATEGORY_CODE_ADVANCE = "ADVANCE";
	public static final String REASON_CATEGORY_CODE_FINES = "FINES";

	// Demand Reason Constants used across Clients
	public static final String ADVANCE_DMD_RSN_CODE = "ADVANCE";
	public static final String PENALTY_DMD_RSN_CODE = "PENALTY";
	public static final String LPPAY_PENALTY_DMDRSNCODE = "PENALTY_FINES";

	// Others
	public static final String CURR_DMD_STR = "CURR_DMD";
	public static final String ARR_DMD_STR = "ARR_DMD";
	public static final String CURR_COLL_STR = "CURR_COLL";
	public static final String ARR_COLL_STR = "ARR_COLL";
	public static final String CURRENT_REBATE_STR = "CURRENT_REBATE";
	public static final String ARREAR_REBATE_STR = "ARREAR_REBATE";

	public static final String BUILT_UP_PROPERTY = "BuiltUpProperty";
	public static final String VACANT_PROPERTY = "VacantProperty";

	// objection status CODE values
	public static final String OBJECTION_MODULE = "PTObejction";
	public static final String OBJECTION_CREATED = "CREATED";
	public static final String OBJECTION_HEARING_FIXED = "HEARING DATE FIXED";
	public static final String OBJECTION_HEARING_COMPLETED = "HEARING COMPLETED";
	public static final String OBJECTION_INSPECTION_COMPLETED = "INSPECTION COMPLETED";
	public static final String OBJECTION_ACCEPTED = "OBJECTION ACCEPTED";
	public static final String OBJECTION_REJECTED = "OBJECTION REJECTED";

	public static final String OBJECTION_ADDHEARING_DATE = "ADD HEARING DATE";
	public static final String OBJECTION_RECORD_HEARINGDETAILS = "RECORD HEARING DETAILS";
	public static final String OBJECTION_RECORD_INSPECTIONDETAILS = "RECORD INSPECTION DETAILS";
	public static final String OBJECTION_RECORD_OBJECTIONOUTCOME = "RECORD OBJECTION OUTCOME";
	public static final String OBJECTION_RECORD_SAVED = "OBJECTION RECORD";
	public static final String OBJECTION_HEARINGDATE_SAVED = "HEARING DATE";

	public static final String RECOVERY_MODULE = "PTRecovery";
	public static final String RECOVERY_NOTICE155CREATED = "NOTICE155CREATED";
	public static final String RECOVERY_NOTICE155GENERATIONPENDING = "NOTICE155GENERATIONPENDING";
	public static final String RECOVERY_NOTICE155GENERATED = "NOTICE155GENERATED";
	public static final String RECOVERY_WARRANTPREPARED = "WARRANT APPLICATION PREPARED";
	public static final String RECOVERY_WARRANTAPPROVED = "WARRANT APPLICATION APPROVED";
	public static final String RECOVERY_WARRANTNOTICECREATED = "WARRANT NOTICE CREATED";
	public static final String RECOVERY_WARRANTNOTICEISSUED = "WARRANT NOTICE ISSUED";
	public static final String RECOVERY_CEASENOTICECREATED = "CEASE NOTICE CREATED";
	public static final String RECOVERY_CEASENOTICEISSUED = "CEASE NOTICE ISSUED";

	// GIS Search Property
	public static final String SRCH_BOUNDARY_ID = "BOUNDARYID";
	public static final String SRCH_OWNER_NAME = "OWNERNAME";
	public static final String SRCH_NEW_HOUSE_NO = "NEWHOUSENO";
	public static final String SRCH_OLD_HOUSE_NO = "OLDHOUSENO";
	public static final String SRCH_PROPERTY_TYPE = "PROPERTYTYPE";
	public static final String SRCH_DEMAND_FROM_AMOUNT = "DMD_FROM_AMOUNT";
	public static final String SRCH_DEMAND_TO_AMOUNT = "DMD_TO_AMOUNT";
	public static final String SRCH_DEFAULTER_FROM_AMOUNT = "DEFAULTER_FROM_AMOUNT";
	public static final String SRCH_DEFAULTER_TO_AMOUNT = "DEFAULTER_TO_AMOUNT";

	// Property status values
	public static final Character STATUS_ISHISTORY = 'H';
	public static final Character STATUS_ISACTIVE = 'A';
	public static final Character STATUS_WORKFLOW = 'W';
	public static final Character STATUS_OBJECTED = 'O';
	public static final Character STATUS_DEMAND_INACTIVE = 'I';
	public static final Character STATUS_CANCELLED = 'C';

	// Bill types
	public static final String BILLTYPE_MANUAL = "MANUAL";
	public static final String BILLTYPE_AUTO = "AUTO";

	public static final Character CANCELLED_RECEIPT_STATUS = 'C';

	// PTIS MODULEID IN EG_MODULES
	public static final String PTIS_EG_MODULES_ID = "2";

	// Demand Status when Cheque bounced/cheque amount paid
	public static final Character DMD_STATUS_CHEQUE_BOUNCED = 'B';
	public static final Character DMD_STATUS_CHQ_BOUNCE_AMOUNT_PAID = 'P';
	public static final Character DMD_STATUS_NO_CHQ_BOUNCED = 'N';

	// Named Queries
	public static final String QUERY_PROPERTYIMPL_BYID = "PROPERTYIMPL_BYID";
	public static final String GET_PROPERTY_TYPES = "getPropertyTypes";
	public static final String QUERY_STATUS_BY_MODULE_AND_CODE = "getStatusByModuleAndCode";
	public static final String QUERY_PROP_STATUS_BY_STATUSCODE = "getPropStatusByStatusCode";

	public static final String STRING_NOT_AVAILABLE = "N/A";

	public static final String PTIS_COLLECTION_SERVICE_CODE = "PT";

	public static final String STATUS_OBJECTED_STR = "OBJECTED";

	public static final String STR_YES = "Yes";

	public static final String MUTATION_FEE_STR = "MUTATION FEE";

	public static final String PROP_SOURCE = "MNCPL-RECORDS";

	// hierarchy types
	public static final String REVENUE_HIERARCHY_TYPE = "REVENUE";
	public static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
	public static final String LOCATION_HIERARCHY_TYPE = "LOCATION";
	// boundary Types
	public static final String CITY_BNDRY_TYPE = "Revenue City";
	public static final String ZONE_BNDRY_TYPE = "Revenue Zone";
	public static final String WARD_BNDRY_TYPE = "Revenue Ward";
	public static final String AREA_BNDRY_TYPE = "Revenue Area";
	public static final String ZONE = "Zone";
	public static final String WARD = "Ward";
	public static final String STREET = "Street";
	public static final String LOCALITY = "locality";

	// Property Types
	public static final String PROPTYPE_RESD = "RESIDENTIAL";
	public static final String PROPTYPE_NON_RESD = "NON_RESD";
	public static final String PROPTYPE_MIXED = "OP-RESD-NONRESD";
	public static final String PROPTYPE_OPEN_PLOT = "OPEN_PLOT";
	public static final String PROPTYPE_STATE_GOVT = "STATE_GOVT";
	public static final String PROPTYPE_CENTRAL_GOVT = "CENTRAL_GOVT";
	public static final String PROPERTY_TYPE_OPEN_PLOT = "OPEN_PLOT";
	public static final String RESIDENTIAL_FLATS = "RESIDENTIAL_FLATS";
	public static final String RESIDENTIAL_HOUSE = "RESIDENTIAL_HOUSE";

	public static final String PROPTYPE_RESD_STR = "Residential";
	public static final String PROPTYPE_NONRESD_STR = "Non-Residential";
	public static final String PROPTYPE_MIXED_STR = "Mixed";
	public static final String PROPTYPE_OPENPLOT_STR = "Open Plot";
	public static final String PROPTYPE_STATEGOVT_STR = "State Government";
	public static final String PROPTYPE_CENTGOVT_STR = "Central Government";

	// Property Type Categories
	public static final String PROPTYPE_CAT_RESD = "RESD";
	public static final String PROPTYPE_CAT_NON_RESD = "NON_RESD";
	public static final String PROPTYPE_CAT_RESD_CUM_NON_RESD = "RESD_CUM_NON_RESD";
	public static final String PROPTYPE_CAT_MOBILE_TOWER = "MOBILE_TOWER";
	public static final String NONRESD_FLAT = "FLAT";

	// Property Types for Usage
	public static final String USAGES_FOR_RESD = "C1, D1, D2, E1, E2, E3";
	public static final String USAGES_FOR_NON_RESD = "A1, A2, A3, A4, A5, A6, B1, B2, C2, C3, C4";
	public static final String USAGES_FOR_OPENPLOT = "C5, D3, E4";

	// Notice Types
	public static final String NOTICE127 = "Notice 127";
	public static final String NOTICE134 = "Notice 134";
	public static final String NOTICE125 = "Notice125";
	public static final String NOTICE_PRATIVRUTTA = "Prativrutta";
	public static final String NOTICE155 = "Notice 155";
	public static final String NOTICE156 = "Notice 156";
	public static final String NOTICE159 = "Notice 159";
	public static final String MUTATION_CERTIFICATE = "MutationCertificate";

	// Notice number sequence strings for generating sequence number
	public static final String PRATIVRUTTA_SEQ_OBJSTR = "PT_PRATHIVRUTTA";
	public static final String NOTICE155_SEQ_STR = "PT_NOTICE155";
	public static final String NOTICE156_SEQ_STR = "PT_NOTICE156";
	public static final String NOTICE159_SEQ_STR = "PT_NOTICE159";
	public static final String NOTICE127_SEQ_STR = "PT_NOTICE127";
	public static final String NOTICE134_SEQ_STR = "PT_NOTICE134";
	public static final String NOTICE125_SEQ_STR = "PT_NOTICE125";
	public static final String PRATIVRUTTA_SEQ_STR = "PT_PRATHIVRUTTA";
	public static final String OBJECTION_SEQ_STR = "PT_OBJECTION_NO";
	public static final String HEARINGNO_SEQ_STR = "PT_HEARINGNO";
	public static final String MUTATIONAPPLNO_SEQ_STR = "PT_MUT_APPLNO";
	public static final String MEMONO_SEQ_STR = "PT_MEMO_NO";
	public static final String REJECTION_SEQ_STR = "PT_REJECTION_NO";

	// Report names
	public static final String REPORT_TEMPLATENAME_NOTICE127 = "notice127";
	public static final String REPORT_TEMPLATENAME_NOTICE134 = "notice134";
	public static final String REPORT_TEMPLATENAME_NOTICE125 = "notice125";
	public static final String REPORT_TEMPLATENAME_NOTICE_PRATIVRUTTA = "Notice_Prativrutta";
	public static final String REPORT_TEMPLATENAME_DEMAND_CALSHEET = "IndCalculationSheet";
	public static final String WARRANT_APPLICATION = "Warrant-application";
	public static final String REPORT_TEMPLATENAME_EGS_EDU_CESS_COLLECTION = "Egs_EduCess_CollectionInfo";
	public static final String REPORT_TEMPLATENAME_CALSHEET_FOR_GOVT_PROPS = "IndCalSheetForGovtProperties";
	public static final String REPORT_TEMPLATENAME_MUTATION_CERTIFICATE = "MutationCertificate";
	public static final String REPORT_TEMPLATENAME_JAMABANDI = "Jamabandi";
	public static final String REPORT_TEMPLATENAME_BAKAYAFERIST = "Bakaya_Ferist";
	public static final String REPORT_TEMPLATENAME_HEADWISEDMDCOLL = "HeadWiseDmdNdRec";
	public static final String REPORT_TEMPLATENAME_BIGBUILDINGRECOVERY = "BigBuildingRecoveryStmt";
	public static final String REPORT_TEMPLATENAME_DAILY_COLLECTION = "DailyCollectionReport";
	public static final String REPORT_TEMPLATENAME_DAILY_ABSTRACT_RECOVERY_REPORT = "DailyAbstractRecoveryReport";
	public static final String REPORT_TEMPLATENAME_DCBREPORT = "DcbReport";

	// Notice Number Prefixes
	public static final String NOTICE127_NOTICENO_PREFIX = "N127/";
	public static final String NOTICE134_NOTICENO_PREFIX = "N134/";
	public static final String NOTICE125_NOTICENO_PREFIX = "N125/";
	public static final String PRATIVRUTTA_NOTICENO_PREFIX = "PVR/";
	public static final String NOTICE155_NOTICENO_PREFIX = "N155/";
	public static final String NOTICE156_NOTICENO_PREFIX = "N156/";
	public static final String NOTICE159_NOTICENO_PREFIX = "N159/";
	public static final String MUTATION_CERTIFICATE_NOTICENO_PREFIX = "MC/";
	// Occupancy Types
	public static final String TENANT = "TENANT";
	public static final String OWNER = "SELFOCC";
	public static final String OCCUPANCY_VACANT = "VACANT";
	public static final String OCCUPIER = "OCCUPIER";

	public static final String TENANT_OCC = "Tenanted";
	public static final String OWNER_OCC = "Owner";
	public static final String VACANT_OCC = "Vacant";
	public static final String OCCUPIER_OCC = "Occupier";

	// HashMap for Unit Area calculation floor wise of Residential Property
	public static final HashMap<String, String> RESIDENTIAL_FLOOR_AREA_MAP = new HashMap<String, String>() {
		{
			put("-5-46.45", "1");
			put("-5-93", ".8");
			put("-5-139.5", ".7");
			put("-5-139.6", ".6");
			put("-4-46.45", "1");
			put("-4-93", ".8");
			put("-4-139.5", ".7");
			put("-4-139.6", ".6");
			put("0-46.45", "1");
			put("0-93", ".8");
			put("0-139.5", ".7");
			put("0-139.6", ".6");
			put("1-46.45", ".9");
			put("1-93", ".72");
			put("1-139.5", ".63");
			put("1-139.6", ".54");
			put("2-46.45", ".8");
			put("2-93", ".64");
			put("2-139.5", ".56");
			put("2-139.6", ".48");
			put("3-46.45", ".7");
			put("3-93", ".56");
			put("3-139.5", ".49");
			put("3-139.6", ".42");
		}
	};

	// HashMap for Unit Area calculation floor wise of Non-residential Property
	public static final HashMap<String, String> NON_RESIDENTIAL_FLOOR_AREA_MAP = new HashMap<String, String>() {
		{
			put("-5-1", "1");
			put("-5-2", ".5");
			put("-4-1", "1");
			put("-4-2", ".5");
			put("0-1", "1");
			put("0-2", ".5");
			put("1-1", ".7");
			put("1-2", ".35");
		}
	};

	// TreeMap for Residential property types
	public static final TreeMap<String, String> RESIDENTIAL_PROPERTY_TYPE_CATEGORY = new TreeMap<String, String>() {
		{
			put("RESIDENTIAL_HOUSE", "Residential House");
			put("RESIDENTIAL_FLATS", "Residential Flats");
		}
	};

	// TreeMap for Non Residential property types
	public static final TreeMap<String, String> NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY = new TreeMap<String, String>() {
		{
			put("SHOP", "Shop");
			put("RESTAURANT_WITH_BAR", "Restaurant with bar");
			put("DEPARTMENTAL_STORE_AND_SHOPPING_CENTER", "Departmental store and shopping center");
			put("DISPENSARY_CLINIC_AND_PATHOLOGICAL_LABORATORY", "Dispensary, clinic and pathological laboratory");
			put("PRIVATE_HEALTH_CLUB_GYMNASIUM", "Private health club, gymnasium");
			put("OFFICE", "Office");
			put("AIRPORT_BUILDINGS", "Airport buildings");
			put("PASSENGER_TERMINAL_AT_AIRPORT", "Passenger terminal at airport");
			put("HANGAR_AND_WORKSHOP_AT_AIRPORT", "Hangar and workshop at airport");
			put("MANGAL_KARYALAYA_HALL_AIR_CONDITIONED",
					"Mangal Karyalaya / hall / community hall / convention hall / party hall, etc. ( air-conditioned )");
			put("MANGAL_KARYALAYA_HALL_NON_AIR_CONDITIONED",
					"Mangal Karyalaya / hall / community hall / convention hall / party hall, etc. ( non air-conditioned )");
			put("GODOWN_STORAGE_WAREHOUSE", "Godown / storage / warehouse");
			put("UNSTARRED_HOTEL", "Unstarred hotel");
			put("HOTEL_UPTO_FOUR_STAR_AND_SERVICE_APARTMENT", "Hotel upto four star and service apartment");
			put("HOTEL_FIVE_STAR_AND_ABOVE", "Hotel - five star and above");
			put("BANK", "Bank");
			put("NON_BANKING_FINANCIAL_INSTITUTION", "Non-banking financial institution");
			put("LIFE_AND_NON_LIFE_INSURANCE_CORPORATION_OR_COMP", "Life and non-life insurance corporation or company");
			put("ASSET_MANAGEMENT_COMP_AND_TRUSTEE_COMP_OF_MUTUAL_FUND",
					"Asset management company and trustee company of Mutual Fund");
			put("STOCK_EXCHANGE", "Stock exchange");
			put("COMMODITY_EXCHANGE", "Commodity exchange");
			put("SECURITY_EXCHANGE_BOARD_OF_INDIA", "Security Exchange Board of India");
			put("ATM_CENTER_AND_MONEY_CHANGING_CENTER", "Automatic Teller Machine Centre and Money Changing Centre");
			put("CO_OPERATIVE_CREDIT_SOCIETY", "Co-operative credit society");
			put("MALL", "Mall");
			put("EDUCATIONAL_INSTITUTION", "Educational institution");
			put("NURSERY_KIDS_CORNER_PLAYGROUP", "Nursery, kids'corner, playgroup");
			put("CAR_PARKING_IN_STILT_BASEMENT_PODIUM", "Car parking in stilt/basement/podium");
			put("ENCLOSED GARAGE", "Enclosed garage");
			put("SOCIETY_OFFICE", "Society office");
			put("ELECTRIC_SUB_STATION", "Electric sub-station");
			put("HOSPITAL", "Hospital");
			put("SUPERSPECIALITY_HOSPITAL", "Superspeciality hospital");
			put("NURSING_HOME", "Nursing home");
			put("ADVERTISEMENT_HOARDING", "Advertisement hoarding");
			put("MOBILE_TOWER", "Mobile Tower");
			put("CINEMA_HALL_THEATRE_DRAMA_THEATRE", "Cinema hall / theatre / drama theatre");
			put("CLUB_HOUSE", "Club house");
			put("COACHING_CLASS", "Coaching class");
			put("SPECIAL_CAR_PARKING_STRUCTURE", "Special car parking structure");
			put("MULTIPLEX", "Multiplex");
			put("FILM_SHOOTING_STUDIO", "Film shooting studio");
			put("OPEN_AIR_THEATRE_STAGE_AND_OTHER_STRUCTURES", "Open air theatre - stage and other structures");
			put("SWIMMING_POOL", "Swimming pool");
			put("PETROL_PUMP_OR_SERVICE_STATION_OR_LPG_OR_CNG_STATION_OR_KEROSENE_STATION",
					"Structures ancillary to petrol pump or service station or LPG or CNG station or kerosene station");
			put("STABLE", "Stable");
			put("WEIGHBRIDGE", "Weighbridge");
			put("FLAT", "Flat");
			put("AMUSEMENT_PARK", "Amusement park");

		}
	};
	// TreeMap for OpenPlot property types
	public static final HashMap<String, String> OPEN_PLOT_PROPERTY_TYPE_CATEGORY = new HashMap<String, String>() {
		{
			put("RESD", "Residential");
			put("NON_RESD", "Non-Residential");
			put("RESD_CUM_NON_RESD", "Residential + Non-Residential");
		}
	};

	// Area Constants(in square meter)
	public static final Double AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING = new Double(46.45d);

	public static final Double AREA_CONSTANT_FOR_NONRESIDENTIAL_BUILDING = new Double(7.62d);

	public static final Double AREA_CONSTANT_FOR_LARGE_RESIDENTIAL_PREMISES = new Double(150d);

	// Named Queries
	public static final String QUERY_DEPRECIATION_BY_YEAR = "DEPRECIATION_BY_YEAR";
	public static final String QUERY_BASERATE_BY_OCCUPANCY_ZONE = "BASERATE_BY_OCCUPANCY_ZONE";
	public static final String QUERY_PROPERTY_BY_UPICNO = "getPropertyByUpicNo";
	public static final String QUERY_PROPERTY_BY_UPICNO_AND_STATUS = "getPropertyByUpicNoAndStatus";
	public static final String QUERY_BASICPROPERTY_BY_UPICNO = "getBasicPropertyByUpicNo";
	public static final String QUERY_NOTICE_BY_NOTICENO = "getNoticeByNoticeNo";
	public static final String QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR = "INSTALLMENTLISTBY_MODULE_AND_STARTYEAR";
	public static final String QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID = "DEMANDREASONBY_CODE_AND_INSTALLMENTID";
	public static final String QUERY_DEMANDREASONDETAILBY_DEMANDREASONID = "DEMANDREASONDETAILBY_DEMANDREASONID";
	public static final String QUERY_DEMANDREASONDETAILS_BY_DEMANDREASONID_DATE = "DEMANDREASONDETAILS_BY_DEMANDREASONID_DATE";
	public static final String QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT = "DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT";
	public static final String QUERY_BASERENT_BY_BOUNDARY_FOR_OPENPLOT = "QUERY_BASERENT_BY_BOUNDARY_FOR_OPENPLOT";
	public static final String QUERY_DEPARTMENTS_BY_DEPTCODE = "getDepartmentsByDeptCode";
	public static final String QUERY_LATEST_BILL_FOR_PROPERTY = "getLatestBillsForProperty";
	public static final String QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE = "getPropStatValByUpicNoAndStatCodeAndISActive";
	public static final String QUERY_BILLRECEIPT_FOR_BILL = "getBillRcptGForBill";
	public static final String QUERY_INSTALLMENTBY_MODULE_AND_DESC = "INSTALLMENTBY_MODULE_AND_DESC";
	public static final String QUERY_BILLDETAIL_BILL_INST_RSN = "getBillDetailsforBillANDInstAndRsn";
	public static final String QUERY_BASICPROPERTY_BY_BASICPROPID = "getBasicPropertyByBasicPropId";
	public static final String QUERY_PROPSTATVALUE_BY_BASICPROPID_CODE_ISACTIVE = "getPropStatValByBasicPropIdAndStatCodeAndISActive";
	public static final String QUERY_NOCOLL_DEMANDDET_FOR_PROPERTY = "getNoCollDemandDetForProperty";
	public static final String QUERY_NOCOLL_DEMANDDET_FOR_PROPERTY_FORINST = "getNoCollDemandDetForPropertyAndInst";
	public static final String QUERY_DEMANDDET_FOR_PROPERTY = "getDemandDetForProperty";
	public static final String QUERY_DEMANDDETAILS_EXCLUDING_REASONS = "demandDetailsExcludingReasons";
	public static final String QUERY_PROPERTIES_FOR_PENALTY_CALCULATION = "propertiesForPenaltyCalculation";

	// Base Rents
	public static final BigDecimal BASERENT_FROM_APRIL2008_BUILDINGS = new BigDecimal("10");
	public static final BigDecimal BASERENT_FROM_APRIL2008_OPENPLOT = new BigDecimal("5");

	// Standard Deduction 10%
	public static final Double STANDARD_DEDUCTION_PERCENTAGE = new Double(.1d);

	// ALV Constant for Large Residential Premises Tax
	public static final BigDecimal LARGE_RESIDENTIAL_PREMISES_ALV = new BigDecimal("1500");

	// Others
	public static final String STRING_SEPERATOR = "-";
	/*
	 * it is break date for rent chart and base rate methods, as now we want
	 * only rent chart calculation on system for all the installments, we
	 * changed this date to 2020. its just hack
	 */
	public static final String DATE_CONSTANT = "01/04/2020";

	public static final char BLANK_CHAR = ' ';
	public static final char PIPE_CHAR = '|';
	public static final String COMMA_STR = ",";
	public static final String BLANK_STR = " ";
	public static final String EMPTY_STR = "";

	// Demand Reason master codes
	public static final String DEMANDRSN_CODE_GENERAL_TAX = "GEN_TAX";
	public static final String DEMANDRSN_CODE_LIBRARY_CESS = "LIB_CESS";
	public static final String DEMANDRSN_CODE_EDUCATIONAL_CESS = "EDU_CESS";
	public static final String DEMANDRSN_CODE_UNAUTHORIZED_PENALTY = "UNAUTH_PENALTY";
	public static final String DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY = "CHQ_BUNC_PENALTY";
	public static final String DEMANDRSN_CODE_PENALTY_FINES = "PENALTY_FINES";
	public static final String DEMANDRSN_CODE_ADVANCE = "ADVANCE";
	public static final String DEMANDRSN_CODE_ADVANCE_REBATE = "ADVANCE_REBATE";
	public static final String DEMANDRSN_REBATE = "REBATE";
	public static final String DEMANDRSN_CODE_WARRANT_FEE = "WARRANT_FEE";
	public static final String DEMANDRSN_CODE_NOTICE_FEE = "NOTICE_FEE";
	public static final String DEMANDRSN_CODE_COURT_FEE = "COURT_FEE";
	public static final String DEMANDRSN_CODE_RECOVERY_FEE = "RECOVERY_FEE";

	// Demand Reason master Strings
	public static final String DEMANDRSN_STR_GENERAL_TAX = "GENERAL TAX";
	public static final String DEMANDRSN_STR_LIBRARY_CESS = "LIBRARY CESS";
	public static final String DEMANDRSN_STR_EDUCATIONAL_CESS = "EDUCATION CESS";
	public static final String DEMANDRSN_STR_UNAUTHORIZED_PENALTY = "UNAUTHORIZED PENALTY";
	public static final String DEMANDRSN_STR_CHQ_BOUNCE_PENALTY = "CHEQUE BOUNCE PENALTY";
	public static final String DEMANDRSN_STR_PENALTY_FINES = "PENALTY_FINES";
	public static final String DEMANDRSN_STR_ADVANCE = "ADVANCE";
	public static final String DEMANDRSN_STR_ADVANCE_REBATE = "ADVANCE_REBATE";

	// Tax Payable constants
	public static final String DEMANDRSN_CODE_GEN_TAX_PAYABLE = "PROPERTY TAX-PROPERTY TAX ON ALL PROPERTIES";
	public static final String DEMANDRSN_CODE_EDUCATIONAL_CESS_PAYABLE = "EDUCATION CESS PAYABLE";
	public static final String DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX_PAYABLE = "BIG RESIDENTIAL BUILDING TAX PAYABLE";
	public static final String DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX_PAYABLE = "EMPLOYMENT GUARANTEE CESS PAYABLE";
	public static final String DEMANDRSN_CODE_GENERAL_WATER_TAX_PAYABLE = "WATER TAX-WATER TAX";
	public static final String DEMANDRSN_CODE_SEWERAGE_TAX_PAYABLE = "SEWERAGE TAX-SEWERAGE TAX";
	public static final String DEMANDRSN_CODE_FIRE_SERVICE_TAX_PAYABLE = "FIRE SERVICE TAX PAYABLE";
	public static final String DEMANDRSN_CODE_LIGHTINGTAX_PAYABLE = "LIGHTING TAX-LIGHT TAX";

	// HashMap for order of demand reasons to be set in bill
	public static final HashMap<String, Integer> DEMAND_REASON_ORDER_MAP = new HashMap<String, Integer>() {
		{
			put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, 0);
			put(DEMANDRSN_CODE_PENALTY_FINES, 1);
			put(DEMANDRSN_CODE_GENERAL_TAX, 2);
			put(DEMANDRSN_CODE_LIBRARY_CESS, 3);
			put(DEMANDRSN_CODE_EDUCATIONAL_CESS, 4);
			put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, 5);
			put(DEMANDRSN_REBATE, 6);
		}
	};

	// General water rates
	public static final String WATER_METER = "WATER_METER";
	public static final String GWR_IMPOSED = "GWR_IMPOSED";
	public static final String GWR_NOT_IMPOSED = "GWR_NOT_IMPOSED";
	public static final String WATER_LINE_WITHOUT_METER = "WATER_LINE_WITHOUT_METER";
	public static final String WATER_LINE_WITH_METER20 = "WATER_LINE_WITH_METER20";

	public static final String EFFECTIVE_ASSESSMENT_PERIOD1 = "1 Jan";
	public static final String EFFECTIVE_ASSESSMENT_PERIOD2 = "1 Apr";
	public static final String EFFECTIVE_ASSESSMENT_PERIOD3 = "1 Jul";
	public static final String EFFECTIVE_ASSESSMENT_PERIOD4 = "1 Oct";

	public static final Map<Integer, String> FLOOR_MAP = new HashMap<Integer, String>() {
		{
			put(-5, "Lower Ground Floor");
			put(-4, "Upper Ground Floor");
			put(-3, "N/A");
			put(0, "Ground Floor");
			put(1, "First Floor");
			put(2, "Second Floor");
			put(3, "Third Floor");
			put(4, "Fourth Floor");
			put(5, "Fifth Floor");
			put(6, "Sixth Floor");
			put(7, "Seventh Floor");
			put(8, "Eighth Floor");
			put(9, "Ninenth Floor");
			put(10, "Tenth Floor");
			put(11, "Eleventh Floor");
			put(12, "Twelfth Floor");
			put(13, "Thirteenth Floor");
			put(14, "Fourteenth Floor");
			put(15, "Fifteenth Floor");
			put(16, "Sixteenth Floor");
			put(17, "Seventeenth Floor");
			put(18, "Eighteenth Floor");
			put(19, "Nineteenth Floor");
			put(20, "Twentieth Floor");
			put(21, "Twenty First Floor");
			put(22, "Twenty Second Floor");
			put(23, "Twenty Third Floor");
			put(24, "Twenty Fourth Floor");
			put(25, "Twenty Fifth Floor");
			put(26, "Twenty Sixth Floor");
			put(27, "Twenty Seventh Floor");
			put(28, "Twenty Eighth Floor");
			put(29, "Twenty Ninth Floor");
			put(30, "Thirtieth Floor");
			put(31, "Thirty First Floor");
			put(32, "Thirty Second Floor");
			put(33, "Thirty Third Floor");
			put(34, "Thirty Fourth Floor");
			put(35, "Thirty Fifth Floor");
			put(36, "Thirty Sixth Floor");
			put(37, "Thirty Seventh Floor");
			put(38, "Thirty Eighth Floor");
			put(39, "Thirty Ninth Floor");
			put(40, "Fortieth Floor");
			put(41, "Forty First Floor");
			put(42, "Forty Second Floor");
			put(43, "Forty Third Floor");
			put(44, "Forty Fourth Floor");
			put(45, "Forty Fifth Floor");
			put(46, "Forty Sixth Floor");
			put(47, "Forty Seventh Floor");
			put(48, "Forty Eighth Floor");
			put(49, "Forty Nineth Floor");
			put(50, "Fiftieth Floor");

		}
	};

	// workflow step names
	public static final String WFLOW_ACTION_STEP_CREATE = "Create";
	public static final String WFLOW_ACTION_STEP_SAVE = "Save";
	public static final String WFLOW_ACTION_STEP_FORWARD = "Forward";
	public static final String WFLOW_ACTION_STEP_APPROVE = "Approve";
	public static final String WFLOW_ACTION_STEP_NOTICE_GENERATED = "Notice_Generated";
	public static final String WFLOW_ACTION_STEP_REJECT = "Reject";

	// workflow action names
	public static final String WFLOW_ACTION_NAME_CREATE = "Create";
	public static final String WFLOW_ACTION_NAME_TRANSFER = "Mutation";
	public static final String WFLOW_ACTION_NAME_DEACTIVATE = "Deactivate";
	public static final String WFLOW_ACTION_NAME_CHANGEADDRESS = "ChangeAddress";
	public static final String WFLOW_ACTION_NAME_MODIFY = "Modify";
	public static final String WFLOW_ACTION_NAME_AMALGAMATE = "Amalgamate";
	public static final String WFLOW_ACTION_NAME_BIFURCATE = "Bifurcate";
	public static final String WFLOW_ACTION_NAME_GENERATE_NOTICE = "NoticeGeneration";

	// WORKFLOW property states
	public static final String WF_STATE_NEW = "New";
	public static final String WF_STATE_APPROVAL_PENDING = "Approval_Pending";
	public static final String WF_STATE_NOTICE_GENERATION_PENDING = "Notice_Generation_Pending";

	public static final String REPORT_TEMPLATENAME_BILL_GENERATION = "propertybill";

	// Prefix for sequence names, for bill number generation
	public static final String BILLGEN_SEQNAME_PREFIX = "BDIV-";
	public static final String MANUAL_BILLGEN_SEQNAME_PREFIX = "MBDIV-";

	// Type of demand dues
	public static final String ARREARS_DMD = "ARREARS";
	public static final String CURRENT_DMD = "CURRENT";

	public static final String ARREARS_DEMAND = "ARREARS_DEMAND";
	public static final String CURRENT_DEMAND = "CURRENT_DEMAND";

	// Voucher constants
	public static final String VOUCH_CREATE_RSN_CREATE = "Create";
	public static final String VOUCH_CREATE_RSN_MODIFY = "Modify";
	public static final String VOUCH_CREATE_RSN_DEACTIVATE = "Deactivate";

	// Property Creation reasons
	public static final String PROP_CREATE_RSN = "CREATE";
	public static final String PROP_CREATE_RSN_BIFUR = "BIFUR";

	// Property is default values
	public static final Character PROPERTY_IS_DEFAULT = 'Y';
	public static final Character PROPERTY_IS_NOT_DEFAULT = 'N';

	// Property Modification Reasons
	public static final String PROPERTY_MODIFY_REASON_AMALG = "AMALG";
	public static final String PROPERTY_MODIFY_REASON_BIFURCATE = "BIFURCATE";
	public static final String PROPERTY_MODIFY_REASON_OBJ = "OBJ";
	public static final String PROPERTY_MODIFY_REASON_MODIFY = "MODIFY";
	public static final String PROPERTY_MODIFY_REASON_COURT_RULE = "COURT_RULE";
	public static final String PROPERTY_MODIFY_REASON_DATA_ENTRY = "DATA_ENTRY";
	public static final String PROPERTY_MODIFY_REASON_DATA_UPDATE = "DATA_UPDATE";
	public static final String PROPERTY_MODIFY_REASON_EDIT_OWNER = "EDIT_OWNER";
	public static final String PROPERTY_MODIFY_REASON_ADD_OR_ALTER = "ADD OR ALTER";
	public static final List<String> modifyReasons = Arrays.asList(PROPERTY_MODIFY_REASON_AMALG,
			PROPERTY_MODIFY_REASON_BIFURCATE, PROPERTY_MODIFY_REASON_OBJ, PROPERTY_MODIFY_REASON_DATA_ENTRY,
			PROPERTY_MODIFY_REASON_DATA_UPDATE, PROPERTY_MODIFY_REASON_MODIFY);

	// Constants for Government Properties
	public static final Double STATEGOVT_BUILDING_ALV_PERCENTAGE = new Double(8.25d);
	public static final Double CENTRALGOVT_BUILDING_ALV_PERCENTAGE = new Double(9d);
	public static final Double STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION = new Double(20d);

	// Amenities
	public static final String AMENITY_TYPE_FULL = "Full";
	public static final String AMENITY_TYPE_PARTIAL = "Partial";
	public static final String AMENITY_TYPE_NIL = "Nil";
	public static final Double AMENITY_PERCENTAGE_FULL = new Double(75d);
	public static final Double AMENITY_PERCENTAGE_PARTIAL = new Double(50d);
	public static final Double AMENITY_PERCENTAGE_NIL = new Double(33.33d);

	// Residential Cum Commercial properties
	public static final Double RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE = new Double(50d);

	// Property Status
	public static final String PROPERTY_STATUS_MARK_DEACTIVE = "MARK_DEACTIVE";
	public static final String PROPERTY_STATUS_INACTIVE = "INACTIVE";
	public static final String PROP_STATUS_TYPE_DEACT = "DEACTIVATE";

	public static final String DEFAULT_FUNCTIONARY_CODE = "1";
	public static final String DEFAULT_FUND_SRC_CODE = "01";
	public static final String DEFAULT_FUND_CODE = "01";
	public static final String DEPT_CODE_TAX = "T";

	// Roles
	public static final String PROPERTY_CSC_OPERATOR = "CSC Operator";
	public static final String ASSISTANT_ROLE = "ASSISTANT";
	public static final String PTCREATOR_ROLE = "PTCreator";
	public static final String PTVALIDATOR_ROLE = "PTValidator";
	public static final String PTAPPROVER_ROLE = "PTApprover";

	// Designations
	public static final String ASSISTANT_DESGN = "Assistant";
	public static final String REVENUE_OFFICER_DESGN = "Revenue officer";
	public static final String COMMISSIONER_DESGN = "Commissioner";
	public static final String END_APPROVER_DESGN = "RO";

	public static final String WFSTATUS = "WFSTATUS";
	public static final String WFOWNER = "WFOWNER";

	// GIS
	public static final String GISCITY = "nmc";
	public static final String GISVERSION = "/mapguide/";

	// HashMap for GLCodes for Current Taxes
	public static final HashMap<String, String> GLCODEMAP_FOR_CURRENTTAX = new HashMap<String, String>() {
		{

			put(DEMANDRSN_CODE_GENERAL_TAX, "4311002");
			put(DEMANDRSN_CODE_LIBRARY_CESS, "4311032");
			put(DEMANDRSN_CODE_EDUCATIONAL_CESS, "4311042");
			put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, "4311012");

		}
	};
	// HashMap for GLCodes for Arrear Taxes
	public static final HashMap<String, String> GLCODEMAP_FOR_ARREARTAX = new HashMap<String, String>() {
		{
			put(DEMANDRSN_CODE_GENERAL_TAX, "4311001");
			put(DEMANDRSN_CODE_LIBRARY_CESS, "4311031");
			put(DEMANDRSN_CODE_EDUCATIONAL_CESS, "4311041");
			put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, "4311011");
		}
	};

	// HashMap for GLCodes for Tax Payables

	public static final HashMap<String, String> GLCODEMAP_FOR_TAX_PAYABLE = new HashMap<String, String>() {
		{
			put(DEMANDRSN_CODE_GENERAL_TAX, "1100101");
			put(DEMANDRSN_CODE_LIBRARY_CESS, "1105201");
			put(DEMANDRSN_CODE_EDUCATIONAL_CESS, "1105201");
			put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, "1100102");
		}
	};

	// List for GLCodes for Current Taxes
	public static final List<String> GLCODES_FOR_CURRENTTAX = new ArrayList<String>() {
		{
			for (Map.Entry<String, String> glCode : GLCODEMAP_FOR_CURRENTTAX.entrySet()) {
				add(glCode.getValue());
			}
		}
	};
	// List for GLCodes for Arrear Taxes
	public static final List<String> GLCODES_FOR_ARREARTAX = new ArrayList<String>() {
		{
			for (Map.Entry<String, String> glCode : GLCODEMAP_FOR_ARREARTAX.entrySet()) {
				add(glCode.getValue());
			}
		}
	};

	// HashMap map b/n Demand reason string and code
	public static final LinkedHashMap<String, String> DMDRSN_CODE_MAP = new LinkedHashMap<String, String>() {
		{
			put(DEMANDRSN_STR_GENERAL_TAX, DEMANDRSN_CODE_GENERAL_TAX);
			put(DEMANDRSN_STR_LIBRARY_CESS, DEMANDRSN_CODE_LIBRARY_CESS);
			put(DEMANDRSN_STR_EDUCATIONAL_CESS, DEMANDRSN_CODE_EDUCATIONAL_CESS);
			put(DEMANDRSN_STR_UNAUTHORIZED_PENALTY, DEMANDRSN_CODE_UNAUTHORIZED_PENALTY);
			put(DEMANDRSN_STR_CHQ_BOUNCE_PENALTY, DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY);
			put(DEMANDRSN_STR_PENALTY_FINES, DEMANDRSN_CODE_PENALTY_FINES);
		}
	};

	public static final String GLCODE_FOR_TAXREBATE = "4314209";// 2801002
	public static final String GLCODE_FOR_PENALTY = "1402002";// 1402001,
																// 4314208

	public static final String GLCODE_FOR_ADVANCE_REBATE = "4314209";
	public static final String GLCODE_FOR_ADVANCE = "3504102";

	public static final BigDecimal FIRST_REBATETAX_PERC = new BigDecimal("4");
	public static final BigDecimal SECOND_REBATETAX_PERC = new BigDecimal("2");
	public static final BigDecimal CHQ_BOUNCE_PENALTY = new BigDecimal("1000");

	// Penalty
	public static final String ARR_LP_DATE_CONSTANT = "01/06/2010";
	public static final String CURR_LP_DATE_CONSTANT = "01/06/2010";
	public static final BigDecimal LP_PERCENTAGE_CONSTANT = new BigDecimal(2);
	public static final String ARR_LP_DATE_BREAKUP = "01/04/2010";
	public static final String PENALTY_WATERTAX_EFFECTIVE_DATE = "01/01/2012";

	public static final String APPCONFIG_TAXCALC_RULE_RENTCHART = "PTTAXCALC_RULE_RENTCHART";

	// Property Docs
	public static final String DOCS_CREATE_PROPERTY = "CREATE";
	public static final String DOCS_MUTATION_PROPERTY = "MUTATION";
	public static final String DOCS_MODIFY_PROPERTY = "MODIFICATION";
	public static final String DOCS_AMALGAMATE_PROPERTY = "AMALGAMATION";
	public static final String DOCS_BIFURCATE_PROPERTY = "BIFURCATION";
	public static final String DOCS_ADDRESS_CHANGE_PROPERTY = "PROPERTY ADDRESS CHANGE";
	public static final String DOCS_DEACTIVATE_PROPERTY = "DEACTIVATE";

	public static final Map<String, String> FLOOR_TYPES = new HashMap<String, String>() {
		{
			put("Varandah", "Varandah");
			put("Loft", "Loft");
			put("Mezzanine Floor", "Mezzanine Floor");
			put("Covered Parking", "Covered Parking");
			put("Uncovered Parking", "Uncovered Parking");
			put("Basement", "Basement");
			put("Godown", "Godown");
		}
	};

	// Unit Types
	public static final String UNITTYPE_OPEN_PLOT = "OPEN_PLOT";
	public static final String UNITTYPE_RESD = "RESIDENTIAL";
	public static final String UNITTYPE_NON_RESD = "NON_RESD";

	public static final Integer OPEN_PLOT_UNIT_FLOORNUMBER = -3;

	public static final Map<Integer, String> MONTHS_MAP = new TreeMap<Integer, String>() {
		{
			put(JANUARY, "Jan");
			put(FEBRUARY, "Feb");
			put(MARCH, "Mar");
			put(APRIL, "Apr");
			put(MAY, "May");
			put(JUNE, "Jun");
			put(JULY, "Jul");
			put(AUGUST, "Aug");
			put(SEPTEMBER, "Sep");
			put(OCTOBER, "Oct");
			put(NOVEMBER, "Nov");
			put(DECEMBER, "Dec");
		}
	};

	public static final String GLCODE_FOR_EDU_CESS_ARREARS = "4312001";
	public static final String GLCODE_FOR_EDU_CESS_CURRENT = "4312002";
	public static final String GLCODE_FOR_EGS_CESS_ARREARS = "4312003";
	public static final String GLCODE_FOR_EGS_CESS_CURRENT = "4312004";
	public static final String GLCODE_FOR_GENERAL_TAX_ARREARS = "4311001";
	public static final String GLCODE_FOR_GENERAL_TAX_CURRENT = "4311002";
	public static final String GLCODE_FOR_FIRE_SERVICE_TAX_ARREARS = "4311011";
	public static final String GLCODE_FOR_FIRE_SERVICE_TAX_CURRENT = "4311012";
	public static final String GLCODE_FOR_GENERAL_WATER_TAX_ARREARS = "4311021";
	public static final String GLCODE_FOR_GENERAL_WATER_TAX_CURRENT = "4311022";
	public static final String GLCODE_FOR_SEWERAGE_TAX_ARREARS = "4311031";
	public static final String GLCODE_FOR_SEWERAGE_TAX_CURRENT = "4311032";
	public static final String GLCODE_FOR_LIGHTINGTAX_ARREARS = "4311041";
	public static final String GLCODE_FOR_LIGHTINGTAX_CURRENT = "4311042";
	public static final String GLCODE_FOR_BIG_RESIDENTIAL_BLDG_TAX_ARREARS = "4311003";
	public static final String GLCODE_FOR_BIG_RESIDENTIAL_BLDG_TAX_CURRENT = "4311004";
	public static final String GLCODE_FOR_MUTATION_FEE = "4311002";

	public static final List<String> EDU_EGS_CESS_GLCODE_LIST = new ArrayList<String>() {
		{
			add("4312001");
			add("4312002");
			add("4312003");
			add("4312004");
		}
	};

	public static final String STR_EDU_CESS = "Education Cess";
	public static final String STR_EGS_CESS = "EGS Cess";

	public static final String REPORT_START_DATE = "01/04/2012";

	public static final List<String> FLOORNO_WITH_DIFF_MULFACTOR_RESD = new ArrayList<String>() {
		{
			add("-5");
			add("-4");
			add("0");
			add("1");
			add("2");
		}
	};

	public static final List<String> FLOORNO_WITH_DIFF_MULFACTOR_NONRESD = new ArrayList<String>() {
		{
			add("-5");
			add("-4");
			add("0");
		}
	};

	public static final String OPEN_PLOT_SHORTFORM = "OP";
	public static final String RESD_SHORTFORM = "R";
	public static final String NONRESD_SHORTFORM = "NR";
	public static final String STATE_GOVT_SHORTFORM = "SGovt";
	public static final String CENTRAL_GOVT_SHORTFORM = "CGovt";
	public static final String MIXED_SHORTFORM = "R-NR";

	// FLoor Types
	public static final String GODOWN = "Godown";
	public static final String BASEMENT = "Basement";

	// 1Sq.Mt = 10.76Sq.Ft
	public static final BigDecimal SqFt = new BigDecimal(10.76);

	// Used during Notice Generation
	public static final String STYLE_TAG_BEGIN = "<style forecolor=\"#000000\" isBold=\"true\" pdfFontName=\"Times-Bold\" pdfEncoding=\"Cp1252\" isPdfEmbedded=\"true\">";
	public static final String STYLE_TAG_END = "</style>";

	public static final String ROLE_OPERATOR = "Operator";

	// antisamy hack
	public static final String AMP_ENCODED_STR = "&amp;";
	public static final String AMP_ACTUAL_STR = "&";

	public static final List<String> DEMAND_RSNS_LIST = new ArrayList<String>() {
		{
			add("GEN_TAX");
			add("SEWERAGETAX");
			add("FIRE_SER_TAX");
			add("LIGHTINGTAX");
			add("GEN_WATER_TAX");
			add("EDU_CESS_RESD");
			add("EMP_GUA_CESS");
			add("BIG_RESD_TAX");
			add("EDU_CESS_NONRESD");
		}
	};

	public static final Map<String, String> waterRates = new HashMap<String, String>() {
		{
			put("WATER_METER", "Water Meter");
			put("GWR_IMPOSED", "GWR to be imposed");
			put("GWR_NOT_IMPOSED", "GWR not to be imposed");
			put("WATER_LINE_WITHOUT_METER", "Water Line Without Meter");
			put("WATER_LINE_WITH_METER20", "Water line without meter 20 mm dia");
		}
	};

	public static final String NOTICE_TYPE_BILL = "Bill";

	public static final String CREATE_AUDIT_ACTION = "Create Property";
	public static final String MODIFY_AUDIT_ACTION = "Modify Property";
	public static final String AMALG_AUDIT_ACTION = "Amalgamate Property";
	public static final String BIFUR_AUDIT_ACTION = "Bifurcate Property";
	public static final String TRANSFER_AUDIT_ACTION = "Transfer Property";
	public static final String CHANGEADDRESS_AUDIT_ACTION = "Change Property Address";
	public static final String DEACTIVE_AUDIT_ACTION = "Deactivate Property";
	public static final String AUDITDATA_STRING_SEP = "\n";
	public static final String DATAUPDATE_AUDIT_ACTION = "Assessment Data Update";
	public static final String EDIT_DEMAND_AUDIT_ACTION = "Edit Demand";
	public static final String EDIT_OWNER_AUDIT_ACTION = "Edit Owner";

	public static final String APPCONFIG_KEY_BULKBILL_WARD = "WardNum";
	public static final Integer QUARTZ_BULKBILL_JOBS = 5;

	public static final List<String> ORDERED_DEMAND_RSNS_LIST = new ArrayList<String>() {
		{
			add(DEMANDRSN_CODE_GENERAL_TAX);
			add(DEMANDRSN_CODE_LIBRARY_CESS);
			add(DEMANDRSN_CODE_EDUCATIONAL_CESS);
			add(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY);
			add(DEMANDRSN_CODE_PENALTY_FINES);
			add(DEMANDRSN_CODE_ADVANCE);
		}
	};

	public static final String APPCONFIG_KEY_WARDSFOR_BULKBILL = "WardNum";
	public static final String APPCONFIG_KEY_WARDSFOR_TAXXMLMIGRTN = "WardNo";

	public static final HashMap<String, String> PROPERTYTYPE_STR_TO_CODE = new HashMap<String, String>() {
		{
			put(PROPTYPE_RESD_STR, PROPTYPE_RESD);
			put(PROPTYPE_NONRESD_STR, PROPTYPE_NON_RESD);
			put(PROPTYPE_MIXED_STR, PROPTYPE_MIXED);
			put(PROPTYPE_OPENPLOT_STR, PROPTYPE_OPEN_PLOT);
			put(PROPTYPE_STATEGOVT_STR, PROPTYPE_STATE_GOVT);
			put(PROPTYPE_CENTGOVT_STR, PROPTYPE_CENTRAL_GOVT);
		}
	};

	public static final Character HISTORY_TAX_DETAIL = 'Y';
	public static final Character NON_HISTORY_TAX_DETAIL = 'N';
	public static final String FORMAT_YEAR = "yyyy";

	public static final HashMap<String, String> PROPERTYTYPE_CODE_TO_STR = new HashMap<String, String>() {
		{
			put(PROPTYPE_RESD, PROPTYPE_RESD_STR);
			put(PROPTYPE_NON_RESD, PROPTYPE_NONRESD_STR);
			put(PROPTYPE_MIXED, PROPTYPE_MIXED_STR);
			put(PROPTYPE_OPEN_PLOT, PROPTYPE_OPENPLOT_STR);
			put(PROPTYPE_STATE_GOVT, PROPTYPE_STATEGOVT_STR);
			put(PROPTYPE_CENTRAL_GOVT, PROPTYPE_CENTGOVT_STR);
		}
	};

	public static final Character STATUS_MIGRATED = 'Y';
	public static final Character STATUS_NON_MIGRATED = 'N';

	public static final String MUTATION_CODE_NEW = "NEW";
	public static final String MUTATION_CODE_DATA_ENTRY = "DATA_ENTRY";

	public static final String NOTICE_GENERATED_YES = "Yes";

	public static final Character STATUS_YES_XML_MIGRATION = 'Y';
	public static final Character STATUS_NO_XML_MIGRATION = 'N';
	public static final Character STATUS_FAIL_XML_MIGRATION = 'F';

	public static final String STR_MIGRATED = "Migrated";
	public static final String STR_MIGRATED_REMARKS = "Migrated from NMC Legacy DB";

	// Sequence for unit_identifier
	public static final String UNIT_IDENTIFIER_SEQ_STR = "PT_UNIT_IDENTIFIER";

	public static final String NOTAVAIL = "N/A";
	public static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DDMMYYY);

	public static final String PATTERN_BEGINS_WITH_1TO9 = "^[1-9][0-9]*";

	public static final Character STATUS_BILL_CREATED = 'Y';
	public static final Character STATUS_BILL_NOTCREATED = 'N';
	public static final Character STATUS_BILL_CREATE_FAILED = 'F';

	public static final String STRING_EMPTY = new String();

	public static final BigDecimal ADVANCE_REBATE_PERCENTAGE = new BigDecimal(5);
	public static final Integer MAX_ADVANCES_ALLOWED = 5;
	
	public static final String MUTATIONRS_OBJECTION_CODE = "OBJ";
	public static final String MUTATIONRS_OTHERS = "OTHERS";
	public static final String MUTATIONRS_SALES_DEED = "Sale Deed";
	public static final String MUTATIONRS_COURT_ORDER = "Court Deed";
	
	public static final Map<String, String> SALUTATION = new HashMap<String, String>() {
		{
			put("-1","Select");
			put("Mr", "Mr");
			put("Ms", "Ms");
			put("Mrs", "Mrs");
		}
	};

	public static final String PROPERTY_NOT_EXIST_ERR_CODE = "PTAX100";
	public static final String PROPERTY_DEACTIVATE_ERR_CODE = "PTAX101";
	public static final String PROPERTY_MARK_DEACTIVATE_ERR_CODE = "PTAX102";
	
	public static final String PROPERTY_NOT_EXIST_ERR_MSG_PREFIX = "Property with assessment no ";
	public static final String PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX = " does not exist";
	public static final String PROPERTY_DEACTIVATE_ERR_MSG = "Property is decativated";
	public static final String PROPERTY_MARK_DEACTIVATE_ERR_MSG = "Property is marked for deactivation";
	
	public static final String MARK_DEACTIVE = "MARK_DEACTIVE";
	public static final BigDecimal PENALTY_PERCENTAGE = new BigDecimal(2);
	public static final String STATUS_CODE_ASSESSED = "ASSESSED";
	public static final BigDecimal BIGDECIMAL_100 = new BigDecimal("100");
	public static final SimpleDateFormat DATEFORMATTER_DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
}