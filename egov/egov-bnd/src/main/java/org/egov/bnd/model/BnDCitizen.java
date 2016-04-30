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

import org.apache.log4j.Logger;
import org.egov.infra.persistence.entity.Address;
import org.egov.portal.entity.Citizen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BnDCitizen extends Citizen {

	private static final long serialVersionUID = 8739168927130968076L;

	public BnDCitizen() {
		super();

	}

	// private Long m_citizenId = null;
	private String firstNameCorrected;
	private String middleNameCorrected;
	private String lastNameCorrected;
	// private Address address = null;

	private List<BndNameChange> newcitizenname = new ArrayList<BndNameChange>(); // collection
	// of
	// citizen
	// name

	private List<CitizenRelation> relations = new ArrayList<CitizenRelation>();// Collection
	// of
	// CCitizenRelation
	// objects

	protected final Logger logger = Logger.getLogger(getClass().getName());

	// private List nameInclusion =new ArrayList();

	/**
	 * @return Returns the firstNameCorrected.
	 */
	public String getFirstNameCorrected() {
		return firstNameCorrected;
	}

	/**
	 * @param firstNameCorrected
	 *            The firstNameCorrected to set.
	 */
	public void setFirstNameCorrected(final String firstNameCorrected) {
		this.firstNameCorrected = firstNameCorrected;
	}

	/**
	 * @return Returns the lastNameCorrected.
	 */
	public String getLastNameCorrected() {
		return lastNameCorrected;
	}

	/**
	 * @param lastNameCorrected
	 *            The lastNameCorrected to set.
	 */
	public void setLastNameCorrected(final String lastNameCorrected) {
		this.lastNameCorrected = lastNameCorrected;
	}

	/**
	 * @return Returns the middleNameCorrected.
	 */
	public String getMiddleNameCorrected() {
		return middleNameCorrected;
	}

	/**
	 * @param middleNameCorrected
	 *            The middleNameCorrected to set.
	 */
	public void setMiddleNameCorrected(final String middleNameCorrected) {
		this.middleNameCorrected = middleNameCorrected;
	}

	/**
	 * @return Returns the address.
	 */
	/*
	 * public Address getAddress() { return address; }
	 */
	/**
	 * @param address
	 *            The address to set.
	 */
	/*
	 * public void setAddress(Address address) { this.address = address; }
	 */
	/**
	 * @return Returns the relations.
	 */
	public List<CitizenRelation> getRelations() {
		return relations;
	}

	/**
	 * @param relations
	 *            The relations to set.
	 */
	public void setRelations(final List<CitizenRelation> relations) {
		this.relations = relations;
	}

	/**
	 * @param relatedCost
	 *            a string value that defines the relationship.
	 * @return A Citizen object that satisfies the relationship This method is a
	 *         helper method to get the person associated with a relationship.
	 *         We assume that there is only one person of a particular relation.
	 */

	public BnDCitizen getRelatedPerson(final String relatedCost) {
		BnDCitizen person = null;
		if (relations != null && !relations.isEmpty()) {
			final Iterator<CitizenRelation> itr = relations.iterator();
			while (itr.hasNext()) {
				final CitizenRelation citRelation = itr.next();
				if (citRelation != null)
					if (citRelation.getRelatedAs() != null
							&& relatedCost.equals(citRelation.getRelatedAs()
									.getRelatedAsConst())) {
						person = citRelation.getPerson();
						break;
					}
			}
		}
		return person;
	}

	/**
	 * @param relatedCost
	 *            a string value that defines the relationship.
	 * @return A Citizen object that satisfies the relationship This method is a
	 *         helper method to get the person associated with a relationship.
	 *         We assume that there is only one person of a particular relation.
	 */

	@SuppressWarnings("unchecked")
	public Address getRelatedAddress(final String relatedCost) {
		Address returnAddress = null;
		final Set<Address> addressSet = (Set<Address>) getAddress();
		if (addressSet != null && !addressSet.isEmpty()) {
			final Iterator<Address> itr = addressSet.iterator();
			while (itr.hasNext()) {
				final Address address = itr.next();
				if (address != null)
					if (address.getType() != null
							&& relatedCost.equals(address.getType().name())) {
						returnAddress = address;
						break;
					}
			}
		}
		return returnAddress;
	}

	/**
	 * @param relation
	 *            A BnDCitizen object that is related to this citizen
	 * @return A CRelation object that defines the relationship This method is a
	 *         helper method to get the relation associated with a person
	 *         object. We assume that every relation has only one person
	 *         associated with it.
	 */
	public CRelation getRelation(final BnDCitizen relation) {

		if (relation != null)
			if (relations != null && !relations.isEmpty()) {
				final Iterator<CitizenRelation> itr = relations.iterator();
				while (itr.hasNext()) {
					final CitizenRelation citRelation = itr.next();
					if (citRelation != null)
						if (citRelation.getPerson() != null
								&& citRelation.getPerson().getId().intValue() == relation
										.getId().intValue()) {
							logger.info("inside get relation getRelatedAs=="
									+ citRelation.getRelatedAs().getDesc());
							return citRelation.getRelatedAs();
						}
				}
			}
		return null;
	}

	public List<BndNameChange> addcitizenname(final BndNameChange citizen) {
		logger.debug(" Citizen new name add Invoked");
		citizen.setCitizen(this);
		getNewcitizenname().add(citizen);
		return newcitizenname;
	}

	public List<BndNameChange> removecitizenname(final BndNameChange citizen) {
		logger.debug("Citizen new name remove Invoked");
		getNewcitizenname().remove(citizen);
		return newcitizenname;
	}

	/**
	 * @return Returns the newcitizenname.
	 */
	public List<BndNameChange> getNewcitizenname() {
		return newcitizenname;
	}

	/**
	 * @param newcitizenname
	 *            The newcitizenname to set.
	 */

	public void setNewcitizenname(final List<BndNameChange> newcitizenname) {
		this.newcitizenname = newcitizenname;
	}

	/**
	 * This method is to update existing citizen details name with new citizen
	 * details
	 *
	 * @param newCitizen
	 */

	public void updateCitizenName(final BnDCitizen newCitizen) {
		// TODO egifix
		/*
		 * this.setFirstName(newCitizen.getName());
		 * this.setLastName(newCitizen.getLastName());
		 * this.setMiddleName(newCitizen.getMiddleName());
		 * setMobileNumber(newCitizen.getMobileNumber());
		 */
		setEmailId(newCitizen.getEmailId());
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		// TODO egifix
		/*
		 * builder.append("ID  :").append(getId()).append(" First Name :")
		 * .append(this.getFirstName() == null ? " " :
		 * this.getFirstName()).append(" Middle Name :")
		 * .append(this.getMiddleName() == null ? " " :
		 * this.getMiddleName()).append(" Last Name : ")
		 * .append(this.getLastName() == null ? " " :
		 * this.getLastName()).append(" Sex : ").append(getGender());
		 */
		return builder.toString();
	}

}
