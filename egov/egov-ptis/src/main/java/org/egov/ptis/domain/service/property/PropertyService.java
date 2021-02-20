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
package org.egov.ptis.domain.service.property;

import static java.lang.Boolean.FALSE;
import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.search.elasticsearch.entity.ApplicationIndex;
import org.egov.search.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.search.elasticsearch.entity.enums.ClosureStatus;
import org.egov.search.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.MoneyUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.portal.entity.PortalInbox;
import org.egov.portal.entity.PortalInboxBuilder;
import org.egov.portal.service.PortalInboxService;
import org.egov.ptis.bean.AmalgamateWaterConnection;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.calculator.APTaxCalculationInfo;
import org.egov.ptis.client.service.PenaltyCalculationService;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyHibernateDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyOccupationDAO;
import org.egov.ptis.domain.dao.property.PropertySourceHibernateDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.document.DocumentTypeDetails;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.objection.Petition;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDepartment;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.PtApplicationType;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionApproval;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.BoundaryDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.PropertyDetails;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.domain.repository.PropertyDepartmentRepository;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.master.service.ApartmentService;
import org.egov.ptis.master.service.FloorTypeService;
import org.egov.ptis.master.service.RoofTypeService;
import org.egov.ptis.master.service.StructureClassificationService;
import org.egov.ptis.master.service.TaxExemptionReasonService;
import org.egov.ptis.master.service.WallTypeService;
import org.egov.ptis.master.service.WoodTypeService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service class to perform services related to an Assessment
 *
 * @author subhash
 */
@Transactional(readOnly = true)
public class PropertyService {
    private static final String IS_INCREASED = "isIncreased";
    private static final String AMOUNT = "amount";
    private static final String ARREAR_TAX = "ARREAR_TAX";
    private static final String CURR_TAX = "CURR_TAX";
    private static final String AND_PMV_HOUSE_NO_LIKE = " and pmv.houseNo like ?";
    private static final String WHERE_PMV_IS_ACTIVE_TRUE = "where pmv.isActive = true ";
    private static final String SELECT_COUNT_PMV = "select count(pmv) ";
    private static final String SELECT_PMV = "select pmv ";
    private static final String FROM_PROPERTY_MATERLIZE_VIEW_PMV = "from PropertyMaterlizeView pmv ";
    private static final String MV_QUERY = "select pmv from PropertyMaterlizeView pmv where pmv.isActive = true ";
    private static final String OWNER_NAME = "OwnerName";
    private static final String HOUSE_NO = "HouseNo";
    private static final String NEW_PROPERTY = ", newProperty: ";
    private static final String DATE_OF_COMPLETION = ", dateOfCompletion: ";
    private static final Logger LOGGER = Logger.getLogger(PropertyService.class);
    public static final String APPLICATION_VIEW_URL = "/ptis/view/viewProperty-viewForm.action?applicationNo=%s&applicationType=%s";
    private static final String PROPERTY_WORKFLOW_STARTED = "Property Workflow Started";
    private static final String APPLICATION_NO = "Application no ";
    private static final String REGARDING = " regarding ";
    private static final String STATUS = " status ";
    public static final String SEARCH = "search";
    public static final String COUNT = "count";
    public static final String PARAMS = "params";
    protected static final Set<String> DEMAND_REASONS = new LinkedHashSet<>(Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
            DEMANDRSN_CODE_GENERAL_TAX, DEMANDRSN_CODE_DRAINAGE_TAX, DEMANDRSN_CODE_SCAVENGE_TAX,
            DEMANDRSN_CODE_WATER_TAX, DEMANDRSN_CODE_LIGHT_TAX, DEMANDRSN_CODE_VACANT_TAX, DEMANDRSN_CODE_EDUCATIONAL_TAX,
            DEMANDRSN_CODE_LIBRARY_CESS, DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY));
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressWarnings("rawtypes")
    private PersistenceService propPerServ;
    private Installment currentInstall;
    private BigDecimal totalAlv = BigDecimal.ZERO;
    protected PersistenceService<BasicProperty, Long> basicPropertyService;
    private final Map<Installment, Set<EgDemandDetails>> demandDetails = new HashMap<>();
    private Map<Installment, Map<String, BigDecimal>> excessCollAmtMap = new LinkedHashMap<>();

    @Autowired
    private APTaxCalculator taxCalculator;
    private HashMap<Installment, TaxCalculationInfo> instTaxMap;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    protected EisCommonsService eisCommonsService;
    @Autowired
    private ModuleService moduleService;
    @SuppressWarnings("rawtypes")
    @Autowired
    private InstallmentHibDao installmentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;
    @Autowired
    @Qualifier("fileStoreService")
    private FileStoreService fileStoreService;
    @Autowired
    private ApplicationIndexService applicationIndexService;
    @Autowired
    private SimpleRestClient simpleRestClient;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PropertyStatusValuesDAO propertyStatusValuesDAO;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PenaltyCalculationService penaltyCalculationService;
    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private PropertyDepartmentRepository ptDepartmentRepository;
    @Autowired
    private VacantLandPlotAreaRepository vacantLandPlotAreaRepository;
    @Autowired
    private LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepository;
    @Autowired
    @Qualifier("documentTypeDetailsService")
    private PersistenceService<DocumentTypeDetails, Long> documentTypeDetailsService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    private PortalInboxService portalInboxService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PropertyHibernateDAO propertyHibernateDAO;
    @Autowired
    private FloorTypeService floorTypeService;
    @Autowired
    private RoofTypeService roofTypeService;
    @Autowired
    private WallTypeService wallTypeService;
    @Autowired
    private WoodTypeService woodTypeService;
    @Autowired
    private TaxExemptionReasonService taxExemptionReasonService;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private PropertyMutationMasterDAO propertyMutationMasterDAO;
    @Autowired
    private PropertyUsageDAO propertyUsageDAO;
    @Autowired
    private PropertyOccupationDAO propertyOccupationDAO;
    @Autowired
    private StructureClassificationService structureClassificationService;
    @Autowired
    private PropertyStatusDAO propertyStatusDAO;
    @Autowired
    private PropertyDAO propertyDAO;
    @Autowired
    private PropertySourceHibernateDAO propertySourceHibernateDAO;
    /**
     * Creates a new property if property is in transient state else updates persisted property
     *
     * @param property
     * @param areaOfPlot
     * @param mutationCode
     * @param propTypeId
     * @param propUsageId
     * @param propOccId
     * @param status
     * @param docnumber
     * @param nonResPlotArea
     * @param floorTypeId
     * @param roofTypeId
     * @param wallTypeId
     * @param woodTypeId
     * @param taxExemptId
     * @param nonVacant
     * @return Created or Updated property
     */
    public PropertyImpl createProperty(final PropertyImpl property, final String areaOfPlot, final String mutationCode,
            final String propTypeId, final String propUsageId, final String propOccId, final Character status,
            final String docnumber, final String nonResPlotArea, final Long floorTypeId, final Long roofTypeId,
            final Long wallTypeId, final Long woodTypeId, final Long taxExemptId, final Long propertyDepartmentId,
            final Long vacantLandPlotAreaId, final Long layoutApprovalAuthorityId, final Boolean nonVacant) {
		currentInstall = (Installment) installmentDao.getInstallmentsByModuleBetweenFromDateAndToDate(
				moduleService.getModuleByName(PTMODULENAME), new Date(), new Date()).get(0);
		final PropertySource propertySource = propertySourceHibernateDAO.getPropertySourceByCode(PROP_SOURCE);
        if (floorTypeId != null && floorTypeId != -1) {
            final FloorType floorType = floorTypeService.getFloorTypeById(floorTypeId);
            property.getPropertyDetail().setFloorType(floorType);
        } else
            property.getPropertyDetail().setFloorType(null);
        if (roofTypeId != null && roofTypeId != -1) {
            final RoofType roofType = roofTypeService.getRoofTypeById(roofTypeId);
            property.getPropertyDetail().setRoofType(roofType);
        } else
            property.getPropertyDetail().setRoofType(null);
        if (wallTypeId != null && wallTypeId != -1) {
            final WallType wallType = wallTypeService.getWallTypeById(wallTypeId);
            property.getPropertyDetail().setWallType(wallType);
        } else
            property.getPropertyDetail().setWallType(null);
        if (woodTypeId != null && woodTypeId != -1) {
            final WoodType woodType = woodTypeService.getWoodTypeById(woodTypeId);
            property.getPropertyDetail().setWoodType(woodType);
        } else
            property.getPropertyDetail().setWoodType(null);
        if (taxExemptId != null && taxExemptId != -1) {
            final TaxExemptionReason taxExemptionReason = taxExemptionReasonService.getExemptionReasonById(taxExemptId);
            property.setTaxExemptedReason(taxExemptionReason);
            property.setIsExemptedFromTax(Boolean.TRUE);
        }

        final Area area = new Area();
        if (property.getId() == null && property.getPropertyDetail().isAppurtenantLandChecked()) {
            if (nonVacant)
                area.setArea(new Float(property.getPropertyDetail().getExtentAppartenauntLand()));
            else
                area.setArea(property.getPropertyDetail().getSitalArea().getArea());
        } else if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
            area.setArea(new Float(areaOfPlot));
            property.getPropertyDetail().setSitalArea(area);
        } else
            area.setArea(property.getPropertyDetail().getSitalArea().getArea());
        property.getPropertyDetail().setSitalArea(area);

        if (property.getPropertyDetail().getApartment() != null
                && property.getPropertyDetail().getApartment().getId() != null) {
			final Apartment apartment = apartmentService
					.getApartmentById(property.getPropertyDetail().getApartment().getId());
            property.getPropertyDetail().setApartment(apartment);
        } else
            property.getPropertyDetail().setApartment(null);

        if (nonResPlotArea != null && !nonResPlotArea.isEmpty()) {
            area.setArea(new Float(nonResPlotArea));
            property.getPropertyDetail().setNonResPlotArea(area);
        }

        property.getPropertyDetail().setFieldVerified('Y');
        property.getPropertyDetail().setProperty(property);
        final PropertyTypeMaster propTypeMstr;
        if (property.getId() == null && property.getPropertyDetail().isAppurtenantLandChecked() && !nonVacant)
            propTypeMstr = propertyTypeMasterDAO.getPropertyTypeMasterByCode(OWNERSHIP_TYPE_VAC_LAND);
        else
            propTypeMstr = propertyTypeMasterDAO.findById(Long.valueOf(propTypeId), false);
		final PropertyMutationMaster propMutMstr = propertyMutationMasterDAO
				.getPropertyMutationMasterByCode(mutationCode);
        if (propUsageId != null) {
			final PropertyUsage usage = propertyUsageDAO.findById(Long.valueOf(propUsageId), false);
            property.getPropertyDetail().setPropertyUsage(usage);
        } else
            property.getPropertyDetail().setPropertyUsage(null);
        if (propOccId != null) {
			final PropertyOccupation occupancy = propertyOccupationDAO.findById(Long.valueOf(propOccId), false);
            property.getPropertyDetail().setPropertyOccupation(occupancy);
        } else
            property.getPropertyDetail().setPropertyOccupation(null);
        property.getPropertyDetail().setPropertyType(
                propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND) ? VACANT_PROPERTY : BUILT_UP_PROPERTY);

        property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        property.getPropertyDetail().setPropertyMutationMaster(propMutMstr);
        property.getPropertyDetail().setUpdatedTime(new Date());
        if (!property.getPropertyDetail().isAppurtenantLandChecked()
                || property.getPropertyDetail().isAppurtenantLandChecked() && nonVacant)
            createFloors(property, mutationCode, propUsageId, propOccId);
        property.setStatus(status);
        property.setIsDefaultProperty(PROPERTY_IS_DEFAULT);
        property.setInstallment(currentInstall);
        property.setEffectiveDate(currentInstall.getFromDate());
        property.setPropertySource(propertySource);
        if (StringUtils.isBlank(property.getSource()))
            property.setSource(propertyTaxCommonUtils.setSourceOfProperty(securityUtils.getCurrentUser(),
                    ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())));
        property.setDocNumber(docnumber);
        if (property.getApplicationNo() == null)
            property.setApplicationNo(applicationNumberGenerator.generate());
        if (propertyDepartmentId != null && propertyDepartmentId != -1) {
            final PropertyDepartment propertyDepartment = ptDepartmentRepository.findOne(propertyDepartmentId);
            property.getPropertyDetail().setPropertyDepartment(propertyDepartment);
        } else
            property.getPropertyDetail().setPropertyDepartment(null);
        if (vacantLandPlotAreaId != null)
            property.getPropertyDetail()
                    .setVacantLandPlotArea(vacantLandPlotAreaRepository.findOne(vacantLandPlotAreaId));
        else
            property.getPropertyDetail().setVacantLandPlotArea(null);
        if (layoutApprovalAuthorityId != null)
            property.getPropertyDetail()
                    .setLayoutApprovalAuthority(layoutApprovalAuthorityRepository.findOne(layoutApprovalAuthorityId));
        else
            property.getPropertyDetail().setLayoutApprovalAuthority(null);
        return property;
    }

    /**
     * Creates floors for a property by getting list of floors from the property details proxy, by removing the existing floors
     * from property detail if any
     *
     * @param property
     * @param mutationCode
     * @param propUsageId
     * @param propOccId
     */
    public void createFloors(final Property property, final String mutationCode, final String propUsageId,
            final String propOccId) {
        LOGGER.debug("Entered into createFloors");
        LOGGER.debug("createFloors: Property: " + property + ", mutationCode: " + mutationCode + ", propUsageId: "
                + propUsageId + ", propOccId: " + propOccId);

        final Area totBltUpArea = new Area();
        Float totBltUpAreaVal = 0F;
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            property.getPropertyDetail().getFloorDetails().clear();
            int floorUid = 1;
            for (final Floor floor : property.getPropertyDetail().getFloorDetailsProxy())
                if (floor != null) {
                    totBltUpAreaVal = totBltUpAreaVal + floor.getBuiltUpArea().getArea();
                    PropertyTypeMaster unitType = null;
                    PropertyUsage usage = null;
                    PropertyOccupation occupancy = null;
                    if (floor.getUnitType() != null)
                    	unitType = propertyTypeMasterDAO.findById(floor.getUnitType().getId(), false);
                    if (floor.getPropertyUsage() != null)
                    	usage = propertyUsageDAO.findById(floor.getPropertyUsage().getId(), false);
                    if (floor.getPropertyOccupation() != null)
                    	occupancy = propertyOccupationDAO.findById(floor.getPropertyOccupation().getId(), false);
                   
                    StructureClassification structureClass = null;

                    if (floor.getStructureClassification() != null)
						structureClass = structureClassificationService
								.findOne(floor.getStructureClassification().getId());

                    if (floor.getOccupancyDate() != null && floor.getConstructionDate() != null)
                        floor.setDepreciationMaster(propertyTaxUtil.getDepreciationByDate(floor.getConstructionDate(),
                                floor.getOccupancyDate()));
                    if (unitType != null
                            && unitType.getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT))
                        floor.setFloorNo(OPEN_PLOT_UNIT_FLOORNUMBER);

                    floor.setUnitType(unitType);
                    floor.setPropertyUsage(usage);
                    floor.setPropertyOccupation(occupancy);
                    floor.setStructureClassification(structureClass);
                    floor.setPropertyDetail(property.getPropertyDetail());
                    floor.setCreatedDate(new Date());
                    floor.setModifiedDate(new Date());
                    floor.setFloorUid(floorUid++);
                    final User user = userService.getUserById(ApplicationThreadLocals.getUserId());
                    floor.setCreatedBy(user);
                    floor.setModifiedBy(user);
                    property.getPropertyDetail().getFloorDetails().add(floor);
                    totBltUpArea.setArea(totBltUpAreaVal);
                    totBltUpArea.setLength(floor.getBuiltUpArea().getLength());
                    totBltUpArea.setBreadth(floor.getBuiltUpArea().getBreadth());
                    property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);

                }
            property.getPropertyDetail().setNoofFloors(property.getPropertyDetail().getFloorDetailsProxy().size());
        } else {
            property.getPropertyDetail().setNoofFloors(0);
            property.getPropertyDetail().getFloorDetails().clear();
            totBltUpArea.setArea(totBltUpAreaVal);
            property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);
        }
    }

    /**
     * Creates property status values based on the status code
     *
     * @param basicProperty
     * @param statusCode
     * @param propCompletionDate
     * @param courtOrdNum
     * @param orderDate
     * @param judgmtDetails
     * @param parentPropId
     * @return PropertyImpl
     */
    public PropertyStatusValues createPropStatVal(final BasicProperty basicProperty, final String statusCode,
            final Date propCompletionDate, final String courtOrdNum, final Date orderDate, final String judgmtDetails,
            final String parentPropId) {
        final PropertyStatusValues propStatVal = new PropertyStatusValues();
		final PropertyStatus propertyStatus = propertyStatusDAO.getPropertyStatusByCode(statusCode);
        propStatVal.setIsActive("Y");
        final User user = userService.getUserById(ApplicationThreadLocals.getUserId());
        propStatVal.setCreatedDate(new Date());
        propStatVal.setModifiedDate(new Date());
        propStatVal.setCreatedBy(user);
        propStatVal.setModifiedBy(user);
        propStatVal.setPropertyStatus(propertyStatus);
        if (orderDate != null || courtOrdNum != null && !courtOrdNum.equals("")
                || judgmtDetails != null && !judgmtDetails.equals("")) {
            propStatVal.setReferenceDate(orderDate);
            propStatVal.setReferenceNo(courtOrdNum);
            propStatVal.setRemarks(judgmtDetails);
        } else {
            propStatVal.setReferenceDate(new Date());
            propStatVal.setReferenceNo("0001");// There should be rule to create
            // order number, client has to
            // give it
        }
        if (!statusCode.equals(PROP_CREATE_RSN) && propCompletionDate != null) {
            // persist the DateOfCompletion in case of modify property for
            // future reference
            final String propCompDateStr = DateUtils.getFormattedDate(propCompletionDate, DATE_FORMAT_DDMMYYY);
            propStatVal.setExtraField1(propCompDateStr);
        }
        propStatVal.setBasicProperty(basicProperty);
        if (basicProperty.getPropertyMutationMaster() != null
                && basicProperty.getPropertyMutationMaster().getCode().equals(PROP_CREATE_RSN_BIFUR)) {
			final BasicProperty referenceBasicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(parentPropId);
            propStatVal.setReferenceBasicProperty(referenceBasicProperty);
        }
        return propStatVal;
    }

    /**
     * Creates installment wise demands for a property
     *
     * @param property
     * @param dateOfCompletion
     * @return Property with installment wise demand set
     * @throws TaxCalculatorExeption
     */
    public Property createDemand(final PropertyImpl property, final Date dateOfCompletion)
            throws TaxCalculatorExeption {
        instTaxMap = taxCalculator.calculatePropertyTax(property, dateOfCompletion);
        Ptdemand ptDemand;
        final Set<Ptdemand> ptDmdSet = new HashSet<>();
        Set<EgDemandDetails> dmdDetailSet;
        List<Installment> instList;
        instList = new ArrayList<>(instTaxMap.keySet());
        currentInstall = propertyTaxCommonUtils.getCurrentInstallment();
        property.getPtDemandSet().clear();

        final Map<String, Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        final Installment installmentFirstHalf = yearwiseInstMap.get(CURRENTYEAR_FIRST_HALF);
        final Installment installmentSecondHalf = yearwiseInstMap.get(CURRENTYEAR_SECOND_HALF);

        APTaxCalculationInfo taxCalcInfo;

        /**
         * Only 1 Ptdemand will be created, i.e., for 1st half of current year. Demand Details will be created from the effective
         * date till 2nd installment of current year
         *
         * In case of demolition done in 2nd half, APTaxCalculationInfo is generated for current year 2nd installment. In this
         * case, demand needs to be calculated for both installments of next financial year only. Currently, entering future date
         * is not allowed during create/modify etc Demolition in 2nd is the only use case where demand will be calculated for
         * future date The below conditions are added to handle all above use cases
         */
        if (instList.size() == 1 && instList.get(0).equals(installmentSecondHalf))
            taxCalcInfo = (APTaxCalculationInfo) instTaxMap.get(installmentSecondHalf);
        else if (dateOfCompletion.after(installmentSecondHalf.getToDate())) {
            taxCalcInfo = (APTaxCalculationInfo) instTaxMap.get(installmentSecondHalf);
            instList.remove(installmentSecondHalf);
        } else
            taxCalcInfo = (APTaxCalculationInfo) instTaxMap.get(installmentFirstHalf);

        dmdDetailSet = createAllDmdDetails(instList, instTaxMap);
        final PTDemandCalculations ptDmdCalc = new PTDemandCalculations();
        ptDemand = new Ptdemand();
        ptDemand.setBaseDemand(taxCalcInfo.getTotalTaxPayable());
        ptDemand.setCreateDate(new Date());
        ptDemand.setModifiedDate(new Date());
        ptDemand.setEgInstallmentMaster(installmentFirstHalf);

        ptDemand.setEgDemandDetails(dmdDetailSet);
        ptDemand.setIsHistory("N");
        ptDemand.setEgptProperty(property);
        ptDmdSet.add(ptDemand);
        ptDmdCalc.setPtDemand(ptDemand);
        ptDmdCalc.setPropertyTax(taxCalcInfo.getTotalTaxPayable());
        ptDmdCalc.setTaxInfo(taxCalcInfo.getTaxCalculationInfoXML().getBytes());
        propPerServ.applyAuditing(ptDmdCalc);
        ptDemand.setDmdCalculations(ptDmdCalc);

        // In case of Property Type as (Open Plot,State Govt,Central Govt), set
        // the alv to PTDemandCalculations
        if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            ptDmdCalc.setAlv(taxCalcInfo.getTotalNetARV());
        else if (installmentFirstHalf.equals(currentInstall)) {
            // FloorwiseDemandCalculations should be set only for the current
            // installment for each floor
            for (final Floor floor : property.getPropertyDetail().getFloorDetails())
                ptDmdCalc.addFlrwiseDmdCalculations(createFloorDmdCalc(ptDmdCalc, floor, taxCalcInfo));
            ptDmdCalc.setAlv(totalAlv);
        }

        property.getPtDemandSet().addAll(ptDmdSet);
        return property;
    }

    /**
     * Called to modify Property demands when the property is modified
     *
     * @param oldProperty
     * @param newProperty
     * @param dateOfCompletion
     * @return newProperty
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Property createDemandForModify(final Property oldProperty, final Property newProperty,
            final Date dateOfCompletion) {
        List<Installment> instList;
        instList = new ArrayList<>(instTaxMap.keySet());
        Ptdemand ptDemandOld;
        Ptdemand ptDemandNew;
        final Map<String, Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        final Installment installmentFirstHalf = yearwiseInstMap.get(CURRENTYEAR_FIRST_HALF);
        final Installment installmentSecondHalf = yearwiseInstMap.get(CURRENTYEAR_SECOND_HALF);
        final Map<String, Ptdemand> oldPtdemandMap = getPtdemandsAsInstMap(oldProperty.getPtDemandSet());
        ptDemandOld = oldPtdemandMap.get(installmentFirstHalf.getDescription());
        if (ptDemandOld == null) {
            ptDemandOld = getLatestDemandforHistoryProp(oldProperty);
        }
        final PropertyTypeMaster oldPropTypeMaster = oldProperty.getPropertyDetail().getPropertyTypeMaster();
        final PropertyTypeMaster newPropTypeMaster = newProperty.getPropertyDetail().getPropertyTypeMaster();

        if (!oldProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(newProperty.getPropertyDetail().getPropertyTypeMaster().getCode())
                || !oldProperty.getIsExemptedFromTax() ^ !newProperty.getIsExemptedFromTax())
            createAllDmdDetails(oldProperty, newProperty, installmentFirstHalf, instList);

        final Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());
        ptDemandNew = newPtdemandMap.get(installmentFirstHalf.getDescription());

        final Map<Installment, Set<EgDemandDetails>> newDemandDtlsMap = getEgDemandDetailsSetAsMap(
                new ArrayList(ptDemandNew.getEgDemandDetails()), instList);

        for (final Installment inst : instList) {
            carryForwardCollection(inst, newDemandDtlsMap.get(inst), ptDemandOld, oldPropTypeMaster,
                    newPropTypeMaster);

            /*
             * If current year second half is the only installment in the list, then create the arrear demand details from the
             * Ptdemand of current year first installment
             */
            if (instList.size() == 1 && instList.get(0).equals(installmentSecondHalf))
                carryForwardPenalty(ptDemandOld, ptDemandNew);
            else if (inst.equals(currentInstall))
                carryForwardPenalty(ptDemandOld, ptDemandNew);
        }

        // sort the installment list in ascending order to start the
        // excessCollection adjustment from 1st inst
        Collections.sort(instList);

        if (!excessCollAmtMap.isEmpty())
            adjustExcessCollectionAmount(instList, newDemandDtlsMap, ptDemandNew);
        return newProperty;
    }

    /**
     * Carries forward the penalty from the old property to the new property
     *
     * @param ptDemandOld
     * @param ptDemandNew
     */
    private void carryForwardPenalty(final Ptdemand ptDemandOld, final Ptdemand ptDemandNew) {
        List<EgDemandDetails> penaltyDmdDtlsList;
        penaltyDmdDtlsList = getEgDemandDetailsListForReason(ptDemandOld.getEgDemandDetails(),
                DEMANDRSN_CODE_PENALTY_FINES);
        if (!penaltyDmdDtlsList.isEmpty())
            for (final EgDemandDetails penaltyDmdDet : penaltyDmdDtlsList)
                ptDemandNew.getEgDemandDetails().add((EgDemandDetails) penaltyDmdDet.clone());
        penaltyDmdDtlsList = getEgDemandDetailsListForReason(ptDemandOld.getEgDemandDetails(),
                DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY);
        if (!penaltyDmdDtlsList.isEmpty())
            for (final EgDemandDetails penaltyDmdDet : penaltyDmdDtlsList)
                ptDemandNew.getEgDemandDetails().add((EgDemandDetails) penaltyDmdDet.clone());
    }

    /**
     * Modifies property active demand and creates arrears demand and performs the excess collection adjustment
     *
     * @param propertyModel
     * @param oldProperty
     * @return
     * @throws TaxCalculatorExeption
     */
    public Property modifyDemand(final PropertyImpl propertyModel, final PropertyImpl oldProperty)
            throws TaxCalculatorExeption {
        Date propCompletionDate;
        if (!propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
        final PropertyImpl newProperty = (PropertyImpl) createDemand(propertyModel, propCompletionDate);
        Property modProperty = null;
        if (oldProperty == null)
            LOGGER.info("modifyBasicProp, Could not get the previous property. DCB for arrears will be incorrect");
        else {
            modProperty = createDemandForModify(oldProperty, newProperty, propCompletionDate);
            if (!(propertyModel.getPropertyModifyReason().equalsIgnoreCase(PROPERTY_MODIFY_REASON_REVISION_PETITION) &&
                    (oldProperty.getPropertyModifyReason().equalsIgnoreCase(PROP_CREATE_RSN) ||
                            oldProperty.getPropertyModifyReason().equalsIgnoreCase(PROPERTY_MODIFY_REASON_ADD_OR_ALTER))))
                modProperty = createArrearsDemand(oldProperty, propCompletionDate, newProperty);
        }

        Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment;
        List<Installment> installments;

        final Set<EgDemandDetails> oldEgDemandDetailsSet = getOldDemandDetails(oldProperty, newProperty);
        demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(oldEgDemandDetailsSet);
        installments = new ArrayList<>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);
        for (final Installment inst : installments) {
            final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<>();
            for (final String rsn : DEMAND_RSNS_LIST) {
                final EgDemandDetails newDmndDtls = getEgDemandDetailsForReason(demandDetailsSetByInstallment.get(inst),
                        rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null
                        && newDmndDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0)
                    dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                            newDmndDtls.getAmtCollected());
            }
            getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }
        final Ptdemand currentDemand = getCurrrentDemand(modProperty);
        demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(currentDemand.getEgDemandDetails());
        installments = new ArrayList<>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);
        for (final Installment inst : installments) {
            final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<>();
            for (final String rsn : DEMAND_RSNS_LIST) {
                final EgDemandDetails newDmndDtls = getEgDemandDetailsForReason(demandDetailsSetByInstallment.get(inst),
                        rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
                    final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected()
                            .subtract(propertyTaxCommonUtils.getTotalDemandVariationAmount(newDmndDtls));
                    // If there is extraColl then add to map
                    if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
                        dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                                extraCollAmt);
                        newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
                        newDmndDtls.setModifiedDate(new Date());
                    }
                }
            }
            getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }
        LOGGER.info("Excess Collection - " + getExcessCollAmtMap());
        adjustExcessCollectionAmount(installments, demandDetailsSetByInstallment, currentDemand);
        return modProperty;
    }

    /**
     * Returns old demand details
     *
     * @param oldProperty
     * @param newProperty
     * @return set of old demand details
     */
    public Set<EgDemandDetails> getOldDemandDetails(final Property oldProperty, final Property newProperty) {
        final Set<EgDemandDetails> oldDemandDetails = new HashSet<>();
        for (final EgDemandDetails dd : getCurrrentDemand(oldProperty).getEgDemandDetails())
            if (dd.getEgDemandReason().getEgInstallmentMaster().getFromDate().before(newProperty.getEffectiveDate()))
                oldDemandDetails.add(dd);
        return oldDemandDetails;
    }

    /**
     * Prepares map of installment wise demand details set
     *
     * @param demandDetailsSet
     * @return Map of Installment wise demand details set
     */
    public Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetByInstallment(
            final Set<EgDemandDetails> demandDetailsSet) {
        final Map<Installment, Set<EgDemandDetails>> newEgDemandDetailsSetByInstallment = new HashMap<>();
        for (final EgDemandDetails dd : demandDetailsSet) {
            if (dd.getAmtCollected() == null)
                dd.setAmtCollected(ZERO);
            if (newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
                final Set<EgDemandDetails> ddSet = new HashSet<>();
                ddSet.add(dd);
                newEgDemandDetailsSetByInstallment.put(dd.getEgDemandReason().getEgInstallmentMaster(), ddSet);
            } else
                newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()).add(dd);
        }
        return newEgDemandDetailsSetByInstallment;
    }

    /**
     * Returns current installment's demand of a property
     *
     * @param property
     * @return
     */
    public Ptdemand getCurrrentDemand(final Property property) {
        Ptdemand currentDemand = null;
        for (final Ptdemand ptdemand : property.getPtDemandSet())
            if (ptdemand.getEgInstallmentMaster().equals(propertyTaxCommonUtils.getCurrentInstallment())) {
                currentDemand = ptdemand;
                break;
            }
        return currentDemand;
    }

    /**
     * Returns formatted property occupation date
     *
     * @param dateOfCompletion
     * @return
     */
    public Date getPropOccupatedDate(final String dateOfCompletion) {
        Date occupationDate = null;
        try {
            if (dateOfCompletion != null && !"".equals(dateOfCompletion))
                occupationDate = dateFormatter.parse(dateOfCompletion);
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return occupationDate;
    }

    /**
     * Creates installment wise demand details
     *
     * @param installment
     * @param instList
     * @param instTaxMap
     * @return
     */
    private Set<EgDemandDetails> createAllDmdDetails(final List<Installment> instList,
            final HashMap<Installment, TaxCalculationInfo> instTaxMap) {
        final Set<EgDemandDetails> dmdDetSet = new HashSet<>();
        for (final Installment inst : instList) {
            final TaxCalculationInfo taxCalcInfo = instTaxMap.get(inst);
            final Map<String, BigDecimal> taxMap = taxCalculator
                    .getMiscTaxesForProp(taxCalcInfo.getUnitTaxCalculationInfos());
            for (final Map.Entry<String, BigDecimal> tax : taxMap.entrySet()) {
                final EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(tax.getKey(), inst);
                dmdDetSet.add(createDemandDetails(tax.getValue(), egDmdRsn));
            }
        }
        return dmdDetSet;
    }

    /**
     * Modifies demand details of newly created property
     *
     * @param oldProperty
     * @param newProperty
     * @param installment
     * @param instList
     * @param instTaxMap
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void createAllDmdDetails(final Property oldProperty, final Property newProperty,
            final Installment installment, final List<Installment> instList) {
        final Set<EgDemandDetails> adjustedDmdDetailsSet = new HashSet<>();
        final Map<String, Ptdemand> oldPtdemandMap = getPtdemandsAsInstMap(oldProperty.getPtDemandSet());
        final Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());
        Ptdemand ptDemandOld;
        Ptdemand ptDemandNew;
        Set<EgDemandDetails> newEgDemandDetailsSet;
        Set<EgDemandDetails> oldEgDemandDetailsSet;
        final List<String> adjstmntReasons = new ArrayList<String>() {
            private static final long serialVersionUID = 860234856101419601L;
            {
                addAll(NON_VACANT_TAX_DEMAND_CODES);
                add(DEMANDRSN_CODE_VACANT_TAX);
                add(DEMANDRSN_CODE_LIBRARY_CESS);
                add(DEMANDRSN_CODE_SEWERAGE_TAX);
                add(DEMANDRSN_CODE_EDUCATIONAL_TAX);
            }
        };
        final List<String> rsnsForNewResProp = new ArrayList<String>() {
            private static final long serialVersionUID = -1654413629447625291L;
            {
                addAll(NON_VACANT_TAX_DEMAND_CODES);
                add(DEMANDRSN_CODE_VACANT_TAX);
                add(DEMANDRSN_CODE_LIBRARY_CESS);
                add(DEMANDRSN_CODE_SEWERAGE_TAX);
                add(DEMANDRSN_CODE_EDUCATIONAL_TAX);
            }
        };
        new ArrayList<String>() {
            private static final long serialVersionUID = -8513477823231046385L;
            {
                addAll(NON_VACANT_TAX_DEMAND_CODES);
                add(DEMANDRSN_CODE_VACANT_TAX);
                add(DEMANDRSN_CODE_LIBRARY_CESS);
                add(DEMANDRSN_CODE_SEWERAGE_TAX);
                add(DEMANDRSN_CODE_EDUCATIONAL_TAX);
            }
        };

        ptDemandOld = oldPtdemandMap.get(installment.getDescription());
        if (ptDemandOld == null) {
            ptDemandOld = oldPtdemandMap.get(new TreeMap(oldPtdemandMap).lastEntry().getKey());
        }
        ptDemandNew = newPtdemandMap.get(installment.getDescription());
        final Map<Installment, Set<EgDemandDetails>> oldDemandDtlsMap = getEgDemandDetailsSetAsMap(
                new ArrayList(ptDemandOld.getEgDemandDetails()), instList);
        for (final Installment inst : instList) {
            oldEgDemandDetailsSet = oldDemandDtlsMap.get(inst);
            final Set<EgDemandDetails> demandDtls = demandDetails.get(inst);
            if (demandDtls != null)
                for (final EgDemandDetails dd : demandDtls) {
                    final EgDemandDetails ddClone = (EgDemandDetails) dd.clone();
                    ddClone.setEgDemand(ptDemandNew);
                    adjustedDmdDetailsSet.add(ddClone);
                }
            else {
                EgDemandDetails oldEgdmndDetails;
                EgDemandDetails newEgDmndDetails;
                newEgDemandDetailsSet = new HashSet<>();

                // Getting EgDemandDetails for inst installment
                for (final EgDemandDetails edd : ptDemandNew.getEgDemandDetails())
                    if (edd.getEgDemandReason().getEgInstallmentMaster().equals(inst))
                        newEgDemandDetailsSet.add((EgDemandDetails) edd.clone());
                if (!oldProperty.getIsExemptedFromTax() && !newProperty.getIsExemptedFromTax())
                    for (int i = 0; i < adjstmntReasons.size(); i++) {
                        final String oldPropRsn = adjstmntReasons.get(i);
                        String newPropRsn;

                        /*
                         * Gives EgDemandDetails from newEgDemandDetailsSet for demand reason oldPropRsn, if we dont have
                         * EgDemandDetails then doing collection adjustments
                         */
                        newEgDmndDetails = getEgDemandDetailsForReason(newEgDemandDetailsSet, oldPropRsn);
                        if (newEgDmndDetails == null) {
                            newPropRsn = rsnsForNewResProp.get(i);
                            oldEgdmndDetails = getEgDemandDetailsForReason(oldEgDemandDetailsSet, oldPropRsn);
                            newEgDmndDetails = getEgDemandDetailsForReason(newEgDemandDetailsSet, newPropRsn);
                            if (newEgDmndDetails != null && oldEgdmndDetails != null)
                                newEgDmndDetails.setAmtCollected(
                                        newEgDmndDetails.getAmtCollected().add(oldEgdmndDetails.getAmtCollected()));
                            else
                                continue;
                        }
                    }
                else if (!oldProperty.getIsExemptedFromTax())
                    newEgDemandDetailsSet = adjustmentsForTaxExempted(ptDemandOld.getEgDemandDetails(),
                            newEgDemandDetailsSet, inst);

                // Collection carry forward logic (This logic is moved out
                // of this method, bcoz it has to be invoked in all usecases
                // and not only when there is property type change
                adjustedDmdDetailsSet.addAll(newEgDemandDetailsSet);
                demandDetails.put(inst, newEgDemandDetailsSet);
            }
        }

        // forwards the base collection for current installment Ptdemand
        if (installment.equals(installment)) {
            Ptdemand ptdOld = oldPtdemandMap.get(installment.getDescription());
            if (ptdOld == null) {
                ptdOld = oldPtdemandMap.get(new TreeMap(oldPtdemandMap).lastEntry().getKey());
            }
            final Ptdemand ptdNew = newPtdemandMap.get(installment.getDescription());
            ptdNew.setAmtCollected(ptdOld.getAmtCollected());
        }
        ptDemandNew.setEgDemandDetails(adjustedDmdDetailsSet);
    }

    /**
     * Carry forwards collection from the old property to the newly created property
     *
     * @param newProperty
     * @param inst
     * @param newEgDemandDetailsSet
     * @param ptDmndOld
     * @param oldPropTypeMaster
     * @param newPropTypeMaster
     * @return
     */
    private Set<EgDemandDetails> carryForwardCollection(final Installment inst,
            final Set<EgDemandDetails> newEgDemandDetailsSet, final Ptdemand ptDmndOld,
            final PropertyTypeMaster oldPropTypeMaster, final PropertyTypeMaster newPropTypeMaster) {
        final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<>();
        final List<String> demandReasonsWithAdvance = new ArrayList<>(DEMAND_RSNS_LIST);
        demandReasonsWithAdvance.add(DEMANDRSN_CODE_ADVANCE);
        for (final String rsn : demandReasonsWithAdvance) {
            List<EgDemandDetails> oldEgDmndDtlsList;
            List<EgDemandDetails> newEgDmndDtlsList;
            oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(), rsn);
            newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet, rsn);
            Map<Installment, EgDemandDetails> oldDemandDtlsMap;
            Map<Installment, EgDemandDetails> newDemandDtlsMap;
            EgDemandDetails oldDmndDtls = null;
            EgDemandDetails newDmndDtls = null;
            if (!oldEgDmndDtlsList.isEmpty()) {
                oldDemandDtlsMap = getEgDemandDetailsAsMap(oldEgDmndDtlsList);
                oldDmndDtls = oldDemandDtlsMap.get(inst);
            }
            if (!newEgDmndDtlsList.isEmpty()) {
                newDemandDtlsMap = getEgDemandDetailsAsMap(newEgDmndDtlsList);
                newDmndDtls = newDemandDtlsMap.get(inst);
            }
            calculateExcessCollection(dmdRsnAmt, rsn, oldDmndDtls, newDmndDtls);
        }
        excessCollAmtMap.put(inst, dmdRsnAmt);

        demandDetails.put(inst, newEgDemandDetailsSet);
        return newEgDemandDetailsSet;
    }

    /**
     * Calculates and prepares demand reason wise excess collection amount
     *
     * @param dmdRsnAmt
     * @param rsn
     * @param oldDmndDtls
     * @param newDmndDtls
     */
    public void calculateExcessCollection(final Map<String, BigDecimal> dmdRsnAmt, final String rsn,
            final EgDemandDetails oldDmndDtls, final EgDemandDetails newDmndDtls) {
        /**
         * If old and new demand details are present then set the old collection amount to the new demand details else if old
         * demand details are not present then make the new collection amount as Zero
         */
        if (newDmndDtls != null && oldDmndDtls != null) {
            newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(oldDmndDtls.getAmtCollected()));
            newDmndDtls.setAmtRebate(newDmndDtls.getAmtRebate().add(oldDmndDtls.getAmtRebate()));
        } else if (newDmndDtls != null) {
            newDmndDtls.setAmtCollected(ZERO);
            newDmndDtls.setAmtRebate(ZERO);
        }

        /**
         * prepares reason wise extra collection amount if any of the demand details has
         */
        if (newDmndDtls != null && !rsn.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)) {
            // This part of code handles the adjustment of extra collections
            // when there is decrease in tax during property modification.

            final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected()
                    .subtract(propertyTaxCommonUtils.getTotalDemandVariationAmount(newDmndDtls));
            // If there is extraColl then add to map
            if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
                dmdRsnAmt.put(rsn, extraCollAmt);
                newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
            }
        }

        /**
         * after modify the old demand reason is not there in new property just take the entire collected amount as excess
         * collection when a unit in new property is exempted from tax 16-Oct-2014 with new requirement, refer card #3427
         */
        if (oldDmndDtls != null && newDmndDtls == null && oldDmndDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0)
            dmdRsnAmt.put(rsn, oldDmndDtls.getAmtCollected());
    }

    /**
     * Called locally to get Map of Installment/Ptdemand pair
     *
     * @param ptdemandSet
     * @return
     */
    private Map<String, Ptdemand> getPtdemandsAsInstMap(final Set<Ptdemand> ptdemandSet) {
        final Map<String, Ptdemand> ptDemandMap = new TreeMap<>();
        for (final Ptdemand ptDmnd : ptdemandSet)
            ptDemandMap.put(ptDmnd.getEgInstallmentMaster().getDescription(), ptDmnd);
        return ptDemandMap;
    }

    /**
     * Called locally to get Map of Installment/EgDemandDetail pair from list of EgDemandDetails
     *
     * @param demandDetailsList
     * @return demandDetailsMap
     */
    public Map<Installment, EgDemandDetails> getEgDemandDetailsAsMap(final List<EgDemandDetails> demandDetailsList) {
        final Map<Installment, EgDemandDetails> demandDetailsMap = new HashMap<>();
        for (final EgDemandDetails dmndDtls : demandDetailsList)
            demandDetailsMap.put(dmndDtls.getEgDemandReason().getEgInstallmentMaster(), dmndDtls);
        return demandDetailsMap;
    }

    /**
     * Called locally to get Installment/Set<EgDemandDetails> pair map
     *
     * @param demandDetailsList
     * @return
     */
    public Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetAsMap(
            final List<EgDemandDetails> demandDetailsList, final List<Installment> instList) {
        final Map<Installment, Set<EgDemandDetails>> demandDetailsMap = new HashMap<>();
        Set<EgDemandDetails> ddSet;
        for (final Installment inst : instList) {
            ddSet = new HashSet<>();
            for (final EgDemandDetails dd : demandDetailsList)
                if (dd.getEgDemandReason().getEgInstallmentMaster().equals(inst))
                    ddSet.add(dd);
            demandDetailsMap.put(inst, ddSet);
        }
        return demandDetailsMap;
    }

    /**
     * Called locally to get EgDemandDetails from the egDemandDetailsSet for demand reason demandReason
     *
     * @param egDemandDetailsSet
     * @param demandReason
     * @return EgDemandDetails
     */
    public EgDemandDetails getEgDemandDetailsForReason(final Set<EgDemandDetails> egDemandDetailsSet,
            final String demandReason) {
        final List<Map<String, EgDemandDetails>> egDemandDetailsList = getEgDemandDetailsAsMap(egDemandDetailsSet);
        EgDemandDetails egDemandDetails = null;

        for (final Map<String, EgDemandDetails> egDmndDtlsMap : egDemandDetailsList) {
            egDemandDetails = egDmndDtlsMap.get(demandReason);
            if (egDemandDetails != null)
                break;
        }
        return egDemandDetails;
    }

    /**
     * Called locally to get EgDemandDetails from the egDemandDetailsSet for demand reason demandReason
     *
     * @param egDemandDetailsSet
     * @param demandReason
     * @return EgDemandDetails
     */
    private List<EgDemandDetails> getEgDemandDetailsListForReason(final Set<EgDemandDetails> egDemandDetailsSet,
            final String demandReason) {
        final List<Map<String, EgDemandDetails>> egDemandDetailsList = getEgDemandDetailsAsMap(egDemandDetailsSet);
        final List<EgDemandDetails> demandListForReason = new ArrayList<>();
        for (final Map<String, EgDemandDetails> egDmndDtlsMap : egDemandDetailsList)
            if (egDmndDtlsMap.get(demandReason) != null)
                demandListForReason.add(egDmndDtlsMap.get(demandReason));
        return demandListForReason;
    }

    /**
     * Called locally to get the egDemandDetailsSet as list of maps with demand reason as key and EgDemandDetails as value
     *
     * @param egDemandDetailsSet
     * @param installment
     * @return
     */
    public List<Map<String, EgDemandDetails>> getEgDemandDetailsAsMap(final Set<EgDemandDetails> egDemandDetailsSet) {
        final List<EgDemandDetails> egDemandDetailsList = new ArrayList<>(egDemandDetailsSet);
        final List<Map<String, EgDemandDetails>> egDemandDetailsListOfMap = new ArrayList<>();
        for (final EgDemandDetails egDmndDtls : egDemandDetailsList) {
            final Map<String, EgDemandDetails> egDemandDetailsMap = new HashMap<>();
            final EgDemandReasonMaster dmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();
            if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_GENERAL_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIGHT_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_LIGHT_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_SCAVENGE_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_SCAVENGE_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_DRAINAGE_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_DRAINAGE_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_WATER_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_WATER_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_VACANT_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_VACANT_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_EDUCATIONAL_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS))
                egDemandDetailsMap.put(DEMANDRSN_CODE_LIBRARY_CESS, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                egDemandDetailsMap.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES))
                egDemandDetailsMap.put(DEMANDRSN_CODE_PENALTY_FINES, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY))
                egDemandDetailsMap.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE))
                egDemandDetailsMap.put(DEMANDRSN_CODE_ADVANCE, egDmndDtls);
            egDemandDetailsListOfMap.add(egDemandDetailsMap);
        }
        return egDemandDetailsListOfMap;
    }

    /**
     * Called locally to Adjust EgDemandDetails for Tax Exempted property
     *
     * @param ptDemandOld
     * @param newEgDemandDetails
     * @return newEgDemandDetails
     */
    private Set<EgDemandDetails> adjustmentsForTaxExempted(final Set<EgDemandDetails> oldEgDemandDetails,
            final Set<EgDemandDetails> newEgDemandDetails, final Installment inst) {
        BigDecimal totalCollAdjstmntAmnt = BigDecimal.ZERO;

        for (final EgDemandDetails egDmndDtls : oldEgDemandDetails)
            if (egDmndDtls.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
                final EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();
                if (!egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS)
                        && !egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_TAX)
                        && !egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                    totalCollAdjstmntAmnt = totalCollAdjstmntAmnt.add(egDmndDtls.getAmtCollected());
            }

        final List<EgDemandDetails> newEgDmndDetails = new ArrayList<>(newEgDemandDetails);

        for (final EgDemandDetails egDmndDtls : newEgDemandDetails) {

            final EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();

            if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_TAX))
                egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.50")));
            else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS))
                egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));
            else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));
        }
        return new HashSet<>(newEgDmndDetails);
    }

    /**
     * Creates demand details for the demand reason which being passed
     *
     * @param amount
     * @param dmdRsn
     * @param inst
     * @return Demand details
     */
    private EgDemandDetails createDemandDetails(final BigDecimal amount, final EgDemandReason dmdRsn) {
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(amount);
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(dmdRsn);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        return demandDetail;
    }

    /**
     * Creates demand details for the demand reason which being passed and sets demand and collection
     *
     * @param amount
     * @param amountCollected
     * @param dmdRsn
     * @param inst
     * @return
     */
    public EgDemandDetails createDemandDetails(final BigDecimal amount, final BigDecimal amountCollected,
            final EgDemandReason dmdRsn, final Installment inst) {
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(amount != null ? amount : BigDecimal.ZERO);
        demandDetail.setAmtCollected(amountCollected != null ? amountCollected : BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(dmdRsn);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        return demandDetail;
    }

    /**
     * Creates Floor wise demand calculations
     *
     * @param ptDmdCal
     * @param floor
     * @param taxCalcInfo
     * @return FloorwiseDemandCalculations
     */
    private FloorwiseDemandCalculations createFloorDmdCalc(final PTDemandCalculations ptDmdCal, final Floor floor,
            final TaxCalculationInfo taxCalcInfo) {
        final FloorwiseDemandCalculations floorDmdCalc = new FloorwiseDemandCalculations();
        floorDmdCalc.setPTDemandCalculations(ptDmdCal);
        floorDmdCalc.setFloor(floor);
        for (final UnitTaxCalculationInfo unitTax : taxCalcInfo.getUnitTaxCalculationInfos())
            if (FLOOR_MAP.get(floor.getFloorNo()).equals(unitTax.getFloorNumber())
                    && floor.getPropertyUsage().getUsageCode().equalsIgnoreCase(unitTax.getUnitUsage())
                    && floor.getPropertyOccupation().getOccupancyCode().equalsIgnoreCase(unitTax.getUnitOccupation())
                    && floor.getStructureClassification().getConstrTypeCode()
                            .equalsIgnoreCase(unitTax.getUnitStructure())
                    && floor.getBuiltUpArea().getArea().equals(Float.valueOf(unitTax.getFloorArea().toString())))
                setFloorDmdCalTax(unitTax, floorDmdCalc);
        totalAlv = totalAlv.add(floorDmdCalc.getAlv());
        return floorDmdCalc;
    }

    /**
     * Sets floor demand calculation taxes
     *
     * @param unitTax
     * @param floorDmdCalc
     */
    public void setFloorDmdCalTax(final UnitTaxCalculationInfo unitTax,
            final FloorwiseDemandCalculations floorDmdCalc) {
        floorDmdCalc.setAlv(unitTax.getNetARV());
        floorDmdCalc.setMrv(unitTax.getMrv());
        floorDmdCalc.setCategoryAmt(unitTax.getBaseRate());
        floorDmdCalc.setTotalTaxPayble(unitTax.getTotalTaxPayable());
        for (final MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes())
            if (DEMANDRSN_CODE_GENERAL_TAX.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax1(floorDmdCalc.getTax1().add(miscTax.getTotalCalculatedTax()));
            else if (DEMANDRSN_CODE_VACANT_TAX.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax2(floorDmdCalc.getTax2().add(miscTax.getTotalCalculatedTax()));
            else if (DEMANDRSN_CODE_LIBRARY_CESS.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax3(floorDmdCalc.getTax3().add(miscTax.getTotalCalculatedTax()));
            else if (DEMANDRSN_CODE_EDUCATIONAL_TAX.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax4(floorDmdCalc.getTax4().add(miscTax.getTotalCalculatedTax()));
            else if (DEMANDRSN_CODE_SEWERAGE_TAX.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax5(floorDmdCalc.getTax5().add(miscTax.getTotalCalculatedTax()));
            else if (DEMANDRSN_CODE_UNAUTHORIZED_PENALTY.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax6(floorDmdCalc.getTax6().add(miscTax.getTotalCalculatedTax()));
            else if (DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax7(floorDmdCalc.getTax7().add(miscTax.getTotalCalculatedTax()));
    }

    /**
     * Returns least date from the floors
     *
     * @param floorList
     * @return
     */
    public Date getLowestDtOfCompFloorWise(final List<Floor> floorList) {
        Date completionDate = null;
        Date floorDate;
        for (final Floor floor : floorList) {
            if (floor != null) {
                floorDate = floor.getOccupancyDate();
                if (floorDate != null && (completionDate == null || completionDate.after(floorDate)))
                    completionDate = floorDate;
            }
        }
        return completionDate;
    }

    public Date getLowestDtOfConstFloorWise(final List<Floor> floorList) {
        Date constructionDate = null;
        Date floorConstDate;
        for (final Floor floor : floorList) {
            if (floor != null) {
                floorConstDate = floor.getConstructionDate();
                if (floorConstDate != null && (constructionDate == null || constructionDate.after(floorConstDate)))
                    constructionDate = floorConstDate;
            }
        }
        return constructionDate;
    }

    /**
     * Creates amalgamation property status values
     *
     * @param amalgPropIds
     * @param parentBasicProperty
     */
    public void createAmalgPropStatVal(final String[] amalgPropIds, final BasicProperty parentBasicProperty) {
        LOGGER.debug("Entered into createAmalgPropStatVal, amalgPropIds(length): "
                + (amalgPropIds != null ? amalgPropIds.length : ZERO) + ", parentBasicProperty: "
                + parentBasicProperty);
		final List<PropertyStatusValues> activePropStatVal = propertyStatusValuesDAO
				.getPropStatValByUpicNoAndStatCodeAndISActive(parentBasicProperty.getUpicNo(), "Y", PROP_CREATE_RSN);
        if (!activePropStatVal.isEmpty())
            for (final PropertyStatusValues propstatval : activePropStatVal)
                propstatval.setIsActive("N");

        for (final String amalgId : amalgPropIds)
            if (StringUtils.isNotBlank(amalgId)) {
                final BasicProperty amalgBasicProp = basicPropertyDAO.getBasicPropertyByPropertyID(amalgId);
                final PropertyStatusValues amalgPropStatVal = new PropertyStatusValues();
				final PropertyStatus propertyStatus = propertyStatusDAO.getPropertyStatusByCode(MARK_DEACTIVE);
                amalgPropStatVal.setIsActive("Y");
                amalgPropStatVal.setPropertyStatus(propertyStatus);
                amalgPropStatVal.setReferenceDate(new Date());
                amalgPropStatVal.setReferenceNo("0001");
                amalgPropStatVal.setRemarks("Property Amalgamated");
                amalgPropStatVal.setReferenceBasicProperty(parentBasicProperty);
                amalgBasicProp.setActive(FALSE);
                amalgBasicProp.addPropertyStatusValues(amalgPropStatVal);
                amalgBasicProp.setUnderWorkflow(FALSE);
                amalgPropStatVal.setBasicProperty(amalgBasicProp);
                parentBasicProperty.addPropertyStatusValues(amalgPropStatVal);
            }
        LOGGER.debug("Exiting from createAmalgPropStatVal");
    }

    /**
     * Creates arrears demand for newly created property
     *
     * @param oldproperty
     * @param dateOfCompletion
     * @param property
     * @return
     */
    public Property createArrearsDemand(final Property oldproperty, final Date dateOfCompletion,
            final PropertyImpl property) {
        LOGGER.debug("Entered into createArrearsDemand, oldproperty: " + oldproperty + DATE_OF_COMPLETION
                + dateOfCompletion + ", property: " + property);
        Ptdemand currPtDmd = null;
        Ptdemand oldCurrPtDmd = null;
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final Installment effectiveInstall = installmentDao.getInsatllmentByModuleForGivenDate(module,
                dateOfCompletion);
        final Installment currInstall = propertyTaxCommonUtils.getCurrentInstallment();
        for (final Ptdemand demand : property.getPtDemandSet())
            if ("N".equalsIgnoreCase(demand.getIsHistory()) && demand.getEgInstallmentMaster().equals(currInstall)) {
                currPtDmd = demand;
                break;
            }

        for (final Ptdemand ptDmd : oldproperty.getPtDemandSet())
            if ("N".equalsIgnoreCase(ptDmd.getIsHistory()) && ptDmd.getEgInstallmentMaster().equals(currInstall))
                oldCurrPtDmd = ptDmd;

        addArrDmdDetToCurrentDmd(oldCurrPtDmd, currPtDmd, effectiveInstall, false);

        LOGGER.debug("Exiting from createArrearsDemand");
        return property;
    }

    /**
     * Adds arrears demand details to the current demand
     *
     * @param ptDmd
     * @param currPtDmd
     * @param effectiveInstall
     * @param isDemolition
     */
    public void addArrDmdDetToCurrentDmd(final Ptdemand ptDmd, final Ptdemand currPtDmd,
            final Installment effectiveInstall, final boolean isDemolition) {
        /*
         * For create/modify/GRP/Bifurcation arrear penalty demand details will be added before, other demand details will be
         * added below In case of demolition, arrear penalty also needs to be added along with other demand details This check is
         * done using isDemolition flag
         */
        if (ptDmd != null) {
            for (final EgDemandDetails dmdDet : ptDmd.getEgDemandDetails())
                if (dmdDet.getInstallmentStartDate().before(effectiveInstall.getFromDate()))
                    if (!isDemolition) {
                        if (!dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES))
                            currPtDmd.addEgDemandDetails((EgDemandDetails) dmdDet.clone());
                    } else
                        currPtDmd.addEgDemandDetails((EgDemandDetails) dmdDet.clone());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PropertyImpl creteNewPropertyForObjectionWorkflow(final BasicProperty basicProperty2,
            final String objectionNum, final Date objectionDate, final User objWfInitiator, final String docNumber,
            final String modifyRsn) {

        final BasicProperty basicProperty = basicProperty2;

        basicProperty.setAllChangesCompleted(FALSE);

        LOGGER.debug("initiateModifyWfForObjection: basicProperty: " + basicProperty);

        final PropertyImpl oldProperty = (PropertyImpl) basicProperty.getProperty();
        final PropertyImpl newProperty = (PropertyImpl) oldProperty.createPropertyclone();

        LOGGER.debug("initiateModifyWfForObjection: oldProperty: " + oldProperty + NEW_PROPERTY + newProperty);
        final List floorProxy = new ArrayList();

        for (final Floor floor : newProperty.getPropertyDetail().getFloorDetails())
            if (floor != null) {
                basicPropertyService.applyAuditing(floor);
                floor.setPropertyDetail(newProperty.getPropertyDetail());
                floorProxy.add(floor);
            }
        newProperty.setApplicationNo("");
        newProperty.getPropertyDetail().setFloorDetails(floorProxy);
        if (newProperty.getPropertyDetail().getPropertyOccupation() != null)
            newProperty.getPropertyDetail().getPropertyOccupation().getId().toString();
        if (newProperty.getPropertyDetail().getPropertyUsage() != null)
            newProperty.getPropertyDetail().getPropertyUsage().getId().toString();
        if (modifyRsn.equalsIgnoreCase(NATURE_OF_WORK_RP))
            newProperty.setPropertyModifyReason(NATURE_OF_WORK_RP);
        else if(modifyRsn.equalsIgnoreCase(WFLOW_ACTION_APPEALPETITION))
            newProperty.setPropertyModifyReason(APPEAL_PETITION);
        else
            newProperty.setPropertyModifyReason(NATURE_OF_WORK_GRP);
        newProperty.setStatus(STATUS_WORKFLOW);
        newProperty.setBasicProperty(basicProperty);

        newProperty.getPtDemandSet().clear();
        basicProperty.addProperty(newProperty);

        LOGGER.debug("Exiting from creteNewPropertyForObjectionWorkflow");
        return newProperty;
    }

    /**
     * Returns property completion date based on the property type
     *
     * @param basicProperty
     * @param newProperty
     * @return
     */
    private Date getPropertyCompletionDate(final BasicProperty basicProperty, final PropertyImpl newProperty) {
        LOGGER.debug("Entered into getPropertyCompletionDate - basicProperty.upicNo=" + basicProperty.getUpicNo());
        Date propCompletionDate = null;
        final String propertyTypeMasterCode = newProperty.getPropertyDetail().getPropertyTypeMaster().getCode();
        if (propertyTypeMasterCode.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            for (final PropertyStatusValues propstatval : basicProperty.getPropertyStatusValuesSet())
                if (propstatval.getExtraField1() != null)
                    try {
                        propCompletionDate = dateFormatter.parse(propstatval.getExtraField1());
                    } catch (final ParseException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                else
                    propCompletionDate = basicProperty.getPropOccupationDate();
        } else {
            final List<Floor> floorList = newProperty.getPropertyDetail().getFloorDetails();
            propCompletionDate = getLowestDtOfCompFloorWise(floorList);
            if (propCompletionDate == null)
                propCompletionDate = basicProperty.getPropOccupationDate();
        }

        LOGGER.debug("propCompletionDate=" + propCompletionDate + "\nExiting from getPropertyCompletionDate");
        return propCompletionDate;
    }

    public PersistenceService getPropPerServ() {
        return propPerServ;
    }

    public void setPropPerServ(final PersistenceService propPerServ) {
        this.propPerServ = propPerServ;
    }

    public APTaxCalculator getTaxCalculator() {
        return taxCalculator;
    }

    public void setTaxCalculator(final APTaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public void setBasicPropertyService(final PersistenceService<BasicProperty, Long> basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    /**
     * setting property status values to Basic Property
     *
     * @param basicProperty
     */
    public void setWFPropStatValActive(final BasicProperty basicProperty) {
        LOGGER.debug("Entered into setWFPropStatValActive, basicProperty: " + basicProperty);
        for (final PropertyStatusValues psv : basicProperty.getPropertyStatusValuesSet()) {
            if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(psv.getPropertyStatus().getStatusCode())
                    && "W".equals(psv.getIsActive()))
                updateWFStatusValues(basicProperty, PROPERTY_MODIFY_REASON_ADD_OR_ALTER);
            if (PROPERTY_MODIFY_REASON_AMALG.equals(psv.getPropertyStatus().getStatusCode())
                    && "W".equals(psv.getIsActive()))
                updateWFStatusValues(basicProperty, PROPERTY_MODIFY_REASON_AMALG);
            if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(psv.getPropertyStatus().getStatusCode())
                    && "W".equals(psv.getIsActive()))
                updateWFStatusValues(basicProperty, PROPERTY_MODIFY_REASON_BIFURCATE);
            if (REVISIONPETITION_STATUS_CODE.equals(psv.getPropertyStatus().getStatusCode())
                    && "W".equals(psv.getIsActive()))
                updateWFStatusValues(basicProperty, REVISIONPETITION_STATUS_CODE);
            if (PROP_CREATE_RSN.equals(psv.getPropertyStatus().getStatusCode()) && "W".equals(psv.getIsActive()))
                psv.setIsActive("Y");
        }
        LOGGER.debug("Exitinf from setWFPropStatValActive");
    }

    private void updateWFStatusValues(final BasicProperty basicProperty, final String statusCode) {
    	final PropertyStatusValues activePropStatVal = propertyStatusValuesDAO
				.getPropStatValByUpicNoAndStatCodeAndISActive(basicProperty.getUpicNo(), "Y", statusCode).get(0);
        if (activePropStatVal != null)
            activePropStatVal.setIsActive("N");
        final PropertyStatusValues wfPropStatVal = propertyStatusValuesDAO
				.getPropStatValByUpicNoAndStatCodeAndISActive(basicProperty.getUpicNo(), "W", statusCode).get(0);
        if (wfPropStatVal != null)
            wfPropStatVal.setIsActive("Y");
    }

    /**
     * Prepares a map of installment and respective reason wise demand for each installment
     *
     * @param property
     * @return Map of installment and respective reason wise demand for each installment
     */
    public Map<Installment, Map<String, BigDecimal>> populateTaxesForVoucherCreation(final Property property) {
        LOGGER.debug("Entered into populateTaxesForVoucherCreation, property: " + property);
        Map<Installment, Map<String, BigDecimal>> amounts = new HashMap<>();
        if (instTaxMap != null) {
            /*
             * for (Map.Entry<Installment, TaxCalculationInfo> instTaxRec : instTaxMap.entrySet()) { Map<String, BigDecimal>
             * taxMap = taxCalculator.getMiscTaxesForProp(instTaxRec.getValue() .getConsolidatedUnitTaxCalculationInfo());
             * amounts.put(instTaxRec.getKey(), taxMap); }
             */
        } else
            amounts = prepareRsnWiseDemandForOldProp(property);
        LOGGER.debug("amounts: " + amounts + "\nExiting from populateTaxesForVoucherCreation");
        return amounts;
    }

    /**
     * Prepares a map of installment and respective reason wise demand for each installment
     *
     * @param property
     * @return Map of installment and respective reason wise demand for each installment
     */
    public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForOldProp(final Property property) {
        LOGGER.debug("Entered into prepareRsnWiseDemandForOldProp, property: " + property);
        Installment inst = null;
        final Map<Installment, Map<String, BigDecimal>> instWiseDmd = new HashMap<>();
        for (final Ptdemand ptdemand : property.getPtDemandSet())
            if ("N".equals(ptdemand.getIsHistory())) {
                inst = ptdemand.getEgInstallmentMaster();
                final Map<String, BigDecimal> rsnWiseDmd = new HashMap<>();
                for (final EgDemandDetails dmdDet : ptdemand.getEgDemandDetails())
                    if (inst.equals(dmdDet.getEgDemandReason().getEgInstallmentMaster())
                            && !dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
                            && !dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY))
                        rsnWiseDmd.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                                propertyTaxCommonUtils.getTotalDemandVariationAmount(dmdDet));
                instWiseDmd.put(inst, rsnWiseDmd);
            }
        return instWiseDmd;
    }

    /**
     * Prepares a map of installment and respective reason wise demand for each installment
     *
     * @param property
     * @return
     */
    public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForPropToBeDeactivated(
            final Property property) {
        LOGGER.debug("Entered into prepareRsnWiseDemandForPropToBeDeactivated, property: " + property);

        final Map<Installment, Map<String, BigDecimal>> amts = prepareRsnWiseDemandForOldProp(property);
        for (final Installment inst : amts.keySet())
            for (final String dmdRsn : amts.get(inst).keySet())
                amts.get(inst).put(dmdRsn, amts.get(inst).get(dmdRsn).negate());
        LOGGER.debug("amts: " + amts + "\n Exiting from prepareRsnWiseDemandForPropToBeDeactivated");
        return amts;
    }

    /**
     * <p>
     * Adjusts the excess collection amount to Demand Details
     * </p>
     * Ex: if there is excess collection for GEN_TAX then adjustments happens from beginning installment to current installment if
     * still there is excess collecion remaining then it will be adjust to the group to which GEN_TAX belongs.
     *
     * @param installments
     * @param newDemandDetailsByInstallment
     */
    public void adjustExcessCollectionAmount(final List<Installment> installments,
            final Map<Installment, Set<EgDemandDetails>> newDemandDetailsByInstallment, final Ptdemand ptDemand) {
        LOGGER.info("Entered into adjustExcessCollectionAmount");
        LOGGER.info("adjustExcessCollectionAmount: installments - " + installments
                + ", newDemandDetailsByInstallment.size - " + newDemandDetailsByInstallment.size());

        if (!excessCollAmtMap.isEmpty())
            adjustCollection(installments, newDemandDetailsByInstallment, DEMAND_REASONS, ptDemand);

        LOGGER.info("Excess collection adjustment is successfully completed..");
        LOGGER.debug("Exiting from adjustExcessCollectionAmount");
    }

    /**
     * Adjusts Collection amount to installment wise demand details
     *
     * @param installments
     * @param newDemandDetailsByInstallment
     * @param demandReasons
     * @param ptDemand
     * @return
     */
    private Ptdemand adjustCollection(final List<Installment> installments,
            final Map<Installment, Set<EgDemandDetails>> newDemandDetailsByInstallment, final Set<String> demandReasons,
            final Ptdemand ptDemand) {

        LOGGER.info("Entered into adjustCollection");
        EgDemandDetails advanceDemandDetails = null;
        BigDecimal balanceDemand;
        BigDecimal excessCollection = BigDecimal.ZERO;
        for (final Map<String, BigDecimal> map : excessCollAmtMap.values())
            for (final BigDecimal amount : map.values())
                excessCollection = excessCollection.add(amount);
        final Installment currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                .get(CURRENTYEAR_SECOND_HALF);
        if (excessCollection.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal collection = BigDecimal.ZERO;
            for (final EgDemandDetails demandDetials : ptDemand.getEgDemandDetails()) {
                if (advanceDemandDetails == null
                        && DEMANDRSN_CODE_ADVANCE
                                .equals(demandDetials.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        && currSecondHalf.equals(demandDetials.getEgDemandReason().getEgInstallmentMaster()))
                    advanceDemandDetails = demandDetials;
                if (!demandDetials.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
                    collection = collection.add(demandDetials.getAmtCollected());
                    demandDetials.setAmtCollected(BigDecimal.ZERO);
                }
            }
            collection = collection.add(excessCollection);
            for (final Installment installment : installments) {
                for (final String demandReason : demandReasons) {
                    final EgDemandDetails newDemandDetail = getEgDemandDetailsForReason(
                            newDemandDetailsByInstallment.get(installment), demandReason);

                    if (newDemandDetail != null) {
                        balanceDemand = propertyTaxCommonUtils.getTotalDemandVariationAmount(newDemandDetail)
                                .subtract(newDemandDetail.getAmtCollected());

                        if (balanceDemand.compareTo(BigDecimal.ZERO) > 0 && collection.compareTo(BigDecimal.ZERO) > 0)
                            if (collection.compareTo(balanceDemand) <= 0) {
                                newDemandDetail.setAmtCollected(newDemandDetail.getAmtCollected().add(collection));
                                newDemandDetail.setModifiedDate(new Date());
                                collection = BigDecimal.ZERO;
                            } else {
                                newDemandDetail
                                        .setAmtCollected(newDemandDetail.getAmtCollected().add(balanceDemand));
                                newDemandDetail.setModifiedDate(new Date());
                                collection = collection.subtract(balanceDemand);
                            }
                    }
                    if (collection.compareTo(BigDecimal.ZERO) == 0)
                        break;
                }
                if (collection.compareTo(BigDecimal.ZERO) == 0)
                    break;
            }

            if (collection.compareTo(BigDecimal.ZERO) > 0)
                if (advanceDemandDetails == null) {
                    final EgDemandDetails demandDetails = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_ADVANCE,
                            collection, currSecondHalf);
                    ptDemand.getEgDemandDetails().add(demandDetails);
                } else
                    advanceDemandDetails.getAmtCollected().add(collection);
            LOGGER.info("Exiting from adjustCollection");

        }
        return ptDemand;
    }

    /**
     * Initiates data entry workflow
     *
     * @param basicProperty
     * @param initiater
     */
    public void initiateDataEntryWorkflow(BasicProperty basicProperty, final User initiater) {
        LOGGER.debug("Entered into initiateDataEntryWorkflow");

        final PropertyImpl oldProperty = (PropertyImpl) basicProperty.getProperty();
        final PropertyImpl newProperty = (PropertyImpl) oldProperty.createPropertyclone();

        // Setting the property state to the objection workflow initiator
        final Position owner = eisCommonsService.getPositionByUserId(initiater.getId());
        final String desigName = propertyTaxUtil.getDesignationName(initiater.getId());
        final String value = WFLOW_ACTION_NAME_MODIFY + ":" + desigName + "_" + WF_STATE_APPROVAL_PENDING;

        newProperty.transition().start().withSenderName(initiater.getName()).withComments(PROPERTY_WORKFLOW_STARTED)
                .withStateValue(value).withOwner(owner).withDateInfo(new Date());

        final PropertyMutationMaster propMutMstr = propertyMutationMasterDAO
				.getPropertyMutationMasterByCode(PROPERTY_MODIFY_REASON_DATA_ENTRY);
        newProperty.getPropertyDetail().setPropertyMutationMaster(propMutMstr);
        newProperty.setStatus(STATUS_WORKFLOW);
        basicProperty.addProperty(newProperty);

        basicProperty.addPropertyStatusValues(createPropStatVal(basicProperty, PROPERTY_MODIFY_REASON_ADD_OR_ALTER,
                getPropertyCompletionDate(basicProperty, newProperty), null, null, null, null));

        basicProperty = basicPropertyService.update(basicProperty);
    }

    /**
     * Changes property details
     *
     * @param modProperty
     * @param propDetail
     * @param numOfFloors
     * @return
     */
    public PropertyImpl changePropertyDetail(final PropertyImpl modProperty, final PropertyDetail propDetail,
            final Integer numOfFloors) {

        LOGGER.debug("Entered into changePropertyDetail, Property is Vacant Land");

        final PropertyDetail propertyDetail = modProperty.getPropertyDetail();

        propDetail.setSitalArea(propertyDetail.getSitalArea());
        propDetail.setTotalBuiltupArea(propertyDetail.getTotalBuiltupArea());
        propDetail.setCommBuiltUpArea(propertyDetail.getCommBuiltUpArea());
        propDetail.setPlinthArea(propertyDetail.getPlinthArea());
        propDetail.setCommVacantLand(propertyDetail.getCommVacantLand());
        propDetail.setCurrentCapitalValue(propertyDetail.getCurrentCapitalValue());
        propDetail.setSurveyNumber(propertyDetail.getSurveyNumber());
        propDetail.setFieldVerified(propertyDetail.getFieldVerified());
        propDetail.setFieldVerificationDate(propertyDetail.getFieldVerificationDate());
        propDetail.setFloorDetails(propertyDetail.getFloorDetails());
        propDetail.setPropertyDetailsID(propertyDetail.getPropertyDetailsID());
        propDetail.setWaterMeterNum(propertyDetail.getWaterMeterNum());
        propDetail.setElecMeterNum(propertyDetail.getElecMeterNum());
        propDetail.setNoofFloors(numOfFloors);
        propDetail.setFieldIrregular(propertyDetail.getFieldIrregular());
        propDetail.setDateOfCompletion(propertyDetail.getDateOfCompletion());
        propDetail.setProperty(propertyDetail.getProperty());
        propDetail.setUpdatedTime(propertyDetail.getUpdatedTime());
        propDetail.setPropertyTypeMaster(propertyDetail.getPropertyTypeMaster());
        propDetail.setPropertyType(propertyDetail.getPropertyType());
        propDetail.setInstallment(propertyDetail.getInstallment());
        propDetail.setPropertyOccupation(propertyDetail.getPropertyOccupation());
        propDetail.setPropertyMutationMaster(propertyDetail.getPropertyMutationMaster());
        propDetail.setComZone(propertyDetail.getComZone());
        propDetail.setCornerPlot(propertyDetail.getCornerPlot());

        if (numOfFloors == 0)
            propDetail.setPropertyUsage(propertyDetail.getPropertyUsage());
        else
            propDetail.setPropertyUsage(null);

        propDetail.setManualAlv(propertyDetail.getManualAlv());
        propDetail.setOccupierName(propertyDetail.getOccupierName());

        modProperty.setPropertyDetail(propDetail);

        LOGGER.debug("Exiting from changePropertyDetail");
        return modProperty;
    }

    public List<DocumentType> getDocumentTypesForTransactionType(final TransactionType transactionType) {
		return (List<DocumentType>) entityManager.createNamedQuery(DocumentType.DOCUMENTTYPE_BY_TRANSACTION_TYPE)
				.setParameter("transactionType", transactionType).getResultList();
    }

    /**
     * Stores Documents
     *
     * @param documents
     */
    public void processAndStoreDocument(final List<Document> documents) {
        documents.forEach(document -> {
            if (!(document.getUploads().isEmpty() || document.getUploadsContentType().isEmpty())) {
                int fileCount = 0;
                if (!document.getFiles().isEmpty())
                    document.getFiles().clear();
                for (final File file : document.getUploads()) {
                    final FileStoreMapper fileStore = fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), FILESTORE_MODULE_NAME);
                    basicPropertyService.applyAuditing(fileStore);
                    document.getFiles().add(fileStore);
                    if (document.getId() != null && document.getType() != null)
                        document.setType(entityManager.find(DocumentType.class, document.getType().getId()));
                }
            }
            if (document.getType() != null)
                document.setType(entityManager.find(DocumentType.class, document.getType().getId()));
        });
    }

    public List<String> propertyApplicationTypes() {
        final List<String> applicationTypes = new ArrayList<>();
        applicationTypes.add(APPLICATION_TYPE_NEW_ASSESSENT);
        applicationTypes.add(APPLICATION_TYPE_ALTER_ASSESSENT);
        applicationTypes.add(APPLICATION_TYPE_BIFURCATE_ASSESSENT);
        applicationTypes.add(APPLICATION_TYPE_TAX_EXEMTION);
        applicationTypes.add(APPLICATION_TYPE_DEMOLITION);
        applicationTypes.add(APPLICATION_TYPE_AMALGAMATION);
        return applicationTypes;
    }

    /**
     * Creates or Updates Application index
     *
     * @param stateAwareObject
     * @param applicationType
     */
    @Transactional
    public void updateIndexes(final StateAware stateAwareObject, final String applicationType) {
        final User stateOwner = getOwnerName(stateAwareObject);
        final int sla = getSlaValue(applicationType);
        if (!applicationType.isEmpty() && propertyApplicationTypes().contains(applicationType))
            updatePropertyIndex(stateAwareObject, applicationType, stateOwner, sla);
        else if (!applicationType.isEmpty() && (applicationType.equalsIgnoreCase(APPLICATION_TYPE_REVISION_PETITION)
                || applicationType.equalsIgnoreCase(APPLICATION_TYPE_GRP)
                || applicationType.equalsIgnoreCase(APPLICATION_TYPE_APPEAL_PETITION)))
            updateRevisionPetitionIndex(stateAwareObject, applicationType, stateOwner, sla);
        else if (!applicationType.isEmpty()
                && Arrays.asList(NATURE_REGISTERED_TRANSFER, NATURE_FULL_TRANSFER).contains(applicationType))
            updatePropertyMutationIndex(stateAwareObject, stateOwner, sla);
        else if (!applicationType.isEmpty() && applicationType.equalsIgnoreCase(APPLICATION_TYPE_VACANCY_REMISSION))
            updateVacancyRemissionIndex(stateAwareObject, applicationType, stateOwner, sla);
        else if (!applicationType.isEmpty()
                && applicationType.equalsIgnoreCase(APPLICATION_TYPE_VACANCY_REMISSION_APPROVAL))
            updateVacancyRemissionApprovalIndex(stateAwareObject, applicationType, stateOwner, sla);

    }

    public User getOwnerName(final StateAware<Position> stateAwareObject) {
        User user = null;
        final Position position = stateAwareObject.getState().getOwnerPosition() != null
                ? stateAwareObject.getState().getOwnerPosition() : stateAwareObject.getState().getPreviousOwner();
        if (position != null)
            user = assignmentService.getAssignmentsForPosition(position.getId(), new Date()).get(0).getEmployee();
        return user;
    }

    @Transactional
    public void updateVacancyRemissionApprovalIndex(final StateAware stateAwareObject, final String applictionType,
            final User stateOwner, final int sla) {
        final VacancyRemissionApproval vacancyRemissionApproval = (VacancyRemissionApproval) stateAwareObject;
        final VacancyRemission vacancyRemission = vacancyRemissionApproval.getVacancyRemission();
        final ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(vacancyRemission.getApplicationNumber());
        final User owner = vacancyRemission.getBasicProperty().getPrimaryOwner();
        final String source = propertyTaxCommonUtils.getVRSource(vacancyRemission);
        if (applicationIndex == null)
            createVacancyRemissionApprovalApplicationIndex(applictionType, stateOwner, sla, vacancyRemission, owner,
                    vacancyRemissionApproval, source);
        else
            updateVacancyRemissionApprovalApplicationIndex(stateOwner, applicationIndex, owner,
                    vacancyRemissionApproval);
    }

    @Transactional
    public void createVacancyRemissionApprovalApplicationIndex(final String applictionType, final User stateOwner,
            final int sla, final VacancyRemission vacancyRemission, final User owner,
            final VacancyRemissionApproval vacancyRemissionApproval, final String source) {
        ApplicationIndex applicationIndex;
        final Date applicationDate = vacancyRemission.getCreatedDate() != null ? vacancyRemission.getCreatedDate()
                : new Date();
        final ClosureStatus closureStatus = vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)
                ? ClosureStatus.YES : ClosureStatus.NO;
        applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                .withApplicationNumber(vacancyRemission.getApplicationNumber()).withApplicationDate(applicationDate)
                .withApplicationType(applictionType).withApplicantName(owner.getName())
                .withStatus(vacancyRemissionApproval.getState().getValue())
                .withUrl(format(APPLICATION_VIEW_URL, vacancyRemission.getApplicationNumber(), applictionType))
                .withApplicantAddress(vacancyRemission.getBasicProperty().getAddress().toString())
                .withOwnername(stateOwner.getUsername() + "::" + stateOwner.getName().trim())
                .withChannel(source).withMobileNumber(owner.getMobileNumber())
                .withAadharNumber(owner.getAadhaarNumber())
                .withConsumerCode(vacancyRemission.getBasicProperty().getUpicNo()).withClosed(closureStatus)
                .withApproved(vacancyRemissionApproval.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                        ? ApprovalStatus.APPROVED
                        : vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)
                                ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS)
                .withSla(sla).build();
        applicationIndexService.createApplicationIndex(applicationIndex);
    }

    @Transactional
    public void updateVacancyRemissionApprovalApplicationIndex(final User stateOwner,
            final ApplicationIndex applicationIndex, final User owner,
            final VacancyRemissionApproval vacancyRemissionApproval) {
        applicationIndex.setStatus(vacancyRemissionApproval.getState().getValue());
        applicationIndex.setApplicantName(owner.getName());
        applicationIndex.setOwnerName(stateOwner.getUsername() + "::" + stateOwner.getName().trim());
        applicationIndex.setMobileNumber(owner.getMobileNumber());
        applicationIndex.setAadharNumber(owner.getAadhaarNumber());
        applicationIndex.setClosed(vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)
                ? ClosureStatus.YES : ClosureStatus.NO);
        if (!ApprovalStatus.APPROVED.equals(applicationIndex.getApproved())
                || vacancyRemissionApproval.getState().getValue().contains(WF_STATE_REJECTED))
            applicationIndex
                    .setApproved(vacancyRemissionApproval.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                            ? ApprovalStatus.APPROVED
                            : vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)
                                    ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS);
        applicationIndexService.updateApplicationIndex(applicationIndex);
    }

    @Transactional
    public void updatePropertyMutationIndex(final StateAware stateAwareObject, final User stateOwner, final int sla) {
        final PropertyMutation propertyMutation = (PropertyMutation) stateAwareObject;
        final ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(propertyMutation.getApplicationNo());
        final User owner = propertyMutation.getBasicProperty().getPrimaryOwner();
        final String source = propertyTaxCommonUtils.getMutationSource(propertyMutation);
        if (applicationIndex == null)
            createMutationApplicationIndex(APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP, stateOwner, sla, propertyMutation, owner,
                    source);
        else
            updateMutationApplicationIndex(stateOwner, propertyMutation, applicationIndex, owner);
    }

    @Transactional
    public void createMutationApplicationIndex(final String applictionType, final User stateOwner, final int sla,
            final PropertyMutation propertyMutation, final User owner, final String source) {
        ApplicationIndex applicationIndex;
        final Date applicationDate = propertyMutation.getCreatedDate() != null ? propertyMutation.getCreatedDate()
                : new Date();
        final ClosureStatus closureStatus = propertyMutation.getState().getValue().contains(WF_STATE_CLOSED)
                ? ClosureStatus.YES : ClosureStatus.NO;
        applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                .withApplicationNumber(propertyMutation.getApplicationNo()).withApplicationDate(applicationDate)
                .withApplicationType(applictionType).withApplicantName(owner.getName())
                .withStatus(propertyMutation.getState().getValue())
                .withUrl(format(APPLICATION_VIEW_URL, propertyMutation.getApplicationNo(), applictionType))
                .withApplicantAddress(propertyMutation.getBasicProperty().getAddress().toString())
                .withOwnername(stateOwner.getUsername() + "::" + stateOwner.getName().trim())
                .withChannel(source).withMobileNumber(owner.getMobileNumber())
                .withAadharNumber(owner.getAadhaarNumber())
                .withConsumerCode(propertyMutation.getBasicProperty().getUpicNo()).withClosed(closureStatus)
                .withApproved(propertyMutation.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                        ? ApprovalStatus.APPROVED
                        : propertyMutation.getState().getValue().contains(WF_STATE_CLOSED)
                                ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS)
                .withSla(sla).build();
        applicationIndexService.createApplicationIndex(applicationIndex);
    }

    @Transactional
    public void updateMutationApplicationIndex(final User stateOwner, final PropertyMutation propertyMutation,
            final ApplicationIndex applicationIndex, final User owner) {
        applicationIndex.setStatus(propertyMutation.getState().getValue());
        applicationIndex.setApplicantName(owner.getName());
        applicationIndex.setOwnerName(stateOwner.getUsername() + "::" + stateOwner.getName().trim());
        applicationIndex.setMobileNumber(owner.getMobileNumber());
        applicationIndex.setAadharNumber(owner.getAadhaarNumber());
        applicationIndex.setClosed(propertyMutation.getState().getValue().contains(WF_STATE_CLOSED) ? ClosureStatus.YES
                : ClosureStatus.NO);
        if (!ApprovalStatus.APPROVED.equals(applicationIndex.getApproved())
                || propertyMutation.getState().getValue().contains(WF_STATE_REJECTED))
            applicationIndex.setApproved(propertyMutation.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                    ? ApprovalStatus.APPROVED
                    : propertyMutation.getState().getValue().contains(WF_STATE_CLOSED)
                            ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS);
        applicationIndexService.updateApplicationIndex(applicationIndex);
    }

    @Transactional
    public void updateRevisionPetitionIndex(final StateAware stateAwareObject, final String applictionType,
            final User stateOwner, final int sla) {
        final Petition petition = (Petition) stateAwareObject;
        final ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(petition.getObjectionNumber());
        final String source = propertyTaxCommonUtils.getObjectionSource(petition);
        if (applicationIndex == null)
            createRevisionPetitionApplicationIndex(applictionType, stateOwner, sla, petition, source);
        else
            updateRevisionPetitionApplicationIndex(applictionType, stateOwner, petition, applicationIndex);
    }

    @Transactional
    public void createRevisionPetitionApplicationIndex(final String applictionType, final User stateOwner,
            final int sla, final Petition petition, final String source) {
        ApplicationIndex applicationIndex;
        final User owner = petition.getBasicProperty().getPrimaryOwner();
        final Date applicationDate = petition.getCreatedDate() != null ? petition.getCreatedDate()
                : new Date();
        final ClosureStatus closureStatus = petition.getState().getValue().contains(WF_STATE_CLOSED)
                ? ClosureStatus.YES : ClosureStatus.NO;
        applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                .withApplicationNumber(petition.getObjectionNumber()).withApplicationDate(applicationDate)
                .withApplicationType(applictionType).withApplicantName(owner.getName())
                .withStatus(petition.getState().getValue())
                .withUrl(format(APPLICATION_VIEW_URL, petition.getObjectionNumber(), applictionType))
                .withApplicantAddress(petition.getBasicProperty().getAddress().toString())
                .withOwnername(stateOwner.getUsername() + "::" + stateOwner.getName().trim())
                .withChannel(source).withMobileNumber(owner.getMobileNumber())
                .withAadharNumber(owner.getAadhaarNumber())
                .withConsumerCode(petition.getBasicProperty().getUpicNo()).withClosed(closureStatus)
                .withApproved(petition.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                        ? ApprovalStatus.APPROVED
                        : petition.getState().getValue().contains(WF_STATE_CLOSED)
                                ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS)
                .withSla(sla).build();
        applicationIndexService.createApplicationIndex(applicationIndex);
    }

    @Transactional
    public void updateRevisionPetitionApplicationIndex(final String applictionType, final User stateOwner,
            final Petition petition, final ApplicationIndex applicationIndex) {
        applicationIndex.setStatus(petition.getState().getValue());
        if (applictionType.equalsIgnoreCase(APPLICATION_TYPE_REVISION_PETITION)
                || applictionType.equalsIgnoreCase(APPLICATION_TYPE_GRP)
                || applictionType.equalsIgnoreCase(APPLICATION_TYPE_APPEAL_PETITION)) {
            applicationIndex.setOwnerName(stateOwner.getUsername() + "::" + stateOwner.getName().trim());
            applicationIndex.setClosed(petition.getState().getValue().contains(WF_STATE_CLOSED)
                    ? ClosureStatus.YES : ClosureStatus.NO);
            if (!ApprovalStatus.APPROVED.equals(applicationIndex.getApproved())
                    || petition.getState().getValue().contains(WF_STATE_REJECTED))
                applicationIndex
                        .setApproved(petition.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                                ? ApprovalStatus.APPROVED
                                : petition.getState().getValue().contains(WF_STATE_CLOSED)
                                        ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS);
        }
        applicationIndexService.updateApplicationIndex(applicationIndex);
    }

    @Transactional
    public void updatePropertyIndex(final StateAware stateAwareObject, final String applictionType,
            final User stateOwner, final int sla) {
        final PropertyImpl property = (PropertyImpl) stateAwareObject;
        if (!SOURCE_SURVEY.equalsIgnoreCase(property.getSource())) {
            final ApplicationIndex applicationIndex = applicationIndexService
                    .findByApplicationNumber(property.getApplicationNo());
            final User owner = property.getBasicProperty().getPrimaryOwner();
            final String source = propertyTaxCommonUtils.getPropertySource(property);
            if (applicationIndex == null)
                createPropertyApplicationIndex(applictionType, stateOwner, sla, property, owner, source);
            else
                updatePropertyApplicationIndex(applictionType, stateOwner, property, applicationIndex, owner);
        }
    }

    /**
     * @param applictionType
     * @param user
     * @param sla
     * @param property
     * @param owner
     * @param source
     */
    @Transactional
    public void createPropertyApplicationIndex(final String applictionType, final User stateOwner, final int sla,
            final PropertyImpl property, final User owner, final String source) {
        ApplicationIndex applicationIndex;
        final Date applicationDate = property.getCreatedDate() != null ? property.getCreatedDate() : new Date();
        final ClosureStatus closureStatus = property.getState().getValue().contains(WF_STATE_CLOSED) ? ClosureStatus.YES
                : ClosureStatus.NO;
        applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                .withApplicationNumber(property.getApplicationNo()).withApplicationDate(applicationDate)
                .withApplicationType(applictionType).withApplicantName(owner.getName())
                .withStatus(property.getState().getValue())
                .withUrl(format(APPLICATION_VIEW_URL, property.getApplicationNo(), applictionType))
                .withApplicantAddress(property.getBasicProperty().getAddress().toString())
                .withOwnername(stateOwner.getUsername() + "::" + stateOwner.getName().trim())
                .withChannel(source).withMobileNumber(owner.getMobileNumber())
                .withAadharNumber(owner.getAadhaarNumber()).withConsumerCode(property.getBasicProperty().getUpicNo())
                .withClosed(closureStatus)
                .withApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                        ? ApprovalStatus.APPROVED
                        : property.getState().getValue().contains(WF_STATE_CLOSED) ? ApprovalStatus.REJECTED
                                : ApprovalStatus.INPROGRESS)
                .withSla(sla).build();

        applicationIndexService.createApplicationIndex(applicationIndex);
    }

    /**
     * @param applictionType
     * @param user
     * @param property
     * @param applicationIndex
     * @param owner
     */

    @Transactional
    public void updatePropertyApplicationIndex(final String applictionType, final User stateOwner,
            final PropertyImpl property, final ApplicationIndex applicationIndex, final User owner) {
        applicationIndex.setStatus(property.getState().getValue());
        if (propertyApplicationTypes().contains(applictionType)) {
            applicationIndex.setConsumerCode(property.getBasicProperty().getUpicNo());
            applicationIndex.setApplicantName(owner.getName());
            applicationIndex.setOwnerName(stateOwner.getUsername() + "::" + stateOwner.getName().trim());
            applicationIndex.setMobileNumber(owner.getMobileNumber());
            applicationIndex.setAadharNumber(owner.getAadhaarNumber());
            applicationIndex.setClosed(
                    property.getState().getValue().contains(WF_STATE_CLOSED) ? ClosureStatus.YES : ClosureStatus.NO);
            if (!ApprovalStatus.APPROVED.equals(applicationIndex.getApproved())
                    || property.getState().getValue().contains(WF_STATE_REJECTED))
                applicationIndex.setApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                        ? ApprovalStatus.APPROVED
                        : (property.getState().getValue().contains(WF_STATE_CLOSED) ? ApprovalStatus.REJECTED
                                : ApprovalStatus.INPROGRESS));

        }
        applicationIndexService.updateApplicationIndex(applicationIndex);
    }

    public int getSlaValue(final String applicationType) {
        int sla = 0;
        if (APPLICATION_TYPE_NEW_ASSESSENT.equals(applicationType))
			sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"CREATE").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType))
			sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"MODIFY").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_GRP.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"GENERAL_REVISION_PETETION").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_REVISION_PETITION.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"REVISION_PETETION").getSingleResult()).getResolutionTime().intValue();
        else if (NATURE_FULL_TRANSFER.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"FULL TRANSFER").getSingleResult()).getResolutionTime().intValue();
        else if (NATURE_REGISTERED_TRANSFER.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"REGISTERED TRANSFER").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_VACANCY_REMISSION.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"VACANCY_REMISSION").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_TAX_EXEMTION.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"TAX_EXEMPTION").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_DEMOLITION.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"DEMOLITION").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_AMALGAMATION.equals(applicationType))
        	sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
					"AMALGAMATION").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_BIFURCATE_ASSESSENT.equals(applicationType))
                sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
                                        "BIFURCATION").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_WRITE_OFF.equals(applicationType))
                sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
                                        "WRITEOFF").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_COURT_VERDICT.equals(applicationType))
                sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
                                        "COURTVERDICT").getSingleResult()).getResolutionTime().intValue();
        else if (APPLICATION_TYPE_APPEAL_PETITION.equals(applicationType))
		sla = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE).setParameter("code",
                                        "APPEAL_PETETION").getSingleResult()).getResolutionTime().intValue();
        return sla;
    }

    /**
     * Returns whether assessment has demand dues or not
     *
     * @param assessmentNo
     * @return
     */
    public Boolean hasDemandDues(final String assessmentNo) {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final BigDecimal currentWaterTaxDue = getWaterTaxDues(assessmentNo)
                .get(WATER_TAX_DUES) == null ? BigDecimal.ZERO
                        : new BigDecimal(Double.valueOf(
                                (Double) getWaterTaxDues(assessmentNo).get(WATER_TAX_DUES)));
        final Map<String, BigDecimal> propertyTaxDetails = getCurrentPropertyTaxDetails(
                basicProperty.getActiveProperty());
        final BigDecimal currentPropertyTaxDue = propertyTaxDetails.get(CURR_DMD_STR)
                .subtract(propertyTaxDetails.get(CURR_COLL_STR));
        final BigDecimal arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR)
                .subtract(propertyTaxDetails.get(ARR_COLL_STR));
        return currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0;
    }

    /**
     * Returns Water tax due of an assessment
     *
     * @param assessmentNo
     * @return
     */
    public Map<String, Object> getWaterTaxDues(final String assessmentNo) {
        final String wtmsRestURL = format(WTMS_TAXDUE_RESTURL,
                WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false), assessmentNo);
        return simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
    }

    /**
     * Returns Water tax due of an assessment
     *
     * @param assessmentNo
     * @param request
     * @return
     */
    public Map<String, Object> getWaterTaxDues(final String assessmentNo, final HttpServletRequest request) {
        final String wtmsRestURL = format(WTMS_TAXDUE_RESTURL, WebUtils.extractRequestDomainURL(request, false),
                assessmentNo);
        return simpleRestClient.getRESTResponseAsMap(wtmsRestURL);

    }

    /**
     * @param assessmentNo
     * @param dueDate
     * @param request
     * @return
     */
    public Map<String, Object> getWaterTaxDues(final String assessmentNo, final Date dueDate,
            final HttpServletRequest request) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String effectiveDate = df.format(dueDate);
        final String wtmsRestURL = format(WTMS_TAXDUE_WITH_DATE_RESTURL,
                WebUtils.extractRequestDomainURL(request, false), assessmentNo, effectiveDate);
        return simpleRestClient.getRESTResponseAsMap(wtmsRestURL);

    }

    /**
     * Method to validate bifurcation of property either using create assessment or alter assessment
     *
     * @param propertyModel model object
     * @param basicProperty basic property of the property which is being bifurcated
     * @param reason Reason for creation or Modification
     * @return
     */
    public String validationForBifurcation(final PropertyImpl propertyModel, final BasicProperty basicProperty,
            final String reason) {
        final List<PropertyStatusValues> children = propertyStatusValuesDAO
                .getPropertyStatusValuesByReferenceBasicProperty(basicProperty);
        final Boolean parentBifurcated = isPropertyBifurcated(basicProperty);
        final Boolean childrenCreated = !children.isEmpty();
        String errorMsg = null;
        /**
         * Reason For Modification is Bifurcation of Assessment
         */
        if (PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(reason)) {
            if (parentBifurcated && !childrenCreated)
                errorMsg = "error.child.not.created";
        }
        /**
         * Reason For Modification is Alteration of Assessment
         */
        else if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equalsIgnoreCase(reason)) {
            if (!childrenCreated) {
                if (parentBifurcated)
                    errorMsg = "error.child.not.created";
            } else if (!parentBifurcated)
                errorMsg = "error.parent.not.bifurcated";
        }
        /**
         * Reason For Modification is Transfer
         */
        else if (PROP_MUTATION_RSN.equalsIgnoreCase(reason) && childrenCreated && !parentBifurcated) {
            errorMsg = "error.parent.not.bifurcated";
        }
        /**
         * Reason For Creation is Bifurcation of Assessment
         */
        else if (PROP_CREATE_RSN_BIFUR.equals(reason))
            if (parentBifurcated)
				propertyDAO.getHistoryPropertyForBasicProperty(basicProperty);
            else
                basicProperty.getActiveProperty();
        return errorMsg;
    }

    /**
     * Tells whether the parent is bifurcated or not
     *
     * @param basicProperty
     * @return
     */
    public Boolean isPropertyBifurcated(final BasicProperty basicProperty) {
        Boolean propBifurcated = Boolean.FALSE;
        for (final Property property : basicProperty.getPropertySet())
            if ((PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(property.getPropertyModifyReason())
                    || PROP_CREATE_RSN_BIFUR.equalsIgnoreCase(property.getPropertyModifyReason()))
                    && !(STATUS_WORKFLOW.equals(property.getStatus())
                            || STATUS_CANCELLED.equals(property.getStatus()))) {
                propBifurcated = Boolean.TRUE;
                break;
            }
        return propBifurcated;
    }

    /**
     * Converting sqr yards to sqr meters
     *
     * @param vacantLandArea
     * @return
     */
    public BigDecimal convertYardToSquareMeters(final Float vacantLandArea) {
        Float areaInSqMts = vacantLandArea * SQUARE_YARD_TO_SQUARE_METER_VALUE;
        return new BigDecimal(areaInSqMts).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns the proper area value for bifurcation calculation
     *
     * @param area
     * @param property
     * @return
     */
    public BigDecimal getPropertyArea(BigDecimal area, final Property property) {
        final PropertyDetail propertyDetail = property.getPropertyDetail();
        if (propertyDetail.isAppurtenantLandChecked() != null && propertyDetail.isAppurtenantLandChecked())
            area = area.add(BigDecimal.valueOf(propertyDetail.getExtentAppartenauntLand()));
        else
            area = area.add(BigDecimal.valueOf(propertyDetail.getSitalArea().getArea()));
        return area;
    }

    /**
     * Checks whether user is an employee or not
     *
     * @param user
     * @return
     */
    public Boolean isEmployee(final User user) {
        for (final Role role : user.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles())
                if (role != null && role.getName().equals(appconfig.getValue()))
                    return false;
        return true;
    }

    /**
     * Checks whether user is meeseva user or not
     *
     * @param user
     * @return
     */
    public Boolean isMeesevaUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(MEESEVA_OPERATOR_ROLE))
                return true;
        return false;
    }

    public Boolean isCitizenPortalUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(CITIZEN_ROLE))
                return true;
        return false;
    }
    
    /**
     * Checks whether user is csc operator or not
     *
     * @param user
     * @return
     */
    public Boolean isCscOperator(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(CSC_OPERATOR_ROLE))
                return true;
        return false;
    }

    public Boolean isDataEntryOperator(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(ROLE_DATAENTRY_OPERATOR))
                return true;
        return false;
    }

    /**
     * Getting User assignment based on designation ,department and zone boundary Reading Designation and Department from
     * appconfig values and Values should be 'Senior Assistant,Junior Assistant' for designation and
     * 'Revenue,Accounts,Administration' for department
     *
     * @param basicProperty
     * @return
     */
    public Assignment getUserPositionByZone(final BasicProperty basicProperty, final boolean isForMobile) {
        final String designationStr = getDesignationForThirdPartyUser(isForMobile);
        final String department = getDepartmentForWorkFlow();
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        if (StringUtils.isNotBlank(department)) {
            for (final String desg : designation) {
                assignment = assignmentService.findByDepartmentDesignationAndBoundary(
                        departmentService.getDepartmentByName(department).getId(),
                        designationService.getDesignationByName(desg).getId(),
                        basicProperty.getPropertyID().getElectionBoundary().getId());
                if (!assignment.isEmpty())
                    break;
            }
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    /**
     * Getting User assignment based on designation ,department and Election ward boundary Reading Designation and Department from
     * appconfig values and Values should be 'Junior Assistant, Senior Assistant' for designation and 'Revenue' for department
     *
     * @param basicProperty
     * @return
     */
    public Assignment getAssignmentByDeptDesigElecWard(final BasicProperty basicProperty) {
        final String designationStr = getDesignationForCscOperatorWorkFlow();
        final String department = getDepartmentForCscOperatorWorkFlow();
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        if (StringUtils.isNotBlank(department)) {
            for (final String desg : designation) {
                final Long deptId = departmentService.getDepartmentByName(department).getId();
                final Long desgId = designationService.getDesignationByName(desg).getId();
                final Long boundaryId = basicProperty.getPropertyID().getElectionBoundary().getId();
                assignment = assignmentService.findAssignmentByDepartmentDesignationAndBoundary(deptId, desgId,
                        boundaryId);
                if (!assignment.isEmpty())
                    break;
            }
        }

        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    /**
     * Returns the latest history property of a basic property
     *
     * @param upicNo
     * @return
     */
	public PropertyImpl getLatestHistoryProperty(final String upicNo) {

		final StringBuilder queryStr = new StringBuilder();
		queryStr.append(
				"from PropertyImpl prop where prop.basicProperty.upicNo = :upicno and prop.status = 'H' order by prop.id desc");
		final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString());
		query.setParameter("upicno", upicNo);
		PropertyImpl property = null;
		List<PropertyImpl> properties = query.list();
		if (!properties.isEmpty())
			property = properties.get(0);
		return property;
	}
    
    /*
     * api to fetch the assignee details for business user request (CSC opertaor , Ward Secretary)
     */
    public Assignment getMappedAssignmentForBusinessUser(final BasicProperty basicProperty) {
        Assignment assignment;
        assignment = getAssignmentByDeptDesigElecWard(basicProperty);
        if (assignment == null)
            assignment = getUserPositionByZone(basicProperty, false);
        return assignment;
    }

    /**
     * Returns Designation for property tax csc operator workflow
     *
     * @return
     */
    public String getDesignationForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PROPERTYTAX_WORKFLOWDESIGNATION_FOR_CSCOPERATOR);
        return null != appConfigValue ? appConfigValue.get(0).getValue() : null;
    }

    /**
     * Returns Department for property tax csc operator workflow
     *
     * @return
     */
    public String getDepartmentForCscOperatorWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PROPERTYTAX_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    /**
     * Returns Department for property tax workflow
     *
     * @return
     */
    public String getDepartmentForWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PROPERTYTAX_WORKFLOWDEPARTEMENT);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    /**
     * Returns Designation for third party user
     *
     * @return
     */
    public String getDesignationForThirdPartyUser(final boolean isForMobile) {
        String appConfigKey;
        if (isForMobile)
            appConfigKey = PT_WORKFLOWDESIGNATION_MOBILE;
        else
            appConfigKey = PROPERTYTAX_WORKFLOWDESIGNATION;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                appConfigKey);
        return null != appConfigValue ? appConfigValue.get(0).getValue() : null;
    }

    /**
     * Returns third party user roles
     *
     * @return
     */
    public List<AppConfigValues> getThirdPartyUserRoles() {

        final List<AppConfigValues> appConfigValueList = appConfigValuesService
                .getConfigValuesByModuleAndKey(PTMODULENAME, PROPERTYTAX_ROLEFORNONEMPLOYEE);

        return !appConfigValueList.isEmpty() ? appConfigValueList : null;

    }

    /**
     * @param fromDemand
     * @param toDemand
     * @return List of property having demand between fromDemand and toDemand
     */
    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByDemand(final String fromDemand, final String toDemand) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select pmv from PropertyMaterlizeView pmv where pmv.aggrCurrFirstHalfDmd is not null and pmv.aggrCurrFirstHalfDmd>=:fromDemand ")
                .append("and pmv.aggrCurrFirstHalfDmd<=:toDemand and pmv.isActive = true ");
		final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString())
				.setParameter("fromDemand", new BigDecimal(fromDemand))
				.setParameter("toDemand", new BigDecimal(toDemand));
        return (List<PropertyMaterlizeView>) query.list();
    }

    /**
     * @param assessmentNum,ownerName,doorNo
     * @return List of property matching the input params
     */
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByAssessmentAndOwnerDetails(final String assessmentNum,
            final String oldMuncipalNum, final String ownerName, final String doorNo) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select pmv from PropertyMaterlizeView pmv ").append(" where pmv.isActive = true ");
        if (assessmentNum != null && !assessmentNum.trim().isEmpty())
            queryStr.append(" and pmv.propertyId=:assessmentNum ");

        if (oldMuncipalNum != null && !oldMuncipalNum.trim().isEmpty())
            queryStr.append(" and pmv.oldMuncipalNum=:oldMuncipalNum ");

        if (ownerName != null && !ownerName.trim().isEmpty())
            queryStr.append(" and upper(trim(pmv.ownerName)) like :OwnerName ");
        if (doorNo != null && !doorNo.trim().isEmpty())
            queryStr.append(" and pmv.houseNo like :HouseNo ");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (assessmentNum != null && !assessmentNum.trim().isEmpty())
            query.setString("assessmentNum", assessmentNum);

        if (oldMuncipalNum != null && !oldMuncipalNum.trim().isEmpty())
            query.setString("oldMuncipalNum", oldMuncipalNum);

        if (doorNo != null && !doorNo.trim().isEmpty())
            query.setString(HOUSE_NO, doorNo + "%");
        if (ownerName != null && !ownerName.trim().isEmpty())
            query.setString(OWNER_NAME, "%" + ownerName.toUpperCase() + "%");

        return (List<PropertyMaterlizeView>) query.list();
    }

    /**
     * @param locationId
     * @param houseNo
     * @param ownerName
     * @return List of property matching the input params
     */
    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByLocation(final Integer locationId, final String houseNo,
            final String ownerName) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select pmv from PropertyMaterlizeView pmv ")
                .append(" where pmv.locality.id=:locationId and pmv.isActive = true ");
        if (houseNo != null && !houseNo.trim().isEmpty())
            queryStr.append("and pmv.houseNo like :HouseNo ");
        if (ownerName != null && !ownerName.trim().isEmpty())
            queryStr.append("and upper(trim(pmv.ownerName)) like :OwnerName");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        query.setLong("locationId", locationId);
        if (houseNo != null && !houseNo.trim().isEmpty())
            query.setString(HOUSE_NO, houseNo + "%");
        if (ownerName != null && !ownerName.trim().isEmpty())
            query.setString(OWNER_NAME, "%" + ownerName.toUpperCase() + "%");

        return (List<PropertyMaterlizeView>) query.list();
    }

    /**
     * @param zoneId
     * @param wardId
     * @param ownerName
     * @param houseNum
     * @return List of property matching the input params
     */
    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByBoundary(final Long zoneId, final Long wardId,
            final String ownerName, final String houseNum) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select pmv from PropertyMaterlizeView pmv, BasicPropertyImpl bp where pmv.basicPropertyID=bp.id ")
                .append("and bp.active='Y' ");
        if (null != zoneId && zoneId != -1)
            queryStr.append(" and pmv.zone.id=:ZoneID");
        if (null != wardId && wardId != -1)
            queryStr.append(" and pmv.ward.id=:WardID");
        if (houseNum != null && !houseNum.trim().isEmpty())
            queryStr.append(" and pmv.houseNo like :HouseNo ");
        if (ownerName != null && !ownerName.trim().isEmpty())
            queryStr.append(" and upper(trim(pmv.ownerName)) like :OwnerName");

        final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString());

        if (null != zoneId && zoneId != -1)
            query.setLong("ZoneID", zoneId);
        if (null != wardId && wardId != -1)
            query.setLong("WardID", wardId);
        if (houseNum != null && !houseNum.trim().isEmpty())
            query.setString(HOUSE_NO, houseNum + "%");
        if (ownerName != null && !ownerName.trim().isEmpty())
            query.setString(OWNER_NAME, "%" + ownerName.toUpperCase() + "%");

        return (List<PropertyMaterlizeView>) query.list();
    }

    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByDoorNo(final String doorNo) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(MV_QUERY);
        if (StringUtils.isNotBlank(doorNo))
            queryStr.append("and pmv.houseNo like :doorNo ");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(doorNo))
            query.setString("doorNo", doorNo + "%");
        return (List<PropertyMaterlizeView>) query.list();
    }

    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByOldMunicipalNo(final String oldMuncipalNum) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(MV_QUERY);
        if (StringUtils.isNotBlank(oldMuncipalNum))
            queryStr.append("and pmv.oldMuncipalNum=:oldMuncipalNum ");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(oldMuncipalNum))
            query.setString("oldMuncipalNum", oldMuncipalNum);
        return (List<PropertyMaterlizeView>) query.list();
    }

    public Map<String, Object> getOldMunicipalNumQuery(final String oldMuncipalNum) {
        final Map<String, Object> map = new HashMap<>();
        final String from = FROM_PROPERTY_MATERLIZE_VIEW_PMV;
        final String where = "where pmv.isActive = true and pmv.oldMuncipalNum = ?1 ";
        final StringBuilder search = new StringBuilder(SELECT_PMV);
        final StringBuilder count = new StringBuilder(SELECT_COUNT_PMV);
        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, Arrays.asList(oldMuncipalNum));
        return map;
    }

    public Map<String, Object> getAssessmentNumQuery(final String assessmentNumber) {
        final Map<String, Object> map = new HashMap<>();
        final String from = "from BasicPropertyImpl bp ";
        final String where = "where bp.upicNo = ?1 and bp.active='Y' ";
        final StringBuilder search = new StringBuilder("select bp ");
        final StringBuilder count = new StringBuilder("select count(bp) ");
        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, Arrays.asList(assessmentNumber));
        return map;
    }

    public Map<String, Object> getDoorNoQuery(final String doorNo) {
        final Map<String, Object> map = new HashMap<>();
        final String from = FROM_PROPERTY_MATERLIZE_VIEW_PMV;
        final String where = "where pmv.isActive = true and pmv.houseNo like ?1 ";
        final StringBuilder search = new StringBuilder(SELECT_PMV);
        final StringBuilder count = new StringBuilder(SELECT_COUNT_PMV);
        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, Arrays.asList(doorNo + "%"));
        return map;
    }

    public Map<String, Object> getMobileNumberQuery(final String mobileNumber) {
        final Map<String, Object> map = new HashMap<>();
        final String from = FROM_PROPERTY_MATERLIZE_VIEW_PMV;
        final String where = "where pmv.isActive = true and pmv.mobileNumber = ?1 ";
        final StringBuilder search = new StringBuilder(SELECT_PMV);
        final StringBuilder count = new StringBuilder(SELECT_COUNT_PMV);
        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, Arrays.asList(mobileNumber));
        return map;
    }

    public Map<String, Object> getBoundaryQuery(final Long zoneId, final Long wardId, final String ownerName,
            final String houseNum) {

        final Map<String, Object> map = new HashMap<>();
        final StringBuilder search = new StringBuilder(SELECT_PMV);
        final StringBuilder count = new StringBuilder(SELECT_COUNT_PMV);
        final String from = FROM_PROPERTY_MATERLIZE_VIEW_PMV;
        final StringBuilder where = new StringBuilder(WHERE_PMV_IS_ACTIVE_TRUE);

        final List params = new ArrayList();
        int i=0;
        if (null != zoneId && zoneId != -1) {
            where.append(" and pmv.zone.id = ?").append(++i);
            params.add(zoneId);
        }
        if (null != wardId && wardId != -1) {
            where.append(" and pmv.ward.id = ?").append(++i);
            params.add(wardId);
        }
        if (houseNum != null && !houseNum.trim().isEmpty()) {
            where.append(AND_PMV_HOUSE_NO_LIKE).append(++i);
            params.add(houseNum + "%");
        }
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            where.append(" and upper(trim(pmv.ownerName)) like ?").append(++i);
            params.add("%" + ownerName.toUpperCase() + "%");
        }

        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, params);
        return map;
    }

    public Map<String, Object> getLocationQuery(final Long locationId, final String houseNo, final String ownerName) {

        final Map<String, Object> map = new HashMap<>();
        final StringBuilder search = new StringBuilder(SELECT_PMV);
        final StringBuilder count = new StringBuilder(SELECT_COUNT_PMV);
        final String from = FROM_PROPERTY_MATERLIZE_VIEW_PMV;
        final StringBuilder where = new StringBuilder(WHERE_PMV_IS_ACTIVE_TRUE);

        final List params = new ArrayList();
        int i=0;
        if (null != locationId && locationId != -1) {
            where.append(" and pmv.locality.id = ?").append(++i);
            params.add(locationId);
        }
        if (houseNo != null && !houseNo.trim().isEmpty()) {
            where.append(AND_PMV_HOUSE_NO_LIKE).append(++i);
            params.add(houseNo + "%");
        }
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            where.append(" and upper(trim(pmv.ownerName)) like ?").append(++i);
            params.add("%" + ownerName.toUpperCase() + "%");
        }

        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, params);
        return map;
    }

    public Map<String, Object> getDemandQuery(final String fromDemand, final String toDemand) {

        final Map<String, Object> map = new HashMap<>();
        final StringBuilder search = new StringBuilder(SELECT_PMV);
        final StringBuilder count = new StringBuilder(SELECT_COUNT_PMV);
        final String from = FROM_PROPERTY_MATERLIZE_VIEW_PMV;
        final StringBuilder where = new StringBuilder(
                "where pmv.aggrCurrFirstHalfDmd is not null and pmv.aggrCurrFirstHalfDmd >= ?1 ")
                        .append("and pmv.aggrCurrFirstHalfDmd <= ?2 and pmv.isActive = true ");

        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, Arrays.asList(new BigDecimal(fromDemand), new BigDecimal(toDemand)));
        return map;
    }

    public Map<String, Object> getAssessmentAndOwnerDetailsQuery(final String oldMuncipalNum, final String ownerName,
            final String doorNo) {

        final Map<String, Object> map = new HashMap<>();
        final List params = new ArrayList();
        final StringBuilder search = new StringBuilder(SELECT_PMV);
        final StringBuilder count = new StringBuilder(SELECT_COUNT_PMV);
        final String from = FROM_PROPERTY_MATERLIZE_VIEW_PMV;
        final StringBuilder where = new StringBuilder(WHERE_PMV_IS_ACTIVE_TRUE);
        int i=0;
        if (StringUtils.isNotBlank(oldMuncipalNum)) {
            where.append(" and pmv.oldMuncipalNum = ?").append(++i);
            params.add(oldMuncipalNum);
        }
        if (StringUtils.isNotBlank(ownerName)) {
            where.append(" and upper(trim(pmv.ownerName)) like ?").append(++i);
            params.add("%" + ownerName.toUpperCase() + "%");
        }
        if (StringUtils.isNotBlank(doorNo)) {
            where.append(AND_PMV_HOUSE_NO_LIKE).append(++i);
            params.add(doorNo + "%");
        }

        map.put(SEARCH, search.append(from).append(where).toString());
        map.put(COUNT, count.append(from).append(where).toString());
        map.put(PARAMS, params);
        return map;
    }

    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByMobileNumber(final String MobileNo) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(MV_QUERY);
        if (StringUtils.isNotBlank(MobileNo))
            queryStr.append("and pmv.mobileNumber =:MobileNo ");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(MobileNo))
            query.setString("MobileNo", MobileNo);
        return (List<PropertyMaterlizeView>) query.list();
    }

    public Assignment getWorkflowInitiator(final PropertyImpl property) {
        Assignment wfInitiator = null;
        List<Assignment> assignment;
        if (isEmployee(property.getCreatedBy())) {
            if (isStateNotNull(property))
                wfInitiator = getWfInitiatorIfStateNotNull(property);
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId());
        } else if (property.getState().getInitiatorPosition() != null) {
            assignment = assignmentService.getAssignmentsForPosition(property.getState().getInitiatorPosition().getId(),
                    new Date());
            wfInitiator = getActiveAssignment(assignment);
            if (wfInitiator == null && !property.getStateHistory().isEmpty())
                wfInitiator = assignmentService
                        .getPrimaryAssignmentForPositon(property.getStateHistory().get(0).getOwnerPosition().getId());
        } else
            wfInitiator = assignmentService
                    .getPrimaryAssignmentForPositon(property.getState().getOwnerPosition().getId());
        return wfInitiator;
    }

    private Assignment getWfInitiatorIfStateNotNull(final PropertyImpl property) {
        List<Assignment> assignment;
        Assignment wfInitiator = null;
        final Assignment assgmnt = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(
                property.getCreatedBy(), property.getState().getInitiatorPosition());
        if (assgmnt != null && assgmnt.getEmployee().isActive())
            wfInitiator = assgmnt;
        if (wfInitiator == null) {
            assignment = assignmentService.getAssignmentsForPosition(property.getState().getInitiatorPosition().getId(),
                    new Date());
            wfInitiator = getActiveAssignment(assignment);
        }
        return wfInitiator;
    }

    private boolean isStateNotNull(final PropertyImpl property) {
        return property.getState() != null && property.getState().getInitiatorPosition() != null;
    }

    private Assignment getActiveAssignment(final List<Assignment> assignment) {
        Assignment wfInitiator = null;
        for (final Assignment assign : assignment)
            if (assign.getEmployee().isActive()) {
                wfInitiator = assign;
                break;
            }
        return wfInitiator;
    }

    public List<HashMap<String, Object>> populateHistory(final StateAware<Position> stateAware) {
        final List<HashMap<String, Object>> historyTable = new ArrayList<>();
        final HashMap<String, Object> map = new HashMap<>();
        User user;
        Position ownerPosition;
        if (stateAware.hasState()) {
            final State<Position> state = stateAware.getCurrentState();
            map.put("date", state.getLastModifiedDate());
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            map.put("comments", null != state.getComments() ? state.getComments() : "");
            user = state.getOwnerUser();
            ownerPosition = state.getOwnerPosition();
            if (null != ownerPosition) {
                final User approverUser = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != approverUser ? approverUser.getUsername() + "::" + approverUser.getName() : "");
            } else if (null != user)
                map.put("user", user.getUsername() + "::" + user.getName());
            historyTable.add(map);
            final List<StateHistory<Position>> stateHistory = stateAware.getStateHistory();
            if (!stateHistory.isEmpty()) {
                Collections.reverse(stateHistory);
                for (final StateHistory<Position> historyState : stateHistory) {
                    final HashMap<String, Object> workflowHistory = new HashMap<>();
                    workflowHistory.put("date", historyState.getLastModifiedDate());
                    workflowHistory.put("updatedBy", historyState.getLastModifiedBy().getUsername() + "::"
                            + historyState.getLastModifiedBy().getName());
                    workflowHistory.put("status", historyState.getValue());
                    workflowHistory.put("comments", null != historyState.getComments() ? historyState.getComments() : "");
                    ownerPosition = historyState.getOwnerPosition();
                    user = historyState.getOwnerUser();
                    if (null != ownerPosition) {
                        final User approverUser = eisCommonService.getUserForPosition(ownerPosition.getId(),
                                historyState.getLastModifiedDate());
                        workflowHistory.put("user",
                                null != approverUser ? approverUser.getUsername() + "::" + approverUser.getName() : "");
                    } else if (null != user)
                        workflowHistory.put("user", user.getUsername() + "::" + user.getName());
                    historyTable.add(workflowHistory);
                }
            }
        }
        return historyTable;
    }

    public AssessmentDetails loadAssessmentDetails(final BasicProperty basicProperty) {
        final AssessmentDetails assessmentDetail = new AssessmentDetails();
        assessmentDetail.setPropertyID(basicProperty.getUpicNo());
        if (basicProperty.getLatitude() != null && basicProperty.getLongitude() != null) {
            assessmentDetail.setLatitude(basicProperty.getLatitude());
            assessmentDetail.setLongitude(basicProperty.getLongitude());
        }
        assessmentDetail.setFlag(0);
        assessmentDetail.setHouseNo(basicProperty.getAddress().getHouseNoBldgApt());
        assessmentDetail.setPropertyAddress(basicProperty.getAddress().toString());
        final Property property = basicProperty.getProperty();
        final PropertyDetails propertyDetails = new PropertyDetails();
        assessmentDetail.setPropertyDetails(propertyDetails);
        PropertyDetail propertyDetail = null;
        if (property != null) {
            assessmentDetail.setOwnerNames(prepareOwnerInfo(property));
            propertyDetail = property.getPropertyDetail();
            loadPropertyDues(property, assessmentDetail);
        }
        if (null != propertyDetail) {
            assessmentDetail.setBoundaryDetails(prepareBoundaryInfo(basicProperty));
            assessmentDetail.getPropertyDetails().setPropertyType(propertyDetail.getPropertyTypeMaster().getType());
            if (propertyDetail.getPropertyUsage() != null)
                assessmentDetail.getPropertyDetails()
                        .setPropertyUsage(propertyDetail.getPropertyUsage().getUsageName());
            if (null != propertyDetail.getNoofFloors())
                assessmentDetail.getPropertyDetails().setNoOfFloors(propertyDetail.getNoofFloors());
            else
                assessmentDetail.getPropertyDetails().setNoOfFloors(0);
        }
        return assessmentDetail;

    }

    private Set<OwnerName> prepareOwnerInfo(final Property property) {
        final List<PropertyOwnerInfo> propertyOwners = property.getBasicProperty().getPropertyOwnerInfo();
        final Set<OwnerName> ownerNames = new HashSet<>(0);
        if (propertyOwners != null && !propertyOwners.isEmpty())
            for (final PropertyOwnerInfo propertyOwner : propertyOwners) {
                final OwnerName ownerName = new OwnerName();
                ownerName.setAadhaarNumber(propertyOwner.getOwner().getAadhaarNumber());
                ownerName.setOwnerName(propertyOwner.getOwner().getName());
                ownerName.setMobileNumber(propertyOwner.getOwner().getMobileNumber());
                ownerName.setEmailId(propertyOwner.getOwner().getEmailId());
                ownerNames.add(ownerName);
            }
        return ownerNames;
    }

    private BoundaryDetails prepareBoundaryInfo(final BasicProperty basicProperty) {
        final BoundaryDetails boundaryDetails = new BoundaryDetails();
        final PropertyID propertyID = basicProperty.getPropertyID();
        if (null != propertyID) {
            if (null != propertyID.getZone()) {
                boundaryDetails.setZoneId(propertyID.getZone().getId());
                boundaryDetails.setZoneNumber(propertyID.getZone().getBoundaryNum());
                boundaryDetails.setZoneName(propertyID.getZone().getName());
                boundaryDetails.setZoneBoundaryType(propertyID.getZone().getBoundaryType().getName());
            }
            if (null != propertyID.getWard()) {
                boundaryDetails.setWardId(propertyID.getWard().getId());
                boundaryDetails.setWardNumber(propertyID.getWard().getBoundaryNum());
                boundaryDetails.setWardName(propertyID.getWard().getName());
                boundaryDetails.setWardBoundaryType(propertyID.getWard().getBoundaryType().getName());
            }
            if (null != propertyID.getElectionBoundary()) {
                boundaryDetails.setAdminWardId(propertyID.getElectionBoundary().getId());
                boundaryDetails.setAdminWardNumber(propertyID.getElectionBoundary().getBoundaryNum());
                boundaryDetails.setAdminWardName(propertyID.getElectionBoundary().getName());
                boundaryDetails.setAdminWardBoundaryType(propertyID.getElectionBoundary().getBoundaryType().getName());
            }
            if (null != propertyID.getArea()) {
                boundaryDetails.setBlockId(propertyID.getArea().getId());
                boundaryDetails.setBlockNumber(propertyID.getArea().getBoundaryNum());
                boundaryDetails.setBlockName(propertyID.getArea().getName());
            }
            if (null != propertyID.getLocality()) {
                boundaryDetails.setLocalityId(propertyID.getLocality().getId());
                boundaryDetails.setLocalityName(propertyID.getLocality().getName());
            }
            if (null != propertyID.getStreet()) {
                boundaryDetails.setStreetId(propertyID.getStreet().getId());
                boundaryDetails.setStreetName(propertyID.getStreet().getName());
            }
        }
        return boundaryDetails;
    }

    private void loadPropertyDues(final Property property, final AssessmentDetails assessmentDetail) {
        final Map<String, BigDecimal> resultmap = ptDemandDAO.getDemandCollMap(property);
        if (null != resultmap && !resultmap.isEmpty()) {
            final BigDecimal currDmd = resultmap.get(CURR_FIRSTHALF_DMD_STR);
            final BigDecimal arrDmd = resultmap.get(ARR_DMD_STR);
            final BigDecimal currCollection = resultmap.get(CURR_FIRSTHALF_COLL_STR);
            final BigDecimal arrColelection = resultmap.get(ARR_COLL_STR);

            final BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrColelection);
            assessmentDetail.getPropertyDetails().setTaxDue(taxDue);
            assessmentDetail.getPropertyDetails().setCurrentTax(currDmd);
            assessmentDetail.getPropertyDetails().setArrearTax(arrDmd);
        }
    }

    public Map<String, BigDecimal> getCurrentPropertyTaxDetails(final Property propertyImpl) {
        return ptDemandDAO.getDemandCollMap(propertyImpl);
    }

    public Map<String, BigDecimal> getCurrentPropertyTaxDetailsIncludingPenalty(final Property propertyImpl) {
        return ptDemandDAO.getDemandCollMapIncludingPenaltyAndAdvance(propertyImpl);
    }

    /**
     * Returns a map of current tax and balance, based on passed date being in first half or second half of the installments for
     * current year
     *
     * @param propertyTaxDetails
     * @param currDate
     * @return
     */
    public Map<String, BigDecimal> getCurrentTaxAndBalance(final Map<String, BigDecimal> propertyTaxDetails,
            final Date currDate) {
        final Map<String, BigDecimal> taxValues = new HashMap<>();
        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
        final Installment currInstFirstHalf = currYearInstMap.get(CURRENTYEAR_FIRST_HALF);
        if (DateUtils.between(new Date(), currInstFirstHalf.getFromDate(), currInstFirstHalf.getToDate())) {
            taxValues.put(CURR_DMD_STR,
                    propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR));
            taxValues.put(CURR_BAL_STR,
                    propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                            .subtract(propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR)));
        } else {
            taxValues.put(CURR_DMD_STR,
                    propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR));
            taxValues.put(CURR_BAL_STR,
                    propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR)
                            .subtract(propertyTaxDetails.get(CURR_SECONDHALF_COLL_STR)));
        }
        return taxValues;
    }

    public Map<String, BigDecimal> getCurrentTaxAndBalanceIncludingPenalty(final Map<String, BigDecimal> propertyTaxDetails,
            final Date currDate) {
        final Map<String, BigDecimal> taxValues = new HashMap<>();
        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
        final Installment currInstFirstHalf = currYearInstMap.get(CURRENTYEAR_FIRST_HALF);
        if (DateUtils.between(new Date(), currInstFirstHalf.getFromDate(), currInstFirstHalf.getToDate())) {
            taxValues.put(CURR_DMD_STR,
                    propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR));
            taxValues.put(CURR_BAL_STR,
                    (propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                            .add(propertyTaxDetails.get(CURR_FIRSTHALF_PENALTY_DMD_STR)))
                                    .subtract((propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR))
                                            .add(propertyTaxDetails.get(CURR_FIRSTHALF_PENALTY_COLL_STR))));
        } else {
            taxValues.put(CURR_DMD_STR,
                    propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR));
            taxValues.put(CURR_BAL_STR,
                    (propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                            .add(propertyTaxDetails.get(CURR_FIRSTHALF_PENALTY_DMD_STR))
                            .add(propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR))
                            .add(propertyTaxDetails.get(CURR_SECONDHALF_PENALTY_DMD_STR)))
                                    .subtract((propertyTaxDetails.get(CURR_SECONDHALF_COLL_STR))
                                            .add(propertyTaxDetails.get(CURR_SECONDHALF_PENALTY_COLL_STR))
                                            .add(propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR))
                                            .add(propertyTaxDetails.get(CURR_FIRSTHALF_PENALTY_COLL_STR))));
        }
        return taxValues;
    }

    /**
     * Returns a map of current tax and balance, based on passed date being in first half or second half of the installments for
     * current year
     *
     * @param propertyTaxDetails
     * @param currDate
     * @return
     */
    public Map<String, BigDecimal> getCurrentTaxDetails(final Map<String, Map<String, BigDecimal>> propertyTaxDetails,
            final Date currDate) {
        final Map<String, BigDecimal> taxValues = new HashMap<>();
        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
        final Installment currInstFirstHalf = currYearInstMap.get(CURRENTYEAR_FIRST_HALF);
        if (DateUtils.between(new Date(), currInstFirstHalf.getFromDate(), currInstFirstHalf.getToDate()))
            getTaxDetails(propertyTaxDetails, taxValues, CURRENTYEAR_FIRST_HALF,
                    currInstFirstHalf);
        else
            getTaxDetails(propertyTaxDetails, taxValues, CURRENTYEAR_SECOND_HALF, null);
        return taxValues;
    }

    /**
     * Gives the tax details for the installment
     *
     * @param propertyTaxDetails
     * @param taxValues
     * @param installmentHalf
     * @param currInstFirstHalf
     */
    private void getTaxDetails(final Map<String, Map<String, BigDecimal>> propertyTaxDetails,
            final Map<String, BigDecimal> taxValues, final String installmentHalf,
            final Installment currInstFirstHalf) {
        if (currInstFirstHalf != null) {
            taxValues.put(CURR_DMD_STR,
                    propertyTaxDetails.get(installmentHalf).get(CURR_FIRSTHALF_DMD_STR));
            taxValues.put(CURR_COLL_STR,
                    propertyTaxDetails.get(installmentHalf).get(CURR_FIRSTHALF_COLL_STR));
            taxValues.put(CURR_BAL_STR,
                    propertyTaxDetails.get(installmentHalf).get(CURR_FIRSTHALF_DMD_STR).subtract(
                            propertyTaxDetails.get(installmentHalf).get(CURR_FIRSTHALF_COLL_STR)));
        } else {
            taxValues.put(CURR_DMD_STR,
                    propertyTaxDetails.get(installmentHalf).get(CURR_SECONDHALF_DMD_STR));
            taxValues.put(CURR_COLL_STR,
                    propertyTaxDetails.get(installmentHalf).get(CURR_SECONDHALF_COLL_STR));
            taxValues.put(CURR_BAL_STR,
                    propertyTaxDetails.get(installmentHalf).get(CURR_SECONDHALF_DMD_STR)
                            .subtract(propertyTaxDetails.get(installmentHalf)
                                    .get(CURR_SECONDHALF_COLL_STR)));
        }
        if (propertyTaxDetails.get(installmentHalf).get(DEMANDRSN_STR_GENERAL_TAX) != null)
            taxValues.put(DEMANDRSN_STR_GENERAL_TAX,
                    propertyTaxDetails.get(installmentHalf).get(DEMANDRSN_STR_GENERAL_TAX));
        else
            taxValues.put(DEMANDRSN_STR_VACANT_TAX,
                    propertyTaxDetails.get(installmentHalf).get(DEMANDRSN_STR_VACANT_TAX));
        taxValues.put(DEMANDRSN_STR_LIBRARY_CESS,
                propertyTaxDetails.get(installmentHalf).get(DEMANDRSN_STR_LIBRARY_CESS));
        taxValues.put(DEMANDRSN_STR_EDUCATIONAL_TAX,
                propertyTaxDetails.get(installmentHalf).get(DEMANDRSN_STR_EDUCATIONAL_TAX));
        taxValues.put(DEMANDRSN_STR_UNAUTHORIZED_PENALTY,
                propertyTaxDetails.get(installmentHalf).get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
        taxValues.put(ARR_DMD_STR,
                propertyTaxDetails.get(ARREARS).get(ARR_DMD_STR));
        taxValues.put(ARR_COLL_STR,
                propertyTaxDetails.get(ARREARS).get(ARR_COLL_STR));
        taxValues.put(ARR_BAL_STR,
                propertyTaxDetails.get(ARREARS).get(ARR_DMD_STR).subtract(
                        propertyTaxDetails.get(ARREARS).get(ARR_COLL_STR)));
    }

    /**
     * Calculates penalty for General Revision Petition
     *
     * @param modProperty
     * @param propCompletionDate
     */
    public void calculateGrpPenalty(final Property modProperty, final Date propCompletionDate) {
        currentInstall = propertyTaxCommonUtils.getCurrentInstallment();
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        EgDemand currentDemand = null;
        for (final EgDemand egDemand : modProperty.getPtDemandSet())
            if (egDemand.getEgInstallmentMaster().equals(currentInstall)) {
                currentDemand = egDemand;
                break;
            }

        final Map<Installment, BigDecimal> installmentWiseDemand = getInstallmentWiseDemand(currentDemand);
        final Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail = penaltyCalculationService
                .getInstallmentWisePenaltyDemandDetails(null, currentDemand);
        final List<EgDemandDetails> penaltyList = new ArrayList<>();
        final DateTime nagarPanchayatPenDate = DateTime.now().withDate(2016, 1, 1);
        final Installment nagarPanchayatPenEndInstallment = installmentDao.getInsatllmentByModuleForGivenDate(module,
                nagarPanchayatPenDate.toDate());
        BigDecimal tax;
        Installment installment;
        BigDecimal excessPenalty = BigDecimal.ZERO;
        for (final Map.Entry<Installment, BigDecimal> mapEntry : installmentWiseDemand.entrySet()) {
            installment = mapEntry.getKey();
            if (installment.getToDate().compareTo(propCompletionDate) >= 0) {
                tax = mapEntry.getValue();
                final EgDemandDetails existingPenaltyDemandDetail = installmentWisePenaltyDemandDetail.get(installment);
                Date penaltyEffectiveDate;
                if (propertyTaxUtil.checkIsNagarPanchayat()
                        && installment.compareTo(nagarPanchayatPenEndInstallment) <= 0)
                    penaltyEffectiveDate = nagarPanchayatPenDate.toDate();
                else
                    penaltyEffectiveDate = getPenaltyEffectiveDate(installment);
                if (penaltyEffectiveDate.before(new Date())) {
                    final BigDecimal penaltyAmount = calculatePenalty(null, penaltyEffectiveDate, tax);
                    if (existingPenaltyDemandDetail == null) {
                        final EgDemandDetails penaltyDemandDetails = ptBillServiceImpl
                                .insertDemandDetails(DEMANDRSN_CODE_PENALTY_FINES, penaltyAmount, installment);
                        penaltyList.add(penaltyDemandDetails);
                    } else {
                        if (existingPenaltyDemandDetail.getAmtCollected().compareTo(penaltyAmount) > 0) {
                            excessPenalty = existingPenaltyDemandDetail.getAmtCollected().subtract(penaltyAmount);
                            existingPenaltyDemandDetail.setAmtCollected(penaltyAmount);
                        }
                        existingPenaltyDemandDetail.setAmount(penaltyAmount);
                    }
                }
            }
        }
        currentDemand.getEgDemandDetails().addAll(penaltyList);
        final List<Installment> installments = new ArrayList<>(installmentWiseDemand.keySet());
        Collections.sort(installments);
        if (excessPenalty.compareTo(BigDecimal.ZERO) > 0)
            adjustExcessPenalty(currentDemand, installments, excessPenalty);
    }

    /**
     * Adjusts excess penalty collection amount
     *
     * @param currentDemand
     * @param installments
     * @param excessPenalty
     */
    private void adjustExcessPenalty(final EgDemand currentDemand, final List<Installment> installments,
            BigDecimal excessPenalty) {
        final List<EgDemandDetails> demandDetailsList = new ArrayList<>(currentDemand.getEgDemandDetails());
        final Map<Installment, Set<EgDemandDetails>> installmentWiseDemandDetails = getEgDemandDetailsSetAsMap(
                demandDetailsList, installments);
        for (final Installment installment : installments) {
            final Set<EgDemandDetails> demandDetailsSet = installmentWiseDemandDetails.get(installment);
            for (final String demandReason : DEMAND_REASONS) {
                final EgDemandDetails demandDetails = getEgDemandDetailsForReason(demandDetailsSet, demandReason);
                if (demandDetails != null) {
                    final BigDecimal balance = propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetails)
                            .subtract(demandDetails.getAmtCollected());
                    if (balance.compareTo(BigDecimal.ZERO) > 0)
                        if (excessPenalty.compareTo(BigDecimal.ZERO) > 0 && excessPenalty.compareTo(balance) <= 0) {
                            demandDetails.setAmtCollected(demandDetails.getAmtCollected().add(excessPenalty));
                            demandDetails.setModifiedDate(new Date());
                            excessPenalty = BigDecimal.ZERO;
                        } else {
                            demandDetails.setAmtCollected(demandDetails.getAmtCollected().add(balance));
                            demandDetails.setModifiedDate(new Date());
                            excessPenalty = excessPenalty.subtract(balance);
                        }
                }
                if (excessPenalty.compareTo(BigDecimal.ZERO) == 0)
                    break;
            }
            if (excessPenalty.compareTo(BigDecimal.ZERO) == 0)
                break;
        }
        if (excessPenalty.compareTo(BigDecimal.ZERO) > 0) {
            final EgDemandDetails advanceDemandDetails = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_ADVANCE,
                    excessPenalty, currentInstall);
            currentDemand.getEgDemandDetails().add(advanceDemandDetails);
        }
    }

    /**
     * Returns installment wise demand amount
     *
     * @param currentDemand
     * @return
     */
    public Map<Installment, BigDecimal> getInstallmentWiseDemand(final EgDemand currentDemand) {
        final Map<Installment, BigDecimal> installmentWiseDemand = new TreeMap<>();
        String demandReason = "";
        Installment installment = null;
        final List<String> demandReasonExcludeList = Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
                DEMANDRSN_CODE_ADVANCE);
        for (final EgDemandDetails dmdDet : currentDemand.getEgDemandDetails()) {
            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();
            if (!demandReasonExcludeList.contains(demandReason)) {
                installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                if (installmentWiseDemand.get(installment) == null)
                    installmentWiseDemand.put(installment, propertyTaxCommonUtils.getTotalDemandVariationAmount(dmdDet));
                else
                    installmentWiseDemand.put(installment,
                            installmentWiseDemand.get(installment)
                                    .add(propertyTaxCommonUtils.getTotalDemandVariationAmount(dmdDet)));

            }
        }
        return installmentWiseDemand;
    }

    /**
     * Calculates penalty on tax amount
     *
     * @param latestCollReceiptDate
     * @param fromDate
     * @param amount
     * @return
     */
    public BigDecimal calculatePenalty(final Date latestCollReceiptDate, final Date fromDate, final BigDecimal amount) {
        BigDecimal penalty;
        final int noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(fromDate, new Date());
        penalty = amount.multiply(PENALTY_PERCENTAGE.multiply(new BigDecimal(noOfMonths)))
                .divide(BIGDECIMAL_100);
        return MoneyUtils.roundOff(penalty);
    }

    /**
     * Returns penalty effective date for an installment
     *
     * @param installment
     * @return
     */
    private Date getPenaltyEffectiveDate(final Installment installment) {
        return penalyDateWithThreeMonths(installment.getFromDate());
    }

    /**
     * Returns date by adding three months
     *
     * @param date
     * @return
     */
    private Date penalyDateWithThreeMonths(final Date date) {
        final Calendar penalyDate = Calendar.getInstance();
        penalyDate.setTime(date);
        penalyDate.add(Calendar.MONTH, 3);
        penalyDate.set(Calendar.DAY_OF_MONTH, 1);
        return penalyDate.getTime();
    }

    /**
     * Updates the PropertyDetail for a Property
     *
     * @param property
     * @param floorTypeId
     * @param roofTypeId
     * @param wallTypeId
     * @param woodTypeId
     * @param areaOfPlot
     * @param propertyCategory
     * @param nonResPlotArea
     * @param propUsageId
     * @param propOccId
     * @param propTypeId
     */
    public void updatePropertyDetail(final Property property, final Long floorTypeId, final Long roofTypeId,
            final Long wallTypeId, final Long woodTypeId, final String areaOfPlot, final String propertyCategory,
            final String nonResPlotArea, final String propUsageId, final String propOccId, final String propTypeId) {
        final PropertyDetail propertyDetail = property.getPropertyDetail();
        if (floorTypeId != null && floorTypeId != -1) {
            final FloorType floorType = floorTypeService.getFloorTypeById(floorTypeId);
            propertyDetail.setFloorType(floorType);
        }
        if (roofTypeId != null && roofTypeId != -1) {
            final RoofType roofType = roofTypeService.getRoofTypeById(roofTypeId);
            propertyDetail.setRoofType(roofType);
        }
        if (wallTypeId != null && wallTypeId != -1) {
            final WallType wallType = wallTypeService.getWallTypeById(wallTypeId);
            propertyDetail.setWallType(wallType);
        }
        if (woodTypeId != null && woodTypeId != -1) {
            final WoodType woodType = woodTypeService.getWoodTypeById(woodTypeId);
            propertyDetail.setWoodType(woodType);
        }
        if (areaOfPlot != null && !areaOfPlot.isEmpty())
            propertyDetail.getSitalArea().setArea(new Float(areaOfPlot));
        propertyDetail.setCategoryType(propertyDetail.getCategoryType());

        if (propertyDetail.getApartment() != null && propertyDetail.getApartment().getId() != null) {
			final Apartment apartment = apartmentService
					.getApartmentById(property.getPropertyDetail().getApartment().getId());
            propertyDetail.setApartment(apartment);
        }

        if (nonResPlotArea != null && !nonResPlotArea.isEmpty())
            propertyDetail.getNonResPlotArea().setArea(new Float(nonResPlotArea));

        propertyDetail.setFieldVerified('Y');
        propertyDetail.setProperty(property);
		final PropertyMutationMaster propMutMstr = propertyMutationMasterDAO
				.getPropertyMutationMasterByCode(property.getBasicProperty().getPropertyMutationMaster().getCode());
		final PropertyTypeMaster propTypeMstr = propertyTypeMasterDAO.findById(Long.valueOf(propTypeId), false);
        if (propUsageId != null) {
			final PropertyUsage usage = propertyUsageDAO.findById(Long.valueOf(propUsageId), false);
			property.getPropertyDetail().setPropertyUsage(usage);
			propertyDetail.setPropertyUsage(usage);
        }
        if (propOccId != null) {
            final PropertyOccupation occupancy = propertyOccupationDAO.findById(Long.valueOf(propOccId), false);
            propertyDetail.setPropertyOccupation(occupancy);
        }
        if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            propertyDetail.setPropertyType(VACANT_PROPERTY);
        else
            propertyDetail.setPropertyType(BUILT_UP_PROPERTY);

        propertyDetail.setPropertyTypeMaster(propTypeMstr);
        propertyDetail.setPropertyMutationMaster(propMutMstr);
        propertyDetail.setUpdatedTime(new Date());

        if (propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            propertyDetail.setNoofFloors(0);
            if (!property.getPropertyDetail().getFloorDetails().isEmpty())
                property.getPropertyDetail().getFloorDetails().clear();
            property.getPropertyDetail().getTotalBuiltupArea().setArea(0F);
        }
    }

    /**
     * Update the Floor details for a property
     *
     * @param property
     * @param savedFloorDetails
     */
    public void updateFloorDetails(final Property property, final List<Floor> savedFloorDetails) {
        PropertyTypeMaster unitType = null;
        PropertyUsage usage = null;
        PropertyOccupation occupancy = null;
        StructureClassification structureClass = null;
        final Area totBltUpArea = new Area();
        Float totBltUpAreaVal = 0F;
        for (final Floor floorProxy : property.getPropertyDetail().getFloorDetailsProxy())
            for (final Floor savedFloor : savedFloorDetails) {
                if (floorProxy != null && savedFloor != null && floorProxy.getFloorUid().equals(savedFloor.getFloorUid())) {
                    totBltUpAreaVal = totBltUpAreaVal + floorProxy.getBuiltUpArea().getArea();
                    // set all fields for each floor, if UID matches
                    if (floorProxy.getUnitType() != null)
						unitType = propertyTypeMasterDAO.findById(floorProxy.getUnitType().getId(), false);
                    if (floorProxy.getPropertyUsage() != null)
                        usage = propertyUsageDAO.findById(floorProxy.getPropertyUsage().getId(), false);
                    if (floorProxy.getPropertyOccupation() != null)
                        occupancy = propertyOccupationDAO.findById(floorProxy.getPropertyOccupation().getId(), false);
					if (floorProxy.getStructureClassification() != null)
						structureClass = structureClassificationService
								.findOne(floorProxy.getStructureClassification().getId());
                    if (floorProxy.getOccupancyDate() != null && floorProxy.getConstructionDate() != null)
                        savedFloor.setDepreciationMaster(propertyTaxUtil.getDepreciationByDate(
                                floorProxy.getConstructionDate(), floorProxy.getOccupancyDate()));

                    if (unitType != null
                            && unitType.getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT))
                        savedFloor.setFloorNo(OPEN_PLOT_UNIT_FLOORNUMBER);

                    savedFloor.setUnitType(unitType);
                    savedFloor.setPropertyUsage(usage);
                    savedFloor.setPropertyOccupation(occupancy);
                    savedFloor.setStructureClassification(structureClass);
                    savedFloor.setPropertyDetail(property.getPropertyDetail());
                    savedFloor.setModifiedDate(new Date());
                    final User user = userService.getUserById(ApplicationThreadLocals.getUserId());
                    savedFloor.setModifiedBy(user);
                    savedFloor.getBuiltUpArea().setArea(floorProxy.getBuiltUpArea().getArea());
                    savedFloor.getBuiltUpArea().setLength(floorProxy.getBuiltUpArea().getLength());
                    savedFloor.getBuiltUpArea().setBreadth(floorProxy.getBuiltUpArea().getLength());
                    savedFloor.setFirmName(floorProxy.getFirmName());
                    // setting total builtup area.
                    totBltUpArea.setArea(totBltUpAreaVal);
                    totBltUpArea.setLength(floorProxy.getBuiltUpArea().getLength());
                    totBltUpArea.setBreadth(floorProxy.getBuiltUpArea().getBreadth());
                    property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);

                }
                property.getPropertyDetail().setNoofFloors(property.getPropertyDetail().getFloorDetailsProxy().size());
            }
    }

    /**
     * Convert string to date
     *
     * @param dateInString
     * @return
     * @throws ParseException
     */
    public Date convertStringToDate(final String dateInString) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(dateInString);
    }

    /**
     * Fetch the assignments for the designation
     *
     * @param designationName
     * @return
     */
    public List<Assignment> getAssignmentsForDesignation(final String designationName) {
        return assignmentService.findPrimaryAssignmentForDesignationName(designationName);
    }

    /**
     * Update Reference Basic Property in Property Status values (Bifurcation workflow)
     *
     * @param basicProperty, parentPropId
     */
    public void updateReferenceBasicProperty(final BasicProperty basicProperty, final String parentPropId) {

		final PropertyStatusValues propStatVal = (PropertyStatusValues) propertyStatusValuesDAO
				.getPropertyStatusValuesByBasicProperty(basicProperty);
        final BasicProperty referenceBasicProperty = basicPropertyDAO.getInActiveBasicPropertyByPropertyID(parentPropId);
        if (referenceBasicProperty != null)
            propStatVal.setReferenceBasicProperty(referenceBasicProperty);
    }

    /**
     * Returns Water connection details of an assessment
     *
     * @param assessmentNo
     * @param request
     * @return
     */
    public List<Map<String, Object>> getWCDetails(final String assessmentNo, final HttpServletRequest request) {
        final List<Map<String, Object>> waterConnDtls = new ArrayList<>();
        final String url = request.getRequestURL().toString();
        final String uri = request.getRequestURI();
        final String host = url.substring(0, url.indexOf(uri));
        final String wtmsRestURL = String.format(WTMS_CONNECT_DTLS_RESTURL, host, assessmentNo);
        final String dtls = simpleRestClient.getRESTResponse(wtmsRestURL);
        JSONArray jsonArr = null;
        final ArrayList<String> nameList = new ArrayList<>();
        try {
            jsonArr = new JSONArray(dtls);
        } catch (final JSONException e1) {
            LOGGER.error("Error in converting string into json array " + e1);
        }

        for (int i = 0; i < jsonArr.length(); i++)
            try {
                final Map<String, Object> newMap = new HashMap<>();
                final org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
                final JSONArray names = jsonObj.names();
                if (names != null)
                    for (int n = 0; n < names.length(); n++)
                        nameList.add(names.get(n).toString());
                for (final String str : nameList)
                    if (!"propertyID".equals(str))
                        newMap.put(str, jsonObj.get(str).toString());
                waterConnDtls.add(newMap);
            } catch (final JSONException e) {
                LOGGER.error("Error in converting json array into json object " + e);
            }
        return waterConnDtls;
    }

    /**
     * Method to get total property tax due
     *
     * @param basicProperty
     * @return Total property tax due
     */
    @ReadOnly
    public BigDecimal getTotalPropertyTaxDue(final BasicProperty basicProperty) {
        final Map<String, BigDecimal> propertyTaxDetails = ptDemandDAO.getDemandCollMap(basicProperty.getProperty());
        final Map<String, BigDecimal> currentTaxAndDue = getCurrentTaxAndBalance(propertyTaxDetails, new Date());
        final BigDecimal currentPropertyTaxDue = currentTaxAndDue.get(CURR_BAL_STR);
        final BigDecimal arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR)
                .subtract(propertyTaxDetails.get(ARR_COLL_STR));
        return currentPropertyTaxDue.add(arrearPropertyTaxDue);
    }

    public Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays.asList(files).stream().filter(file -> !file.isEmpty()).map(file -> {
                try {
                    return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                            file.getContentType(), FILESTORE_MODULE_NAME);
                } catch (final Exception e) {
                    throw new ApplicationRuntimeException("err.input.stream", e);
                }
            }).collect(Collectors.toSet());
        else
            return Collections.emptySet();
    }

    public BigDecimal getTotalPropertyTaxDueIncludingPenalty(final BasicProperty basicProperty) {
        final Map<String, BigDecimal> propertyTaxDetails = ptDemandDAO
                .getDemandIncludingPenaltyCollMap(basicProperty.getProperty());
        final BigDecimal crntFirstHalfTaxDue = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                .subtract(propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR));
        final BigDecimal crntSecondHalfTaxDue = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR)
                .subtract(propertyTaxDetails.get(CURR_SECONDHALF_COLL_STR));
        final BigDecimal arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR)
                .subtract(propertyTaxDetails.get(ARR_COLL_STR));
        return crntFirstHalfTaxDue.add(crntSecondHalfTaxDue).add(arrearPropertyTaxDue);
    }

    /**
     * Method to get children created for property
     *
     * @param basicProperty
     * @return List<PropertyStatusValues>
     */
    public List<PropertyStatusValues> findChildrenForProperty(final BasicProperty basicProperty) {
        return propertyStatusValuesDAO.getPropertyStatusValuesByReferenceBasicProperty(basicProperty);
    }

    public Assignment getUserOnRejection(final StateAware stateAware) {
        final List<StateHistory> history = stateAware.getStateHistory();
        Collections.reverse(history);
        Assignment userAssignment = null;
        boolean exists = false;
        for (final StateHistory state : history) {
            final List<Assignment> assignments = assignmentService
                    .getAssignmentsForPosition(state.getOwnerPosition().getId());
            for (final Assignment assignment : assignments)
                if (assignment != null
                        && assignment.getDesignation().getName().equals(REVENUE_INSPECTOR_DESGN)
                        && assignment.getEmployee().isActive()) {
                    userAssignment = assignment;
                    exists = true;
                    break;
                }
            if (exists)
                break;
        }
        return userAssignment;
    }

    public String getDesignationForPositionAndUser(final Long positionId, final Long userId) {
        final List<Assignment> assignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(positionId, userId,
                new Date());
        return !assignment.isEmpty() ? assignment.get(0).getDesignation().getName() : null;
    }

    @Transactional
    public void saveDocumentTypeDetails(final BasicProperty basicProperty,
            final DocumentTypeDetails documentTypeDetails) {
        documentTypeDetails.setBasicPropertyId(basicProperty.getId());
        documentTypeDetailsService.persist(documentTypeDetails);
    }

    @Transactional
    public void updateDocumentTypeDetails(final BasicProperty basicProperty,
            final DocumentTypeDetails documentTypeDetails) {
        documentTypeDetails.setBasicPropertyId(basicProperty.getId());
        documentTypeDetailsService.update(documentTypeDetails);
    }

    public void clearOldDocumentAttachments(final List<Document> documents,
            final DocumentTypeDetails documentTypeDetails) {
        final List<DocumentType> excludedDocumentTypes = excludeOldDocumentAttachments(documentTypeDetails);
        documents.forEach(document -> {
            if (excludedDocumentTypes.contains(document.getType()) && !document.getFiles().isEmpty())
                document.getFiles().clear();
        });
    }

    public List<DocumentType> excludeOldDocumentAttachments(final DocumentTypeDetails documentTypeDetails) {
        final List<DocumentType> documentTypes = getDocumentTypesForTransactionType(TransactionType.CREATE_ASMT_DOC);
        final Iterator<DocumentType> documentTypeIterator = documentTypes.iterator();
        while (documentTypeIterator.hasNext()) {
            final DocumentType dt = documentTypeIterator.next();
            if (documentTypeDetails.getDocumentName().equals(DOCUMENT_NAME_PATTA_CERTIFICATE)) {
                if (dt.getName().equals(DOCUMENT_TYPE_PATTA_CERTIFICATE)
                        || dt.getName().equals(DOCUMENT_TYPE_MRO_PROCEEDINGS))
                    documentTypeIterator.remove();
            } else if (documentTypeDetails.getDocumentName()
                    .equals(DOCUMENT_NAME_REGD_WILL_DOCUMENT)
                    || documentTypeDetails.getDocumentName()
                            .equals(DOCUMENT_NAME_UNREGD_WILL_DOCUMENT)) {
                if (dt.getName().equals(DOCUMENT_TYPE_WILL_DEED))
                    documentTypeIterator.remove();
            } else if (documentTypeDetails.getDocumentName()
                    .equals(DOCUMENT_NAME_DECREE_BY_CIVILCOURT)) {
                if (dt.getName().equals(DOCUMENT_TYPE_DECREE_DOCUMENT))
                    documentTypeIterator.remove();
            } else if (documentTypeDetails.getDocumentName().equals(DOCUMENT_NAME_REGD_DOCUMENT)) {
                if (dt.getName().equals(DOCUMENT_TYPE_REGD_DOCUMENT))
                    documentTypeIterator.remove();
            } else if (dt.getName().equals(DOCUMENT_TYPE_PHOTO_PROPERTY_HOLDER))
                documentTypeIterator.remove();
        }
        return documentTypes;

    }

    public void amalgamateWaterConnections(final String assessmentNo, final List<String> childAssessments,
            final HttpServletRequest request) {
        final RestTemplate restTemplate = new RestTemplate();
        final String wtmsRestURL = format(WTMS_AMALGAMATE_WATER_CONNECTIONS_URL,
                WebUtils.extractRequestDomainURL(request, false));
        final AmalgamateWaterConnection amalgamateWaterConnection = new AmalgamateWaterConnection(assessmentNo);
        amalgamateWaterConnection.setChildAssessmentNumber(childAssessments);
        restTemplate.postForObject(wtmsRestURL, amalgamateWaterConnection, AmalgamateWaterConnection.class);

    }

    public boolean isDuplicateDoorNumber(final String houseNo, final BasicProperty basicProperty) {
        final Query basicPropByDoorNo = entityManager.unwrap(Session.class).createQuery(
                "from BasicPropertyImpl bp where bp.address.houseNoBldgApt = :houseNo  and bp.active = 'Y' and bp.upicNo is not null");
        basicPropByDoorNo.setParameter("houseNo", houseNo);
        // this condition is required because, after rejection the validation shouldn't happen for the same houseNo
        return !basicPropByDoorNo.list().isEmpty()
                && (basicProperty == null || !(basicProperty.getAddress().getHouseNoBldgApt() == null ? ""
                        : basicProperty.getAddress().getHouseNoBldgApt()).equals(houseNo));
    }

    public void copyCollection(final PropertyImpl oldProperty, final PropertyImpl newProperty) {
        BigDecimal totalColl = BigDecimal.ZERO;

        final Ptdemand ptDemandOld = getCurrrentDemand(oldProperty);
        final Ptdemand ptDemandNew = getCurrrentDemand(newProperty);

        /**
         * CHANGE START
         *
         * COPY REBATE COLUMN WHILE COPYING MODIFIED AS PART OF PROPERTY TAX WAIVER GO 53
         */

        HashMap<Installment, HashMap<String, BigDecimal>> threeWayMap = new HashMap<>();

        /**
         * CHANGE END
         */

        if (ptDemandOld != null) {

            for (final EgDemandDetails dmdDetails : ptDemandOld.getEgDemandDetails()) {
                totalColl = totalColl.add(dmdDetails.getAmtCollected());
            }

            /**
             * CHANGE START
             *
             * COPY REBATE COLUMN WHILE COPYING MODIFIED AS PART OF PROPERTY TAX WAIVER GO 53
             */
            final Map<Installment, Set<EgDemandDetails>> installmentWiseDemandDetailsOld = getEgDemandDetailsSetByInstallment(
                    ptDemandOld.getEgDemandDetails());
            final List<Installment> installments = new ArrayList<>(installmentWiseDemandDetailsOld.keySet());
            Collections.sort(installments);

            for (final Installment installment : installments) {
                for (final String demandReason : DEMAND_REASONS) {
                    final EgDemandDetails oldDemandDetail = getEgDemandDetailsForReason(
                            installmentWiseDemandDetailsOld.get(installment), demandReason);
                    if (oldDemandDetail == null) {
                        continue;
                    }
                    LOGGER.debug(format("penalty.waiver installment: %s, oldDemandDetail: %s", installment, oldDemandDetail));
                    if (oldDemandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equals(DEMANDRSN_CODE_PENALTY_FINES)) {
                        HashMap<String, BigDecimal> hm = new HashMap<>();
                        hm.put(demandReason, oldDemandDetail.getAmtRebate());
                        threeWayMap.put(installment, hm);
                    }
                }
            }
            LOGGER.debug(format("penalty.waiver threeWayMap: %s", threeWayMap));

            /**
             * CHANGE END
             */

        }

        if (ptDemandNew != null) {
            final Map<Installment, Set<EgDemandDetails>> installmentWiseDemandDetails = getEgDemandDetailsSetByInstallment(
                    ptDemandNew.getEgDemandDetails());
            final List<Installment> installments = new ArrayList<>(installmentWiseDemandDetails.keySet());
            Collections.sort(installments);

            for (final Installment installment : installments) {
                for (final String demandReason : DEMAND_REASONS) {
                    final EgDemandDetails newDemandDetail = getEgDemandDetailsForReason(
                            installmentWiseDemandDetails.get(installment), demandReason);
                    totalColl = updateCollection(totalColl, newDemandDetail);

                    /**
                     * CHANGE START
                     *
                     * COPY REBATE COLUMN WHILE COPYING MODIFIED AS PART OF PROPERTY TAX WAIVER GO 53
                     */
                    LOGGER.debug(format(
                            "penalty.waiver installment: %s, demandReason: %s, threeWayMap.get(installment): %s",
                            installment,
                            demandReason,
                            threeWayMap.get(installment)));
                    if (newDemandDetail != null && demandReason.equals(DEMANDRSN_CODE_PENALTY_FINES)
                            && threeWayMap.get(installment) != null) {
                        BigDecimal amtWaivedOff = threeWayMap.get(installment).get(demandReason);
                        newDemandDetail.setAmtRebate(amtWaivedOff);
                    }
                    /**
                     * CHANGE END
                     */
                }
            }
            if (totalColl.compareTo(BigDecimal.ZERO) > 0) {
                final Installment currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                        .get(CURRENTYEAR_SECOND_HALF);
                final EgDemandDetails advanceDemandDetails = ptBillServiceImpl.getDemandDetail(ptDemandNew, currSecondHalf,
                        DEMANDRSN_CODE_ADVANCE);
                if (advanceDemandDetails == null) {
                    final EgDemandDetails dmdDetails = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_ADVANCE,
                            totalColl, currSecondHalf);
                    ptDemandNew.getEgDemandDetails().add(dmdDetails);
                } else
                    advanceDemandDetails.setAmtCollected(totalColl);
            }
        }
    }

    /**
     * @param totalColl
     * @param newDemandDetail
     * @return
     */
    private BigDecimal updateCollection(BigDecimal totalColl, final EgDemandDetails newDemandDetail) {
        BigDecimal remaining = totalColl;
        if (newDemandDetail != null) {
            newDemandDetail.setAmtCollected(ZERO);
            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                if (remaining.compareTo(propertyTaxCommonUtils.getTotalDemandVariationAmount(newDemandDetail)) <= 0) {
                    newDemandDetail.setAmtCollected(remaining);
                    newDemandDetail.setModifiedDate(new Date());
                    remaining = BigDecimal.ZERO;
                } else {
                    newDemandDetail.setAmtCollected(propertyTaxCommonUtils.getTotalDemandVariationAmount(newDemandDetail));
                    newDemandDetail.setModifiedDate(new Date());
                    remaining = remaining.subtract(propertyTaxCommonUtils.getTotalDemandVariationAmount(newDemandDetail));
                }
            }
        }
        return remaining;
    }

    /**
     * Method to push data for citizen portal inbox
     */

    @Transactional
    public void pushPortalMessage(final StateAware stateAware, final String applictionType) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final PropertyImpl property = (PropertyImpl) stateAware;
        final BasicProperty basicProperty = property.getBasicProperty();
        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                property.getPropertyModifyReason() + " " + module.getDisplayName(), property.getApplicationNo(),
                basicProperty.getUpicNo(), basicProperty.getId(), property.getPropertyModifyReason(),
                getDetailedMessage(stateAware, applictionType),
                format(APPLICATION_VIEW_URL, property.getApplicationNo(), applictionType), isResolved(property),
                basicProperty.getStatus().getName(), getSlaEndDate(applictionType), property.getState(),
                Arrays.asList(securityUtils.getCurrentUser()));
        final PortalInbox portalInbox = portalInboxBuilder.build();
        portalInboxService.pushInboxMessage(portalInbox);
    }

    /**
     * Method to update data for citizen portal inbox
     */
    @Transactional
    public void updatePortalMessage(final StateAware stateAware, final String applictionType) {
        final PropertyImpl property = (PropertyImpl) stateAware;
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final BasicProperty basicProperty = property.getBasicProperty();
        portalInboxService.updateInboxMessage(property.getApplicationNo(), module.getId(),
                property.getState().getValue(), isResolved(property), getSlaEndDate(applictionType),
                property.getState(), null, basicProperty.getUpicNo(),
                format(APPLICATION_VIEW_URL, property.getApplicationNo(), applictionType));
    }

    private Date getSlaEndDate(final String applicationType) {
        final DateTime dt = new DateTime(new Date());
        return dt.plusDays(getSlaValue(applicationType)).toDate();
    }

    private String getDetailedMessage(final StateAware stateAware, final String applictionType) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final StringBuilder detailedMessage = new StringBuilder();
        if (!applictionType.isEmpty() && propertyApplicationTypes().contains(applictionType)) {
            final PropertyImpl property = (PropertyImpl) stateAware;
            detailedMessage.append(APPLICATION_NO).append(property.getApplicationNo()).append(REGARDING)
                    .append(property.getPropertyModifyReason() + " " + module.getDisplayName()).append(" in ")
                    .append(property.getBasicProperty().getStatus().getName()).append(STATUS);
        } else if (!applictionType.isEmpty() && (applictionType.equalsIgnoreCase(APPLICATION_TYPE_REVISION_PETITION)
                || applictionType.equalsIgnoreCase(APPLICATION_TYPE_GRP)
                || applictionType.equalsIgnoreCase(APPLICATION_TYPE_APPEAL_PETITION))) {
            final Petition petition = (Petition) stateAware;
            detailedMessage.append(APPLICATION_NO).append(petition.getObjectionNumber()).append(REGARDING)
                    .append(petition.getType() + " " + module.getDisplayName()).append(" in ")
                    .append(petition.getBasicProperty().getStatus().getName()).append(STATUS);
        } else if (!applictionType.isEmpty()
                && applictionType.equalsIgnoreCase(APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP)) {
            final PropertyMutation propertyMutation = (PropertyMutation) stateAware;
            detailedMessage.append(APPLICATION_NO).append(propertyMutation.getApplicationNo()).append(REGARDING)
                    .append(propertyMutation.getType() + " " + module.getDisplayName()).append(" in ")
                    .append(propertyMutation.getBasicProperty().getStatus().getName()).append(STATUS);
        } else if (!applictionType.isEmpty() && applictionType.equalsIgnoreCase(APPLICATION_TYPE_VACANCY_REMISSION)) {
            final VacancyRemission vacancyRemission = (VacancyRemission) stateAware;
            detailedMessage.append(APPLICATION_NO).append(vacancyRemission.getApplicationNumber()).append(REGARDING)
                    .append(vacancyRemission.getStateType() + " " + module.getDisplayName()).append(" in ")
                    .append(vacancyRemission.getBasicProperty().getStatus().getName()).append(STATUS);
        }

        return detailedMessage.toString();
    }

    private boolean isResolved(final StateAware stateAware) {
        return "CLOSED".equalsIgnoreCase(stateAware.getState().getValue());
    }

    public PortalInbox getPortalInbox(final String applicationNumber) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        return portalInboxService.getPortalInboxByApplicationNo(applicationNumber, module.getId());
    }

    /**
     * Updates Portal
     *
     * @param stateAware
     * @param applicationType
     */
    @Transactional
    public void updatePortal(final StateAware stateAware, final String applicationType) {
        if (!applicationType.isEmpty() && propertyApplicationTypes().contains(applicationType))
            updatePortalMessage(stateAware, applicationType);
        else if (!applicationType.isEmpty() && (applicationType.equalsIgnoreCase(APPLICATION_TYPE_REVISION_PETITION)
                || applicationType.equalsIgnoreCase(APPLICATION_TYPE_GRP)))
            updateRevisionPetitionPortalmessage(stateAware, applicationType);
        else if (!applicationType.isEmpty()
                && Arrays.asList(NATURE_REGISTERED_TRANSFER, NATURE_FULL_TRANSFER).contains(applicationType))
            updatePropertyMutationPortalmessage(stateAware, applicationType);
        else if (!applicationType.isEmpty() && applicationType.equalsIgnoreCase(APPLICATION_TYPE_VACANCY_REMISSION))
            updateVacancyRemissionPortalmessage(stateAware, applicationType);

    }

    /**
     * Method to push Property Mutation data for citizen portal inbox
     */

    @Transactional
    public void pushPropertyMutationPortalMessage(final StateAware stateAware, final String applictionType) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final PropertyMutation propertyMutation = (PropertyMutation) stateAware;
        final BasicProperty basicProperty = propertyMutation.getBasicProperty();
        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                propertyMutation.getType() + " " + module.getDisplayName(), propertyMutation.getApplicationNo(),
                basicProperty.getUpicNo(), basicProperty.getId(), propertyMutation.getType(),
                getDetailedMessage(stateAware, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP),
                format(APPLICATION_VIEW_URL, propertyMutation.getApplicationNo(), APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP),
                isResolved(propertyMutation), basicProperty.getStatus().getName(), getSlaEndDate(applictionType),
                propertyMutation.getState(), Arrays.asList(securityUtils.getCurrentUser()));
        final PortalInbox portalInbox = portalInboxBuilder.build();
        portalInboxService.pushInboxMessage(portalInbox);
    }

    /**
     * Method to update Property Mutation data for citizen portal inbox
     */
    @Transactional
    public void updatePropertyMutationPortalmessage(final StateAware stateAware, final String applictionType) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final PropertyMutation propertyMutation = (PropertyMutation) stateAware;
        final BasicProperty basicProperty = propertyMutation.getBasicProperty();
        portalInboxService.updateInboxMessage(propertyMutation.getApplicationNo(), module.getId(),
                propertyMutation.getState().getValue(), isResolved(propertyMutation), getSlaEndDate(applictionType),
                propertyMutation.getState(), null, basicProperty.getUpicNo(),
                format(APPLICATION_VIEW_URL, propertyMutation.getApplicationNo(), APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP));
    }

    /**
     * Method to push Vacancy Remission data for citizen portal inbox
     */

    @Transactional
    public void pushVacancyRemissionPortalMessage(final StateAware stateAware, final String applictionType) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final VacancyRemission vacancyRemission = (VacancyRemission) stateAware;
        final BasicProperty basicProperty = vacancyRemission.getBasicProperty();
        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                vacancyRemission.getStateType() + " " + module.getDisplayName(),
                vacancyRemission.getApplicationNumber(), basicProperty.getUpicNo(), basicProperty.getId(),
                vacancyRemission.getStateType(), getDetailedMessage(stateAware, applictionType),
                format(APPLICATION_VIEW_URL, vacancyRemission.getApplicationNumber(), applictionType),
                isResolved(stateAware), basicProperty.getStatus().getName(), getSlaEndDate(applictionType),
                vacancyRemission.getState(), Arrays.asList(securityUtils.getCurrentUser()));
        final PortalInbox portalInbox = portalInboxBuilder.build();
        portalInboxService.pushInboxMessage(portalInbox);
    }

    /**
     * Method to update Vacancy Remission data for citizen portal inbox
     */
    @Transactional
    public void updateVacancyRemissionPortalmessage(final StateAware stateAware, final String applicationType) {
        String stateVal;
        Boolean resolved;
        VacancyRemissionApproval vacancyRemissionApproval;
        State state;
        String applicationNo;
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final VacancyRemission vacancyRemission = (VacancyRemission) stateAware;
        final BasicProperty basicProperty = vacancyRemission.getBasicProperty();

        if ("CLOSED".equalsIgnoreCase(vacancyRemission.getState().getValue())
                && !vacancyRemission.getVacancyRemissionApproval().isEmpty()) {
            vacancyRemissionApproval = vacancyRemission.getVacancyRemissionApproval().get(0);
            stateVal = vacancyRemissionApproval.getState().getValue() + ": " + vacancyRemissionApproval.getStatus();
            resolved = isResolved(vacancyRemissionApproval);
            state = vacancyRemissionApproval.getState();
            applicationNo = vacancyRemissionApproval.getVacancyRemission().getApplicationNumber();
        } else {
            stateVal = vacancyRemission.getState().getValue() + ": " + vacancyRemission.getStatus();
            resolved = isResolved(vacancyRemission);
            state = vacancyRemission.getState();
            applicationNo = vacancyRemission.getApplicationNumber();
        }
        portalInboxService.updateInboxMessage(applicationNo, module.getId(), stateVal, resolved,
                getSlaEndDate(applicationType), state, null, basicProperty.getUpicNo(),
                format(APPLICATION_VIEW_URL, applicationNo, applicationType));

    }

    /**
     * Method to push revision petition data for citizen portal inbox
     */

    @Transactional
    public void pushRevisionPetitionPortalMessage(final StateAware stateAware, final String applictionType) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final Petition petition = (Petition) stateAware;
        final BasicProperty basicProperty = petition.getBasicProperty();
        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                petition.getType() + " " + module.getDisplayName(), petition.getObjectionNumber(),
                basicProperty.getUpicNo(), basicProperty.getId(), petition.getType(),
                getDetailedMessage(stateAware, applictionType),
                format(APPLICATION_VIEW_URL, petition.getObjectionNumber(), applictionType),
                isResolved(stateAware), basicProperty.getStatus().getName(), getSlaEndDate(applictionType),
                petition.getState(), Arrays.asList(securityUtils.getCurrentUser()));
        final PortalInbox portalInbox = portalInboxBuilder.build();
        portalInboxService.pushInboxMessage(portalInbox);
    }

    /**
     * Method to update revision petition data for citizen portal inbox
     */
    @Transactional
    public void updateRevisionPetitionPortalmessage(final StateAware stateAware, final String applictionType) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final Petition petition = (Petition) stateAware;
        final BasicProperty basicProperty = petition.getBasicProperty();
        portalInboxService.updateInboxMessage(petition.getObjectionNumber(), module.getId(),
                petition.getState().getValue(), isResolved(stateAware), getSlaEndDate(applictionType),
                petition.getState(), null, basicProperty.getUpicNo(),
                format(APPLICATION_VIEW_URL, petition.getObjectionNumber(), applictionType));
    }

    @Transactional
    public void updateVacancyRemissionIndex(final StateAware stateAwareObject, final String applictionType,
            final User stateOwner, final int sla) {
        final VacancyRemission vacancyRemission = (VacancyRemission) stateAwareObject;
        final ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(vacancyRemission.getApplicationNumber());
        final User owner = vacancyRemission.getBasicProperty().getPrimaryOwner();
        final String source = propertyTaxCommonUtils.getVRSource(vacancyRemission);
        if (applicationIndex == null)
            createVacancyRemissionApplicationIndex(applictionType, stateOwner, sla, vacancyRemission, owner, source);
        else
            updateVacancyRemissionApplicationIndex(stateOwner, applicationIndex, owner, vacancyRemission);
    }

    @Transactional
    public void createVacancyRemissionApplicationIndex(final String applictionType, final User stateOwner,
            final int sla, final VacancyRemission vacancyRemission, final User owner, final String source) {
        ApplicationIndex applicationIndex;
        final Date applicationDate = vacancyRemission.getCreatedDate() != null ? vacancyRemission.getCreatedDate()
                : new Date();
        final ClosureStatus closureStatus = vacancyRemission.getState().getValue().contains(WF_STATE_CLOSED)
                ? ClosureStatus.YES : ClosureStatus.NO;
        applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                .withApplicationNumber(vacancyRemission.getApplicationNumber()).withApplicationDate(applicationDate)
                .withApplicationType(applictionType).withApplicantName(owner.getName())
                .withStatus(vacancyRemission.getState().getValue())
                .withUrl(format(APPLICATION_VIEW_URL, vacancyRemission.getApplicationNumber(), applictionType))
                .withApplicantAddress(vacancyRemission.getBasicProperty().getAddress().toString())
                .withOwnername(stateOwner.getUsername() + "::" + stateOwner.getName().trim())
                .withChannel(source).withMobileNumber(owner.getMobileNumber())
                .withAadharNumber(owner.getAadhaarNumber())
                .withConsumerCode(vacancyRemission.getBasicProperty().getUpicNo()).withClosed(closureStatus)
                .withApproved(vacancyRemission.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                        ? ApprovalStatus.APPROVED
                        : vacancyRemission.getState().getValue().contains(WF_STATE_REJECTED)
                                || vacancyRemission.getState().getValue().contains(WF_STATE_CLOSED)
                                        ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS)
                .withSla(sla).build();
        applicationIndexService.createApplicationIndex(applicationIndex);
    }

    @Transactional
    public void updateVacancyRemissionApplicationIndex(final User stateOwner, final ApplicationIndex applicationIndex,
            final User owner, final VacancyRemission vacancyRemission) {
        applicationIndex.setStatus(vacancyRemission.getState().getValue());
        applicationIndex.setApplicantName(owner.getName());
        applicationIndex.setOwnerName(stateOwner.getUsername() + "::" + stateOwner.getName().trim());
        applicationIndex.setMobileNumber(owner.getMobileNumber());
        applicationIndex.setAadharNumber(owner.getAadhaarNumber());
        applicationIndex.setClosed(vacancyRemission.getState().getValue().contains(WF_STATE_CLOSED) ? ClosureStatus.YES
                : ClosureStatus.NO);
        if (!ApprovalStatus.APPROVED.equals(applicationIndex.getApproved())
                || vacancyRemission.getState().getValue().contains(WF_STATE_REJECTED))
            applicationIndex.setApproved(vacancyRemission.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)
                    ? ApprovalStatus.APPROVED
                    : vacancyRemission.getState().getValue().contains(WF_STATE_CLOSED)
                            ? ApprovalStatus.REJECTED : ApprovalStatus.INPROGRESS);
        applicationIndexService.updateApplicationIndex(applicationIndex);
    }

    public BigDecimal getTotalTaxExcludingUACPenalty(Installment installment, Ptdemand ptDemand) {
        BigDecimal halfYearlyTax = ZERO;
        for (EgDemandDetails demandDetails : ptDemand.getEgDemandDetails()) {
            if (installment.getFromDate().equals(demandDetails.getInstallmentStartDate()) &&
                    !DEMANDRSN_CODE_UNAUTHORIZED_PENALTY
                            .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                halfYearlyTax = halfYearlyTax.add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetails));
        }
        return halfYearlyTax;
    }

    public Property getHistoryPropertyByUpinNo(BasicProperty basicProperty) {
        return propertyHibernateDAO.getHistoryPropertyForBasicProperty(basicProperty);
    }

    public Ptdemand getLatestDemandforHistoryProp(Property oldProperty) {
        return propertyHibernateDAO.getLatestDemand(oldProperty);
    }

    public Map<String, BigDecimal> getDCBDetailsForProperty(Property property) {
        return ptDemandDAO.getDemandCollMap(property);
    }

    public Map<String, String> getGlCodesForTaxes() {
        Map<String, String> glCodeMap = new HashMap<>();
        List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_PT_DEMAND_VOUCHER_GLCODES);
        String[] taxHeads;
        String[] value;
        for (AppConfigValues appConfig : appConfigValues) {
            taxHeads = appConfig.getValue().split("~");
            for (String taxHead : taxHeads) {
                value = taxHead.split("=");
                glCodeMap.put(value[0], value[1]);
            }
        }
        return glCodeMap;
    }

    private Map<String, Map<String, Object>> prepareDataForDemandVoucher(Property currProperty, Property oldProperty,
            boolean demandIncreased, boolean forCreate) {
        Map<String, Map<String, Object>> voucherDetails = new HashMap<>();
        currentInstall = propertyTaxCommonUtils.getCurrentInstallment();
        Map<String, String> glCodeMap = getGlCodesForTaxes();
        Module module = moduleService.getModuleByName(PTMODULENAME);
        Date effectiveDate;
        if (forCreate) {
            if (!currProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
                    .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                effectiveDate = getLowestDtOfCompFloorWise(currProperty.getPropertyDetail().getFloorDetails());
            else
                effectiveDate = currProperty.getPropertyDetail().getDateOfCompletion();
        } else
            effectiveDate = currProperty.getEffectiveDate();
        Installment effectiveInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, effectiveDate);
        Map<String, Object> values;
        Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment currFirstHalf = currYearInstMap.get(CURRENTYEAR_FIRST_HALF);
        Installment currSecondHalf = currYearInstMap.get(CURRENTYEAR_SECOND_HALF);
        /*
         * if demandIncreased, yearwise current and arrear tax details go as debit and headwise taxes go as credit, else yearwise
         * current and arrear tax details go as credit and headwise taxes go as debit
         */
        Ptdemand ptDemand = currProperty.getPtDemandSet().iterator().next();
        Ptdemand oldPtDemand;
        Map<String, BigDecimal> oldPropertyTaxMap = new LinkedHashMap<>();
        Map<String, BigDecimal> currPropertyTaxMap = fetchHeadwiseDetailsForDemandVoucher(effectiveInstall,
                currFirstHalf, currSecondHalf, ptDemand);
        if (oldProperty != null) {
            oldPtDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(oldProperty);
            oldPropertyTaxMap = fetchHeadwiseDetailsForDemandVoucher(effectiveInstall, currFirstHalf, currSecondHalf,
                    oldPtDemand);
        }
        if (!currPropertyTaxMap.isEmpty())
            prepareVoucherDetailsMap(voucherDetails, glCodeMap, oldPropertyTaxMap, currPropertyTaxMap);

        values = new HashMap<>();
        values.put(IS_INCREASED, demandIncreased ? true : false);
        voucherDetails.put("demandIncreased", values);
        return voucherDetails;
    }

    private void prepareVoucherDetailsMap(Map<String, Map<String, Object>> voucherDetails,
            Map<String, String> glCodeMap, Map<String, BigDecimal> oldPropertyTaxMap,
            Map<String, BigDecimal> currPropertyTaxMap) {
        Map<String, Object> values;
        BigDecimal advance = ZERO;
        if (currPropertyTaxMap.get(DEMANDRSN_CODE_ADVANCE) != null)
            advance = currPropertyTaxMap.get(DEMANDRSN_CODE_ADVANCE)
                    .subtract(oldPropertyTaxMap.get(DEMANDRSN_CODE_ADVANCE) == null
                            ? BigDecimal.ZERO
                            : oldPropertyTaxMap.get(DEMANDRSN_CODE_ADVANCE));
        BigDecimal generaltax = (currPropertyTaxMap.get(DEMANDRSN_CODE_GENERAL_TAX) == null
                ? BigDecimal.ZERO : currPropertyTaxMap.get(DEMANDRSN_CODE_GENERAL_TAX))
                        .subtract(oldPropertyTaxMap.get(DEMANDRSN_CODE_GENERAL_TAX) == null
                                ? BigDecimal.ZERO
                                : oldPropertyTaxMap.get(DEMANDRSN_CODE_GENERAL_TAX));
        BigDecimal vacantTax = (currPropertyTaxMap.get(DEMANDRSN_CODE_VACANT_TAX) == null
                ? BigDecimal.ZERO : currPropertyTaxMap.get(DEMANDRSN_CODE_VACANT_TAX))
                        .subtract(oldPropertyTaxMap.get(DEMANDRSN_CODE_VACANT_TAX) == null
                                ? BigDecimal.ZERO
                                : oldPropertyTaxMap.get(DEMANDRSN_CODE_VACANT_TAX));
        BigDecimal libCess = (currPropertyTaxMap.get(DEMANDRSN_CODE_LIBRARY_CESS) == null
                ? BigDecimal.ZERO : currPropertyTaxMap.get(DEMANDRSN_CODE_LIBRARY_CESS))
                        .subtract(oldPropertyTaxMap.get(DEMANDRSN_CODE_LIBRARY_CESS) == null
                                ? BigDecimal.ZERO
                                : oldPropertyTaxMap.get(DEMANDRSN_CODE_LIBRARY_CESS));
        BigDecimal currTax = (currPropertyTaxMap.get(CURR_TAX) == null
                ? BigDecimal.ZERO : currPropertyTaxMap.get(CURR_TAX))
                        .subtract(oldPropertyTaxMap.get(CURR_TAX) == null
                                ? BigDecimal.ZERO
                                : oldPropertyTaxMap.get(CURR_TAX));
        BigDecimal arrearTax = (currPropertyTaxMap.get(ARREAR_TAX) == null
                ? BigDecimal.ZERO : currPropertyTaxMap.get(ARREAR_TAX))
                        .subtract(oldPropertyTaxMap.get(ARREAR_TAX) == null
                                ? BigDecimal.ZERO
                                : oldPropertyTaxMap.get(ARREAR_TAX));

        if (advance.compareTo(BigDecimal.ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, advance);
            values.put(IS_INCREASED, advance.compareTo(BigDecimal.ZERO) < 0 ? false : true);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_ADVANCE), values);
        }
        if (generaltax.compareTo(BigDecimal.ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, generaltax);
            values.put(IS_INCREASED, generaltax.compareTo(BigDecimal.ZERO) < 0 ? false : true);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_GENERAL_TAX), values);
        }
        if (vacantTax.compareTo(BigDecimal.ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, vacantTax);
            values.put(IS_INCREASED, vacantTax.compareTo(BigDecimal.ZERO) < 0 ? false : true);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_VACANT_TAX), values);
        }
        if (libCess.compareTo(BigDecimal.ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, libCess);
            values.put(IS_INCREASED, libCess.compareTo(BigDecimal.ZERO) < 0 ? false : true);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_LIBRARY_CESS), values);
        }
        if (currTax.compareTo(BigDecimal.ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, currTax);
            values.put(IS_INCREASED, currTax.compareTo(BigDecimal.ZERO) < 0 ? false : true);
            voucherDetails.put(CURRENT_DEMANDRSN_GLCODE, values);
        }
        if (arrearTax.compareTo(BigDecimal.ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, arrearTax);
            values.put(IS_INCREASED, arrearTax.compareTo(BigDecimal.ZERO) < 0 ? false : true);
            voucherDetails.put(ARREAR_DEMANDRSN_GLCODE, values);
        }
    }

    public Map<String, BigDecimal> fetchHeadwiseDetailsForDemandVoucher(Installment effectiveInstall,
            Installment currFirstHalf, Installment currSecondHalf, Ptdemand ptDemand) {
        String taxHead;
        BigDecimal advance = BigDecimal.ZERO;
        BigDecimal generalTax = BigDecimal.ZERO;
        BigDecimal vacantLandTax = BigDecimal.ZERO;
        BigDecimal libCess = BigDecimal.ZERO;
        BigDecimal currTax = BigDecimal.ZERO;
        BigDecimal arrearTax = BigDecimal.ZERO;
        Map<String, BigDecimal> currPropertyTaxMap = new LinkedHashMap<>();
        for (EgDemandDetails demandDetails : ptDemand.getEgDemandDetails()) {
            if (!demandDetails.getInstallmentStartDate().before(effectiveInstall.getFromDate())) {
                taxHead = demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode();
                if (DEMANDRSN_CODE_ADVANCE.equalsIgnoreCase(taxHead)) {
                    advance = advance.add(demandDetails.getAmtCollected());
                }
                if (DEMANDRSN_CODE_GENERAL_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_EDUCATIONAL_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_WATER_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_DRAINAGE_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_SCAVENGE_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_LIGHT_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_UNAUTHORIZED_PENALTY.equalsIgnoreCase(taxHead))
                    generalTax = generalTax.add(demandDetails.getAmount());

                if (DEMANDRSN_CODE_VACANT_TAX.equalsIgnoreCase(taxHead))
                    vacantLandTax = vacantLandTax.add(demandDetails.getAmount());
                if (DEMANDRSN_CODE_LIBRARY_CESS.equalsIgnoreCase(taxHead))
                    libCess = libCess.add(demandDetails.getAmount());

                if (!DEMANDRSN_CODE_PENALTY_FINES.equalsIgnoreCase(taxHead)
                        && !DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY.equalsIgnoreCase(taxHead)) {
                    if (demandDetails.getInstallmentStartDate().equals(currFirstHalf.getFromDate())
                            || demandDetails.getInstallmentStartDate().equals(currSecondHalf.getFromDate())) {
                        currTax = currTax.add(demandDetails.getAmount());
                    } else {
                        arrearTax = arrearTax.add(demandDetails.getAmount());
                    }
                }
            }
        }
        if (advance.compareTo(BigDecimal.ZERO) > 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_ADVANCE, advance);
        if (generalTax.compareTo(BigDecimal.ZERO) > 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_GENERAL_TAX, generalTax);
        if (vacantLandTax.compareTo(BigDecimal.ZERO) > 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_VACANT_TAX, vacantLandTax);
        if (libCess.compareTo(BigDecimal.ZERO) > 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_LIBRARY_CESS, libCess);
        if (currTax.compareTo(BigDecimal.ZERO) > 0)
            currPropertyTaxMap.put(CURR_TAX, currTax);
        if (arrearTax.compareTo(BigDecimal.ZERO) > 0)
            currPropertyTaxMap.put(ARREAR_TAX, arrearTax);

        return currPropertyTaxMap;
    }

    public BigDecimal getSurveyTax(Property property, Date fromDate) {
        BigDecimal totalTax = BigDecimal.ZERO;
        Map<String, Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(fromDate);
        Date firstInstStartDate = yearwiseInstMap.get(CURRENTYEAR_FIRST_HALF).getFromDate();
        Date secondInstStartDate = yearwiseInstMap.get(CURRENTYEAR_SECOND_HALF).getFromDate();
        for (EgDemandDetails demandDetail : property.getPtDemandSet().iterator().next().getEgDemandDetails()) {
            if (firstInstStartDate.equals(demandDetail.getInstallmentStartDate())
                    || secondInstStartDate.equals(demandDetail.getInstallmentStartDate())
                            && !DEMANDRSN_CODE_PENALTY_FINES.equalsIgnoreCase(
                                    demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())
                            && !DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY.equalsIgnoreCase(
                                    demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                totalTax = totalTax.add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
        }
        return totalTax;
    }

    public boolean isLatestPropertyMutationClosed(String upicno) {
        boolean closed = true;
        final javax.persistence.Query qry = entityManager.createNamedQuery("UNDER_WF_MUTATION_BY_UPICNO");
        qry.setParameter("upicNo", upicno);
        qry.setMaxResults(1);
        if (!qry.getResultList().isEmpty())
            closed = false;
        return closed;
    }
    
    public Map<Installment, Map<String, BigDecimal>> getExcessCollAmtMap() {
        return excessCollAmtMap;
    }

    public void setExcessCollAmtMap(final Map<Installment, Map<String, BigDecimal>> excessCollAmtMap) {
        this.excessCollAmtMap = excessCollAmtMap;
    }

    public EisCommonsService getEisCommonsService() {
        return eisCommonsService;
    }

    public void setEisCommonsService(final EisCommonsService eisCommonsService) {
        this.eisCommonsService = eisCommonsService;
    }

    public BigDecimal getTotalAlv() {
        return totalAlv;
    }

    public void setTotalAlv(final BigDecimal totalAlv) {
        this.totalAlv = totalAlv;
    }
}

