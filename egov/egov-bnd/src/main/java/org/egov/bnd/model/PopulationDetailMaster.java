/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.model;

import java.util.Date;

public class PopulationDetailMaster implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6584628474147265411L;
    private Integer id;
    private Integer populationyear;
    private Integer totalpopulation;
    private Date createddate;

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getPopulationyear() {
        return populationyear;
    }

    public void setPopulationyear(final Integer populationyear) {
        this.populationyear = populationyear;
    }

    public Integer getTotalpopulation() {
        return totalpopulation;
    }

    public void setTotalpopulation(final Integer totalpopulation) {
        this.totalpopulation = totalpopulation;
    }

    public PopulationDetailMaster() {

    }

    public PopulationDetailMaster(final Integer id, final Integer populationyear, final Integer totalpopulation,
            final Date createddate) {
        super();
        this.id = id;
        this.populationyear = populationyear;
        this.totalpopulation = totalpopulation;
        this.createddate = createddate;
    }

}
