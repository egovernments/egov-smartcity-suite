package org.egov.council.autonumber.impl;

import java.io.Serializable;

import org.egov.council.autonumber.MOMResolutionNumberGenerator;
import org.egov.council.entity.MeetingMOM;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MOMResolutionNumberGeneratorImpl implements MOMResolutionNumberGenerator{
		 private static final String MOM_NUMBER_SEQ = "SEQ_EGCNCL_MOM_NUMBER";
		 
		 @Autowired
		 private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;
		 
		@Override
		public String getNextNumber(MeetingMOM meetingMOM) {
	        final String sequenceName = MOM_NUMBER_SEQ;
	        final String currentYear = DateUtils.currentDateToYearFormat();
	        Serializable sequenceNumber = applicationSequenceNumberGenerator.getNextSequence(sequenceName);
	        final String result = String.format("%d/%s", sequenceNumber, currentYear);
	        return result;
	}

}
