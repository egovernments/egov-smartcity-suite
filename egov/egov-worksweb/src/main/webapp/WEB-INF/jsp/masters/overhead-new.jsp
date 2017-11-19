<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>

<style type="text/css">
.yui-dt table{
  width:100%;
}
.yui-dt-col-Add{
  width:5%;
}
.yui-dt-col-Delete{
  width:5%;
}

</style>
<html>  
<head>  
    <s:if test="%{not model.id}">
    <title><s:text name="title.overhead.add" /></title>
    </s:if>
    <s:else>
    <title><s:text name="title.overhead.edit" /></title>
    </s:else>  
</head>  
	<body> 
	 <script>
	 function enableFields()
	 {
 		<s:iterator id="overheadRateIterator" value="model.overheadRates" status="rate_row_status">
		var record = overheadRateDataTable.getRecord(parseInt('<s:property value="#rate_row_status.index"/>'));
		
		var column = overheadRateDataTable.getColumn('startDate');  
        dom.get(column.getKey()+record.getId()).disabled = false;
      
     			var column = overheadRateDataTable.getColumn('endDate');  
        dom.get(column.getKey()+record.getId()).disabled = false;
	
		</s:iterator>  
		validateOverheadFormAndSubmit();
	 }
	</script> 
	
	    <div class="new-page-header">
			Create Overhead
		</div>
	
		<s:if test="%{hasErrors()}">
        <div class="alert alert-danger">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    	</s:if>
    	
	    <s:if test="%{hasActionMessages()}">
	        <div class="messagestyle">
	        	<s:actionmessage theme="simple"/>
	        </div>
	    </s:if>
   
		<s:form action="overhead" theme="simple" name="overhead" 
			cssClass="form-horizontal form-groups-bordered">  
		<s:token/> 
		 <div class="alert alert-danger" id="overheads_error" style="display:none;"></div>
		<s:push value="model">
		    <!-- <s:hidden  name="model.id" /> -->
			<s:hidden name="id" />
		    <%@ include file='overhead-form.jsp'%>
		    
		    
		    <div class="row">
				<div class="col-xs-12 text-center buttonholdersearch">
					<input type="button" class="btn btn-primary" value="Save" id="saveButton" name="button"  onclick="enableFields();" />
					 &nbsp;
					 <input type="button" name="addOverheadButton" id="buttonfinal" class="btn btn-primary" value="Add a New Overhead"
					 onclick="window.open('${pageContext.request.contextPath}/masters/overhead!newform.action','_self');" />
					  &nbsp;
					<input type="button" name="listOverheadsButton" id="listOverheadsButton" class="btn btn-primary" value="Overhead Listing" 
					onclick="window.open('${pageContext.request.contextPath}/masters/overhead.action','_self');"/>
				</div>
			</div>
			
		    
		
			</s:push>
		</s:form>  
	    
	    <script>
	     </script>
	</body>  
</html>
