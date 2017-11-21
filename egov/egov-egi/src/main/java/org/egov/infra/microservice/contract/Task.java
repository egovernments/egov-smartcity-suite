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

package org.egov.infra.microservice.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Task {

    /**
     * Id of the task getting updated. This field Maps to Id of the Task or ProcessInstance in case of eGov internal Work flow or
     * the Work flow Matrix based implementation .
     */
    @NotNull
    private String id;

    /**
     * businessKey is the name representing the process flow of the a particular Item For example For Financial vouchers work flow
     * process may be defined with a businessKey of "voucher_workflow" . For eGov internal work flow Implementation it is same as
     * the class name of the java object under going work flow. example businessKey "Voucher"
     */
    @Length(max = 128, min = 1)
    @NotNull
    private String businessKey;
    /**
     * type field can be used to further divide the work flow processes. For example Voucher might have 4 different flows 1.
     * Expense Work flow 2. Contractor Journal Work flow 3. Supplier Journal Work flow 4. General JV Work flow Each process is
     * different .Another example is Property might have different flows like 1.Create,Transfer,Bifurcation,Update etc.
     */
    @Length(max = 128, min = 1)
    private String type;
    /**
     * assignee is the position of the user to be set while creating a instantiating of Process. For Automatic work flow this
     * comes from the process definition for manual work flow it is the position selected from the UI.
     */
    @NotNull
    private Position assignee;

    /**
     * comments is the comment provided by the user while he is initiating a process
     */
    @Length(max = 1024, min = 1)
    private String comments;
    /**
     * createDate is the date on which the process is instantiated. This is set internally by the system . For clients it is read
     * only data
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss a",timezone="IST")
    private Date createdDate;

    /**
     * lastupdatedSince is the date on which the process is updated last time. This is set internally by the system . For clients
     * it is read only data
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss a",timezone="IST")
    private Date lastupdatedSince;

    /**
     * owner gives the Position current owner of the process. This data is only for the display purpose. So it will not be
     * considered in the request .
     */
    private Position owner;

    /**
     * state gives the current state of the process.
     */

    @Length(max = 128, min = 1)
    private String state;

    /**
     * status also another representation of the status of the process
     */
    @Length(max = 128, min = 1)
    private String status;
    /**
     * url provides the complete url of the work flow item. This link takes you to the view of the item along with provision to
     * select the next assignee. This data is also configured in work flow type object. This data is also read only . UI Can
     * consider this field to redirect to the page or UI can be build without consuming this.
     */
    @Length(max = 256, min = 1)
    private String url;
    /**
     * action represents the action performed by the end user . it can be forward,approve,reject,cancel etc For eGov internal work
     * flow these four values are confirmed and will do the tasks of forwarding,approving,rejecting,cancelling accordingly. This
     * is mandatory data in case of manual work flow
     */
    @NotBlank
    @Length(max = 128, min = 1)
    private String action;

    /**
     * senderName represents who initiated the work flow process. This is the logged in users primary position . Also this is set
     * by system by taking the logged in users primary position.
     */

    @Length(max = 128, min = 1)
    private String senderName;

    /**
     * details provides more information on the processs/Task instance. Example : In voucher work flow it is VoucherNumber,
     * Property it is the propertyId ,Grievance it is the complaint or request number This data is set internally by the system
     * which is configured in work flow type
     */

    @Length(max = 128, min = 1)
    private String details;
    /**
     * natureOfTask Detaild Description of the task or process . For example in case of voucher it can be set as
     * "Finaicial Voucher Workflow " . This data is also configured by the display name of the workflowtype object. This data is
     * read only. .
     * 
     */
    @Length(max = 128, min = 1)
    private String natureOfTask;
    /**
     * entity Maps to the json representation of the item under workflow. This will be used when work flow considers rule engine.
     * like amount based flow etc
     */
    private String entity;

    private String tenantId;

    private Map<String, Attribute> attributes = new HashMap<>();

    // To be used to fetch single value attributes
    public String getValueForKey(String key) {
        if (Objects.nonNull(attributes.get(key)))
            return attributes.get(key).getValues().get(0).getName();

        return "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Position getAssignee() {
        return assignee;
    }

    public void setAssignee(Position assignee) {
        this.assignee = assignee;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastupdatedSince() {
        return lastupdatedSince;
    }

    public void setLastupdatedSince(Date lastupdatedSince) {
        this.lastupdatedSince = lastupdatedSince;
    }

    public Position getOwner() {
        return owner;
    }

    public void setOwner(Position owner) {
        this.owner = owner;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNatureOfTask() {
        return natureOfTask;
    }

    public void setNatureOfTask(String natureOfTask) {
        this.natureOfTask = natureOfTask;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

}
