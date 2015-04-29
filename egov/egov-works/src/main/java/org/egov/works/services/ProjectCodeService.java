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
