/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.restapi.service;

import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.model.dashboard.CollIndexTableData;
import org.egov.restapi.model.dashboard.CollectionIndexDetails;
import org.egov.restapi.model.dashboard.CollectionTrend;
import org.egov.restapi.model.StateCityInfo;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

/**
 * Service to provide apis for CM Dashboard
 */

@Service
public class DashboardService {

	@PersistenceContext
    private EntityManager entityManager;
	
	/**
	 * Gives the State-City information across all ULBs
	 * @return List
	 */
	public List<StateCityInfo> getStateCityDetails(){
		String query = "select regionname as region, districtname as district, city as city, grade as grade, citycode as ulbcode from public.statecityinfo order by city ";
		SQLQuery sqlQuery = entityManager.unwrap(Session.class).createSQLQuery(query);
		sqlQuery.addScalar("region", StandardBasicTypes.STRING);
		sqlQuery.addScalar("district", StandardBasicTypes.STRING);
		sqlQuery.addScalar("city", StandardBasicTypes.STRING);
		sqlQuery.addScalar("grade", StandardBasicTypes.STRING);
		sqlQuery.addScalar("ulbCode", StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(StateCityInfo.class));
		List<StateCityInfo> stateCityDetails = sqlQuery.list();
		return stateCityDetails;
	}
	
	/**
	 * Gives the Collection Index details across all ULBs
	 * @return CollectionIndexDetails
	 */
	public CollectionIndexDetails getCollectionIndexDetails(){
		//Temporarily all values are being hard coded, actual values will be read from the elastic search index later
		CollectionIndexDetails collectionIndexDetails = new CollectionIndexDetails();
		collectionIndexDetails.setTodayColl(BigDecimal.valueOf(156.583));
		collectionIndexDetails.setCytdColl(BigDecimal.valueOf(32499.615));
		collectionIndexDetails.setCytdDmd(BigDecimal.valueOf(51338.92));
		collectionIndexDetails.setTotalDmd(BigDecimal.valueOf(123213.409));
		collectionIndexDetails.setLytdColl(BigDecimal.valueOf(16727.998));

		prepareCollectionTrends(collectionIndexDetails);
		prepareCollIndexData(collectionIndexDetails);
		
		ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
		collectionIndexDetails.setErrorDetails(errorDetails);
			
		return collectionIndexDetails;
	}

	public void prepareCollIndexData(CollectionIndexDetails collectionIndexDetails) {
		CollIndexTableData collIndData = new CollIndexTableData();
		List<CollIndexTableData> collIndexResponseList = new ArrayList<>();
		collIndData.setRegionName("ANANTAPUR");
		collIndData.setTotalDmd(BigDecimal.valueOf(25204.194));
		collIndData.setCytdDmd(BigDecimal.valueOf(12602.097));
		collIndData.setCytdColl(BigDecimal.valueOf(8326.337));
		collIndData.setCytdBalDmd(BigDecimal.valueOf(4558.5));
		collIndData.setLytdColl(BigDecimal.valueOf(2558.5));
		collIndexResponseList.add(collIndData);

		collIndData = new CollIndexTableData();
		collIndData.setRegionName("GUNTUR");
		collIndData.setTotalDmd(BigDecimal.valueOf(22639.184));
		collIndData.setCytdDmd(BigDecimal.valueOf(11319.592));
		collIndData.setCytdColl(BigDecimal.valueOf(7960.525));
		collIndData.setCytdBalDmd(BigDecimal.valueOf(3595.227));
		collIndData.setLytdColl(BigDecimal.valueOf(1995.227));
		collIndexResponseList.add(collIndData);
		
		collIndData = new CollIndexTableData();
		collIndData.setRegionName("RAJAHMUNDRY");
		collIndData.setTotalDmd(BigDecimal.valueOf(19053.534));
		collIndData.setCytdDmd(BigDecimal.valueOf(9526.767));
		collIndData.setCytdColl(BigDecimal.valueOf(6399.553));
		collIndData.setCytdBalDmd(BigDecimal.valueOf(3313.139));
		collIndData.setLytdColl(BigDecimal.valueOf(2113.139));
		collIndexResponseList.add(collIndData);

		collIndData = new CollIndexTableData();
		collIndData.setRegionName("VISAKHAPATNAM");
		collIndData.setTotalDmd(BigDecimal.valueOf(35780.93));
		collIndData.setCytdDmd(BigDecimal.valueOf(17890.465));
		collIndData.setCytdColl(BigDecimal.valueOf(9813.2));
		collIndData.setCytdBalDmd(BigDecimal.valueOf(8348.316));
		collIndData.setLytdColl(BigDecimal.valueOf(4348.316));
		collIndexResponseList.add(collIndData);
		collectionIndexDetails.setResponseDetails(collIndexResponseList);
	}

	public void prepareCollectionTrends(CollectionIndexDetails collectionIndexDetails) {
		CollectionTrend collTrend = new CollectionTrend();
		List<CollectionTrend> collTrendsList = new ArrayList<>();
		collTrend.setMonth("April");
		collTrend.setPyColl(BigDecimal.valueOf(90.10));
		collTrend.setLyColl(BigDecimal.valueOf(101.31));
		collTrend.setCyColl(BigDecimal.valueOf(150.41));
		collTrendsList.add(collTrend);
		collTrend = new CollectionTrend();
		collTrend.setMonth("May");
		collTrend.setPyColl(BigDecimal.valueOf(190.20));
		collTrend.setLyColl(BigDecimal.valueOf(200.11));
		collTrend.setCyColl(BigDecimal.valueOf(250.01));
		collTrendsList.add(collTrend);
		collTrend = new CollectionTrend();
		collTrend.setMonth("June");
		collTrend.setPyColl(BigDecimal.valueOf(280.02));
		collTrend.setLyColl(BigDecimal.valueOf(300.04));
		collTrend.setCyColl(BigDecimal.valueOf(360.52));
		collTrendsList.add(collTrend);
		collTrend = new CollectionTrend();
		collTrend.setMonth("July");
		collTrend.setPyColl(BigDecimal.valueOf(375.98));
		collTrend.setLyColl(BigDecimal.valueOf(380.95));
		collTrend.setCyColl(BigDecimal.valueOf(400.92));
		collTrendsList.add(collTrend);
		collTrend = new CollectionTrend();
		collTrend.setMonth("August");
		collTrend.setPyColl(BigDecimal.valueOf(400.92));
		collTrend.setLyColl(BigDecimal.valueOf(450.21));
		collTrend.setCyColl(BigDecimal.valueOf(500.25));
		collTrendsList.add(collTrend);
		collTrend = new CollectionTrend();
		collTrend.setMonth("September");
		collTrend.setPyColl(BigDecimal.valueOf(450.93));
		collTrend.setLyColl(BigDecimal.valueOf(550.96));
		collTrend.setCyColl(BigDecimal.valueOf(600.23));
		collTrendsList.add(collTrend);
		collectionIndexDetails.setCollTrends(collTrendsList);
	}
}
