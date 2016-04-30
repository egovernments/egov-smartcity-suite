package org.egov.bpa.services.extd.common;

import org.apache.log4j.Logger;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.BpaFeeDetailExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.egov.bpa.constants.BpaConstants.BPAMODULENAME;
import static org.egov.bpa.constants.BpaConstants.CATEGORY_FEE;

//import org.egov.commons.dao.CommonsDaoFactory;

@Transactional(readOnly = true)
public class BpaDmdCollExtnService {

	@PersistenceContext
	private EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	private FeeExtnService feeExtnService;
	private static final Logger LOGGER = Logger
			.getLogger(BpaDmdCollExtnService.class);

	protected PersistenceService<EgDemandReasonMaster, Long> egDemandReasonMasterService;
	protected PersistenceService<EgDemandReason, Long> demandReasonService;
	protected PersistenceService<EgDemand, Long> egDemandService;
	// private PersistenceService persistenceService;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleService moduleDao;
	@Autowired
	private InstallmentDao installmentDao;

	/**
	 * @param serviceTypeId
	 * @param areasqmt
	 * @param feeType
	 * @return EgDemand
	 */
	@Transactional
	public EgDemand createDemand(Long serviceTypeId, BigDecimal areasqmt,
			String feeType) {
		EgDemand demand = null;
		Set<EgDemandDetails> demandDetailSet = new HashSet<EgDemandDetails>();
		BigDecimal totaldmdAmt = BigDecimal.ZERO;
		Installment installment = getCurrentInstallment();
		// PersistenceService persistenceService =
		// feeService.getPersistenceService();
		Boolean isAmountExist = false;

		if (installment == null) {
			throw new EGOVRuntimeException(
					"Installment is null in CreateDemand api.");
		}
		try {

			List<BpaFeeDetailExtn> bpafeeDet = feeExtnService
					.getFeeDetailByServiceTypeandArea(serviceTypeId, areasqmt,
							feeType);

			if (bpafeeDet != null && !bpafeeDet.isEmpty()) {

				for (BpaFeeDetailExtn feeDet : bpafeeDet) {
					if (!feeDet.getAmount().equals(BigDecimal.ZERO)) {
						isAmountExist = true;
						EgDemandDetails dmdDet = createDemandDetail(
								feeDet.getBpafee(), feeDet.getAmount());
						totaldmdAmt = totaldmdAmt.add(feeDet.getAmount());

						if (dmdDet != null)
							demandDetailSet.add(dmdDet);
					}
				}
				// generate demand only when amount exists
				if (isAmountExist) {
					demand = buildDemandObject(demandDetailSet, totaldmdAmt,
							installment);
					// persistenceService.setType(EgDemand.class);
					if (getEgDemandService() != null)
						getEgDemandService().persist(demand);
				}
			} else {
				LOGGER.info("ERROR:In create Demand !!!!!!!!!!  Fee details are not available for the selected service type,fee type and area");
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception in createDemand method "
					+ e);
		}

		return demand;
	}

	protected EgDemand buildDemandObject(Set<EgDemandDetails> demandDetailSet,
			BigDecimal totaldmdAmt, Installment installment) {
		EgDemand dmd = new EgDemand();
		dmd.setEgDemandDetails(demandDetailSet);
		dmd.setAmtCollected(BigDecimal.ZERO);
		dmd.setAmtRebate(BigDecimal.ZERO);
		dmd.setBaseDemand(totaldmdAmt);
		dmd.setIsHistory("N");
		dmd.setModifiedDate(new Date());
		dmd.setCreateDate(new Date());
		dmd.setEgInstallmentMaster(installment);

		return dmd;
	}

	protected EgDemandDetails createDemandDetail(BpaFeeExtn feeDet,
			BigDecimal amount) {
		EgDemandDetails dmdDet = new EgDemandDetails();
		dmdDet.setAmount(amount);
		dmdDet.setAmtCollected(BigDecimal.ZERO);
		dmdDet.setAmtRebate(BigDecimal.ZERO);
		dmdDet.setEgDemandReason(getEgDemandReason(feeDet));
		dmdDet.setCreateDate(new Date());
		dmdDet.setModifiedDate(new Date());
		return dmdDet;
	}

	/**
	 * @param serviceTypeId
	 * @param areasqmt
	 * @param feeType
	 * @param demand
	 *            (existing demand)
	 * @return EgDemand This api used only first time when admission fee
	 *         collected.
	 */
	@Transactional
	public EgDemand updateDemand(Long serviceTypeId, BigDecimal areasqmt,
			String feeType, EgDemand demand) {
		// Set<EgDemandDetails> demandDetailSet = new
		// HashSet<EgDemandDetails>();
		// Set<EgDemandDetails> removeDmdDetSet = new
		// HashSet<EgDemandDetails>();
		BigDecimal totaldmdAmt = BigDecimal.ZERO;
		// PersistenceService persistenceService =
		// feeService.getPersistenceService();
		// Boolean isAmountExist = false;
		Boolean updateDemandFlag = false, createNewDemandDetailFlag = true;
		DemandGenericDao demandGenericDao = new DemandGenericHibDao();

		List<BpaFeeDetailExtn> bpafeeDet = feeExtnService
				.getFeeDetailByServiceTypeandArea(serviceTypeId, areasqmt,
						feeType);

		if (bpafeeDet != null && !bpafeeDet.isEmpty() && bpafeeDet.size() != 0) {

			try {
				List<EgDemandReason> dmdRsnList = new ArrayList<EgDemandReason>();

				for (BpaFeeDetailExtn feeDet : bpafeeDet) {
					createNewDemandDetailFlag = true;

					dmdRsnList = new ArrayList<EgDemandReason>();
					EgDemandReason dmdRsn = getEgDemandReason(feeDet
							.getBpafee());
					if (dmdRsn != null)
						dmdRsnList.add(dmdRsn);
					// Checking whether demand reason already present in
					// existing demand ? Id yes then update demand details.
					List<EgDemandDetails> dmdDetList = demandGenericDao
							.getDemandDetailsForDemandAndReasons(demand,
									dmdRsnList);

					if (dmdDetList != null && !dmdDetList.isEmpty()
							&& dmdDetList.size() != 0) {

						for (EgDemandDetails dmdDtl : dmdDetList) {

							if (!feeDet.getAmount().equals(BigDecimal.ZERO)
									&& !(dmdDtl.getAmount().compareTo(
											feeDet.getAmount()) == 0)) {
								dmdDtl.getEgDemand().setBaseDemand(
										dmdDtl.getEgDemand().getBaseDemand()
												.add(feeDet.getAmount())
												.subtract(dmdDtl.getAmount()));
								dmdDtl.setAmount(feeDet.getAmount());
								demand.setModifiedDate(new Date());
								updateDemandFlag = true;
								// totaldmdAmt =
								// totaldmdAmt.add(feeDet.getAmount());
								// persistenceService.setType(EgDemandDetails.class);
								// persistenceService.update(dmdDet);

							}
						}
					} else {
						if (!feeDet.getAmount().equals(BigDecimal.ZERO)) {

							for (EgDemandDetails dmdDtl : demand
									.getEgDemandDetails()) {
								// Check any demand details contain admission
								// fee type ? if yes... update the fee..Else
								// create new demand detail
								if (dmdRsn != null
										&& dmdRsn.getEgDemandReasonMaster()
												.getReasonMaster() != null
										&& feeDet.getBpafee() != null
										&& dmdRsn.getEgDemandReasonMaster()
												.getReasonMaster() != null
										&& feeDet.getBpafee()
												.getFeeDescription() != null
										&& dmdRsn
												.getEgDemandReasonMaster()
												.getReasonMaster()
												.trim()
												.equalsIgnoreCase(
														feeDet.getBpafee()
																.getFeeDescription()
																.trim())) {
									// using the same fee detail for admission
									// fee. We are not deleting if user changed
									// service type.
									createNewDemandDetailFlag = false;
									updateDemandFlag = true;
									dmdDtl.getEgDemand()
											.setBaseDemand(
													dmdDtl.getEgDemand()
															.getBaseDemand()
															.add(feeDet
																	.getAmount())
															.subtract(
																	dmdDtl.getAmount()));
									dmdDtl.setAmount(feeDet.getAmount());
									demand.setModifiedDate(new Date());

								}
							}

							if (createNewDemandDetailFlag) {

								EgDemandDetails dmdDet = createDemandDetail(
										feeDet.getBpafee(), feeDet.getAmount());
								totaldmdAmt = totaldmdAmt.add(feeDet
										.getAmount());
								demand.setBaseDemand(demand.getBaseDemand()
										.add(feeDet.getAmount()));

								if (dmdDet != null) {
									demand.addEgDemandDetails(dmdDet);
								}

								demand.setModifiedDate(new Date());
								updateDemandFlag = true;
							}
						}
					}
				}

				if (updateDemandFlag)
					egDemandService.update(demand);

				// Mean demand detail already present. Need to update.
			} catch (Exception e) {
				throw new EGOVRuntimeException(
						"Exception in createDemand method " + e);
			}

			/*
			 * try { for (BpaFeeDetail feeDet : bpafeeDet) {
			 * 
			 * List<EgDemandReason> dmdRsnList = new
			 * ArrayList<EgDemandReason>(); EgDemandReason dmdRsn =
			 * getEgDemandReason(feeDet.getBpafee()); dmdRsnList.add(dmdRsn);
			 * List<EgDemandDetails> dmdDetList = demandGenericDao
			 * .getDemandDetailsForDemandAndReasons(demand, dmdRsnList); check
			 * if EgDemandDetails exist for this EgDemandReason (ie.
			 * BpaFeeDetail), then update existing EgDemandDetails else create
			 * new EgDemandDetails and add to existing demand if (dmdDetList !=
			 * null && !dmdDetList.isEmpty() && dmdDetList.size() != 0) {
			 * EgDemandDetails dmdDet = dmdDetList.get(0);
			 * 
			 * if the BpaFeeDetail amount is ZERO and the EgDemandDetails
			 * exists, then remove the specific EgDemandDetails EgDemand. Else
			 * update the existing EgDemandDetails if
			 * (feeDet.getAmount().equals(BigDecimal.ZERO)) {
			 * removeDmdDetSet.add(dmdDet); } else {
			 * dmdDet.setAmount(feeDet.getAmount());
			 * 
			 * persistenceService.setType(EgDemandDetails.class);
			 * persistenceService.update(dmdDet); totaldmdAmt =
			 * totaldmdAmt.add(feeDet.getAmount()); } } else { if
			 * (!feeDet.getAmount().equals(BigDecimal.ZERO)) { EgDemandDetails
			 * dmdDet = createDemandDetail(feeDet);
			 * 
			 * totaldmdAmt = totaldmdAmt.add(feeDet.getAmount());
			 * 
			 * demand.addEgDemandDetails(dmdDet); } } } //generate demand only
			 * when amount exists if (isAmountExist) {
			 * demand.setBaseDemand(totaldmdAmt); demand.setModifiedDate(new
			 * Date());
			 * 
			 * persistenceService.setType(EgDemand.class);
			 * persistenceService.update(demand); } } catch(Exception e) { throw
			 * new EGOVRuntimeException("Exception in createDemand method "+ e);
			 * }
			 */} else {
			LOGGER.info("ERROR: in Update Demand !!!!!!!!!!  Fee details are not available for the selected service type,fee type and area");
		}

		return demand;
	}

	/**
	 * @return Installment..added ModuleService changes
	 */
	public Installment getCurrentInstallment() {
		Installment installment = null;
		Module module = moduleDao.getModuleByName(BPAMODULENAME);
		installment = installmentDao.getInsatllmentByModuleForGivenDate(module,
				new Date());

		return installment;
	}

	/**
	 * @param bpaFee
	 * @return EgDemandReason(If either EgDemandReasonMaster or EgDemandReason
	 *         does not exist, its generated dynamically and then returned)
	 */
	public EgDemandReason getEgDemandReason(BpaFeeExtn bpaFee) {
		ModuleService moduleDao = null;
		// GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = null;
		// moduleDao.getModuleByName(BPAMODULENAME);
		EgDemandReason egDemandReason = null;
		EgDemandReasonMaster egDemandReasonMaster = null;
		DemandGenericDao demandGenericDao = new DemandGenericHibDao();
		// PersistenceService persistenceService =
		// feeService.getPersistenceService();

		try {
			egDemandReasonMaster = (EgDemandReasonMaster) demandGenericDao
					.getDemandReasonMasterByCode(bpaFee.getFeeCode(), module);

			if (egDemandReasonMaster == null) {
				egDemandReasonMaster = createEgDemandReasonMaster(bpaFee);
			}

			egDemandReason = (EgDemandReason) demandGenericDao
					.getDmdReasonByDmdReasonMsterInstallAndMod(
							egDemandReasonMaster, getCurrentInstallment(),
							module);

			if (egDemandReason == null) {
				egDemandReason = createEgDemandReason(bpaFee,
						egDemandReasonMaster);
			}

		} catch (Exception ex) {
			throw new EGOVRuntimeException(
					"Exception in getEgDemandReason method." + ex);
		}

		return egDemandReason;
	}

	/**
	 * @param bpaFee
	 * @return EgDemandReasonMaster
	 */
	public EgDemandReasonMaster createEgDemandReasonMaster(BpaFeeExtn bpaFee) {
		EgDemandReasonMaster egDmdRsnMstr = null;
		ModuleService moduleDao = null;
		// GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = null;
		// moduleDao.getModuleByName(BPAMODULENAME);
		DemandGenericDao demandGenericDao = new DemandGenericHibDao();
		// PersistenceService persistenceService =
		// feeService.getPersistenceService();

		egDmdRsnMstr = (EgDemandReasonMaster) demandGenericDao
				.getDemandReasonMasterByCode(bpaFee.getFeeCode(), module);
		if (egDmdRsnMstr == null) {
			egDmdRsnMstr = new EgDemandReasonMaster();
			EgReasonCategory egRsnCategory = demandGenericDao
					.getReasonCategoryByCode(CATEGORY_FEE);

			// egDmdRsnMstr.setReasonMaster(bpaFee.getFeeType());
			egDmdRsnMstr.setReasonMaster(bpaFee.getFeeDescription());
			egDmdRsnMstr.setCode(bpaFee.getFeeCode());
			egDmdRsnMstr.setEgModule(module);
			egDmdRsnMstr.setOrderId(Long.valueOf("1"));
			egDmdRsnMstr.setEgReasonCategory(egRsnCategory);
			egDmdRsnMstr.setIsDebit("N");
			egDmdRsnMstr.setCreatedDate(new Date());
			egDmdRsnMstr.setModifiedDate(new Date());
			egDemandReasonMasterService.persist(egDmdRsnMstr);

			// Create defaultly demand reason along with demand master
			createEgDemandReason(bpaFee, egDmdRsnMstr);

			// egDmdRsn.setEgDemandReasonMaster(egDmdRsnMstr);
			// egDmdRsnMstr.getEgDemandReasons().add(egDmdRsn);

			/*
			 * persistenceService.setType(EgDemandReasonMaster.class);
			 * persistenceService.create(egDmdRsnMstr);
			 * 
			 * persistenceService.setType(EgDemandReason.class);
			 * persistenceService.create(egDmdRsn);
			 */

		}
		/*
		 * if (egDmdRsnMstr!=null &&
		 * (egDmdRsnMstr.getEgDemandReasons().isEmpty() ||
		 * egDmdRsnMstr.getEgDemandReasons().size()==0)) {
		 * 
		 * createEgDemandReason(bpaFee,egDmdRsnMstr); EgDemandReason egDmdRsn =
		 * createEgDemandReason(bpaFee);
		 * egDmdRsn.setEgDemandReasonMaster(egDmdRsnMstr);
		 * egDmdRsnMstr.getEgDemandReasons().add(egDmdRsn);
		 * persistenceService.setType(EgDemandReasonMaster.class);
		 * persistenceService.update(egDmdRsnMstr);
		 * 
		 * persistenceService.setType(EgDemandReason.class);
		 * persistenceService.create(egDmdRsn); }
		 */
		return egDmdRsnMstr;
	}

	/**
	 * @param bpaFee
	 * @param egDemandReasonMaster
	 * @return EgDemandReason
	 */
	public EgDemandReason createEgDemandReason(BpaFeeExtn bpaFee,
			EgDemandReasonMaster egDemandReasonMaster) {
		EgDemandReason egDmdRsn = new EgDemandReason();
		// PersistenceService persistenceService =
		// feeService.getPersistenceService();
		egDmdRsn.setEgDemandReasonMaster(egDemandReasonMaster);
		egDmdRsn.setEgInstallmentMaster(getCurrentInstallment());
		egDmdRsn.setGlcodeId(bpaFee.getGlcode());
		egDmdRsn.setCreateDate(new Date());
		egDmdRsn.setModifiedDate(new Date());
		demandReasonService.persist(egDmdRsn);
		return egDmdRsn;
	}

	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}

	public void setFeeExtnService(FeeExtnService feeService) {
		this.feeExtnService = feeService;
	}

	public PersistenceService<EgDemandReasonMaster, Long> getEgDemandReasonMasterService() {
		return egDemandReasonMasterService;
	}

	public void setEgDemandReasonMasterService(
			PersistenceService<EgDemandReasonMaster, Long> egDemandReasonMasterService) {
		this.egDemandReasonMasterService = egDemandReasonMasterService;
	}

	public PersistenceService<EgDemandReason, Long> getDemandReasonService() {
		return demandReasonService;
	}

	public void setDemandReasonService(
			PersistenceService<EgDemandReason, Long> demandReasonService) {
		this.demandReasonService = demandReasonService;
	}

	public PersistenceService<EgDemand, Long> getEgDemandService() {
		return egDemandService;
	}

	public void setEgDemandService(
			PersistenceService<EgDemand, Long> egDemandService) {
		this.egDemandService = egDemandService;
	}

	@Transactional
	public RegistrationExtn generateDemandUsingSanctionFeeList(
			List<BpaFeeExtn> santionFeeList, RegistrationExtn registrationObj) {

		Installment installment = getCurrentInstallment();
		List<Long> delDmdDetailList = new ArrayList<Long>();
		if (installment == null) {
			throw new EGOVRuntimeException("Installment is null");
		}

		EgDemand dmd = registrationObj.getEgDemand() != null ? registrationObj
				.getEgDemand() : buildDemandObject(
				new HashSet<EgDemandDetails>(), BigDecimal.ZERO, installment);

		BigDecimal totaldmdAmt = BigDecimal.ZERO;

		for (BpaFeeExtn bpaFee : santionFeeList) {

			// if(bpaFee.getFeeGroup()!=null &&
			// bpaFee.getFeeGroup().equals(BpaConstants.COCFEE)){
			if (bpaFee.getFeeAmount() != null
					&& !bpaFee.getFeeAmount().equals(BigDecimal.ZERO)) {

				// DemandDetailid null mean, its a new fee details entered from
				// UI.
				if (bpaFee.getDemandDetailId() == null) {
					EgDemandDetails dmdDet = createDemandDetail(bpaFee,
							bpaFee.getFeeAmount());
					if (dmdDet != null) {
						dmdDet.setEgDemand(dmd);
						dmd.getEgDemandDetails().add(dmdDet);
					}
				} else {

					for (EgDemandDetails dmdDtl : dmd.getEgDemandDetails()) {
						if (dmdDtl.getId() != null
								&& dmdDtl.getId().equals(
										bpaFee.getDemandDetailId())) {
							dmdDtl.setAmount(bpaFee.getFeeAmount());
							break;
						}
					}
				}

			} else {
				if (bpaFee.getDemandDetailId() != null
						&& !bpaFee.getDemandDetailId().equals("")) {
					delDmdDetailList.add(bpaFee.getDemandDetailId()); // Delete
																		// from
																		// the
																		// existing
																		// list.
				}
			}
			// }
		}

		// Delete from the existing list.
		for (Long dmdDtlId : delDmdDetailList) {
			for (EgDemandDetails dmdDtl : dmd.getEgDemandDetails()) {
				if (dmdDtl.getId() != null && dmdDtlId != null
						&& dmdDtl.getId().equals(dmdDtlId)) {
					dmd.getEgDemandDetails().remove(dmdDtl);
					break;
				}
			}
		}

		for (EgDemandDetails det : dmd.getEgDemandDetails()) {
			totaldmdAmt = totaldmdAmt.add(det.getAmount());
		}

		dmd.setBaseDemand(totaldmdAmt);

		if (registrationObj.getEgDemand() == null) {
			registrationObj.setEgDemand(dmd);

		}
		entityManager.flush();
		getEgDemandService().persist(dmd);
		// persistenceService.getSession().flush();
		LOGGER.debug("Exit save ");

		return registrationObj;
	}

	public RegistrationExtn updateDemandByUsingSanctionFeeList(
			List<BpaFeeExtn> santionFeeList, RegistrationExtn registrationObj) {

		Installment installment = getCurrentInstallment();
		List<EgDemandReason> delDmdDetailList = new ArrayList<EgDemandReason>();
		if (installment == null) {
			throw new EGOVRuntimeException("Installment is null");
		}

		EgDemand dmd = registrationObj.getEgDemand() != null ? registrationObj
				.getEgDemand() : buildDemandObject(
				new HashSet<EgDemandDetails>(), BigDecimal.ZERO, installment);

		BigDecimal totaldmdAmt = BigDecimal.ZERO;
		for (BpaFeeExtn bpaFee : santionFeeList) {

			Boolean demandReasonAlreadyPresent = Boolean.FALSE;
			EgDemandReason dmdReason = getEgDemandReason(bpaFee);

			// if for demand reason , if amount present, then we are
			// updating/adding the same in demand details.
			if (bpaFee.getFeeAmount() != null
					&& bpaFee.getFeeAmount().compareTo(BigDecimal.ZERO) > 0) {

				for (EgDemandDetails dmdDtl : dmd.getEgDemandDetails()) {
					if (dmdReason != null
							&& dmdDtl.getEgDemandReason() != null
							&& dmdDtl.getEgDemandReason().getId()
									.equals(dmdReason.getId())) {
						dmdDtl.setAmount(bpaFee.getFeeAmount());
						demandReasonAlreadyPresent = Boolean.TRUE;
						break;
					}
				}

				if (!demandReasonAlreadyPresent) {
					EgDemandDetails dmdDet = createDemandDetail(bpaFee,
							bpaFee.getFeeAmount());
					if (dmdDet != null) {
						dmdDet.setEgDemand(dmd);
						dmd.getEgDemandDetails().add(dmdDet);
					}
				}
			} else {
				delDmdDetailList.add(dmdReason);
			}

		}

		// Delete from the existing list.
		for (EgDemandReason dmdReason : delDmdDetailList) {
			for (EgDemandDetails dmdDtl : dmd.getEgDemandDetails()) {
				if (dmdReason != null
						&& dmdDtl.getEgDemandReason() != null
						&& dmdDtl.getEgDemandReason().getId()
								.equals(dmdReason.getId())) {
					dmd.getEgDemandDetails().remove(dmdDtl);
					break;
				}
			}
		}

		for (EgDemandDetails det : dmd.getEgDemandDetails()) {
			totaldmdAmt = totaldmdAmt.add(det.getAmount());
		}

		dmd.setBaseDemand(totaldmdAmt);

		if (registrationObj.getEgDemand() == null) {
			registrationObj.setEgDemand(dmd);

		}
		entityManager.flush();
		getEgDemandService().persist(dmd);
		LOGGER.debug("Exit updateDemandByUsingSanctionFeeList ");

		return registrationObj;
	}

	/*
	 * private EgDemand createNewDemand() {
	 * LOGGER.debug("Enter createNewDemand "); Installment installment =
	 * getCurrentInstallment(); EgDemand demand = new EgDemand();
	 * demand.setAmtCollected(BigDecimal.ZERO);
	 * demand.setAmtRebate(BigDecimal.ZERO); demand.setIsHistory("N");
	 * demand.setModifiedDate(new Date()); demand.setCreateTimestamp(new
	 * Date()); if (installment == null) { throw new
	 * EGOVRuntimeException("Installment is null"); } else {
	 * demand.setEgInstallmentMaster(installment); }
	 * persistenceService.setType(EgDemand.class);
	 * persistenceService.create(demand); LOGGER.debug("Created new Demand ");
	 * LOGGER.debug("Exited createNewDemand "); return demand; }
	 */
}