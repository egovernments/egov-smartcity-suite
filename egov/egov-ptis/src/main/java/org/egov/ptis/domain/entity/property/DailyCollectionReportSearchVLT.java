package org.egov.ptis.domain.entity.property;

import org.jboss.logging.Logger;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyCollectionReportSearchVLT {
	private static final Logger logger = Logger.getLogger(DailyCollectionReportSearchVLT.class);
	private String fromDate;
	private String toDate;
	private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
	private String collectionMode;
	private String collectionOperator;
	private String status;
	private String revenueWard;
	private String searchText;
	private String ulbName;
	private List<String> consumerCode = new ArrayList<String>();

	public void setFromDate(final String fromDate) {
		if (null != fromDate)
			try {
				logger.info("setFromDate,Date Range From start:" + ft.format(dtft.parse(fromDate)));
				if (logger.isDebugEnabled())
					logger.debug("Date Range From start.. :" + ft.format(dtft.parse(fromDate)));
				this.fromDate = ft.format(dtft.parse(fromDate));
				logger.info("setFromDate,Date Range From start assigned:" + this.fromDate);
			} catch (final ParseException e) {
				throw new ValidationException(e.getMessage());
			}
	}

	public void setToDate(final String toDate) {
		final Calendar cal = Calendar.getInstance();
		if (null != toDate)
			try {
				logger.info("setToDate,Date Range Till:" + ft.format(cal.getTime()));
				  cal.setTime(dtft.parse(toDate));
                                  cal.set(Calendar.HOUR_OF_DAY, 23);
			          cal.set(Calendar.MINUTE, 59);
			          cal.set(Calendar.SECOND, 59);
			          cal.set(Calendar.MILLISECOND, 999);
				if (logger.isDebugEnabled())
					logger.debug("Date Range Till .. :" + ft.format(cal.getTime()));
				this.toDate = ft.format(cal.getTime());
				logger.info("setToDate,Date Range Till assigned.. :" + toDate);
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

	/*public Filters searchCollectionFilters() {
		final List<Filter> andFilters = new ArrayList<>(0);
		logger.info("searchCollectionFilters,clauses.cityname:" + ulbName);
		andFilters.add(termsStringFilter("clauses.cityname", ulbName));
		andFilters.add(termsStringFilter("clauses.channel", collectionMode));
		andFilters.add(termsStringFilter("clauses.status", status));
		andFilters.add(termsStringFilter("clauses.receiptcreator", collectionOperator));
		andFilters.add(termsStringFilter("clauses.billingservice", "VLT"));
		logger.info("searchCollectionFilters,clauses.billingservice:"+ "VLT");
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
	}*/

	public String searchQuery() {
		return searchText;
	}

}
