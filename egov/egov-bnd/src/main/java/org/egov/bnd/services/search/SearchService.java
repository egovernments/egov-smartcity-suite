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
package org.egov.bnd.services.search;

import org.egov.bnd.model.BirthRegistration;
import org.egov.bnd.model.CitizenRelation;
import org.egov.bnd.model.DeathRegistration;
import org.egov.bnd.model.Registration;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

@Transactional(readOnly = true)
public class SearchService extends PersistenceService<Registration, Long> {

    private BndCommonService bndCommonService;

    @Transactional
    public EgovPaginatedList searchRecords(final HashMap<String, Object> hashMap, final Integer page, final int pagesize) {
        final Criteria criteria = getBuildCriteria(hashMap);
        final Criteria issueLineCountCriteria = getBuildCriteria(hashMap);
        final Page resultPage = new Page(criteria, page, pagesize);
        final HashSet result = new HashSet(issueLineCountCriteria.list());
        final int count = result.size();
        final EgovPaginatedList pagedResults = new EgovPaginatedList(resultPage, count);
        return pagedResults;
    }

    @Transactional
    private Criteria getBuildCriteria(final HashMap<String, Object> hashMap) {
        Criteria criteria = null;
        if (hashMap.get("REGTYPE") != null) {
            if (hashMap.get("REGTYPE").equals(BndConstants.SEARCHBIRTH)
                    || hashMap.get("REGTYPE").equals(BndConstants.SEARCHSTILLBIRTH))
                criteria = getSession().createCriteria(BirthRegistration.class, "birth")
                .createAlias("birth.citizen", "citizenObj").createAlias("birth.placeType", "placetype")
                .createAlias("birth.citizenBDDetails", "citizenBDDetail")
                .createAlias("birth.status", "statusObj");
            else if (hashMap.get("REGTYPE").equals(BndConstants.SEARCHDEATH))
                criteria = getSession().createCriteria(DeathRegistration.class, "death")
                .createAlias("death.citizen", "citizenObj").createAlias("death.placeType", "placetype")
                .createAlias("death.status", "statusObj");

            /*
             * to show only approved records in the search result
             */
            EgwStatus status;

            if (hashMap.get("REGTYPE").equals(BndConstants.SEARCHSTILLBIRTH)) {
                criteria.add(Restrictions.eq("citizenBDDetail.isStillBirth", Character.valueOf('Y')));
                if (hashMap.get("MODE").equals(BndConstants.MODEUNLOCK)) {
                    /* to search locked records */
                    status = bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,
                            BndConstants.LOCK);
                    criteria.add(Restrictions.eq("statusObj.id", status.getId()));
                } else {
                    status = bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,
                            BndConstants.APPROVED);
                    criteria.add(Restrictions.eq("statusObj.id", status.getId()));
                }
            } else if (hashMap.get("REGTYPE").equals(BndConstants.SEARCHBIRTH)) {

                criteria.add(Restrictions.eq("citizenBDDetail.isStillBirth", Character.valueOf('N')));
                /* to search locked records */
                if (hashMap.get("MODE").equals(BndConstants.MODEUNLOCK)) {
                    status = bndCommonService.getStatusByModuleAndCode(BndConstants.BIRTHREGISTRATION,
                            BndConstants.LOCK);
                    criteria.add(Restrictions.eq("statusObj.id", status.getId()));
                } else {
                    status = bndCommonService.getStatusByModuleAndCode(BndConstants.BIRTHREGISTRATION,
                            BndConstants.APPROVED);
                    criteria.add(Restrictions.eq("statusObj.id", status.getId()));
                }
            } else if (hashMap.get("REGTYPE").equals(BndConstants.SEARCHDEATH))
                /* to search locked records */
                if (hashMap.get("MODE").equals(BndConstants.MODEUNLOCK)) {
                    status = bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                            BndConstants.LOCK);
                    criteria.add(Restrictions.eq("statusObj.id", status.getId()));
                } else {
                    status = bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                            BndConstants.APPROVED);
                    criteria.add(Restrictions.eq("statusObj.id", status.getId()));
                }

            if (hashMap.get("REGNO") != null && !hashMap.get("REGNO").equals("")) {
                criteria.add(Restrictions.ilike("registrationNo", hashMap.get("REGNO")));

                if (hashMap.get("REGYEAR") != null && !hashMap.get("REGYEAR").equals("")) {
                    final Date startDate = BndDateUtils.getStartDateFromYear((Integer) hashMap.get("REGYEAR"));
                    final Date endDate = BndDateUtils.getEndDateFromYear((Integer) hashMap.get("REGYEAR"));

                    if (hashMap.get("REGTYPE") != null
                            && (hashMap.get("REGTYPE").equals(BndConstants.SEARCHBIRTH) || hashMap.get("REGTYPE")
                                    .equals(BndConstants.SEARCHSTILLBIRTH))) {
                        criteria.add(Restrictions.gt("registrationDate", startDate));
                        criteria.add(Restrictions.le("registrationDate", endDate));
                        // criteria.add(Restrictions.sqlRestriction(" extract(year from {alias}.BIRTHDATE)="+
                        // (String)hashMap.get("REGYEAR")));
                    } else if (hashMap.get("REGTYPE") != null
                            && hashMap.get("REGTYPE").equals(BndConstants.SEARCHDEATH)) {
                        criteria.add(Restrictions.gt("registrationDate", startDate));
                        criteria.add(Restrictions.le("registrationDate", endDate));
                        // criteria.add(Restrictions.sqlRestriction(" extract(year from {alias}.DEATHDATE)="+
                        // (String)hashMap.get("REGYEAR")));
                    }
                }
            } else {

                if (hashMap.get("FROMDATE") != null && !hashMap.get("FROMDATE").equals(""))
                    criteria.add(Restrictions.ge("dateOfEvent", hashMap.get("FROMDATE")));

                if (hashMap.get("TODATE") != null && !hashMap.get("TODATE").equals(""))
                    criteria.add(Restrictions.le("dateOfEvent", hashMap.get("TODATE")));

                if (hashMap.get("FIRSTNAME") != null && !hashMap.get("FIRSTNAME").equals(""))
                    criteria.add(Restrictions.ilike("citizenObj.firstName", (String) hashMap.get("FIRSTNAME"),
                            MatchMode.ANYWHERE));

                if (hashMap.get("MIDNAME") != null && !hashMap.get("MIDNAME").equals(""))
                    criteria.add(Restrictions.ilike("citizenObj.middleName", (String) hashMap.get("MIDNAME"),
                            MatchMode.ANYWHERE));

                if (hashMap.get("LASTNAME") != null && !hashMap.get("LASTNAME").equals(""))
                    criteria.add(Restrictions.ilike("citizenObj.lastName", (String) hashMap.get("LASTNAME"),
                            MatchMode.ANYWHERE));

                if (hashMap.get("SEXTYPE") != null && !hashMap.get("SEXTYPE").equals("-1"))
                    criteria.add(Restrictions.ilike("citizenObj.sex", hashMap.get("SEXTYPE")));

                /*
                 * if a child is adopted then real parents are hided and search
                 * and result is w.r.t adoptee parents(no where real parents are
                 * shown)
                 */
                if (hashMap.get("FATHERNAME") != null && !hashMap.get("FATHERNAME").equals("")
                        || hashMap.get("MOTHERNAME") != null && !hashMap.get("MOTHERNAME").equals("")) {
                    criteria.createAlias("citizenObj.relations", "relation")
                    .createAlias("relation.relatedAs", "relatedas")
                    .createAlias("relation.person", "personname");

                    Criterion critAdopteeFather = null;
                    Criterion critRealFather = null;
                    Criterion critAdopteeMother = null;
                    Criterion critRealMother = null;
                    Criterion critAdopteeParentsResult = null;
                    Criterion critRealParentsResult = null;

                    // Get relations. Project father from the list. add
                    // condition like father name.
                    // In relations. Project mother from the list. add condition
                    // like mother name.

                    if (hashMap.get("FATHERNAME") != null && !hashMap.get("FATHERNAME").equals("")) {
                        final String tempFatherName = (String) hashMap.get("FATHERNAME");
                        final String fatherName = tempFatherName.replace(" ", "");

                        /*
                         * get father name from the detached criteria and
                         * compare with search parameter(real father)
                         */
                        final DetachedCriteria subCriteriafather = createDetachedCriteriaFather();
                        subCriteriafather.setProjection(Property.forName("persnFather"));
                        subCriteriafather.setProjection(Property.forName("child.id"));
                        subCriteriafather.add(Restrictions.eqProperty("child.id", "citizenObj.id"));
                        subCriteriafather.add(Restrictions
                                .ilike("persnFather.fullName", fatherName, MatchMode.ANYWHERE));

                        if (hashMap.get("REGTYPE") != null && hashMap.get("REGTYPE").equals(BndConstants.SEARCHBIRTH)) {
                            critRealFather = Restrictions.conjunction()
                                    .add(Restrictions.eq("isChildAdopted", Boolean.FALSE))
                                    .add(Subqueries.propertyIn("citizenObj.id", subCriteriafather));

                            /*
                             * get adoptee father name from the detached
                             * criteria and compare with search
                             * parameter(adoptee father)
                             */
                            final DetachedCriteria subCriteriaAdoption = createDetachedCriteriaAdoption();
                            subCriteriaAdoption.setProjection(Property.forName("id"));
                            subCriteriaAdoption.add(Restrictions.ilike("adoptionDetailsFather.fullName", fatherName,
                                    MatchMode.ANYWHERE));
                            critAdopteeFather = Subqueries.propertyIn("id", subCriteriaAdoption);
                        } else
                            critRealFather = Subqueries.propertyIn("citizenObj.id", subCriteriafather);

                    }

                    if (hashMap.get("MOTHERNAME") != null && !hashMap.get("MOTHERNAME").equals("")) {
                        final String tempMotherName = (String) hashMap.get("MOTHERNAME");
                        final String motherName = tempMotherName.replace(" ", "");

                        /*
                         * get mother name from the detached criteria and
                         * compare with search parameter(real mother)
                         */
                        final DetachedCriteria subCriteriaMother = createDetachedCriteriaMother();
                        subCriteriaMother.setProjection(Property.forName("persnMother"));
                        subCriteriaMother.setProjection(Property.forName("childObj.id"));
                        subCriteriaMother.add(Restrictions.eqProperty("childObj.id", "citizenObj.id"));
                        subCriteriaMother.add(Restrictions
                                .ilike("persnMother.fullName", motherName, MatchMode.ANYWHERE));

                        if (hashMap.get("REGTYPE") != null && hashMap.get("REGTYPE").equals(BndConstants.SEARCHBIRTH)) {
                            critRealMother = Restrictions.conjunction()
                                    .add(Restrictions.eq("isChildAdopted", Boolean.FALSE))
                                    .add(Subqueries.propertyIn("citizenObj.id", subCriteriaMother));

                            /*
                             * get adoptee mother name from the detached
                             * criteria and compare with search
                             * parameter(adoptee mother)
                             */
                            final DetachedCriteria subCriteriaAdoption = createDetachedCriteriaAdoption();
                            subCriteriaAdoption.setProjection(Property.forName("id"));
                            subCriteriaAdoption.add(Restrictions.ilike("adoptionDetailsMother.fullName", motherName,
                                    MatchMode.ANYWHERE));
                            critAdopteeMother = Subqueries.propertyIn("id", subCriteriaAdoption);
                        } else
                            critRealMother = Subqueries.propertyIn("citizenObj.id", subCriteriaMother);
                    }

                    /*
                     * if both real mother and real father are given as search
                     * parameters then perform conjuction
                     */
                    if (critRealMother != null && critRealFather != null)
                        critRealParentsResult = Restrictions.conjunction().add(critRealMother).add(critRealFather);
                    else if (critRealMother == null && critRealFather != null)
                        critRealParentsResult = critRealFather;
                    else if (critRealFather == null && critRealMother != null)
                        critRealParentsResult = critRealMother;

                    /*
                     * if both adoptee mother and adoptee father both are given
                     * as search parameters then perform conjuction
                     */
                    if (critAdopteeMother != null && critAdopteeFather != null)
                        critAdopteeParentsResult = Restrictions.conjunction().add(critAdopteeMother)
                        .add(critAdopteeFather);
                    else if (critAdopteeMother == null && critAdopteeFather != null)
                        critAdopteeParentsResult = critAdopteeFather;
                    else if (critAdopteeFather == null && critAdopteeMother != null)
                        critAdopteeParentsResult = critAdopteeMother;

                    /*
                     * perform disjunction because to get all the matching
                     * parents name (if adoption els real parents) ex: real
                     * father name of child1 is "abc" and adopted father name of
                     * child2 is also "abc" both results should be displayd
                     * hence disjunction
                     */
                    if (critAdopteeParentsResult != null && critRealParentsResult != null)
                        criteria.add(Restrictions.disjunction().add(critRealParentsResult)
                                .add(critAdopteeParentsResult));
                    else if (critAdopteeParentsResult == null && critRealParentsResult != null)
                        criteria.add(critRealParentsResult);
                    else if (critRealParentsResult == null && critAdopteeParentsResult != null)
                        criteria.add(critAdopteeParentsResult);

                }

                if (hashMap.get("EVENTTYPE") != null && !hashMap.get("EVENTTYPE").equals(""))
                    criteria.add(Restrictions.eq("placetype.desc", hashMap.get("EVENTTYPE")));

                if (hashMap.get("REGTYPE") != null
                        && (hashMap.get("REGTYPE").equals(BndConstants.SEARCHBIRTH) || hashMap.get("REGTYPE").equals(
                                BndConstants.SEARCHSTILLBIRTH))) {

                    if (hashMap.get("HOSPITALID") != null && !hashMap.get("HOSPITALID").equals(-1)) {
                        criteria.createAlias("birth.establishment", "hospital");
                        criteria.add(Restrictions.eq("hospital.id", hashMap.get("HOSPITALID")));
                    }

                    if (hashMap.get("PINCODE") != null && !hashMap.get("PINCODE").equals("")) {
                        criteria.createAlias("birth.eventAddress", "address");
                        criteria.add(Restrictions.eq("address.pinCode", hashMap.get("PINCODE")));
                    }

                    if (hashMap.get("REGISTRATIONUNITID") != null && !hashMap.get("REGISTRATIONUNITID").equals("")) {
                        criteria.createAlias("birth.registrationUnit", "regUnit");
                        criteria.add(Restrictions.eq("regUnit.id", hashMap.get("REGISTRATIONUNITID")));
                    }
                } else if (hashMap.get("REGTYPE") != null && hashMap.get("REGTYPE").equals(BndConstants.SEARCHDEATH)) {

                    if (hashMap.get("HOSPITALID") != null && !hashMap.get("HOSPITALID").equals(-1)) {
                        criteria.createAlias("death.establishment", "hospital");
                        criteria.add(Restrictions.eq("hospital.id", hashMap.get("HOSPITALID")));
                    }

                    if (hashMap.get("PINCODE") != null && !hashMap.get("PINCODE").equals("")) {
                        criteria.createAlias("death.eventAddress", "address");
                        criteria.add(Restrictions.eq("address.pinCode", hashMap.get("PINCODE")));
                    }

                    if (hashMap.get("REGISTRATIONUNITID") != null && !hashMap.get("REGISTRATIONUNITID").equals(-1)) {
                        criteria.createAlias("death.registrationUnit", "regUnit");
                        criteria.add(Restrictions.eq("regUnit.id", hashMap.get("REGISTRATIONUNITID")));
                    }
                }
            }
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            criteria.addOrder(Order.asc("registrationNo"));
        }

        return criteria;

    }

    /*
     * detached criteria to get father from CitizenRelation
     */
    @Transactional
    private DetachedCriteria createDetachedCriteriaFather() {

        final DetachedCriteria subCriteria = DetachedCriteria.forClass(CitizenRelation.class, "citFather")
                .createAlias("citFather.person", "persnFather").createAlias("citFather.relatedAs", "related")
                .createAlias("citFather.cit", "child");

        subCriteria.add(Restrictions.ilike("related.desc", BndConstants.FATHER));
        return subCriteria;
    }

    /*
     * detached criteria to get mother from CitizenRelation
     */
    @Transactional
    private DetachedCriteria createDetachedCriteriaMother() {

        final DetachedCriteria subCriteria = DetachedCriteria.forClass(CitizenRelation.class, "citMother")
                .createAlias("citMother.person", "persnMother").createAlias("citMother.relatedAs", "relatedMother")
                .createAlias("citMother.cit", "childObj");

        subCriteria.add(Restrictions.ilike("relatedMother.desc", BndConstants.MOTHER));
        return subCriteria;

    }

    /*
     * detatched criteria to get adoption details
     */
    @Transactional
    private DetachedCriteria createDetachedCriteriaAdoption() {
        final DetachedCriteria subCriteria = DetachedCriteria.forClass(BirthRegistration.class, "birthadpt")
                .createAlias("birthadpt.adoptionDetail", "adoptiondetails")
                .createAlias("adoptiondetails.adopteeFather", "adoptionDetailsFather")
                .createAlias("adoptiondetails.adopteeMother", "adoptionDetailsMother");

        subCriteria.add(Restrictions.eq("isChildAdopted", Boolean.TRUE));
        return subCriteria;
    }

    public BndCommonService getBndCommonService() {
        return bndCommonService;
    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

}
