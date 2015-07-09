#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/struts-tags" %>  
	
	<s:date name="deathHistory.registrationDate" format="dd/MM/yyyy" var="registrationDateHistory" />
	<s:date name="deathHistory.dateOfEvent" format="dd/MM/yyyy" var="dateOfEventHistory" />		
	<s:hidden id="deathHistory.registrationDate" name="deathHistory.registrationDate" value="%{registrationDateHistory}" />
	<s:hidden id="deathHistory.registrationNo" name="deathHistory.registrationNo" value="%{deathHistory.registrationNo}" />
	<s:hidden id="deathHistory.dateOfEvent" name="deathHistory.dateOfEvent" value="%{dateOfEventHistory}" />
	<s:hidden id="deathHistory.citizen.sex" value="%{deathHistory.citizen.sex}" name="deathHistory.citizen.sex" />
	<s:hidden id="deathHistory.citizen.firstName" value="%{deathHistory.citizen.firstName}" name="deathHistory.citizen.firstName"/>
	<s:hidden id="deathHistory.citizen.middleName" value="%{deathHistory.citizen.middleName}" name="deathHistory.citizen.middleName"/>
	<s:hidden id="deathHistory.citizen.lastName" value="%{deathHistory.citizen.lastName}" name="deathHistory.citizen.lastName"/>
	<s:hidden id="deathHistory.permanentCitizenAddress.addressID" value="%{deathHistory.permanentCitizenAddress.addressID}" name="deathHistory.permanentCitizenAddress.addressID"  />
	<s:hidden id="deathHistory.permanentCitizenAddress.streetAddress1" value="%{deathHistory.permanentCitizenAddress.streetAddress1}" name="deathHistory.permanentCitizenAddress.streetAddress1"  />
	<s:hidden id="deathHistory.permanentCitizenAddress.streetAddress2" value="%{deathHistory.permanentCitizenAddress.streetAddress2}" name="deathHistory.permanentCitizenAddress.streetAddress2"/>
	<s:hidden id="deathHistory.permanentCitizenAddress.cityTownVillage" value="%{deathHistory.permanentCitizenAddress.cityTownVillage}" name="deathHistory.permanentCitizenAddress.cityTownVillage"/>
	<s:hidden name="deathHistory.permanentCitizenAddress.taluk" id="deathHistory.permanentCitizenAddress.taluk" value="%{deathHistory.permanentCitizenAddress.taluk}"/>
	<s:hidden name="deathHistory.permanentCitizenAddress.pinCode" id="deathHistory.permanentCitizenAddress.pinCode" value="%{deathHistory.permanentCitizenAddress.pinCode}" />
	<s:hidden name="deathHistory.permanentCitizenAddress.district" id="deathHistory.permanentCitizenAddress.district" value="%{deathHistory.permanentCitizenAddress.district}"/>
	<s:hidden name="deathHistory.permanentCitizenAddress.state" id="deathHistory.permanentCitizenAddress.state" value="%{deathHistory.permanentCitizenAddress.state}"/>
	<s:hidden value="%{placeTypeTempDeathHistory}" name="placeTypeTempDeathHistory" id="placeTypeTempDeathHistory"/>
	<s:hidden name="deathHistory.hospitalType" id="deathHistory.hospitalType" value="%{deathHistory.establishment.type.desc}"/>
	<s:hidden name="deathHistory.establishment1" id="deathHistory.establishment1" value="%{deathHistory.establishment.id}"/>
	<s:hidden name="deathHistory.eventAddress.addressID" id="deathHistory.eventAddress.addressID" value="%{deathHistory.eventAddress.addressID}" />
	<s:hidden name="deathHistory.eventAddress.streetAddress1" id="deathHistory.eventAddress.streetAddress1" value="%{deathHistory.eventAddress.streetAddress1}" />
	<s:hidden name="deathHistory.eventAddress.streetAddress2" id="deathHistory.eventAddress.streetAddress2" value="%{deathHistory.eventAddress.streetAddress2}" />
	<s:hidden name="deathHistory.eventAddress.taluk" id="deathHistory.eventAddress.taluk" value="%{deathHistory.eventAddress.taluk}"/>
	<s:hidden name="deathHistory.eventAddress.cityTownVillage" id="deathHistory.eventAddress.cityTownVillage" value="%{deathHistory.eventAddress.cityTownVillage}"/>
	<s:hidden name="deathHistory.eventAddress.pinCode" id="deathHistory.eventAddress.pinCode" value="%{deathHistory.eventAddress.pinCode}" />
	<s:hidden name="deathHistory.eventAddress.district" id="deathHistory.eventAddress.district" value="%{deathHistory.eventAddress.district}"/>
	<s:hidden name="deathHistory.eventAddress.state" id="deathHistory.eventAddress.state" value="%{deathHistory.eventAddress.state}" />
	<s:hidden name="deathHistory.mother.firstName" id="deathHistory.mother.firstName" value="%{deathHistory.mother.firstName}"/>
	<s:hidden name="deathHistory.mother.middleName" id="deathHistory.mother.middleName" value="%{deathHistory.mother.middleName}" />
	<s:hidden name="deathHistory.mother.lastName" id="deathHistory.mother.lastName" value="%{deathHistory.mother.lastName}"/>
	<s:hidden name="deathHistory.deceasedrelation.firstName" id="deathHistory.deceasedrelation.firstName" value="%{deathHistory.deceasedrelation.firstName}"/>
	<s:hidden name="deathHistory.deceasedrelation.citizenID" id="deathHistory.deceasedrelation.citizenID" value="%{deathHistory.deceasedrelation.citizenID}"/>
	<s:hidden name="deathHistory.deceasedrelation.middleName" id="deathHistory.deceasedrelation.middleName" value="%{deathHistory.deceasedrelation.middleName}" />
	<s:hidden name="deathHistory.deceasedrelation.lastName" id="deathHistory.deceasedrelation.lastName" value="%{deathHistory.deceasedrelation.lastName}"/>
	<s:hidden name="deathHistory.deceasedAddress.addressID" id="deathHistory.deceasedAddress.addressID" value="%{deathHistory.deceasedAddress.addressID}" />
	<s:hidden name="deathHistory.deceasedAddress.streetAddress1" id="deathHistory.deceasedAddress.streetAddress1" value="%{deathHistory.deceasedAddress.streetAddress1}" />
	<s:hidden name="deathHistory.deceasedAddress.streetAddress2" id="deathHistory.deceasedAddress.streetAddress2" value="%{deathHistory.deceasedAddress.streetAddress2}" />
	<s:hidden name="deathHistory.deceasedAddress.taluk" id="deathHistory.deceasedAddress.taluk" value="%{deathHistory.deceasedAddress.taluk}"/>
	<s:hidden name="deathHistory.deceasedAddress.cityTownVillage" id="deathHistory.deceasedAddress.cityTownVillage" value="%{deathHistory.deceasedAddress.cityTownVillage}"/>
	<s:hidden name="deathHistory.deceasedAddress.pinCode" id="deathHistory.deceasedAddress.pinCode" value="%{deathHistory.deceasedAddress.pinCode}"/>
	<s:hidden name="deathHistory.deceasedAddress.district" id="deathHistory.deceasedAddress.district" value="%{deathHistory.deceasedAddress.district}"/>
	<s:hidden name="deathHistory.deceasedAddress.state" id="deathHistory.deceasedAddress.state" value="%{deathHistory.deceasedAddress.state}" />
	<s:hidden id="deathHistory.deceasedrelationType.relatedAsConst" name="deathHistory.deceasedrelationType.relatedAsConst" value="%{deathHistory.deceasedrelationType.relatedAsConst}"/>
	<s:hidden id="deathHistory.ageType.ageTypedesc" name="deathHistory.ageType.ageTypedesc" value="%{deathHistory.ageType.ageTypedesc}"/>
	<s:hidden id="deathHistory.age" name="deathHistory.age" value="%{deathHistory.age}"/>
	<s:hidden id="deathHistory.crematorium.crematoriumconst" name="deathHistory.crematorium.crematoriumconst" value="%{deathHistory.crematorium.crematoriumconst}"/>
	<s:hidden id="deathHistory.relationType.relatedAsConst" name="deathHistory.relationType.relatedAsConst" value="%{deathHistory.relationType.relatedAsConst}"/>
	<s:hidden name="informantCitizenDeathHistory.firstName" id="informantCitizenDeathHistory.firstName" value="%{informantCitizenDeathHistory.firstName}"/>
	<s:hidden name="informantCitizenDeathHistory.middleName" id="informantCitizenDeathHistory.middleName" value="%{informantCitizenDeathHistory.middleName}" />
	<s:hidden name="informantCitizenDeathHistory.lastName" id="informantCitizenDeathHistory.lastName" value="%{informantCitizenDeathHistory.lastName}"/>
	<s:hidden name="deathHistory.informantAddress.addressID" id="deathHistory.informantAddress.addressID" value="%{deathHistory.informantAddress.addressID}" />
	<s:hidden name="deathHistory.informantAddress.streetAddress1" id="deathHistory.informantAddress.streetAddress1" value="%{deathHistory.informantAddress.streetAddress1}" />
	<s:hidden name="deathHistory.informantAddress.streetAddress2" id="deathHistory.informantAddress.streetAddress2" value="%{deathHistory.informantAddress.streetAddress2}" />
	<s:hidden name="deathHistory.informantAddress.taluk" id="deathHistory.informantAddress.taluk" value="%{deathHistory.informantAddress.taluk}"/>
	<s:hidden name="deathHistory.informantAddress.cityTownVillage" id="deathHistory.informantAddress.cityTownVillage" value="%{deathHistory.informantAddress.cityTownVillage}"/>
	<s:hidden name="deathHistory.informantAddress.pinCode" id="deathHistory.informantAddress.pinCode" value="%{deathHistory.informantAddress.pinCode}" />
	<s:hidden name="deathHistory.informantAddress.district" id="deathHistory.informantAddress.district" value="%{deathHistory.informantAddress.district}"/>
	<s:hidden name="deathHistory.informantAddress.state" id="deathHistory.informantAddress.state" value="%{deathHistory.informantAddress.state}" />
	<s:hidden name="remarksHistory" id="remarksHistory" value="%{remarksHistory}" />
	
