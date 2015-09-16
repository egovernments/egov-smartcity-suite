<!-- -------------------------------------------------------------------------------
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
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %> 
<html>
<title><s:text name="contractor.grade.list" /></title>
<body >
 
		<s:if test="%{hasErrors()}">
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
       <div id="msgsDiv" class="messagestyle">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr><td colspan="5" class="headingwk"><div class="arrowiconwk"><img src="/egi/resources/erp2/images/arrow.gif" /></div><div class="headplacer"><s:text name="contractor.grade.master" /></div></td></tr>
          <tr>
            <display:table name="contractorGradeList"  uid="currentRow"
													cellpadding="0" cellspacing="0" requestURI=""
													style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Sl.No" titleKey="column.title.SLNo"
														style="width:4%;text-align:right" >
														<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Grade" titleKey="contractor.grade.master.grade"
														style="width:20%;text-align:left" property="grade"/>
																										
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Description" titleKey="contractor.grade.master.description"
														style="width:40%;text-align:left" property="description"/>
																										
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Minimum Amount " titleKey="contractor.grade.master.minamount"
														style="width:10%;text-align:right" >
														<s:text name="contractor.format.number" >
												   	<s:param name="rate" value='%{#attr.currentRow.minAmount}' /></s:text>
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Maximum Amount" titleKey="contractor.grade.master.maxamount"
														style="width:10%;text-align:right"  >
														<s:text name="contractor.format.number" >
												   	<s:param name="rate" value='%{#attr.currentRow.maxAmount}' /></s:text>
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Edit/View" style="width:13%;text-align:left" >
														<table width="100" border="0" cellpadding="0" cellspacing="2">
															<tr>                    		
																<td width="20">
																	<a href="${pageContext.request.contextPath}/masters/contractorGrade!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																		<s:text name="sor.edit" /></a>
																</td>
																<td width="20">
																	<a href="${pageContext.request.contextPath}/masters/contractorGrade!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																		<img src='/egi/resources/erp2/images/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" />
																	</a>
																</td>
																<td width="20">&#47;
																</td>
																<td width="20">
																	<a href="${pageContext.request.contextPath}/masters/contractorGrade!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
																		<s:text name="sor.view" />
																	</a>
																</td>
																<td width="20">
																	<a href="${pageContext.request.contextPath}/masters/contractorGrade!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
																		<img src='/egi/resources/erp2/images/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" />
																	</a>
																</td>
															</tr>
														</table>
													</display:column>
												</display:table>
                      
                                </tr>
                 <tr>
                    <td>&nbsp;</td>
                </tr>
        </table>
        </div>
      <div class="rbbot2"><div></div></div>
      </div>
	  
	  
</div>
  </div>

<div class="buttonholderwk">
  <input type="submit" name="closeButton" id="closeButton" value="CLOSE" class="buttonfinal" onclick="window.close();" />
&nbsp;&nbsp;</div>

</body>

</html>

			
