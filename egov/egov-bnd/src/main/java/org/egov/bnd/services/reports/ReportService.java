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
package org.egov.bnd.services.reports;

import org.apache.log4j.Logger;
import org.egov.bnd.model.PopulationDetailMaster;
import org.egov.bnd.model.RegistrationHistory;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.model.ReportDetails;
import org.egov.bnd.model.ViewBirthDeathRegistration;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//import org.egov.bnd.model.FeeCollection;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public class ReportService extends PersistenceService<ViewBirthDeathRegistration, Long> {

    private static final String MONTH = "MONTH";
    private static final String DELAYED = "DELAYED";
    private static final String NO = "NO";
    private static final String YES = "YES";
    private static final String SEX = "sex";
    private static final String REGTYPE = "REGTYPE";
    private static final String YEAR = "YEAR";
    private static final String FROMDATE = "FROMDATE";
    private static final String TODATE = "TODATE";
    private static final String REGISTRATIONDATE = "registrationdate";
    private static final String STILLBIRTH = "stillbirth";
    private static final String TYPE = "type";
    private static final String PROGRESSIVE = "PROGRESSIVE";
    private static final String PLACEOFDEATH = "PLACEOFDEATH";
    private static final String REGISTRATIONUNIT = "REGISTRATIONUNIT";
    private static final String ESTABLISHMENTID = "ESTABLISHMENTID";
    private static final String REGISTRATIONNO = "REGISTRATIONNO";
    private static final String HOSPITAL = "HOSPITAL";
    private static final String STATUS = "STATUS";
    private static final Logger LOGGER = Logger.getLogger(ReportService.class);
    private Map<Integer, String> STATUSMAP;
    private BndCommonService bndCommonService;

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    public Map getSummaryReportResult(final HashMap<String, Object> InputMap) {

        LOGGER.debug("Started delayedRegistration method");
        final Map<String, Object> resultMap = prepareMap();
        InputMap.put(PROGRESSIVE, Boolean.FALSE);
        buildCriteriaCurrent(InputMap, resultMap);
        buildCriteriaDelayed(InputMap, resultMap);
        resultMap.put(
                "Total",
                (Integer) resultMap.get("numberofcurrentRegistrations")
                        + (Integer) resultMap.get("numberofdelayedRegistrations"));
        resultMap.put("Duration", BndConstants.MONTHMAP.get(Integer.parseInt(InputMap.get("MONTH").toString())) + " - "
                + InputMap.get("YEAR").toString());
        LOGGER.debug("Completed delayedRegistration method");
        return resultMap;
    }

    @Transactional
    public Map buildCriteriaCurrent(final Map<String, Object> InputMap, final Map<String, Object> resultMap) {
        LOGGER.debug("Started delayedRegistration method");
        InputMap.put(DELAYED, NO);
        final Criteria criteria = getBuildCriteria(InputMap);
        resultMap.put("numberofcurrentRegistrations", criteria.list().size());
        LOGGER.debug("Completed delayedRegistration method");
        return resultMap;
    }

    @Transactional
    public Map buildCriteriaDelayed(final Map<String, Object> InputMap, final Map<String, Object> resultMap) {
        LOGGER.debug("Started delayedRegistration method");
        InputMap.put(DELAYED, YES);
        final Criteria criteria = getBuildCriteria(InputMap);
        resultMap.put("numberofdelayedRegistrations", criteria.list().size());
        LOGGER.debug("Completed delayedRegistration method");
        return resultMap;
    }

    public Map prepareMap() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();
        return hashMap;
    }

    @Transactional
    public EgovPaginatedList getStillBirthDeath(final HashMap<String, Object> InputMap, final Integer page,
            final int pagesize) {

        LOGGER.debug("Started delayedRegistration method");
        InputMap.put(PROGRESSIVE, Boolean.FALSE);
        final Criteria criteria = getBuildCriteria(InputMap);
        final HashSet result = new HashSet(criteria.list());
        final Page resultPage = new Page(criteria, page, pagesize);
        final int count = result.size();
        final EgovPaginatedList pagedResults = new EgovPaginatedList(resultPage, count);
        LOGGER.debug("Completed delayedRegistration method");
        return pagedResults;

    }

    public List<ReportDetails> getDelayedRegistrationDetails(final Map<String, Object> InputMap) {
        LOGGER.debug("Started delayedRegistration method");
        final Boolean delayedflag = Boolean.TRUE;
        final List<ReportDetails> reportdetailList = new ArrayList<ReportDetails>();
        for (int i = 1; i <= 12; i++) {
            ReportDetails reportdet = new ReportDetails();
            reportdet = setupReportforRegistration(reportdet, i, InputMap, delayedflag, Boolean.FALSE);
            reportdet.setMonth(BndConstants.MONTHMAP.get(i));
            reportdet.setSlNo(i);
            reportdetailList.add(reportdet);
        }
        LOGGER.debug("Completed delayedRegistration method");
        return reportdetailList;
    }

    public Map<String, Object> setdelayedinfotocriteria(final Map<String, Object> inputRegMap, final Boolean delayedFlag) {

        LOGGER.debug("Started delayedRegistration method");
        if (delayedFlag != null && delayedFlag.equals(Boolean.TRUE))
            inputRegMap.put(DELAYED, YES);
        if (delayedFlag != null && delayedFlag.equals(Boolean.FALSE))
            inputRegMap.put(DELAYED, NO);
        LOGGER.debug("Completed delayedRegistration method");
        return inputRegMap;
    }

    @Transactional
    public ReportDetails setupReportforRegistration(final ReportDetails reportdet, final Integer monthno,
            final Map<String, Object> InputMap, final Boolean delayedflag, final Boolean progressiveFlag) {

        LOGGER.debug("Started delayedRegistration method");
        Map<String, Object> inputMapBirth = prepareMap();
        Criteria criteria = prepareRegdetails(InputMap, inputMapBirth, BndConstants.BIRTH, monthno, progressiveFlag);
        criteria.add(Restrictions.eq(SEX, BndConstants.MALE));
        inputMapBirth = setdelayedinfotocriteria(inputMapBirth, delayedflag);
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setBirthMale(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setBirthMaleprogressive(criteria.list().size());
        criteria = getBuildCriteria(inputMapBirth);
        criteria.add(Restrictions.eq(SEX, BndConstants.FEMALE));

        inputMapBirth = setdelayedinfotocriteria(inputMapBirth, delayedflag);

        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setBirthFemale(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setBirthFemaleprogressive(criteria.list().size());
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setBirthTotal(reportdet.getBirthFemale() + reportdet.getBirthMale());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setBirthTotalprogressive(reportdet.getBirthFemaleprogressive()
                    + reportdet.getBirthMaleprogressive());

        Map<String, Object> inputMapDeath = prepareMap();
        criteria = prepareRegdetails(InputMap, inputMapDeath, BndConstants.DEATH, monthno, progressiveFlag);
        criteria = getBuildCriteria(inputMapDeath);
        criteria.add(Restrictions.eq(SEX, BndConstants.MALE));
        inputMapDeath = setdelayedinfotocriteria(inputMapDeath, delayedflag);

        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setDeathMale(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setDeathMaleprogressive(criteria.list().size());
        criteria = getBuildCriteria(inputMapDeath);
        criteria.add(Restrictions.eq(SEX, BndConstants.FEMALE));

        inputMapDeath = setdelayedinfotocriteria(inputMapDeath, delayedflag);

        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setDeathFemale(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setDeathFemaleprogressive(criteria.list().size());
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setDeathTotal(reportdet.getDeathFemale() + reportdet.getDeathMale());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setDeathTotalprogressive(reportdet.getDeathFemaleprogressive()
                    + reportdet.getDeathMaleprogressive());
        LOGGER.debug("Completed delayedRegistration method");
        return reportdet;

    }

    @Transactional
    public Criteria prepareRegdetails(final Map<String, Object> inputMap, final Map<String, Object> inputMapReg,
            final String regType, final Integer monthno, final Boolean progressiveFlag) {

        LOGGER.debug("Started delayedRegistration method");
        inputMapReg.put(YEAR, inputMap.get(YEAR).toString());

        if (inputMap.get(PLACEOFDEATH) != null && inputMap.get(PLACEOFDEATH) != "")
            inputMapReg.put(PLACEOFDEATH, inputMap.get(PLACEOFDEATH).toString());

        if (inputMap.get(REGISTRATIONUNIT) != null && inputMap.get(REGISTRATIONUNIT) != "")
            inputMapReg.put(REGISTRATIONUNIT, inputMap.get(REGISTRATIONUNIT).toString());

        if (inputMap.get(ESTABLISHMENTID) != null && inputMap.get(ESTABLISHMENTID) != "")
            inputMapReg.put(ESTABLISHMENTID, inputMap.get(ESTABLISHMENTID).toString());

        if (inputMap.get(STATUS) != null && inputMap.get(STATUS) != "")
            inputMapReg.put(STATUS, inputMap.get(STATUS).toString());

        inputMapReg.put(MONTH, monthno.toString());
        inputMapReg.put(REGTYPE, regType);
        inputMapReg.put(PROGRESSIVE, progressiveFlag);
        final Criteria criteria = getBuildCriteria(inputMapReg);
        LOGGER.debug("Completed delayedRegistration method");
        return criteria;
    }

    @Transactional
    private Criteria getBuildCriteria(final Map<String, Object> hashMap) {
        LOGGER.debug("Started delayedRegistration method");
        Criteria criteria = null;
        criteria = getSession().createCriteria(ViewBirthDeathRegistration.class, "birthdeath");
        if (hashMap.get(REGTYPE) != null && hashMap.get(REGTYPE) != ""
                && hashMap.get(REGTYPE).equals(BndConstants.BIRTH)) {
            criteria.add(Restrictions.eq(TYPE, hashMap.get(REGTYPE)));
            criteria.add(Restrictions.eq(STILLBIRTH, 'N'));
        } else if (hashMap.get(REGTYPE) != null && hashMap.get(REGTYPE) != ""
                && hashMap.get(REGTYPE).equals(BndConstants.DEATH))
            criteria.add(Restrictions.eq(TYPE, hashMap.get(REGTYPE)));
        else if (hashMap.get(REGTYPE) != null && hashMap.get(REGTYPE) != ""
                && hashMap.get(REGTYPE).equals(BndConstants.STILLBIRTH))
            criteria.add(Restrictions.eq(STILLBIRTH, 'Y'));

        if (hashMap.get(PROGRESSIVE) != null && hashMap.get(PROGRESSIVE) != ""
                && hashMap.get(PROGRESSIVE).equals(Boolean.TRUE)) {

            if (hashMap.get(MONTH) != null && hashMap.get(MONTH) != "" && hashMap.get(YEAR) != null
                    && hashMap.get(YEAR) != "")
                criteria.add(Restrictions.between(
                        REGISTRATIONDATE,
                        BndDateUtils.calenderutils(Integer.parseInt((String) hashMap.get(YEAR)), 1).get(0),
                        BndDateUtils.calenderutils(Integer.parseInt((String) hashMap.get(YEAR)),
                                Integer.parseInt((String) hashMap.get(MONTH))).get(1)));

        } else {

            if (hashMap.get(MONTH) != null && hashMap.get(MONTH) != "" && hashMap.get(YEAR) != null
                    && hashMap.get(YEAR) != "")
                criteria.add(Restrictions.between(
                        REGISTRATIONDATE,
                        BndDateUtils.calenderutils(Integer.parseInt((String) hashMap.get(YEAR)),
                                Integer.parseInt((String) hashMap.get(MONTH))).get(0),
                        BndDateUtils.calenderutils(Integer.parseInt((String) hashMap.get(YEAR)),
                                Integer.parseInt((String) hashMap.get(MONTH))).get(1)));
            if (hashMap.get(FROMDATE) != null && hashMap.get(FROMDATE) != "" && hashMap.get(TODATE) != null
                    && hashMap.get(TODATE) != "")
                criteria.add(Restrictions.between(REGISTRATIONDATE, hashMap.get(FROMDATE), hashMap.get(TODATE)));
            else {

                if (hashMap.get(FROMDATE) != null && hashMap.get(FROMDATE) != "")
                    criteria.add(Restrictions.gt(REGISTRATIONDATE, hashMap.get(FROMDATE)));
                if (hashMap.get(TODATE) != null && hashMap.get(TODATE) != "")
                    criteria.add(Restrictions.le(REGISTRATIONDATE, hashMap.get(TODATE)));
            }
        }

        if (hashMap.get(DELAYED) != null && hashMap.get(DELAYED) != "")
            if (hashMap.get(DELAYED).equals(YES))
                criteria.add(Restrictions.gt("eventregdatediff", 365));
            else if (hashMap.get(DELAYED).equals(NO))
                criteria.add(Restrictions.le("eventregdatediff", 365));

        if (hashMap.get(PLACEOFDEATH) != null && hashMap.get(PLACEOFDEATH) != "") {
            criteria.createAlias("birthdeath.placeid", "placetype");
            if (hashMap.get(PLACEOFDEATH).equals("hospital"))
                criteria.add(Restrictions.eq("placetype.typeConst", HOSPITAL));
            if (hashMap.get(PLACEOFDEATH).equals("nonhospital"))
                criteria.add(Restrictions.ne("placetype.typeConst", HOSPITAL));
        }

        if (hashMap.get(STATUS) != null && hashMap.get(STATUS) != "") {
            criteria.createAlias("birthdeath.registrationstatus", "status");
            criteria.add(Restrictions.eq("status.code", hashMap.get(STATUS).toString()));
        } else if (hashMap.get("REPORTTYPE") == null) {
            criteria.createAlias("birthdeath.registrationstatus", "status");
            criteria.add(Restrictions.eq("status.code", "Approved"));
        }

        if (hashMap.get(ESTABLISHMENTID) != null && hashMap.get(ESTABLISHMENTID) != "") {
            criteria.createAlias("birthdeath.establishmentmasterid", "establishment");
            criteria.add(Restrictions.eq("establishment.id", Long.valueOf(hashMap.get(ESTABLISHMENTID).toString())));
        }

        if (hashMap.get(REGISTRATIONUNIT) != null && hashMap.get(REGISTRATIONUNIT) != "") {
            criteria.createAlias("birthdeath.regunitid", "regunit");
            criteria.add(Restrictions.eq("regunit.id", Long.valueOf(hashMap.get(REGISTRATIONUNIT).toString())));
        }

        if (hashMap.get(REGISTRATIONNO) != null && hashMap.get(REGISTRATIONNO) != "")
            criteria.add(Restrictions.eq("registrationno", hashMap.get(REGISTRATIONNO).toString()));

        LOGGER.debug("Completed delayedRegistration method");
        return criteria;
    }

    public ReportDetails getUnitwiseRegistrationDetailsGeneral(final HashMap<String, Object> InputMap) {

        LOGGER.debug("Started getUnitwiseRegistrationDetailsGeneral method");
        final RegistrationUnit regUnit = bndCommonService
                .getRegistrationUnitById((Long) InputMap.get(REGISTRATIONUNIT));
        ReportDetails reportdet = new ReportDetails();
        reportdet = setupReportforRegistration(reportdet, (Integer) InputMap.get(MONTH), InputMap, Boolean.FALSE,
                Boolean.TRUE);
        reportdet = setupYearlyReport(reportdet, (Integer) InputMap.get(MONTH), InputMap, Boolean.FALSE, Boolean.TRUE);
        reportdet = setupReportforRegistration(reportdet, (Integer) InputMap.get(MONTH), InputMap, Boolean.FALSE,
                Boolean.FALSE);
        reportdet = setupYearlyReport(reportdet, (Integer) InputMap.get(MONTH), InputMap, Boolean.FALSE, Boolean.FALSE);
        reportdet = getReportdetailsregunit(reportdet, (Integer) InputMap.get(MONTH), InputMap, Boolean.FALSE,
                Boolean.FALSE);
        reportdet = getReportdetailsregunit(reportdet, (Integer) InputMap.get(MONTH), InputMap, Boolean.FALSE,
                Boolean.TRUE);
        reportdet.setRegUnit(regUnit.getRegUnitDesc());
        LOGGER.debug("Completed getUnitwiseRegistrationDetailsGeneral method");
        return reportdet;
    }

    public List<ReportDetails> getUnitwiseRegistrationDetails(final HashMap<String, Object> InputMap) {

        LOGGER.debug("Started getUnitwiseRegistrationDetails method");
        final List<ReportDetails> unitwiseList = new ArrayList<ReportDetails>();
        unitwiseList.add(getUnitwiseRegistrationDetailsHospital(InputMap));
        unitwiseList.add(getUnitwiseRegistrationDetailsNotHospital(InputMap));
        LOGGER.debug("Completed getUnitwiseRegistrationDetails method");
        return unitwiseList;
    }

    public ReportDetails getUnitwiseRegistrationDetailsHospital(final HashMap<String, Object> InputMap) {
        InputMap.put(PLACEOFDEATH, "hospital");
        return getUnitwiseRegistrationDetailsGeneral(InputMap);

    }

    public ReportDetails getUnitwiseRegistrationDetailsNotHospital(final HashMap<String, Object> InputMap) {

        InputMap.put(PLACEOFDEATH, "nonhospital");
        return getUnitwiseRegistrationDetailsGeneral(InputMap);
    }

    @Transactional
    public List<ReportDetails> getCompleteRegistrationDetails(final HashMap<String, Object> InputMap) {

        LOGGER.debug("Started getCompleteRegistrationDetails method");
        final List<ReportDetails> reportdetailList = new ArrayList<ReportDetails>();
        final List<RegistrationUnit> regUnitList = bndCommonService.getRegistrationUnit();
        for (final RegistrationUnit regUnit : regUnitList) {
            ReportDetails reportdet = new ReportDetails();
            InputMap.put(REGISTRATIONUNIT, regUnit.getId());
            reportdet = setupReportforRegistration(reportdet, (Integer) InputMap.get(MONTH), InputMap, null,
                    Boolean.TRUE);
            reportdet = setupYearlyReport(reportdet, (Integer) InputMap.get(MONTH), InputMap, null, Boolean.TRUE);
            reportdet = setupReportforRegistration(reportdet, (Integer) InputMap.get(MONTH), InputMap, null,
                    Boolean.FALSE);
            reportdet = setupYearlyReport(reportdet, (Integer) InputMap.get(MONTH), InputMap, null, Boolean.FALSE);
            reportdet.setRegUnit(regUnit.getRegUnitDesc());
            reportdetailList.add(reportdet);
        }
        LOGGER.debug("Completed getCompleteRegistrationDetails method");
        return reportdetailList;
    }

    public ReportDetails setupYearlyReport(ReportDetails reportdet, final Integer monthno,
            final HashMap<String, Object> InputMap, final Boolean delayedflag, final Boolean progressiveFlag) {

        reportdet = getReportdetailsmonthy(reportdet, InputMap, monthno, delayedflag, progressiveFlag);
        return reportdet;

    }

    @Transactional
    public ReportDetails getReportdetailsmonthy(final ReportDetails reportdet, final Map<String, Object> InputMap,
            final Integer monthno, final Boolean delayedFlag, final Boolean progressiveFlag) {

        LOGGER.debug("Started getReportdetailsmonthy method");
        Criteria criteria = null;
        Map<String, Object> inputMapStillbirth = prepareMap();
        inputMapStillbirth = setdelayedinfotocriteria(inputMapStillbirth, delayedFlag);
        criteria = prepareRegdetails(InputMap, inputMapStillbirth, BndConstants.STILLBIRTH, monthno, progressiveFlag);
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setStillbirth(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setStillbirthprogressive(criteria.list().size());
        Map<String, Object> inputMapInfantdeath = prepareMap();
        inputMapInfantdeath = setdelayedinfotocriteria(inputMapInfantdeath, delayedFlag);
        criteria = prepareRegdetails(InputMap, inputMapInfantdeath, BndConstants.DEATH, monthno, progressiveFlag);
        criteria.createAlias("ageType", "AGETYPE");
        criteria.add(Restrictions.ne("AGETYPE.ageTypedesc", "Year"));
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setInfantdeath(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setInfantdeathprogressive(criteria.list().size());
        Map<String, Object> inputMapDeliverydeath = prepareMap();
        inputMapDeliverydeath = setdelayedinfotocriteria(inputMapDeliverydeath, delayedFlag);
        criteria = prepareRegdetails(InputMap, inputMapDeliverydeath, BndConstants.DEATH, monthno, progressiveFlag);
        criteria.add(Restrictions.eq("isdeathatpreg", "0"));
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setDeliverydeath(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setDeliverydeathprogressive(criteria.list().size());
        Map<String, Object> inputMapcertifieddeath = prepareMap();
        inputMapcertifieddeath = setdelayedinfotocriteria(inputMapcertifieddeath, delayedFlag);
        criteria = prepareRegdetails(InputMap, inputMapcertifieddeath, BndConstants.DEATH, monthno, progressiveFlag);
        criteria.add(Restrictions.eq("certifiedmed", "0"));
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setCertifieddeath(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setCertifieddeathprogressive(criteria.list().size());
        LOGGER.debug("Completed getReportdetailsmonthy method");
        return reportdet;
    }

    @Transactional
    public ReportDetails getReportdetailsregunit(final ReportDetails reportdet, final Integer monthno,
            final HashMap<String, Object> InputMap, final Boolean delayedFlag, final Boolean progressiveFlag) {

        LOGGER.debug("Started getReportdetailsregunit method");
        Criteria criteria = null;
        Map<String, Object> inputMapStillbirth = prepareMap();
        inputMapStillbirth = setdelayedinfotocriteria(inputMapStillbirth, delayedFlag);
        criteria = prepareRegdetails(InputMap, inputMapStillbirth, BndConstants.STILLBIRTH, monthno, progressiveFlag);
        criteria.add(Restrictions.eq(SEX, BndConstants.MALE));
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setStillbirthmale(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setStillbirthmaleprogressive(criteria.list().size());
        criteria = prepareRegdetails(InputMap, inputMapStillbirth, BndConstants.STILLBIRTH, monthno, progressiveFlag);
        criteria.add(Restrictions.eq(SEX, BndConstants.FEMALE));
        if (progressiveFlag.equals(Boolean.FALSE))
            reportdet.setStillbirthfemale(criteria.list().size());
        if (progressiveFlag.equals(Boolean.TRUE))
            reportdet.setStillbirthfemaleprogressive(criteria.list().size());
        LOGGER.debug("Completed getReportdetailsregunit method");
        return reportdet;

    }

    public ReportDetails getTotalObj(final List<ReportDetails> reportList) {

        LOGGER.debug("Started getTotalObj method");
        final ReportDetails totalObj = new ReportDetails();
        totalObj.setBirthMale(0);
        totalObj.setBirthFemale(0);
        totalObj.setBirthTotal(0);
        totalObj.setDeathMale(0);
        totalObj.setDeathFemale(0);
        totalObj.setDeathTotal(0);
        for (final ReportDetails repDetobj : reportList) {

            totalObj.setBirthMale(totalObj.getBirthMale() + repDetobj.getBirthMale());
            totalObj.setBirthFemale(totalObj.getBirthFemale() + repDetobj.getBirthFemale());
            totalObj.setBirthTotal(totalObj.getBirthTotal() + repDetobj.getBirthTotal());
            totalObj.setDeathMale(totalObj.getDeathMale() + repDetobj.getDeathMale());
            totalObj.setDeathFemale(totalObj.getDeathFemale() + repDetobj.getDeathFemale());
            totalObj.setDeathTotal(totalObj.getDeathTotal() + repDetobj.getDeathTotal());
        }
        LOGGER.debug("Completed getTotalObj method");
        return totalObj;
    }

    public ReportDetails getTotalObjforyear(final List<ReportDetails> reportList) {

        LOGGER.debug("Started getTotalObj method");
        final ReportDetails totalObj = new ReportDetails();
        totalObj.setBirthMale(0);
        totalObj.setBirthFemale(0);
        totalObj.setBirthTotal(0);
        totalObj.setDeathMale(0);
        totalObj.setDeathFemale(0);
        totalObj.setDeathTotal(0);
        totalObj.setBirthMaleprogressive(0);
        totalObj.setBirthTotalprogressive(0);
        totalObj.setDeathMaleprogressive(0);
        totalObj.setDeathFemaleprogressive(0);
        totalObj.setDeathTotalprogressive(0);
        totalObj.setStillbirth(0);
        totalObj.setStillbirthprogressive(0);
        totalObj.setInfantdeath(0);
        totalObj.setInfantdeathprogressive(0);
        totalObj.setDeliverydeath(0);
        totalObj.setDeliverydeathprogressive(0);
        totalObj.setCertifieddeath(0);
        totalObj.setCertifieddeathprogressive(0);
        totalObj.setBirthFemaleprogressive(0);

        for (final ReportDetails repDetobj : reportList) {

            totalObj.setBirthMale(totalObj.getBirthMale() + repDetobj.getBirthMale());
            totalObj.setBirthFemale(totalObj.getBirthFemale() + repDetobj.getBirthFemale());
            totalObj.setBirthTotal(totalObj.getBirthTotal() + repDetobj.getBirthTotal());
            totalObj.setDeathMale(totalObj.getDeathMale() + repDetobj.getDeathMale());
            totalObj.setDeathFemale(totalObj.getDeathFemale() + repDetobj.getDeathFemale());
            totalObj.setDeathTotal(totalObj.getDeathTotal() + repDetobj.getDeathTotal());
            totalObj.setBirthMaleprogressive(totalObj.getBirthMaleprogressive() + repDetobj.getBirthMaleprogressive());
            totalObj.setBirthTotalprogressive(totalObj.getBirthTotalprogressive()
                    + repDetobj.getBirthTotalprogressive());
            totalObj.setDeathMaleprogressive(totalObj.getDeathMaleprogressive() + repDetobj.getDeathMaleprogressive());
            totalObj.setDeathFemaleprogressive(totalObj.getDeathFemaleprogressive()
                    + repDetobj.getDeathFemaleprogressive());
            totalObj.setDeathTotalprogressive(totalObj.getDeathTotalprogressive()
                    + repDetobj.getDeathTotalprogressive());
            totalObj.setStillbirth(totalObj.getStillbirth() + repDetobj.getStillbirth());
            totalObj.setStillbirthprogressive(totalObj.getStillbirthprogressive()
                    + repDetobj.getStillbirthprogressive());
            totalObj.setInfantdeath(totalObj.getInfantdeath() + repDetobj.getInfantdeath());
            totalObj.setInfantdeathprogressive(totalObj.getInfantdeathprogressive()
                    + repDetobj.getInfantdeathprogressive());
            totalObj.setDeliverydeath(totalObj.getDeliverydeath() + repDetobj.getDeliverydeath());
            totalObj.setDeliverydeathprogressive(totalObj.getDeliverydeathprogressive()
                    + repDetobj.getDeliverydeathprogressive());
            totalObj.setCertifieddeath(totalObj.getCertifieddeath() + repDetobj.getCertifieddeath());
            totalObj.setCertifieddeathprogressive(totalObj.getCertifieddeathprogressive()
                    + repDetobj.getCertifieddeathprogressive());

        }
        LOGGER.debug("Completed getTotalObj method");
        return totalObj;
    }

    /*
     * public List<FeeCollection> getFeeCollectionDetails(HashMap<String,
     * Object> inputMap) {
     * LOGGER.debug("Started getFeeCollectionDetails method"); Criteria criteria
     * = null; ViewBirthDeathRegistration registrationObj=null; criteria =
     * getBuildCriteria(inputMap); if(!criteria.list().isEmpty())
     * registrationObj=(ViewBirthDeathRegistration) criteria.list().get(0);
     * Criteria feecriteria = null; if(registrationObj!=null){
     * feecriteria=getSession().createCriteria(FeeCollection.class,"feecoll");
     * feecriteria.add(Restrictions.eq("reportId",
     * registrationObj.getRegistrationid()));
     * LOGGER.debug("Completed delayedRegistration method"); return
     * feecriteria.list(); }else{
     * LOGGER.debug("Completed getFeeCollectionDetails method"); return null; }
     * }
     */

    @Transactional
    public long getNumberOfLiveBirths(final HashMap<String, Object> InputMap) {

        LOGGER.debug("Started getNumberOfLiveBirths method");
        InputMap.put(REGTYPE, BndConstants.BIRTH);
        InputMap.put(PROGRESSIVE, Boolean.TRUE);
        InputMap.put(MONTH, "12");
        final Criteria criteria = getBuildCriteria(InputMap);
        LOGGER.debug("Completed getNumberOfLiveBirths method");
        return criteria.list().isEmpty() ? 0 : criteria.list().size();

    }

    @Transactional
    public long getNumberOfDeaths(final HashMap<String, Object> InputMap) {
        LOGGER.debug("Started getNumberOfDeaths method");
        InputMap.put(REGTYPE, BndConstants.DEATH);
        InputMap.put(PROGRESSIVE, Boolean.TRUE);
        InputMap.put(MONTH, "12");
        final Criteria criteria = getBuildCriteria(InputMap);
        LOGGER.debug("Completed getNumberOfDeaths method");
        return criteria.list().isEmpty() ? 0 : criteria.list().size();
    }

    @Transactional
    public long getNumberOfStillBirths(final HashMap<String, Object> InputMap) {
        LOGGER.debug("Started getNumberOfStillBirths method");
        InputMap.put(REGTYPE, BndConstants.STILLBIRTH);
        InputMap.put(PROGRESSIVE, Boolean.TRUE);
        InputMap.put(MONTH, "12");
        final Criteria criteria = getBuildCriteria(InputMap);
        LOGGER.debug("Completed getNumberOfStillBirths method");
        return criteria.list().isEmpty() ? 0 : criteria.list().size();
    }

    @Transactional
    public long getInfantDeaths(final HashMap<String, Object> InputMap) {
        LOGGER.debug("Started getInfantDeaths method");
        InputMap.put(REGTYPE, BndConstants.DEATH);
        InputMap.put(PROGRESSIVE, Boolean.TRUE);
        InputMap.put(MONTH, "12");
        final Criteria criteria = getBuildCriteria(InputMap);
        criteria.createAlias("ageType", "AGETYPE");
        criteria.add(Restrictions.ne("AGETYPE.ageTypedesc", "Year"));
        LOGGER.debug("Completed getInfantDeaths method");
        return criteria.list().isEmpty() ? 0 : criteria.list().size();
    }

    @Transactional
    public long getMaternalDeath(final HashMap<String, Object> InputMap) {
        LOGGER.debug("Started getMaternalDeath method");
        InputMap.put(REGTYPE, BndConstants.DEATH);
        InputMap.put(PROGRESSIVE, Boolean.TRUE);
        InputMap.put(MONTH, "12");
        final Criteria criteria = getBuildCriteria(InputMap);
        criteria.add(Restrictions.eq("isdeathatpreg", "1"));
        LOGGER.debug("Completed getMaternalDeath method");
        return criteria.list().isEmpty() ? 0 : criteria.list().size();
    }

    @Transactional
    public long getPopulation(final HashMap<String, Object> InputMap) {
        LOGGER.debug("Started getPopulation method");
        Criteria criteria = null;
        PopulationDetailMaster populationObj = null;
        criteria = getSession().createCriteria(PopulationDetailMaster.class, "population");
        Restrictions.eq("populationyear", InputMap.get(YEAR).toString());
        if (!criteria.list().isEmpty())
            populationObj = (PopulationDetailMaster) criteria.list().get(0);
        LOGGER.debug("Completed getPopulation method");
        return populationObj.getTotalpopulation();
    }

    /**
     * @param fromDate
     * @param todate
     * @param regType
     * @return
     */

    @Transactional
    public EgovPaginatedList getRegistrationUpdatedList(final Date fromDate, final Date todate, final String regType,
            final String regno, final Integer page, final Integer pageSize) {
        LOGGER.debug("Started getRegistrationUpdatedList method");
        final Criteria registrationHistory = buildCriteriaForUpdatedList(fromDate, todate, regType, regno);
        final Criteria registrationHistoryCount = buildCriteriaForUpdatedList(fromDate, todate, regType, regno);
        final HashSet result = new HashSet(registrationHistoryCount.list());
        final Page resultPage = new Page(registrationHistory, page, pageSize);
        LOGGER.debug("Completed getRegistrationUpdatedList method");
        return new EgovPaginatedList(resultPage, result.size());
    }

    @Transactional
    private Criteria buildCriteriaForUpdatedList(final Date fromDate, final Date todate, final String regType,
            final String regNo) {
        LOGGER.debug("Started buildCriteriaForUpdatedList method");
        final Criteria registrationHistory = getSession().createCriteria(RegistrationHistory.class);
        if (fromDate != null)
            registrationHistory.add(Restrictions.ge("modifiedDate", fromDate));
        if (todate != null)
            registrationHistory.add(Restrictions.le("modifiedDate", todate));
        if (regType != null && !"".equals(regType.trim()))
            registrationHistory.add(Restrictions.eq("type", regType.toUpperCase()));
        if (regNo != null && !"".equals(regNo.trim()))
            registrationHistory.add(Restrictions.eq("registrationno", regNo));
        LOGGER.debug("Completed buildCriteriaForUpdatedList method");
        return registrationHistory;
    }

    @Transactional
    public Map<Integer, String> getStatusMap() {
        STATUSMAP = new TreeMap<Integer, String>();
        final List<EgwStatus> statusList = bndCommonService.findAll("from EgwStatus where moduletype=?",
                BndConstants.BIRTHREGISTRATION);

        for (final EgwStatus statusObj : statusList)
            STATUSMAP.put(statusObj.getId(), statusObj.getCode());

        return STATUSMAP;
    }

}
