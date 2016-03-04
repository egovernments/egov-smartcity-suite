<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Accountdetailtype</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.name" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.name}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.description" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.description}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.tablename" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.tablename}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.columnname" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.columnname}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.attributename" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.attributename}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.nbroflevels" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.nbroflevels}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.isactive}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.fullqualifiedname" />
						</div>
						<div class="col-sm-3 add-margin view-content">${accountdetailtype.fullQualifiedName}</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
<div class="row text-center">
	<div class="add-margin">
		<a href="javascript:void(0)" class="btn btn-default"
			onclick="self.close()">Close</a>
	</div>
</div>