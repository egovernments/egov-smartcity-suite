<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Account Entity</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.name" /><span class="mandatory"></label>
						<div class="col-sm-3 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.description" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="description"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required" />
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isactive" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isactive" />
							<form:errors path="isactive" cssClass="error-msg" />
						</div>
						<label />
						<div class="col-sm-3 add-margin"></div>
					</div>
					<input type="hidden" name="accountdetailtype"
						value="${accountdetailtype.id}" />