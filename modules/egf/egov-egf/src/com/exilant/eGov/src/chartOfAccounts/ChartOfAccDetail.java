/*
 * Created on Mar 4, 2005
 *
 * To change the template for this generated file go to
 * Window - Prences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.chartOfAccounts;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import com.exilant.GLEngine.ChartOfAccounts;
import org.egov.infstr.utils.EGovConfig;
import com.exilant.eGov.src.domain.ChartOfAccountDetail;
import com.exilant.eGov.src.domain.ChartOfAccts;
import com.exilant.eGov.src.transactions.RoleRuleValidate;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.*;
import org.hibernate.jdbc.Work;

public class ChartOfAccDetail extends AbstractTask implements AccountCodeRuleData{
	
	private final static Logger LOGGER=Logger.getLogger(ChartOfAccDetail.class);
	HashSet hs=new HashSet();
	int coaId;
	public Integer roleId=null;
	public Integer actionId=null;
	private static final String EXILRPRRROR="exilRPError" ;
	private static final String CHARTOFACCGLCODE="chartOfAccounts_glCode";
	private static final String CHARTOFACCTYPE= "chartOfAccounts_accType1";
	private static final String CHARTOFACCID = "chartOfAccounts_ID";
	public String setQuotes(String str){
		return str.replaceAll("'","''");
		 
	}
	public void execute(final String taskName,
			final String gridName,
			final DataCollection dc,
			Connection conn,
			final boolean erroOrNoData,
			final boolean gridHasColumnHeading,final String prefix) throws TaskFailedException{
		
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection conn) throws SQLException {

				ChartOfAccts chart = new ChartOfAccts();
				
				String glcode=dc.getValue(CHARTOFACCGLCODE);
				hs.add(glcode);
				try{
					//Session session = HibernateUtil.getCurrentSession();
					//HibernateUtil.beginTransaction();
					LOGGER.debug("glcode>>>>>>>   "+hs);
					Statement st=conn.createStatement();
					ResultSet rset=st.executeQuery("select b.id_role from eg_user a,eg_roles b,eg_userrole c where a.id_user=c.id_user and b.id_role=c.id_role and a.id_user="+dc.getValue("egUser_id"));
						if(rset.next()){
							 roleId=Integer.parseInt(rset.getString(1));

					}
						RoleRuleValidate rv=new RoleRuleValidate();
						//ChartOfAccDetail coa=new ChartOfAccDetail();
						actionId= Integer.valueOf((Integer.parseInt(dc.getValue("actionId"))));
						dc.addValue("actionId",actionId);
						LOGGER.info("Connection in RBAC 1:"+conn.isClosed());
						rv.validateAction(roleId,actionId,ChartOfAccDetail.this);
						LOGGER.info("Connection in RBAC..:"+conn.isClosed());
				}
				catch (Exception e) {
					HibernateUtil.rollbackTransaction();
					LOGGER.debug("Exp="+e.getMessage());
					dc.addMessage(EXILRPRRROR,"Invalid Action : " + e.getMessage());
					
				}
				//Get the connection from current session as its closed above.		
				chart.setGLCode(dc.getValue(CHARTOFACCGLCODE));
				chart.setName(setQuotes(dc.getValue("chartOfAccounts_name")));
				chart.setDescription(setQuotes(dc.getValue("chartOfAccounts_description")));
				chart.setIsActiveForPosting(dc.getValue("coa_isActiveForPosting"));
				chart.setClassification(dc.getValue("chartOfAccounts_classification"));
				//String purpose=(String)dc.getValue("chartOfAccounts_purpose");  
				if(dc.getValue("chartOfAccounts_purpose").length()>0){
					LOGGER.info("inside if purpose id");
				    chart.setPurposeId(dc.getValue("chartOfAccounts_purpose"));
				 }
				else{
					chart.setPurposeId(null);  
				}
				chart.setType(dc.getValue("chartOfAccounts_type"));
				chart.setFunctionReqd(dc.getValue("chartOfAccounts_funcReqd"));
				chart.setParentId(dc.getValue("chartOfAccounts_parentID"));
				chart.setModifiedBy(dc.getValue("egUser_id"));
		        
		        String budgetCheck=(String)dc.getValue("budgetCheckReqd");
		        //Check glcode is of detail code then check for budget check required or not
		        boolean  detailAccountCode=isDetailAccountCode(glcode,dc); //LOGGER.debug("detailAccountCode:"+detailAccountCode);
		        if( budgetCheck.equalsIgnoreCase("1") && detailAccountCode )
		        {
		            chart.setBudgetCheckReqd(budgetCheck); LOGGER.debug("inside if maxlength ");
		        }
		        else if( budgetCheck.equalsIgnoreCase("1") && !detailAccountCode )
		        { 
		            dc.addMessage(EXILRPRRROR, "\"Budget Check Reqd\" cannot be checked for non detail Account code: "+glcode);
		            throw new SQLException();
		        }else if( !budgetCheck.equalsIgnoreCase("1") && detailAccountCode )
		        {    
		            chart.setBudgetCheckReqd("0");
		        }
		        
		        if(dc.getValue(CHARTOFACCID).equalsIgnoreCase("")){

					if(!isMaxLength(glcode,dc))
						{
							throw new SQLException();
						}
					try{
						LOGGER.info("Connection before isUniqueGL:"+conn.isClosed());
					}catch(Exception e){LOGGER.error("Exp in check conn"+e);}
					if(!isUniqueGL(glcode,dc, conn))
						{
							throw new SQLException();
						}
					try{
						chart.insert(conn);
						coaId=chart.getId();
						dc.addValue(CHARTOFACCID, String.valueOf(chart.getId()));
						String str1=(String)dc.getValue(CHARTOFACCTYPE);
						if(!str1.equals("0") && str1.length()!=0 && str1!=null)
						{
							ChartOfAccountDetail chartDetail = new ChartOfAccountDetail();
							chartDetail.setGLCodeId(String.valueOf(coaId));
							chartDetail.setDetailTypeId(dc.getValue(CHARTOFACCTYPE));
							chartDetail.insert(conn);

						}
						String str2=(String)dc.getValue("chartOfAccounts_funcReqd");
						if("1".equals(str2))
						{
							Statement stmt=conn.createStatement();
							String query="UPDATE chartofaccounts SET functionreqd=1 where glcode LIKE '"+dc.getValue(CHARTOFACCGLCODE)+"%'";
							int i=stmt.executeUpdate(query);
							LOGGER.debug("number of rows updated "+i);

						}
						
						ChartOfAccounts.getInstance().reLoadAccountData();
						dc.addMessage("eGovSuccess","Insertion of Account code");
					}
					catch(Exception e){
						LOGGER.debug(" Error in Insertion in chartofaccounts "+e.toString());
						dc.addMessage("eGovFailure","Adding Glcode");
						LOGGER.debug("Exp="+e.getMessage());
						throw new SQLException();
					}
				}
				else{
					chart.setId(dc.getValue(CHARTOFACCID));
					try{
						String glCode="";
						Statement stat = conn.createStatement();
						ResultSet rs=null;
						stat=conn.createStatement();
						rs=stat.executeQuery("select glcode as \"code\" from chartofaccounts where id="+dc.getValue(CHARTOFACCID));
						if(rs.next())
						{
								glCode=rs.getString("code");
						}
						stat.close();

						chart.update(conn);
						stat = conn.createStatement();
						String sql = "DELETE FROM chartofaccountdetail WHERE glcodeid="+dc.getValue(CHARTOFACCID);
						stat.execute(sql);
						stat.close();
						String str1=(String)dc.getValue(CHARTOFACCTYPE);
						if(!str1.equals("0") && str1.length()!=0 && str1!=null)
						{
							ChartOfAccountDetail chartDetail = new ChartOfAccountDetail();
							chartDetail.setGLCodeId(dc.getValue(CHARTOFACCID));
							chartDetail.setDetailTypeId(dc.getValue(CHARTOFACCTYPE));
							chartDetail.insert(conn);

						}


						stat=conn.createStatement();
						String query="UPDATE chartofaccounts SET functionreqd=0 where glcode LIKE '"+glCode+"%'";
						stat.executeUpdate(query);
						stat.close();
						String str2=(String)dc.getValue("chartOfAccounts_funcReqd");
						if("1".equals(str2))
						{
						//	Statement stmt=conn.createStatement();
						//	String query1="UPDATE chartofaccounts SET functionreqd=1 where glcode LIKE '"+dc.getValue(CHARTOFACCGLCODE)+"%'";
							//int i=stmt.executeUpdate(query1);


						}
						ChartOfAccounts.getInstance().reLoadAccountData();
						HibernateUtil.release(null, rs);
					}
					catch(Exception e){
						LOGGER.debug(" Error in updation " + e.toString());
						dc.addMessage("eGovFailure","Adding Glcode");
						LOGGER.debug("Exp="+e.getMessage());
						throw new SQLException();
					}
				}
			}
		});
	}
	public Set getIeList()
	{
		return hs;
	}

	public void setIeList(Set ieList)
	{

	}

	 public boolean isUniqueGL(String glcode,DataCollection dc, Connection conn){
		boolean isUnique = false;
		try{
			Statement st = conn.createStatement();
			ResultSet rs ;
			st = conn.createStatement();
			LOGGER.debug("SELECT id FROM CHARTOFACCOUNTS WHERE glcode = '" + glcode + "'");
			rs = st.executeQuery("SELECT id FROM CHARTOFACCOUNTS WHERE glcode = '" + glcode + "'");
			if(rs.next()){
				dc.addMessage(EXILRPRRROR, "Duplicate Glcode ");
			}
			else{
				isUnique = true;
			}
			rs.close();
			st.close();

		}catch(SQLException ex){
			dc.addMessage(EXILRPRRROR, "DataBase Error(isUniqueGL) : " + ex.toString());
			LOGGER.debug("EXP="+ex.getMessage());
		}
		return isUnique;
	}

	 public boolean isMaxLength(String glcode,DataCollection dc){
		boolean isMax = false;
		try{
			String glcodelength=EGovConfig.getProperty("egf_config.xml","glcodeMaxLength","","AccountCode");
			int gl=glcode.length();
			Integer.parseInt(glcodelength);
			LOGGER.debug("glcodelength ==="+gl+"glcodelength from xml=="+Integer.parseInt(glcodelength));
			if(gl>Integer.parseInt(glcodelength))
			{
				dc.addMessage(EXILRPRRROR, "Glcode length should be Less than or Equal to "+Integer.parseInt(glcodelength));
			}
			else {
				isMax=true;
			}


		}catch(Exception ex){

			LOGGER.debug("EXP="+ex.getMessage());
		}
		return isMax;
	}

     public boolean isDetailAccountCode(String glcode,DataCollection dc){
            boolean isDetailCode = false;
            try{LOGGER.debug("glcodelength === "+glcode);
                String glcodelength=EGovConfig.getProperty("egf_config.xml","glcodeMaxLength","","AccountCode");
                int gl=glcode.length();
                LOGGER.debug("glcodelength ==="+gl+"glcodelength from xml=="+Integer.parseInt(glcodelength));
                if(gl!=Integer.parseInt(glcodelength))
                {
                   isDetailCode=false;
                }
                else {
                	isDetailCode=true;
                }
                
                }catch(Exception ex){
    
                	LOGGER.debug("EXP="+ex.getMessage());
                }
                return isDetailCode;
            }


}

