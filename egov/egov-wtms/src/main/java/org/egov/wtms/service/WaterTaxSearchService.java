/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.wtms.service;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityName;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.ptis.constants.PropertyTaxConstants.WATER_TAX_INDEX_NAME;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.DISCONNECTED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ACTIVE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CONNECTIONTYPE_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_OPERATOR;
import static org.egov.wtms.utils.constants.WaterTaxConstants.END;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.entity.es.ConnectionSearchRequest;
import org.egov.wtms.entity.es.WaterChargeDocument;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.repository.es.WaterChargeDocumentRepository;
import org.egov.wtms.utils.WaterTaxUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class WaterTaxSearchService {

    private static final String TEMP_CLOSURE_TYPE = "T";
    private static final String RECONNECTION = "Reconnection";
    private static final String ENTER_METER_READING = "Enter Meter Reading";
    private static final String CLOSED = "CLOSED";
    private static final String HOLDING = "HOLDING";
    private static final String CHANGE_OF_USE = "Change of use";
    private static final String VIEW_DCB_SCREEN = "View DCB Screen";
    private static final String COLLECT_CHARGES = "Collect Charge";
    private static final String VIEW_WATER_CONNECTION = "View water tap connection";
    private static final String ADDITIONAL_CONNECTION = "Additional connection";
    private static final String CLOSURE_OF_CONNECTION = "Closure of Connection";
    private static final String DOWNLOAD_RECONNEC_PROCEEDING = "Download Reconnection Proceeding";
    private static final String DOWNLOAD_CLOSURE_PROCEEDING = "Download Closure Proceeding";
    private static final String DOWNLOAD_REGULARISE_PROCEEDING = "Download Regularise Connection Proceedings";

    @Autowired
    private WaterChargeDocumentRepository waterChargeDocumentRepository;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtils securityUtils;

    public List<ConnectionSearchRequest> searchConnection(ConnectionSearchRequest searchRequest) {

        User currentUser = getUserId() == null ? securityUtils.getCurrentUser() : userService.getUserById(getUserId());
        boolean publicRole = waterTaxUtils.isPublicRole();
        boolean collectionOperatorRole = waterTaxUtils.checkCollectionOperatorRole();
        boolean superUserRole = waterTaxUtils.isSuperUser(currentUser);
        boolean adminRole = waterTaxUtils.isRoleAdmin(currentUser);
        boolean ulbOperator = waterTaxUtils.isUlbOperator();
        boolean cscUser = waterTaxUtils.getCurrentUserRole();
        boolean bankCollectorOperator = waterTaxUtils.isRoleBankCollectorOperator(currentUser);
        List<ConnectionSearchRequest> finalResult = new ArrayList<>();
        List<WaterChargeDocument> temList = findAllWaterChargeIndexByFilter(searchRequest);
        for (WaterChargeDocument waterChargeIndex : temList) {
            WaterConnectionDetails closureApplication = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                    waterChargeIndex.getConsumerCode(), ConnectionStatus.CLOSED);
            WaterConnectionDetails reconnApplication = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                    waterChargeIndex.getConsumerCode(), ConnectionStatus.ACTIVE);
            ConnectionSearchRequest customerObj = new ConnectionSearchRequest();
            if (closureApplication != null)
                customerObj.setApplicationcode(closureApplication.getApplicationNumber());
            else if (reconnApplication != null)
                customerObj.setApplicationcode(reconnApplication.getApplicationNumber());
            customerObj.setApplicantName(waterChargeIndex.getConsumerName());
            customerObj.setConsumerCode(waterChargeIndex.getConsumerCode());
            customerObj.setOldConsumerNumber(waterChargeIndex.getOldConsumerCode());
            customerObj.setPropertyid(waterChargeIndex.getPropertyId());
            customerObj.setAddress(waterChargeIndex.getLocality());
            customerObj.setApplicationType(waterChargeIndex.getApplicationCode());
            customerObj.setUsage(waterChargeIndex.getUsage());
            customerObj.setIslegacy(waterChargeIndex.getLegacy());
            customerObj.setPropertyTaxDue(waterChargeIndex.getTotalDue());
            customerObj.setStatus(waterChargeIndex.getStatus());
            customerObj.setConnectiontype(waterChargeIndex.getConnectionType());
            customerObj.setWaterTaxDue(waterChargeIndex.getWaterTaxDue());
            customerObj.setClosureType(waterChargeIndex.getClosureType());
            customerObj.setActions(addActions(waterChargeIndex, publicRole, collectionOperatorRole, cscUser, superUserRole,
                    ulbOperator, adminRole, bankCollectorOperator));
            finalResult.add(customerObj);
        }
        return finalResult;

    }

    private BoolQueryBuilder getFilterQuery(ConnectionSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("cityName", getCityName()));
        if (isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(
                    QueryBuilders.wildcardQuery("consumerName", "*".concat(searchRequest.getApplicantName()).concat("*")));
        if (isNotBlank(searchRequest.getConsumerCode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerCode", searchRequest.getConsumerCode()));
        if (isNotBlank(searchRequest.getOldConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("oldConsumerCode", searchRequest.getOldConsumerNumber()));
        if (isNotBlank(searchRequest.getPropertyid()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("propertyId", searchRequest.getPropertyid()));
        if (isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueWard", searchRequest.getRevenueWard()));
        if (isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("mobileNumber", searchRequest.getMobileNumber()));
        if (isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("doorNo", searchRequest.getDoorNumber()));
        if (isNotBlank(searchRequest.getLocality()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("locality", searchRequest.getLocality()));

        return boolQuery;
    }

    public List<WaterChargeDocument> findAllWaterChargeIndexByFilter(ConnectionSearchRequest searchRequest) {

        long count = getCountOfConnectionBySearchRequest(searchRequest);

        Iterable<WaterChargeDocument> sampleEntities = waterChargeDocumentRepository
                .search(getFilterQuery(searchRequest), new PageRequest(0, count > 0 ? (int) count : 1));
        List<WaterChargeDocument> sampleEntitiesTemp = new ArrayList<>();
        for (WaterChargeDocument document : sampleEntities)
            sampleEntitiesTemp.add(document);

        return sampleEntitiesTemp;
    }

    public int getCountOfConnectionBySearchRequest(ConnectionSearchRequest request) {

        SearchResponse count = elasticsearchTemplate.getClient().prepareSearch(WATER_TAX_INDEX_NAME).setSize(0)
                .setQuery(getFilterQuery(request))
                .execute().actionGet();
        return (int) count.getHits().totalHits();
    }

    private List<String> addActions(WaterChargeDocument connection, boolean publicRole, boolean collectionOperatorRole,
            boolean cscUser, boolean superUserRole, boolean ulbOperator, boolean adminRole, boolean bankCollectorOperator) {

        List<String> connectionActions = new ArrayList<>();

        if (publicRole && ACTIVE.equals(connection.getStatus())) {
            connectionActions.add(VIEW_DCB_SCREEN);
        } else if (superUserRole) {
            connectionActions.add(VIEW_DCB_SCREEN);
            connectionActions.add(VIEW_WATER_CONNECTION);
            connectionActions.add(DOWNLOAD_RECONNEC_PROCEEDING);
            connectionActions.add(DOWNLOAD_CLOSURE_PROCEEDING);

        } else if (ADDNLCONNECTION.equals(connection.getApplicationCode())) {
            addAdditionalConnectionActions(connection, collectionOperatorRole, cscUser, ulbOperator, adminRole, connectionActions,
                    bankCollectorOperator);
        } else if (NEWCONNECTION.equals(connection.getApplicationCode())) {
            addNewConnectionActions(connection, collectionOperatorRole, cscUser, ulbOperator, connectionActions,
                    bankCollectorOperator);
        } else if (CHANGEOFUSE.equals(connection.getApplicationCode()) && ACTIVE.equals(connection.getStatus())) {
            addChangeOfUseActions(connection, collectionOperatorRole, ulbOperator, connectionActions, bankCollectorOperator);
        } else if (RECONNECTION.equals(connection.getApplicationCode()) && ACTIVE.equals(connection.getStatus())) {
            addReconnectionActions(connection, collectionOperatorRole, ulbOperator, connectionActions);
        } else if (CLOSINGCONNECTION.equals(connection.getApplicationCode())) {
            addClosingConnectionActions(connection, cscUser, ulbOperator, collectionOperatorRole, connectionActions);
        } else if (REGULARIZE_CONNECTION.equals(connection.getApplicationCode()) && ACTIVE.equals(connection.getStatus())) {
            connectionActions.add(VIEW_WATER_CONNECTION);
            connectionActions.add(DOWNLOAD_REGULARISE_PROCEEDING);
        } else if (CLOSED.equals(connection.getStatus()) || HOLDING.equals(connection.getStatus())
                || ROLE_OPERATOR.equals(waterTaxUtils.getUserRole(ROLE_OPERATOR))) {
            connectionActions.add(VIEW_WATER_CONNECTION);
        } else if (ROLE_OPERATOR.equals(waterTaxUtils.getUserRole(ROLE_OPERATOR))) {
            connectionActions.add(COLLECT_CHARGES);
        }
        return connectionActions;
    }

    private void addAdditionalConnectionActions(WaterChargeDocument connection,
            boolean collectionOperatorRole, boolean cscUser,
            boolean ulbOperator, boolean adminRole, List<String> connectionActions, boolean bankCollectorOperator) {
        if (ACTIVE.equals(connection.getStatus())) {

            if ((bankCollectorOperator || collectionOperatorRole) && connection.getWaterTaxDue() > 0) {
                connectionActions.add(COLLECT_CHARGES);
                if (collectionOperatorRole) {
                    connectionActions.add(VIEW_DCB_SCREEN);
                }

            } else if (cscUser) {
                connectionActions.add(VIEW_WATER_CONNECTION);
                connectionActions.add(CHANGE_OF_USE);
            } else if (collectionOperatorRole && connection.getWaterTaxDue() >= 0) {
                connectionActions.add(COLLECT_CHARGES);
                connectionActions.add(VIEW_DCB_SCREEN);
            } else if (CONNECTIONTYPE_METERED.equals(connection.getConnectionType())) {
                getMeteredActions(connectionActions, connection, ulbOperator, collectionOperatorRole);
            } else if (!CONNECTIONTYPE_METERED.equals(connection.getConnectionType())) {
                getNonMeteredActions(connectionActions, connection, ulbOperator, collectionOperatorRole);
            } else if (connection.getLegacy() && adminRole) {
                connectionActions.add(VIEW_DCB_SCREEN);
                connectionActions.add(VIEW_WATER_CONNECTION);
            }
        } else if ((ulbOperator || cscUser) && DISCONNECTED.toString().equals(connection.getStatus())) {
            connectionActions.add(RECONNECTION);
        } else if (ulbOperator && TEMP_CLOSURE_TYPE.equals(connection.getClosureType())
                && CLOSED.equals(connection.getStatus())) {
            connectionActions.add(VIEW_WATER_CONNECTION);
            connectionActions.add(RECONNECTION);
            connectionActions.add(CHANGE_OF_USE);
            connectionActions.add(ENTER_METER_READING);
        }
    }

    private void addNewConnectionActions(WaterChargeDocument connection, boolean collectionOperatorRole,
            boolean cscUser, boolean ulbOperator, List<String> connectionActions, boolean bankCollectorOperator) {
        if (ACTIVE.equals(connection.getStatus())) {
            if ((bankCollectorOperator || collectionOperatorRole) && connection.getWaterTaxDue() > 0) {
                connectionActions.add(COLLECT_CHARGES);
                if (collectionOperatorRole) {
                    connectionActions.add(VIEW_DCB_SCREEN);
                }

            } else if (ulbOperator) {
                getMeteredActions(connectionActions, connection, ulbOperator, collectionOperatorRole);
            } else if (cscUser) {
                connectionActions.add(VIEW_DCB_SCREEN);
                connectionActions.add(VIEW_WATER_CONNECTION);
                connectionActions.add(CHANGE_OF_USE);
                connectionActions.add(ADDITIONAL_CONNECTION);
            } else {
                connectionActions.add(VIEW_DCB_SCREEN);
                connectionActions.add(VIEW_WATER_CONNECTION);
            }
        } else if ((ulbOperator || cscUser) && DISCONNECTED.toString().equals(connection.getStatus())) {
            connectionActions.add(RECONNECTION);
            connectionActions.add(VIEW_WATER_CONNECTION);
        }
    }

    private void addChangeOfUseActions(WaterChargeDocument connection, boolean collectionOperatorRole,
            boolean ulbOperator, List<String> connectionActions, boolean bankCollectorOperator) {
        if ((bankCollectorOperator || collectionOperatorRole) && connection.getWaterTaxDue() > 0) {
            connectionActions.add(COLLECT_CHARGES);
            if (collectionOperatorRole) {
                connectionActions.add(VIEW_DCB_SCREEN);
            }

        } else if (collectionOperatorRole && connection.getWaterTaxDue() > 0) {
            connectionActions.add(connection.getWaterTaxDue() > 0 ? COLLECT_CHARGES : VIEW_DCB_SCREEN);
        } else if (CONNECTIONTYPE_METERED.equals(connection.getConnectionType()) && ulbOperator) {
            connectionActions.add(ENTER_METER_READING);
            addActionsForChangeOFUse(connection, collectionOperatorRole, connectionActions);
        } else if (!CONNECTIONTYPE_METERED.equals(connection.getConnectionType()) && ulbOperator) {
            addActionsForChangeOFUse(connection, collectionOperatorRole, connectionActions);
        } else {
            connectionActions.add(VIEW_DCB_SCREEN);
            connectionActions.add(VIEW_WATER_CONNECTION);
        }
    }

    private void addActionsForChangeOFUse(WaterChargeDocument connection, boolean collectionOperatorRole,
            List<String> connectionActions) {
        connectionActions.add(VIEW_DCB_SCREEN);
        connectionActions.add(VIEW_WATER_CONNECTION);
        connectionActions.add(CLOSURE_OF_CONNECTION);
        connectionActions.add(collectionOperatorRole && connection.getWaterTaxDue() > 0 ? COLLECT_CHARGES : CHANGE_OF_USE);
    }

    private void addClosingConnectionActions(WaterChargeDocument connection, boolean cscUser,
            boolean ulbOperator, boolean collectionOperatorRole, List<String> connectionActions) {
        if ((cscUser || ulbOperator) && (CLOSED.equals(connection.getStatus()) || END.equals(connection.getStatus()))) {
            connectionActions.add(VIEW_WATER_CONNECTION);
            connectionActions.add(DOWNLOAD_CLOSURE_PROCEEDING);
            if (TEMP_CLOSURE_TYPE.equals(connection.getClosureType())) {
                connectionActions.add(RECONNECTION);
            }
        } else if (collectionOperatorRole) {
            connectionActions.add(VIEW_WATER_CONNECTION);
        }
    }

    private void addReconnectionActions(WaterChargeDocument connection, boolean collectionOperatorRole,
            boolean ulbOperator, List<String> connectionActions) {
        if (ulbOperator && CONNECTIONTYPE_METERED.equals(connection.getConnectionType())) {
            connectionActions.add(VIEW_DCB_SCREEN);
            connectionActions.add(VIEW_WATER_CONNECTION);
            connectionActions.add(ENTER_METER_READING);
            if (collectionOperatorRole && connection.getWaterTaxDue() > 0)
                connectionActions.add(COLLECT_CHARGES);
        } else if (ulbOperator && TEMP_CLOSURE_TYPE.equals(connection.getClosureType()) && connection.getWaterTaxDue() > 0) {
            connectionActions.add(VIEW_WATER_CONNECTION);
            if (collectionOperatorRole) {
                connectionActions.add(VIEW_DCB_SCREEN);
                connectionActions.add(COLLECT_CHARGES);
            } else {
                connectionActions.add(DOWNLOAD_RECONNEC_PROCEEDING);
            }
        } else if (collectionOperatorRole && connection.getWaterTaxDue() > 0) {
            connectionActions.add(COLLECT_CHARGES);
        } else {
            connectionActions.add(VIEW_DCB_SCREEN);
            connectionActions.add(VIEW_WATER_CONNECTION);
            connectionActions.add(DOWNLOAD_RECONNEC_PROCEEDING);
        }
    }

    private void getMeteredActions(List<String> connectionActions, WaterChargeDocument connection, boolean ulbOperator,
            boolean collectionOperatorRole) {
        if (collectionOperatorRole || ulbOperator) {
            if (ulbOperator) {
                getActionForMeterAndNonMeter(connectionActions);
                connectionActions.add(ENTER_METER_READING);
            }
            if (collectionOperatorRole && connection.getWaterTaxDue() >= 0) {
                connectionActions.add(COLLECT_CHARGES);
                connectionActions.add(VIEW_DCB_SCREEN);
            } else {
                connectionActions.add(VIEW_DCB_SCREEN);
                connectionActions.add(VIEW_WATER_CONNECTION);
            }
        }
    }

    private void getNonMeteredActions(List<String> connectionActions, WaterChargeDocument connection,
            boolean ulbOperator, boolean collectionOperatorRole) {
        if (collectionOperatorRole || ulbOperator) {
            if (ulbOperator) {
                getActionForMeterAndNonMeter(connectionActions);
                connectionActions.add(VIEW_DCB_SCREEN);
            }
            if (collectionOperatorRole && connection.getWaterTaxDue() >= 0)
                connectionActions.add(COLLECT_CHARGES);
            else {
                connectionActions.add(VIEW_DCB_SCREEN);
                connectionActions.add(VIEW_WATER_CONNECTION);
            }
        }
    }

    private void getActionForMeterAndNonMeter(List<String> connectionActions) {
        connectionActions.add(VIEW_WATER_CONNECTION);
        connectionActions.add(CHANGE_OF_USE);
        connectionActions.add(CLOSURE_OF_CONNECTION);
    }

}