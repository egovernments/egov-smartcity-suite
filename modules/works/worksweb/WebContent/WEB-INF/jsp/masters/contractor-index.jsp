<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<title><s:text name="contractor.list" /></title>
<body>
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
            <td colspan="6" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div><div class="headplacer"><s:text name="contractor.contDetails" /></div></td>
              </tr>
              <tr>
                <td width="12%" class="tablesubheadwk"><s:text name="contractor.code" /></td>
                <td width="12%" class="tablesubheadwk"><s:text name="contractor.name" /></td>
               <td width="14%" class="tablesubheadwk"><!--<s:text name="sor.edit" />/--><s:text name="sor.view" /></td>
                </tr>
              </table>
			<% 
							int count=0;
						%>
					<s:iterator  id="detailsIterator" value="contractorList"> 
						<% 
							count++;
						%>
					</s:iterator>
				<% if(count>20){ %>

				<div style=" height:350px" class="scrollerboxaddestimate">
				<%}%>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                
                <s:iterator id="rateIterator" value="contractorList" status="row_status">
                <tr>
                	<td width="12%"><s:property value="%{code}" /></td>
					<td width="13%"><s:property value="%{name}" /> </td>				
					<td width="13%">
						<table width="80" border="0" cellpadding="0" cellspacing="2">
                  		<tr>                    		
                    		<td width="20"><a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{id}'/>&mode=edit"><s:text name="sor.edit" /></a></td>
                    		 <td width="20"><a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{id}'/>&mode=edit"><img src='${pageContext.request.contextPath}/image/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" /></a></td>
                    		 <td width="20"><a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{id}'/>&mode=view"><s:text name="sor.view" /></a></td>
                    		 <td width="20"><a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{id}'/>&mode=view"><img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" /></a></td>
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
        <div class="rbbot2"><div></div></div>
      </div>
	  
</div>
  </div>

<div class="buttonholderwk">
  <input type="submit" name="closeButton" id="closeButton" value="CLOSE" class="buttonfinal" onclick="window.close();" />
&nbsp;&nbsp;</div>

</body>

</html>

			
