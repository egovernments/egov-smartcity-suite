/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
function CookieManager()
{
}
/******************* Functions ********************/
CookieManager.setCookie = cookieManager_SetCookie;
CookieManager.getCookie = cookieManager_GetCookie;
CookieManager.delCookie = cookieManager_DelCookie;
/**************************************************/
function cookieManager_SetCookie(NameOfCookie, value, expiredays) 
{  
	var ExpireDate = new Date ();
	ExpireDate.setTime(ExpireDate.getTime() + (expiredays * 24 * 3600 * 1000));
  	document.cookie = NameOfCookie + "=" + escape(value) + ((expiredays == null) ? "" : "; expires=" + ExpireDate.toGMTString());
}
function cookieManager_GetCookie(NameOfCookie)
{  
	if (document.cookie.length > 0) 
	{
		begin = document.cookie.indexOf(NameOfCookie+"="); 
    		if (begin != -1) 
    		{
    			begin += NameOfCookie.length+1; 
      			end = document.cookie.indexOf(";", begin);
      		
      			if (end == -1) 
      				end = document.cookie.length;
      			return unescape(document.cookie.substring(begin, end));
      		} 
 	}
	return null; 
}
function cookieManager_DelCookie(NameOfCookie) 
{  
	if (getCookie(NameOfCookie)) 	
		document.cookie = NameOfCookie + "=" + "; expires=Thu, 01-Jan-70 00:00:01 GMT";	
}