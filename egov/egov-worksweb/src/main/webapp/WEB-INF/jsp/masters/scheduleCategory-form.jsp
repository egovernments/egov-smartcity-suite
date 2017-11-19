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

<div class="panel panel-primary" data-collapsed="0" style="text-align: left">
	<div class="panel-heading">
		<div class="panel-title">
			<s:if test="%{id==null}"><s:text name="scheduleCategory.sor.category" />
		</s:if>
		<s:elseif test="%{id!=null && mode=='edit'}"><s:text name="scheduleCategory.modify.sor"/>
		</s:elseif>
		<s:else><s:text name="scheduleCategory.view.sor"/>
		</s:else>
		</div>
	</div>
	<div class="panel-body">
		<label class="col-sm-2 control-label text-right"> <s:text name="schedCategory.code" /></label>
			<s:hidden name="id"	id="id" />
			<s:hidden name="mode"	id="mode" />
		<div class="col-sm-3 add-margin">
			<s:textfield cssClass="form-control" name="code" maxlength="15"	id="code" size="40" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text name="schedCategory.description" /></label>
		<div class="col-sm-3 add-margin">
			<s:textfield cssClass="form-control" name="description"	maxlength="150" id="description" size="40" />
		</div>
	</div>
</div>

<s:if test="%{id==null}">
<div class="row">
	<div class="col-sm-12 text-center buttonholdersearch">
		<s:submit cssClass="btn btn-primary" value="Save" id="saveButton" name="button" method="save" onclick="methodTest();" /> &nbsp; 
		<input type="button" class="btn btn-default" value="Close" id="closeButton" name="button" onclick="window.close();" />
	</div>
</div>

<div class="row report-section"><br />
	<div  style="overflow-y:scroll; height:400px; ">
		<table align="centre" border="0" cellpadding="0" cellspacing="0" class="table table-hover">
			<thead>
				<tr>
					<th ><s:text name="schedCategory.code" /></th>
					<th ><s:text name="schedCategory.description" /></th>
					<th ><s:text name="scheduleCategory.View/Modify" /></th>
				</tr>
			</thead>
			<tbody >
				<s:iterator var="p" value="scheduleCategoryList">
					<tr>
						<td class="whitebox3wka" ><s:property value="%{code}" /></td>
						<td class="whitebox3wka"><s:property value="%{description}" /></td>
						<td class="whitebox3wka">
							<table >
								<tr>
								<td >
									<a class="buttonfinal" id="VIEW" name="button" onclick="window.open('${pageContext.request.contextPath}/masters/scheduleCategory-edit.action?id=<s:property value='%{id}'/>&mode=view','', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"/>
 									<s:text name="sor.view" /><td > / </td>
								</td>	
									<egov-authz:authorize actionName="WorksSOREditAutho">
								<td >
									<a  class="buttonfinal" id="MODIFY" name="button" onclick="window.open('${pageContext.request.contextPath}/masters/scheduleCategory-edit.action?id=<s:property value='%{id}'/>&mode=edit','', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"/>
									<s:text name="schedCategory.modify" /></td>
									</egov-authz:authorize>
								</tr>
							</table>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
</div>
</s:if>

<div class="row">
	<div class="col-sm-12 text-center buttonholdersearch">
		<s:if test="%{mode=='edit'}">
			<s:submit id="modifyButton" class="btn btn-primary" type="submit" onclick="methodTest();" value="MODIFY" name="MODIFY" />
 			<input  id="closeButton" class="btn btn-default" type="button" onclick="window.close();" name="button" value="Close" />
		</s:if>
		<s:if test="%{mode=='view'}">
			<input  id="closeButton" class="btn btn-default" type="button" onclick="window.close();" name="button" value="Close">
		</s:if>
	</div>
</div>