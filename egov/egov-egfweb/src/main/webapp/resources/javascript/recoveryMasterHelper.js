/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
function initRequest() {
	if (window.XMLHttpRequest) {
		var req=new XMLHttpRequest();
		if (req.overrideMimeType) 
			req.overrideMimeType("text/html;charset=utf-8");
		return req;
	} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}


/* This function checks whether entered field is unique or not.
 * It returns a boolean value if it is unique, 
 * it returns true else false 
 * */

function uniqueIdentifierBoolean(url,tablename,columnname,fieldobj,uppercase,lowercase)
{
	var fieldvalue = document.getElementById(fieldobj).value;
	var isUnique;
		
	if(url != "" && tablename != "" && columnname != "" && fieldvalue != "" && uppercase != "" && lowercase != "")
	{
	fieldvalue = trimFieldValue(fieldvalue);
	var link = ""+url+"?tablename=" + tablename+"&columnname=" + columnname+ "&fieldvalue=" + fieldvalue+ "&uppercase=" + uppercase+ "&lowercase=" + lowercase+ " ";
	var request = initRequest();
	request.open("GET", link, false);
	request.send(null);
	
		if (request.readyState == 4) 
		{
			if (request.status == 200) 
			{
				var response=request.responseText.split("^");
				
				if(response[0]=="false")
				{
					isUnique=false;
	 			}
	 			else
	 			isUnique=true;
			}
		}
	}
	
	return isUnique;
}

function trimFieldValue(value) {
   if(value!=undefined)
   {

	   while (value.charAt(value.length-1) == " ")
	   {
		value = value.substring(0,value.length-1);
		
	   }
	   while(value.substring(0,1) ==" ")
	   {
		value = value.substring(1,value.length);
		
	   }	   
   }
   
   return value ;
}

