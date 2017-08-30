/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.utils;

import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED_WITH_DEMAND_NOT_PAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGENOOFCLOSET;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGENOOFCLOSETURL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSECONNECTION_ACTIONDROPDOWN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTIONURL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.COLLECTDONATIONCHARHGES;
import static org.egov.stms.utils.constants.SewerageTaxConstants.COLLECTDONATIONCHARHGESURL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.GENERATEBEMANDBILL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.GENERATEBEMANDBILLURL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODIFYLEGACYCONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODIFYLEGACYCONNECTIONACTIONDROPDOWN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODIFYLEGACYCONNECTIONURL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_COLLECTIONOPERATOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_CSCOPERTAOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_SEWERAGETAX_ADMINISTRATOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_SEWERAGETAX_APPROVER;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_SEWERAGETAX_CREATOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_SEWERAGETAX_REPORTVIEWER;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_STMS_VIEW_ACCESS_ROLE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_SUPERUSER;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_ULBOPERATOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.VIEW;
import static org.egov.stms.utils.constants.SewerageTaxConstants.VIEWDCB;
import static org.egov.stms.utils.constants.SewerageTaxConstants.VIEWDCBURL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.VIEWURL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_CITIZEN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.stms.elasticSearch.entity.SewerageSearchResult;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;

public class SewerageActionDropDownUtil {

    private static final String DEFAULT = "DEFAULT";
    private static final Map<String, Map<String, String>> actionUrlMap = new HashMap();
    private static final Map<String, List<String>> STATUSACTIONMAP = new HashMap();
    private static Logger logger = Logger.getLogger(SewerageActionDropDownUtil.class);

    static {
        // Status wise define different actions.
        STATUSACTIONMAP.put(DEFAULT, Arrays.asList(VIEW));

        STATUSACTIONMAP.put("CREATED", Arrays.asList(VIEW));
        STATUSACTIONMAP.put("COLLECTINSPECTIONFEE", Arrays.asList(VIEW, VIEWDCB, COLLECTDONATIONCHARHGES));
        STATUSACTIONMAP.put("INITIAL APPROVED", Arrays.asList(VIEW, VIEWDCB));
        STATUSACTIONMAP.put("ESTIMATION NOTICE GENERATED", Arrays.asList(VIEW, VIEWDCB, COLLECTDONATIONCHARHGES));
        STATUSACTIONMAP.put("ESTIMATION AMOUNT PAID", Arrays.asList(VIEW, VIEWDCB));
        STATUSACTIONMAP.put("WORK ORDER GENERATED", Arrays.asList(VIEW, VIEWDCB));
        STATUSACTIONMAP.put("FINAL APPROVED", Arrays.asList(VIEW, VIEWDCB));
        STATUSACTIONMAP.put("SANCTIONED",
                Arrays.asList(VIEW, VIEWDCB, CHANGENOOFCLOSET, CLOSECONNECTION_ACTIONDROPDOWN, GENERATEBEMANDBILL,
                        MODIFYLEGACYCONNECTIONACTIONDROPDOWN));
        STATUSACTIONMAP.put("SANCTIONEDWITHDEMAND", Arrays.asList(VIEW, VIEWDCB, CHANGENOOFCLOSET, COLLECTDONATIONCHARHGES));

        // For each action, define url mapping
        actionUrlMap.put(VIEW, getActionWithUrl(VIEWURL, VIEW));
        actionUrlMap.put(COLLECTDONATIONCHARHGES, getActionWithUrl(COLLECTDONATIONCHARHGESURL, COLLECTDONATIONCHARHGES));
        actionUrlMap.put(VIEWDCB, getActionWithUrl(VIEWDCBURL, VIEWDCB));
        actionUrlMap.put(CHANGENOOFCLOSET, getActionWithUrl(CHANGENOOFCLOSETURL, CHANGENOOFCLOSET));
        actionUrlMap.put(CLOSECONNECTION_ACTIONDROPDOWN,
                getActionWithUrl(CLOSESEWERAGECONNECTIONURL, CLOSECONNECTION_ACTIONDROPDOWN));
        actionUrlMap.put(GENERATEBEMANDBILL, getActionWithUrl(GENERATEBEMANDBILLURL, GENERATEBEMANDBILL));
        actionUrlMap.put(MODIFYLEGACYCONNECTIONACTIONDROPDOWN,
                getActionWithUrl(MODIFYLEGACYCONNECTIONURL, MODIFYLEGACYCONNECTION));

    }

    private static Map<String, String> getActionWithUrl(final String url, final String action) {
        final Map<String, String> actionwithurl = new LinkedHashMap<>();
        actionwithurl.put(url, action);
        return actionwithurl;
    }

    public static Map<String, String> filterActionsByStatus(final List<String> actions, final String status,
            final SewerageApplicationDetails sewerageApplicationDetails) {
        if (actions != null && !actions.isEmpty()) {
            final Map<String, String> result = new LinkedHashMap<>();
            List<String> statusActionList = Collections.EMPTY_LIST;

            if (status != null && !"".equals(status)) {
                logger.info(" ***** registrationStatus  " + status);
                statusActionList = STATUSACTIONMAP.get(status.toUpperCase());
                logger.info(" ....... statusActionList  " + statusActionList);
                selectUserMappingActions(actions, result, statusActionList);

                if (status.equalsIgnoreCase(APPLICATION_STATUS_SANCTIONED) && sewerageApplicationDetails.getCurrentDemand()
                        .getBaseDemand().compareTo(sewerageApplicationDetails.getCurrentDemand().getAmtCollected()) == 1) {
                    logger.info(" ************ registrationStatus  " + status);
                    statusActionList = STATUSACTIONMAP.get(APPLICATION_STATUS_SANCTIONED_WITH_DEMAND_NOT_PAID);
                    logger.info(" ....... statusActionList  " + statusActionList);
                    selectUserMappingActions(actions, result, statusActionList);
                }
                if (result.size() == 0) // GET default actions
                {
                    statusActionList = STATUSACTIONMAP.get(DEFAULT);
                    selectUserMappingActions(actions, result, statusActionList);
                }
                return result;
            } else
                return Collections.EMPTY_MAP;
        } else
            return Collections.EMPTY_MAP;
    }

    private static void selectUserMappingActions(final List<String> actions, final Map<String, String> result,
            final List<String> statusActionList) {
        for (final String action : actions)
            if (statusActionList != null && statusActionList.contains(action))
                result.putAll(actionUrlMap.get(action));
    }

    public static Map<String, String> getActionsByRoles(final List<String> roleName, final String collectionStatus,
            final SewerageApplicationDetails sewerageApplicationDetails) {
        Set<String> actionList = new HashSet<>();

        logger.debug(" ************ Role Name " + roleName);
        logger.debug(" ************ registrationStatus  " + collectionStatus);

        for (String role : roleName) {
            if (role.equalsIgnoreCase(ROLE_SEWERAGETAX_CREATOR) || role.equalsIgnoreCase(ROLE_SEWERAGETAX_APPROVER))
                actionList.add(VIEWDCB);

            if (role.equalsIgnoreCase(ROLE_ULBOPERATOR)
                    || role.equalsIgnoreCase(ROLE_SEWERAGETAX_REPORTVIEWER)
                    || role.equalsIgnoreCase(ROLE_STMS_VIEW_ACCESS_ROLE)) {
                actionList.add(VIEWDCB);
            }
            if (role.equalsIgnoreCase(ROLE_SEWERAGETAX_CREATOR) || role.equalsIgnoreCase(ROLE_ULBOPERATOR)) {
                actionList.add(CLOSECONNECTION_ACTIONDROPDOWN);
                actionList.add(GENERATEBEMANDBILL);
            }
            if (role.equalsIgnoreCase(ROLE_ULBOPERATOR) || role.equalsIgnoreCase(ROLE_SEWERAGETAX_CREATOR)
                    || role.equalsIgnoreCase(ROLE_SEWERAGETAX_APPROVER) || role.equalsIgnoreCase(ROLE_CITIZEN)) {
                actionList.add(CHANGENOOFCLOSET);
            }
            if (role.equalsIgnoreCase(ROLE_CSCOPERTAOR) || role.equalsIgnoreCase(ROLE_ULBOPERATOR)
                    || role.equalsIgnoreCase(ROLE_COLLECTIONOPERATOR)
                    || role.equalsIgnoreCase(ROLE_SUPERUSER)) {
                actionList.add(COLLECTDONATIONCHARHGES);
            }
            if (role.equalsIgnoreCase(ROLE_SEWERAGETAX_ADMINISTRATOR) || role.equalsIgnoreCase(ROLE_SUPERUSER)) {
                actionList.add(MODIFYLEGACYCONNECTIONACTIONDROPDOWN);
            }

        }
        actionList.add(VIEW);

        return filterActionsByStatus(new ArrayList<String>(actionList), collectionStatus, sewerageApplicationDetails);
    }

    public static final SewerageSearchResult getSearchResultWithActions(List<String> roleName, final String status,
            final SewerageApplicationDetails sewerageApplicationDetails) {
        SewerageSearchResult searchActions = new SewerageSearchResult();
        if (status != null && sewerageApplicationDetails != null)
            searchActions.setActions(getActionsByRoles(roleName, status, sewerageApplicationDetails));
        return searchActions;
    }

}