<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<s:select list="dropdownData.budgetList"  listKey="id" listValue="name" name="budgetDetail.budget.id" headerKey="0" headerValue="--- Select ---"  id="newBudgetDropDownList" cssStyle="display:none"/>
<input type="hidden" name="newBeRe" id="newBeRe" value='<s:property value="beRe"/>'/>