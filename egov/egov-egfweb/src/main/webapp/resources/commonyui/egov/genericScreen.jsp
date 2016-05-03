<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" import="org.apache.log4j.Logger,org.egov.infstr.utils.EGovConfig,java.util.Enumeration" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">
<% 
	String xmlConfigName = request.getParameter("xmlconfigname");
	String categoryName  = request.getParameter("categoryname");
%>

<html>
   <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel=stylesheet href="<%=request.getContextPath() +"/css/egov.css" %>" type="text/css">
	<link rel=stylesheet href="<%=request.getContextPath() +"/commonyui/build/treeview/assets/tree.css" %>" type="text/css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/commonyui/build/assets/skins/sam/datatable.css" %>" type="text/css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/build/button/assets/button.css" %>" type="text/css">

	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/yahoo/yahoo.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/dom/dom.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/event/event.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/element/element-beta.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/datasource/datasource-beta.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/datatable/datatable-beta.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/button/button-beta.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/treeview/treeview.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></script>
	<title>Search screen</title>
	<style>
	.yui-skin-sam, thead th, thead th.locked {
		border-right: 1px solid silver;
		position:relative;
  		cursor: default; 
  		z-index:0;
  	}
	</style>
	<script>
		var parameterMap="";
       /* Variables used to display the data */
       var id="", name="", narration ="" ;
       var checkid, checkname, checknarration ="" ;
<%
		Logger logger = Logger.getLogger(genericScreen_jsp.class);
		String CONST_TREEVIEW = "treeView";
		String CONST_LISTVIEW = "listView";
		String queryParamName=null;
		String queryParamValue="";
		Map  paramMap= new HashMap();
		String filterText = request.getParameter("filterText");
        if(filterText != null) {
			filterText=("'"+filterText.trim().toUpperCase()+"%25'");
        } else {
        	filterText = ("'"+"%25'");
		}
        Enumeration enumParamNames=request.getParameterNames();
		while(enumParamNames.hasMoreElements()) {
			queryParamName=(String)enumParamNames.nextElement();
			logger.info("queryParamName ------>"+queryParamName);
			if(!(queryParamName.equalsIgnoreCase("xmlconfigname") || queryParamName.equalsIgnoreCase("screenname") || queryParamName.equalsIgnoreCase("filterText") || queryParamName.equalsIgnoreCase("categoryname"))) {
				if(request.getParameter(queryParamName)!=null ) {
					queryParamValue=(String)request.getParameter(queryParamName);
					paramMap.put(queryParamName,queryParamValue);
				}
			}
		}

      	//reading displayType from config file
       	String displayType=null;
		displayType = EGovConfig.getProperty(xmlConfigName,"displayType","",categoryName);
     	//check if displaytype is null. If so, default to tree to support earlier versions
       	if (displayType == null || displayType == "") {
          displayType = CONST_TREEVIEW;
       	}
       	logger.info("displayType---------->"+displayType);
%>
       /* Namespace used in the other function's to call treeview related things  */
       	YAHOO.namespace("genericscreen");
		YAHOO.genericscreen.generictree = function() {
			var tree, currentIconMode;
	        function loadNodeData(node, fnLoadComplete) {
				var nodedata = node.data.split("~");
           		parameterMap="";
           		parameterMap=parameterMap+"parentId="+nodedata[1];
				doAjaxStuff("<%=xmlConfigName%>","<%=categoryName%>","intermediatequery",parameterMap);
            	if(name != "") {
            		for(var b = 0; b < name.length; b++) {
						var searchnode = new YAHOO.widget.TextNode(name[b] +"~"+ trimAll(id[b])+"~"+narration[b], node, false);
                	    var hasChildNode = "<%= EGovConfig.getProperty(xmlConfigName,"childquery","",categoryName) %>";
                	    if(hasChildNode != "") {
							searchnode.onLabelClick = onLabelClick;
							searchnode.setDynamicLoad(loadNodeData, currentIconMode);
						} else {
							/* If the node is in the last level, we need to paint the values to the table */
                        	var nodedata =   searchnode.data.split("~");
                        	parameterMap="parentId="+nodedata[1];
                        	doAjaxStuffCheck("<%=xmlConfigName%>","<%=categoryName%>","intermediatequery",parameterMap);
                        	if(checkname == "") {
								/* The last level nodes need to be shown on the tree as well as on the table. The following code does the painting the values to the table */
								addRow();
                            	var tableObj = document.getElementById("DataTable1");
                            	document.getElementsByName("attributeId")[tableObj.rows.length - 2].innerHTML=trimAll(id[b]);
                            	document.getElementsByName("attributeName")[tableObj.rows.length - 2].innerHTML=name[b];
                            	document.getElementsByName("attributeDesc")[tableObj.rows.length - 2].innerHTML=narration[b];
                            	searchnode.onLabelClick = sendDataToCallingWindow;

							} else {
			                	searchnode.onLabelClick = onLabelClick;
                            	searchnode.setDynamicLoad(loadNodeData, currentIconMode);
							}
						}
					}
				} else {
					parameterMap="parentId="+nodedata[1];
					doAjaxStuff("<%=xmlConfigName%>","<%=categoryName%>","childquery",parameterMap);
        			for(var b = 0; b < name.length; b++) {
						if(name[b] != "") {
							var childnode = new YAHOO.widget.TextNode(name[b] +"~"+ trimAll(id[b])+"~"+narration[b], node, false);
							childnode.onLabelClick = sendDataToCallingWindow;
						}
					}
    				/* This function deletes the existing rows before painting the new values */
    				deleteRow();
					/* The child nodes need to be shown on the tree as well as on the table. The following code does the painting the values to the table */
					for(var c = 0; c < name.length; c++) {
						addRow();
        				var tableObj = document.getElementById("DataTable1");
        				document.getElementsByName("attributeId")[tableObj.rows.length - 2].innerHTML=trimAll(id[c]);
        				document.getElementsByName("attributeName")[tableObj.rows.length - 2].innerHTML=name[c];
        				document.getElementsByName("attributeDesc")[tableObj.rows.length - 2].innerHTML=narration[c];
					}
				}
			
			fnLoadComplete();
		}

       /* This function reloads the node (because dynamic loading is only for the first time )
          If it is not loaded everytime we cannot show the values in the table  */

       var onLabelClick = function(oNode){
			deleteRow();
            tree.removeChildren(oNode);
       }

       /* While clicking the parent row, we need to clear off the table values */
		var onLabelClickDeleteRow = function(oNode){
        	deleteRow();
            tree.removeChildren(oNode);
		}

       /* On click the child node, we send the values to the calling window */

		var sendDataToCallingWindow = function(oNode){
	   		var wind;
	   		wind=window.dialogArguments;
	   		var data = oNode.data.split("~");
	   		/* check window type is showModal dialog or window.open and assing selected vaues to corresponding window obj */
			if(wind==undefined)  {
				wind=window.opener;	//window.open obj
				window.opener.update(data);
			} else {
				wind=window.dialogArguments; //showModal window obj
				wind.idValue=trimAll(data[1]);
				wind.nameValue=data[0];
				wind.descValue=data[2];
			}
			var tableObj = document.getElementById("DataTable1");

			for(var i=2;i<tableObj.rows.length;i++){
	   			if(document.getElementsByName("attributeId")[i - 1].innerHTML == data[1]){
	   				wind.descValue = document.getElementsByName("attributeDesc")[i - 1].innerHTML;
	   				break;
				}
			}
	   		window.close();
	    }

	/* This function builds the parent tree */

	function buildTree(){
		parameterMap="text="+<%=filterText%>;
		<%
		if(paramMap!=null){
			Iterator itr = paramMap.entrySet().iterator();
			while (itr.hasNext()){
				Map.Entry pairs = (Map.Entry)itr.next();
		%>      parameterMap=parameterMap+"&<%=pairs.getKey()%>=<%=pairs.getValue()%>"
		<%
			logger.info(pairs.getKey() + " = " + pairs.getValue());
		}
	}
    	%>
		doAjaxStuff("<%=xmlConfigName%>","<%=categoryName%>","parentquery",parameterMap);
		/* menutree is the <div> element used to display the tree. */
		tree = new YAHOO.widget.TreeView("menutree");
        var root = tree.getRoot();
        for(var b = 0; b < name.length; b++){
			var parentnode = new YAHOO.widget.TextNode(name[b] +"~"+ trimAll(id[b])+"~"+narration[b], root, false);
            parentnode.onLabelClick = onLabelClickDeleteRow;
            parentnode.setDynamicLoad(loadNodeData, currentIconMode);
		}
        tree.draw();
	}
		return {
        	init: function() {
            	buildTree();
			}
		}
	} ();
       
       YAHOO.genericscreen.genericdatatable = function() {

		function fillGrid(){
        	var tableObj = document.getElementById("DataTable1");
            tableObj.style.display='none';
            var query = "<%= EGovConfig.getProperty(xmlConfigName,"parentquery","",categoryName) %>";
            var filterText="<%=filterText%>";
            var replaceFilterText = "'" +  "%25'";
            if(trimText(filterText) != "") {
				replaceFilterText = "'" + filterText.toUpperCase() + "%25'" ;
			}
            parameterMap="text="+<%=filterText%>;
            <%
			if(paramMap!=null) {
				Iterator itr = paramMap.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry pairs = (Map.Entry)itr.next();
			%>
				parameterMap=parameterMap+"&<%=pairs.getKey()%>=<%=pairs.getValue()%>";
			<%
				}
			}
			%>
            doAjaxStuff("<%=xmlConfigName%>","<%=categoryName%>","parentquery",parameterMap);
			/* storing  the ajax results in a array to create data source*/
            var dataArr = [];
			for (var i=0; i<id.length ; i++ ) {
				dataObj = {attributeId:trimAll(id[i]),attributeName:name[i],attributeDesc:narration[i]};
                dataArr.push(dataObj);
			}
			/* Defing Coulmns or fields  to create data source  */
			var myColumnDefs = [
				   {key:"attributeId",label:"id", sortable:true, resizeable:true },
				   {key:"attributeName", label:"Name",sortable:true, sortOptions:{defaultDir:YAHOO.widget.DataTable.CLASS_DESC},resizeable:true},
				   {key:"attributeDesc",label:"Description", sortable:true, resizeable:true}
			];


            //Creating data source
			var myDataSource = new YAHOO.util.DataSource(dataArr);
			myDataSource.responseType=YAHOO.util.DataSource.TYPE_JSARRAY;
			myDataSource.responseSchema = {	fields: ["attributeId","attributeName","attributeDesc"],metaFields : {totalRecords: 'totalRecords'}};

			var buildQueryString = function (state,single) {
				return "startIndex=" + state.pagination.recordOffset +
			  				             "&results=" + state.pagination.rowsPerPage;
			};
			var myPaginator = new YAHOO.widget.Paginator({
				containers         : ['paging'],
			    pageLinks          : 0,
			    rowsPerPage        : 20,
			    rowsPerPageOptions : [20,40,60,80],
			    template           : "<strong>{CurrentPageReport}</strong> {PreviousPageLink} {PageLinks} {NextPageLink} {RowsPerPageDropdown}"
			});

			var myTableConfig = {
				initialRequest         : 'startIndex=0&results=8',
			    generateRequest        : buildQueryString,
			    paginator              : myPaginator
			};
            //Creating data Table
			var singleSelectDataTable = new YAHOO.widget.DataTable("single",myColumnDefs,myDataSource,myTableConfig);

			// Subscribe to events for row selection
			singleSelectDataTable.subscribe("rowMouseoverEvent",singleSelectDataTable.onEventHighlightRow);
			singleSelectDataTable.subscribe("rowMouseoutEvent", singleSelectDataTable.onEventUnhighlightRow);
			singleSelectDataTable.subscribe("rowClickEvent",function(oArgs) {
				this.onEventSelectRow(oArgs);
				var row = oArgs.target;
			    var elTargetRow = singleSelectDataTable.getTrEl(row);
			  	if(elTargetRow) {
			  		var rec = singleSelectDataTable.getRecord(elTargetRow);
			  		var key1="attributeName";
			  		var key2="attributeId";
			  		var key3="attributeDesc";

					var wind=window.opener;
			  		var datas=new Array();

					datas[0]=rec.getData(key1);
			  		datas[1]=rec.getData(key2)
			  		datas[2]=rec.getData(key3)

			  		window.opener.update(datas);
				}
			    window.close();
			   	});

	          // Programmatically select the first row
	          singleSelectDataTable.selectRow(singleSelectDataTable.getTrEl(0));
              };
			return {
				init: function() {
					fillGrid();
				}
			}
       }();

       /* Initially this Event listener is invoked to do the initialization stuff */

       <% if (displayType.equals(CONST_TREEVIEW)) { %>
			YAHOO.util.Event.addListener(window, "load", YAHOO.genericscreen.generictree.init, YAHOO.genericscreen.generictree,true);
       <% } else { %>
			YAHOO.util.Event.addListener(window, "load", YAHOO.genericscreen.genericdatatable.init, YAHOO.genericscreen.genericdatatable,true);
       <% } %>
       
		function doAjaxStuff(xmlConfigName,queryName,qryLevel,parameterMap) {
			var link = "genericScreenAjax.jsp?xmlConfigName="+xmlConfigName+"&qryName="+queryName+"&qryLevel="+qryLevel+"&"+parameterMap;
            var request = initiateRequest();
       		request.open("GET", link, false);
       		request.send(null);
       		if (request.status == 200) {
				var response=request.responseText;
                var result = response.split("^");
       			id = result[0].split("+");
       			name = result[1].split("+");
                narration = result[2].split("+");
			}
		}

       /*function is same as above function. We use different variable names because the variables in the doAjaxStuff()
               is used in the same process where these variables are also used (to avoid name collisons)  */
       function doAjaxStuffCheck(xmlConfigName,queryName,qryLevel,parameterMap) {
			var link = "genericScreenAjax.jsp?xmlConfigName="+xmlConfigName+"&qryName="+queryName+"&qryLevel="+qryLevel+"&"+parameterMap;
            request.open("GET", link, false);
       		request.send(null);
            request.onreadystatechange = function() {
				if (request.readyState == 4) {
					if (request.status == 200) {
						var response=request.responseText;
						var result = response.split("^");
               			checkid = result[0].split("+");
               			checkname = result[1].split("+");
               			checknarration = result[2].split("+");
               		}
               }
			};
		}

       function addRow() {
			var tableObj = document.getElementById("DataTable1");
            var tbody=tableObj.tBodies[0];
            var lastRow = tableObj.rows.length;
            var rowObj = tableObj.rows[1].cloneNode(true);
            tbody.appendChild(rowObj);
       }

       function deleteRow() {
			var tableObj = document.getElementById("DataTable1");
            for(var i=2;i<tableObj.rows.length;) {
				tableObj.deleteRow(i);
			}
       }

       /* This function is used to get the current row object in a table. We can use rowIndex on this object to get the rownumber */
       function getRow(obj) {
			if(!obj)return null;
            tag = obj.nodeName.toUpperCase();
            while(tag != 'BODY') {
               if (tag == 'TR') 
					return obj;
				obj=obj.parentNode;
				tag = obj.nodeName.toUpperCase();
			}
            return null;
       }

       /* This function stores the value to the variables of the calling window and closes the window  */
      function myclose(obj)	{
		var wind;
		var data = new Array();
		wind=window.dialogArguments;
		var rowNumber=getRow(obj).rowIndex;

		if(wind==undefined) {
			wind=window.opener;
			data[0]=document.getElementsByName("attributeName")[rowNumber - 1].innerHTML;
			data[1]=document.getElementsByName("attributeId")[rowNumber - 1].innerHTML;
			data[2]=document.getElementsByName("attributeDesc")[rowNumber - 1].innerHTML;

			window.opener.update(data);
		} else {
			wind=window.dialogArguments;
			wind.idValue=document.getElementsByName("attributeId")[rowNumber - 1].innerHTML;
			wind.nameValue=document.getElementsByName("attributeName")[rowNumber - 1].innerHTML;
			wind.descValue=document.getElementsByName("attributeDesc")[rowNumber - 1].innerHTML;
		}
		window.close();
	}

   </script>

	</head>
   <body class="bodyStyle" >
      		 <table align="center" id="table2">
           		<tr>
              		 <td>
              		 <div id="topStyle"><div id="bottomStyle"><div id="tabletextStyle">
                  		 <table align="center" id="mainTable" name="mainTable" class="smallTableStyle">
                      		 <tr>
                      	 	        <tr><td colspan=4>&nbsp;</td></tr>
                             		 <td  class="tableheader" colspan="4" align="center"><span id="screenName"> Search screen - <%= EGovConfig.getProperty(xmlConfigName,"headername","",categoryName) %> </span></td>
                      		 </tr>
                   			 <tr>
                    		   <td >
                 				  <table align="center">

                     				  <tr><td colspan=4 align="center">&nbsp;</td></tr>
                       				  <tr><td><div id="menutree" align="left"></div></td></tr>
                     				  <tr><td colspan=4>&nbsp;</td></tr>
                				  </table>

                				  <div id="paging" align="center"></div>
                 				  <div class="yui-skin-sam" id="single" align="center"></div>
	
	               				  <table id="DataTable1" name="DataTable1" align="center">
                                     	  <tr class="thStlyle">
                                            	   <td class="thStlyle"><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= EGovConfig.getProperty(xmlConfigName,"headername","",categoryName) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>
                                            	   <td class="thStlyle"><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Description&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>
                                      	 </tr>
                                      	 <tr id=detailsRow name=detailsRow>
                                               <td class="tdStlyle"><A style="text-decoration: none;" onClick="myclose(this)" href="#"><div id="attributeName" name="attributeName"></div></td>
                                               <td class="tdStlyle"><A style="text-decoration: none;" onClick="myclose(this)" href="#"><div id="attributeDesc" name="attributeDesc"></div></td>
                                               <td style="visibility:hidden"><div id="attributeId" name="attributeId"></div></td>
                                      	 </tr>

                 				  </table>


                				    <table align="center">
                      				 <tr><td colspan=4>&nbsp;</td></tr>
                      				 <tr> <td colspan="4"><input type="button" class="button" onclick="window.close();"value="Close" /></td></tr>
              					  </table>
                  				</td>
                  		    </tr>
                     </table>
              		</div></div></div>
               </td>
           </tr>
       </table>
   </body>
</html>
