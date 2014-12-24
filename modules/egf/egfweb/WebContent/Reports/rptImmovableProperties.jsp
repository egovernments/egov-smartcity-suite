<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ page import="org.osgeo.mapguide.*" %>

<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.egov.asset.*"%>


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
	window.location="rptImmovableProperties.jsp";

}



</SCRIPT>
<title>Register of Movable Properties</title>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoadofPage()" >
<jsp:useBean id = "csReportBean" scope ="request" class="com.exilant.eGov.src.reports.ImmovableAssetBean"/>
<jsp:setProperty name ="csReportBean" property="category" />



<form name="mvAssetRpt" action = "rptImmovableProperties.jsp"?scrollbars="+scrollbars1 method = "get">
<input type=hidden name="category" id="category" value="">

<table width="109%" border=0 cellpadding="3" cellspacing="0">
	<tr >
		<td colspan="6" class="rowheader" valign="center"  width="100%"><span id="screenName" class="headerwhite2">Register of Immovable Properties</span></td>
	</tr>

	<tr class="row1">

			<td width="25%" align="left" valign="center" class="normaltext"><div align="left">Asset category<span class="leadon">*&nbsp&nbsp</span></div>
							</td>
			<td width="25%" align="left" valign="center" class="normaltext">
							<div align="left" >
						<SELECT  id="asset_category"  name="asset_category" class="smallfieldinput"  >
							<%
								System.out.println("hi");
								String assetType=request.getParameter("asset_category");
								AssetCategoryDAO assetCategory = new AssetCategoryDAO();
								HashSet categoryList =(HashSet) assetCategory.getAllAssetCategory();
								System.out.println("hi");
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
    System.out.println("before submit "+request.getParameter("fromBean")+" category "+request.getParameter("category"));
      //
      // request.setAttribute("asset_category",mvAsset.getImmovableAssetCategory());
       if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
       {
   	 System.out.println("after submit "+request.getParameter("fromBean")+" category "+request.getParameter("category"));

   	 try{

   	   com.exilant.eGov.src.reports.ImmovableAsset mvAsset = new com.exilant.eGov.src.reports.ImmovableAsset();
   	   	request.setAttribute("links",mvAsset.getImmovableAssetRegister(csReportBean));
	    } catch(Exception e) { System.out.println("Error:"+"connecting to databse failed");}

%>
<div class="tbl-bill" id="tbl-container" >
 <display:table style="width:100%" name="links"  id="currentRowObject"  export="true" sort="list" pagesize = "15" class="its" >

<display:caption><div class = "alignright"><b><font size=2>FORM KMF NO 46 <br> (Rule 87(1)) </font>   </b></div>
	<div  class = "normaltext"> REGISTER OF MOVABLE PROPERTIES   </div>
	<div >
</div>
</display:caption>

	<display:column headerClass="assetLabels" style="width:5% nowrap=true"  property="assetId"  title="Asset identification No." />
	 <display:column  headerClass="assetLabels" property="description" style="width:25% nowrap=true"  title="Description of the Asset" />

	 <display:column  headerClass="assetLabels" property="dimension" style="width:25% nowrap=true"  title="Dimensions (Plinth Area/Length/Breadth etc)" />
	<display:column headerClass="assetLabels" property="reference" style="width:25% nowrap=true"  title="Reference to Land Register" />

	<display:column headerClass="assetLabels" style="width:5% nowrap=true" property="aquisitionMode"  title="Mode of Acquisition" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="aquisitionDate"  title="Date of Acquisition/ Improvement" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="workOrder"  title="Work Order/ Procurment Order Reference" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="location"  title="For what purpose used(Department)" />

	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="revenueYielding"  title="Whether Revenue Yeilding(Y/N))If yes reference to reference DCB Register" />

	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="aquisitionCost"  title="Cost of Acquisition/ Opening written value(Rs)" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="additionCost"  title="Cost of Addition if any during the year(Rs)" />
	 <display:column headerClass="assetLabels" style="width:5% nowrap=true" property="depreciation" class="textAlign" title="Depreciation for the year" />
	 <display:column  headerClass="assetLabels" style="width:5% nowrap=true" property="accDepreciation" title="Accumulated Depreciation" />
	 <display:column  headerClass="assetLabels" style="width:5% nowrap=true" property="closingWrittenValue" title="Closing written down value(Rs)" />
	 <display:column  headerClass="assetLabels" style="width:5% nowrap=true" property="disposalDate" title="Date of Disposal" />
	 <display:column  headerClass="assetLabels" style="width:5% nowrap=true" property="recptNo" title="Receipt Voucher no." />
	 <display:column  headerClass="assetLabels" style="width:5% nowrap=true" property="saleValue" title="Sale Value(Rs)" />
	 <display:column  headerClass="assetLabels" style="width:5% nowrap=true"  title="Intials of the Authorised Officer" />

	 <display:setProperty name="export.pdf" value="true" />


</display:table>
</div>
<%
	}
%>
</form>

</body>
</html>
