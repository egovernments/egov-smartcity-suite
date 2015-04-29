/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.estimate;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.infstr.ValidationError;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.works.models.masters.ScheduleOfRate;

public class EstimateTemplateActivity  extends BaseModel{
	
	private EstimateTemplate estimateTemplate;
	
	private ScheduleOfRate schedule;
	
	@Valid 
	private NonSor nonSor;
	
	private EgUom uom;
	
	private Money rate=new Money(0.0);

	public EstimateTemplate getEstimateTemplate() {
		return estimateTemplate;
	}

	public void setEstimateTemplate(EstimateTemplate estimateTemplate) {
		this.estimateTemplate = estimateTemplate;
	}

	public ScheduleOfRate getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduleOfRate schedule) {
		this.schedule = schedule;
	}

	public NonSor getNonSor() {
		return nonSor;
	}

	public void setNonSor(NonSor nonSor) {
		this.nonSor = nonSor;
	}

	public EgUom getUom() {
		return uom;
	}

	public void setUom(EgUom uom) {
		this.uom = uom;
	}

	public Money getRate() {
		return rate;
	}

	public void setRate(Money rate) {
		this.rate = rate;
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		/*if (rate.getValue() <= 0.0) {
			validationErrors.add(new ValidationError("estimateTemplateActivity.rate.not.null", "estimateTemplateActivity.rate.not.null"));
		}*/
		if(nonSor!=null && (nonSor.getUom()==null||nonSor.getUom().getId()==null ||nonSor.getUom().getId()==0)){
			validationErrors.add(new ValidationError("estimateTemplateActivity.nonsor.invalid", "estimateTemplateActivity.nonsor.invalid"));
		}
		return validationErrors;
	}

}
