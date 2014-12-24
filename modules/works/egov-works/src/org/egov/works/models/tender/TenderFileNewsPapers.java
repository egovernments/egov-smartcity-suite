package org.egov.works.models.tender;

import org.egov.infstr.models.BaseModel;
import org.egov.works.models.masters.NewsPaper;


public class TenderFileNewsPapers extends BaseModel{
		
	private TenderFile tenderFile;
	
	private NewsPaper newsPaper;

	public TenderFile getTenderFile() {
		return tenderFile;
	}

	public void setTenderFile(TenderFile tenderFile) {
		this.tenderFile = tenderFile;
	}

	public NewsPaper getNewsPaper() {
		return newsPaper;
	}

	public void setNewsPaper(NewsPaper newsPaper) {
		this.newsPaper = newsPaper;
	}
	
}
