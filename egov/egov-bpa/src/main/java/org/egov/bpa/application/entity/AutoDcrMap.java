package org.egov.bpa.application.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_AUTODCRMAP")
@SequenceGenerator(name = AutoDcrMap.SEQ_EGBPA_AUTODCRMAP, sequenceName = AutoDcrMap.SEQ_EGBPA_AUTODCRMAP, allocationSize = 1)
public class AutoDcrMap extends AbstractAuditable {

	private static final long serialVersionUID = 3078684328383202788L;
	public static final String SEQ_EGBPA_AUTODCRMAP = "SEQ_EGBPA_AUTODCRMAP";

	@Id
	@GeneratedValue(generator = SEQ_EGBPA_AUTODCRMAP, strategy = GenerationType.SEQUENCE)
	private Long id;
	@Length(min = 1, max = 128)
	private String autodcrNumber;
	@ManyToOne(cascade = CascadeType.ALL)
	@Valid
	@NotNull
	@JoinColumn(name = "application", nullable = false)
	private Application application;
	@ManyToOne(cascade = CascadeType.ALL)
        @Valid
        @NotNull
        @JoinColumn(name = "letterToParty")
	private LettertoParty letterToParty; 
	private Boolean isActive;

	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getAutodcrNumber() {
		return autodcrNumber;
	}

	public void setAutodcrNumber(final String autodcrNumber) {
		this.autodcrNumber = autodcrNumber;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(final Application application) {
		this.application = application;
	}


	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(final Boolean isActive) {
		this.isActive = isActive;
	}
    public LettertoParty getLetterToParty() {
        return letterToParty;
    }
    public void setLetterToParty(LettertoParty letterToParty) {
        this.letterToParty = letterToParty;
    }

}
