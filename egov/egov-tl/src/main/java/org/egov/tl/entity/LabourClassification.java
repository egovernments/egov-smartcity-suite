
/**
 * 
 */
package org.egov.tl.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * @author Pabitra
 *
 */
@Entity
@Table(name = "EGTL_MSTR_CLASSIFICATION_TYPE")
@SequenceGenerator(name = LabourClassification.SEQUENCE, sequenceName = LabourClassification.SEQUENCE, allocationSize = 1)
@Unique(fields = "name", enableDfltMsg = true)
public class LabourClassification extends AbstractPersistable<Long>{
	protected static final String SEQUENCE = "SEQ_EGTL_MSTR_CLASSIFICATION_TYPE";
    private static final long serialVersionUID = 5631753833454331638L;

    @Id
    @GeneratedValue(generator = SEQUENCE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(max = 25)
    @SafeHtml
    private String name;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LabourClassification))
            return false;
        LabourClassification that = (LabourClassification) obj;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

