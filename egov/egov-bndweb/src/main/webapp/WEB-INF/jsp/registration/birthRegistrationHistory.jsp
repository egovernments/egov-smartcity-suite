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
	
	<s:date name="birthHistory.registrationDate" format="dd/MM/yyyy" var="registrationDateHistory" />
	<s:date name="birthHistory.dateOfEvent" format="dd/MM/yyyy" var="dateOfEventHistory" />		
	<s:hidden id="birthHistory.registrationDate" name="birthHistory.registrationDate" value="%{registrationDateHistory}" />
	<s:hidden id="birthHistory.registrationNo" name="birthHistory.registrationNo" value="%{birthHistory.registrationNo}" />
	<s:hidden id="birthHistory.dateOfEvent" name="birthHistory.dateOfEvent" value="%{dateOfEventHistory}" />
	<s:hidden id="birthHistory.citizen.sex" value="%{birthHistory.citizen.sex}" name="birthHistory.citizen.sex" />
	<s:hidden id="birthHistory.citizen.firstName" value="%{birthHistory.citizen.firstName}" name="birthHistory.citizen.firstName"/>
	<s:hidden id="birthHistory.citizen.middleName" value="%{birthHistory.citizen.middleName}" name="birthHistory.citizen.middleName"/>
	<s:hidden id="birthHistory.citizen.lastName" value="%{birthHistory.citizen.lastName}" name="birthHistory.citizen.lastName"/>
	<s:hidden id="birthHistory.permanentCitizenAddress.addressID" value="%{birthHistory.permanentCitizenAddress.addressID}" name="birthHistory.permanentCitizenAddress.addressID"  />
	<s:hidden id="birthHistory.permanentCitizenAddress.streetAddress1" value="%{birthHistory.permanentCitizenAddress.streetAddress1}" name="birthHistory.permanentCitizenAddress.streetAddress1"  />
	<s:hidden id="birthHistory.permanentCitizenAddress.streetAddress2" value="%{birthHistory.permanentCitizenAddress.streetAddress2}" name="birthHistory.permanentCitizenAddress.streetAddress2"/>
	<s:hidden id="birthHistory.permanentCitizenAddress.cityTownVillage" value="%{birthHistory.permanentCitizenAddress.cityTownVillage}" name="birthHistory.permanentCitizenAddress.cityTownVillage"/>
	<s:hidden name="birthHistory.permanentCitizenAddress.taluk" id="birthHistory.permanentCitizenAddress.taluk" value="%{birthHistory.permanentCitizenAddress.taluk}"/>
	<s:hidden name="birthHistory.permanentCitizenAddress.pinCode" id="birthHistory.permanentCitizenAddress.pinCode" value="%{birthHistory.permanentCitizenAddress.pinCode}" />
	<s:hidden name="birthHistory.permanentCitizenAddress.district" id="birthHistory.permanentCitizenAddress.district" value="%{birthHistory.permanentCitizenAddress.district}"/>
	<s:hidden name="birthHistory.permanentCitizenAddress.state" id="birthHistory.permanentCitizenAddress.state" value="%{birthHistory.permanentCitizenAddress.state}"/>
	<s:hidden value="%{placeTypeTempBirthHistory}" name="placeTypeTempBirthHistory" id="placeTypeTempBirthHistory"/>
	<s:hidden name="birthHistory.hospitalType" id="birthHistory.hospitalType" value="%{birthHistory.establishment.type.desc}"/>
	<s:hidden name="birthHistory.establishment.id" id="birthHistory.establishment" value="%{birthHistory.establishment.id}"/>
	<s:hidden name="birthHistory.eventAddress.addressID" id="birthHistory.eventAddress.addressID" value="%{birthHistory.eventAddress.addressID}" />
	<s:hidden name="birthHistory.eventAddress.streetAddress1" id="birthHistory.eventAddress.streetAddress1" value="%{birthHistory.eventAddress.streetAddress1}" />
	<s:hidden name="birthHistory.eventAddress.streetAddress2" id="birthHistory.eventAddress.streetAddress2" value="%{birthHistory.eventAddress.streetAddress2}" />
	<s:hidden name="birthHistory.eventAddress.taluk" id="birthHistory.eventAddress.taluk" value="%{birthHistory.eventAddress.taluk}"/>
	<s:hidden name="birthHistory.eventAddress.cityTownVillage" id="birthHistory.eventAddress.cityTownVillage" value="%{birthHistory.eventAddress.cityTownVillage}"/>
	<s:hidden name="birthHistory.eventAddress.pinCode" id="birthHistory.eventAddress.pinCode" value="%{birthHistory.eventAddress.pinCode}" />
	<s:hidden name="birthHistory.eventAddress.district" id="birthHistory.eventAddress.district" value="%{birthHistory.eventAddress.district}"/>
	<s:hidden name="birthHistory.eventAddress.state" id="birthHistory.eventAddress.state" value="%{birthHistory.eventAddress.state}" />
	<s:hidden name="birthHistory.mother.firstName" id="birthHistory.mother.firstName" value="%{birthHistory.mother.firstName}"/>
	<s:hidden name="birthHistory.mother.middleName" id="birthHistory.mother.middleName" value="%{birthHistory.mother.middleName}" />
	<s:hidden name="birthHistory.mother.lastName" id="birthHistory.mother.lastName" value="%{birthHistory.mother.lastName}"/>
	<s:hidden name="birthHistory.mother.mobilePhone" id="birthHistory.mother.mobilePhone" value="%{birthHistory.mother.mobilePhone}"/>
	<s:hidden name="birthHistory.mother.emailAddress" id="birthHistory.mother.emailAddress" value="%{birthHistory.mother.emailAddress}" />
	<s:hidden name="birthHistory.father.firstName" id="birthHistory.father.firstName" value="%{birthHistory.father.firstName}"/>
	<s:hidden name="birthHistory.father.citizenID" id="birthHistory.father.citizenID" value="%{birthHistory.father.citizenID}"/>
	<s:hidden name="birthHistory.father.middleName" id="birthHistory.father.middleName" value="%{birthHistory.father.middleName}" />
	<s:hidden name="birthHistory.father.lastName" id="birthHistory.father.lastName" value="%{birthHistory.father.lastName}"/>
	<s:hidden name="birthHistory.father.mobilePhone" id="birthHistory.father.mobilePhone" value="%{birthHistory.father.mobilePhone}"/>
	<s:hidden name="birthHistory.father.emailAddress" id="birthHistory.father.emailAddress" value="%{birthHistory.father.emailAddress}" />
	<s:hidden name="birthHistory.parentAddress.addressID" id="birthHistory.parentAddress.addressID" value="%{birthHistory.parentAddress.addressID}" />
	<s:hidden name="birthHistory.parentAddress.streetAddress1" id="birthHistory.parentAddress.streetAddress1" value="%{birthHistory.parentAddress.streetAddress1}" />
	<s:hidden name="birthHistory.parentAddress.streetAddress2" id="birthHistory.parentAddress.streetAddress2" value="%{birthHistory.parentAddress.streetAddress2}" />
	<s:hidden name="birthHistory.parentAddress.taluk" id="birthHistory.parentAddress.taluk" value="%{birthHistory.parentAddress.taluk}"/>
	<s:hidden name="birthHistory.parentAddress.cityTownVillage" id="birthHistory.parentAddress.cityTownVillage" value="%{birthHistory.parentAddress.cityTownVillage}"/>
	<s:hidden name="birthHistory.parentAddress.pinCode" id="birthHistory.parentAddress.pinCode" value="%{birthHistory.parentAddress.pinCode}"/>
	<s:hidden name="birthHistory.parentAddress.district" id="birthHistory.parentAddress.district" value="%{birthHistory.parentAddress.district}"/>
	<s:hidden name="birthHistory.parentAddress.state" id="birthHistory.parentAddress.state" value="%{birthHistory.parentAddress.state}" />
	<s:hidden id="informantFlagBirthHistory" name="informantFlagBirthHistory" value="%{informantFlagBirthHistory}"/>
	<s:hidden name="informantCitizenBirthHistory.firstName" id="informantCitizenBirthHistory.firstName" value="%{informantCitizenBirthHistory.firstName}"/>
	<s:hidden name="informantCitizenBirthHistory.middleName" id="informantCitizenBirthHistory.middleName" value="%{informantCitizenBirthHistory.middleName}" />
	<s:hidden name="informantCitizenBirthHistory.lastName" id="informantCitizenBirthHistory.lastName" value="%{informantCitizenBirthHistory.lastName}"/>
	<s:hidden name="birthHistory.informantAddress.addressID" id="birthHistory.informantAddress.addressID" value="%{birthHistory.informantAddress.addressID}" />
	<s:hidden name="birthHistory.informantAddress.streetAddress1" id="birthHistory.informantAddress.streetAddress1" value="%{birthHistory.informantAddress.streetAddress1}" />
	<s:hidden name="birthHistory.informantAddress.streetAddress2" id="birthHistory.informantAddress.streetAddress2" value="%{birthHistory.informantAddress.streetAddress2}" />
	<s:hidden name="birthHistory.informantAddress.taluk" id="birthHistory.informantAddress.taluk" value="%{birthHistory.informantAddress.taluk}"/>
	<s:hidden name="birthHistory.informantAddress.cityTownVillage" id="birthHistory.informantAddress.cityTownVillage" value="%{birthHistory.informantAddress.cityTownVillage}"/>
	<s:hidden name="birthHistory.informantAddress.pinCode" id="birthHistory.informantAddress.pinCode" value="%{birthHistory.informantAddress.pinCode}" />
	<s:hidden name="birthHistory.informantAddress.district" id="birthHistory.informantAddress.district" value="%{birthHistory.informantAddress.district}"/>
	<s:hidden name="birthHistory.informantAddress.state" id="birthHistory.informantAddress.state" value="%{birthHistory.informantAddress.state}" />
	<s:hidden name="remarksHistory" id="remarksHistory" value="%{remarksHistory}" />
	
