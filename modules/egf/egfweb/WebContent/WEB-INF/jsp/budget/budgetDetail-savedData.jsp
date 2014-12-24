<%@ include file='budgetDetailList.jsp'%>
<input type="hidden" name="newPreviousYearValue" id="newPreviousYearValue" value='<s:property value="%{previousYearRange}"/>'/>
<input type="hidden" name="newCurrentYearValue" id="newCurrentYearValue" value='<s:property value="%{currentYearRange}"/>'/>
<input type="hidden" name="newLastButOneYearValue" id="newLastButOneYearValue" value='<s:property value="%{lastButOneYearRange}"/>'/>
<input type="hidden" name="newNextYearValue" id="newNextYearValue" value='<s:property value="%{nextYearRange}"/>'/>
<input type="hidden" name="re" id="re" value='<s:property value="re"/>'/>
<input type="hidden" name="referenceBudgetName" id="referenceBudgetName" value='<s:property value="referenceBudget.name"/>'/>
<input type="hidden" id="newBudgetDocNum" value='<s:property value="%{budgetDocumentNumber}"/>'/>
