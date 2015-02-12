package org.egov.pgr.repository;

import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.lib.rjbac.role.RoleImpl;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintStatusMapping;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ComplaintStatusMappingRepository extends HibernateRepository<ComplaintStatusMapping> {
	
	@Autowired
	 public ComplaintStatusMappingRepository(SessionFactory sessionFactory) {
	        super(sessionFactory, ComplaintStatusMapping.class);  
	    }

		public List<ComplaintStatus> getStatusByRoleAndCurrentStatus(RoleImpl role,ComplaintStatus status)
		{
			StringBuffer query=new StringBuffer(100);
			if(role==null)
			{
				throw new EGOVRuntimeException("Role is null for selecting Complaint Status");
			}
			
			if(status==null)
			{
				query.append("select csp.status from ComplaintStatusMapping  csp where csp.role=:role  order by csp.orderNo ");
			}else
			{
				//TODO define what should be returned if the current status also passed
				query.append("select csp.status from ComplaintStatusMapping  csp where csp.role=?   order by "
						+ " csp.orderNo ");
			}
			Query statusQuery = sessionFactory.getCurrentSession().createQuery(query.toString());	
			statusQuery.setEntity("role",role);

				
			return (List<ComplaintStatus>) statusQuery.list();
		}

	
}
