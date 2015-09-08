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
<title><s:text name="sor.list" /></title>
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
	
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
            <td colspan="6" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div><div class="headplacer"><s:text name="sor.scheduleOfRate" /></div></td>
              </tr>
              <tr>
                <!--  <td width="10%" class="tablesubheadwk">ID</td>  -->
                <td width="12%" class="tablesubheadwk"><s:text name="master.sor.category" /></td>
                <td width="12%" class="tablesubheadwk"><s:text name="master.sor.code" /></td>
                <td width="49%" class="tablesubheadwk"><s:text name="master.sor.description" /></td>
                <td width="10%" class="tablesubheadwk"><s:text name="master.sor.uom" /></td>
                <td width="14%" class="tablesubheadwk"><s:text name="sor.edit" />/<s:text name="sor.view" /></td>
                </tr>
              </table>
			<% 
							int count=0;
						%>
					<s:iterator  id="rateIterator" value="scheduleOfRateList"> 
						<% 
							count++;
						%>
					</s:iterator>
				<% if(count>20){ %>

				<div style=" height:350px" class="scrollerboxaddestimate">
				<%}%>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                
                <s:iterator id="rateIterator" value="scheduleOfRateList" status="row_status">
                <tr>
               		<!-- <td width="10%"><s:property value="%{id}" /> </td> -->	
                	<td width="12%"><s:property value="%{scheduleCategory.code}" /></td>
					<td width="13%"><s:property value="%{code}" /> </td>	   
					<td width="51%"><s:property value="%{description}" /></td>
					<td width="9%"><s:property value="%{uom.uom}" /></td>					
					<td width="13%">
						<table width="80" border="0" cellpadding="0" cellspacing="2">
                  		<tr>                    		
                    		<td width="20"><a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{id}'/>&mode=edit"><s:text name="sor.edit" /></a></td>
                    		<td width="20"><a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{id}'/>&mode=edit"><img src='${pageContext.request.contextPath}/image/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" />&nbsp;/</a></td>
                    		<td width="20"><a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{id}'/>&mode=view"><s:text name="sor.view" /></a></td>
                    		<td width="20"><a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{id}'/>&mode=view"><img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" /></a></td>
                    	</tr>
                		</table>
                	</td>

					</tr>
				</s:iterator>
                <td colspan="6" class="shadowwk"></td>
            </table> 
			<% if(count>20){ %>
			</div>
			<%}%>
			</td>
          </tr>
        </table>
      </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>

<div class="buttonholderwk">
  <input type="submit" name="closeButton" id="closeButton" value="CLOSE" class="buttonfinal" onclick="window.close();" />
&nbsp;&nbsp;</div>

</body>

</html>

			
