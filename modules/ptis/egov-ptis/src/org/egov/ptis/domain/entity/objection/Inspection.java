/**
 * 
 */
package org.egov.ptis.domain.entity.objection;

import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

/**
 * @author manoranjan
 * 
 */
public class Inspection extends BaseModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Objection objection;

    @Length(max = 1024, message = "inspection.remarks.length")
    private String inspectionRemarks;

    private String documentNumber;

    public String getInspectionRemarks() {
        return inspectionRemarks;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setInspectionRemarks(String inspectionRemarks) {
        this.inspectionRemarks = inspectionRemarks;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Objection getObjection() {
        return objection;
    }

    public void setObjection(Objection objection) {
        this.objection = objection;
    }
    
    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder();
    
    	sb.append("ObjectionNo :").append(null!=objection ?objection.getObjectionNumber():" ");
    	sb.append("InspectionRemarks :").append(null!=inspectionRemarks?inspectionRemarks:" ");
    	return sb.toString();
    }
}
