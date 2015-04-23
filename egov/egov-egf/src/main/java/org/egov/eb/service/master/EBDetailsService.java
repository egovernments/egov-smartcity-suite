package org.egov.eb.service.master;

import java.util.List;

import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.eb.domain.master.entity.EBDetails;
import org.egov.eb.utils.EBConstants;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;

public class EBDetailsService{ 
//extends PersistenceService<EBDetails, Long>{
	//This fix is for Phoenix Migration.
	private static String EBDETAILS_BY_CONSUMER = "from EBDetails where ebConsumer = :consumer";
	private static String EBDETAILS_BY_CONSUMER_AND_STATUSCODE  = EBDETAILS_BY_CONSUMER + " and month = :month " +
			"and status.code <> :statusCode";

	public boolean hasValidEBDetails(Long consumerId) {
		
			boolean hasValidEBDetails=false;
			if (null!= consumerId)
			{
				 List<Long> list =HibernateUtil.getCurrentSession().createQuery(" select count(id) from  EBDetails " +
				 		"where ebConsumer.id=:consumenrId " +
				 		"and status.code!=:status")
				 .setLong("consumenrId", consumerId)
				 .setString("status","Cancelled").list();
				 if(list.get(0)==0)
				 {
					 hasValidEBDetails = false;
				 }
				 else
				 {
				     hasValidEBDetails = true;
				 }
				 
			}
			return hasValidEBDetails;
		
	}
	
	/**
	 * Gives the <code> EBDetails </code> for the given <code> EBConsumer </code>
	 * 
	 * @param consumer
	 * @return <code> EBDetails </code>
	 */
	public EBDetails getEBDetailsByConsumer(EBConsumer consumer) {
		return (EBDetails)HibernateUtil.getCurrentSession().createQuery(EBDETAILS_BY_CONSUMER)
				.setEntity("consumer", consumer)
				.list().get(0);
	}
	
	/**
	 * Gives the <code> EBDetails </code> for the given <code> EBConsumer </code> and <code> EgwStatus.code <code>
	 * 
	 * <p>
	 * <li>RECIEVED - Received the bill info from webservice </li>
	 * <li>CREATED - Created the bill in egov system </li>
	 * <li>REJECTED - TNEB Bill', 'Bill is rejected</li>
	 * <li>CANCELLED - Bill is cancelled </li>
	 * <li>APPROVED - Bill is approved </li>
	 * <li>RECEIVE_FAILED - Failed to receive the bill info from webservice </li>
	 * 
	 * @param consumer
	 * @param statusCode
	 * @return <code> EBDetails </code>
	 */
	@SuppressWarnings("unchecked")
	public EBDetails getValidEBDetailsByConsumer(EBConsumer consumer, Integer month) {
		List<EBDetails> ebDetails =HibernateUtil.getCurrentSession().createQuery(EBDETAILS_BY_CONSUMER_AND_STATUSCODE)
				.setEntity("consumer", consumer)
				.setInteger("month", month)
				.setString("statusCode", EBConstants.CODE_BILLINFO_CANCELLED)
				.list();
		
		if (!ebDetails.isEmpty()) {
			return ebDetails.get(0);
		}
		return null;
	}
	
	//Code Review : Code is compliant 
	@SuppressWarnings("unchecked")
	public List<EBDetails> getAllValidEBDetails(String billingCycle) {
		List<EBDetails> ebDetails =HibernateUtil.getCurrentSession()
				.createQuery(
						"from EBConsumer c left join fetch EBDetails d where d.ebConsumer = c "
								+ "and d.status.code <> :statusCode and c.oddOrEvenBilling = :billingCycle")
				.setString("statusCode", EBConstants.CODE_BILLINFO_CANCELLED)
				.setString("billingCycle", billingCycle)
				.list();
		return ebDetails;
	}
	
	/**
	 * Gives the list of <code> EBDetails </code> for the group [month-year-targetArea-region]
	 * 
	 * @param group
	 * @return list of <code> EBDetails </code>
	 */
	@SuppressWarnings("unchecked")
	public List<EBDetails> getEBDetialsByGroup(String group, String userId) {
		String[] groupParts = group.split("-");
		Integer month = Integer.valueOf(groupParts[0]);
		Integer year = Integer.valueOf(groupParts[1]);
		String targetArea = groupParts[2];
		String region = groupParts[3];
		
		//Position position = new EisCommonService().getPositionByUserId(Integer.valueOf(userId));
		
		List<EBDetails> ebDetails =HibernateUtil.getCurrentSession()
				.createQuery(
						"from EBDetails where month = :month and year(add_months(dueDate, -2)) =:year " +
						"and area.name = :targetArea and ebConsumer.region = :region " +
						"and billAmount > 0 "+		
						"and state.next is null and state.value !=:end and state.owner = :position")
				.setInteger("month", month)
				.setInteger("year", year)
				.setString("targetArea", targetArea)
				.setString("region", region).list();
//				.setString("end", State.END)
//				.setEntity("position", position).list();
		
		return ebDetails;
	}
}
