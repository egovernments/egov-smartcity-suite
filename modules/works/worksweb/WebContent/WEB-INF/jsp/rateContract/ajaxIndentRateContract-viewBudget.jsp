<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
		<title><s:text name='estimate.budget.details.title' />
		</title>
		<body onload="roundOffbudgetAmount()" >
		<style type="text/css">
#warning {
	display: none;
	color: blue;
}
</style>
<script src="/js/helper.js"></script>
<script>
function roundOffbudgetAmount()
{

document.viewBudget.totalBudget.value = roundTo(document.viewBudget.totalBudgethidden.value);
document.viewBudget.budgetUtilized.value = roundTo(document.viewBudget.budgetUtilizedhidden.value);
document.viewBudget.budgetAvailable.value = roundTo(document.viewBudget.budgetAvailablehidden.value);
}
</script>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>

	<FORM name="viewBudget"> 
		<table align="center">
			<tr>
				<td class="headingwk">
					<s:text name='estimate.budget.details.total.budget' />
				</td>
				<td class="headingwk">
					<s:text name='estimate.budget.details.utilized.budget' />
				</td>
				<td class="headingwk">
					<s:text name='estimate.budget.details.available.budget' />
				</td>
			</tr>
			<tr>
				<td class="greyboxwk">
				 	<input type="hidden" id="totalBudgethidden" value='<s:property value="totalBudget" />'/>
				 	<input type="text" id="totalBudget"/>
				</td>
				<td class="greyboxwk">
				<input type="hidden" id="budgetUtilizedhidden" value='<s:property value="budgetUtilized" />'/>
				<input type="text" id="budgetUtilized" />
					
				</td>
				<td class="greyboxwk">
				<input type="hidden" id="budgetAvailablehidden" value='<s:property value="budgetAvailable" />'/>
				<input type="text" id="budgetAvailable" />
					
				</td>
			</tr>
		</table>
		
			<table align="center">
				<tr>
					<td>
						<input type="button" name="closebutton" id="closeButton" value="CLOSE"  class="buttonfinal" onclick="window.close();" />
					</td>
				</tr>
			</table>

		</FORM>

	</body>

</html>

