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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.dao.property;

import org.egov.demand.dao.EgDemandDao;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.ptis.domain.dao.demand.PtDemandDao;

/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * Either use the <tt>DEFAULT</tt> to get the same concrete RNDDAOFactory
 * throughout your application, or a concrete factory by name, e.g.
 * <tt>RNDDAOFactory.HIBERNATE</tt> is a concrete
 * <tt>RNDHibernateDAOFactory</tt>.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it. If
 * you add a new persistence mechanism, add an additional concrete factory for
 * it to the enumeration of factories.
 * <p>
 * It probably wouldn't be a bad idea to move the <tt>DEFAULT</tt> setting into
 * external configuration.
 * 
 * @author christian.bauer@jboss.com
 */
public abstract class PropertyDAOFactory {

	private static final PropertyDAOFactory HIBERNATE = new PropertyHibernateDAOFactory();

	public static PropertyDAOFactory getDAOFactory() {
		return HIBERNATE;
	}

	public abstract BasicPropertyDAO getBasicPropertyDAO();

	public abstract ConstructionTypeSetDAO getConstructionTypeSetDAO();

	public abstract ConstructionTypeDAO getConstructionTypeDAO();

	public abstract FloorDAO getFloorDAO();

	public abstract PropertyDAO getPropertyDAO();

	public abstract PropertyDetailDAO getPropertyDetailDAO();

	public abstract PropertyOccupationDAO getPropertyOccupationDAO();

	public abstract PropertySourceDAO getPropertySourceDAO();

	public abstract PropertyStatusDAO getPropertyStatusDAO();

	public abstract PropertyUsageDAO getPropertyUsageDAO();

	public abstract StructureClassificationDAO getStructureClassificationDAO();

	public abstract FloorDAO getFloorImplDAO();

	public abstract PropertyCreationReasonsDAO getPropertyCreationReasonsDAO();

	public abstract PropertyIDDAO getPropertyIDDAO();

	public abstract PropertyTypeMasterDAO getPropertyTypeMasterDAO();

	public abstract CategoryDao getCategoryDao();

	public abstract BoundaryCategoryDao getBoundaryCategoryDao();

	public abstract TaxPercDAO getTaxPercDao();

	public abstract PropertyModifyReasonsDAO getPropertyModifyReasonsDAO();

	public abstract TaxPercentageforDatesDAO getTaxPercentageforDatesDAO();

	public abstract PropertyMutationMasterDAO getPropertyMutationMstrDAO();

	public abstract PropertyReferenceDAO getPropertyReferenceDAO();

	public abstract PropertyMutationDAO getPropertyMutationDAO();

	public abstract PtDemandDao getPtDemandDao();

	public abstract PropertyStatusValuesDAO getPropertyStatusValuesDAO();

	public abstract PropertyIntegrationDAO getPropertyIntegrationDAO();

	public abstract EgDemandDao getEgDemandDao();

	public abstract EgDemandDetailsDao getEgDemandDetailsDao();
}
