package org.egov.pgr.repository.elasticsearch;

import java.util.HashMap;

import org.egov.pgr.entity.elasticsearch.ComplaintDashBoardRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;



public interface ComplaintIndexCustomRepository {

	public HashMap<String, Object> findAllGrievanceByFilter(ComplaintDashBoardRequest complaintDashBoardRequest, BoolQueryBuilder query,String grouByField);
	
}