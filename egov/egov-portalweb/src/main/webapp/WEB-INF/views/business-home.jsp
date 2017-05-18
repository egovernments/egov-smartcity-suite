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
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
    <div class="container-fluid">
      <div class="">
        <div class="left-menu">
          <ul class="modules-ul">
            <li class="modules-li inbox active" data-module="home">
              <a href="javascript:void(0)">
                <div><i class="fa fa-home img-view"></i></div>
                <div class="module-text">Home</div>
              </a>
            </li>
            <c:forEach items="${moduleNames}" var="moduleName" varStatus="item">
	            <li class="modules-li" data-module="${moduleName }">
	              <a href="javascript:void(0)">
	                <div><i class="fa fa-building img-view" aria-hidden="true"></i></div>
	                <div class="module-text">${moduleName }</div>
	              </a>
	            </li>
            </c:forEach>
       </ul>
        </div>
        <div class="right-content">
          <header>
            <nav class="navbar">
              <div class="col-md-6 col-sm-9 col-xs-12 left-section">
                <span><img src="<cdn:url value='/resources/global/images/logo@2x.png' context='/egi'/>" height="52px">
                
                </span>
                <span class="corporation-name"><spring:message code="lbl.citizenportalservices" /></span>
              </div>
              <div class="col-md-6 col-sm-3 col-xs-12 right-section">
                <span class="pull-right profile-dd"><a href="/egi/logout"><i class="fa fa-sign-out" aria-hidden="true"></i></a></span>
                <span class="pull-right profile-name">
                  <span class="text hidden-sm">${userName }</span> <span><i class="fa fa-caret-down" aria-hidden="true"></i></span>
                  <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
                  
                    <li><a href="/egi/home/profile/edit"  onClick="MyWindow=window.open('/egi/home/profile/edit','MyWindow',width=980,height=650); return false;" >Edit Profile</a></li>
                    <li><a href="javascript:void(0)" onclick="jQuery('.change-password').modal('show', {backdrop: 'static'});" >Change Password</a></li>
                  </ul>
                </span>
              </div>
            </nav>
          </header>
          <div class="main-content">
            <div class="action-bar hide">
              <div class="action-item"><input type="text" id="search" placeholder="Search"></div>
              <div class="action-item"><i class="material-icons">search</i></div>
            </div>
            <div class="inbox-modules">
              <div class="row stats-item">
                <div class="col-md-4 col-sm-4 services">
                <div id="totalServicesAppliedDiv" style="cursor: pointer">
                  <div class="content x ">
                    <div class="count">
                      ${totalServicesAppliedSize }
                    </div>
                    <div class="text">
                      <spring:message code="lbl.totalservicesapplied" />
                    </div>
                  </div>
                  </div>
                </div>
                <div class="col-md-4 col-sm-4 services">
                <div id="servicesUnderScrutinyDiv" style="cursor: pointer" >
                  <div class="content y">
                    <div class="count">
                      ${totalServicesPendingSize}
                    </div>
                    <div class="text">
                    <spring:message code="lbl.servicesunderscrutiny" />
                    </div>
                  </div>
                  </div>
                </div>
                <div class="col-md-4 col-sm-4 services">
                 <div id="servicesCmpletedDiv" style="cursor: pointer">
                  <div class="content z">
                    <div class="count">
                      ${totalServicesCompletedSize }
                    </div>
                    <div class="text">
                    	<spring:message code="lbl.servicescompleted" />
                    </div>
                  </div>
                  </div>
                </div>
              </div><br>
              <%-- ${inboxItems.portalInbox} --%>
              <table class="table table-striped">
                <thead>
                  <tr>
                    <th><spring:message code="lbl.slno" /></th>
                    <th><spring:message code="lbl.applicartionno" /></th>
                    <th><spring:message code="lbl.applicationdate" /></th>
                    <th><spring:message code="lbl.servicename" /></th>
                    <th><spring:message code="lbl.status" /></th>
                    <th><spring:message code="lbl.pendingaction" /></th>
                    <th><spring:message code="lbl.expectedservicedelivery" /></th>
                    <th><spring:message code="lbl.actualservicedelivery" /></th>
                  </tr>
                </thead>
                <tbody class="servicesUnderScrutinyHide">
                <c:forEach items="${totalServicesPending}" var="inboxItem" varStatus="item">
		                 <tr onclick="openPopUp('${inboxItem.portalInbox.link}');">
		                   <td>${item.index + 1}</td>
		                   <td>${inboxItem.portalInbox.applicationNumber}</td>
		                   <td>${inboxItem.portalInbox.applicationDate}</td>
		                   <td>${inboxItem.portalInbox.serviceType}</td>
		                   <td>${inboxItem.portalInbox.status}</td>
		                   <td>
								<c:choose>
		 							<c:when test="${inboxItem.portalInbox.state != null && inboxItem.portalInbox.state.nextAction != ''}">
		 								${inboxItem.portalInbox.state.nextAction}
	 								</c:when>
	 								<c:otherwise>
	 									<div class="text-center">
	 										<c:out value="-" ></c:out>
	 									</div>
	 								</c:otherwise>
		 						</c:choose>
							</td>
		                   <td>
		 						<c:choose>
		 							<c:when test="${inboxItem.portalInbox.slaEndDate != null }">
		 								${inboxItem.portalInbox.state.slaEndDate}
	 								</c:when>
	 								<c:otherwise>
	 									<div class="text-center">
	 										<c:out value="-" ></c:out>
	 									</div>
	 								</c:otherwise>
		 						</c:choose>
		 					</td>
		 					<td>
		 						<c:choose>
		 							<c:when test="${inboxItem.portalInbox.resolvedDate != null }">
		 								${inboxItem.portalInbox.resolvedDate}
	 								</c:when>
	 								<c:otherwise>
	 									<div class="text-center">
	 										<c:out value="-" ></c:out>
	 									</div>
	 								</c:otherwise>
		 						</c:choose>
		 					</td>
		                 </tr>
                  </c:forEach>
                </tbody>
                <tbody class="totalServicesAppliedHide">
                <c:forEach items="${totalServicesApplied}" var="inboxItem" varStatus="item">
	                	<tr onclick="openPopUp('${inboxItem.portalInbox.link}');">
	                    <td>${item.index + 1}</td>
	                    <td>${inboxItem.portalInbox.applicationNumber}</td>
	                    <td>${inboxItem.portalInbox.applicationDate}</td>
	                    <td>${inboxItem.portalInbox.serviceType}</td>
	                    <td>${inboxItem.portalInbox.status}</td>
	                    <td>
	 						<c:choose>
	 							<c:when test="${inboxItem.portalInbox.state != null && inboxItem.portalInbox.state.nextAction != ''}">
	 								${inboxItem.portalInbox.state.nextAction}
 								</c:when>
 								<c:otherwise>
 									<div class="text-center">
 										<c:out value="-" ></c:out>
 									</div>
 								</c:otherwise>
	 						</c:choose>
	 					</td>
	                    <td>
	 						<c:choose>
	 							<c:when test="${inboxItem.portalInbox.slaEndDate != null }">
	 								${inboxItem.portalInbox.state.slaEndDate}
 								</c:when>
 								<c:otherwise>
 									<div class="text-center">
 										<c:out value="-" ></c:out>
 									</div>
 								</c:otherwise>
	 						</c:choose>
	 					</td>
	                    <td>
	 						<c:choose>
	 							<c:when test="${inboxItem.portalInbox.resolvedDate != null }">
	 								${inboxItem.portalInbox.resolvedDate}
 								</c:when>
 								<c:otherwise>
 									<div class="text-center">
 										<c:out value="-" ></c:out>
 									</div>
 								</c:otherwise>
	 						</c:choose>
	 					</td>
	                  	</tr>
                  </c:forEach>
                </tbody>
                 <tbody class="totalServicesCompletedHide">
                <c:forEach items="${totalServicesCompleted}" var="inboxItem" varStatus="item">
	                  <tr onclick="openPopUp('${inboxItem.portalInbox.link}');">
	                    <td>${item.index + 1}</td>
	                    <td>${inboxItem.portalInbox.applicationNumber}</td>
	                    <td>${inboxItem.portalInbox.applicationDate}</td>
	                    <td>${inboxItem.portalInbox.serviceType}</td>
	                    <td>${inboxItem.portalInbox.status}</td>
	                    <td>
	 						<c:choose>
	 							<c:when test="${inboxItem.portalInbox.state != null && inboxItem.portalInbox.state.nextAction != ''}">
	 								${inboxItem.portalInbox.state.nextAction}
 								</c:when>
 								<c:otherwise>
 									<div class="text-center">
 										<c:out value="-" ></c:out>
 									</div>
 								</c:otherwise>
	 						</c:choose>
	 					</td>
	                    <td>
	 						<c:choose>
	 							<c:when test="${inboxItem.portalInbox.slaEndDate != null }">
	 								${inboxItem.portalInbox.state.slaEndDate}
 								</c:when>
 								<c:otherwise>
 									<div class="text-center">
 										<c:out value="-" ></c:out>
 									</div>
 								</c:otherwise>
	 						</c:choose>
	 					</td>
	                    <td>
	 						<c:choose>
	 							<c:when test="${inboxItem.portalInbox.resolvedDate != null }">
	 								${inboxItem.portalInbox.resolvedDate}
 								</c:when>
 								<c:otherwise>
 									<div class="text-center">
 										<c:out value="-" ></c:out>
 									</div>
 								</c:otherwise>
	 						</c:choose>
	 					</td>
	                  </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
            <c:forEach items="${services}" var="service" varStatus="item">
	            <div class="is-flex services-item">
	              <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 services" data-services="${service.module.displayName }">
	                <a href="${service.url }">
	                  <div class="content a">${service.name}</div>
	                </a>
	              </div>
	            </div>
	        </c:forEach>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <!-- <div id="myModal" class="modal fade" role="dialog" tabindex='-1'>
      <div class="modal-dialog">

        Modal content
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">W102416170000303</h4>
          </div>
          <div class="modal-body">
            <ul class="timeline" id="timeline">
              <li class="li complete">
                <div class="timestamp">
                  <span class="date">16/03/2017<span>
                </div>
                <div class="status">
                  <h4> Application Received </h4>
                </div>
              </li>
              <li class="li complete">
                <div class="timestamp">
                  <span class="date">16/03/2017<span>
                </div>
                <div class="status">
                  <h4> Preliminary Check of Documents </h4>
                </div>
              </li>
              <li class="li complete">
                <div class="timestamp">
                  <span class="date">16/03/2017<span>
                </div>
                <div class="status">
                  <h4> Processing </h4>
                </div>
              </li>
              <li class="li">
                <div class="timestamp">
                  <span class="date">TBD<span>
                </div>
                <div class="status">
                  <h4> Approval </h4>
                </div>
              </li>
              <li class="li">
                <div class="timestamp">
                  <span class="date">TBD<span>
                </div>
                <div class="status">
                  <h4> Certificate Print </h4>
                </div>
              </li>
             </ul>  
            <div class="row">
              <div class="col-md-12 title">
                <h4 class="modal-title">Primary Details</h4>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Application Date</label>
                <div class="field">15/03/2017</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Service Name</label>
                <div class="field">Water Connection</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Status</label>
                <div class="field">Rejected</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Pending User Action</label>
                <div class="field">-</div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Maximum Days For Issuing Certificate</label>
                <div class="field">15</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Expected Service Delivery Date</label>
                <div class="field">30/03/2017</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Actual Service Delivery Date</label>
                <div class="field">15/03/2017</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Application Fee</label>
                <div class="field"><span>&#8377; </span>40</div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Processing Fee</label>
                <div class="field"><span>&#8377; </span>500</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Rejection Reason</label>
                <div class="field">It is rejected since the property has pending tax dues.Please pay the taxes and apply again</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Payment Receipt</label>
                <div class="field"><button class="btn btn-xs btn-success">Download</button></div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Print</label>
                <div class="field"><button class="btn btn-xs btn-info">Print Acknowledgement</button></div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Download Certifcate</label>
                <div class="field"><button class="btn btn-xs btn-success">Download</button></div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12 title">
                <h4 class="modal-title">Applicant Details</h4>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Name</label>
                <div class="field">Natalie Portman</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Mobile No.</label>
                <div class="field">9784563210</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Email ID</label>
                <div class="field">natalie.portman@gmail.com</div>
              </div>
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">Address</label>
                <div class="field">25, 13th cross, 17th main, Madiwala</div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-4 col-xs-6">
                <label for="">UID</label>
                <div class="field">2546965896578</div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12 title">
                <h4 class="modal-title">CheckList</h4>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-3 col-xs-3">
                <label for="">S.no</label>
              </div>
              <div class="col-md-3 col-sm-3 col-xs-3">
                <label for="">Document Name</label>
              </div>
              <div class="col-md-3 col-sm-3 col-xs-3">
                <label for="">File</label>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-3 col-xs-3">
                <div class="field">1</div>
              </div>
              <div class="col-md-3 col-sm-3 col-xs-3">
                <div class="field">Proof of address for bride , bride groom and witnesses</div>
              </div>
              <div class="col-md-3 col-sm-3 col-xs-3">
                <div class="field"><a href="javascript:void(0)">address.pdf</a></div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-3 col-sm-3 col-xs-3">
                <div class="field">2</div>
              </div>
              <div class="col-md-3 col-sm-3 col-xs-3">
                <div class="field">Proof of age for bride , bride groom and witnesses</div>
              </div>
              <div class="col-md-3 col-sm-3 col-xs-3">
                <div class="field"><a href="javascript:void(0)">age.pdf</a></div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
        </div>

      </div>
    </div> -->