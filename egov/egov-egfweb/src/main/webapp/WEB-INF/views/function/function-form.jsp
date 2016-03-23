<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Function</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.name" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="100" required="required" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.code" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="code"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required" />
							<form:errors path="code" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.llevel" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="llevel"
								class="form-control text-right patternvalidation"
								data-pattern="number" />
							<form:errors path="llevel" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isactive" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isActive" />
							<form:errors path="isActive" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.parentType" /></label>
						<div class="col-sm-3 add-margin">
							<form:select path="parentId" id="parentId"
								
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${functions}" itemValue="id"
									itemLabel="name" />
									<lable></lable>
									<div></div>
							</form:select>
							<form:errors path="parentId" cssClass="error-msg" />
						</div>
						</div>
						<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isnotleaf" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isNotLeaf" />
							<form:errors path="isNotLeaf" cssClass="error-msg" />
						</div>
					</div>
					<input type="hidden" name="CFunction" value="${function.id}" />
				</div>
			</div>
		</div>
	</div>
</div>
