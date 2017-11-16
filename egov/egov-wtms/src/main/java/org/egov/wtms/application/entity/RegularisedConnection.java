package org.egov.wtms.application.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;

@Entity
@Table(name = "egwtr_regularise_connection_detail")
@SequenceGenerator(name = RegularisedConnection.SEQ_REGULARISE_CONNECTION, sequenceName = RegularisedConnection.SEQ_REGULARISE_CONNECTION, allocationSize = 1)
public class RegularisedConnection extends StateAware<Position> {

    public static final String SEQ_REGULARISE_CONNECTION = "SEQ_EGWTR_REGULARISE_CONNECTION_DETAIL";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = SEQ_REGULARISE_CONNECTION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String ulbCode;

    @NotNull
    private String propertyIdentifier;

    @Override
    public String getStateDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public void setPropertyIdentifier(final String propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }

}
