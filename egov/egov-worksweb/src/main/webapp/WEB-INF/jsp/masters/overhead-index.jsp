<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
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

			
