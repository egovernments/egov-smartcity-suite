<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<script>
	var isdefault = true;
</script>
<div id="performanceWin" class="container-win" style="display:none">
	<div class="row">
		<div class="col-md-6 col-lg-6 col-sm-12">
			<div id="performanceTable" style="min-height:500;width:100%;" class="gm-style"></div>
		</div>
		<div class="col-md-6 col-sm-12 col-lg-6">
			<div id="performanceGraph" style="min-height:500;width:100%;" class="gm-style"></div>
		</div>
	</div>
</div>
<div id="overviewWin" class="container-win" >
	<div class="row">
		<div class="col-md-6 col-sm-12 col-lg-6">
			<div id="overviewGraphCumilative" class="gm-style"></div>
		</div>
		<div class="col-md-6 col-sm-12 col-lg-6">
			<div id="overviewGraph" class="gm-style"></div>
		</div>
	</div>
</div>
<div id="collectionsPaymentWin" class="container-win" style="display:none">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-lg-12">
			<div id="collectionsPaymentGraph" style="width:100%;" class="gm-style"></div>
		</div>
	</div>
</div>
<div id="coverageEfficiencyWin" class="container-win" style="display:none">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-lg-12">
			<div id="coverageEfficiencyGraph" style="width:100%;" class="gm-style"></div>
		</div>
	</div>
</div>
<div id="zonewiseAnalysisWin" class="container-win" style="display:none">
	<div class="row" style="margin-top:-40px">
		<div class="col-md-4 col-sm-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-blue">Demand per Property</div> 
			<div id="zonewiseAnalysis1"  style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-green">Collection per Property</div>
			<div id="zonewiseAnalysis2" style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-red">Balance per Property</div>
			<div id="zonewiseAnalysis3" style="height:500px"></div>
		</div>
	</div>
	<div class="row" style="margin-bottom:50px">
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-violet">No of Properties / Sq.km</div>
			<div id="zonewiseAnalysis4" style="min-height:500px"></div>			
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-maroon">Coverage Efficiency</div>
			<div id="zonewiseAnalysis5" style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-olivegreen">Collection Efficiency</div>
			<div id="zonewiseAnalysis6" style="min-height:500px"></div>
		</div>
	</div>
</div>
<div id="wardwiseAnalysisWin" class="container-win" style="display:none">
	<div class="row" style="margin-top:-40px">
		<div class="col-md-4 col-sm-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-blue">Demand per Property</div> 
			<div id="wardwiseAnalysis1"  style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-green">Collection per Property</div>
			<div id="wardwiseAnalysis2" style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-red">Balance per Property</div>
			<div id="wardwiseAnalysis3" style="height:500px"></div>
		</div>
	</div>
	<div class="row" style="margin-bottom:50px">
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-violet">No of Properties / Sq.km</div>
			<div id="wardwiseAnalysis4" style="min-height:500px"></div>			
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-maroon">Coverage Efficiency</div>
			<div id="wardwiseAnalysis5" style="min-height:500px"></div>
		</div>
		<div class="col-md-4 col-sm-4 col-ms-4 col-lg-4" style="height:500px;margin-top:50px">
			<div class="bs-callout bs-callout-info header-olivegreen">Collection Efficiency</div>
			<div id="wardwiseAnalysis6" style="min-height:500px"></div>
		</div>
	</div>
</div>