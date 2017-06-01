package org.egov.infra.microservice.contract;

import javax.validation.constraints.Max;

import org.springframework.data.domain.Page;

public class Pagination {

    public static int DEFAULT_PAGE_SIZE = 20;
    public static int DEFAULT_PAGE_OFFSET = 0;

    private Integer totalResults;

    private Integer totalPages;

    @Max(500l)
    private Integer pageSize = Integer.valueOf(DEFAULT_PAGE_SIZE);

    private Integer currentPage;

    private Integer offSet = Integer.valueOf(DEFAULT_PAGE_OFFSET);

    public void map(Page page) {
        this.setCurrentPage(page.getNumber());
        this.setTotalPages(page.getTotalPages());
        this.setPageSize(page.getSize());
        this.setTotalResults(page.getNumberOfElements());

    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getOffSet() {
        return offSet;
    }

    public void setOffSet(Integer offSet) {
        this.offSet = offSet;
    }

}
