package org.egov.ptis.domain.entity.property;

import static org.egov.search.domain.Filter.rangeFilter;
import static org.egov.search.domain.Filter.termsStringFilter;
import org.egov.ptis.constants.PropertyTaxConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import javax.validation.ValidationException;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.jboss.logging.Logger;

public class DailyCollectionReportSearchVLT {

	 private String fromDate;
	    private String toDate;
	    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
	    private String collectionMode;
	    private String collectionOperator;
	    private String status;
	    private String revenueWard;
	    private String searchText;
	    private String ulbName;
	    private List<String> consumerCode = new ArrayList<String>();

	    private static final Logger logger = Logger.getLogger(DailyCollectionReportSearchVLT.class);

	    public void setFromDate(final String fromDate) {
	        if (null != fromDate)
	            try {
	                if (logger.isDebugEnabled())
	                    logger.debug("Date Range From start.. :" + ft.format(dtft.parse(fromDate)));
	                this.fromDate = ft.format(dtft.parse(fromDate));
	            } catch (final ParseException e) {
	                throw new ValidationException(e.getMessage());
	            }
	    }

	    public void setToDate(final String toDate) {
	        final Calendar cal = Calendar.getInstance();
	        if (null != toDate)
	            try {
	                cal.setTime(dtft.parse(toDate));
	                cal.add(Calendar.DAY_OF_YEAR, 1);
	                if (logger.isDebugEnabled())
	                    logger.debug("Date Range Till .. :" + ft.format(cal.getTime()));
	                this.toDate = ft.format(cal.getTime());
	            } catch (final ParseException e) {
	                throw new ValidationException(e.getMessage());
	            }
	    }

	    public String getFromDate() {
	        return fromDate;
	    }

	    public String getToDate() {
	        return toDate;
	    }

	    public String getCollectionMode() {
	        return collectionMode;
	    }

	    public void setCollectionMode(String collectionMode) {
	        this.collectionMode = collectionMode;
	    }

	    public String getCollectionOperator() {
	        return collectionOperator;
	    }

	    public void setCollectionOperator(String collectionOperator) {
	        this.collectionOperator = collectionOperator;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public String getRevenueWard() {
	        return revenueWard;
	    }

	    public void setRevenueWard(String revenueWard) {
	        this.revenueWard = revenueWard;
	    }

	    public String getSearchText() {
	        return searchText;
	    }

	    public void setSearchText(String searchText) {
	        this.searchText = searchText;
	    }

	    public List<String> getConsumerCode() {
	        return consumerCode;
	    }

	    public void setConsumerCode(List<String> consumerCode) {
	        this.consumerCode = consumerCode;
	    }
	    
	    public String getUlbName() {
	        return ulbName;
	    }

	    public void setUlbName(String ulbName) {
	        this.ulbName = ulbName;
	    }

		public Filters searchCollectionFilters() {
	        final List<Filter> andFilters = new ArrayList<>(0);
	        andFilters.add(termsStringFilter("clauses.cityname", ulbName));
	        andFilters.add(termsStringFilter("clauses.channel", collectionMode));
	        andFilters.add(termsStringFilter("clauses.status", status));
	        andFilters.add(termsStringFilter("clauses.receiptcreator", collectionOperator));
	        andFilters.add(termsStringFilter("clauses.billingservice", PropertyTaxConstants.INDEX_COLLECTION_CLAUSES_BILLINGSERVICE_VACANT_LAND));
	        if (!consumerCode.isEmpty()) {
	            String[] consumerCodes = consumerCode.toArray(new String[consumerCode.size()]);
	            andFilters.add(termsStringFilter("common.consumercode", consumerCodes));
	        }
	        andFilters.add(rangeFilter("searchable.receiptdate", fromDate, toDate));
	        if (logger.isDebugEnabled())
	            logger.debug("finished filters");
	        return Filters.withAndFilters(andFilters);
	    }

	    public Filters searchProperyForWardFilters() {
	        final List<Filter> andFilters = new ArrayList<>(0);
	        andFilters.add(termsStringFilter("clauses.cityname", ulbName));
	        andFilters.add(termsStringFilter("clauses.revwardname", revenueWard));
	        if (logger.isDebugEnabled())
	            logger.debug("finished property tax filters");
	        return Filters.withAndFilters(andFilters);
	    }
	    
	    public String searchQuery() {
	        return searchText;
	    }

}
