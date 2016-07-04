<%@ tag dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="documentNumber" required="true" %>
<%@ attribute name="moduleName" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="showOnLoad" rtexprvalue="true" type="java.lang.Boolean" required="false" description="Controls if the files should be shown on window load. Default true"%>
<%@ attribute name="cssClass" required="false" %>

<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%
 if(cssClass==null) cssClass="bluebox";
 if(showOnLoad!=null && showOnLoad == false) 
 	showOnLoad=false;
 else
 	showOnLoad=true;
 	
%>

<script>

function populateFiles${id}(){
 	var content = document.getElementById("${id}filePanel");
 	content.innerHTML = "";
	var sUrl = "/egi/docmgmt/documentManager!viewDocumentObjectFiles.action?docNumber=${documentNumber}&moduleName=${moduleName}";
	var callback = {
		success:function (oResponse) {
			 createFileInfo(oResponse.responseText);
		}, 
	 	failure:function (oResponse) {
	 		content.innerHTML = "Error in retrieving files";
	 	}, 
		timeout:30000, 
		cache:false
	};
	var conn=YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	
	
	function createFileInfo (associatedFiles) {
		var files = eval(associatedFiles);
		var fileData="";
		for(i=0; i<files.length; i++)
		{
		   fileData = fileData + "<a href='/egi/docmgmt/ajaxFileDownload.action?moduleName=${moduleName}&docNumber=" + files[i].docNumber;
		   fileData += "&fileName=" + files[i].fileName + "' target='_parent' >"
		   fileData += files[i].fileName + "</a><br/>";
		}
		content.innerHTML = fileData;
		
	}
	
}


<%if(showOnLoad){%>
	YAHOO.util.Event.on(window, "load", populateFiles${id});
<%}%>

</script>

<div class="${cssClass}" id="${id}filePanel">
</div>

