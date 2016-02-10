package org.egov.api.controller;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.infra.workflow.inbox.InboxRenderService.RENDER_Y;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.api.adapter.UserAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.support.ui.Inbox;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderService;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/v1.0")
public class EmployeeController extends ApiController {
	
	private static final Logger LOGGER = Logger.getLogger(EmployeeController.class);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a");	

	@Autowired
    private TokenStore tokenStore;
	
	@Autowired
    private PersistenceService<State, Long> statePersistenceService;

    @Autowired
    private PersistenceService<WorkflowTypes, Long> workflowTypePersistenceService;
    
    @Autowired
    private PositionMasterService posMasterService;
	
	@Autowired
	private SecurityUtils securityUtils;
	

	@Autowired
	InboxRenderServiceDeligate<StateAware> inboxRenderServiceDelegate;
	
	@RequestMapping(value = ApiUrl.EMPLOYEE_INBOX_LIST_WFT_COUNT, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getWorkFlowTypesWithItemsCount(HttpServletRequest request) {
        ApiResponse res = ApiResponse.newInstance();
        try
        {
        	List<Long> ownerpostitions=new ArrayList<Long>();
        	ownerpostitions.add(posMasterService.getPositionByUserId(securityUtils.getCurrentUser().getId()).getId());
        	
        	
        	return res.setDataAdapter(new UserAdapter()).success(getWorkflowTypesWithCount(securityUtils.getCurrentUser().getId(), ownerpostitions));
        }
        catch(Exception ex)
        {
            LOGGER.error("EGOV-API ERROR ",ex);
        	return res.error(getMessage("server.error"));
        }
    }
	
	@RequestMapping(value = ApiUrl.EMPLOYEE_INBOX_LIST_FILTER_BY_WFT, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getInboxListByWorkFlowType(@PathVariable final String workFlowType,@PathVariable final int resultsFrom, @PathVariable final int resultsTo) {
        ApiResponse res = ApiResponse.newInstance();
        try
        {
        	List<Long> ownerpostitions=new ArrayList<Long>();
        	ownerpostitions.add(posMasterService.getPositionByUserId(securityUtils.getCurrentUser().getId()).getId());
        	return res.setDataAdapter(new UserAdapter()).success(createInboxData(getWorkflowItemsByUserAndWFType(securityUtils.getCurrentUser().getId(), ownerpostitions, workFlowType, resultsFrom, resultsTo)));
        }
        catch(Exception ex)
        {
            LOGGER.error("EGOV-API ERROR ",ex);
        	return res.error(getMessage("server.error"));
        }
    }

	
	// --------------------------------------------------------------------------------//
    /**
     * Clear the session
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = ApiUrl.EMPLOYEE_LOGOUT, method = RequestMethod.POST)
    public ResponseEntity<String> logout(HttpServletRequest request, OAuth2Authentication authentication) {
    	try
    	{
	        OAuth2AccessToken token = tokenStore.getAccessToken(authentication);
	        if (token == null) {
	            return ApiResponse.newInstance().error(getMessage("msg.logout.unknown"));
	        }
	
	        tokenStore.removeAccessToken(token);
	        return ApiResponse.newInstance().success("",getMessage("msg.logout.success"));
    	}
        catch(Exception ex)
        {
            LOGGER.error("EGOV-API ERROR ",ex);
        	return ApiResponse.newInstance().error(getMessage("server.error"));
        }
    }
    
    
    
    /* DATA RELATED FUCNTIONS */
    
	private List<Inbox> createInboxData(final List<StateAware> inboxStates) {
        final List<Inbox> inboxItems = new LinkedList<>();
        inboxStates.sort(byCreatedDate());
        for (final StateAware stateAware : inboxStates) {
            final State state = stateAware.getCurrentState();
            final WorkflowTypes workflowTypes = getWorkflowType(stateAware.getStateType());
            final Inbox inboxItem = new Inbox();
            inboxItem.setId(InboxRenderService.GROUP_Y.equals(workflowTypes.getGroupYN()) ? EMPTY : state.getId() + "#" + workflowTypes.getId());
            inboxItem.setDate(DATE_FORMATTER.print(new DateTime(state.getCreatedDate())));
            inboxItem.setSender(state.getSenderName());
            inboxItem.setTask(isBlank(state.getNatureOfTask()) ? workflowTypes.getDisplayName() : state.getNatureOfTask());
            final String nextAction = inboxRenderServiceDelegate.getNextAction(state);
            inboxItem.setStatus(state.getValue() + (isBlank(nextAction) ? EMPTY : " - " + nextAction));
            inboxItem.setDetails(isBlank(stateAware.getStateDetails()) ? EMPTY : stateAware.getStateDetails());
            inboxItem.setLink(workflowTypes.getLink().replace(":ID", stateAware.myLinkId()));
            inboxItems.add(inboxItem);
        }
        return inboxItems;
    }
	
	private Comparator<? super StateAware> byCreatedDate() {
        return (stateAware_1, stateAware_2) -> {
            int returnVal = 1;
            if (stateAware_1 == null)
                returnVal = stateAware_2 == null ? 0 : -1;
            else if (stateAware_2 == null)
                returnVal = 1;
            else {
                final Date first_date = stateAware_1.getState().getCreatedDate();
                final Date second_date = stateAware_2.getState().getCreatedDate();
                if (first_date.after(second_date))
                    returnVal = -1;
                else if (first_date.equals(second_date))
                    returnVal = 0;
            }
            return returnVal;
        };
    }
    
    @SuppressWarnings("unchecked")
	public List<StateAware> getWorkflowItemsByUserAndWFType(final Long userId, final List<Long> owners, final String workFlowType, final int resultsFrom, final int resultsTo) throws HibernateException, ClassNotFoundException {
			return this.statePersistenceService.getSession().createCriteria(Class.forName(getWorkflowType(workFlowType).getTypeFQN()))
					.setFirstResult(resultsFrom)
					.setMaxResults(resultsTo)
			        .setFetchMode("state", FetchMode.JOIN).createAlias("state", "state")
			        .setFlushMode(FlushMode.MANUAL).setReadOnly(true).setCacheable(true)
			        .add(Restrictions.eq("state.type", workFlowType))
			        .add(Restrictions.in("state.ownerPosition.id", owners))
			        .add(Restrictions.ne("state.status", StateStatus.ENDED))
			        .add(Restrictions.not(Restrictions.conjunction().add(Restrictions.eq("state.status", StateStatus.STARTED))
			                .add(Restrictions.eq("createdBy.id", userId)))).addOrder(Order.desc("state.createdDate"))
			                .list();
		
   }
   
   public List<HashMap<String, Object>> getWorkflowTypesWithCount(final Long userId, final List<Long> ownerPostitions) {
        
        	List<HashMap<String, Object>> workFlowTypesWithItemsCount=new ArrayList<HashMap<String,Object>>();
        	Query query = this.workflowTypePersistenceService.getSession().createQuery("select type, count(type) from State  where ownerPosition.id in (:ownerPositions) and status <> :statusEnded and NOT (status <> :statusStarted and createdBy.id <> :userId) group by type");
        	query.setParameterList("ownerPositions", ownerPostitions);
            query.setParameter("statusEnded", StateStatus.ENDED);
            query.setParameter("statusStarted", StateStatus.STARTED);
            query.setParameter("userId", userId);
        	
            List<Object[]> result=query.list();
            for(Object[] rowObj:result)
            {
            	HashMap<String, Object> workFlowType=new HashMap<String, Object>();
            	workFlowType.put("workflowtype", rowObj[0]);
            	workFlowType.put("inboxlistcount", rowObj[1]);
            	workFlowType.put("workflowtypename", getWorkflowType(String.valueOf(rowObj[0])).getDisplayName());
            	workFlowTypesWithItemsCount.add(workFlowType);
            }
            return workFlowTypesWithItemsCount;
            
			/*Number count = (Number) this.workflowTypePersistenceService.getSession().createCriteria(State.class)
			        .setFlushMode(FlushMode.MANUAL).setReadOnly(true).setCacheable(true)
			        .setProjection(Projections.rowCount())
			        .add(Restrictions.in("ownerPosition.id", owners))
			        .add(Restrictions.ne("status", StateStatus.ENDED))
			        .add(Restrictions.not(Restrictions.conjunction().add(Restrictions.eq("status", StateStatus.STARTED))
			                .add(Restrictions.eq("createdBy.id", userId)))).addOrder(Order.desc("createdDate")).setProjection(Projections.groupProperty("type")).uniqueResult();*/
    }
    
    @Transactional(readOnly=true)
    public WorkflowTypes getWorkflowType(final String wfType) {
        WorkflowTypes workflowType = (WorkflowTypes) this.workflowTypePersistenceService.getSession().createCriteria(WorkflowTypes.class)
                .add(Restrictions.eq("renderYN", RENDER_Y)).add(Restrictions.eq("type", wfType))
                .setReadOnly(true).uniqueResult();
        return workflowType;
    }

}
