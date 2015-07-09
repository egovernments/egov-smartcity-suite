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
/*
 * Created on Apr 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class CitizenRelation {

    private CRelation relatedAs; // Relationship such as father or mother or
    // brother etc
    private BnDCitizen person; // Related citizen that satisfies the
    // Relationship
    private Long id;
    private BnDCitizen cit; // Citizen Object

    /**
     * @return Returns the person.
     */
    public BnDCitizen getPerson() {
        return person;
    }

    /**
     * @param person
     *            The person to set.
     */
    public void setPerson(final BnDCitizen person) {
        this.person = person;
    }

    /**
     * @return Returns the relatedAs.
     */
    public CRelation getRelatedAs() {
        return relatedAs;
    }

    /**
     * @param relatedAs
     *            The relatedAs to set.
     */
    public void setRelatedAs(final CRelation relatedAs) {
        this.relatedAs = relatedAs;
    }

    /*
     * public boolean equals(Object obj) {
     * System.out.println("CCitizenRelation:in equals::::::::::"); if(obj ==
     * null) return false; CCitizenRelation tr = (CCitizenRelation)obj; if(this
     * == tr) return true; if(this.getPerson() != null && tr.getPerson()!= null)
     * { if(!this.getPerson().equals(tr.getPerson())) return false; } else
     * if(!(this.getPerson() == null && tr.getPerson() == null)) return false;
     * if(this.getRelatedAs() != null && tr.getRelatedAs() != null) {
     * if(!this.getRelatedAs().equals(tr.getRelatedAs())) return false; } else
     * if(!(this.getRelatedAs() == null && tr.getRelatedAs() == null)) return
     * false; if(this.getCit() != null && tr.getCit()!= null) {
     * if(!this.getCit().equals(tr.getCit())) return false; } else
     * if(!(this.getCit() == null && tr.getCit() == null)) return false;
     * if(this.getId()!= null && tr.getId()!= null) {
     * if(!this.getId().equals(tr.getId())) return false; } else
     * if(!(this.getId() == null && tr.getId() == null)) return false; return
     * true; } public int hashCode() {
     * System.out.println("CCitizenRelation:in hashcode::::::::::"); int
     * hashcode = 0; if(this.getPerson() != null) { hashcode +=
     * this.getPerson().hashCode(); } if(this.getCit() != null) { hashcode +=
     * this.getCit().hashCode(); } if(this.getId() != null) { hashcode +=
     * this.getId().hashCode(); } if(this.getRelatedAs() != null) { hashcode +=
     * this.getRelatedAs().hashCode(); } return hashcode; }
     */
    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return Returns the cit.
     */

    public BnDCitizen getCit() {
        return cit;
    }

    /**
     * @param cit
     *            The cit to set.
     */
    public void setCit(final BnDCitizen cit) {
        this.cit = cit;
    }

}
