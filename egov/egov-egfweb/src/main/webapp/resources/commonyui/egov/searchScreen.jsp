<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" import="org.apache.log4j.Logger , org.egov.infstr.utils.EGovConfig,java.util.Enumeration" %>
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
   <%!


   final String LABELS="labels";
   final String CATEGORIES="categories";
    String[] labelArr;//to get Radio Button labels
    String[] qryArr;


    %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=request.getContextPath() +"/css/egov.css" %>" type="text/css">

	<link rel="stylesheet" href="<%=request.getContextPath() +"/commonyui/build/treeview/assets/tree.css" %>" type="text/css">

	<script type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></script>

	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/yahoo/yahoo.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/event/event.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/treeview/treeview.js"%>"></script>

        <title>Search screen</title>
    </head>


    	   <script>

    	   <%
    	   		Logger logger = Logger.getLogger(searchScreen_jsp.class);

				String xmlConfigName = request.getParameter("xmlconfigname");
				String screenName = request.getParameter("screenname");
				String filterText=request.getParameter("filterText");
				if(filterText==null)
				filterText="";
				
				String queryParamName=null;
				String queryParamValue="";
				Enumeration paramEnum=request.getParameterNames();
				String queryParams="";
				
				while(paramEnum.hasMoreElements())
				{
					queryParamName=(String)paramEnum.nextElement(); 
					//gettting only queryParameters selecting/entering from prevoius page
					if(!(queryParamName.equalsIgnoreCase("xmlconfigname") || queryParamName.equalsIgnoreCase("screenname") || queryParamName.equalsIgnoreCase("filterText")))					
					{
						if(request.getParameter(queryParamName)!=null )
						{
						queryParamValue=(String)request.getParameter(queryParamName);
						   queryParams=(queryParams+"&"+queryParamName+"="+queryParamValue);
							
							
						}						
					}							   
		   		}
				if(screenName!=null)
				{
				labelArr=EGovConfig.getProperty(xmlConfigName,LABELS,"",("screenType."+screenName)).split(",");
				qryArr=  EGovConfig.getProperty(xmlConfigName,CATEGORIES,"",("screenType."+screenName)).split(",");				
				}


    	%>

var idValue = "";
var nameValue = "";
var descValue="";
var categoryValue="";
var queryParams="<%=queryParams%>";
var choosen="";
    function  openWindow()
     	{

     		var xmlconfigname="<%=xmlConfigName%>";
     		//var len=document.all.radiobutton1.length;
     		var len=document.getElementsByName('radiobutton1').length;     		

			for(i=0;i<len;i++)
			{

				if (document.getElementsByName('radiobutton1')[i].checked) {
				 choosen=document.getElementsByName('radiobutton1')[i].value;

   			 	}
   			 	
     			 }
     			 
     			 <%
     			 if(qryArr!=null && labelArr!=null)
     			 {
     				 for(int i=0;i<qryArr.length;i++)
				{
			%>
			
			if(choosen=="<%=labelArr[i]%>")
			{
			choosen="<%=qryArr[i]%>";
			}
			
			<%
				}
			}
     			 %>
     		var filtertext=document.getElementById('search').value;

		//frame string from enum instead of map and pass as request param
     		

     		window.location.href="<%=request.getContextPath()+"/commonyui/egov/genericScreen.jsp"%>"+"?xmlconfigname="+xmlconfigname+"&categoryname="+choosen+"&filterText="+filtertext +queryParams;

           }


           function checkEnterKey(e)
		   {

		     		var e = window.event;
		            
		                  if(e==undefined || e.keyCode ==0 || e.keyCode == 13 )
		                  {
									
		                          return false;
		                  }
		                  else{
		                  return true;
		                  }             
       		}

       		function populateFilterText()
       		{
       		document.getElementById('search').value="<%=filterText%>";
       		}


    </script>


        <body onload="populateFilterText()">
        <form name="searchScrnForm" onsubmit="return checkEnterKey(event);">
         <!-- Adding search screen-->


	<table  id="srchTable"  align="center"  class="smallTableStyle"  >
	   <tr >

		<td align="left" width="50%">Name:
		</td>
		<td align="left" width="50%">Search In:</td>
	   </tr>
	   <tr>
		<td align="left" width="50%">
		<input type="text" id="search" >
		</td>
		<td align="left" width="50%">
		 <% if(screenName!=null)
	   {

		   for(int i=0;i<labelArr.length;i++)
		   {
			   if(i==0){

	   %>

		<input id="radiobutton1" name="radiobutton1" tabindex="1" type="radio"  checked value="<%=labelArr[i]%>">
		<%=labelArr[i]%><br/>

		<%
		 		}
	 			else
	 			{
	 %>

		<input id="radiobutton1" name="radiobutton1" tabindex="1" type="radio"   value="<%=labelArr[i]%>">
		<%=labelArr[i]%><br/>
	<%
	 		}

	 	}
	}

		 %>
		</td>
	  </tr>
	  <tr>
		<td colspan="2" align="center" valign="center" class="normaltext">
			<input type="button"   id ="srcButton" value="Search" tabindex="1" onClick="openWindow();">
		</td>
	 </tr>
    </table>
            </form>
	       </body>
	   </html>
