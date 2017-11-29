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

package org.egov.infra.persistence.utils;

import org.hibernate.Criteria;
import org.hibernate.Query;

import javax.persistence.TypedQuery;
import java.util.List;

public class Page<T> {

    private final List<T> results;
    private final int pageSize;
    private final int pageNumber;
    private int recordTotal;

    public Page(Query query, int pageNumber, int pageSize, int recordTotal) {
        this(query, ++pageNumber, pageSize);
        this.recordTotal = recordTotal;
    }

    public Page(Query query, int pageNumber, int pageSize) {
        int currentPageNo = pageNumber;
        if (pageNumber < 1) {
            currentPageNo = 1;
        }

        this.pageNumber = currentPageNo;
        if (pageSize > 0) {
            query.setFirstResult((currentPageNo - 1) * pageSize);
            query.setMaxResults(pageSize + 1);
            this.pageSize = pageSize;
        } else {
            this.pageSize = -1;
        }
        this.results = query.list();
    }

    public Page(Criteria criteria, int pageNumber, int pageSize) {
        int currentPageNo = pageNumber;
        if (pageNumber < 1) {
            currentPageNo = 1;
        }

        this.pageNumber = currentPageNo;

        if (pageSize > 0) {
            criteria.setFirstResult((currentPageNo - 1) * pageSize);
            criteria.setMaxResults(pageSize + 1);
            this.pageSize = pageSize;
        } else {
            this.pageSize = -1;
        }
        this.results = criteria.list();
    }

    public Page(TypedQuery<T> query, int pageNumber, int pageSize, int recordTotal) {
        int currentPageNo = pageNumber;
        if (pageNumber < 1) {
            currentPageNo = 1;
        }

        this.pageNumber = currentPageNo;

        if (pageSize > 0) {
            query.setFirstResult((currentPageNo - 1) * pageSize);
            query.setMaxResults(pageSize + 1);
            this.pageSize = pageSize;
        } else {
            this.pageSize = -1;
        }
        this.results = query.getResultList();
        this.recordTotal = recordTotal;
    }

    public boolean isNextPage() {
        return this.pageSize != -1 && this.results.size() > this.pageSize;
    }

    public boolean isPreviousPage() {
        return this.pageNumber > 0;
    }

    public List<T> getList() {
        return isNextPage() ? this.results.subList(0, this.pageSize) : this.results;
    }

    public int getPageNo() {
        return this.pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getRecordTotal() {
        return this.recordTotal;
    }
}
