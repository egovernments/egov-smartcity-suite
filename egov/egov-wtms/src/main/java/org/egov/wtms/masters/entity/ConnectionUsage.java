package org.egov.wtms.masters.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.search.domain.Searchable;

@Entity
@Searchable
@Table(name = "egwtr_connectionusage")
@SequenceGenerator(name = ConnectionUsage.SEQ_CONNECTIONUSAGE, sequenceName = ConnectionUsage.SEQ_CONNECTIONUSAGE, allocationSize = 1)
public class ConnectionUsage extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 8604331107634946265L;
    public static final String SEQ_CONNECTIONUSAGE = "SEQ_EGWTR_CONNECTIONUSAGE";

    @Id
    @GeneratedValue(generator = SEQ_CONNECTIONUSAGE, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    
    
    @NotNull
    @Searchable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usagetype")
    private UsageType usagetype;
    
   
    @Searchable
	private String connectionType;
    

	 public Long getId() {
	        return id;
	    }

	    protected void setId(final Long id) {
	        this.id = id;
	    }

	
	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	 
	public UsageType getUsagetype() {
			return usagetype;
		}
		

	public void setUsagetype(UsageType usagetype) {
			this.usagetype = usagetype;
		}
	
    

}

