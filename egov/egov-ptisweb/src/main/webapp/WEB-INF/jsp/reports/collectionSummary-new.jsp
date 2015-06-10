<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title><s:text name="collectionsummary.pagetitle"></s:text></title>
	</head>
	<body>
	   
		<div class="formmainbox">
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
			  <tr>
			    <td>
				  <div class="headingbg">					
					Collection Summary									
				  </div>
				</td>
			  </tr>	
			  
			  <tr>
			    <td>
			    <br/>
			     <table border="0" cellspacing="0" cellpadding="0" width="100%" style="max-width:960px;margin:0 auto;">
			       <s:form name="zoneform" theme="simple" id="zoneform">
				       <tr>
				         <td class="bluebox"><s:text name="collectionsummary.zone" /> :</td>
				         <td class="bluebox">
				           <s:select list="{'Select'}" name="sbzone" id="sbzone"></s:select>
				         </td>
				       </tr>
				       
				       <tr>
				         <td class="bluebox"><s:text name="collectionsummary.fromdate" /> <span class="mandatory1">*</span> :</td>
				         <td class="bluebox">
				           <s:textfield name="fromdate" id="fromdate" />
				         </td>
				         <td class="bluebox"><s:text name="collectionsummary.todate" /> <span class="mandatory1">*</span> :</td>
				         <td class="bluebox">
				           <s:textfield name="todate" id="todate" />
				         </td>
				       </tr>
				       
				        <tr>
				         <td class="bluebox"><s:text name="collectionsummary.collectionmode" /> :</td>
				         <td class="bluebox">
				           <s:select list="{'Select'}" name="sbcollectionmode" id="sbzone"></s:select>
				         </td>
				         <td class="bluebox"><s:text name="collectionsummary.transactionmode" /> :</td>
				         <td class="bluebox">
				           <s:select list="{'Select'}" name="sbtransactionmode" id="sbzone"></s:select>
				         </td>
				       </tr>
				       
				       <tr>
				         <td class="bluebox" colspan="4" align="center">
				         
				           <div class="buttonbottom" align="center">		   
		    		
							 <input type="submit" id="btnsearch" name="btnsearch" value="Search" class="buttonsubmit" >
							 <input type="submit" id="btnclose" name="btnclose" value="Close" class="buttonsubmit normal" >
									
						   </div>
				         </td>
				       </tr>
				       
			       </s:form>
			     </table>
			     
			    </td>
			  </tr>			
			  <tr>
			  
			  <td class="bluebox" style="text-align:center;">
			    <b><s:text name="collectionsummary.notetext" /></b>
			  </td>
			  
			  </tr>
			 </table>
			
			 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom" id="floorDetails" style="max-width:960px;margin:0 auto;border:1px solid #d4d4d4;">
			    
			    <tbody>
			    
			    <tr>
			        <th class="bluebgheadtd" colspan="5" style="text-align:center;font-size:14px;"><s:text name="collectionsummary,tabletitle" /></th>
			    </tr>
			    
			    <tr>
			        <th class="bluebgheadtd" style="text-align:center;"><s:text name="collectionsummary.table.zone" /></th>
					<th class="bluebgheadtd" style="text-align:center;"><s:text name="collectionsummary.table.noreceipt" /></th>
					<th class="bluebgheadtd" style="text-align:center;"><s:text name="collectionsummary.table.taxamt" /></th>
					<th class="bluebgheadtd" style="text-align:center;"><s:text name="collectionsummary.table.penalty" /></th>
					<th class="bluebgheadtd" style="text-align:center;"><s:text name="collectionsummary.table.totamt" /></th>
			    </tr>
			    
			    <tr>
			        <td class="blueborderfortd" style="text-align:center;">NO4</td>
					<td class="blueborderfortd" style="text-align:right;">7496</td>
					<td class="blueborderfortd" style="text-align:right;">16,514,085.36</td>
					<td class="blueborderfortd" style="text-align:right;">10,000.00</td>
					<td class="blueborderfortd" style="text-align:right;">16,524,085.36</td>
			    </tr>
			    
			    <tr>
			        <td class="blueborderfortd"></td>
					<th class="bluebgheadtd" style="text-align:right;">Total</th>
					<td class="blueborderfortd" style="text-align:right;">16,514,085.36</td>
					<td class="blueborderfortd" style="text-align:right;">10,000.00</td>
					<td class="blueborderfortd" style="text-align:right;">16,524,085.36</td>
			    </tr>
			    
			    </tbody>
			 </table>
			 <br/>
			
			
		</div>
	</body>
</html>
