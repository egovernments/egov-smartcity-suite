<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<div class="alert alert-danger" id="schedulecategory_error" class="alert alert-danger" style="display: none;"></div>
<div class="panel panel-primary" data-collapsed="0" style="text-align: left">
	<div class="panel-heading">
		<div class="panel-title">
			<s:if test="%{id==null}"><s:text name="estimate.scheduleCategory.name" />
		</s:if>
		<s:elseif test="%{id!=null && mode=='edit'}"><s:text name="schedCategory.modify"/>
		</s:elseif>
		<s:else><s:text name="scheduleCategory.view.sor"/>
		</s:else>
		</div>
	</div>
	<div class="panel-body">
		<label class="col-sm-3 control-label text-right"> <s:text name="schedCategory.code" /><span class="mandatory"></span></label>
			<s:hidden name="id"	id="id" />
			<s:hidden name="mode" id="mode" />
		<div class="col-sm-3 add-margin">
			<s:textfield cssClass="form-control patternvalidation" data-pattern="alphanumericwithallspecialcharacterswithoutspace" name="code" maxlength="15"	id="code" size="40" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text name="schedCategory.description" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<s:textfield cssClass="form-control" name="description"	maxlength="150" id="description" size="40" />
		</div>
	</div>
</div>

<div class="row">
	<div class="col-sm-12 text-center buttonholdersearch">
		<s:if test="%{id==null}">
			<s:submit cssClass="btn btn-primary" value="Create" id="saveButton"
				name="button" method="save" onclick="return methodTest();" /> &nbsp;
		</s:if>
		<s:elseif test="%{id!=null && mode=='edit'}">
			<s:submit cssClass="btn btn-primary" value="Modify" id="saveButton"
				name="button" method="save" onclick="return methodTest();" /> &nbsp;
		</s:elseif>
		<s:else>
			<s:text name="scheduleCategory.view.sor" />
		</s:else>
		<input type="button" class="btn btn-default" value="Close" id="closeButton" name="button" onclick="window.close();" />
	</div>
</div>
