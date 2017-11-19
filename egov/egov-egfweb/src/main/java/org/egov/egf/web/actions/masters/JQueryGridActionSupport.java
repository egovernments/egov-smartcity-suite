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
package org.egov.egf.web.actions.masters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.egov.egf.web.actions.masters.JQueryGridActionSupport.MultipleSearchFilter.Rule;
import org.egov.infra.persistence.utils.Page;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * This will supports Action classes to integrate with jqgrid, to do pagination, searching, filtering (single and group) and ajax
 * form saving
 **/
public abstract class JQueryGridActionSupport extends BaseFormAction {
    protected static final String ADD = "add";
    protected static final String EDIT = "edit";
    protected static final String DELETE = "del";
    private static final long serialVersionUID = 1L;
    protected Integer id;
    protected String oper;

    @Autowired
    @Qualifier("persistenceService")
    protected PersistenceService persistenceService;

    private boolean _search;
    private Integer rows;
    private Integer page;
    private String ord;
    private String searchField;
    private String searchString;
    private String searchOper;
    private String sidx;
    private String sord;
    private String filters;
    private Integer totalPages;
    private Integer totalRecords;

    @Override
    public Object getModel() {
        return null;
    }

    /**
     * Returns Page {@link Page} result for the given hibernate model class with a mandatory keyFieldName, keyFieldValue.
     * Internally this method will apply all filtering and ordering according to the value arrived from jqgrid.
     **/
    protected Page getPagedResult(final Class<?> clazz, final String keyFieldName, final Object keyFieldValue) {
        final Criteria criteria = persistenceService.getSession().createCriteria(clazz);
        criteria.add(Restrictions.eq(keyFieldName, keyFieldValue));
        applySearchCriteriaIfAny(criteria);

        final Criteria countCriteria = persistenceService.getSession().createCriteria(clazz);
        countCriteria.add(Restrictions.eq(keyFieldName, keyFieldValue));
        countCriteria.setProjection(Projections.rowCount());
        totalRecords = ((Number) countCriteria.uniqueResult()).intValue();
        return new Page(criteria, page, rows);
    }

    /**
     * Use to send response text directly to the HttpServletResponse
     **/
    protected void sendAJAXResponse(final String response) {
        try {
            HttpServletResponse httpResponse = ServletActionContext.getResponse();
            Writer httpResponseWriter = httpResponse.getWriter();
            IOUtils.write(response, httpResponseWriter);
        } catch (final IOException e) {
            LOG.error("Error occurred while processing Ajax response", e);
        }
    }

    /**
     * Populates full jqgrid json data string with the given model json data which is used by jqgrid to populated data-table.
     **/
    protected String constructJqGridResponse(final String jsonData) {
        return new StringBuilder().
                append("{\"page\":").append(page).
                append(",\"total\":").append(getTotalPages()).
                append(",\"records\":").append(totalRecords).
                append(",\"rows\":").append(jsonData).append("}").toString();
    }

    /**
     * This will get invoked only if user uses search on jqgrid. This is capable applying jqgrid single and group filtering
     * searches.
     **/
    private void applySearchCriteriaIfAny(final Criteria criteria) {
        if (_search)
            if (StringUtils.isBlank(filters))
                criteria.add(applyRestriction());
            else {
                final MultipleSearchFilter multipleSearchFilter = getMultiSearchFilter();
                if ("AND".equals(multipleSearchFilter.getGroupOp()))
                    applyJunctionCriterion(Restrictions.conjunction(), criteria, multipleSearchFilter);
                else if ("OR".equals(multipleSearchFilter.getGroupOp()))
                    applyJunctionCriterion(Restrictions.disjunction(), criteria, multipleSearchFilter);
            }
        criteria.addOrder(applyOrderBy());
    }

    /**
     * Used when search comes from jqgrid group filtering
     **/
    private void applyJunctionCriterion(final Junction junction, final Criteria criteria,
                                        final MultipleSearchFilter multipleSearchFilter) {
        for (final Rule rule : multipleSearchFilter.getRules()) {
            searchOper = rule.getOp();
            searchField = rule.getField();
            searchString = rule.getData();
            junction.add(applyRestriction());
        }
        criteria.add(junction);
    }

    /**
     * Implementing Action need to override this method incase the search criteria contains non string values. eg: <code>
     * protected Object convertType (String searchField, String searchValue) {
     * Object convertedValue = null;
     * if (searchField.equals("accountNumber")) {
     * convertedValue = new BigDecimal(searchValue);
     * }
     * return convertedValue;
     * }
     * </code>
     **/
    protected Object convertValueType(final String searchField, final String searchValue) {
        return searchValue;
    }

    /**
     * Used to convert jqgrid search operator to hibernate restriction.
     **/
    private Criterion applyRestriction() {
        final Object convertedValue = convertValueType(searchField, searchString);

        if ("eq".equals(searchOper))
            return Restrictions.eq(searchField, convertedValue);
        else if ("ne".equals(searchOper))
            return Restrictions.ne(searchField, convertedValue);

        if (convertedValue instanceof String) {
            if ("bw".equals(searchOper))
                return Restrictions.ilike(searchField, searchString + "%");
            else if ("cn".equals(searchOper))
                return Restrictions.ilike(searchField, "%" + searchString + "%");
            else if ("ew".equals(searchOper))
                return Restrictions.ilike(searchField, "%" + searchString);
            else if ("bn".equals(searchOper))
                return Restrictions.not(Restrictions.ilike(searchField, searchString + "%"));
            else if ("en".equals(searchOper))
                return Restrictions.not(Restrictions.ilike(searchField, "%" + searchString));
            else if ("nc".equals(searchOper))
                return Restrictions.not(Restrictions.ilike(searchField, "%" + searchString + "%"));
            else if ("in".equals(searchOper))
                return Restrictions.in(searchField, searchString.split(","));
            else if ("ni".equals(searchOper))
                return Restrictions.not(Restrictions.in(searchField, searchString.split(",")));
        } else if ("lt".equals(searchOper))
            return Restrictions.lt(searchField, convertedValue);
        else if ("le".equals(searchOper))
            return Restrictions.le(searchField, convertedValue);
        else if ("gt".equals(searchOper))
            return Restrictions.gt(searchField, convertedValue);
        else if ("ge".equals(searchOper))
            return Restrictions.ge(searchField, convertedValue);
        return null;
    }

    /**
     * Used to convert jqgrid order by to hibernate Order by
     **/
    private Order applyOrderBy() {
        final String orderBy = sord == null ? ord : sord;
        final String orderByField = sidx == null ? searchField : sidx;
        if ("asc".equals(orderBy))
            return Order.asc(orderByField);
        else
            return Order.desc(orderByField);
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setRows(final Integer rows) {
        this.rows = rows;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    public void setOrd(final String ord) {
        this.ord = ord;
    }

    public void setSearchField(final String searchField) {
        this.searchField = searchField;
    }

    public void setSearchString(final String searchString) {
        this.searchString = searchString;
    }

    public void setSearchOper(final String searchOper) {
        this.searchOper = searchOper;
    }

    public void set_search(final boolean search) {
        this._search = search;
    }

    public void setSidx(final String sidx) {
        this.sidx = sidx;
    }

    public void setSord(final String sord) {
        this.sord = sord;
    }

    public void setOper(final String oper) {
        this.oper = oper;
    }

    public void setFilters(final String filters) {
        this.filters = filters;
    }

    /**
     * This method will convert the incoming group search filter to java class {@link MultipleSearchFilter}
     **/
    private MultipleSearchFilter getMultiSearchFilter() {
        return new GsonBuilder().create().fromJson(filters, MultipleSearchFilter.class);
    }

    /**
     * Returns total number of pages
     **/
    private Integer getTotalPages() {
        if (totalPages == null) {
            totalPages = totalRecords / rows;
            if (totalRecords % rows != 0)
                totalPages++;
        }
        return totalPages;
    }

    /**
     * Inner class which used with {@link Gson} to convert the group search filter json to a Java class.
     **/
    class MultipleSearchFilter {
        private String groupOp;
        private List<Rule> rules;

        public String getGroupOp() {
            return groupOp;
        }

        public void setGroupOp(final String groupOp) {
            this.groupOp = groupOp;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public void setRules(final List<Rule> rules) {
            this.rules = rules;
        }

        class Rule {
            private String field;
            private String op;
            private String data;

            public String getField() {
                return field;
            }

            public void setField(final String field) {
                this.field = field;
            }

            public String getOp() {
                return op;
            }

            public void setOp(final String op) {
                this.op = op;
            }

            public String getData() {
                return data;
            }

            public void setData(final String data) {
                this.data = data;
            }
        }
    }

}