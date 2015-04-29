#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ page language="java" import="org.egov.infstr.utils.EGovConfig,
			 java.util.*,
			 org.apache.log4j.Logger,
			org.json.JSONObject,
			 org.json.JSONArray" %>

<%

try {

	String xmlConfigName = request.getParameter("xmlconfigname");
	String categoryName = (String)request.getParameter("categoryname");
	String assetCatType =(String) request.getParameter("catTypeId");
	
     
	
	
	Map  paramMap= new HashMap();
	Enumeration enumParamNames=request.getParameterNames();

	while(enumParamNames.hasMoreElements()) {
		String queryParamName = (String)enumParamNames.nextElement();
		
		if(! (queryParamName.equalsIgnoreCase("xmlconfigname") || queryParamName.equalsIgnoreCase("categoryname"))) {		
			
			if(request.getParameter(queryParamName)!=null ) {
				String queryParamValue = (String)request.getParameter(queryParamName);
				paramMap.put(queryParamName,queryParamValue);
			}

		}

	}
%>

<script>


function trimAll(strValue ) {
      var objRegExp = /^(\s*)$/;
      // alert("strValue"+strValue);

      //check for all spaces
      if(objRegExp.test(strValue))
      {
         strValue = strValue.replace(objRegExp, '');
        // alert("strValue-------"+strValue);
         if( strValue.length == 0)
            return strValue;
      }

      //check for leading & trailing spaces
      objRegExp = /^(\s*)([\W\w]*)(\b\s*$)/;
      if(objRegExp.test(strValue))
      {
         //remove leading and trailing whitespace characters
         strValue = strValue.replace(objRegExp, '$2');
      }
      return strValue;
 }



/* Variables used to display the data */
var id, code, name ="" ;
var dataArr = [];
name =[];id=[];
var checkid, checkcode, checkname ="" ;

<% if (xmlConfigName != null &&  categoryName !=null &&  xmlConfigName.trim().length() > 0 && categoryName.trim().length() > 0) { %>

	   YAHOO.namespace("genericscreen");
	   var tree, currentIconMode;
       function loadNodeData(node, fnLoadComplete)
       {
	       var query = "<%= EGovConfig.getProperty(xmlConfigName,"intermediatequery","",categoryName) %>";
	       var nodedata = node.data.split("~");

              	parameterMap="";
		        parameterMap=parameterMap+"parentId="+nodedata[1].trim();

               /* doAjaxStuff() function calls the genericScreenAjax.jsp to perform query operations and gets the query output  */

               doAjaxStuff("<%=xmlConfigName%>","<%=categoryName%>","intermediatequery",parameterMap);

               /* If the query has some output (name has some value) then the node has some sub-nodes in the same table. These nodes are search nodes */

               if(name != "")
               {
                       for(var b = 0; b < name.length; b++)
                       {
                               var searchnode = new YAHOO.widget.TextNode(code[b] +"~"+ trimAll(id[b])+"~"+name[b], node, false);

                               var hasChildNode = "<%= EGovConfig.getProperty(xmlConfigName,"childquery","",categoryName) %>";
                               if(hasChildNode != "")
                               {
                                       searchnode.onLabelClick = onLabelClick;
                                       searchnode.setDynamicLoad(loadNodeData, currentIconMode);
                               }
                               else
                               {
                                       /* If the node is in the last level, we need to paint the values to the table */

                                       var query = "<%= EGovConfig.getProperty(xmlConfigName,"intermediatequery","",categoryName) %>";

                                       var nodedata =   searchnode.data.split("~");
                                       parameterMap="parentId="+nodedata[1].trim();

                                       doAjaxStuffCheck("<%=xmlConfigName%>","<%=categoryName%>","intermediatequery",parameterMap);


                                       if(checkname == "")
                                       {
                                             /* The last level nodes need to be shown on the tree as well as on the table. The following code does the painting the values to the table */

                                               //addRow();
                                             //  var tableObj = document.getElementById("DataTable1");
                                              // document.getElementsByName("attributeId")[tableObj.rows.length - 2].innerHTML=trimAll(id[b]);
                                              // document.getElementsByName("attributeName")[tableObj.rows.length - 2].innerHTML=name[b];
                                              //sendDataToCallingWindow;
                                            //  searchnode.onLabelClick = onLabelClick;
						                       searchnode.onLabelClick = sendDataToCallingWindow;

                                       }
                                       else
                                       {
                                     
						//alert("bbbbbbbbbbbb");
						searchnode.onLabelClick = sendDataToCallingWindow;
						searchnode.setDynamicLoad(loadNodeData, currentIconMode);
                                              // searchnode.onLabelClick = onLabelClick;
                                              // searchnode.setDynamicLoad(loadNodeData, currentIconMode);
                                       }

                               }
                       }

               }

               /* Else we need the fetch the last level nodes (child nodes) from the other table */

               else
               {
                       var query = "<%= EGovConfig.getProperty(xmlConfigName,"childquery","",categoryName) %>";

                       if(query != "")
                       {
                               parameterMap="parentId="+nodedata[1];
                              

                               /* doAjaxStuff() function calls the genericScreenAjax.jsp to perform query operations and gets the query output  */

                               doAjaxStuff("<%=xmlConfigName%>","<%=categoryName%>","childquery",parameterMap);

                               for(var b = 0; b < code.length; b++)
                               {
                                       if(code[b] != "")
                                       {
                                               var childnode = new YAHOO.widget.TextNode(code[b] +"~"+ trimAll(id[b])+"~"+name[b], node, false);
                                               childnode.onLabelClick = sendDataToCallingWindow;
                                       }
                               }


                               /* This function deletes the existing rows before painting the new values */

                             //  deleteRow();


                               /* The child nodes need to be shown on the tree as well as on the table. The following code does the painting the values to the table */

                               for(var c = 0; c < code.length; c++)
                               {
                                     
                                       //addRow();
                                      // var tableObj = document.getElementById("DataTable1");
                                      // document.getElementsByName("attributeId")[tableObj.rows.length - 2].innerHTML=trimAll(id[c]);
                                      // document.getElementsByName("attributeName")[tableObj.rows.length - 2].innerHTML=code[c];
                                      // document.getElementsByName("attributeDesc")[tableObj.rows.length - 2].innerHTML=name[c];
                               }
                       }

               }

               fnLoadComplete();
       }

 
  /* This function reloads the node (because dynamic loading is only for the first time )
	If it is not loaded everytime we cannot show the values in the table  */

	var onTreeNodeClick = function(oNode)
	{
                
	       setTreeNodeValueSelected(oNode.data.nodeId, oNode.data.label);
	    // tree.removeChildren(oNode);

	}

	var onCollapse = function(oNode)
	{

	       setTreeNodeValueUnSelected(oNode.data.nodeId);
	    // tree.removeChildren(oNode);

	}

	YAHOO.genericscreen.generictree = function() {
	    return {
		    init: function() {
		     	//buildTree();
		     	
		     	 if($F('assettype') == '')
	     			buildMyTree('',"AssetCategoryName");
	  	 		else	
			     	buildMyTree($F('assettype'),"AssetCategoryParent");
			}
    	}
	}();

YAHOO.util.Event.addListener(window, "load", YAHOO.genericscreen.generictree.init, YAHOO.genericscreen.generictree,true)

<% } %>

/* This function builds the parent tree */

	function buildMyTree(assetCatTypeVal,categoryNameVal)
    {
    	//alert(assetCatTypeVal+' / '+categoryNameVal);
	   	parameterMap="assetCatType="+assetCatTypeVal;
	   /* doAjaxStuff() function calls the genericScreenAjax.jsp to perform query operations and gets the query output  */

		doAjaxStuff("<%=xmlConfigName%>",categoryNameVal,"parentquery",parameterMap);

	     /* egovtree is the <div> element used to display the tree. */
	     tree = new YAHOO.widget.TreeView("egovtree");
	     var root = tree.getRoot();

	     for(var b = 0; b < name.length; b++)
	     {
	     	var parentnode = new YAHOO.widget.TextNode(code[b]+"~"+id[b]+"~"+name[b], root, false);
			parentnode.onLabelClick = onLabelClickDeleteRow;
			parentnode.onLabelClick = sendDataToCallingWindow;
			
			// var nodeObj = { label: code[b], nodeId:id[b], description: name[b] } ;
			// var parentnode = new YAHOO.widget.TextNode(nodeObj, root, false);
		 	//   var parentnode = new YAHOO.widget.MenuNode(nodeObj, root, false);
		  	// parentnode.onTreeNodeClick = onTreeNodeClick;
		   	parentnode.setDynamicLoad(loadNodeData, currentIconMode);
	     }
	    tree.subscribe("expand", onTreeNodeClick);

   		tree.subscribe("collapse", onCollapse);

		tree.draw();
    }
    

	   function doAjaxStuff(xmlConfigName,queryName,qryLevel,parameterMap)
       {

               var link = encodeURI("${pageContext.request.contextPath}/commonyui/egov/genericScreenAjax.jsp?xmlConfigName="+xmlConfigName+"&qryName="+queryName+"&qryLevel="+qryLevel+"&"+parameterMap);
               var request = initiateRequest();
               request.open("GET", link, false);
               request.send(null);
              
               if (request.readyState == 4) 
               {
		          if (request.status == 200)
		          {
		          	var response=request.responseText;
		
					//alert("111111111111111"+response); 
		          	var result = response.split("^");
		
		         	id = result[0].split("+");
		        	// alert("id--"+id);
		         	code = result[1].split("+");
		          	name = result[2].split("+"); 

               		}
               }
       }

/* doAjaxStuffCheck() function is same as above function. We use different variable names because the variables in the doAjaxStuff()
		is used in the same process where these variables are also used (to avoid name collisons)  */

	function doAjaxStuffCheck(xmlConfigName,queryName,qryLevel,parameterMap)
       {

		  var link = encodeURI("${pageContext.request.contextPath}/commonyui/egov/genericScreenAjax.jsp?xmlConfigName="+xmlConfigName+"&qryName="+queryName+"&qryLevel="+qryLevel+"&"+parameterMap);
        //var link = "genericScreenAjax.jsp?xmlConfigName="+xmlConfigName+"&qryName="+queryName+"&qryLevel="+qryLevel+"&"+parameterMap;
		var request = initiateRequest();
		request.onreadystatechange = function()
		{
		if (request.readyState == 4)
		{
		if (request.status == 200)
		{
		var response=request.responseText;
		var result = response.split("^");
		checkid = result[0].split("+");
		checkname = result[1].split("+");
		checknarration = result[2].split("+");

		}
		}
		};
		request.open("GET", link, false);
		request.send(null);
	}

function setTreeNodeValueSelected(idVal,labelVal)
{
	
		document.assetViewForm.parentId.value = idVal.trim();
		document.assetViewForm.category.value = labelVal;
	
	

}
function setTreeNodeValueUnSelected(val)
{
	document.assetViewForm.parentId.value = "";
		document.assetViewForm.categoryName.value ="";
}
var onLabelClickDeleteRow = function(oNode)
	    {
		//alert("99999999999999");
		//deleteRow();
		tree.removeChildren(oNode);
	    }

/* On click the child node, we send the values to the calling window */

	    var sendDataToCallingWindow = function(oNode)
	    {
	  
	    var mozillaFirefox=document.getElementById&&!document.all;
		//alert(mozillaFirefox);
		if(mozillaFirefox){
		
			var wind=window.opener;
			
			var data = oNode.data.split("~");
			document.assetViewForm.parentId.value = data[1].trim();
		  document.assetViewForm.category.value =data[2];
			wind.idValue=data[1];
			wind.nameValue=data[0];
			wind.descValue=data[2];
			//wind.assignValues(wind.document.forms[0].ModuleTreeMenu);
		}
		else{
			var wind=window.dialogArguments;
			var data = oNode.data.split("~");
			document.assetViewForm.parentId.value = data[1].trim();
		  document.assetViewForm.category.value =data[2];
			wind.idValue=trimAll(data[1]);
			wind.nameValue=data[0];
			wind.descValue=data[2];
			
		}
	  	//document.assetViewForm.parentId.value = id;
		//document.assetViewForm.category.value =name;
			
		
	    }
</script>

<%
}
catch(Exception e){
	e.printStackTrace();
	throw e;
}

%>
