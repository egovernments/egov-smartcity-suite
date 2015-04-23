package org.egov.web.actions.masters;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.egov.infstr.services.Page;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.masters.JQueryGridActionSupport.MultipleSearchFilter.Rule;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.google.gson.GsonBuilder;
/**
 * This will supports Action classes to integrate with jqgrid,
 * to do pagination, searching, filtering (single and group) and ajax form saving 
 **/
public abstract class JQueryGridActionSupport extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	protected static final String ADD ="add";
	protected static final String EDIT ="edit";
	protected static final String DELETE ="del";
	// Forjquery datatable pagination, saving and search filter.
	protected Integer id;
	protected String oper;
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

	public Object getModel() {
		return null;
	}

	/**
	 * Returns Page {@link Page} result for the given hibernate model class 
	 * with a mandatory keyFieldName, keyFieldValue. Internally this method 
	 * will apply all filtering and ordering according to the value arrived 
	 * from jqgrid.
	 **/
	protected Page getPagedResult(final Class<?> clazz, final String keyFieldName, final Object keyFieldValue) {
		final Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq(keyFieldName, keyFieldValue));		
		applySearchCriteriaIfAny(criteria);
		
		final Criteria countCriteria = HibernateUtil.getCurrentSession().createCriteria(clazz);
		countCriteria.add(Restrictions.eq(keyFieldName, keyFieldValue));
		countCriteria.setProjection(Projections.rowCount());
		this.totalRecords = ((Number)countCriteria.uniqueResult()).intValue();	
		return new Page(criteria, page, rows); 
	}
	
	/**
	 * Use to send response text directly to the HttpServletResponse 
	 **/
	protected void sendAJAXResponse(final String response) {
		try {
			final HttpServletResponse httpResponse = ServletActionContext.getResponse();
			final Writer httpResponseWriter = httpResponse.getWriter();
			IOUtils.write(response, httpResponseWriter);
			IOUtils.closeQuietly(httpResponseWriter);
		} catch (final IOException e) {
			LOG.error("Error occurred while processing Ajax response", e);
		}
	}
	
	/**
	 * Populates full jqgrid json data string with the given model json data
	 * which is used by jqgrid to populated data-table.  
	 **/
	protected String constructJqGridResponse(final String jsonData) {
		return new StringBuilder().
				append("{\"page\":").append(this.page).
				append(",\"total\":").append(getTotalPages()).
				append(",\"records\":").append(this.totalRecords).
				append(",\"rows\":").append(jsonData).append("}").toString();
	}
	
	/**
	 * This will get invoked only if user uses search on jqgrid. 
	 * This is capable applying jqgrid single and group filtering searches. 
	 **/
	private void applySearchCriteriaIfAny(final Criteria criteria) {
		if (_search) {
			if (StringUtils.isBlank(filters)) {
				criteria.add(applyRestriction());
			} else {
				final MultipleSearchFilter multipleSearchFilter =  getMultiSearchFilter();
				if (multipleSearchFilter.getGroupOp().equals("AND")) {
					applyJunctionCriterion(Restrictions.conjunction(),criteria,multipleSearchFilter);					
				} else if (multipleSearchFilter.getGroupOp().equals("OR")) {
					applyJunctionCriterion(Restrictions.disjunction(),criteria,multipleSearchFilter);					
				}
			}
		}
		criteria.addOrder(applyOrderBy());
	}
	
	/**
	 * Used when search comes from jqgrid group filtering
	 **/
	private void applyJunctionCriterion(final Junction junction, final Criteria criteria, final MultipleSearchFilter multipleSearchFilter) {
		for (final Rule  rule : multipleSearchFilter.getRules()) {
			this.searchOper = rule.getOp();
			this.searchField = rule.getField();
			this.searchString = rule.getData();
			junction.add(applyRestriction());
		}
		criteria.add(junction);
	}
	
	/**
	 * Implementing Action need to override this method incase the search criteria
	 * contains non string values.
	 *eg:
	 *<code> 
	 *  protected Object convertType (String searchField, String searchValue) {
	 *  	Object convertedValue = null;
	 *  	if (searchField.equals("accountNumber")) {
	 *  		convertedValue = new BigDecimal(searchValue);
	 *      }
	 *  	return convertedValue;
	 *  }
	 * </code>
	 **/
	protected Object convertValueType (String searchField, String searchValue) {
		return searchValue;
	}
	
	/**
	 * Used to convert jqgrid search operator to hibernate restriction.
	 **/
	private Criterion applyRestriction() {
		final Object convertedValue = convertValueType (searchField,searchString);
		
		if (searchOper.equals("eq")) {
			return Restrictions.eq(searchField, convertedValue);
		} else if (searchOper.equals("ne")) {
			return Restrictions.ne(searchField, convertedValue);
		}
		
		if (convertedValue instanceof String) {
			if (searchOper.equals("bw")) {
				return Restrictions.ilike(searchField, searchString + "%");
			} else if (searchOper.equals("cn")) {
				return Restrictions.ilike(searchField, "%" + searchString + "%");
			} else if (searchOper.equals("ew")) {
				return Restrictions.ilike(searchField, "%" + searchString);
			} else if (searchOper.equals("bn")) {
				return Restrictions.not(Restrictions.ilike(searchField, searchString + "%"));
			} else if (searchOper.equals("en")) {
				return Restrictions.not(Restrictions.ilike(searchField, "%" + searchString));
			} else if (searchOper.equals("nc")) {
				return Restrictions.not(Restrictions.ilike(searchField, "%" + searchString + "%"));
			} else if (searchOper.equals("in")) {
				return Restrictions.in(searchField, searchString.split(","));
			} else if (searchOper.equals("ni")) {
				return Restrictions.not(Restrictions.in(searchField, searchString.split(",")));
			} 
		} else {
			if (searchOper.equals("lt")) {
				return Restrictions.lt(searchField, convertedValue);
			} else if (searchOper.equals("le")) {
				return Restrictions.le(searchField, convertedValue);
			} else if (searchOper.equals("gt")) {
				return Restrictions.gt(searchField, convertedValue);
			} else if (searchOper.equals("ge")) {
				return Restrictions.ge(searchField, convertedValue);
			} 
		}
		return null;
	}

	/**
	 * Used to convert jqgrid order by to hibernate Order by
	 **/
	private Order applyOrderBy() {
		final String orderBy = sord == null ? ord : sord;
		final String orderByField = sidx == null ? searchField : sidx;
		if (orderBy.equals("asc")) {
			return Order.asc(orderByField);
		} else {
			return Order.desc(orderByField);
		}
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

	public void set_search(final boolean _search) {
		this._search = _search;
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

	public void setFilters(String filters) {
		this.filters = filters;
	}
	
	/**
	 * This method will convert the incoming group search filter to java class
	 * {@link MultipleSearchFilter}
	 **/
	private MultipleSearchFilter getMultiSearchFilter() {
		return new GsonBuilder().create().fromJson(this.filters, MultipleSearchFilter.class);
	}
	
	/**
	 * Returns total number of pages
	 **/
	private Integer getTotalPages() {
		if (totalPages == null) {
			totalPages = totalRecords / rows;
			if (totalRecords % rows != 0) {
				totalPages++;
			}
		}
		return totalPages;
	}

	/**
	 * Inner class which used with {@link Gson} to convert the group search filter json
	 * to a Java class.
	 **/
	class MultipleSearchFilter {
		private String groupOp;
		private List<Rule>rules;
		class Rule {
			private String field;
			private String op;
			private String data;
			public String getField() {
				return field;
			}
			public void setField(String field) {
				this.field = field;
			}
			public String getOp() {
				return op;
			}
			public void setOp(String op) {
				this.op = op;
			}
			public String getData() {
				return data;
			}
			public void setData(String data) {
				this.data = data;
			}
		}
		public String getGroupOp() {
			return groupOp;
		}
		public void setGroupOp(String groupOp) {
			this.groupOp = groupOp;
		}
		public List<Rule> getRules() {
			return rules;
		}
		public void setRules(List<Rule> rules) {
			this.rules = rules;
		}
	}
	
}
