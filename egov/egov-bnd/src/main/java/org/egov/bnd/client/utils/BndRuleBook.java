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
package org.egov.bnd.client.utils;

import org.egov.bnd.model.Registrar;
import org.egov.bnd.model.Registration;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a single ton class This class defines what are the actions can be
 * performed by a particular role. This class needs to be changed as per the
 * clients requirement.
 *
 * @author pritiranjan
 */

@SuppressWarnings("unchecked")
public class BndRuleBook {

    // @BIRTHSTATUSAPPROVED - There is no need of work flow for
    // BirthRegistration,it will be entered in approved status.
    public static final String BIRTHSTATUSAPPROVED = "BIRTHSTATUSAPPROVED";
    // @BIRTHSTATUSCREATED - Work flow is required for Birth,it will be entered
    // in created status.
    public static final String BIRTHSTATUSCREATED = "BIRTHSTATUSCREATED";
    // @BIRTHDELAYEDREG - Delayed registration is allowed
    public static final String BIRTHDELAYEDREG = "BIRTHDELAYEDREG";
    // @BIRTHADOPTIONDETAILS - Delayed registration is allowed
    public static final String BIRTHADOPTIONDETAILS = "BIRTHADOPTIONDETAILS";
    // @BIRTHSTATISTICALINFO - Statistical Information can be entered
    public static final String BIRTHSTATISTICALINFO = "BIRTHSTATISTICALINFO";
    // @BIRTHPLACEHOSPITAL - Birth place is hospital
    public static final String BIRTHPLACEHOSPITAL = "BIRTHPLACEHOSPITAL";
    // @BIRTHPLACEHOSPITAL - Birth place is hospital
    public static final String CURRENTYEARBIRTHREG = "CURRENTYEARBIRTHREG";

    public static final String DEATHSTATUSAPPROVED = "DEATHSTATUSAPPROVED";
    public static final String DEATHSTATUSCREATED = "DEATHSTATUSCREATED";
    public static final String DEATHDELAYEDREG = "DEATHDELAYEDREG";
    public static final String DEATHSTATISTICALINFO = "DEATHSTATISTICALINFO";
    public static final String DEATHPLACEHOSPITAL = "DEATHPLACEHOSPITAL";
    // Role Names
    public static final String REGISTRAR = "REGISTRAR";
    public static final String HOSPITALREGISTRAR = "HOSPITALREGISTRAR";
    public static final String SUPERUSER = "SUPERUSER";
    public static final String HOSPITALUSER = "HOSPITALUSER";
    public static final String OPERATOR = "OPERATOR";
    public static final String ACCOUNTANT = "ACCOUNTANT";
    public static final String CITIZEN = "CITIZEN";
    public static final String DEFAULT = "DEFAULT";
    public static final String ADMIN = "ADMIN";
    // No such role in database(for internal reference)
    // if Ismainregunit is true then its headQuater Registrar
    public static final String HEADQUATERREGISTRAR = "HEADQUATERREGISTRAR";
    public static final String HEADQUATERREGISTRARHRRECORDWITHIN1YEAR = "HEADQUATERREGISTRARHRRECORDWITHIN1YEAR";
    public static final String REGISTRAROWNREGUNIT = "REGISTRAROWNREGUNIT";
    public static final String REGISTRAROTHERREGUNIT = "REGISTRAROTHERREGUNIT";
    public static final String REGISTRARHRRECORDWITHIN1YEAR = "REGISTRARHRRECORDWITHIN1YEAR";
    public static final String REGISTRARHRRECORDAFTER1YEAROWNREGUNIT = "REGISTRARHRRECORDAFTER1YEAROWNREGUNIT";
    public static final String REGISTRARHRRECORDAFTER1YEAROTHERREGUNIT = "REGISTRARHRRECORDAFTER1YEAROTHERREGUNIT";
    public static final String HROWNRECORDWITHIN1YEAR = "HROWNRECORDWITHIN1YEAR";
    public static final String HROWNRECORDAFTER1YEAR = "HROWNRECORDAFTER1YEAR";
    public static final String HROTHERRECORDS = "HROTHERRECORDS";

    public static final String HROTHERRECORD = "HROTHERRECORD";
    public static final String HOSPITALUSEROWNRECORD = "HOSPITALUSEROWNRECORD";
    public static final String HOSPITALUSEROTHERRECORD = "HOSPITALUSEROTHERRECORD";
    public static final String OPERATOROWNREGUNIT = "OPERATOROWNREGUNIT";
    public static final String OPERATORPOTHERREGUNIT = "OPERATORPOTHERREGUNIT";

    public static final Map<String, List<String>> BIRTHROLEACTIONMAP = new HashMap<String, List<String>>();
    public static final Map<String, List<String>> DEATHROLEACTIONMAP = new HashMap<String, List<String>>();
    public static final Map<String, List<String>> SEARCHROLEACTIONMAP = new HashMap<String, List<String>>();

    private static final BndRuleBook ruleBook = new BndRuleBook();

    private BndRuleBook() {
        super();
    }

    static

    {

        final List<String> registrarActions = Arrays.asList(BIRTHSTATUSAPPROVED, BIRTHDELAYEDREG, BIRTHADOPTIONDETAILS,
                BIRTHSTATISTICALINFO);
        BIRTHROLEACTIONMAP.put(SUPERUSER, registrarActions);
        BIRTHROLEACTIONMAP.put(HEADQUATERREGISTRAR, registrarActions);
        BIRTHROLEACTIONMAP.put(REGISTRAR, Arrays.asList(BIRTHSTATUSAPPROVED, BIRTHDELAYEDREG, BIRTHSTATISTICALINFO));
        BIRTHROLEACTIONMAP.put(HOSPITALREGISTRAR, Arrays.asList(BIRTHSTATUSAPPROVED, BIRTHSTATISTICALINFO,
                BIRTHADOPTIONDETAILS, CURRENTYEARBIRTHREG, BIRTHPLACEHOSPITAL));
        BIRTHROLEACTIONMAP.put(HOSPITALUSER,
                Arrays.asList(BIRTHSTATUSCREATED, BIRTHPLACEHOSPITAL, BIRTHSTATISTICALINFO, CURRENTYEARBIRTHREG));
        BIRTHROLEACTIONMAP
        .put(OPERATOR, Arrays.asList(BIRTHSTATUSAPPROVED, BIRTHADOPTIONDETAILS, BIRTHSTATISTICALINFO));
        BIRTHROLEACTIONMAP.put(ACCOUNTANT,
                Arrays.asList(BIRTHSTATUSCREATED, BIRTHADOPTIONDETAILS, BIRTHSTATISTICALINFO));
        BIRTHROLEACTIONMAP.put(DEFAULT, registrarActions);

        final List<String> defaultActions = Arrays.asList(DEATHSTATUSCREATED, DEATHDELAYEDREG, DEATHSTATISTICALINFO);
        DEATHROLEACTIONMAP.put(SUPERUSER, Arrays.asList(DEATHSTATUSCREATED, DEATHDELAYEDREG, DEATHSTATISTICALINFO));
        DEATHROLEACTIONMAP.put(REGISTRAR, Arrays.asList(DEATHSTATUSCREATED, DEATHDELAYEDREG, DEATHSTATISTICALINFO));
        DEATHROLEACTIONMAP.put(HOSPITALREGISTRAR,
                Arrays.asList(DEATHSTATUSCREATED, DEATHSTATISTICALINFO, CURRENTYEARBIRTHREG, DEATHPLACEHOSPITAL));
        DEATHROLEACTIONMAP.put(HOSPITALUSER, Arrays.asList(DEATHSTATUSCREATED, DEATHDELAYEDREG, DEATHPLACEHOSPITAL,
                DEATHSTATISTICALINFO, CURRENTYEARBIRTHREG));
        DEATHROLEACTIONMAP.put(OPERATOR, Arrays.asList(DEATHSTATUSAPPROVED, DEATHSTATISTICALINFO));
        DEATHROLEACTIONMAP.put(ACCOUNTANT, Arrays.asList(DEATHSTATUSCREATED, DEATHSTATISTICALINFO));
        DEATHROLEACTIONMAP.put(DEFAULT, defaultActions);

        final List<String> searchActions = Arrays.asList(BndConstants.VIEW, BndConstants.UPDATE,
                BndConstants.CHILDADOPTION, BndConstants.CERTIFICATEGENERATION,
                BndConstants.CERTIFICATEGENERATIONFORBIRTH, BndConstants.SIDELETTER, BndConstants.LOCKRECORD,
                BndConstants.NAMEINCLUSION);
        SEARCHROLEACTIONMAP.put(SUPERUSER, searchActions);
        SEARCHROLEACTIONMAP.put(HEADQUATERREGISTRAR, searchActions);

        // if record is entered by hospital registrar and is within 1year the
        // HQR can perform these actions
        SEARCHROLEACTIONMAP.put(HEADQUATERREGISTRARHRRECORDWITHIN1YEAR,
                Arrays.asList(BndConstants.VIEW, BndConstants.LOCKRECORD));

        // if record is entered by registrar and its his unit record then he can
        // perform these actions
        SEARCHROLEACTIONMAP.put(REGISTRAROWNREGUNIT, Arrays.asList(BndConstants.VIEW, BndConstants.UPDATE,
                BndConstants.CERTIFICATEGENERATION, BndConstants.CERTIFICATEGENERATIONFORBIRTH,
                BndConstants.NAMEINCLUSION));

        // if record is entered by registrar and its other registration unit
        // record then he can perform these actions
        SEARCHROLEACTIONMAP.put(REGISTRAROTHERREGUNIT, Arrays.asList(BndConstants.VIEW,
                BndConstants.CERTIFICATEGENERATION, BndConstants.CERTIFICATEGENERATIONFORBIRTH));

        // if record is entered by hospital registrar and is within 1year then
        // Registrar can perform these actions
        SEARCHROLEACTIONMAP.put(REGISTRARHRRECORDWITHIN1YEAR, Arrays.asList(BndConstants.VIEW));

        // if record is entered by hospital registrar and is After 1year and its
        // his unit record then Registrar can perform these actions
        SEARCHROLEACTIONMAP.put(REGISTRARHRRECORDAFTER1YEAROWNREGUNIT, Arrays.asList(BndConstants.VIEW,
                BndConstants.UPDATE, BndConstants.CERTIFICATEGENERATION, BndConstants.CERTIFICATEGENERATIONFORBIRTH));

        // if record is entered by hospital registrar and is After 1year and its
        // other unit record then Registrar can perform these actions
        SEARCHROLEACTIONMAP.put(REGISTRARHRRECORDAFTER1YEAROTHERREGUNIT, Arrays.asList(BndConstants.VIEW,
                BndConstants.CERTIFICATEGENERATION, BndConstants.CERTIFICATEGENERATIONFORBIRTH));

        // if record is entered by hospital registrar and is within 1year and
        // its his unit record the Hospital Registrar can perform these actions
        SEARCHROLEACTIONMAP.put(HROWNRECORDWITHIN1YEAR, Arrays.asList(BndConstants.VIEW, BndConstants.UPDATE,
                BndConstants.CHILDADOPTION, BndConstants.CERTIFICATEGENERATION,
                BndConstants.CERTIFICATEGENERATIONFORBIRTH, BndConstants.NAMEINCLUSION));

        // if record is entered by hospital registrar and is After 1year and its
        // his unit record the Hospital Registrar can perform these actions
        SEARCHROLEACTIONMAP.put(HROWNRECORDAFTER1YEAR, Arrays.asList(BndConstants.VIEW));

        // if record is entered by some other person then Hospital Registrar can
        // perform these actions
        SEARCHROLEACTIONMAP.put(HROTHERRECORDS, Arrays.asList(BndConstants.VIEW));

        // if record is entered by Hospital User and its his hospital record
        // then he can perform these actions
        SEARCHROLEACTIONMAP.put(HOSPITALUSEROWNRECORD, Arrays.asList(BndConstants.VIEW,
                BndConstants.CERTIFICATEGENERATION, BndConstants.CERTIFICATEGENERATIONFORBIRTH));

        // if record is entered by some other person then Hospital User can
        // perform these actions
        SEARCHROLEACTIONMAP.put(HOSPITALUSEROTHERRECORD, Arrays.asList(BndConstants.VIEW));

        // if record is entered by Operator and its his unit record then he can
        // perform these actions
        SEARCHROLEACTIONMAP.put(OPERATOROWNREGUNIT, Arrays.asList(BndConstants.VIEW,
                BndConstants.CERTIFICATEGENERATION, BndConstants.CERTIFICATEGENERATIONFORBIRTH));

        // if record is entered by some other person then operator can perform
        // these actions
        SEARCHROLEACTIONMAP.put(OPERATORPOTHERREGUNIT, Arrays.asList(BndConstants.VIEW));

        SEARCHROLEACTIONMAP.put(ACCOUNTANT, Arrays.asList(BndConstants.VIEW));

        SEARCHROLEACTIONMAP.put(ADMIN, Arrays.asList(BndConstants.VIEW));

        SEARCHROLEACTIONMAP.put(DEFAULT, searchActions);

    }

    public static BndRuleBook getInstance() {
        return ruleBook;
    }

    /**
     * @param roleName
     *            - list of role names for a particular user
     * @return - set of actions performed by the role
     */

    public List<String> getBirthActionsByRoles(final List<String> roleName) {
        List<String> actionList = Collections.EMPTY_LIST;

        if (roleName != null && !roleName.isEmpty()) {
            if (roleName.contains(SUPERUSER))
                actionList = BIRTHROLEACTIONMAP.get(SUPERUSER);
            if (roleName.contains(HEADQUATERREGISTRAR))
                actionList = BIRTHROLEACTIONMAP.get(HEADQUATERREGISTRAR);
            else if (roleName.contains(REGISTRAR))
                actionList = BIRTHROLEACTIONMAP.get(REGISTRAR);
            else if (roleName.contains(HOSPITALREGISTRAR))
                actionList = BIRTHROLEACTIONMAP.get(HOSPITALREGISTRAR);
            else if (roleName.contains(HOSPITALUSER))
                actionList = BIRTHROLEACTIONMAP.get(HOSPITALUSER);
            else if (roleName.contains(OPERATOR))
                actionList = BIRTHROLEACTIONMAP.get(OPERATOR);
            else if (roleName.contains(ACCOUNTANT))
                actionList = BIRTHROLEACTIONMAP.get(ACCOUNTANT);
            else
                actionList = BIRTHROLEACTIONMAP.get(DEFAULT);
        }

        return actionList;
    }

    /**
     * This Api returns highest privileged role for the user
     *
     * @param roleName
     *            - list of role names for the user
     * @return highest privileged role for the user
     */

    public String getHighestPrivilegedRole(final List<String> roleName) {
        if (roleName != null && !roleName.isEmpty())
            if (roleName.contains(SUPERUSER))
                return SUPERUSER;
            else if (roleName.contains(REGISTRAR))
                return REGISTRAR;
            else if (roleName.contains(HOSPITALREGISTRAR))
                return HOSPITALREGISTRAR;
            else if (roleName.contains(HOSPITALUSER))
                return HOSPITALUSER;
            else if (roleName.contains(OPERATOR))
                return OPERATOR;
            else if (roleName.contains(ACCOUNTANT))
                return ACCOUNTANT;
            else
                return DEFAULT;
        return DEFAULT;
    }

    public List<String> getDeathActionsByRoles(final List<String> roleName) {
        List<String> actionList = Collections.EMPTY_LIST;

        if (roleName != null && !roleName.isEmpty())
            if (roleName.contains(SUPERUSER))
                actionList = DEATHROLEACTIONMAP.get(SUPERUSER);
            else if (roleName.contains(REGISTRAR))
                actionList = DEATHROLEACTIONMAP.get(REGISTRAR);
            else if (roleName.contains(HOSPITALREGISTRAR))
                actionList = DEATHROLEACTIONMAP.get(HOSPITALREGISTRAR);
            else if (roleName.contains(HOSPITALUSER))
                actionList = DEATHROLEACTIONMAP.get(HOSPITALUSER);
            else if (roleName.contains(OPERATOR))
                actionList = DEATHROLEACTIONMAP.get(OPERATOR);
            else if (roleName.contains(ACCOUNTANT))
                actionList = DEATHROLEACTIONMAP.get(ACCOUNTANT);
            else
                actionList = DEATHROLEACTIONMAP.get(DEFAULT);

        return actionList;
    }

    public List<String> getSearchActionsByRoles(final List<String> roleName, final Registrar registrar,
            final Registration reg) {
        List<String> actionList = Collections.EMPTY_LIST;

        if (roleName != null && !roleName.isEmpty() && reg != null)
            if (roleName.contains(SUPERUSER))
                actionList = SEARCHROLEACTIONMAP.get(SUPERUSER);
            else if (roleName.contains(REGISTRAR)) {
                /* condition for headquater registrar */
                if (registrar != null && registrar.getRegUnitId() != null
                        && registrar.getRegUnitId().getIsmainregunit()) {
                    if (!BndDateUtils.hasJurisdiction(reg, reg.getDateOfEvent()))
                        actionList = SEARCHROLEACTIONMAP.get(HEADQUATERREGISTRAR);
                    else
                        actionList = SEARCHROLEACTIONMAP.get(HEADQUATERREGISTRARHRRECORDWITHIN1YEAR);
                } else if (registrar != null && registrar.getRegUnitId() != null
                        && !registrar.getRegUnitId().getIsmainregunit())
                    /*
                     * if login user has registered the record or if registered
                     * record belongs to his zone
                     */
                    if (reg.getRegistrationUnit().equals(registrar.getRegUnitId())) {
                        if (!BndDateUtils.hasJurisdiction(reg, reg.getDateOfEvent()))
                            actionList = SEARCHROLEACTIONMAP.get(REGISTRAROWNREGUNIT);
                        else
                            actionList = SEARCHROLEACTIONMAP.get(REGISTRARHRRECORDWITHIN1YEAR);
                    }
                /* if registered record belongs to different zone */
                    else if (!BndDateUtils.hasJurisdiction(reg, reg.getDateOfEvent()))
                        actionList = SEARCHROLEACTIONMAP.get(REGISTRAROTHERREGUNIT);
                    else
                        actionList = SEARCHROLEACTIONMAP.get(REGISTRARHRRECORDWITHIN1YEAR);
            } else if (roleName.contains(HOSPITALREGISTRAR)) {

                if (reg.getRegistrationUnit().equals(registrar.getRegUnitId())) {
                    if (BndDateUtils.hasJurisdiction(reg, reg.getDateOfEvent()))
                        actionList = SEARCHROLEACTIONMAP.get(HROWNRECORDWITHIN1YEAR);
                    else
                        actionList = SEARCHROLEACTIONMAP.get(HROWNRECORDAFTER1YEAR);
                } else
                    actionList = SEARCHROLEACTIONMAP.get(HROTHERRECORDS);
            } else if (roleName.contains(HOSPITALUSER)) {
                if (reg.getEstablishment() != null && registrar.getEstablishment() != null
                        && reg.getEstablishment().equals(registrar.getEstablishment()))
                    actionList = SEARCHROLEACTIONMAP.get(HOSPITALUSEROWNRECORD);
                else
                    actionList = SEARCHROLEACTIONMAP.get(HOSPITALUSEROTHERRECORD);
            } else if (roleName.contains(OPERATOR)) {
                if (reg.getRegistrationUnit().equals(registrar.getRegUnitId()))
                    actionList = SEARCHROLEACTIONMAP.get(OPERATOROWNREGUNIT);
                else
                    actionList = SEARCHROLEACTIONMAP.get(OPERATORPOTHERREGUNIT);
            } else if (roleName.contains(ACCOUNTANT))
                actionList = SEARCHROLEACTIONMAP.get(ACCOUNTANT);
            else
                actionList = SEARCHROLEACTIONMAP.get(DEFAULT);

        return actionList;
    }

    public Boolean isFreeCertificateIssue(final Date eventDate, final Date registartionDate) {

        long diff = eventDate.getTime() - registartionDate.getTime();
        diff = diff / (1000 * 60 * 60 * 24);
        if (diff > 21)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

}
