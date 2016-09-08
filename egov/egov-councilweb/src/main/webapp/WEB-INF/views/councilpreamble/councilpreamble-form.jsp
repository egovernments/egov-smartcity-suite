<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div>
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">Create Preamble</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.department" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="department" id="department"
						cssClass="form-control" cssErrorClass="form-control error" required ="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${departments}" itemValue="id"
							itemLabel="name" />
					</form:select>
					<form:errors path="department" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.amount" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="sanctionAmount" class="form-control text-left patternvalidation" data-pattern="number" />
					<form:errors path="sanctionAmount" cssClass="error-msg" />
				</div>
				</div>
				<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.gistofpreamble" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:textarea path="gistOfPreamble" class="form-control text-left patternvalidation" maxlength="5000" required ="required"/>
					<form:errors path="gistOfPreamble" cssClass="error-msg" />
				</div>
				<%-- <label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.PreambleType" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="type" id="type" required="required"
						cssClass="form-control" cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${type}" />
					</form:select>
					<form:errors path="type" cssClass="error-msg" />
				</div> --%>
			</div>
				
				<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.upload" /></label>
				<%-- <div class="col-sm-3 add-margin">
							<input type="file" id="attachments" name="attachments"
								data-id="1" class="filechange inline btn" />
							<form:errors path="attachments" cssClass="error-msg" />
				</div> --%>
				<div class="col-sm-3 add-margin">
					<c:choose>
						<c:when test="${councilPreamble.filestoreid != null}">

							<form:input path="attachments" type="file" id="attachments" name="attachments"
								data-id="1" class="filechange inline btn" />
							<form:errors path="attachments" cssClass="error-msg" />

							<form:hidden path="filestoreid.id" value="${councilPreamble.filestoreid.id}" />
							<form:hidden path="filestoreid.fileStoreId"
								value="${councilPreamble.filestoreid.fileStoreId}" />

							<a
								href="/council/councilmember/downloadfile/${councilPreamble.filestoreid.fileStoreId}"
								data-gallery> <img class="img-width add-margin"
								style="max-width: 25%; max-height: 25%;"
								src="/council/councilmember/downloadfile/${councilPreamble.filestoreid.fileStoreId}"
								alt="${councilPreamble.filestoreid.fileName}" /></a>
						</c:when>
						<c:otherwise>
							<form:input path ="attachments" type="file" id="attachments" name="attachments"
								data-id="1" class="filechange inline btn" />
							<form:errors path="attachments" cssClass="error-msg" />
						</c:otherwise>
					</c:choose>
				</div>
			<%-- 	<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.ward.no" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<div>
							<form:select path="wards" multiple="true" id="ward" size="10" cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="0">ALL</form:option>
								<form:options items="${wards}" itemValue="id" itemLabel="name"></form:options>
							</form:select>
							<form:errors path="wards" cssClass="error-msg" /> 
						</div>
						<spring:message code="lbl.pressCntrlToSelectMultipleWards"></spring:message>
				</div> --%>
				
				<c:if test="${councilPreamble.preambleNumber!= null && !''.equalsIgnoreCase(councilPreamble.preambleNumber)}">
				<label class="col-sm-2 control-label text-right">	<spring:message code="lbl.preamble.number" /> </label>
				<div class="col-sm-3 add-margin"> ${councilPreamble.preambleNumber}		</div>
				</c:if>
				</div>
				<input type="hidden" name="councilPreamble" value="${councilPreamble.id}" />
					<form:hidden path="preambleNumber" id="preambleNumber" value="${councilPreamble.preambleNumber}" />
					<form:hidden path="type" id="type" value="${councilPreamble.type}" />
					
			</div>
		</div>
	</div>
