/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.ptis.domain.service.notice;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;

import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

@Service 
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NoticeService extends PersistenceService<PtNotice, Long> {
    private static final Logger LOGGER = Logger.getLogger(NoticeService.class);
    @Autowired
    PersistenceService<BasicProperty, Long> basicPropertyService;
    @Autowired
    private ModuleService moduleDao;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    /**
     * This method populates the <code>PtNotice</code> object along with notice input stream
     *
     * @param basicProperty the <code>BasicProperty</code> object for which the notice is generated
     * @param noticeNo - notice no
     * @param noticeType - type of notice
     * @param fileStream - input stream of generated notice.
     */
    public PtNotice saveNotice(final String applicationNumber, final String noticeNo, final String noticeType,
            final BasicProperty basicProperty,
            final InputStream fileStream) {
        final PtNotice ptNotice = new PtNotice();
        final Module module = moduleDao.getModuleByName(PTMODULENAME);
        ptNotice.setModuleId(module.getId());
        ptNotice.setNoticeDate(new Date());
        ptNotice.setNoticeNo(noticeNo);
        ptNotice.setNoticeType(noticeType);
        ptNotice.setUserId(ApplicationThreadLocals.getUserId());
        ptNotice.setBasicProperty(basicProperty);
        ptNotice.setApplicationNumber(applicationNumber);
        final String fileName = ptNotice.getNoticeNo() + ".pdf";
        final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                FILESTORE_MODULE_NAME);
        ptNotice.setFileStore(fileStore);
        basicProperty.addNotice(ptNotice);
        basicPropertyService.update(basicProperty);
        getSession().flush();
        return ptNotice;
    }

    /**
     * Using this method to attach different file store if document is already signed and been sent for sign again
     * 
     * @param notice
     * @param fileStream
     * @return
     */
    public PtNotice updateNotice(PtNotice notice, InputStream fileStream) {
        final String fileName = notice.getNoticeNo() + ".pdf";
        final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                FILESTORE_MODULE_NAME);
        notice.setFileStore(fileStore);
        notice.setNoticeDate(new Date());
        basicPropertyService.update(notice.getBasicProperty());
        getSession().flush();
        return notice;
    }

    public PtNotice getPtNoticeByNoticeNumberAndNoticeType(final String noticeNo, final String noticeType) {

        final Query qry = getSession().createQuery(
                "from PtNotice Pn where upper(Pn.noticeNo) = :noticeNumber and upper(noticeType)=:noticeType ");
        qry.setString("noticeNumber", noticeNo.toUpperCase());
        qry.setString("noticeType", noticeType.toUpperCase());
        return (PtNotice) qry.uniqueResult();
    }

    public PtNotice getNoticeByApplicationNumber(final String applicationNo) {
        return (PtNotice) basicPropertyService.find("from PtNotice where applicationNumber = ?", applicationNo);
    }

    public PtNotice getNoticeByNoticeTypeAndApplicationNumber(final String noticeType, final String applicationNo) {
        return (PtNotice) basicPropertyService.find("from PtNotice where noticeType = ? and applicationNumber = ?", noticeType,
                applicationNo);
    }

    public PersistenceService<BasicProperty, Long> getBasicPropertyService() {
        return basicPropertyService;
    }

    public void setbasicPropertyService(final PersistenceService<BasicProperty, Long> basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

}
