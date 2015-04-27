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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*package org.egov.bnd.web.actions.bill;

 import java.io.InputStream;
 import java.net.URLEncoder;
 import java.util.ArrayList;
 import java.util.List;

 import org.apache.struts2.convention.annotation.ParentPackage;
 import org.apache.struts2.convention.annotation.Result;
 import org.apache.struts2.convention.annotation.Results;
 import org.apache.struts2.dispatcher.StreamResult;
 import org.apache.struts2.interceptor.validation.SkipValidation;
 import org.egov.bnd.client.utils.BndRuleBook;
 import org.egov.bnd.model.BndFeeTypes;
 import org.egov.bnd.model.FeeCollection;
 import org.egov.bnd.model.FeeCollectionDetails;
 import org.egov.bnd.services.bill.BndFeeBillable;
 import org.egov.bnd.services.bill.FeeCollectionService;
 import org.egov.bnd.services.bill.FeeCollectionServiceImpl;
 import org.egov.bnd.services.common.BndCommonService;
 import org.egov.bnd.services.common.GenerateCertificateService;
 import org.egov.bnd.services.registration.BirthRegistrationService;
 import org.egov.bnd.services.registration.DeathRegistrationService;
 import org.egov.bnd.utils.BndConstants;
 import org.egov.commons.CChartOfAccounts;
 import org.egov.commons.EgwStatus;
 //import org.egov.demand.model.EgBill;
 import org.egov.infstr.client.filter.EGOVThreadLocals;
 import org.egov.infstr.utils.DateUtils;
 import org.egov.infra.admin.master.entity.User;
 import org.egov.web.actions.BaseFormAction;

 *//**
 *
 * @author Pradeep Kumar
 *
 */
/*

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results( {
	@Result(name = FeeCollectionAction.PRINTBIRTH, type = StreamResult.class, value = "BirthCertificatePDF", params = {
			"inputName", "BirthCertificatePDF", "contentType",
			"application/pdf", "contentDisposition",
			"no-cache;filename=BirthCertificate.pdf" }),
	@Result(name = FeeCollectionAction.PRINTDEATH, type = StreamResult.class, value = "DeathCertificatePDF", params = {
			"inputName", "DeathCertificatePDF", "contentType",
			"application/pdf", "contentDisposition",
			"no-cache;filename=DeathCertificate.pdf" }),
	@Result(name = FeeCollectionAction.PRINTNA, type = StreamResult.class, value = "NonAvailCertificatePDF", params = {
			"inputName", "NonAvailCertificatePDF", "contentType",
			"application/pdf", "contentDisposition",
			"no-cache;filename=NonAvailCertificate.pdf" })

})
public class FeeCollectionAction extends BaseFormAction {
private FeeCollection feeCollection = new FeeCollection();
protected String mode;
private static final String NEW = "new";
private static final String EMPTY = "";
private static final String NOPERMISSIONTOCOLLECTFEE = "noPermissionToCollectFee";

private BndFeeBillable bndFeeBillable;
private Integer reportId = -1;
private Integer printReportId;
protected BndCommonService bndCommonService;
private boolean isFreeCertificate = false;
private String collectXML;
private String regType;
private FeeCollectionServiceImpl feeCollectionServiceImpl;
private FeeCollectionService feeCollectionService;
private List<FeeCollectionDetails> feeCollnList = new ArrayList<FeeCollectionDetails>();
private List<FeeCollectionDetails> feeCollnTempList = new ArrayList<FeeCollectionDetails>();
private GenerateCertificateService generateCertificateService;
public static final String PRINTBIRTH = "printbirth";
private InputStream birthCertificatePDF;
public static final String PRINTNA = "printna";
private InputStream nonAvailCertificatePDF;
public static final String PRINTDEATH = "printdeath";
private InputStream deathCertificatePDF;
private BirthRegistrationService birthRegistrationService;
private DeathRegistrationService deathRegistrationService;

public void setBirthRegistrationService(
		BirthRegistrationService birthRegistrationService) {
	this.birthRegistrationService = birthRegistrationService;
}

public void setDeathRegistrationService(
		DeathRegistrationService deathRegistrationService) {
	this.deathRegistrationService = deathRegistrationService;
}

public void setGenerateCertificateService(
		GenerateCertificateService generateCertificateService) {
	this.generateCertificateService = generateCertificateService;
}

public void setBndCommonService(BndCommonService bndCommonService) {
	this.bndCommonService = bndCommonService;
}

private String transType;

public List<String> getTrsancationTypesList() {
	return BndConstants.TRANSACTIONTYPES;
}

public String getTransType() {
	return transType;
}

public void setTransType(String transType) {
	this.transType = transType;
}

public String getRegType() {
	return regType;
}

public void setRegType(String regType) {
	this.regType = regType;
}

public FeeCollectionAction() {
	addRelatedEntity("statusType", EgwStatus.class);
	addRelatedEntity("egBills", EgBill.class);
	addRelatedEntity("statusType", EgwStatus.class);
	addRelatedEntity("createdBy", User.class);
	addRelatedEntity("glcodeId", CChartOfAccounts.class);
	addRelatedEntity("feeType", BndFeeTypes.class);
}

@Override
public Object getModel() {
	return feeCollection;
}

@Override
public void prepare() {
	super.prepare();
	if (feeCollection.getNo_Of_copies() == null)
		feeCollection.setNo_Of_copies(1);
	feeCollection.setCollectionDate(DateUtils.today());

	if (getReportId() != null)
		feeCollection.setReportId(getReportId().longValue());

	feeCollection.setType(getRegType());

	feeCollection.setStatusType(bndCommonService.getStatusByModuleAndCode(
			BndConstants.BNDFEECOLLECTIONSTATUS,
			BndConstants.BNDFEECOLLECTIONCREATEDSTATUS));
	// feeCollection.setAddTypeMaster(bndCommonService.getAddressType(BndConstants.PERMANENTADDRESS));
	// feeCollection.setFeeTypeList(bndCommonService.getBndFeeTypes());
	if (EGOVThreadLocals.getUserId() != null)
		feeCollection.setCreatedBy(bndCommonService
				.getUserByPassingUserId(Integer.valueOf(EGOVThreadLocals
						.getUserId())));

}

@Override
public void validate() {
	boolean atLeastOneValueSelected = false;
	
 * Applicant name is mandatory field
	 
	if (feeCollection.getApplicantName() == null
			|| EMPTY.equals(feeCollection.getApplicantName().trim()))
		addActionError(getMessage("applicantName.validate"));

	if (feeCollection.getNo_Of_copies() == null)
		addActionError(getMessage("numberOfCopies.validate"));

	if (feeCollection.getNo_Of_copies() != null
			&& feeCollection.getNo_Of_copies() <= 0)
		addActionError(getMessage("messageForNoOfCopies.validate"));

	
 * If transaction type is paid, then atleast one fee should be greater
 * than zero.
	 
	if (getTransType() != null
			&& getTransType().equals(BndConstants.PAIDCERTIFICATE)) {

		for (FeeCollectionDetails feeCollDetail : feeCollnList) {
			if (feeCollDetail.getAmount() != null
					&& !"".equals(feeCollDetail.getAmount())
					&& (feeCollDetail.getAmount().floatValue() > 0.0)) {
				atLeastOneValueSelected = true;
				break;
			}
		}
		
		if (!atLeastOneValueSelected) {
			addActionError(getMessage("messageForPaidCertificate.validate"));
		}
	}

		
}

public String create() {

	bndFeeBillable.setFeeCollection(feeCollection);

	
 * Saved free certificate details in fee collection object.
	 

	if (getTransType() != null
			&& getTransType().equals(BndConstants.FREECERTIFICATE)) {
		feeCollection.setIsFreeCertificate(true);
		feeCollection.setStatusType(bndCommonService
				.getStatusByModuleAndCode(
						BndConstants.BNDFEECOLLECTIONSTATUS,
						BndConstants.BNDFEECOLLECTIONCOLLECTEDSTATUS));

	}

	for (FeeCollectionDetails feeCollDetail : feeCollnList) {
		if (feeCollDetail.getAmount() == null
				|| "".equals(feeCollDetail.getAmount())) {
			feeCollnTempList.add(feeCollDetail);
		}

		
 * In case of Paid Certificate, removed all the fee types where
 * amount is zero.
		 
		if (getTransType() != null
				&& getTransType().equals(BndConstants.PAIDCERTIFICATE)
				&& (feeCollDetail.getAmount() != null
						&& !"".equals(feeCollDetail.getAmount()) && feeCollDetail
						.getAmount().floatValue() <= 0.0)) {
			feeCollnTempList.add(feeCollDetail);
		}
	}
	if (!feeCollnTempList.isEmpty())
		feeCollnList.removeAll(feeCollnTempList);

	feeCollectionService.save(feeCollection, feeCollnList);

	if (getTransType() != null
			&& getTransType().equals(BndConstants.FREECERTIFICATE)) {
		 printReportId = generateCertificateService.generateCertificate(
		 feeCollection.getReportId(),feeCollection.getType(),
		 getRoleNameByLoginUserId(),null, getSession());
		 if (feeCollection.getType().equals(BndConstants.SEARCHBIRTH)){
			 setRegType("Birth");
			//setIdTemp(birthRegistrationService.getBirthRegistrationById(feeCollection.getReportId()).getId()); 
		 }else if (feeCollection.getType().equals(BndConstants.SEARCHDEATH)){
			 setRegType("Death");
			// setIdTemp(deathRegistrationService.getDeathRegistrationById(feeCollection.getReportId()).getId()); 
		 }
		 
		 return "report";
		ReportOutput reportOutput = generateCertificateService
				.generateCertificateBirthDeathNA(feeCollection
						.getReportId(), feeCollection.getType(),
						getRoleNameByLoginUserId(), null, getSession());
		if (feeCollection.getType().equals(BndConstants.SEARCHBIRTH)) {
			if (reportOutput != null
					&& reportOutput.getReportOutputData() != null)
				birthCertificatePDF = new ByteArrayInputStream(reportOutput
						.getReportOutputData());
			return PRINTBIRTH;
			//reportId = generateCertificateService.generateCertificate(feeCollection.getReportId(), BndConstants.SEARCHBIRTH,getRoleNameByLoginUserId(), null, getSession());
			
		} else if (feeCollection.getType().equals(BndConstants.SEARCHDEATH)) {
			if (reportOutput != null
					&& reportOutput.getReportOutputData() != null)
				deathCertificatePDF = new ByteArrayInputStream(reportOutput
						.getReportOutputData());
			return PRINTDEATH;
		} else if (feeCollection.getType().equals(
				BndConstants.SEARCHNONAVAILABILITY)) {
			if (reportOutput != null
					&& reportOutput.getReportOutputData() != null)
				nonAvailCertificatePDF = new ByteArrayInputStream(
						reportOutput.getReportOutputData());
			return PRINTNA;
		}
	} else {
		collectXML = URLEncoder.encode(feeCollectionServiceImpl
				.getBillXML(bndFeeBillable));
	}
	return "viewCollectTax";
}

public String getRoleNameByLoginUserId() {
	if (EGOVThreadLocals.getUserId() != null) {
		List<String> roleList = bndCommonService
				.getRoleNamesByPassingUserId(Integer
						.valueOf(EGOVThreadLocals.getUserId()));

		if (!roleList.isEmpty())
			return BndRuleBook.getInstance().getHighestPrivilegedRole(
					roleList);
	}
	return null;
}

@SkipValidation
public String setUp() {
	setMode("create");
	// TODO:IF NAME IS ALREADY INCLUDED,IS IT NECESSARY TO COLLECT THE FEE

	
 * Check whether fees is already collected in the previous transaction.
	 
	Boolean amountCollecteTOGeneratcertificate = feeCollectionService
			.isFeesAlreadyCollectedForReport(reportId, regType);

	String roleName = getRoleNameByLoginUserId();
	Boolean issueFreeCertificate = Boolean.FALSE;

	if (regType != null && regType.equalsIgnoreCase(BndConstants.BIRTH))
		issueFreeCertificate = birthRegistrationService
				.issueFreeCertificate(reportId.longValue(), roleName);
	else if (regType != null
			&& regType.equalsIgnoreCase(BndConstants.DEATH))
		issueFreeCertificate = deathRegistrationService
				.issueFreeCertificate(reportId.longValue(), roleName);

	
 * If hospital user already generated certificate, then there is no
 * permission for hospital user to generate second certificate.
	 
	if (roleName != null && roleName.equals(BndConstants.HOSPITALUSER)) {
		if (issueFreeCertificate)
			setTransType(BndConstants.FREECERTIFICATE);
		else {
			return NOPERMISSIONTOCOLLECTFEE;
		}

	} else if (issueFreeCertificate != null) {
		if (issueFreeCertificate && !amountCollecteTOGeneratcertificate)
			// checking free certificate issue condition and amount not
			// collected to generate certificate.
			setTransType(BndConstants.FREECERTIFICATE);
		else {
			setTransType(BndConstants.PAIDCERTIFICATE);
		}

	} else
		setTransType(BndConstants.PAIDCERTIFICATE); // Default
	// value,considered as
	// paid copy

	buildFeeCollectionDetails();
	return NEW;
}

private void buildFeeCollectionDetails() {
	List<BndFeeTypes> bndFeeType = bndCommonService
			.getBndFeeTypes(getRegType());
	for (BndFeeTypes feeType : bndFeeType) {
		FeeCollectionDetails feeCollectionDtl = new FeeCollectionDetails();
		feeCollectionDtl.setFeeType(feeType);
		feeCollnList.add(feeCollectionDtl);
	}
}

public String getMode() {
	return mode;
}

public void setMode(String mode) {
	this.mode = mode;
}

public Integer getReportId() {
	return reportId;
}

public void setReportId(Integer idTemp) {
	this.reportId = idTemp;
}

public boolean isFreeCertificate() {
	return isFreeCertificate;
}

public void setFreeCertificate(boolean isFreeCertificate) {
	this.isFreeCertificate = isFreeCertificate;
}

protected String getMessage(String text) {
	return getText(text);
}

public String getCollectXML() {
	return collectXML;
}

@SuppressWarnings("deprecation")
public void setCollectXML(String collectXML) {
	this.collectXML = java.net.URLDecoder.decode(collectXML);
}

public void setFeeCollectionServiceImpl(
		FeeCollectionServiceImpl feeCollectionServiceImpl) {
	this.feeCollectionServiceImpl = feeCollectionServiceImpl;
}

public void setFeeCollectionService(
		FeeCollectionService feeCollectionService) {
	this.feeCollectionService = feeCollectionService;
}

public List<FeeCollectionDetails> getFeeCollnList() {
	return feeCollnList;
}

public void setFeeCollnList(List<FeeCollectionDetails> feeCollnList) {
	this.feeCollnList = feeCollnList;
}

public void setBndFeeBillable(BndFeeBillable bndFeeBillable) {
	this.bndFeeBillable = bndFeeBillable;
}

public InputStream getBirthCertificatePDF() {
	return birthCertificatePDF;
}

public InputStream getNonAvailCertificatePDF() {
	return nonAvailCertificatePDF;
}

public InputStream getDeathCertificatePDF() {
	return deathCertificatePDF;
}

public void setBirthCertificatePDF(InputStream birthCertificatePDF) {
	this.birthCertificatePDF = birthCertificatePDF;
}

public void setNonAvailCertificatePDF(InputStream nonAvailCertificatePDF) {
	this.nonAvailCertificatePDF = nonAvailCertificatePDF;
}

public void setDeathCertificatePDF(InputStream deathCertificatePDF) {
	this.deathCertificatePDF = deathCertificatePDF;
}


public Integer getPrintReportId() {
	return printReportId;
}

}
 */
