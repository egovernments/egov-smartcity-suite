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

package org.egov.infra.admin.master.service;

import org.egov.infra.admin.master.entity.Action;
import org.egov.infra.admin.master.repository.ActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.web.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.text.similarity.LevenshteinDistance.getDefaultInstance;
import static org.egov.infra.web.utils.WebUtils.decodeQueryString;
import static org.egov.infra.web.utils.WebUtils.extractURLWithoutQueryParams;

@Service
@Transactional(readOnly = true)
public class ActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionService.class);

    @Autowired
    private ActionRepository actionRepository;

    public Action getActionByName(String name) {
        return actionRepository.findByName(name);
    }

    public Action getActionById(Long id) {
        return actionRepository.findOne(id);
    }

    @Transactional
    public Action saveAction(Action action) {
        return actionRepository.save(action);
    }

    @ReadOnly
    public Optional<Action> getActionByUrlAndContextRoot(String fullURL, String queryString, String contextRoot) {
        Action action;
        if (isBlank(queryString)) {
            action = Optional.ofNullable(actionRepository.findByUrlAndContextRootAndQueryParamsIsNull(fullURL, contextRoot))
                    .orElse(findNearestMatchingAction(fullURL, actionRepository.findByMatchingUrlAndContextRoot(fullURL, contextRoot)));

        } else {
            String queryStr = decodeQueryString(queryString);
            String url = extractURLWithoutQueryParams(fullURL);
            action = Optional.ofNullable(actionRepository.findByUrlAndContextRootAndQueryParams(url, contextRoot, queryStr))
                    .orElse(findNearestMatchingAction(url,
                            actionRepository.findByMatchingUrlAndContextRootAndQueryParams(url, contextRoot, queryStr)));
            if (action != null && !action.queryParamMatches(queryStr)) {
                LOGGER.warn("Action URL query params doesn't match with the expected, provided:- {}, expected:- {}",
                        queryStr, action.getQueryParamRegex());
                return Optional.empty();
            }
        }

        return Optional.ofNullable(action);
    }

    private Action findNearestMatchingAction(String url, List<Action> actions) {
        return actions.isEmpty() ? null : actions
                .stream()
                .filter(action -> getDefaultInstance().apply(url, action.getUrl()) < 1)
                .findFirst()
                .orElse(actions.get(0));
    }

}
