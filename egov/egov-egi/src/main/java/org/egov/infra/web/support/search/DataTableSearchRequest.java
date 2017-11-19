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

package org.egov.infra.web.support.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.egov.infra.utils.JsonUtils.fromJSON;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataTableSearchRequest {

    private DataTableSearchParam searchParam;

    public int pageNumber() {
        return searchParam.getPageNumber();
    }

    public int pageSize() {
        return searchParam.getPageSize();
    }

    public int draw() {
        return searchParam.getDraw();
    }

    public String orderBy() {
        return searchParam.orderBy();
    }

    public Sort.Direction orderDir() {
        return Sort.Direction.fromString(searchParam.orderDir());
    }

    public void setArgs(String args) {
        searchParam = fromJSON(args, DataTableSearchParam.class);
    }

    private static class DataTableSearchParam {
        private int start;
        private int length;
        private int draw;
        private List<Columns> columns;
        private List<Order> order;

        public void setStart(int start) {
            this.start = start;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getPageNumber() {
            return (start / length + 1) - 1;
        }

        public int getPageSize() {
            return length == -1 ? Integer.MAX_VALUE : length;
        }

        public int getDraw() {
            return draw;
        }

        public void setDraw(int draw) {
            this.draw = draw;
        }

        public String orderBy() {
            return defaultIfBlank(this.columns.get(order.get(0).column).name, "id");
        }

        public String orderDir() {
            return order.get(0).dir;
        }

        public void setColumns(List<Columns> columns) {
            this.columns = columns;
        }

        public void setOrder(List<Order> order) {
            this.order = order;
        }
    }

    private static class Columns {
        private String data;
        private String name;
        private boolean orderable;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isOrderable() {
            return orderable;
        }

        public void setOrderable(boolean orderable) {
            this.orderable = orderable;
        }

    }

    private static class Order {
        private int column;
        private String dir;

        public int getColumn() {
            return column;
        }

        public void setColumn(final int column) {
            this.column = column;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(final String dir) {
            this.dir = dir;
        }
    }
}
