package org.egov.infra.admin.master.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.Unique;


@Entity
@Unique(
        id = "id", 
        tableName = "eg_city_website", 
        fields = { "cityBaseURL" }, 
        columnName = { "citybaseurl" }
)
@Table(name = "eg_city_website")
public class CityWebsite extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -6267923687226233397L;

    public static final String QUERY_CITY_BY_URL = "CITY_BY_URL";

    @ManyToOne
    @JoinColumn(name = "boundary")
    private Boundary boundary;

    private String cityName;
    private String cityNameLocal;
    private boolean isActive;
    private String cityBaseURL;
    private String logo;

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameLocal() {
        return cityNameLocal;
    }

    public void setCityNameLocal(String cityNameLocal) {
        this.cityNameLocal = cityNameLocal;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getCityBaseURL() {
        return cityBaseURL;
    }

    public void setCityBaseURL(String cityBaseURL) {
        this.cityBaseURL = cityBaseURL;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
