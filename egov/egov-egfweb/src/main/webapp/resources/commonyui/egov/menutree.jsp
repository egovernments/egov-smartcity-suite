<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<html>
	<head>
		<!-- Required CSS -->
		<link rel="stylesheet" type="text/css"
			href="../build/treeview/assets/css/folders/tree.css">

		<!-- Dependency source files -->
		<script type="text/javascript" src="../build/yahoo/yahoo-min.js"></script>
		<script type="text/javascript" src="../build/event/event-min.js"></script>
		<script type="text/javascript"
			src="../build/connection/connection-min.js"></script>
		<script type="text/javascript" src="../../script/jsCommonMethods.js"></script>
		<script type="text/javascript" src="../build/treeview/treeview-min.js"></script>
	</head>
	<body onload="treeInit()">
		<h2>
			Features
		</h2>
		<c:set var="eGovAppName" value="${param.eGovAppName}" scope="page" />
		<c:set var="order" value="${param.order}" scope="page" />
		<c:set var="winObj" value="${param.window}" scope="page" />

		<div id="treeDiv1">

			<script>

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
	    if (!myPopupWindow.closed && myPopupWindow.location) {
	        myPopupWindow.location.href = encodeUrl(url);
	    } else {
	    	if(url.lastIndexOf('window=left')==-1 && url.lastIndexOf('window=right')==-1 && '${winObj}'!='left' && '${winObj}'!='right')
	    		window.open(url,name, "resizable=yes,scrollbars=yes,left="+leftPos+",top=40, width="+width+", height= "+height);
	     	else if(url.lastIndexOf('window=right')!=-1 || '${winObj}'=='right')
	    		parent.mainFrame.location=url;
 			else if(url.lastIndexOf('window=left')!=-1 || '${winObj}'=='left')
		    	window.open(url,"_top");
	    }

	}

      /* This function is same as loadselectdata(), but here we load the values for a row in a table */
	function loadSelectDataForMenutree(url,tablename,columnname1,columnname2, columnname3, columnname4, whereclause,sourceobj,destobj) {
		
		if(url != "" && tablename != "" && columnname1 != "" && columnname2 != "" && columnname3 != "" && whereclause != "") {
			var link = ""+url+"?tablename=" + tablename+"&columnnamemod1=" + columnname1+ "&columnnamemod2=" + columnname2+
			"&columnnamemod3="+columnname3+"&columnnamemod4="+columnname4+"&whereclause1=" + whereclause+ " ";
			var request = initiateRequest();
			request.open("GET", link, false);
			request.send(null);
			if (request.status == 200) {
				var response=request.responseText.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
				var actionurl = response[2].split("+");
				var menuObj = destobj;
				menuObj.length = id.length;
				menuObj.length = 0;
				var nodeurl = null;
	
				for(var i = 0 ; i < id.length  ; i++)
				{
	
					if ((actionurl[i] == null) || (actionurl[i].substr("http") <= 0)){
						nodeurl = null;
					} else{
						nodeurl = actionurl[i];
					}
					var menuItem =  { label: name[i], id: id[i], url: nodeurl } ;
					var tempNode = new YAHOO.widget.TextNode(menuItem, menuObj, false);
					tempNode.onLabelClick = onLabelClick;
					if( nodeurl == null || nodeurl=='null') {
						tempNode.setDynamicLoad(loadNodeData);
					} else{
						tempNode.isLeaf = true;
					}
	
				}
			}
		}
	}
     
     function treeInit() {
		buildTree(new YAHOO.widget.TreeView("treeDiv1"));
	 }

     function buildTree(tree){
		if (typeof(userid) != "undefined"){
	      //turn dynamic loading on for entire tree:
	      tree.setDynamicLoad(loadNodeData);
	      //get root node for tree:
	      var root = tree.getRoot();	      
	      loadSelectDataForMenutree('loadmenutreeajax.jsp','V_EG_ROLE_ACTION_MODULE_MAP v, eg_roleaction_map ra',
	      'distinct v.module_id','v.module_name',null,'v.order_number',"v.IS_ENABLED=1 and v.module_id = (select id_module from eg_module where module_name = '${eGovAppName}' )"
	      ,'roleId',
	      root);

	      tree.draw();
	 	}
	}

    var onLabelClick = function(oNode) {
        var baseUrl = oNode.data.url;
        var windowname=oNode.data.label;
        
        if ('null' == baseUrl){
        }
        else{
			//is this a leaf node? If so, it will have an action url on it
			//append the actionid to the action url
			<%
			String context=request.getContextPath();
			if(request.getParameter("context")!=null){
			  context= request.getParameter("context").trim();
			}%>
			var url="<%=context%>";
			var actionidstr = 'actionid='+oNode.data.id;
			if (baseUrl.indexOf("/") != 0 && url.length > 0) {
				url= url + '/';
			}
			url = url + baseUrl;
			if (baseUrl.indexOf("?") > 0){
				url = url +'&'+actionidstr;
			} else{
				url = url +'?'+actionidstr;
			}

			openPopupWindow(url, windowname , 900, 650)
		}

    }

	function loadNodeData(node, fnLoadComplete){
			var nodeLabel = encodeURI(node.label);
			var nodeId = encodeURI(node.data.id);
			var nodeUrl =  node.data.url
			url = 'loadmenutreeajax.jsp';
			var tablename = 'V_EG_ROLE_ACTION_MODULE_MAP v';
			var columnname1Mod = 'distinct v.module_id as id';
			var columnname2Mod = 'v.module_name as name';;
			var columnname3Mod = 'null as url';
			var columnname4Mod = 'v.typeflag as typeflag';
			var columnname5Mod = 'v.order_number as ordernumber';
			var tablename = 'V_EG_ROLE_ACTION_MODULE_MAP v';
			var columnname1Act = 'distinct v.action_id as id';
			var columnname2Act = 'v.action_name as name';;
			var columnname3Act = 'v.action_url as url';
			var columnname4Act = 'v.typeflag as typeflag';
			var columnname5Act = 'v.order_number as ordernumber';
			if("${order}"=="") {
		
			  actionwhereclause = " parent_id = "+nodeId+ " and typeflag='A' and v.IS_ENABLED=1 and ( v.action_id in (select actionid from eg_roleaction_map ra " +
			       " where ra.ROLEID in (select id_role from eg_userrole ur where ur.ID_USER = " + userid + " )) " +
		 		    " OR NOT EXISTS (SELECT actionid FROM eg_roleaction_map ra where actionid = v.action_id)) order by typeflag desc,ordernumber asc";
			} else {
			  actionwhereclause = " parent_id = "+nodeId+ " and typeflag='A' and v.IS_ENABLED=1 and ( v.action_id in (select actionid from eg_roleaction_map ra " +
			   	" where ra.ROLEID in (select id_role from eg_userrole ur where ur.ID_USER = " + userid + " )) " +
		 			" OR NOT EXISTS (SELECT actionid FROM eg_roleaction_map ra where actionid = v.action_id)) order by typeflag ${order},ordernumber asc";
			}
			
			modulewhereclause = ' parent_id = '+nodeId + " and typeflag='M' and v.IS_ENABLED=1 and exists "+
			 "(select id from eg_action a1, eg_roleaction_map ra1 where a1.module_id = v.module_id and a1.is_enabled = 1 and a1.id = ra1.actionid "+
			 " and ra1.roleid in (select id_role from eg_userrole ur1 where ur1.ID_USER = "+ userid +" ) "+
			 "UNION (select id_module from eg_module m1 where m1.parentid = v.module_id and m1.isenabled=1) )";
			 var moduleQuery = url+'?tablename=' + tablename+'&columnnamemod1=' + columnname1Mod+ '&columnnamemod2=' + columnname2Mod +
			     '&columnnamemod3='+columnname3Mod+'&columnnamemod4='+columnname4Mod+'&columnnamemod5='+columnname5Mod+'&whereclause1=' + modulewhereclause+ ' '+'&columnnameact1='+columnname1Act+
		     '&columnnameact2='+columnname2Act+'&columnnameact3='+columnname3Act+'&columnnameact4='+columnname4Act+'&columnnameact5='+columnname5Act+'&whereclause2= '+actionwhereclause+' ';
			
			//first call for modules		
			//prepare our callback object
			var callback = {
		
				//if our XHR call is successful, we want to make use
				//of the returned data and create child nodes.
				success: function(oResponse) {
					var respHeader =oResponse.getResponseHeader["Content-Type"];
					if(respHeader.indexOf("text/xml") < 0){
						top.location = '/egi/logout.do';
						top.opener.location = '/egi/logout.do'
					}			
					
				   	var response=oResponse.responseText.split("^");
					var id = response[0].split("+");
					var name = response[1].split("+");
					var actionurl = response[2].split("+");
					var nodeurl = null;
					
					for(var i = 0 ; i < id.length  ; i++) {		
						
						if ((actionurl[i] == null) || (actionurl[i].length < 1)){
							nodeurl = null;
						} else{
							nodeurl = actionurl[i];
						}
						if ((name[i] != null) && (name[i].length > 0)){
							var menuItem =  { label: name[i], id: id[i], url: nodeurl } ;
							var tempNode = new YAHOO.widget.TextNode(menuItem, node, false);
							tempNode.onLabelClick = onLabelClick;
							if( nodeurl == null || nodeurl == 'null') {
									tempNode.setDynamicLoad(loadNodeData);
								} else{
									tempNode.isLeaf = true;
							}
						}
					}
					//When we're done creating child nodes, we execute the node's
					//loadComplete callback method which comes in via the argument
					//in the response object (we could also access it at node.loadComplete,
					//if necessary):
					oResponse.argument.fnLoadComplete();
				},
		
				//if our XHR call is not successful, we want to
				//fire the TreeView callback and let the Tree
				//proceed with its business.
				failure: function(oResponse) {
					oResponse.argument.fnLoadComplete();
				},
		
				//our handlers for the XHR response will need the same
				//argument information we got to loadNodeData, so
				//we'll pass those along:
				argument: {
					"node": node,
					"fnLoadComplete": fnLoadComplete
				},
		
				//timeout -- if more than 7 seconds go by, we'll abort
				//the transaction and assume there are no children:
				timeout: 7000
			};
		
			//With our callback object ready, it's now time to
			//make our XHR call using Connection Manager's
			//asyncRequest method:
			YAHOO.util.Connect.asyncRequest('GET', moduleQuery, callback);
    }

</script>
		</div>
	</body>
</html>