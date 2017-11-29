/*      
*       eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
*       accountability and the service delivery of the government  organizations.
*       
*        Copyright (C) <2017>  eGovernments Foundation
*       
*        The updated version of eGov suite of products as by eGovernments Foundation
*        is available at http://www.egovernments.org
*       
*        This program is free software: you can redistribute it and/or modify
*        it under the terms of the GNU General Public License as published by
*        the Free Software Foundation, either version 3 of the License, or
*        any later version.
*       
*        This program is distributed in the hope that it will be useful,
*        but WITHOUT ANY WARRANTY; without even the implied warranty of
*        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*        GNU General Public License for more details.
*       
*        You should have received a copy of the GNU General Public License
*        along with this program. If not, see http://www.gnu.org/licenses/ or
*        http://www.gnu.org/licenses/gpl.html .
*       
*        In addition to the terms of the GPL license to be adhered to in using this
*        program, the following additional terms are to be complied with:
*       
*            1) All versions of this program, verbatim or modified must carry this
*               Legal Notice.
*               Further, all user interfaces, including but not limited to citizen facing interfaces, 
*               Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
*               derived works should carry eGovernments Foundation logo on the top right corner.
*       
*               For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
*               For any further queries on attribution, including queries on brand guidelines, 
*               please contact contact@egovernments.org
*       
*            2) Any misrepresentation of the origin of the material is prohibited. It
*               is required that all modified versions of this material be marked in
*               reasonable ways as different from the original version.
*       
*            3) This license does not grant any rights to any user of the program
*               with regards to rights under trademark law for use of the trade names
*               or trademarks of eGovernments Foundation.
*       
*        In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
*/
package org.egov.ptis.domain.entity.property;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

@Entity
@Table(name = "EGPT_SURROUNDINGS_AUDIT")
@Unique(id = "id", tableName = "EGPT_SURROUNDINGS_AUDIT")

@SequenceGenerator(name = SurroundingsAudit.SEQ_EGPT_SURROUNDINGS_AUDIT, sequenceName = SurroundingsAudit.SEQ_EGPT_SURROUNDINGS_AUDIT, allocationSize = 1)
public class SurroundingsAudit extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_EGPT_SURROUNDINGS_AUDIT = "SEQ_EGPT_SURROUNDINGS_AUDIT";

    @Id
    @GeneratedValue(generator = SEQ_EGPT_SURROUNDINGS_AUDIT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "id_basic_property")
    private Long basicproperty;
    @Column(name = "north_bounded")
    private String northBoundary;
    @Column(name = "south_bounded")
    private String southBoundary;
    @Column(name = "east_bounded")
    private String eastBoundary;
    @Column(name = "west_bounded")
    private String westBoundary;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Long getBasicproperty() {
        return basicproperty;
    }

    public void setBasicproperty(Long basicproperty) {
        this.basicproperty = basicproperty;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(String westBoundary) {
        this.westBoundary = westBoundary;
    }

}
