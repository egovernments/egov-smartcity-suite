/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.workflow.multitenant.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.StateAware;

import aj.org.objectweb.asm.Attribute;

@MappedSuperclass
public abstract class WorkflowEntity extends AbstractAuditable {
    private static final long serialVersionUID = 5776408218810221246L;
   
    @Column(name="processid")
    private String workflowId;
    
    @Column(name="state_id")
    private Long state_id;
        
    public Long getState_id() {
        return state_id;
    }
     public void setState_id(Long state_id) {
        this.state_id = state_id;
    }


    public abstract String getStateDetails();
    
    @Deprecated
    @Transient
    private ProcessInstance processInstance;
   
    @Deprecated
    @Transient
    private Task currentTask;
    
    
   

    public Task getCurrentTask() {
        return currentTask;
    }


    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }


    /**
     * To set the Group Link, Any State Aware Object which needs Grouping should override this method
     **/
    public String myLinkId() {
        return getId().toString();
    }

    
    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }
    

   }

