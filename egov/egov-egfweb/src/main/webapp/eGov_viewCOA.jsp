
<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>
<%@ page language="java"%>
<%@page import="org.egov.utils.FinancialConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" type="text/css"
	href="/EGF/resources/commonyui/build/treeview/assets/css/folders/tree.css">
<script src="/EGF/resources/commonyui/build/yahoo/yahoo-min.js"></script>
<script src="/EGF/resources/commonyui/build/event/event-min.js"></script>
<script
	src="/EGF/resources/commonyui/build/connection/connection-min.js"></script>
<!-- TreeView source file -->
<script src="/EGF/resources/commonyui/build/treeview/treeview-min.js"></script>
</head>
<body onload="treeInit()">
	<h2>Chart Of Accounts</h2>

	<div id="treeDiv1">
		<script>
	var coaDetailLength ='<%=FinancialConstants.GLCODEMAXLENGTH%>';
     <%
	 String actionId=request.getParameter("actionid");
	 if(actionId!=null)
		session.setAttribute("actionid",actionId);
	 if(actionId==null)
		actionId=(String)session.getAttribute("actionid");
		%>
		
		var actionId=<%=request.getParameter("actionid")%>

	 var userid = <%=session.getAttribute("com.egov.user.LoginUserId")%>;
	 
	function openPopupWindow(url, name, width, height)
	{
	    var myPopupWindow = '';
	  
	    //Remove special characters from name
	    name = name.replace(/\/|\-|\./gi, "");
	
	    //Remove whitespaces from name;
	    var whitespace = new RegExp("\\s","g");
	    name = name.replace(whitespace,"");
	var leftPos=document.body.clientWidth;
	    //If it is already open
	    if (!myPopupWindow.closed && myPopupWindow.location)
	    {
	        myPopupWindow.location.href = encodeUrl(url);
	    }
	    else
	    {
	     window.open(url,name, "resizable=yes,scrollbars=yes,left="+leftPos+",top=40, width="+width+", height= "+height);
		       
	    }
	    
	}

      /* This function is same as loadselectdata(), but here we load the values for a row in a table */

function loadSelectDataForChartOfacounts(url,sourceobj,destobj)
{
	
		var link=url;
		var request = initiateRequest();
		request.open("GET", link, false);
		request.send(null);		
		if (request.status == 200) 
		{
			var response=request.responseText.split("^");
			var id = response[1].split("\+");
			var name = response[2].split("+");
			var actionurl = response[4].split("+");
			var menuObj = destobj;
			menuObj.length = id.length;
			menuObj.length = 0;
			var nodeurl = null;
			
			for(var i = 0 ; i < id.length  ; i++)
			{
				
				var menuItem =  { label: name[i], id: id[i], url: actionurl[i] } ;
				var tempNode = new YAHOO.widget.TextNode(menuItem, menuObj, false);
				tempNode.onLabelClick = onLabelClick
				
				if( nodeurl == null || nodeurl=='null') {
					tempNode.setDynamicLoad(loadNodeData);
				} else{
				
					tempNode.isLeaf = true;
				}
						
			}					
		}
		
	}

      
      function treeInit()
      {                                                     
         
         buildTree(new YAHOO.widget.TreeView("treeDiv1"));
                              
      }
      function buildTree(tree){
      
      if (typeof(userid) != "undefined"){ 
	    
	     tree.setDynamicLoad(loadNodeData);   
	    
	     var root = tree.getRoot();   
	     
        loadSelectDataForChartOfacounts('/EGF/voucher/common-ajaxloadcoa.action','roleId',root);
	     tree.draw(); 
	      }        
     }             
    var onLabelClick = function(oNode) {       
        var baseUrl = oNode.data.url;
        var id=oNode.data.id;
        var windowname=oNode.data.label;
    
        if ('null' == baseUrl){
        }
        else{
			
			var url="${pageContext.request.contextPath}";
			actionidstr = 'actionId='+oNode.data.id;
			if (!baseUrl.indexOf("/") != 0) {
			    url= url + '/';
			   }
   			url = url + baseUrl;
			if (baseUrl.indexOf("?") > 0){
				url = url +'&'+actionidstr;
			} else
				{
			
			url = url +'?'+actionidstr;
				url="/EGF/masters/chartOfAccounts-viewChartOfAccounts.action?model.id="+id;
				}
				openPopupWindow(url, windowname , 900, 650)
		}			
    }    
function loadNodeData(node, fnLoadComplete){  
	var nodeLabel = encodeURI(node.label);
	var nodeId = encodeURI(node.data.id);
	var nodeUrl =  node.data.url;
	var url = '/EGF/voucher/common-ajaxloadcoa.action';
	var moduleQuery = url+'?glCode='+nodeId;
	url="/EGF/masters/chartOfAccounts-viewChartOfAccounts.action?model.id="+nodeId;
	//prepare our callback object
	var callback = {
	
			
		success: function(oResponse) {

			var response=oResponse.responseText.split("^");
			var id = response[1].split("+");
			var name = response[2].split("+");
			var actionurl = response[4].split("+");
			
			var nodeurl = null;
			
			for(var i = 0 ; i < id.length  ; i++)
			{
				
				if ((actionurl[i] != null) && (actionurl[i].length < 1)){
					nodeurl = null;
				} else{
					nodeurl = actionurl[i];
				}
				if ((name[i] != null) && (name[i].length > 0)){
				
					var menuItem =  { label: name[i], id: id[i], url: nodeurl } ;
					var tempNode = new YAHOO.widget.TextNode(menuItem, node, false);
					tempNode.onLabelClick = onLabelClick
					if( nodeurl.length<parseInt(coaDetailLength)) 
					{
					tempNode.setDynamicLoad(loadNodeData);
					} else{
							
						tempNode.isLeaf = true;
					}
				}
			}					
			oResponse.argument.fnLoadComplete();
		},		
		failure: function(oResponse) {
			oResponse.argument.fnLoadComplete();
		},		
		argument: {
			"node": node,
			"fnLoadComplete": fnLoadComplete
		},		
		timeout: 7000
	};	
	YAHOO.util.Connect.asyncRequest('GET', moduleQuery, callback);	          
    }    
</script>
	</div>
</body>
</html>
