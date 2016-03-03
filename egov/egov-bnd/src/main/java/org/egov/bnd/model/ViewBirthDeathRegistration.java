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
package org.egov.bnd.model;

import java.util.Date;

import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.services.registration.BirthRegistrationService;
import org.egov.bnd.services.registration.DeathRegistrationService;
import org.egov.bnd.utils.BndConstants;
import org.egov.commons.EgwStatus;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.workflow.entity.StateAware;
import org.springframework.web.context.WebApplicationContext;

public class ViewBirthDeathRegistration extends StateAware {

    private static final long serialVersionUID = -8111538102356349677L;
    private Long id;
    private String type;
    private Long registrationid;
    private String registrationno;
    private Date registrationdate;
    private RegistrationUnit regunitid;
    private Registration registrationObject;
    private String isdeathatpreg;
    private Long registration;
    private Integer age;
    private Date eventdate;
    private Establishment establishmentmasterid;
    private Address eventaddressid;
    private Address deceasedaddressid;
    private AgeType ageType;
    private EgwStatus registrationstatus;
    private String hospitalregno;
    private Boolean ismainregunit;
    private String regunitactive;
    private BnDCitizen citizenid;
    private String firstname;
    private String middlename;
    private String lastname;
    private String sex;
    private PlaceType placeid;
    private AdoptionDetails adoptionid;
    private Character stillbirth;
    private int eventRegistrationdiff;
    private Integer eventregdatediff;
    private BndCommonService bndcommonservice;
    private String certifiedmed;

    public String getPlaceofEvent() {
        return getRegistrationObject().getPlaceOfEventAddress().toString();
    }

    public void setPlaceofEvent(final String placeofEvent) {
    }

    public String getCitizenFullName() {
        return getRegistrationObject().getCitizenName().toString();
    }

    public void setCitizenFullName(final String citizenFullName) {
    }

    public void setHospitalRegNo(final String hospitalRegNo) {
    }

    public String getCertifiedmed() {
        return certifiedmed;
    }

    public void setCertifiedmed(final String certifiedmed) {
        this.certifiedmed = certifiedmed;
    }

    public BndCommonService getBndcommonservice() {
        return bndcommonservice;
    }

    public void setBndcommonservice(final BndCommonService bndcommonservice) {
        this.bndcommonservice = bndcommonservice;
    }

    public Integer getEventregdatediff() {
        return eventregdatediff;
    }

    public void setEventregdatediff(final Integer eventregdatediff) {
        this.eventregdatediff = eventregdatediff;
    }

    public int getEventRegistrationdiff() {
        return eventRegistrationdiff;
    }

    public void setEventRegistrationdiff(final int eventRegistrationdiff) {
        this.eventRegistrationdiff = eventRegistrationdiff;
    }

    public PlaceType getPlaceid() {
        return placeid;
    }

    public void setPlaceid(final PlaceType placeid) {
        this.placeid = placeid;
    }

    public AdoptionDetails getAdoptionid() {
        return adoptionid;
    }

    public void setAdoptionid(final AdoptionDetails adoptionid) {
        this.adoptionid = adoptionid;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public AgeType getAgeType() {
        return ageType;
    }

    public void setAgeType(final AgeType ageType) {
        this.ageType = ageType;
    }

    public Establishment getEstablishmentmasterid() {
        return establishmentmasterid;
    }

    public void setEstablishmentmasterid(final Establishment establishmentmasterid) {
        this.establishmentmasterid = establishmentmasterid;
    }

    public Address getEventaddressid() {
        return eventaddressid;
    }

    public void setEventaddressid(final Address eventaddressid) {
        this.eventaddressid = eventaddressid;
    }

    public Address getDeceasedaddressid() {
        return deceasedaddressid;
    }

    public void setDeceasedaddressid(final Address deceasedaddressid) {
        this.deceasedaddressid = deceasedaddressid;
    }

    public Character getStillbirth() {
        return stillbirth;
    }

    public void setStillbirth(final Character stillbirth) {
        this.stillbirth = stillbirth;
    }

    public EgwStatus getRegistrationstatus() {
        return registrationstatus;
    }

    public void setRegistrationstatus(final EgwStatus registrationstatus) {
        this.registrationstatus = registrationstatus;
    }

    public Boolean getIsmainregunit() {
        return ismainregunit;
    }

    public void setIsmainregunit(final Boolean ismainregunit) {
        this.ismainregunit = ismainregunit;
    }

    public BnDCitizen getCitizenid() {
        return citizenid;
    }

    public void setCitizenid(final BnDCitizen citizenid) {
        this.citizenid = citizenid;
    }

    public Long getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(final Long registrationid) {
        this.registrationid = registrationid;
    }

    public String getRegistrationno() {
        return registrationno;
    }

    public void setRegistrationno(final String registrationno) {
        this.registrationno = registrationno;
    }

    public Date getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(final Date registrationdate) {
        this.registrationdate = registrationdate;
    }

    public String getIsdeathatpreg() {
        return isdeathatpreg;
    }

    public void setIsdeathatpreg(final String isdeathatpreg) {
        this.isdeathatpreg = isdeathatpreg;
    }

    public Date getEventdate() {
        return eventdate;
    }

    public void setEventdate(final Date eventdate) {
        this.eventdate = eventdate;
    }

    public String getHospitalregno() {
        return hospitalregno;
    }

    public void setHospitalregno(final String hospitalregno) {
        this.hospitalregno = hospitalregno;
    }

    public String getRegunitactive() {
        return regunitactive;
    }

    public void setRegunitactive(final String regunitactive) {
        this.regunitactive = regunitactive;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(final String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    public RegistrationUnit getRegunitid() {
        return regunitid;
    }

    public void setRegunitid(final RegistrationUnit regunitid) {
        this.regunitid = regunitid;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String getStateDetails() {
        return null;
    }

    public Long getRegistration() {
        return registration;
    }

    public void setRegistration(final Long registration) {
        this.registration = registration;
    }

    public Registration getRegistrationObject() {

        if (type != null)
            try {
                String regTypeService;
                if (type.equals("DEATH"))
                    regTypeService = "DeathRegistrationService";
                else
                    regTypeService = "BirthRegistrationService";

                //FIX ME : ServletActionContext outside Action class, which does not work outside struts2 context
                final WebApplicationContext wac = null; /*WebApplicationContextUtils
                        .getWebApplicationContext(ServletActionContext.getServletContext());*/
                final BndCommonService registrationPersistenceService = (BndCommonService) wac
                        .getBean(BndConstants.BNDCOMMONSERVICE);

                if (registrationPersistenceService != null
                        && registrationPersistenceService.getRegistrationTypeServiceMap().get(regTypeService) != null)
                    if (regTypeService.equals("DeathRegistrationService")) {
                        registrationObject = ((DeathRegistrationService) registrationPersistenceService
                                .getRegistrationTypeServiceMap().get(regTypeService))
                                .getDeathRegistrationById(registrationid);

                        return registrationObject;
                    } else
                        registrationObject = ((BirthRegistrationService) registrationPersistenceService
                                .getRegistrationTypeServiceMap().get(regTypeService))
                                .getBirthRegistrationById(registrationid);

                return registrationObject;

            } catch (final Exception e) {
                throw new ApplicationRuntimeException(
                        "Error in BndCommonService/ViewBirthdeathRegistration -getRegistrationObject() method");
            }
        return registrationObject;

    }

    public String getMotherName() {

        if (getRegistrationObject().getMotherFullName() != null
                && !getRegistrationObject().getMotherFullName().toString().equals(""))
            return getRegistrationObject().getMotherFullName().toString();
        else
            return "NA";

    }

    public String getFatherName() {

        if (getRegistrationObject().getFatherFullName() != null
                && !getRegistrationObject().getFatherFullName().toString().equals(""))
            return getRegistrationObject().getFatherFullName().toString();
        else
            return "NA";
    }

    public String getHospitalRegNo() {
        if (getRegistrationObject().getHospitalRegistrationNo() != null
                && !getRegistrationObject().getHospitalRegistrationNo().equals(""))
            return getRegistrationObject().getHospitalRegistrationNo();
        else
            return "NA";

    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
