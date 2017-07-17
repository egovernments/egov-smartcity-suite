<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="row">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">Create Preamble</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<c:if
					test="${councilPreamble.preambleNumber!= null && !''.equalsIgnoreCase(councilPreamble.preambleNumber)}">
					<label class="col-sm-2 control-label text-right"> <spring:message
							code="lbl.preamble.number" />
					</label>
					<div class="col-sm-3 add-margin">
						${councilPreamble.preambleNumber}</div>
				</c:if>

				<input type="hidden" name="councilPreamble"
					value="${councilPreamble.id}" />
				<form:hidden path="preambleNumber" id="preambleNumber"
					value="${councilPreamble.preambleNumber}" />
				<form:hidden path="type" id="type" value="${councilPreamble.type}" />
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.department" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="department" id="department"
						cssClass="form-control" cssErrorClass="form-control error"
						required="required">
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
					<form:input path="sanctionAmount"
						class="form-control text-left patternvalidation"
						data-pattern="number" />
					<form:errors path="sanctionAmount" cssClass="error-msg" />
				</div>
				<form:hidden path="" id="URL" name="URL" value="${URL}" />
				<a
					onclick="window.open('${URL}','name','width=800, height=600,scrollbars=yes')">Click
					here to Check Budget</a>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.gistofpreamble" /><span class="mandatory"></span></label>
				<div class="col-sm-8 add-margin">
					<form:textarea path="gistOfPreamble" id="gistOfPreamble"
						data-role="none" rows="10"
						class="form-control text-left patternvalidation" maxlength="10000"
						required="required" />
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
						code="lbl.upload" /><span class="mandatory"></span></label>
				<%-- <div class="col-sm-3 add-margin">
							<input type="file" id="attachments" name="attachments"
								data-id="1" class="filechange inline btn" />
							<form:errors path="attachments" cssClass="error-msg" />
				</div> --%>
				<div class="col-sm-3 add-margin">
					<c:choose>
						<c:when test="${councilPreamble.filestoreid != null}">

							<form:input path="attachments" type="file" id="attachments"
								name="attachments" data-id="1"
								class="filechange inline btn upload-file" />
							<form:errors path="attachments" cssClass="error-msg" />

							<form:hidden path="filestoreid.id"
								value="${councilPreamble.filestoreid.id}" />
							<form:hidden path="filestoreid.fileStoreId"
								value="${councilPreamble.filestoreid.fileStoreId}" />

							<a
								href="/council/councilmember/downloadfile/${councilPreamble.filestoreid.fileStoreId}"
								data-gallery> ${councilPreamble.filestoreid.fileName}</a>
							<small class="error-msg"><spring:message
									code="lbl.mesg.document" /></small>
						</c:when>
						<c:otherwise>
							<form:input path="attachments" type="file" id="attachments"
								name="attachments" required="true" data-id="1"
								class="filechange inline btn upload-file" />
							<small class="error-msg"><spring:message
									code="lbl.mesg.document" /></small>
							<form:errors path="attachments" cssClass="error-msg" />
						</c:otherwise>
					</c:choose>
				</div>

				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.ward.no" /></label>
				<div class="col-sm-3 add-margin">
					<select name="wards" multiple id="wards" size="7"
						class="form-control wards tick-indicator">
						<option value="">All</option>
						<c:forEach items="${wards}" var="ward">
							<option value="${ward.id}" title="${ward.name}"
								<c:if test="${fn:contains(councilPreamble.wards, ward)}"> Selected </c:if>>${ward.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div>Note: After getting the council preamble prepared and
				approved by the head of the section, the same should be uploaded
				here and forward to the competent authority for further action</div>
		</div>
	</div>
</div>
