package org.egov.tender.utils;



public interface TenderConstants {
	public static final String STATUSQUERY = "from EgwStatus where moduletype=? and code=?";
	//Parameters required to search for tenderfile
	
	public static final String FROMDATE			= "fromdate";
	public static final String TODATE			= "todate";
	public static final String DEPARTMENT		= "departmentid";
	public static final String TENDERFILETYPE	= "tenderfiletype";
	public static final String TENDERFILENUMBER = "tenderfilenumber";
	public static final String STATUS			= "status";
	public static final String GROUPNUMBER		= "groupNumber";
	public static final String RESPONSENUMBER	= "responseNumber";
	public static final String CREATEDBY	    = "createdBy";
	public static final String SEARCHMODE	    = "searchMode";
	public static final String OBJECT_STATUS	= "approvedstatus";
	
	
	public static final String CREATED			  = "Created";
	public static final String ACCEPTED			  = "Accepted";
	public static final String CANCELLED          = "Cancelled";
	public static final String APPROVED           = "Approved";
	//parameters required to search Tender Notice
	public static final String TENDERNOTICENUMBER = "tendernoticenumber";
	
		
	public static final String DATEPATTERN      = "dd/MM/yyyy";
	public static final String TENDERNOTICE		= "TENDERNOTICE";
	public static final String TENDERUNIT		= "TENDERUNIT";
	public static final String TENDERUNITINCREATEDSTATE 	= CREATED;
	public static final String TENDERUNIT_ACCEPTED 			= ACCEPTED;
	public static final String TENDERNOTICE_ACCEPTED 		= ACCEPTED;
	public static final String TENDERUNIT_CANCELLED 		= CANCELLED;
	public static final String TENDERNOTICE_CANCELLED 	    = CANCELLED;
	public static final String TENDERNOTICE_APPROVED		= APPROVED;
	public static final String TENDERNOTICEINCREATEDSTATE 	= CREATED;
	public static final String SCRIPT_SAVE = "save";
	
	public static final String WFSTATUS_NEXT 		          = "nextState";
	public static final String SCRIPT_SAVE_AND_SUBMIT         = "save_and_submit";
	public static final String ACTION_SAVE_AND_SUBMIT         = "savesubmit";
	public static final String ACTION_SAVE				      = "save";
	public static final String WF_CANCELED_STATE              = "CANCELLED";
	public static final String TENDERNEXTSTATUSSCRIPT  		  = "tender.wf.nextstatus";
	public static final String WF_NEW_STATE  		          = "NEW";
	public static final String WF_END_STATE 		          = "END";
	public static final String WF_CREATED_STATE 	          = "CREATED";
	public static final String WF_VERIFIED_STATE 	  		  = "VERIFIED";
	public static final String WF_APPROVE_STATE       		  = "APPROVED";
	
	public static final String TENDER = "TENDER";
	public static final String FLAGTOGENERATENOTICENUMBER     = "AUTOGENERATE_NOTICENUMBER";
	public static final String TENDERNUMBERGENERATORSCRIPT    = "tender.noticenumber.generator";
	public static final String TENDERNOTICENUMBERPREFIX       = "TNN";
	public static final String WFSTATUS_NEXT_TO_NEXT		  = "nextToNextState";
	public static final String ACTION_APPROVE = "approve";
	
	
	//For Tender Response
	public static final String TENDERRESPONSEMODULE           = "GENERICTENDERRESPONSE";
	public static final String TENDERRESPONSE_CREATED          =  CREATED;
	public static final String FLAG_AUTO_TENDERRESPONSENUMBER = "AUTOGENERATE_RESPONSENUMBER";
	public static final String TENDERRESPONSEENUMBERPREFIX    = "BSN";
	public static final String TENDERRESPONSEGENERATORSCRIPT  = "tender.responsenumber.generator";
	public static final String TENDER_WORKS_ESTIMATE		  = "estimate";
	public static final String TENDER_NOTICETYPE_PERCENTAGE	  = "Percentage";
	public static final String TENDER_NOTICETYPE_RATE	  	  = "Rate";
	public static final String TENDER_RESPONSE_TENDERTYPE  	  = "tender.response.tendertype.biddertype";
	public static final String TENDER_TENDERTYPE  	 		  = "TENDERTYPE";
	public static final String TENDER_WORKS 	 		  	  = "works";
	public static final String TENDER_STORES 	 		  	  = "stores";
	public static final String TENDER_LANDESTATE_SHOP	  	  = "lne";
	public static final String TENDER_CONTRACTOR	  	  	  = "contractor";
	public static final String TENDER_SUPPLIER	  	  	  	  = "supplier";
	public static final String TENDER_OWNER	  	  	  	      = "owner";
	public static final String TENDER_BIDDERTYPE	  	  	  = "BIDDERTYPE";
	public static final String TENDERNOTICE_WFDESCRIPTION	  = "Tender Notice Number :";
	public static final String TENDER_LEFILETYPE 			  = "Shop_tender";
	public static final String TENDERRESPONSE_ACCEPTED 	      = ACCEPTED;
	public static final String TENDERRESPONSE_APPROVED 	      = APPROVED;
	public static final String TENDERRESPONSE_NEGOTIATED	  = "Negotiated";
	public static final String TENDERRESPONSE_RENEGOTIATED	  = "Re-Negotiate";
	public static final String TENDERRESPONSE_REJECTED	      = "Rejected";
	public static final String TENDERRESPONSE_CANCELLED       = "Cancelled";
	public static final String TENDERRESPONSE_JUSTIFIED	      = "Justification";
	public static final String JUSTIFICATIONNOTICE            = "JUSTIFICATIONNOTICE";
	public static final String TENDERCOMMONSERVICE            = "tenderCommonService";
	public static final String TENDERRESPONSENUMBER           = "Generic Tender Response :";
	public static final String COMPARISIONREPORT_TEMPLATE     = "comparisionReport";
	
	
	
	//Named Queries
	public static final String GETALLRESPONSESBYUNITTOCANCELL =  "GETALLRESPONSESBYUNITTOCANCELL";
	public static final String GETALLSTATUSINTENDER           =  "GETALLSTATUSINTENDER";
	public static final String GETJUSTIFICATIONBYRESPONSE     =  "GETJUSTIFICATIONBYRESPONSE";
	
	public static final String FILETYPERATECONTRACT_INDENT="RATECONTRACT_INDENT";
	public static final String FILETYPEITEM_INDENT="ITEM_INDENT";
	public static final String FILETYPEWORKS_RC_INDENT="WORKS_RC_INDENT";
	public static final String 	FILETYPEESTIMATE="ESTIMATE";
	public static final String FILETYPELABELINDENTNUMBER= "Indent Number" ;
	public static final String FILETYPELABELESTIMATE="Estimate";
	public static final String FILETYPELABELGROUPNUMBER="Group Number";
	//REPORT
	public static final String DEFAULTLOGONAME   =  "india.png";
	public static final String NEGOTIATIONREPORT =  "tenderNegotiation-report";
	public static final String NEGOTIATIONNOTICE =  "NEGOTIATIONNOTICE";
	public static final String JUSTIFICATIONREPORT = "tenderJustification-report";
	// Tender Notice print filenames
	public static final String TENDERNOTICEFILENAMEFORSTORESMODULE="tenderNoticeForStores";
	public static final String TENDERNOTICEFILENAMEFORWORKSSMODULE="tenderNoticeForWorks";
	public static final String  TENDERNOTICEFILENAMEFORLESMODULE="tenderNoticeForLE";
	
	public static final String TENDERRESPONSE_STATUSSCRIPT =  "tender.response.status";
	
	public static final String TENDERFILEGROUPTYPE="tenderfilegrouptype";
	public static final String TYPE = "type";
	public static final String NUMBER = "number";
	public static final String WORKSINDENT="WorksIndent";

	public static final String TENDERFILETYPENUMBER = "filetypenumber";
	
}
