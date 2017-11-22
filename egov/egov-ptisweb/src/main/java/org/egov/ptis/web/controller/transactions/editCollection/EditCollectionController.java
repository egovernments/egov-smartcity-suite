/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.ptis.web.controller.transactions.editCollection;

import static org.egov.ptis.client.util.PropertyTaxUtil.isZero;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.EDIT_COLL_FOR_CURRYEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_DATAENTRY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.ptis.bean.DemandDetail;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyReceipt;
import org.egov.ptis.domain.service.property.PropertyReceiptService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author subhash
 *
 */
@Controller
@RequestMapping("/editCollection")
public class EditCollectionController {

	private static final String INSTALLMENT = "installment";
	private static final String BASIC_PROPERTY = "basicProperty";
	private static final String MANDATORY_MESSAGE = "mandatory.message";
	private static final String PROPERTY_RECEIPT_REMARKS = "propertyReceipt.remarks";
	private static final String ERROR_PENALTY_NOT_COLLECTED = "error.penalty.not.collected";
	private static final String REVISED_COLLECTION_FIELD = "demandDetailBeans[%d].revisedCollection";
	private static final String EDIT_COLLECTION_FORM = "editCollection-form";
	private static final String EDIT_COLLECTION_ACK = "editCollection-ack";
	private static final String EDIT_COLLECTION_ERROR = "editCollection-error";
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private EgDemandDetailsDao demandDetailsDao;
	@Autowired
	private PropertyTaxCommonUtils propertyTaxCommonUtils;
	@Autowired
	@Qualifier("propertyReceiptService")
	private PropertyReceiptService propertyReceiptService;
	@Autowired
	private InstallmentHibDao installmentDao;
	@Autowired
	private AppConfigValueService appConfigValuesService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editForm/{assessmentNo}", method = RequestMethod.GET)
	public String form(@ModelAttribute("demandDetailBeansForm") final DemandDetailBeansForm demandDetailBeansForm,
			@PathVariable final String assessmentNo, final Model model) {
		final List<DemandDetail> demandDetailBeans = new ArrayList<DemandDetail>();
		final BasicPropertyImpl basicProperty = (BasicPropertyImpl) basicPropertyDAO
				.getBasicPropertyByPropertyID(assessmentNo);
		if (!basicProperty.isEligible())
			return EDIT_COLLECTION_ERROR;

		List<EgDemandDetails> demandDetails = null;
		Boolean isEligible = Boolean.FALSE;
		final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
				EDIT_COLL_FOR_CURRYEAR);
		if (!appConfigValue.isEmpty())
			isEligible = Boolean.valueOf(appConfigValue.get(0).getValue());
		if (isEligible) {
			final javax.persistence.Query qry = entityManager.createNamedQuery("QUERY_DEMAND_DETAILS_FOR_CURR");
			qry.setParameter(BASIC_PROPERTY, basicProperty);
			qry.setParameter(INSTALLMENT, propertyTaxCommonUtils.getCurrentInstallment());
			demandDetails = qry.getResultList();
		} else {
			final javax.persistence.Query qry = entityManager.createNamedQuery("QUERY_DEMAND_DETAILS");
			qry.setParameter(BASIC_PROPERTY, basicProperty);
			qry.setParameter(INSTALLMENT, propertyTaxCommonUtils.getCurrentInstallment());
			qry.setParameter("finYear", propertyTaxCommonUtils.getCurrentInstallment().getFinYearRange());
			demandDetails = qry.getResultList();
		}
		if (!demandDetails.isEmpty())
			Collections.sort(demandDetails, (o1, o2) -> o1.getEgDemandReason().getEgInstallmentMaster()
					.compareTo(o2.getEgDemandReason().getEgInstallmentMaster()));
		if (!demandDetails.isEmpty())
			for (final EgDemandDetails demandDetail : demandDetails) {
				final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
				final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster()
						.getReasonMaster();
				final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, demandDetail.getAmount(),
						demandDetail.getAmtCollected());
				demandDetailBeans.add(dmdDtl);
			}
		demandDetailBeansForm.setDemandDetailBeans(demandDetailBeans);
		demandDetailBeansForm.setBasicProperty(basicProperty);
		model.addAttribute("demandDetailBeans", demandDetailBeans);
		model.addAttribute("property", basicProperty.getActiveProperty());
		model.addAttribute(BASIC_PROPERTY, basicProperty);
		return EDIT_COLLECTION_FORM;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("demandDetailBeansForm") final DemandDetailBeansForm demandDetailBeansForm,
			BindingResult errors, final Model model) {
		errors = validate(demandDetailBeansForm, errors);
		final BasicPropertyImpl basicProperty = (BasicPropertyImpl) basicPropertyDAO
				.getBasicPropertyByPropertyID(demandDetailBeansForm.getBasicProperty().getUpicNo());
		if (errors.hasErrors()) {
			model.addAttribute("demandDetailBeans", demandDetailBeansForm.getDemandDetailBeans());
			model.addAttribute("property", basicProperty.getActiveProperty());
			model.addAttribute(BASIC_PROPERTY, basicProperty);
			return EDIT_COLLECTION_FORM;
		}
		final Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();
		final javax.persistence.Query qry = entityManager.createNamedQuery("QUERY_DEMAND_DETAILS_FOR_CURR");
		qry.setParameter(BASIC_PROPERTY, basicProperty);
		qry.setParameter(INSTALLMENT, propertyTaxCommonUtils.getCurrentInstallment());
		final List<EgDemandDetails> demandDetails = qry.getResultList();
		Boolean persistReceipt = Boolean.FALSE;
		for (final EgDemandDetails dmdDetails : demandDetails)
			for (final DemandDetail dmdDetailBean : demandDetailBeansForm.getDemandDetailBeans()) {
				Boolean isUpdateAmount = Boolean.FALSE;
				Boolean isUpdateCollection = Boolean.FALSE;

				if (dmdDetailBean.getRevisedAmount() != null
						&& dmdDetailBean.getInstallment()
								.equals(dmdDetails.getEgDemandReason().getEgInstallmentMaster())
						&& dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
								.equalsIgnoreCase(dmdDetailBean.getReasonMaster()))
					isUpdateAmount = true;

				if (dmdDetailBean.getRevisedCollection() != null
						&& dmdDetails.getEgDemand().getEgInstallmentMaster().equals(currentInstallment)
						&& dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
								.equalsIgnoreCase(dmdDetailBean.getReasonMaster())
						&& dmdDetails.getEgDemandReason().getEgInstallmentMaster()
								.equals(dmdDetailBean.getInstallment()))
					isUpdateCollection = true;

				if (isUpdateAmount)
					dmdDetails.setAmount(dmdDetailBean.getRevisedAmount() != null ? dmdDetailBean.getRevisedAmount()
							: BigDecimal.ZERO);
				if (isUpdateCollection)
					dmdDetails.setAmtCollected(dmdDetailBean.getRevisedCollection() != null
							? dmdDetailBean.getRevisedCollection() : BigDecimal.ZERO);
				if (isUpdateAmount || isUpdateCollection) {
					persistReceipt = Boolean.TRUE;
					dmdDetails.setModifiedDate(new Date());
					demandDetailsDao.update(dmdDetails);
					break;
				}
			}
		if (persistReceipt) {
			final PropertyReceipt propertyReceipt = demandDetailBeansForm.getPropertyReceipt();
			propertyReceipt.setSource(SOURCEOFDATA_DATAENTRY);
			propertyReceipt.setBasicProperty(basicProperty);
			propertyReceiptService.persist(propertyReceipt);
		}
		model.addAttribute("assessmentNo", basicProperty.getUpicNo());
		return EDIT_COLLECTION_ACK;
	}

	private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
			final BigDecimal amount, final BigDecimal amountCollected) {
		final DemandDetail demandDetail = new DemandDetail();
		demandDetail.setInstallment(installment);
		demandDetail.setReasonMaster(reasonMaster);
		demandDetail.setActualAmount(amount);
		demandDetail.setActualCollection(amountCollected);
		demandDetail.setIsCollectionEditable(true);
		return demandDetail;
	}

	private BindingResult validate(final DemandDetailBeansForm demandDetailBeansForm, final BindingResult errors) {
		Boolean editingCollection = Boolean.FALSE;
		int i = 0;
		BigDecimal totalRevisedCollection = BigDecimal.ZERO;
		BigDecimal totalActualCollection = BigDecimal.ZERO;
		for (final DemandDetail dd : demandDetailBeansForm.getDemandDetailBeans()) {
			dd.setInstallment(installmentDao.findById(dd.getInstallment().getId(), false));
			if (dd.getReasonMaster().equals(DEMANDRSN_STR_PENALTY_FINES)) {
				if (dd.getRevisedAmount() != null && dd.getRevisedCollection() != null) {
					if (dd.getRevisedCollection().compareTo(dd.getRevisedAmount()) > 0) {
						errors.rejectValue("demandDetailBeans[" + i + "].revisedAmount",
								"revised.collection.greater.than.reviseddemand");
						errors.rejectValue(String.format(REVISED_COLLECTION_FIELD, i),
								"revised.demand.less.than.revisedcollection");
					} else if (dd.getRevisedCollection().compareTo(dd.getRevisedAmount()) < 0)
						errors.rejectValue(String.format(REVISED_COLLECTION_FIELD, i), ERROR_PENALTY_NOT_COLLECTED);
					editingCollection = Boolean.TRUE;
					totalRevisedCollection = totalRevisedCollection.add(dd.getRevisedCollection());
					totalActualCollection = totalActualCollection.add(dd.getActualCollection());
				} else if (dd.getRevisedAmount() != null) {
					if (dd.getActualCollection().compareTo(dd.getRevisedAmount()) > 0)
						errors.rejectValue("demandDetailBeans[" + i + "].revisedAmount",
								"actual.collection.greater.than.reviseddemand");
					else if (dd.getRevisedCollection().compareTo(dd.getRevisedAmount()) < 0
							|| dd.getActualCollection().compareTo(dd.getRevisedAmount()) < 0)
						errors.rejectValue(String.format(REVISED_COLLECTION_FIELD, i), ERROR_PENALTY_NOT_COLLECTED);
					editingCollection = Boolean.TRUE;
				} else if (dd.getRevisedCollection() != null) {
					if (dd.getRevisedCollection().compareTo(dd.getActualAmount()) > 0)
						errors.rejectValue(String.format(REVISED_COLLECTION_FIELD, i),
								"revised.collection.greater.than.actualdemand");
					else if (dd.getRevisedCollection().compareTo(dd.getActualAmount()) < 0)
						errors.rejectValue(String.format(REVISED_COLLECTION_FIELD, i), ERROR_PENALTY_NOT_COLLECTED);
					editingCollection = Boolean.TRUE;
					totalRevisedCollection = totalRevisedCollection.add(dd.getRevisedCollection());
					totalActualCollection = totalActualCollection.add(dd.getActualCollection());
				}
			} else if (null != dd.getRevisedCollection() && !isZero(dd.getRevisedCollection())) {
				if (dd.getRevisedCollection().compareTo(dd.getActualAmount()) > 0)
					errors.rejectValue(String.format(REVISED_COLLECTION_FIELD, i),
							"revised.collection.greater.than.actualdemand");
				editingCollection = Boolean.TRUE;
				totalRevisedCollection = totalRevisedCollection.add(dd.getRevisedCollection());
				totalActualCollection = totalActualCollection.add(dd.getActualCollection());
			}
			i++;
		}
		if (editingCollection) {
			if (org.apache.commons.lang.StringUtils.isBlank(demandDetailBeansForm.getPropertyReceipt().getRemarks()))
				errors.rejectValue(PROPERTY_RECEIPT_REMARKS, MANDATORY_MESSAGE);
			if (org.apache.commons.lang.StringUtils
					.isBlank(demandDetailBeansForm.getPropertyReceipt().getReceiptNumber()))
				errors.rejectValue("propertyReceipt.receiptNumber", MANDATORY_MESSAGE);
			if (demandDetailBeansForm.getPropertyReceipt().getReceiptDate() == null)
				errors.rejectValue("propertyReceipt.receiptDate", MANDATORY_MESSAGE);
			if (demandDetailBeansForm.getPropertyReceipt().getReceiptAmount() == null)
				errors.rejectValue("propertyReceipt.receiptAmount", MANDATORY_MESSAGE);
			else if (totalRevisedCollection.subtract(totalActualCollection)
					.compareTo(demandDetailBeansForm.getPropertyReceipt().getReceiptAmount()) != 0)
				errors.rejectValue(PROPERTY_RECEIPT_REMARKS, "error.receipt.amount");
		} else if (!editingCollection)
			errors.rejectValue(PROPERTY_RECEIPT_REMARKS, "error.collection.notmodified");
		return errors;
	}
}
