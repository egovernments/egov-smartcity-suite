/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.actions.edit;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNull;
import static org.egov.ptis.client.util.PropertyTaxUtil.isZero;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILTUP_PROPERTY_DMDRSN_CODE_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_OF_WORK_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_DATAENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY_DMDRSN_CODE_MAP;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.bean.DemandDetail;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.DemandAudit;
import org.egov.ptis.domain.entity.property.DemandAuditDetails;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.DemandAuditService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * <p>
 * Action to edit the installment wise Taxes
 * </p>
 * Audit log format for this action, demand/collection edits
 *
 * <pre>
 * Remarks : &ltremarks&gt
 * Demand-
 * Installment1 : Tax Name | Old Tax | New Tax
 * Installment2 : Tax Name | Old Tax | New Tax
 *     ... ...
 * Installmentn : Tax Name | Old Tax | New Tax
 * Collection -
 * Installment1 : Tax Name | Old Tax | New Tax
 * Installment2 : Tax Name | Old Tax | New Tax
 *     ... ...
 * Installmentn : Tax Name | Old Tax | New Tax
 * </pre>
 *
 * @author nayeem
 */
@SuppressWarnings("serial")
@ParentPackage("egov")
@Namespace("/edit")
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = EditDemandAction.RESULT_NEW, location = "edit/editDemand-editForm.jsp"),
		@Result(name = EditDemandAction.RESULT_ERROR, location = "edit/editDemand-error.jsp"),
		@Result(name = EditDemandAction.RESULT_ACK, location = "edit/editDemand-ack.jsp") })
public class EditDemandAction extends BaseFormAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -2456087421120805855L;
	private static final Logger LOGGER = Logger.getLogger(EditDemandAction.class);
	protected static final String RESULT_NEW = "editForm";
	protected static final String RESULT_ERROR = "error";
	protected static final String RESULT_ACK = "ack";

	private static final String QUERY_DEMAND_DETAILS = "SELECT dd FROM Ptdemand ptd "
			+ "LEFT JOIN ptd.egDemandDetails dd " + "LEFT JOIN ptd.egptProperty p " + "LEFT JOIN  p.basicProperty bp "
			+ "WHERE bp = ? " + "AND bp.active = true " + "AND p.status = 'A' ";

	private static final String queryInstallmentPTDemand = "select ptd from Ptdemand ptd inner join fetch ptd.egDemandDetails dd "
			+ "inner join fetch dd.egDemandReason dr inner join fetch dr.egDemandReasonMaster drm "
			+ "inner join fetch ptd.egptProperty p inner join fetch p.basicProperty bp "
			+ "where bp.active = true and (p.status = 'A' or p.status = 'I' or p.status = 'W') "
			+ "and bp = ? and ptd.egInstallmentMaster = ? ";

	private static final String QUERY_NONZERO_DEMAND_DETAILS = QUERY_DEMAND_DETAILS + " AND dd.amount >= 0 ";

	private static final String queryInstallmentDemandDetails = QUERY_NONZERO_DEMAND_DETAILS
			+ " AND ptd.egInstallmentMaster = ? ";

	private static final String EDIT_DEMAND = "Edit Demand";
	private static final String EDIT_TYPE_POSTFIX = "-";

	private String propertyId;
	private String ownerName;
	private String mobileNumber;
	private String propertyAddress;
	private String remarks;
	private String errorMessage;

	private BasicProperty basicProperty;
	private PropertyService propService;
	private DCBDisplayInfo dcbDispInfo;

	@SuppressWarnings("rawtypes")
	@Autowired
	private InstallmentHibDao installmentDAO;
	@Autowired
	private PropertyTaxUtil propertyTaxUtil;
	@Autowired
	private PropertyTaxCommonUtils propertyTaxCommonUtils;
	@Autowired
	private DemandAuditService demandAuditService;
	@Autowired
	private RevisionPetitionService revisionPetitionService;
	@Autowired
	private EgDemandDetailsDao demandDetailsDao;
	@SuppressWarnings("rawtypes")
	@Autowired
	@Qualifier("propertyImplService")
	private PersistenceService propertyImplService;
	private final DemandAudit demandAudit = new DemandAudit();

	private List<EgDemandDetails> demandDetails = new ArrayList<EgDemandDetails>();
	private List<DemandDetail> demandDetailBeanList = new ArrayList<DemandDetail>();
	private List<Installment> allInstallments = new ArrayList<Installment>();
	private final Set<Installment> propertyInstallments = new TreeSet<Installment>();
	private Map<Installment, Map<String, Boolean>> collectionDetails = new HashMap<Installment, Map<String, Boolean>>();
	private Map<String, String> demandReasonMap = new HashMap<String, String>();

	@Override
	public Object getModel() {
		return demandDetailBeanList;
	}

	@Override
	@SkipValidation
	public void prepare() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entered into prepare");

		basicProperty = (BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
				propertyId);
		if (null != basicProperty.getActiveProperty())
			if (basicProperty.getActiveProperty().getPropertyDetail().getPropertyTypeMaster().getCode()
					.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
				demandReasonMap = VACANT_PROPERTY_DMDRSN_CODE_MAP;
			else
				demandReasonMap = BUILTUP_PROPERTY_DMDRSN_CODE_MAP;

		for (final DemandDetail dd : demandDetailBeanList)
			if (dd.getInstallment() != null && dd.getInstallment().getId() != null
					&& !dd.getInstallment().getId().equals(-1)) {
				dd.setInstallment(installmentDAO.findById(dd.getInstallment().getId(), false));
				if (!dd.getIsNew())
					propertyInstallments.add(dd.getInstallment());
			}

		final DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
		try {
			allInstallments = propertyTaxUtil
					.getInstallmentListByStartDateToCurrFinYearDesc(dateFormat.parse("01/04/1963"));
		} catch (final ParseException e) {
			throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
		}

		allInstallments.removeAll(propertyInstallments);

		addDropdownData("allInstallments", allInstallments);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting from prepare");
	}

	@Override
	public void validate() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entered into validate");

		final Set<Installment> newInstallments = new TreeSet<Installment>();
		final Set<String> installmentsChqPenalty = new TreeSet<String>();
		final Set<String> instDmdRsnMaster = new HashSet<String>();
		List<String> instString;
		final Set<String> actAmtInstallments = new TreeSet<String>();
		List<String> errorParams = null;

		for (final DemandDetail dd : demandDetailBeanList)
			if (dd.getIsNew() != null && dd.getIsNew()) {
				instString = new ArrayList<String>();
				instString.add(dd.getReasonMaster());
				if (dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_GENERAL_TAX)
						|| dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_VACANT_TAX))
					if (dd.getInstallment().getId() == null || dd.getInstallment().getId().equals(-1))
						addActionError(getText("error.editDemand.selectInstallment"));

				if (null != dd.getInstallment().getId() && !dd.getInstallment().getId().equals(-1)) {
					if (null == dd.getActualAmount())
						addActionError(getText("error.editDemand.actualAmount", instString));
					if (null != dd.getActualAmount() && null != dd.getActualCollection())
						if (dd.getActualAmount().intValue() < dd.getActualCollection().intValue())
							addActionError(getText("error.collection.greaterThan.actualAmount"));
				}

				if (dd.getActualAmount() == null) {
					if (dd.getActualCollection() != null)
						actAmtInstallments.add(dd.getInstallment().getDescription());
				} else if (dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_GENERAL_TAX)
						|| dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_VACANT_TAX))
					if (dd.getInstallment().getId() == null || dd.getInstallment().getId().equals(-1))
						addActionError(getText("error.editDemand.selectInstallment"));
					else {
						newInstallments.add(dd.getInstallment());
						final String instRsn = dd.getInstallment().toString().concat(EDIT_TYPE_POSTFIX)
								.concat(dd.getReasonMaster());
						if (instDmdRsnMaster.add(instRsn) == false) {
							instString.add(dd.getInstallment().toString());
							addActionError(getText("error.editDemand.duplicateInstallment", instString));
						}
					}
			} else {
				newInstallments.add(dd.getInstallment());

				if (null != dd.getRevisedAmount() && isZero(dd.getRevisedAmount()))
					if (dd.getActualCollection().compareTo(BigDecimal.ZERO) > 0 && isNull(dd.getRevisedCollection())) {
						errorParams = new ArrayList<String>();
						errorParams.add(dd.getReasonMaster());
						errorParams.add(dd.getInstallment().getDescription());
						addActionError(getText("error.editDemand.collectionForUpdatedDemand", errorParams));
					}
				if (null != dd.getRevisedAmount() && null != dd.getActualCollection())
					if (dd.getRevisedAmount().intValue() < dd.getActualCollection().intValue())
						addActionError(getText("error.collection.greaterThan.revisedAmount"));
				if (null != dd.getRevisedAmount() && null != dd.getRevisedCollection())
					if (dd.getRevisedAmount().intValue() < dd.getRevisedCollection().intValue())
						addActionError(getText("error.revisedCollecion.greaterThan.revisedAmount"));
			}

		if (actAmtInstallments.size() > 0) {
			final String inst = actAmtInstallments.toString().replace('[', ' ').replace(']', ' ');
			final List<String> instStrings = new ArrayList<String>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 475636030882352813L;

				{
					add(inst);
				}
			};
			addActionError(getText("error.editDemand.actualAmount", instStrings));
		}

		List<Installment> installmentsInOrder = null;
		if (!newInstallments.isEmpty()) {
			installmentsInOrder = propertyTaxUtil.getInstallmentListByStartDateToCurrFinYearDesc(
					new ArrayList<Installment>(newInstallments).get(0).getFromDate());

			if (newInstallments.size() != installmentsInOrder.size())
				addActionError(getText("error.editDemand.badInstallmentSelection"));

			final Date currDate = new Date();
			final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
			if (!DateUtils.compareDates(currDate, currYearInstMap.get(CURRENTYEAR_SECOND_HALF).getFromDate())) {
				if (newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))
						&& !newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF))
						|| !newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))
								&& newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))
					addActionError(getText("error.currentyearinstallments"));
			} else if (!newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))
				addActionError(getText("error.currentInst"));

		}

		if (installmentsChqPenalty.size() > 0) {
			final String inst = installmentsChqPenalty.toString().replace('[', ' ').replace(']', ' ');
			final List<String> instStrings = new ArrayList<String>();
			instStrings.add(inst);
			addActionError(getText("error.editDemand.chqBouncePenaltyIsZero", instStrings));
		}

		if (StringUtils.isBlank(remarks))
			addActionError(getText("mandatory.editDmdCollRemarks"));
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting from validate");
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	@Action(value = "/editDemand-newEditForm")
	public String newEditForm() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entered into newEditForm");
		String resultPage;
		final RevisionPetition generalRevisionPetition = revisionPetitionService.getExistingGRP(basicProperty);
		Date effectiveDate;
		PropertyImpl propertyModel = null;
		if (generalRevisionPetition == null) {
			if ((SOURCEOFDATA_DATAENTRY.toString().equalsIgnoreCase(basicProperty.getSource().toString())
                                && basicProperty.getPropertySet().size() == 1) || NATURE_OF_WORK_GRP.equalsIgnoreCase(basicProperty.getActiveProperty().getPropertyModifyReason()))
				propertyModel = basicProperty.getActiveProperty();

		} else if (NATURE_OF_WORK_GRP.equalsIgnoreCase(generalRevisionPetition.getType()))
			propertyModel = generalRevisionPetition.getProperty();

		if (!propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
				.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
			effectiveDate = getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail().getFloorDetails());
		else
			effectiveDate = propertyModel.getPropertyDetail().getDateOfCompletion();
			
		ownerName = basicProperty.getFullOwnerName();
		mobileNumber = basicProperty.getMobileNumber();
		propertyAddress = basicProperty.getAddress().toString();
		demandDetails = getPersistenceService().findAllBy(queryInstallmentDemandDetails, basicProperty,
				propertyTaxCommonUtils.getCurrentInstallment());
		if (!demandDetails.isEmpty())
			Collections.sort(demandDetails, (o1, o2) -> o1.getEgDemandReason().getEgInstallmentMaster()
					.compareTo(o2.getEgDemandReason().getEgInstallmentMaster()));

		final PropertyTaxBillable billable = new PropertyTaxBillable();
		billable.setBasicProperty(basicProperty);
		final Map<Installment, List<String>> newDDMap = new HashMap<>();
		String reason;
		Installment existingInst;
		if (!demandDetails.isEmpty())
			for (final EgDemandDetails dd : demandDetails) {
				List<String> existingReasons = new ArrayList<>();
				reason = dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
				existingInst = dd.getEgDemandReason().getEgInstallmentMaster();
				if (newDDMap.get(existingInst) == null) {
					existingReasons = new ArrayList<>();
					existingReasons.add(reason);
					newDDMap.put(existingInst, existingReasons);
				} else if (newDDMap.get(existingInst) != null) {
					existingReasons.add(reason);
					newDDMap.get(existingInst).addAll(existingReasons);
				} else {
					existingReasons = new ArrayList<>();
					existingReasons.add(reason);
					newDDMap.get(existingInst).addAll(existingReasons);
				}

			}

		final Map<Installment, Map<String, Map<String, Object>>> newMap = new LinkedHashMap<>();
		final Map<String, Map<String, Object>> rsnList = new LinkedHashMap<>();

		if (!demandDetails.isEmpty()) {
			for (final EgDemandDetails dd : demandDetails)
				if (newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
					final Map<String, Map<String, Object>> rsns = new LinkedHashMap<>();
					final Map<String, Object> dtls = new HashMap<>();
					dtls.put("amount", dd.getAmount());
					dtls.put("collection", dd.getAmtCollected());
					dtls.put("isNew", false);
					rsns.put(dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), dtls);
					newMap.put(dd.getEgDemandReason().getEgInstallmentMaster(), rsns);
				} else if (newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()) != null
						&& dd.getAmount().compareTo(BigDecimal.ZERO) == 0) {
					final Map<String, Object> dtls = new HashMap<>();
					dtls.put("amount", BigDecimal.ZERO);
					dtls.put("collection", BigDecimal.ZERO);
					dtls.put("isNew", false);
					newMap.get(dd.getEgDemandReason().getEgInstallmentMaster())
							.put(dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), dtls);

				} else if (newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()) != null
						&& dd.getAmount().compareTo(BigDecimal.ZERO) != 0) {
					final Map<String, Map<String, Object>> rsns = new LinkedHashMap<>();
					final Map<String, Object> dtls = new HashMap<>();
					dtls.put("amount", dd.getAmount());
					dtls.put("collection", dd.getAmtCollected());
					dtls.put("isNew", false);
					rsns.put(dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), dtls);
					newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()).putAll(rsns);
				}

			for (final String rsn : demandReasonMap.keySet())
				for (final Installment inst : newDDMap.keySet())
					if (!newDDMap.get(inst).contains(rsn))
						if (newMap.get(inst) == null) {
							final Map<String, Object> dtls = new HashMap<String, Object>();
							dtls.put("amount", BigDecimal.ZERO);
							dtls.put("collection", BigDecimal.ZERO);
							dtls.put("isNew", true);
							rsnList.put(rsn, dtls);
							newMap.put(inst, rsnList);
						} else if (newMap.get(inst) != null) {
							final Map<String, Object> dtls = new HashMap<String, Object>();
							dtls.put("amount", BigDecimal.ZERO);
							dtls.put("collection", BigDecimal.ZERO);
							dtls.put("isNew", true);
							rsnList.put(rsn, dtls);
							newMap.get(inst).put(rsn, dtls);
						}
			for (final Installment inst1 : newMap.keySet())
				for (final String rsn : newMap.get(inst1).keySet()) {
					final Map<String, Map<String, Object>> amtMap = newMap.get(inst1);
					final Map<String, Object> dtls = amtMap.get(rsn);
					final DemandDetail dmdDtl2 = createDemandDetailBean(inst1, rsn, (BigDecimal) dtls.get("amount"),
							(BigDecimal) dtls.get("collection"), (Boolean) dtls.get("isNew"), effectiveDate);
					demandDetailBeanList.add(dmdDtl2);

				}
		} else
			for (final Map.Entry<String, String> entry : demandReasonMap.entrySet()) {
				final DemandDetail dmdDtl = createDemandDetailBean(null, entry.getKey(), null, null, true, null);
				demandDetailBeanList.add(dmdDtl);
			}
		resultPage = RESULT_NEW;

		allInstallments.removeAll(propertyInstallments);
		addDropdownData("allInstallments", allInstallments);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting from newEditForm");

		return resultPage;
	}

	private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
			final BigDecimal amount, final BigDecimal amountCollected, final Boolean isNew, final Date effectiveDate) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into createDemandDetailBean");
			LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
					+ ", amount=" + amount + ", amountCollected=" + amountCollected);
		}

		final DemandDetail demandDetail = new DemandDetail();
		demandDetail.setInstallment(installment);
		demandDetail.setReasonMaster(reasonMaster);
		demandDetail.setActualAmount(amount);
		demandDetail.setActualCollection(amountCollected);
		demandDetail.setIsCollectionEditable(true);
		demandDetail.setIsNew(isNew);
		demandDetail.setReadOnly(false);
		if (effectiveDate == null)
			demandDetail.setReadOnly(false);
		else {
			final List<Installment> effectiveInstallment = propertyTaxUtil
					.getInstallmentsListByEffectiveDate(effectiveDate);
			for (final Installment inst : effectiveInstallment)
				if (inst.equals(demandDetail.getInstallment()))
					demandDetail.setReadOnly(true);
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug(
					"createDemandDetailBean - demandDetail= " + demandDetail + "\nExiting from createDemandDetailBean");
		return demandDetail;
	}

	/**
	 * To set DcbDispInfo with ReasonCategoryCodes as Tax and Penalty. Here
	 * reasonMasterCodes could also be set to DcbDispInfo.
	 *
	 * @return DCBDisplayInfo
	 */

	@SkipValidation
	private void prepareDisplayInfo() {

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entered into method prepareDisplayInfo");
		dcbDispInfo = new DCBDisplayInfo();
		dcbDispInfo.setReasonCategoryCodes(Collections.<String> emptyList());
		final List<String> reasonList = new ArrayList<String>();
		reasonList.addAll(DEMAND_REASON_ORDER_MAP.keySet());
		dcbDispInfo.setReasonMasterCodes(reasonList);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("DCB Display Info : " + dcbDispInfo);
			LOGGER.debug("Number of Demand Reasons : " + (reasonList != null ? reasonList.size() : ZERO));
			LOGGER.debug("Exit from method prepareDisplayInfo");
		}
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = RESULT_NEW)
	@Action(value = "/editDemand-update")
	public String update() {
		if (LOGGER.isDebugEnabled())
			LOGGER.info("Entered into update, basicProperty=" + basicProperty);
		final List<EgDemandDetails> demandDetailsFromDB = getPersistenceService()
				.findAllBy(QUERY_NONZERO_DEMAND_DETAILS, basicProperty);
		final Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();
		final Map<Installment, List<EgDemandDetails>> demandDetails = new TreeMap<Installment, List<EgDemandDetails>>();

		final String queryZeroDemandDetails = QUERY_DEMAND_DETAILS + " AND dd.amount = 0";

		final List<EgDemandDetails> dmdDtlsWithZeroAmt = getPersistenceService().findAllBy(queryZeroDemandDetails,
				basicProperty);
		final Set<Installment> zeroInstallments = new TreeSet<Installment>();

		BigDecimal totalDmd = BigDecimal.ZERO;
		EgDemandDetails egDemandDtls = null;

		demandAudit.setBasicproperty(basicProperty.getUpicNo());
		demandAudit.setTransaction(EDIT_DEMAND);
		demandAudit.setRemarks(remarks);
		demandAudit.setLastModifiedDate(new Date());

		for (final DemandDetail dmdDetail : demandDetailBeanList)
			if (dmdDetail.getIsNew() != null && dmdDetail.getIsNew() && dmdDetail.getActualAmount() != null) {
				final EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(
						demandReasonMap.get(dmdDetail.getReasonMaster()), dmdDetail.getInstallment());

				/**
				 * Checking whether already EgDemandDetails exists for this, if
				 * yes updating the same. this may be when taxes updated to 0
				 * and then later adding the installment taxes.
				 */

				for (final EgDemandDetails details : dmdDtlsWithZeroAmt)
					if (details.getEgDemandReason().equals(egDmdRsn)) {
						zeroInstallments.add(details.getEgDemandReason().getEgInstallmentMaster());
						details.setAmount(dmdDetail.getActualAmount());
						details.setAmtCollected(dmdDetail.getActualCollection() == null ? BigDecimal.ZERO
								: dmdDetail.getActualCollection());
						egDemandDtls = details;

					} else {

						egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
								dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
						totalDmd = totalDmd.add(egDemandDtls.getAmount());
					}

				if (dmdDtlsWithZeroAmt.isEmpty()) {
					egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
							dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
					totalDmd = totalDmd.add(egDemandDtls.getAmount());

				}
				logAudit(dmdDetail);
				final List<EgDemandDetails> dmdDtl = new ArrayList<EgDemandDetails>();
				if (demandDetails.get(dmdDetail.getInstallment()) == null) {

					dmdDtl.add(egDemandDtls);
					demandDetails.put(dmdDetail.getInstallment(), dmdDtl);
				} else
					demandDetails.get(dmdDetail.getInstallment()).add(egDemandDtls);

			}

		for (final EgDemandDetails ddFromDB : demandDetailsFromDB)
			for (final DemandDetail dmdDetail : demandDetailBeanList)
				if (dmdDetail.getIsNew() != null && !dmdDetail.getIsNew()) {
					Boolean isUpdateAmount = false;
					Boolean isUpdateCollection = false;

					if (dmdDetail.getRevisedAmount() != null
							&& dmdDetail.getInstallment().equals(ddFromDB.getEgDemandReason().getEgInstallmentMaster())
							&& ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode()
									.equalsIgnoreCase(demandReasonMap.get(dmdDetail.getReasonMaster())))
						isUpdateAmount = true;

					if (dmdDetail.getRevisedCollection() != null
							&& ddFromDB.getEgDemand().getEgInstallmentMaster().equals(currentInstallment)
							&& ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode()
									.equalsIgnoreCase(demandReasonMap.get(dmdDetail.getReasonMaster()))) {

						final Installment inst = installmentDAO.findById(dmdDetail.getInstallment().getId(), false);

						if (ddFromDB.getEgDemandReason().getEgInstallmentMaster().equals(inst))
							isUpdateCollection = true;
					}

					if (isUpdateAmount)
						ddFromDB.setAmount(
								dmdDetail.getRevisedAmount() != null ? dmdDetail.getRevisedAmount() : BigDecimal.ZERO);

					if (isUpdateCollection)
						ddFromDB.setAmtCollected(dmdDetail.getRevisedCollection() != null
								? dmdDetail.getRevisedCollection() : BigDecimal.ZERO);

					if (isUpdateAmount || isUpdateCollection) {
						ddFromDB.setModifiedDate(new Date());
						logAudit(dmdDetail);
						demandDetailsDao.update(ddFromDB);

						break;
					}
				}
		if (demandAudit.getDemandAuditDetails() != null && demandAudit.getDemandAuditDetails().size() > 0)
			demandAuditService.saveDetails(demandAudit);

		final List<EgDemandDetails> currentInstdemandDetailsFromDB = getPersistenceService().findAllBy(
				queryInstallmentDemandDetails, basicProperty, propertyTaxCommonUtils.getCurrentInstallment());

		final Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(
				currentInstdemandDetailsFromDB);
		final List<Installment> installments = new ArrayList<Installment>(demandDetailsSetByInstallment.keySet());
		Collections.sort(installments);

		for (final Installment inst : installments) {
			final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();
			for (final String rsn : DEMAND_RSNS_LIST) {
				final EgDemandDetails newDmndDtls = propService
						.getEgDemandDetailsForReason(demandDetailsSetByInstallment.get(inst), rsn);
				if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
					final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
					// If there is extraColl then add to map
					if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
						dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
								extraCollAmt);
						newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
						newDmndDtls.setModifiedDate(new Date());
						demandDetailsDao.update(newDmndDtls);
					}
				}
			}
			propService.getExcessCollAmtMap().put(inst, dmdRsnAmt);
		}

		LOGGER.info("Excess Collection - " + propService.getExcessCollAmtMap());

		final Set<EgDemandDetails> demandDetailsToBeSaved = new HashSet<EgDemandDetails>();
		for (final Map.Entry<Installment, List<EgDemandDetails>> entry : demandDetails.entrySet())
			if (!entry.getValue().get(0).getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
					.equalsIgnoreCase(DEMANDRSN_STR_CHQ_BOUNCE_PENALTY))
				demandDetailsToBeSaved.addAll(new HashSet<EgDemandDetails>(entry.getValue()));
		final List<Ptdemand> currPtdemand = getPersistenceService().findAllBy(queryInstallmentPTDemand, basicProperty,
				propertyTaxCommonUtils.getCurrentInstallment());

		if (currPtdemand != null && currPtdemand.isEmpty()) {
			final Ptdemand ptDemand = new Ptdemand();
			final PTDemandCalculations ptDmdCalc = new PTDemandCalculations();

			ptDemand.setEgInstallmentMaster(propertyTaxCommonUtils.getCurrentInstallment());
			ptDemand.setEgDemandDetails(demandDetailsToBeSaved);
			ptDemand.setBaseDemand(totalDmd);
			ptDemand.setCreateDate(new Date());
			ptDemand.setModifiedDate(new Date());
			ptDemand.setIsHistory("N");
			ptDemand.setEgptProperty((PropertyImpl) basicProperty.getProperty());
			ptDmdCalc.setPtDemand(ptDemand);
			ptDemand.setDmdCalculations(ptDmdCalc);
			getPersistenceService().applyAuditing(ptDmdCalc);
			basicProperty.getProperty().getPtDemandSet().add(ptDemand);

		} else {
			final Ptdemand ptdemand = currPtdemand.get(0);
			ptdemand.getBaseDemand().add(totalDmd);
			ptdemand.getEgDemandDetails().addAll(demandDetailsToBeSaved);
			getPersistenceService().applyAuditing(ptdemand.getDmdCalculations());
			basicProperty.getProperty().getPtDemandSet().add(ptdemand);
		}

		propertyImplService.update(basicProperty.getProperty());

		LOGGER.info("Exiting from update");
		return RESULT_ACK;
	}

	public Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetByInstallment(
			final List<EgDemandDetails> demandDtls) {
		final Map<Installment, Set<EgDemandDetails>> newEgDemandDetailsSetByInstallment = new HashMap<Installment, Set<EgDemandDetails>>();

		for (final EgDemandDetails dd : demandDtls) {

			if (dd.getAmtCollected() == null)
				dd.setAmtCollected(ZERO);

			if (newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
				final Set<EgDemandDetails> ddSet = new HashSet<EgDemandDetails>();
				ddSet.add(dd);
				newEgDemandDetailsSetByInstallment.put(dd.getEgDemandReason().getEgInstallmentMaster(), ddSet);
			} else
				newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()).add(dd);
		}
		return newEgDemandDetailsSetByInstallment;
	}

	private void logAudit(final DemandDetail dmdDetail) {

		final DemandAuditDetails dmdAdtDtls = new DemandAuditDetails();
		dmdAdtDtls.setYear(dmdDetail.getInstallment().toString());
		dmdAdtDtls.setAction(dmdDetail.getIsNew() == true ? "Add" : "Edit");
		dmdAdtDtls.setTaxType(dmdDetail.getReasonMaster());
		dmdAdtDtls.setActualDmd(dmdDetail.getActualAmount() != null ? dmdDetail.getActualAmount() : BigDecimal.ZERO);
		dmdAdtDtls
				.setModifiedDmd(dmdDetail.getRevisedAmount() != null ? dmdDetail.getRevisedAmount() : BigDecimal.ZERO);
		dmdAdtDtls.setActualColl(
				dmdDetail.getActualCollection() != null ? dmdDetail.getActualCollection() : BigDecimal.ZERO);
		dmdAdtDtls.setModifiedColl(
				dmdDetail.getRevisedCollection() != null ? dmdDetail.getRevisedCollection() : BigDecimal.ZERO);
		dmdAdtDtls.setDemandAudit(demandAudit);
		demandAudit.getDemandAuditDetails().add(dmdAdtDtls);

	}
	
	private Date getLowestDtOfCompFloorWise(final List<Floor> floorList) {
		LOGGER.debug("Entered into getLowestDtOfCompFloorWise, floorList: " + floorList);
		Date completionDate = null;
		for (final Floor floor : floorList) {
			Date floorDate;
			if (floor != null) {
				floorDate = floor.getOccupancyDate();
				if (floorDate != null)
					if (completionDate == null)
						completionDate = floorDate;
					else if (completionDate.after(floorDate))
						completionDate = floorDate;
			}
		}
		LOGGER.debug("completionDate: " + completionDate + "\nExiting from getLowestDtOfCompFloorWise");
		return completionDate;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(final String propertyId) {
		this.propertyId = propertyId;
	}

	public List<EgDemandDetails> getDemandDetails() {
		return demandDetails;
	}

	public void setDemandDetails(final List<EgDemandDetails> demandDetails) {
		this.demandDetails = demandDetails;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(final String ownerName) {
		this.ownerName = ownerName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(final String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(final String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public PropertyService getPropService() {
		return propService;
	}

	public void setPropService(final PropertyService propService) {
		this.propService = propService;
	}

	public Map<Installment, Map<String, Boolean>> getCollectionDetails() {
		return collectionDetails;
	}

	public void setCollectionDetails(final Map<Installment, Map<String, Boolean>> collectionDetails) {
		this.collectionDetails = collectionDetails;
	}

	public List<DemandDetail> getDemandDetailBeanList() {
		return demandDetailBeanList;
	}

	public void setDemandDetailBeanList(final List<DemandDetail> demandDetailBeanList) {
		this.demandDetailBeanList = demandDetailBeanList;
	}

	public List<Installment> getAllInstallments() {
		return allInstallments;
	}

	public void setAllInstallments(final List<Installment> allInstallments) {
		this.allInstallments = allInstallments;
	}

	public Map<String, String> getDemandReasonMap() {
		return demandReasonMap;
	}

	public void setDemandReasonMap(final Map<String, String> demandReasonMap) {
		this.demandReasonMap = demandReasonMap;
	}

}
