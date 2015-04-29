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
package org.egov.works.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.AssetsForEstimate;
import org.egov.works.models.estimate.ProjectCode;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectCodeService extends PersistenceService<ProjectCode, Long> implements EntityTypeService{
    
        @Autowired
        private PersistenceService<AssetsForEstimate, Long> assetsForEstimateService;
	public List<ProjectCode> getAllActiveEntities(Integer accountDetailTypeId) {
		return findAllBy("from ProjectCode where isActive=1");
	}
	
	public List<ProjectCode> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		String param = "%" + filterKey.toUpperCase() + "%";
		String qry = "select distinct pc from ProjectCode pc " +
				"where isActive=1 and upper(pc.code) like ? " +
				"order by code";
		return (List<ProjectCode>) findPageBy(qry, 0, pageSize,param).getList();
	}


	@Override
	public List getAssetCodesForProjectCode(Integer accountDetailKey)throws ValidationException{

		if(accountDetailKey==null || accountDetailKey<=0){
			throw new ValidationException(Arrays.asList(new ValidationError("projectcode.invalid","Invalid Account Detail Key")));
		}	

		ProjectCode projectCode=find("from ProjectCode where id=?",accountDetailKey.longValue());

		if(projectCode==null){
			throw new ValidationException(Arrays.asList(new ValidationError("projectcode.doesnt.exist","No Project Code exists for given Account Detail Key")));
		}

		if(projectCode.getEstimates()==null || projectCode.getEstimates().size()==0){
			throw new ValidationException(Arrays.asList(new ValidationError("projectcode.no.link.abstractEstimate","Estimate is not linked with given Account Detail Key")));
		}
		
		List<AbstractEstimate> estimates=new ArrayList<AbstractEstimate>(projectCode.getEstimates());
		
		List<AssetsForEstimate> assetValues =estimates.get(0).getAssetValues();
		
		if(assetValues==null || assetValues.size()==0){
			return Collections.EMPTY_LIST;
		}
		else{
			List<String> assetCodes=new ArrayList<String>();
			for(AssetsForEstimate asset:assetValues){
				assetCodes.add(asset.getAsset().getCode());
			}
			return assetCodes;
		}
	}
	
	public List<ProjectCode> getAllActiveProjectCodes(int fundId,Long functionId, int functionaryId, int fieldId, int deptId) {
            String projectCodeQry = null;           
            List<Object> paramList = new ArrayList<Object>();
            Object[] params;
            
            projectCodeQry="select pc from ProjectCode pc where pc in (select ae.projectCode from AbstractEstimate as ae inner join ae.financialDetails as fd where ae.state.value not in('CANCEL','COMP_CERTIFICATE')";
    
            if(fundId!=0){
                    projectCodeQry=projectCodeQry+" and fd.fund.id= ?";
                    paramList.add(fundId);  
            }
            
            if(functionId!=0){
                    projectCodeQry=projectCodeQry+" and fd.function.id= ?";
                    paramList.add(functionId);
            }
            
            if(functionaryId!=0){
                    projectCodeQry=projectCodeQry+" and fd.functionary.id= ?";
                    paramList.add(functionaryId);
            }
            
            if(fieldId!=0){
                    projectCodeQry=projectCodeQry+" and ae.ward.id= ?";
                    paramList.add(fieldId);
            }
            
            if(deptId!=0){
                    projectCodeQry=projectCodeQry+" and ae.executingDepartment.id= ?";
                    paramList.add(deptId);
            }
            projectCodeQry=projectCodeQry+ ")"; 
            
            if(paramList.isEmpty())
                    return findAllBy(projectCodeQry);
            else{
                    params                  = new Object[paramList.size()];
                    params                  = paramList.toArray(params);
                    return findAllBy(projectCodeQry,params);
            } 
    }
    
    public List getAssetListByProjectCode(Long projectCodeId) throws NoSuchObjectException {
            List<String> assetCodeList=new ArrayList<String>();
            ProjectCode pc = find("from ProjectCode where id=?",projectCodeId);
            if(pc==null){
                    throw new NoSuchObjectException("projectcode.notfound");
            }
            List<AssetsForEstimate> assetsForEstimateList = assetsForEstimateService.findAllByNamedQuery("ASSETS_FOR_PROJECTCODE", projectCodeId);
            if(assetsForEstimateList.isEmpty()){
                    throw new NoSuchObjectException("assetsforestimate.projectcode.asset.notfound");
            }
            else{
                    for(AssetsForEstimate assetsForEstimate: assetsForEstimateList){
                            assetCodeList.add(assetsForEstimate.getAsset().getCode());
                    }                       
            }                       
            return assetCodeList;
    }
    
	public List<ProjectCode> validateEntityForRTGS(List<Long> idsList) throws ValidationException {
		 
		 return null;
		 
	 }
	
	 public List<ProjectCode> getEntitiesById(List<Long> idsList) throws ValidationException {
		 
		 return null;
		 
	 }

}
