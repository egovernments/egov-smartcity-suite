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
package org.egov.ptis.notice;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.ptis.domain.entity.property.BasicProperty;

import java.util.Date;

public class PtNotice implements java.io.Serializable {
    private Long id;
    private Long moduleId;
    private String noticeType;
    private String noticeNo;
    private Date noticeDate;
    private BasicProperty basicProperty;
    private Long userId;
    private FileStoreMapper fileStore;
    private String applicationNumber;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the moduleId
     */
    public Long getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId
     *            the moduleId to set
     */
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * @return the noticeType
     */
    public String getNoticeType() {
        return noticeType;
    }

    /**
     * @param noticeType
     *            the noticeType to set
     */
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * @return the noticeNo
     */
    public String getNoticeNo() {
        return noticeNo;
    }

    /**
     * @param noticeNo
     *            the noticeNo to set
     */
    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    /**
     * @return the noticeDate
     */
    public Date getNoticeDate() {
        return noticeDate;
    }

    /**
     * @param noticeDate
     *            the noticeDate to set
     */
    public void setNoticeDate(Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    @Override
    public String toString() {
        StringBuilder sbf = new StringBuilder();
        sbf.append("Id: ").append(getId()).append("|NoticeType: ").append(getNoticeType()).append("|NoticeNo: ")
                .append(getNoticeNo());
        return sbf.toString();
    }

    public FileStoreMapper getFileStore() {
        return fileStore;
    }

    public void setFileStore(FileStoreMapper fileStore) {
        this.fileStore = fileStore;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

}
