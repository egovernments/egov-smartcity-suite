<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ page import="org.osgeo.mapguide.*" %>
<%@ page import="org.egov.infstr.utils.database.utils.EgovDatabaseManager" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.egov.asset.*" %>
<%@ page import="org.apache.log4j.Logger"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>
<link rel=stylesheet href="../exility/global.css" type="text/css">
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../script/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
<SCRIPT LANGUAGE="javascript">

var category1;
var toDate1;

function onLoadofPage()
{


}



function display()
{


	if (!PageValidator.validateForm()){
		document.getElementById('asset_category').value='';
		return;
	   }

	document.getElementById('category').value=document.getElementById('asset_category').value;
	category1=document.getElementById('category').value;
	document.getElementById('fromBean').value = 1;
	document.mvAssetRpt.submit();
}

function buttonFlush1()
{
	window.location="rptMovableProperties.jsp";

}



</SCRIPT>
<title>Register of Movable Properties</title>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoadofPage()" >
<jsp:useBean id = "csReportBean" scope ="request" class="com.exilant.eGov.src.reports.MovableAssetBean"/>
<jsp:setProperty name ="csReportBean" property="category" />



<form name="mvAssetRpt" action = "rptMovableProperties.jsp"?scrollbars="+scrollbars1 method = "get">
<input type=hidden name="category" id="category" value="">

<table width="109%" border=0 cellpadding="3" cellspacing="0">
	<tr >
		<td colspan="6" class="rowheader" valign="center"  width="100%"><span id="screenName" class="headerwhite2">Register of Movable Properties</span></td>
	</tr>

	<tr class="row1">

			<td width="25%" align="left" valign="center" class="normaltext"><div align="left">Asset category<span class="leadon">*&nbsp&nbsp</span></div>
							</td>
			<td width="25%" align="left" valign="center" class="normaltext">
							<div align="left" >
						<SELECT  id="asset_category"  name="asset_category" class="smallfieldinput"  >
							<%
								Logger LOGGER = Logger.getLogger("rptMovableProperties.jsp");
								LOGGER.info("hi");
								String assetType=request.getParameter("asset_category");
								AssetCategoryDAO assetCategory = new AssetCategoryDAO();
								HashSet categoryList =(HashSet) assetCategory.getAllAssetCategory();
								LOGGER.info("hi");
								AssetCategoryImpl c;
								if(categoryList!=null)
								{
									Iterator myIterator = categoryList.iterator();
									while (myIterator.hasNext())
									{

										c= (AssetCategoryImpl)myIterator.next();
										String id=c.getId().toString();
									 	if(assetType!=null && assetType.equalsIgnoreCase(id))
									 	{
										%>
											<option value='<%=c.getId().toString() %>' selected><%=c.getName()%> </option>
										<%}

										else
										{
										%>
											<option value='<%=c.getId().toString() %>' ><%=c.getName()%> </option>
										<%}
									}

								}
								%>
						</SELECT>
					</div>
			</td>
			<td width="50%">
			</td>

	</tr>
	<tr class="row2">

	</tr>
	<tr class="row1">

	</tr>
	<tr class="row2" height="5"><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr class="row2">
		<td colspan="6" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right"><IMG height=18 src="../images/Button_leftside.gif" width=7></td>
			<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary onclick=display() href="#">Submit</A></td>
			<td><IMG height=18 src="../images/Button_rightside.gif" width=7></td>
			<td><IMG src="../images/spacer.gif" width=8></td>
			<td align="right"><IMG height=18 src="../images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="../images/Button_second_middle.gif"><A class=buttonsecondary onclick=buttonFlush1(); href="#">Cancel</A></td>
			<td><IMG height=18 src="../images/Button_second_rightside.gif" width=6></td>
			<td><IMG src="../images/spacer.gif" width=8></td>
			<td align="right"><IMG height=18 src="../images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="../images/Button_second_middle.gif"><A class=buttonsecondary onclick=window.close() href="#">Close</A></td>
			<td><IMG height=18 src="../images/Button_second_rightside.gif" width=6></td>
		</tr>
		</table>
		</td>
	</tr>
</table>


<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="scrollbars" id="scrollbars1" value="no">


<%
    LOGGER.info("before submit "+request.getParameter("fromBean")+" category "+request.getParameter("category"));
      //
      // request.setAttribute("asset_category",mvAsset.getImmovableAssetCategory());
       if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
       {
   	 	LOGGER.info("after submit "+request.getParameter("fromBean")+" category "+request.getParameter("category"));

   	 try{

   	   com.exilant.eGov.src.reports.MovableAsset mvAsset = new com.exilant.eGov.src.reports.MovableAsset();
   	   	request.setAttribute("links",mvAsset.getImmovableAssetRegister(csReportBean));
	    } catch(Exception e) { LOGGER.error("Error:"+"connecting to databse failed");}

%>
<div class="tbl-bill" id="tbl-container" >
 <display:table style="width:100%" name="links"  id="currentRowObject"  export="true" sort="list" pagesize = "15" class="its" >

<display:caption><div class = "alignright"><b><font size=2>FORM KMF NO 46 <br> (Rule 87(1)) </font>   </b></div>
	<div  class = "normaltext"> REGISTER OF MOVABLE PROPERTIES   </div>
	<div >
</div>
</display:caption>

	<display:column headerClass="assetLabels" style="min-width:5% nowrap=true"  property="assetId"  title="Asset identification No." />
	 <display:column  headerClass="assetLabels" property="description" style="width:25% nowrap=true"  title="Description of the Asset" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="aquisitionMode"  title="Mode of Acquisition" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="aquisitionDate"  title="Date of Acquisition/ Addition" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="workOrder"  title="Work Order / Procurment Order Reference" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="location"  title="Location(Department)" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="aquisitionCost"  title="Cost of Acquisition/ Opening written value" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="additionCost"  title="Cost of Addition if any during the year(Rs)" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="depreciation" class="textAlign" title="Depreciation for the year" />
	 <display:column   headerClass="assetLabels" style="width:5% nowrap=true" property="accDepreciation" title="Accumulated Depreciation" />
	 <display:column   headerClass="assetLabels" style="width:5% nowrap=true" property="closingWrittenValue" title="Closing written down value(Rs)" />
	 <display:column  headerClass="assetLabels" style="width:5% nowrap=true" property="disposalDate" title="Date of Disposal" />
	 <display:column   headerClass="assetLabels" style="width:5% nowrap=true" property="recptNo" title="Receipt Voucher no." />
	 <display:column   headerClass="assetLabels" style="width:5% nowrap=true" property="saleValue" title="Sale Value(Rs)" />
	 <display:column   headerClass="assetLabels" style="width:25% nowrap=true" property="particulars" title="Other Particulars,if any" />
	 <display:column   headerClass="assetLabels" style="width:5% nowrap=true"  title="Intials of the Authorised Officer" />

	 <display:setProperty name="export.pdf" value="true" />


</display:table>
</div>
<%
	}
%>
</form>

</body>
</html>
