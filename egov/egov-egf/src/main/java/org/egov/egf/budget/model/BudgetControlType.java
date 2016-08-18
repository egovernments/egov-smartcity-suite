package org.egov.egf.budget.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="EGF_BudgetControlType")
@SequenceGenerator(name = BudgetControlType.SEQ, sequenceName = BudgetControlType.SEQ, allocationSize = 1)
public class BudgetControlType extends AbstractAuditable{
 
	
	private static final long serialVersionUID = -1663676230513314512L;
	public static final String SEQ = "seq_EGF_BudgetControlType";
	public enum BudgetCheckOption 
	{
		NONE,ANTICIPATORY,MANDATORY
	}
	@Id
	@GeneratedValue(generator = BudgetControlType.SEQ, strategy = GenerationType.SEQUENCE)
	private Long id;
	//@Audited
	private String value;  
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}