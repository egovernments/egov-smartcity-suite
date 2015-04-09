package org.egov.infra.admin.master.model;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.CityWebsite;

public class BoundaryModel {
    
    private Boundary boundary;
    private CityWebsite cityWebsite;
    
    public Boundary getBoundary() {
        return boundary;
    }
    
    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }
    
    public CityWebsite getCityWebsite() {
        return cityWebsite;
    }
    
    public void setCityWebsite(CityWebsite cityWebsite) {
        this.cityWebsite = cityWebsite;
    }

}
