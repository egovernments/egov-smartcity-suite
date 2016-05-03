<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

		<link rel="stylesheet" href="../egi/resources/global/css/font-icons/entypo/css/entypo.css">
		<link rel="stylesheet" href="../egi/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css">
		<script type="text/javascript">
			function onBodyLoad(){
				var unreadMessageCount = "${unreadMessageCount}";
				document.getElementById("unreadMessageCount").innerHTML=unreadMessageCount;
			}
			function refreshInbox(obj, elem){
				$.ajax({
					url: "/portal/home/refreshInbox",
					type : "GET",
					data: {
						citizenInboxId : obj
					},
					success : function(response) {
						document.getElementById("unreadMessageCount").innerHTML=response;
		            }
		        });
				$(elem).find("i").removeClass('unread-msg').addClass('read-msg');
			}
			function search(elem) {
				var searchText = $(elem).val(); 
			    $(".msg").each(function() {
			         var $this = $(this)
			         if ($this.find('div').text().toUpperCase().search(searchText.toUpperCase()) === -1) {
			             $this.hide();
			         }else {
				         $this.show();
				     }
			    });
			};
		</script>
		<div class="citizen-screens" id="inbox-template">
						
						<div class="row padding-tb"><!-- padding-tb -->
							<div class="search-box col-md-9 col-sm-8">
								<i class="fa fa-search"></i>
								<input type="text" class="form-control" placeholder="Search" onkeyup="search(this)">
							</div>
							<div class="col-md-3 col-sm-4 text-center xs-margin-top-10">
								<div class="btn-group btn-input clearfix" id="sortby_drop">
									<button type="button" class="btn btn-default dropdown-toggle form-control" data-toggle="dropdown">
										<span data-bind="label">Sort by : <b><i class="fa fa-clock-o"></i> Recent Messages</b></span> <span class="caret"></span>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li><a href="#"><i class="fa fa-clock-o"></i> Recent Messages</a></li>
										<li><a href="#"><i class="user-msg fa fa-user"></i> User Messages</a></li>
										<li><a href="#"><i class="fa fa-database"></i> System Notifications</a></li>
									</ul>
								</div>
							</div>
						</div>
						
						<div class="row container-msgs">
							
							<section class="col-lg-12">
								<table>
								<c:forEach var="inboxMsg" items="${inboxMessages}" varStatus="status">
								<tr id="#${inboxMsg.id}">
								<div class="msg" data-toggle="collapse" data-target="#${inboxMsg.id}" aria-expanded="true" onclick="refreshInbox(${inboxMsg.id}, this)">
									<header>
										
										<div class="row">
											<c:if test="${inboxMsg.read}">
											<i class="user-msg fa fa-user col-sm-1 col-xs-2 read-msg"></i>
											</c:if>
											<c:if test="${!inboxMsg.read}">
											 <i class="user-msg fa fa-user col-sm-1 col-xs-2 unread-msg"></i>
											</c:if>
											<h3 class="col-sm-11 col-xs-10">
											${inboxMsg.headerMessage}<span class="msg-status">${inboxMsg.status}</span> </h3> 
										</div>
										
										
										<div class="msg-info">
											<a href="${inboxMsg.link}" target="_blank"><u>${inboxMsg.identifier}</u></a> <span class="dot">&bull;</span> 
											<fmt:formatDate value="${inboxMsg.messageDate}" pattern="dd-MM-yyyy hh:mm:ss" var="messageDate"/>
											<span class="msg-date">${messageDate}</span>
										</div>
									</header>
									
									 <c:if test="${status.first}">
										<div id="${inboxMsg.id}" class="msg-content collapse in">
											<p> 
												${inboxMsg.detailedMessage} 
											</p>
										</div>
									 </c:if>
									 <c:if test="${!status.first}">
									 	<div id="${inboxMsg.id}" class="msg-content collapse">
											<p> 
												${inboxMsg.detailedMessage} 
											</p>
										</div>
									 </c:if> 
										
								</div>
								</tr>
								</c:forEach>
								</table>
							</section>
					</div>
					
				</div>
				
				<div class="citizen-screens tabs tabs-style-topline myacc display-hide" id="myaccount">
					<nav>
						<ul>
							<li class="tab-current-myacc" data-section="myaccount" data-myaccount-section="#section-myaccount-1">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-book"></i></div>
									<span class="hidden-sm hidden-xs">My Grievances</span>
								</a>
							</li>
						</ul>
					</nav>
					<div class="content-wrap">
						<section id="section-myaccount-1"  class="content-current-myacc">
							<div class="visible-xs visible-sm add-margin">My Grievance</div>
							<c:forEach var="myAccountMsg" items="${myAccountMessages}">
								<div class="msg" data-toggle="collapse" data-target="#${myAccountMsg.id}" aria-expanded="true">
									<header>
										<div class="row">
											<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">${myAccountMsg.headerMessage}</h3>
										</div>
											<div class="myaccount-actions">
											<a href="${myAccountMsg.link}" target="_blank"><i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Grievance"></i></a>
										</div>
										<div class="msg-info">
											<a href="${myAccountMsg.link}" target="_blank"><u>${myAccountMsg.identifier}</u></a> <span class="dot">&bull;</span> 
											<span class="msg-date">
											<fmt:formatDate value="${myAccountMsg.messageDate}" pattern="dd-MM-yyyy hh:mm:ss" var="messageDate"/>
											</span>
											<span class="msg-date">${messageDate}</span>
										</div>
									</header>
								</div> 
							</c:forEach>
						</section>
						
					</div>
				</div>
				
				<div class="citizen-screens tabs tabs-style-topline newrequest display-hide" id="newreq">
					<nav>
						<ul>
							<li class="tab-current-newreq features" data-section="newrequest" data-newreq-section="#section-newrequest-1" id="pgr">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-book"></i></div>
									<span class="hidden-sm hidden-xs">Grievance Redressal</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-2" id="ptis" class="features">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Property Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-11" id="wats" class="features">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Water Charge Management</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-3" id="bpa" class="features">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-building-o"></i></div>
									<span class="hidden-sm hidden-xs">Building Plan Approval</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-4" id="bnd" class="features">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-medkit"></i></div>
									<span class="hidden-sm hidden-xs">Birth &amp; Death</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-5" id="tl" class="features">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-text-width"></i></div>
									<span class="hidden-sm hidden-xs">Trade Licence</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-6" id="proftax" class="features" >
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Professional Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-7" id="comptax" class="features" >
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Company Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-8" id="shops" class="features" >
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-money"></i></div>
									<span class="hidden-sm hidden-xs">Shops</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-9" id="advtax" class="features" >
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-adn"></i></div>
									<span class="hidden-sm hidden-xs">Advertisement</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-10" id="other" class="features" >
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-hand-o-right"></i></div>
									<span class="hidden-sm hidden-xs">Others</span>
								</a>
							</li>
						</ul>
					</nav>
					<script>
						$(".features").hide();
					</script>
					<c:forEach items="${enabledFeatures}" var="feature">
						<script>
						var feature = '${feature}';
						if (feature === 'all') { $(".features").show(); } else { $("#"+feature).show();}
						</script>
					</c:forEach>
					<script>
					$('.tabs-style-topline nav li').click(function(){
						if($(this).attr('data-section') == "newrequest"){
							$('.tabs-style-topline nav li').removeClass('tab-current-newreq');
							$(this).addClass('tab-current-newreq');
							$('.content-wrap section.newreq').removeClass('content-current-newreq');
							$($(this).data('newreq-section')).addClass('content-current-newreq');
						}
					});
					</script>
					<div class="content-wrap">
						<section id="section-newrequest-1"  class="newreq content-current-newreq">
							<div class="visible-xs visible-sm add-margin">Grievance Redressal</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="/pgr/complaint/citizen/show-reg-form" target="_blank" class="col-sm-11 col-xs-10">Register Grievance</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg"></i><a href="/pgr/complaint/citizen/anonymous/search" target="_blank" class="col-sm-11 col-xs-10">View Grievance</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-2" class="newreq">
							<div class="visible-xs visible-sm add-margin">Property Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Property Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Property to My Account</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-search col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Search Property</a>
									</div>
								</header>
							</div>
						</section>
						
						<section id="section-newrequest-11"  class="newreq">
							<div class="visible-xs visible-sm add-margin">Water Charge Management</div>
							
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg"></i><a href="/wtms/search/waterSearch/" target="_blank" class="col-sm-11 col-xs-10">Pay Charges</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg"></i><a href="/wtms/elastic/appSearch/" target="_blank" class="col-sm-11 col-xs-10">Pay Fees</a>
									</div>
								</header>
							</div>
						</section>
						
						<section id="section-newrequest-3" class="newreq">
							<div class="visible-xs visible-sm add-margin">Building Plan Approval</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Additional Construction</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Demolition and Reconstruction</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Demolition Only</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for New Building Permit</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-4" class="newreq">
							<div class="visible-xs visible-sm add-margin">Birth & Death</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Birth/Death Certificate</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-search col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Search for Birth/Death records</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply for Name Inclusion</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Record Correction</a>
									</div>
								</header>
							</div-->
						</section>
						<section id="section-newrequest-5" class="newreq">
							<div class="visible-xs visible-sm add-margin">Trade Licence</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply For Trade Licence</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Renew Licence</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Licence to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-6" class="newreq">
							<div class="visible-xs visible-sm add-margin">Professional Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Professional Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Profession to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-7" class="newreq">
							<div class="visible-xs visible-sm add-margin">Company Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Company Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Company to my Account</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-8" class="newreq">
							<div class="visible-xs visible-sm add-margin">Shops</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Fees</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Shop to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-9" class="newreq">
							<div class="visible-xs visible-sm add-margin">Advertisement</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Fees</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Hoarding to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-10" class="newreq">
							<div class="visible-xs visible-sm add-margin">Others</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Online Payment For Challans</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply For Road Cut</a>
									</div>
								</header>
							</div> 
						</section>
					</div>
				</div>


																																																			
