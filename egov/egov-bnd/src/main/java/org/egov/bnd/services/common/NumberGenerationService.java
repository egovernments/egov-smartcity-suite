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
package org.egov.bnd.services.common;

import org.apache.log4j.Logger;
import org.egov.bnd.model.BirthRegistration;
import org.egov.bnd.model.DeathRegistration;
import org.egov.bnd.model.NonAvailability;
import org.egov.bnd.model.RegKeys;
import org.egov.bnd.model.Registration;
import org.egov.bnd.model.SideLetter;
import org.egov.bnd.services.masters.RegKeyService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.infstr.utils.SequenceGenerator;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional(readOnly = true)
public class NumberGenerationService {

    private static final Logger LOGGER = Logger.getLogger(NumberGenerationService.class);
    private static final String HYPEN = "-";
    private static final String SLASH = "/";
    private static final String NONAVAILABLE = "NA";
    private static final String SIDELETTER = "SL";
    private static final String HEADQUARTERDEPARTMENT = "HQD";
    private static final String BNDCERTIFICATES = "CERTIFICATE";
    private static final String BND = "BND";
    private SequenceGenerator sequenceGenerator;
    private RegKeyService regKeyService;
    private BndCommonService bndCommonService;

    /**
     * This Api is to auto generate a registration number
     *
     * @param birthRegistration
     * @param roleName
     * @returns the registration number for birthRegistartion
     */

    @Transactional
    public String getBirthRegistrationNumber(final Registration birthRegistration, final Integer hospitalUserFlag) {
        String registrationNumber = null;
        if (birthRegistration.getRegistrationUnit() != null) {
            final StringBuffer regNumberTmp = new StringBuffer("");
            final String type = hospitalUserFlag == 1 ? BndConstants.HOSPITALBIRTH : birthRegistration
                    .getIsChildAdopted() ? BndConstants.CHILDADOPTION : BndConstants.BIRTH;
            final int eventYear = getEventYear(birthRegistration);
            final String objectType = buildObjectType(birthRegistration, eventYear, type);
            regNumberTmp.append(sequenceGenerator.getNextNumber(objectType, Long.valueOf(1)).getNumber().longValue());

            if (hospitalUserFlag == 1)
                regNumberTmp.append(BndConstants.HOSPITALBIRTHSUFFIX);
            else if (birthRegistration.getIsChildAdopted())
                regNumberTmp.append(BndConstants.ADOPTIONSUFFIX);
            else
                updateRegKeys(birthRegistration, regNumberTmp, eventYear, objectType);
            registrationNumber = regNumberTmp.toString();
        }
        LOGGER.debug("Birth Registration number----:" + registrationNumber);
        return registrationNumber;
    }

    @Transactional
    private void updateRegKeys(final Registration registration, final StringBuffer regNumber, final int eventYear,
            final String objectType) {
        final RegKeys regKey = regKeyService.getRegKeyByType(objectType);
        if (regKey == null)
            regKeyService.save(objectType, eventYear, Long.valueOf(regNumber.toString()),
                    Long.valueOf(regNumber.toString()), registration.getRegistrationUnit());
        else
            regKey.setMaxValue(Long.valueOf(regNumber.toString()));
    }

    /**
     * This method is to build object type for birth registration
     *
     * @param birthRegistration
     * @param year
     * @param type
     * @return - It returns object type , which is required to generate the
     *         sequence number for birth registration
     */
    public String buildObjectType(final Registration birthRegistration, final int year, final String type) {
        final StringBuffer objectType = new StringBuffer("");
        objectType.append(type);
        objectType.append(HYPEN);
        objectType.append(birthRegistration.getRegistrationUnit().getRegUnitConst());
        objectType.append(HYPEN);
        objectType.append(year);
        return objectType.toString().toUpperCase();
    }

    /**
     * This method is called on approval of birth registration. If this record
     * is entered by hospital user then on approval of this record it
     * regenerates new birth registration number.
     *
     * @param birthreg
     *            - BirthRegistration object
     * @return
     */

    @Transactional
    public BirthRegistration reGenerateBirthRegistrationNumber(final BirthRegistration birthreg) {
        if (birthreg.getRegistrationNo() != null && !"".equals(birthreg.getRegistrationNo()))
            if (birthreg.getRegistrationNo().contains(BndConstants.HOSPITALBIRTHSUFFIX)) {
                final int eventYear = getEventYear(birthreg);
                final String type = birthreg.getIsChildAdopted() ? BndConstants.CHILDADOPTION : BndConstants.BIRTH;
                final String objectType = buildObjectType(birthreg, eventYear, type);
                RegKeys regKey = regKeyService.getRegKeyByType(objectType);
                if (regKey == null)
                    regKey = regKeyService.save(objectType, eventYear, Long.valueOf(0), Long.valueOf(1),
                            birthreg.getRegistrationUnit());
                birthreg.setRegistrationNo(sequenceGenerator.getNextNumber(objectType, regKey.getMaxValue())
                        .getNumber().toString());
                regKey.setMaxValue(Long.valueOf(birthreg.getRegistrationNo()));
            }
        return birthreg;
    }

    /**
     * This Api is to auto generate a still birth registration number
     *
     * @param birthRegistration
     * @param hospitalUserFlag
     * @returns the registration number for StillBirthRegistartion
     */

    @Transactional
    public String getStillBirthRegistrationNumber(final BirthRegistration birthRegistration,
            final Integer hospitalUserFlag) {
        String registrationNumber = null;
        if (birthRegistration.getRegistrationUnit() != null) {
            final StringBuffer regNumberTmp = new StringBuffer("");
            final int eventYear = getEventYear(birthRegistration);
            final String type = hospitalUserFlag == 1 ? BndConstants.HOSPITALSTILLBIRTH : BndConstants.STILLBIRTHNUM;
            final String objectType = buildObjectType(birthRegistration, eventYear, type);
            regNumberTmp.append(sequenceGenerator.getNextNumber(objectType, Long.valueOf(1)).getNumber().longValue());
            if (hospitalUserFlag == 1)
                regNumberTmp.append(BndConstants.HOSPITALSTILLBIRTHSUFFIX);
            else
                updateRegKeys(birthRegistration, regNumberTmp, eventYear, objectType);
            registrationNumber = regNumberTmp.toString();
        }
        return registrationNumber;
    }

    /**
     * This method is called on approval of birth registration. If this record
     * is entered by hospital user then on approval of this record it
     * regenerates new still birth registration number.
     *
     * @param birthreg
     *            - BirthRegistration object
     * @return
     */
    @Transactional
    public BirthRegistration reGenerateStillBirthRegistrationNumber(final BirthRegistration birthreg) {
        if (birthreg.getRegistrationNo() != null && !"".equals(birthreg.getRegistrationNo()))
            if (birthreg.getRegistrationNo().contains(BndConstants.HOSPITALSTILLBIRTHSUFFIX)) {
                final int eventYear = getEventYear(birthreg);
                final String objectType = buildObjectType(birthreg, eventYear, BndConstants.STILLBIRTHNUM);
                RegKeys regKey = regKeyService.getRegKeyByType(objectType);
                if (regKey == null)
                    regKey = regKeyService.save(objectType, eventYear, Long.valueOf(0), Long.valueOf(1),
                            birthreg.getRegistrationUnit());
                birthreg.setRegistrationNo(sequenceGenerator.getNextNumber(objectType, regKey.getMaxValue())
                        .getNumber().toString());
                regKey.setMaxValue(Long.valueOf(birthreg.getRegistrationNo()));
            }
        return birthreg;
    }

    @Transactional
    public String getDeathRegistrationNumber(final DeathRegistration deathRegistration, final Integer hospitalUserFlag) {
        String registrationNumber = null;
        if (deathRegistration.getRegistrationUnit() != null) {
            final StringBuffer regNumberTmp = new StringBuffer("");
            final int eventYear = getEventYear(deathRegistration);
            final String objectType = buildObjectType(deathRegistration, eventYear,
                    hospitalUserFlag == 1 ? BndConstants.HOSPITALDEATH : BndConstants.DEATH);
            regNumberTmp.append(sequenceGenerator.getNextNumber(objectType, Long.valueOf("1")).getNumber());

            if (hospitalUserFlag == 1)
                regNumberTmp.append(BndConstants.HOSPITALDEATHSUFFIX);
            else
                updateRegKeys(deathRegistration, regNumberTmp, eventYear, objectType);
            registrationNumber = regNumberTmp.toString();
        }
        return registrationNumber;
    }

    @Transactional
    public DeathRegistration reGenerateDeathRegistrationNumber(final DeathRegistration deathreg) {
        if (deathreg.getRegistrationNo() != null && !"".equals(deathreg.getRegistrationNo()))
            if (deathreg.getRegistrationNo().contains(BndConstants.HOSPITALDEATHSUFFIX)) {
                final int eventYear = getEventYear(deathreg);
                final String objectType = buildObjectType(deathreg, eventYear, BndConstants.DEATH);
                RegKeys regKey = regKeyService.getRegKeyByType(objectType);
                if (regKey == null)
                    regKey = regKeyService.save(objectType, eventYear, Long.valueOf(0), Long.valueOf(1),
                            deathreg.getRegistrationUnit());
                deathreg.setRegistrationNo(sequenceGenerator.getNextNumber(objectType, regKey.getMaxValue())
                        .getNumber().toString());
                regKey.setMaxValue(Long.valueOf(deathreg.getRegistrationNo()));
            }
        return deathreg;
    }

    private String buildNonAvailRegObjectType(final NonAvailability nonAvailableReg) {
        final StringBuffer objectType = new StringBuffer("");
        objectType.append(HEADQUARTERDEPARTMENT);
        objectType.append(SLASH);
        objectType.append(NONAVAILABLE);
        objectType.append(SLASH);
        objectType.append(BndDateUtils.getCurrentYear(new Date()));
        return objectType.toString();
    }

    private String buildSideLetterObjectType(final SideLetter sideLetter) {
        final StringBuffer objectType = new StringBuffer("");
        objectType.append(HEADQUARTERDEPARTMENT);
        objectType.append(SLASH);
        objectType.append(SIDELETTER);
        objectType.append(SLASH);
        objectType.append(BndDateUtils.getCurrentYear(new Date()));
        return objectType.toString();
    }

    @Transactional
    public String getNonAvailableRegNumber(final NonAvailability nonAvailableReg) {
        final String objectType = buildNonAvailRegObjectType(nonAvailableReg);
        return sequenceGenerator.getNextNumber(objectType, Long.valueOf(1)).getNumber().toString() + SLASH + objectType;
    }

    @Transactional
    public String getSideLetterRefNumber(final SideLetter sideLetter) {
        final String objectType = buildSideLetterObjectType(sideLetter);
        return sequenceGenerator.getNextNumber(objectType, Long.valueOf(1)).getNumber().toString() + SLASH + objectType;
    }

    @Transactional
    public String getNextCertificateNumber() {
        final String objectType = buildCertificateNumberType();
        return sequenceGenerator.getNextNumber(objectType, Long.valueOf(1)).getNumber().toString();
    }

    private String buildCertificateNumberType() {
        final StringBuffer objectType = new StringBuffer("");
        objectType.append(BND);
        objectType.append(SLASH);
        objectType.append(BNDCERTIFICATES);
        objectType.append(SLASH);
        objectType.append(BndDateUtils.getCurrentYear(new Date()));
        return objectType.toString();
    }

    // If App config value is registration date ,Number will be genearted based
    // on Registration date
    // Otherwise date of event will be considered for number generation...

    public int getEventYear(final Registration registration) {
        final String appConfigValue = bndCommonService.getAppconfigValueResult(BndConstants.BNDMODULE,
                BndConstants.NUMBERGENKEY, BndConstants.REGISTRATIONDATE);
        int eventYear = 0;
        if (appConfigValue != null && BndConstants.REGISTRATIONDATE.equalsIgnoreCase(appConfigValue))
            eventYear = BndDateUtils.getCurrentYear(registration.getRegistrationDate());
        else
            eventYear = BndDateUtils.getCurrentYear(registration.getDateOfEvent());
        return eventYear;
    }

    public void setSequenceGenerator(final SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public void setRegKeyService(final RegKeyService regKeyService) {
        this.regKeyService = regKeyService;
    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

}
