<!--
	Program Name : designationHierarchy-view.jsp
	Author		: Jagadeesan M
	Created	on	: 15-04-2010
	Purpose 	: To view a Designation Hierarchy.
 -->

<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
  <head>
	  
	  <title>View Designation Hierarchy</title>
	  
	  <script type="text/javascript">
	  </script>
   
  </head>
  
  <body>
  
   	<s:form action="designationHierarchy" theme="simple" >  
   		
  		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
			  		<div class="rbcontent2">
			  			<span id="msg"  style="height:1px">
							<s:actionerror cssStyle="font-size:12px;font-weight:bold;" cssClass="mandatory"/>  
							<s:fielderror cssStyle="font-size:12px;font-weight:bold;" cssClass="mandatory"/>
							<s:actionmessage cssClass="actionmessage"/>
						</span>

						<table  width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td class="greyboxwk"  width="50%"><b><s:text name="DesignationHierarchy.ObjectType"/>&nbsp;:&nbsp;</b> </td>
								<td class="greybox2wk" width="50%"><s:text name="%{#attr.objectType.type}"/></td>
							</tr>
							<tr >
								<td class="headingwk" colspan="2">
									<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
					              	<div class="headplacer"><s:text name="DesignationHierarchy.DtlHeading"/></div>
					        	</td>
					        </tr>
						</table>
						
						<div style="height:250px;border: 1px solid #CCCCCC;overflow-x:hidden;overflow-y:auto">
							<s:if test="#attr.viewDefaultDesigHierarchy=='YES'" >
								<display:table name="designationHierarchyViewList" uid="currentRowObject" decorator="" cellpadding="0" cellspacing="0" class="its" export="false" style="background-color:#e8edf1;padding:0px;margin:10 0 5 5px;width:100%;" requestURI="">
									<display:column  property="designationHierarchy.fromDesig.designationName" title="From Designation" style="width:20%" />
									<display:column  property="designationHierarchy.toDesig.designationName" title="To Designation"  style="width:20%"/>
									<div STYLE="display:table-header-group;">			      
					  					<display:setProperty name="basic.show.header" value="true" />
					  					<display:setProperty name="paging.banner.placement" value="" />
					  					<display:setProperty name="basic.msg.empty_list">
					  						<br/><b><font color="red" >Nothing found to display</font></b>
					  					</display:setProperty>
									</div>
								</display:table>
							</s:if>
							<s:elseif test="#attr.viewDefaultDesigHierarchy=='NO'" >
								<display:table name="designationHierarchyViewList" uid="currentRowObject" decorator=""  defaultsort="1" sort="list" cellpadding="0" cellspacing="0" class="its" export="false" style="background-color:#e8edf1;padding:0px;margin:10 0 5 5px;width:100%;" requestURI="">
									<display:column  group="1" property="designationHierarchy.department.deptName" title="Department Name" style="width:20%"/>
									<display:column  property="designationHierarchy.fromDesig.designationName" title="From Designation" style="width:20%" />
									<display:column  property="designationHierarchy.toDesig.designationName" title="To Designation"  style="width:20%"/>
									<div STYLE="display:table-header-group;">			      
					  					<display:setProperty name="basic.show.header" value="true" />
					  					<display:setProperty name="paging.banner.placement" value="" />
					  					<display:setProperty name="basic.msg.empty_list">
					  						<br/><b><font color="red" >Nothing found to display</font></b>
					  					</display:setProperty>
									</div>
								</display:table>
							</s:elseif>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk">
			<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>
	</s:form>
</body>
</html>	