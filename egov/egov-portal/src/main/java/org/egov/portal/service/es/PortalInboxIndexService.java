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
package org.egov.portal.service.es;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.portal.entity.FirmUser;
import org.egov.portal.entity.PortalInbox;
import org.egov.portal.entity.es.PortalInboxIndex;
import org.egov.portal.firm.service.FirmUserService;
import org.egov.portal.repository.es.PortalInboxIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PortalInboxIndexService {

    @Autowired
    private CityService cityService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private FirmUserService firmUserService;

    @Autowired
    private PortalInboxIndexRepository portalInboxIndexRepository;

    public PortalInboxIndex createPortalInboxIndex(final PortalInbox portalInbox) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        final PortalInboxIndex portalInboxIndex = new PortalInboxIndex();
        portalInboxIndex.setUlbName(cityWebsite.getName());
        portalInboxIndex.setApplicationCreatedBy(portalInbox.getCreatedBy() != null ? portalInbox.getCreatedBy()
                .getName() : "");
        portalInboxIndex.setApplicationDate(portalInbox.getApplicationDate());
        portalInboxIndex.setApplicationNumber(portalInbox.getApplicationNumber());
        portalInboxIndex.setApplicationStatus(portalInbox.getStatus() != null ? portalInbox.getStatus() : "");
        portalInboxIndex.setCreatedDate(portalInbox.getCreatedDate());
        portalInboxIndex.setDetailedMessage(portalInbox.getDetailedMessage() != null ? portalInbox.getDetailedMessage()
                : "");
        portalInboxIndex.setEntityReferenceId(portalInbox.getEntityRefId() != null ? portalInbox.getEntityRefId()
                .toString() : "");
        portalInboxIndex.setEntityReferenceNumber(portalInbox.getEntityRefNumber() != null ? portalInbox
                .getEntityRefNumber() : "");
        portalInboxIndex.setHeaderMessage(portalInbox.getHeaderMessage() != null ? portalInbox.getHeaderMessage() : "");
        portalInboxIndex.setId(cityWebsite.getCode().concat("-").concat(portalInbox.getId().toString()));
        portalInboxIndex.setLink(portalInbox.getLink() != null ? portalInbox.getLink() : "");
        portalInboxIndex.setModule(portalInbox.getModule() != null ? portalInbox.getModule().getName() : "");
        portalInboxIndex.setPriority(portalInbox.getPriority() != null ? portalInbox.getPriority().toString() : "");
        portalInboxIndex.setRead(portalInbox.isRead());
        portalInboxIndex.setResolved(portalInbox.isResolved());
        portalInboxIndex.setServiceType(portalInbox.getServiceType() != null ? portalInbox.getServiceType() : "");
        if (portalInbox.getSlaEndDate() != null)
            portalInboxIndex.setSlaendDate(portalInbox.getSlaEndDate());
        if (portalInbox.getResolvedDate() != null)
            portalInboxIndex.setResolvedDate(portalInbox.getResolvedDate());
        setFirmDetails(portalInbox, portalInboxIndex);
        portalInboxIndexRepository.save(portalInboxIndex);
        return portalInboxIndex;
    }

    private void setFirmDetails(final PortalInbox portalInbox, final PortalInboxIndex portalInboxIndex) {
        if (portalInbox.getPortalInboxUsers() != null && portalInbox.getPortalInboxUsers().size() != 0
                && portalInbox.getPortalInboxUsers().get(0).getUser() != null) {
            final FirmUser firmUser = firmUserService.getFirmUserByUserId(portalInbox.getPortalInboxUsers().get(0)
                    .getUser().getId());
            if (firmUser != null) {
                portalInboxIndex.setFirmAddress(firmUser.getFirm().getAddress());
                portalInboxIndex.setFirmName(firmUser.getFirm().getName());
                portalInboxIndex.setFirmPan(firmUser.getFirm().getPan());
            }
        }
    }
}
