package org.egov.ptis.domain.entity.property.view;

import java.util.Date;

import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.web.support.search.DataTableSearchRequest;

public class BillCollectorCollectionRequest extends DataTableSearchRequest {

    private String financilaYear;
    private String userId;
    private Date fromDate;
    private Date toDate;
    private String filterName;
    private ReportFormat printFormat;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(final String filterName) {
        this.filterName = filterName;
    }

    public ReportFormat getPrintFormat() {
        return printFormat;
    }

    public void setPrintFormat(final ReportFormat printFormat) {
        this.printFormat = printFormat;
    }

    public String getFinancilaYear() {
        return financilaYear;
    }

    public void setFinancilaYear(String financilaYear) {
        this.financilaYear = financilaYear;
    }

}
