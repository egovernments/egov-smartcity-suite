package org.egov.dcb.service;

import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBReport;
import org.egov.demand.interfaces.Billable;

public interface DCBService {

    /**
     * Method is used to View the Citizen DCB(i.e DEMAND ,COLELCTION and
     * BALANCE).This DCB is generated based on the Current Installment. If the
     * grouping of the two or more account heads amounts is required,then in
     * DCBDisplayInfo Bean all the EgReasonCategory Master Codes needs to be
     * set. All The EgDemandReason Master codes  which are all used by the  module needs to be 
     * set as a list in DCBDisplayInfo Bean in the order in which
     * DCB should be shown (except those which are not mentioned in grouping. )
     * 
     *@param dcbDispInfo - All those Reason Masters which are required to be grouped into
     *                     a single amount(to be shown to client) needs to be set.
     *                     For Ex:- In PTIS there are four Reason Masters which are to be
     *                     grouped and to be shown to citizen.  
     * 
     *@return org.egov.dcb.bean.DCBReport
     */
    public DCBReport getCurrentDCBAndReceipts(DCBDisplayInfo dcbDispInfo);

    /**
     * Retrieves DCB information without the receipts.
     * 
     * @param dcbDispInfo
     * @return
     */
    public DCBReport getCurrentDCBOnly(DCBDisplayInfo dcbDispInfo);

    /**
     * Retrieves receipt information without the DCB information. Note that the returned Receipt 
     * objects will have their list of ReceiptDetail objects populated, but NOT the list of 
     * Payment objects. If clients want to see payment info like cheque number, payee name etc., 
     * they have to fetch it themselves via the Collections API.
     * 
     * @return
     */
    public DCBReport getReceiptsOnly();

    /**
     * Should be called by the billing system, to set the appropriate Billable object for which
     * the DCB is being requested.
     * 
     * @param billable
     */
    public void setBillable(Billable billable);
    
}
