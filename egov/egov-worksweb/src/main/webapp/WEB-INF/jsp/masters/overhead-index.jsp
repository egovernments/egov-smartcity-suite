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
<title>Overhead List</title>
<body >
 
		<s:if test="%{hasErrors()}">
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	
	  <div class="rbcontent2"><!--<div class="datewk"><span class="bold">Today</span> <egov:now/></div> -->

	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="6" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div><div class="headplacer">Overhead Rate</div></td>
              </tr>
              <tr>
                <!--  <td width="10%" class="tablesubheadwk">ID</td>  -->
                <td width="17%" class="tablesubheadwk">Name</td>
                <td width="20%" class="tablesubheadwk">Account</td>
                <td width="27%" class="tablesubheadwk">Description</td>
                <td width="12%" class="tablesubheadwk">Expenditure Type</td>
                <td width="14%" class="tablesubheadwk">Edit</td>
                </tr>
              <tr>
                
                
                <s:iterator id="overheadIterator" value="overheadList" status="row_status">
				<tr>	
					<!-- <td width="10%"><s:property value="%{id}" /> </td> -->
					<td width="17%"><s:property value="%{name}" /> </td>	   
					<td width="20%"><s:property value="%{account.name}" /></td>
					<td width="27%"><s:property value="%{description}" /></td>
					<td width="12%"><s:property value="%{expenditureType.value}" /></td>
					<td width="14%"><table width="60" border="0" cellpadding="0" cellspacing="2">
                  		<tr>
                    		<td width="18"><a href="#"><img src='${pageContext.request.contextPath}/image/book_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" /></a></td>
                    		<td width="36"><a href='${pageContext.request.contextPath}/masters/overhead!edit.action?id=<s:property value='%{id}'/>'>Edit</a></td>
                    	</tr>
                </table></td>
			
				</tr>
				</s:iterator>
                <td colspan="6" class="shadowwk"></td>
              </tr>
            </table></td>
          </tr>
        </table>
      </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>
<div class="buttonholderwk">
  <input type="submit" name="closeButton" id="closeButton" value="Close Window" class="buttonfinal" onclick="window.close();" />
&nbsp;&nbsp;</div>

</body>

</html>

			
