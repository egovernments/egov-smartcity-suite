package org.egov.pgr.elasticsearch.service.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.pgr.elasticSearch.entity.ComplaintIndex;
import org.egov.pgr.elasticSearch.service.ComplaintIndexService;
import org.egov.pgr.entity.Complaint;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ComplaintIndexingAdvice {

	private static final Logger LOG = LoggerFactory.getLogger(ComplaintIndexingAdvice.class);

	@Autowired
	private ComplaintIndexService complaintIndexService;
	
	@Autowired
    private ApplicationProperties applicationProperties;
	
	@AfterReturning(pointcut = "execution(*  org.egov.pgr.service.ComplaintService.createComplaint (..))", returning = "retVal")
	public void createComplaintIndex(final Object retVal){
		Complaint savedComplaint = (Complaint)retVal;
		final ComplaintIndex savedComplaintIndex = new ComplaintIndex();
		BeanUtils.copyProperties(savedComplaint, savedComplaintIndex);
		try {
			// Indexing complaint here
			complaintIndexService.createComplaintIndex(savedComplaintIndex);
		} catch (NoNodeAvailableException exception) {
			if (applicationProperties.devMode())
				LOG.error("An error occurred, None of the Elastic search nodes are Available ", exception);
			else
				throw exception;
		}
	}

	@AfterReturning(pointcut = "execution(* org.egov.pgr.service.ComplaintService.update (..))", returning = "retVal")
	public void updateComplaintIndex(JoinPoint joinPoint, final Object retVal){
		Complaint savedComplaint = (Complaint)retVal;
		Object[] arguments = joinPoint.getArgs();
		final ComplaintIndex savedComplaintIndex = new ComplaintIndex();
		BeanUtils.copyProperties(savedComplaint, savedComplaintIndex);
		try { 
			// Indexing complaint here
			complaintIndexService.updateComplaintIndex(savedComplaintIndex, Long.valueOf(arguments[1].toString()),arguments[2].toString());
		} catch (NoNodeAvailableException exception) {
			if (applicationProperties.devMode())
				LOG.error("An error occurred, None of the Elastic search nodes are Available ", exception);
			else
				throw exception;
		}
	}
}