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
//<script language=javascript>
// parameters to be set for a gievn installation for Exility


var urlStr = document.location.href;
//bootbox.alert("urlStr = " + urlStr);
urlLen = urlStr.length;


if(urlStr.indexOf("http://") > -1)
		{
			urlStr = urlStr.substring(urlStr.indexOf("http://")+"http://".length,urlLen);
			//bootbox.alert("urlStr AGAIN" + urlStr);
			urlLenHTTP = urlStr.indexOf("http://");
			//bootbox.alert("urlLenHTTP : " + urlLenHTTP);
			urlLenHTTP = urlStr.indexOf("/");
			
			urlStr = urlStr.substring(urlStr.indexOf("/")+"/".length,urlLen);
			urlStr = urlStr.substring(urlStr.indexOf("/")+"/".length,urlLen);
			//bootbox.alert("urlStr finally : " + urlStr);
		}
if(urlStr.indexOf("https://") > -1)
		{
			urlStr = urlStr.substring(urlStr.indexOf("https://")+"https://".length,urlLen);
			//bootbox.alert("urlStr AGAIN" + urlStr);
			urlLenHTTP = urlStr.indexOf("https://");
			//bootbox.alert("urlLenHTTP : " + urlLenHTTP);
			urlLenHTTP = urlStr.indexOf("/");
			
			urlStr = urlStr.substring(urlStr.indexOf("/")+"/".length,urlLen);
			urlStr = urlStr.substring(urlStr.indexOf("/")+"/".length,urlLen);
			//bootbox.alert("urlStr finally : " + urlStr);
		}

var totOcc = 0;
var urlLn=0;
urlLn=urlStr.indexOf("?");
if (urlLn==-1) urlLn=urlStr.length;
//bootbox.alert("&:"+urlLn);
for (var p=1;p<urlLn;p++)
{
	var x=urlStr.charAt(p);
	if (x == "/")
	{
		totOcc++;
	}	
}
//bootbox.alert(totOcc);


var exilParms = new Object(); 
var pathResol = "../";
for (var i=1;i<totOcc;i++ )
{
	pathResol = pathResol+ pathResol;
}
//bootbox.alert(pathResol);

//All Descriotion Service will be requested to this URL
exilParms.descServiceName	= pathResol+"GetDescription.jsp";
//All List Service will be requested to this URL
exilParms.listServiceName	=  pathResol+"GetList.jsp";
//All List Service will be requested to this URL
exilParms.treeServiceName	=  pathResol+"GetTree.jsp";

// If this is set to non-null string, all update services will be
// sent to this with ?serviceID=xxxxxx. Else the serviceID is assumed to be the URL
exilParms.updateServiceName	=  pathResol+"SubmitData.jsp"; 
// similar meaning foor Data service
exilParms.dataServiceName	=  pathResol+"GetData.jsp";
exilParms.minDate = new Date("January 01, 1900"); 
exilParms.maxDate = new Date("January 01, 2100");
exilParms.verbose = false;
//value to be sent to server when a check box is checked/unchecked
exilParms.checkedValue = 1;
exilParms.uncheckedValue = 0;

//</script>
