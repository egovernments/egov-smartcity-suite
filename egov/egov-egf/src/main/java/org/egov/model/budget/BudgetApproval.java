/*
*eGov suite of products aim to improve the internal efficiency,transparency,
*     accountability and the service delivery of the government  organizations.
* 
*      Copyright (C) <2015>  eGovernments Foundation
* 
*      The updated version of eGov suite of products as by eGovernments Foundation
*      is available at http://www.egovernments.org
* 
*      This program is free software: you can redistribute it and/or modify
*      it under the terms of the GNU General Public License as published by
*      the Free Software Foundation, either version 3 of the License, or
*      any later version.
* 
*      This program is distributed in the hope that it will be useful,
*      but WITHOUT ANY WARRANTY; without even the implied warranty of
*      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*      GNU General Public License for more details.
* 
*      You should have received a copy of the GNU General Public License
*      along with this program. If not, see http://www.gnu.org/licenses/ or
*      http://www.gnu.org/licenses/gpl.html .
* 
*      In addition to the terms of the GPL license to be adhered to in using this
*      program, the following additional terms are to be complied with:
* 
*          1) All versions of this program, verbatim or modified must carry this
*             Legal Notice.
* 
*          2) Any misrepresentation of the origin of the material is prohibited. It
*             is required that all modified versions of this material be marked in
*             reasonable ways as different from the original version.
* 
*          3) This license does not grant any rights to any user of the program
*             with regards to rights under trademark law for use of the trade names
*             or trademarks of eGovernments Foundation.
* 
*    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
*/
package org.egov.model.budget;

import java.math.BigDecimal;

public class BudgetApproval {

    private Long id;
    
    private String department;
    
    private String parent;
    
    private String referenceBudget;
    
    private Long count;
    
    private BigDecimal reAmount;
    
    private BigDecimal beAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getReferenceBudget() {
        return referenceBudget;
    }

    public void setReferenceBudget(String referenceBudget) {
        this.referenceBudget = referenceBudget;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getReAmount() {
        return reAmount;
    }

    public void setReAmount(BigDecimal reAmount) {
        this.reAmount = reAmount;
    }

    public BigDecimal getBeAmount() {
        return beAmount;
    }

    public void setBeAmount(BigDecimal beAmount) {
        this.beAmount = beAmount;
    }

    
}
