/**
 * 
 */
package org.egov.pgr.entity;

import java.lang.reflect.Field;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.pims.commons.DesignationMaster;

/**
 * @author Vaibhav.K
 *
 */
public class EscalationBuilder {
    
    private Escalation escalation;
    
    public EscalationBuilder () {
        escalation = new Escalation();
    }

    public EscalationBuilder withId(final long id) {
        try {
            final Field idField = escalation.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(escalation, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public EscalationBuilder withComplaintType(final ComplaintType type) {
        escalation.setComplaintType(type);
        return this;
    }
    
    public EscalationBuilder withDesignation(final DesignationMaster designation) {
        escalation.setDesignation(designation);
        return this;
    }
    
    public EscalationBuilder withHrs(final Integer hrs) {
        escalation.setNoOfHrs(hrs);
        return this;
    }
    
    public Escalation build() {
        return escalation;
    }
}
