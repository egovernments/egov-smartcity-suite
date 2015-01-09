<%@ tag dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="documentNumber" required="true" %>
<%@ attribute name="moduleName" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="showOnLoad" rtexprvalue="true" type="java.lang.Boolean" required="false" description="Controls if the files should be shown on window load. Default true"%>
<%@ attribute name="cssClass" required="false" %>

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

