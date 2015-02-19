package org.egov.lib.admbndry;

import org.hamcrest.core.Is;
import org.json.simple.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class BoundaryImplTest {

    @Test
    public void shouldRetrieveBoundaryDetailsHierarchally() {
        BoundaryTypeImpl ward = new BoundaryTypeImpl();
        ward.setName("Ward");

        BoundaryTypeImpl zone = new BoundaryTypeImpl();
        zone.setName("Zone");

        BoundaryTypeImpl city = new BoundaryTypeImpl();
        city.setName("City");

        BoundaryImpl cityBoundary = new BoundaryImpl();
        cityBoundary.setName("c0122");
        cityBoundary.setBoundaryType(city);

        BoundaryImpl zoneBoundary = new BoundaryImpl();
        zoneBoundary.setName("z0122");
        zoneBoundary.setBoundaryType(zone);
        zoneBoundary.setParent(cityBoundary);

        BoundaryImpl wardBoundary = new BoundaryImpl();
        wardBoundary.setName("022");
        wardBoundary.setBoundaryType(ward);
        wardBoundary.setParent(zoneBoundary);

        JSONObject boundaryJson = wardBoundary.getBoundaryJson();
        assertThat(boundaryJson.get("Ward"), Is.is("022"));
        assertThat(boundaryJson.get("Zone"), Is.is("z0122"));
        assertThat(boundaryJson.get("City"), Is.is("c0122"));
    }

}