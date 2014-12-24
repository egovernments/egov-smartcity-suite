package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.models.Money;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.tender.utils.TenderConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.services.AbstractEstimateService;

public class AjaxTenderFileAction extends BaseFormAction {
	private static final String ESTIMATE_LIST = "estList";
	private static final String INDENT_LIST = "indentList";
	private List<AbstractEstimate> abstractEstimateList=new ArrayList<AbstractEstimate>();
	private List<Indent> indentList=new ArrayList<Indent>();
	private AbstractEstimateService abstractEstimateService;
	private GenericTenderService genericTenderService;
	private Money worktotalValue;
	private String estId;
	private String indntId;
	private Long tenderFileId;
	private String tenderNoticeNumber;
	public String estimateList() {
		if(StringUtils.isNotBlank(estId)){
			abstractEstimateList=abstractEstimateService.getAbEstimateListById(estId);		
			setWorktotalValue(abstractEstimateService.getWorkValueExcludingTaxesForEstList(abstractEstimateList));
		}
		return ESTIMATE_LIST;
	}
	
	public String indentList() {
		if(StringUtils.isNotBlank(indntId)){
			indentList=getIndentListById(indntId);	
		}
		return INDENT_LIST;
	}
	
	public List<Indent> getIndentListById(String indentId) {
		String[] indValues = indentId.split("`~`");
		Long[] indIdLong=new Long[indValues.length];
		Set<Long> indentIdentifierSet = new HashSet<Long>();
		int j=0;
		for(int i=0;i<indValues.length;i++){
			if(StringUtils.isNotBlank(indValues[i])){
				indIdLong[j] = Long.valueOf(indValues[i]);
				j++;
			}
		}
		indentIdentifierSet.addAll(Arrays.asList(indIdLong));
		return (List<Indent>)getPersistenceService().findAllByNamedQuery("getIndentListById", indentIdentifierSet);
	}
	
	public String checkTenderNotice() {
		TenderFile tenderFile=(TenderFile) getPersistenceService().find("from org.egov.works.models.tender.TenderFile tf where tf.id=?", tenderFileId);
		TenderNotice tenderNotice=genericTenderService.getTenderNoticeByTenderFile(tenderFile);
		if(tenderNotice!=null && !TenderConstants.TENDERNOTICE_CANCELLED.equalsIgnoreCase(tenderNotice.getStatus().getCode())) {
			tenderNoticeNumber=tenderNotice.getNumber();
		}
		else {
			tenderNoticeNumber="";
		}
		return "tenderNotice";
	}
	
	public String getTenderNoticeNumber() {
		return tenderNoticeNumber;
	}

	public Money getWorktotalValue() {
		return worktotalValue;
	}

	public void setWorktotalValue(Money worktotalValue) {
		this.worktotalValue = worktotalValue;
	}

	public String getEstId() {
		return estId;
	}

	public void setEstId(String estId) {
		this.estId = estId;
	}

	public Object getModel() {
		return null;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public List<AbstractEstimate> getAbstractEstimateList() {
		return abstractEstimateList;
	}

	public void setAbstractEstimateList(List<AbstractEstimate> abstractEstimateList) {
		this.abstractEstimateList = abstractEstimateList;
	}

	public String getIndntId() {
		return indntId;
	}

	public void setIndntId(String indntId) {
		this.indntId = indntId;
	}

	public List<Indent> getIndentList() {
		return indentList;
	}

	public void setIndentList(List<Indent> indentList) {
		this.indentList = indentList;
	}

	public GenericTenderService getGenericTenderService() {
		return genericTenderService;
	}

	public Long getTenderFileId() {
		return tenderFileId;
	}

	public void setTenderFileId(Long tenderFileId) {
		this.tenderFileId = tenderFileId;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}	

}
