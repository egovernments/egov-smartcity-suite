/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
//var userRole = CookieManager.getCookie('userRole');
var d = document.getElementById('TreeDiv');
//alert (d);
var t = new NamTree(d);
//alert (t);

//if(userRole.toLowerCase() == 'fo') {

//}
t.createNode( "", "1", "Transactions");
t.createNode( "", "2", "Reports");
t.createNode( "", "3", "Masters");
t.createNode( "", "4", "Processing");
t.createNode( "", "5", "Set-up");


t.createNode( "", "101", "Receipts", "1");
	t.createNode( "", "1011", "Property Tax Collection", "101");
	t.createNode( "", "1012", "Other Taxes Collection", "101");
		t.createNode( "", "10121", "Field", "1012");
		t.createNode( "", "10122", "Office", "1012");
	t.createNode( "", "1013", "Miscellaneous Receipts", "101");
	t.createNode( "", "1014", "View Receipts","101");
	t.createNode( "", "1015", "Reverse Receipts","101");
t.createNode( "", "104", "Bills Accounting", "1");
	t.createNode( "", "1040", "Bill Register", "104");
	t.createNode( "", "1041", "Contractors Bill", "104");
		t.createNode( "", "10411", "Enter Contractor Bill", "1041");
		t.createNode( "", "10412", "View Contractor Bill", "1041");
		t.createNode( "", "10413", "Reverse Contractor Bill", "1041");
	t.createNode( "", "1042", "Suppliers Bill", "104");
		t.createNode( "", "10421", "Enter Supplier Bill", "1042");
		t.createNode( "", "10422", "View Supplier Bill", "1042");
		t.createNode( "", "10423", "Reverse Supplier Bill", "1042");
	t.createNode( "", "1043", "Salary Bill", "104");
		t.createNode( "", "10431", "Create Salary Bill", "1043");
		t.createNode( "", "10432", "View Salary Bill", "1043");
		t.createNode( "", "10433", "Reverse Salary Bill", "1043");
t.createNode( "", "102", "Payments", "1");
	t.createNode( "", "1021", "Bank Payments", "102");
		t.createNode( "", "10211", "Bank Payment", "1021");
		t.createNode( "", "10212", "View Bank Payments", "1021");
		t.createNode( "", "10213", "Reverse Bank Payments","1021");
	t.createNode( "", "1022", "Cash Payments", "102");
		t.createNode( "", "10221", "Cash Payment", "1022");
		t.createNode( "", "10222", "View Cash Payments","1022");
		t.createNode( "", "10223", "Reverse Cash Payments","1022");
	t.createNode( "", "1023", "Subledger Payments", "102");
		t.createNode( "", "10231", "Advance Payment", "1023");
		t.createNode( "", "10232", "Pay Supplier/Contractor", "1023");
		t.createNode( "", "10233", "View Subledger Payments", "1023");
		t.createNode( "", "10234", "Reverse Subledger Payments", "1023");
	t.createNode( "", "1024", "Salary Payment", "102");
		t.createNode( "", "10241", "Pay Salary", "1024");
		t.createNode( "", "10242", "View Salary Payment", "1024");
		t.createNode( "", "10243", "reverse Salary Payment", "1024");
t.createNode( "", "105", "Journal Proper", "1");
	t.createNode( "", "1051", "Create Journal Proper", "105");
	t.createNode( "", "1052", "Confirm Journal Proper", "105");
	t.createNode( "", "1053", "View Journal Proper", "105");
	t.createNode( "", "1054", "Modify Journal Proper", "105");
	t.createNode( "", "1055", "Reverse Journal Proper","105");
t.createNode( "", "103", "Contra entry", "1");
	t.createNode( "", "1031", "Cash Deposit", "103");
	t.createNode( "", "1032", "Cash Withdrawal", "103");
	t.createNode( "", "1033", "Bank to Bank Transfer", "103");
	t.createNode( "", "1034", "Inter-Fund Transfer", "103");
	t.createNode( "", "1035", "View Contra Entries", "103");
	t.createNode( "", "1036", "Reverse Contra Entries", "103");
	t.createNode( "", "1037", "Pay-in", "103");

t.createNode( "", "106", "BRS", "1");


t.createNode( "", "201", "Accounting Records", "2");
	t.createNode( "", "2019", "Bill Register Report", "201");
	t.createNode( "", "2016", "Trial Balance", "201");
	t.createNode( "", "2011", "Cash Book", "201");
	t.createNode( "", "2012", "Bank Book", "201");
	t.createNode( "", "2013", "Journal Book", "201");
	t.createNode( "", "2014", "General Ledger", "201");
	t.createNode( "", "2015", "Sub-Ledger", "201");
	t.createNode( "", "2017", "Day Book", "201");
	t.createNode( "", "2018", "SubLedger Schedule", "201");
t.createNode( "", "202", "Financial Statements", "2");
	t.createNode( "", "2021", "Balance Sheet", "202");
	t.createNode( "", "2022", "Income & Expenditure Statement", "202");
//t.createNode( "", "203", "Sub-Ledger Schedules", "2");
	//t.createNode( "", "2031", "Contractors/Suppliers Schedule", "203");
	//t.createNode( "", "2032", "Festival Advance", "203");
	//t.createNode( "", "2033", "Housing Advance", "203");
	


t.createNode( "", "301", "Chart Of Accounts", "3");
	t.createNode( "", "3011", "Chart of Accounts", "301");
	t.createNode( "", "3012", "Bank Accounts", "301");
t.createNode( "", "3019", "CostCentre", "3");
t.createNode( "", "3020", "Department", "3");
t.createNode( "", "3021", "Division", "3");
t.createNode( "", "302", "User-defined Codes", "3");
t.createNode( "", "311", "Tax Setup", "3");
t.createNode( "", "303", "Source of Financing", "3");
//t.createNode( "", "307", "Department", "3");
t.createNode( "", "308", "Bill Collector", "3");
t.createNode( "", "309", "Administrative Hierarchy", "3");
t.createNode( "", "304", "Procurement Orders", "3");
		//t.createNode( "", "3041", "Create Procurement Order", "304");
		//t.createNode( "", "3042", "Modify Procurement Order", "304");
		//t.createNode( "", "3043", "View Procurement Orders", "304");
t.createNode( "", "3018", "Scheme", "3");
t.createNode( "", "3022", "Project", "3");

	//	t.createNode( "", "30181", "Create Scheme", "3018");
	//	t.createNode( "", "30182", "View Scheme", "3018");
	//	t.createNode( "", "30183", "Modify Scheme", "3018");
	//	t.createNode( "", "30184", "Delete Scheme", "3018");
	

		//t.createNode( "", "30191", "Create CostCentre", "3019");
		//t.createNode( "", "30192", "View CostCentre", "3019");
		//t.createNode( "", "30193", "Modify CostCentre", "3019");
		//t.createNode( "", "30194", "Delete CostCentre", "3019");

t.createNode( "", "305", "Supplier/Contractor", "3");
		t.createNode( "", "3051", "Create Supplier/Contractor", "305");
		t.createNode( "", "3052", "View Supplier/Contractor", "305");
		t.createNode( "", "3053", "Modify Supplier/Contractor", "305");

t.createNode( "", "306", "Setup Cheque in Hand/Cash in Hand", "3");	
t.createNode( "", "307", "Accounting Entity", "3");

t.createNode( "", "401", "Financial Year", "4");
	t.createNode( "", "4011", "Opening", "401");
	t.createNode( "", "4012", "Close Period", "401");	
	//t.createNode( "", "4013", "Transfering closing balances", "401");

t.createNode( "", "501", "Chart of Accounts", "5");
t.createNode( "", "503", "Funds", "5");
t.createNode( "", "504", "Functions", "5");
t.createNode( "", "505", "ULB Details", "5");
t.createNode( "", "507", "Code-Screen Mapping", "5");
t.createNode( "", "508", "Creditors Recoveries", "5");
t.createNode( "", "506", "Budget", "5");
	t.createNode( "", "5061", "Create Budget", "506");
	t.createNode( "", "5062", "Copy Budget", "506");
	t.createNode( "", "5063", "Approve Budget", "506");
	t.createNode( "", "5064", "Supplimentary", "506");
t.createNode( "", "509", "Assets", "5");
	t.createNode( "", "5091", "Asset Category", "509");
	t.createNode( "", "5092", "Asset", "509");
	t.createNode( "", "5093", "Depreciation Add", "509");
	
	
t.normalBackgroundColor="white" ;
t.highlightedbackgroundColor="blue";
t.normalColor="navy";
t.highlightedColor="white";
t.fontFamily="tahoma";
t.fontSize="8pt";
t.indent="20";
t.searchIsRequired="true";

t.addImg("main" , "/egi/resources/erp2/images/main.gif");
t.addImg("sub" , "/egi/resources/erp2/images/sub.gif");
t.addImg("accounthead","/egi/resources/erp2/images/head.gif");
t.addImg("account","/egi/resources/erp2/images/account.gif");
//alert ('completed');
t.display();
window.status = 'completed';
	
