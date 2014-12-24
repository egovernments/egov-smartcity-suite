/* 
 * Created on Aug 28, 2005
 * @author Chiranjeevi
 */
package com.exilant.eGov.src.transactions;
 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.exility.common.TaskFailedException;


public class CommonList
{	Connection connection=null;
	Statement statement=null;
	ResultSet resultset=null;

	TaskFailedException taskExc;
    private static final Logger LOGGER = Logger.getLogger(CommonList.class);
    
    String strfield[]=new String[15];//={};//GET COLUMNS OF THE QUERY. EXAMPLE {"slNo","H","B","C","D","I","E","F","G"};
    String tableHeader[]=new String[15];//{"Sl No","Voucher Date","Voucher Number","Contractor","Work Order Reference","Work Order No","Bill Amount","Passed Amount","Status"};
    /*
     * 0 - default
     * 1 - for Bills search, Journal search and contra search
     * 2 - for payment search 
     */
    int recordSetCondition=0;
    
   public CommonList()
    {
        connection = null;
        statement = null;
        resultset = null;
    }
//function to retrive data from recordset and add to been
    public LinkedList getList(final CommonBean reportBean)throws TaskFailedException 
    {
    	
    	return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<LinkedList>() {

			@Override
			public LinkedList execute(Connection connection) throws SQLException {
				

		    	LOGGER.info("Entered file>>>>>>");
		        LinkedList dataList;
		        int totalCount;
		        ArrayList data;
		        dataList = new LinkedList();
		        
		    //    NumberFormat amtFormatter = new DecimalFormat();
			//	amtFormatter = new DecimalFormat("###############.00");
		       
		       // String toDate1=toDate;
		        
				
				LOGGER.info("strfield:"+strfield.length);
		        String query = getQuery(reportBean); //LOGGER.info("strfield:"+strfield.length);
		        LOGGER.info("query:"+query);
		        //String strfield1[]={"slNo","H","B","C","D","I","E","F","G"};
		       // strfield=strfield1;
				
				try	{ statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		        	resultset = statement.executeQuery(query);
		        }catch(SQLException e){ 
		        	LOGGER.error("error: while executing query:"+e);
		        	
		        }
		        catch(Exception e){
		        	LOGGER.error("Exception in creating statement:"+statement);
		        	
		        }
		        totalCount = 0;
		        data = new ArrayList();
		      //  String   contractor,wocode,fundname,fundsource,id,worksdetailid,type,fundid,fundsourceid,selectToPay;
		   		//Double amount;
		        try
		        {
		            while(resultset.next())
		            {
		                String arr[] = new String[13];
		                totalCount++;
		                for(int i = 0; i < arr.length; i++)
		                {
		                    arr[i] = "";
		                }
		               // LOGGER.info("field.length IN MAIN::"+strfield.length);
		                try{
		                	if(recordSetCondition==1)//for bills,journal and contra search
		                	{
				               for(int i = 0; i < strfield.length; i++)
				                {
				             //   LOGGER.info("arr["+i+"]:"+arr[i]+":::"+"col"+strfield[i]);
				                	if(strfield[i].equals("slNo")) arr[i]=totalCount+"";
				                	else if(strfield[i].equals("B")){// B- vouchernumber 
				                		 arr[i] =  resultset.getString("col"+strfield[i])+"`--`"+resultset.getString("colA"); arr[i]=(arr[i] != null && arr[i].trim().length()>0)?arr[i]:"&nbsp;";
				                	}
				                	else if(strfield[i].equals("E") || strfield[i].equals("F")){//amount field
				                		//arr[i] = resultset.getString("col"+strfield[i]) + "`--`AmtField"; arr[i]=(arr[i] != null && arr[i].trim().length()>0)?arr[i]:"&nbsp;";
				                		arr[i] = resultset.getString("col"+strfield[i]) ; arr[i]=(arr[i] != null && arr[i].trim().length()>0)?arr[i]:"&nbsp;";
				                	}
				                	else
				                	 arr[i] = resultset.getString("col"+strfield[i]); arr[i]=(arr[i] != null && arr[i].trim().length()>0)?arr[i]:"&nbsp;";
				                }
		                	}else if(recordSetCondition==2) //for payments search
		                	{
				                 for(int i = 0; i < strfield.length; i++)
				               {
				                //LOGGER.info("arr["+i+"]:"+arr[i]+":::"+strfield[i]);
				                	if(strfield[i].equals("slNo")) arr[i]=totalCount+"";
				                	 else if(strfield[i].equals("voucherNumber")){// B- vouchernumber 
				               		 arr[i] =  resultset.getString(strfield[i])+"`--`"+resultset.getString("cgNumber"); arr[i]=(arr[i] != null && arr[i].trim().length()>0)?arr[i]:"&nbsp;";
				                	}else if(strfield[i].equals("") ){arr[i] = "&nbsp";
				                		//LOGGER.info("empty column");
				                	}
					               else if(strfield[i].equals("hiddenField") ){
					               	arr[i] = 
					               		"<span style=\"display:none\" name=id >" + resultset.getString("id") +"</span>"+
					               		"<span style=\"display:none\" name=type >" + resultset.getString("type") +"</span>"+
					               		"<span style=\"display:none\" name=amount >" + resultset.getString("amount") +"</span>"+
					               		"<span style=\"display:none\" name=worksdetailid >" + resultset.getString("worksdetailid") +"</span>"+
					               		"<span style=\"display:none\" name=fundid >" + resultset.getString("fundid") +"</span>"+
					               		"<span style=\"display:none\" name=fundsourceid >" + resultset.getString("fundsourceid") +"</span>";
				               		//LOGGER.info("hidden field "+i);
				               		}
				                	else
				                	 arr[i] = resultset.getString(strfield[i]); arr[i]=(arr[i] != null && arr[i].trim().length()>0)?arr[i]:"&nbsp;";
				                	
				                }
		                	}
		                  }catch(Exception e){
		              		LOGGER.error("Exp="+e.getMessage());
		              		
		                  }
		                
		                data.add(arr);
		               
		            }
		            String gridData[][] = new String[data.size() + 1][13];
		            
		            for(int i = 0; i < tableHeader.length; i++)
		            {
		                gridData[0][i] = tableHeader[i];
		                //LOGGER.info("tableHeader[i]:"+tableHeader[i]);
		            }
		           try{ if(tableHeader[0]!=null) reportBean.setField1(tableHeader[0]);
		            if(tableHeader[1]!=null) reportBean.setField2(tableHeader[1]);
		            if(tableHeader[2]!=null) reportBean.setField3(tableHeader[2]);
		            if(tableHeader[3]!=null) reportBean.setField4(tableHeader[3]);
		            if(tableHeader[4]!=null) reportBean.setField5(tableHeader[4]);
		            if(tableHeader[5]!=null) reportBean.setField6(tableHeader[5]);
		            if(tableHeader[6]!=null) reportBean.setField7(tableHeader[6]);
		            if(tableHeader[7]!=null) reportBean.setField8(tableHeader[7]);//LOGGER.info("tableHeader[8]:"+tableHeader[8]);
		            if(tableHeader[8]!=null &&  !tableHeader[8].trim().equals("")) reportBean.setField9(tableHeader[8]);LOGGER.info("22");
		            if(tableHeader[9]!=null) reportBean.setField10(tableHeader[9]);
		            if(tableHeader[10]!=null) reportBean.setField11(tableHeader[10]);
		           }catch(Exception e){
		        	   LOGGER.error(e.getMessage());
		        	 
		           }
		           // LOGGER.info("DATA SIZE():"+data.size());
		            for(int i = 1; i <= data.size(); i++)
		            {
		                gridData[i] = (String[])(String[])data.get(i - 1);//LOGGER.info("gridData[i] :"+gridData[i]);
		            }
		            //LOGGER.info("33");
		            
		            for(int i = 1; i <= data.size(); i++)
		            {
		            	CommonBean reportBean1 = new CommonBean();
			            try{
		            		if(gridData[i][0]!=null)reportBean1.setField1(gridData[i][0]);
			                if(gridData[i][1]!=null)reportBean1.setField2(gridData[i][1]);
			                if(gridData[i][2]!=null)reportBean1.setField3(gridData[i][2]);
			                if(gridData[i][3]!=null)reportBean1.setField4(gridData[i][3]);
			                if(gridData[i][4]!=null)reportBean1.setField5(gridData[i][4]);
			                if(gridData[i][5]!=null)reportBean1.setField6(gridData[i][5]);
			                if(gridData[i][6]!=null)reportBean1.setField7(gridData[i][6]);
			                if(gridData[i][7]!=null)reportBean1.setField8(gridData[i][7]);
			                if(gridData[i][8]!=null)reportBean1.setField9(gridData[i][8]);
			                if(gridData[i][9]!=null)reportBean1.setField10(gridData[i][9]);
			                if(gridData[i][10]!=null)reportBean1.setField11(gridData[i][10]);
			               
			                dataList.add(reportBean1);
			            }catch(Exception e){
			            	  LOGGER.error(e.getMessage());
			            	
			            }
		            }

		            LOGGER.info("Datalist is filled");
		            HibernateUtil.release(statement, resultset);
		        }
		        catch(SQLException ex)
		        {
		            LOGGER.error("ERROR:while retrieving and adding to bean "+ex);
		            
		        }

		        LOGGER.info("Leaving file>>>>>>>");
		        return dataList;
		    
				
				
			}
		});
    	
    	
    }


//function to get the query
    private String getQuery(CommonBean bean) 
    {
    	//String condition[]=new String[size];
    	//String qryVariables[]={
    	String qryString="",condition1="",condition2="",condition3="",condition4="",condition5="",
    	condition6="",condition7="",condition8="",condition9="",condition10="", condition11="";
    	String condition12s0="",condition12s1="",condition12s2="",condition12s3="", condition12s4="";
    try{	
    		if(bean.getInputField1()!=null && !bean.getInputField1().equals(""))
    		{
    			condition1 = " and v.voucherdate >='"+formatDate(bean.getInputField1())+"' ";
    		}
    		if(bean.getInputField2()!=null && !bean.getInputField2().equals(""))
    		{
    			condition2 = " and v.voucherdate <='"+formatDate(bean.getInputField2())+"' ";
    		}
    		//LOGGER.info("bean.getInputField3():"+bean.getInputFieldC());
    		if(bean.getInputFieldC()!=null && !bean.getInputFieldC().equals(""))
    		{
    			condition3 = " AND upper(v.voucherNumber) like '%' || upper('"+bean.getInputFieldC()+"') || '%' ";
    		}
    		if(bean.getInputField4()!=null && !bean.getInputField4().equals(""))
    		{
    			condition4 = " AND v.fundid="+bean.getInputField4()+" ";
    		}
    		if(bean.getInputField5()!=null && !bean.getInputField5().equals(""))
    		{
    			condition5 = " AND v.status ="+bean.getInputField5()+" ";
    		}
    		if(bean.getInputField6()!=null && !bean.getInputField6().equals(""))
    		{
    			condition6 = " AND v.isconfirmed = "+bean.getInputField6()+" ";
    		}	//LOGGER.info("bean.getInputField7():"+bean.getInputField7());
    		String ModifyConditionForBill="";
    		if(bean.getShowMode().equalsIgnoreCase("reverse")){
    			ModifyConditionForBill=" and sph.isreversed='0' ";
    		}
    		
    		//LOGGER.info("bean.getInputField8():"+bean.getInputField8());
    		if(bean.getInputField8()!=null && !bean.getInputField8().equals(""))  
    		{
    			condition8 = " AND v.status != "+bean.getInputField8()+" ";
    		}
    		if(bean.getInputField9()!=null && !bean.getInputField9().equals(""))  
    		{
    			condition9 = " AND  v.name in ("+bean.getInputField9()+") ";
    		}
    		if(bean.getInputField10()!=null && !bean.getInputField10().equals(""))  
    		{
    			if(bean.getPaymentType()!=null && bean.getShowMode()!=null && bean.getPaymentType().equalsIgnoreCase("Subledger Payment") )
    				{
    				condition10 = " AND m.type ='"+bean.getInputField10()+"' ";
    				condition11 = " AND m.type ='"+bean.getInputField11()+"' "; 
    				}
    			else
    				condition10 = " AND  p.type ='"+bean.getInputField10()+"' ";
    		}
    		//condition11 is used above also
    		if(bean.getInputField12()!=null && !bean.getInputField12().equals(""))  
    		{
    			String fd[]=new String[5];
    			fd=bean.getInputField12().split("`--`");
				if(!fd[0].trim().equalsIgnoreCase("null"))	condition12s0 = " and voucherdate >='"+fd[0]+"' ";
				if(!fd[1].trim().equalsIgnoreCase("null"))	condition12s1 = " and voucherdate <='"+fd[1]+"' ";
				if(!fd[2].trim().equalsIgnoreCase("null"))	condition12s2 = " and r.relationtypeid ="+fd[2];
				if(!fd[3].trim().equalsIgnoreCase("null"))	condition12s3 = " and r.id="+fd[3]+" ";
				if(!fd[4].trim().equalsIgnoreCase("null"))	condition12s4 = " and f.id="+fd[4]+" ";
    		}
    		
    		if(bean.getBillType()!=null && bean.getBillType().equalsIgnoreCase("Contractor"))// for contractor
    		{
    			recordSetCondition=1;
	    		
	    		if(bean.getShowMode().equalsIgnoreCase("edit")){ LOGGER.info("inside contractor bill edit");
		    		String colNamesTemp[]={"slNo","H","B","C","D","I","E","F","G",};
		    		strfield=colNamesTemp;
		    		String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","Contractor","Work Order Reference","Work Order No","Bill Amount","Passed Amount","Status"};
		    		tableHeader=tableHeader1;
	    			qryString= 
	    	    		" SELECT distinct v.cgn AS \"colA\",v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", v.voucherDate as \"date\",c.name AS \"colC\",wbd.name AS \"colD\",wbd.code AS \"colI\", cbd.billAmount AS \"colE\", cbd.passedAmount AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" FROM  voucherHeader v,relation c, contractorBillDetail cbd , worksdetail wbd " +
	    	    		" WHERE  wbd.id=cbd.worksdetailid and  v.id=cbd.voucherHeaderId  and cbd.contractorId=c.id AND v.type='Journal Voucher' AND v.name='Contractor Journal' "+
	    	    		" and cbd.id not in (	"+ 
							 " SELECT distinct slph1.contractorbillid FROM subledgerpaymentheader slph1,voucherheader vh1 "+
							 " where vh1.id=slph1.voucherheaderid and vh1.status=0 and slph1.contractorbillid is not null "+ 
							 " and cbd.id=slph1.contractorbillid "+
							 "			   ) "+
	    	    		condition1 + condition2 +condition3 +condition4 + condition5 + condition6 + condition7 +condition8 +
	    	    		" ORDER BY \"date\",\"colB\" " ;
	    		}else { 
	    			LOGGER.info("inside contractor bill view modify");
	    			String colNamesTemp[]={"slNo","H","B","C","D","I","E","F","J","G",};
	    			strfield=colNamesTemp;
	    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","Contractor","Work Order Reference","Work Order No","Bill Amount","Passed Amount","Paid Amount","Status"};
		    		tableHeader=tableHeader1;
		    		String ModifyConditionForBill1="";
		    		if(bean.getShowMode().equalsIgnoreCase("modify")){
		    			ModifyConditionForBill1=" and cbd.paidamount=0 ";
		    		}
	    			qryString=
	    				//paid bills
	    	    		"SELECT v.cgn AS \"colA\",v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", v.voucherDate as \"date\", c.name AS \"colC\",wbd.name AS \"colD\",wbd.code AS \"colI\", cbd.billAmount AS \"colE\", cbd.passedAmount AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" , cbd.paidamount as \"colJ\"  FROM  voucherHeader v,relation c, contractorBillDetail cbd , worksdetail wbd "+
	    	    		" WHERE  wbd.id=cbd.worksdetailid and  v.id=cbd.voucherHeaderId  and cbd.contractorId=c.id AND v.type='Journal Voucher' AND v.name='Contractor Journal' and cbd.id in (select sph.contractorbillid from subledgerpaymentheader sph,contractorBillDetail cb where cb.ID=sph.CONTRACTORBILLID and sph.CONTRACTORBILLID is not null "+ ModifyConditionForBill + " group by sph.contractorbillid ) "+
	    	    		condition1 + condition2 +condition3 +condition4 + condition5 + condition6 + condition7 +condition8 + ModifyConditionForBill1 + 
	    	    		" union "+
	    	    		//unpaid bills
	    	    		" SELECT v.cgn AS \"colA\",v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", v.voucherDate as \"date\",c.name AS \"colC\",wbd.name AS \"colD\",wbd.code AS \"colI\", cbd.billAmount AS \"colE\", cbd.passedAmount AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" , null as \"colJ\" FROM  voucherHeader v,relation c, contractorBillDetail cbd , worksdetail wbd " +
	    	    		" WHERE  wbd.id=cbd.worksdetailid and  v.id=cbd.voucherHeaderId  and cbd.contractorId=c.id AND v.type='Journal Voucher' AND v.name='Contractor Journal' and cbd.id  not in (select sph.contractorbillid from subledgerpaymentheader sph where  sph.CONTRACTORBILLID is not null group by sph.contractorbillid ) "+
	    	    		condition1 + condition2 +condition3 +condition4 + condition5 + condition6 + condition7 +condition8 +
	    	    		" union "+  
	    	    		" SELECT mv.cgn AS \"colA\",mv.voucherNumber AS \"colB\",to_char(mv.voucherDate,'dd-Mon-yyyy') AS \"colH\", mv.voucherDate as \"date\",c.name AS \"colC\",wbd.name AS \"colD\",wbd.code AS \"colI\", "+  
	    	    		" cbd.billAmount AS \"colE\", cbd.passedAmount AS \"colF\",decode(mv.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(mv.status,1,'Reversed',decode(mv.status,2,'Reversal',''))) AS \"colG\" , null as \"colJ\" FROM voucherHeader v, relation c, contractorBillDetail cbd, worksdetail wbd,voucherheader mv WHERE  wbd.id=cbd.worksdetailid and "+  
	    	    		" v.id=cbd.voucherHeaderId AND cbd.contractorId=c.id AND v.type='Journal Voucher' AND v.name='Contractor Journal' "+ 
	    	    		" and mv.originalvcid=v.id " +
	    	    		condition1 + condition2 +condition3 +condition4 + condition5 + condition6 + condition8 +
	    	    		" ORDER BY \"date\",\"colB\" " ;
	    		}
    		}
    		else if(bean.getBillType()!=null && bean.getBillType().equalsIgnoreCase("Supplier"))// for supplier
    		{LOGGER.info("inside bills supplier");
    			recordSetCondition=1;
        		if(bean.getShowMode().equalsIgnoreCase("edit")){
        			String colNamesTemp[]={"slNo","H","B","C","D","E","F","G"};
        			strfield=colNamesTemp;
        			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","Supplier","Bill Number","Bill Amount","Passed Amount","Status"};
            		tableHeader=tableHeader1;
        			qryString=
	       			" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\",  v.voucherDate as \"date\",s.name AS \"colC\",sbd.billNumber AS \"colD\", sbd.billAmount AS \"colE\", sbd.passedAmount AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+     
	       			" FROM voucherHeader v, relation s, supplierBillDetail sbd   WHERE  v.id=sbd.voucherHeaderId AND sbd.supplierId=s.id AND "+
	       			 " v.type='Journal Voucher' AND v.name='Supplier Journal' and "+ 
	       			"  sbd.id not in ( "+	 
					 " SELECT distinct slph1.supplierbillid FROM subledgerpaymentheader slph1,voucherheader vh1 "+ 
					 " where vh1.id=slph1.voucherheaderid and vh1.status=0 and slph1.supplierbillid is not null  "+
					 " and sbd.id=slph1.supplierbillid "+
					 " 				) "+
	       			 condition1 + condition2 +condition3 +condition4 + condition5 + condition6 +
	       			 " ORDER BY \"date\",\"colB\"  ";
        		}else{
        			String colNamesTemp[]={"slNo","H","B","C","D","E","F","J","G"};
        			strfield=colNamesTemp;
        			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","Supplier","Bill Number","Bill Amount","Passed Amount","Paid Amount","Status"};
            		tableHeader=tableHeader1;
            		String ModifyConditionForBill1="";
		    		if(bean.getShowMode().equalsIgnoreCase("modify")){
		    			ModifyConditionForBill1=" and sbd.paidamount=0 ";
		    		}
        			qryString=" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\",  v.voucherDate as \"date\",s.name AS \"colC\",sbd.billNumber AS \"colD\", sbd.billAmount AS \"colE\", sbd.passedAmount AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\"  "+
        			" ,  sbd.paidamount as \"colJ\" "+
	       			 " FROM voucherHeader v, relation s, supplierBillDetail sbd   WHERE  v.id=sbd.voucherHeaderId AND sbd.supplierId=s.id AND "+
	       			 " v.type='Journal Voucher' AND v.name='Supplier Journal' and "+
	       			 " sbd.id in (select sph.supplierbillid from subledgerpaymentheader sph,supplierBillDetail sb "+
	       			 " where sb.ID=sph.supplierbillid and sph.supplierbillid is not null "+ ModifyConditionForBill + " group by sph.supplierbillid) "+
	       			 condition1 + condition2 +condition3 +condition4 + condition5 + condition6 +  ModifyConditionForBill1+
	       			 " union  "+
	       			 " SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\",  v.voucherDate as \"date\",s.name AS \"colC\",sbd.billNumber AS \"colD\", sbd.billAmount AS \"colE\", sbd.passedAmount AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+
	       			" , null as \"colJ\""+
	       			 " FROM voucherHeader v, relation s, supplierBillDetail sbd   WHERE  v.id=sbd.voucherHeaderId AND sbd.supplierId=s.id AND "+
	       			 " v.type='Journal Voucher' AND v.name='Supplier Journal' and "+ 
	       			 " sbd.id not in (select sph.supplierbillid from subledgerpaymentheader sph where sph.supplierbillid is not null group by sph.supplierbillid)"+
	       			 condition1 + condition2 +condition3 +condition4 + condition5 + condition6 + 
	       			 " union "+   
	       			 " SELECT mv.cgn AS \"colA\", mv.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\",  v.voucherDate as \"date\","+ 
	       			 " s.name AS \"colC\", sbd.billNumber AS \"colD\", sbd.billAmount AS \"colE\", sbd.passedAmount AS \"colF\",decode(mv.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(mv.status,1,'Reversed',decode(mv.status,2,'Reversal',''))) AS \"colG\" "+
	       			" , null as \"colJ\""+
	       			 " FROM voucherHeader v, relation s, supplierBillDetail sbd,voucherheader mv  WHERE  v.id=sbd.voucherHeaderId AND "+ 
	       			 " sbd.supplierId=s.id AND v.type='Journal Voucher' AND v.name='Supplier Journal'  and mv.originalvcid=v.id "+  
	       			 condition1 + condition2 +condition3 +condition4 + condition5 + condition6 +
	       			 " ORDER BY \"date\",\"colB\"  ";
        		}
    		}else if( bean.getBillType() != null && bean.getBillType().equalsIgnoreCase("Salary")) 
    		{ LOGGER.info("inside bills salary");   
    			recordSetCondition=1;
        		if(bean.getShowMode().equalsIgnoreCase("edit")){
        			String colNamesTemp[]={"slNo","H","B","E","F","G"};
        			strfield=colNamesTemp;
        			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","Month","Net Amount","Status"};
            		tableHeader=tableHeader1;
        			qryString=
    	    		" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\",  v.voucherDate as \"date\",s.mmonth AS \"colE\", s.netPay AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+
    	    		" FROM voucherHeader v,salaryBillDetail s WHERE  v.id=s.voucherHeaderId AND v.type='Journal Voucher' AND v.name='Salary Journal' "+
    	    		" and s.id not in ( "+	 
					 " SELECT distinct slph1.salarybillid FROM subledgerpaymentheader slph1,voucherheader vh1 "+ 
					 " where vh1.id=slph1.voucherheaderid and vh1.status=0 and slph1.salarybillid is not null  "+
					 " and s.id=slph1.salarybillid "+
					 " 				) "+
    	    		condition1 + condition2 +condition3+ condition5 + condition6 + condition8 + condition7 + condition4 +
    				" ORDER BY \"date\",\"colB\" ";
        		}else{
        			String colNamesTemp[]={"slNo","H","B","E","F","J","G"};
        			strfield=colNamesTemp;
        			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","Month","Net Amount","Paid Amount","Status"};
            		tableHeader=tableHeader1;
            		String ModifyConditionForBill1="";
		    		if(bean.getShowMode().equalsIgnoreCase("modify")){
		    			ModifyConditionForBill1=" and s.paidamount=0 ";
		    		}
	    			qryString="	SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\",  v.voucherDate as \"date\",s.mmonth AS \"colE\", s.netPay AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+
	    			", s.paidamount as \"colJ\" "+
		    		" FROM voucherHeader v,salaryBillDetail s WHERE  v.id=s.voucherHeaderId AND v.type='Journal Voucher' AND v.name='Salary Journal' "+
		    		" and s.id in (select sph.salarybillid from subledgerpaymentheader sph,salaryBillDetail sb  where sb.ID=sph.salarybillid and sph.salarybillid is not null "+ ModifyConditionForBill + " group by sph.salarybillid) "+
		    		condition1 + condition2 +condition3+ condition5 + condition6 + condition8 + condition7 + condition4 + ModifyConditionForBill1+
		    		" union "+
		    		" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\",  v.voucherDate as \"date\",s.mmonth AS \"colE\", s.netPay AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+
		    		", null as \"colJ\" "+ 
		    		" FROM voucherHeader v,salaryBillDetail s WHERE  v.id=s.voucherHeaderId AND v.type='Journal Voucher' AND v.name='Salary Journal' "+
		    		" and s.id not in (select sph.salarybillid from subledgerpaymentheader sph,salaryBillDetail sb  where sb.ID=sph.salarybillid and sph.salarybillid is not null  group by sph.salarybillid) "+
		    		condition1 + condition2 +condition3+ condition5 + condition6 + condition8 + condition7 + condition4 +
		    		" union "+
					" SELECT mv.cgn AS \"colA\", mv.voucherNumber AS \"colB\",to_char(mv.voucherDate,'dd-Mon-yyyy')  AS \"colH\",  mv.voucherDate as \"date\",s.mmonth AS \"colE\", s.netPay AS \"colF\",decode(mv.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(mv.status,1,'Reversed',decode(mv.status,2,'Reversal',''))) AS \"colG\" "+
					", null as \"colJ\" "+ 
					" FROM voucherHeader v,salaryBillDetail s,voucherheader mv WHERE  v.id=s.voucherHeaderId AND v.type='Journal Voucher' AND v.name='Salary Journal' and mv.originalvcid=v.id "+
					condition1 + condition2 +condition3+ condition5 + condition6 + condition8 + condition7 + condition4 +
					" ORDER BY \"date\",\"colB\" ";
        		}
    		}else if(bean.getJournalType()!=null && bean.getJournalType().equalsIgnoreCase("General"))// for journal entries
    		{LOGGER.info("inside journal");
    			recordSetCondition=1;
    			String colNamesTemp[]={"slNo","C","B","D","F","G"};
    			strfield=colNamesTemp;
    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","Fund","Amount","Status"};
        		tableHeader=tableHeader1;
    			qryString="SELECT  v.cgn AS \"colA\", v.voucherNumber AS \"colB\", to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colC\",V.VOUCHERDATE AS \"dateForSort\", "+
						" f.name AS \"colD\",SUM(vd.debitamount) AS \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+ 
	    				" FROM voucherHeader v, fund f, voucherDetail vd "+ 
	    				" WHERE v.status!=4 AND vd.debitamount>0 AND v.fundId=f.id AND v.id=vd.voucherHeaderId AND v.type='Journal Voucher'  " +condition9 +  
	    				 condition1 + condition2 +condition3+ condition5 + condition6 + condition4 +
	    				" GROUP BY v.cgn ,vouchernumber,voucherdate,v.status,v.isConfirmed,f.name ORDER BY \"dateForSort\",\"colB\" ";
    		}else if(bean.getJournalType()!=null && bean.getContraType()!=null && 
    				bean.getJournalType().equalsIgnoreCase("Contra") && bean.getContraType().equalsIgnoreCase("Bank To Bank"))
    		{LOGGER.info("inside contra banktobank");
    			recordSetCondition=1;
    			String colNamesTemp[]={"slNo","H","B","C","D","E","F","G"};
    			strfield=colNamesTemp;
    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","From Bank", "To Bank","Cheque Number","Amount","Status"};
        		tableHeader=tableHeader1;
    			qryString=    			
		    		" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colC\", bs.name||' '||bbs.branchName||' '||bas.accountNumber AS \"colD\", cjv.fromChequeNumber AS \"colE\", chq.amount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
    				" FROM voucherHeader v, bank b, bank bs, bankBranch bb, bankBranch bbs, bankAccount ba, bankAccount bas, contraJournalVoucher cjv, chequeDetail chq  "+
    				" WHERE v.status!=4 and v.id=cjv.voucherHeaderId AND cjv.fromBankId=b.id AND cjv.fromBankBranchId=bb.id AND cjv.fromBankAccountId=ba.id "+ 
    				" AND cjv.toBankId=bs.id AND cjv.toBankBranchId=bbs.id AND cjv.toBankAccountId=bas.id AND cjv.voucherHeaderId=chq.voucherHeaderId AND v.type='Contra' AND v.name='BankToBank' "+
    				condition1 + condition2 + condition3 + condition4 + condition5 + condition6 +
		    		 
    				" UNION "+
		
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\",  to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colC\", bs.name||' '||bbs.branchName||' '||bas.accountNumber AS \"colD\", cjv.fromChequeNumber AS \"colE\", ABS(gl.debitamount-gl.creditamount) AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
    				" FROM GENERALLEDGER gl,VOUCHERHEADER v, BANK b, BANK bs, BANKBRANCH bb, BANKBRANCH bbs, BANKACCOUNT ba, BANKACCOUNT bas, CONTRAJOURNALVOUCHER cjv "+  
    				" WHERE  v.status!=4 and gl.voucherheaderid=v.id 	AND gl.voucherlineid IN (SELECT MIN(gl1.voucherlineid) FROM GENERALLEDGER gl1 WHERE gl1.voucherheaderid=v.id ) "+  
    				" AND v.originalvcid=cjv.voucherHeaderId AND cjv.fromBankId=b.id AND cjv.fromBankBranchId=bb.id AND cjv.fromBankAccountId=ba.id "+ 
    				" AND cjv.toBankId=bs.id AND cjv.toBankBranchId=bbs.id AND cjv.toBankAccountId=bas.id "+ 
    				" AND v.TYPE='Contra' AND v.name='BankToBank' "+ 
    				condition1 + condition2 + condition3 + condition4 + condition5 + condition6 +" ORDER BY \"dateForSort\",\"colB\" ";		 
    		}else if(bean.getJournalType()!=null && bean.getContraType()!=null && 
    				bean.getJournalType().equalsIgnoreCase("Contra") && bean.getContraType().equalsIgnoreCase("Cash Withdrawal") && bean.getShowMode().equalsIgnoreCase("edit"))
    		{ LOGGER.info("inside contra cashwithdrawl edit");
    			recordSetCondition=1;
    			String colNamesTemp[]={"slNo","H","B","D","E","F","G"};
    			strfield=colNamesTemp;
    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","From Bank", "To Cash","Amount","Status"};
        		tableHeader=tableHeader1;
    			qryString= "SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",v.voucherDate AS \"dateForSort\",  to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colD\", coa.name AS \"colE\", chq.amount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
							" FROM voucherHeader v, bank b, bankBranch bb, bankAccount ba,contraJournalVoucher cjv, chequeDetail chq, chartOfAccounts coa "+
		    				" WHERE  v.id=cjv.voucherHeaderId AND cjv.fromBankId=b.id AND cjv.fromBankBranchId=bb.id AND cjv.fromBankAccountId=ba.id "+ 
		    				" AND cjv.toCashNameId=coa.id AND cjv.voucherHeaderId=chq.voucherHeaderId AND v.isconfirmed=0 AND v.status=0 AND v.type='Contra' AND v.name='BankToCash' "+
		    				condition1 + condition2 + condition3 + condition4 +" ORDER BY \"dateForSort\",\"colB\" ";
    		}else if(bean.getJournalType()!=null && bean.getContraType()!=null && 
    				bean.getJournalType().equalsIgnoreCase("Contra") && bean.getContraType().equalsIgnoreCase("Cash Withdrawal"))
    		{LOGGER.info("inside contra cashwithdrawl view/reverse");
    			recordSetCondition=1;
    			String colNamesTemp[]={"slNo","H","B","D","E","F","G"};
    			strfield=colNamesTemp;
    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","From Bank", "To Cash","Amount","Status"};
        		tableHeader=tableHeader1;
    			qryString= "SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\",  to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colD\", coa.name AS \"colE\", chq.amount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
					" FROM voucherHeader v, bank b, bankBranch bb, bankAccount ba,contraJournalVoucher cjv, chequeDetail chq, chartOfAccounts coa "+
    				" WHERE  v.status!=4 and v.id=cjv.voucherHeaderId AND cjv.fromBankId=b.id AND cjv.fromBankBranchId=bb.id AND cjv.fromBankAccountId=ba.id "+ 
    				" AND cjv.toCashNameId=coa.id AND cjv.voucherHeaderId=chq.voucherHeaderId AND v.type='Contra' AND v.name='BankToCash' "+
    				condition1 + condition2 + condition3  + condition4 + condition5 + condition6 +
    				" union "+
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\",  to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colD\", coa.name AS \"colE\", chq.amount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
    				" FROM voucherHeader v, bank b, bankBranch bb, bankAccount ba,contraJournalVoucher cjv, chequeDetail chq, chartOfAccounts coa "+
    				" WHERE  v.status!=4 and v.originalvcid=cjv.voucherHeaderId AND cjv.fromBankId=b.id AND cjv.fromBankBranchId=bb.id AND cjv.fromBankAccountId=ba.id "+ 
    				" AND cjv.toCashNameId=coa.id AND cjv.voucherHeaderId=chq.voucherHeaderId AND v.type='Contra' AND v.name='BankToCash' "+
    				 condition1 + condition2 + condition3 + condition4 + condition5 + condition6 +" ORDER BY \"dateForSort\",\"colB\" ";
    		}else if(bean.getJournalType()!=null && bean.getContraType()!=null && 
    				bean.getJournalType().equalsIgnoreCase("Contra") && bean.getContraType().equalsIgnoreCase("Cash Deposit") && bean.getShowMode().equalsIgnoreCase("edit"))
    		{LOGGER.info("inside contra cashdeposit");
    			recordSetCondition=1;
    			String colNamesTemp[]={"slNo","H","B","D","E","F","G"};
    			strfield=colNamesTemp;
    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","From Cash", "To Bank","Amount","Status"};
        		tableHeader=tableHeader1;
    			qryString= 
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\",  to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", coa.name AS \"colD\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colE\", vd.creditAmount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
    				" FROM voucherHeader v, bank b, bankBranch bb, bankAccount ba,contraJournalVoucher cjv, voucherDetail vd, chartOfAccounts coa "+
    				" WHERE  v.id=cjv.voucherHeaderId AND cjv.toBankId=b.id AND cjv.toBankBranchId=bb.id AND cjv.toBankAccountId=ba.id "+ 
    				" AND cjv.fromCashNameId=coa.id AND v.id=vd.voucherHeaderId AND vd.creditAmount>0 AND v.isconfirmed=0 AND v.status=0 AND v.type='Contra' AND v.name='CashToBank' "+
    				condition1 + condition2 + condition3 + condition4 +" ORDER BY \"dateForSort\",\"colB\" ";
    		}else if(bean.getJournalType()!=null && bean.getContraType()!=null && 
    				bean.getJournalType().equalsIgnoreCase("Contra") && bean.getContraType().equalsIgnoreCase("Cash Deposit"))
    		{LOGGER.info("inside contra cashdeposit");
    			recordSetCondition=1;
    			String colNamesTemp[]={"slNo","H","B","D","E","F","G"};
    			strfield=colNamesTemp;
    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number","From Cash", "To Bank","Amount","Status"};
        		tableHeader=tableHeader1;
    			qryString= 
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", coa.name AS \"colD\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colE\", vd.creditAmount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
					" FROM voucherHeader v, bank b, bankBranch bb, bankAccount ba,contraJournalVoucher cjv, voucherDetail vd, chartOfAccounts coa "+
    				" WHERE v.status!=4 and  v.id=cjv.voucherHeaderId AND cjv.toBankId=b.id AND cjv.toBankBranchId=bb.id AND cjv.toBankAccountId=ba.id "+ 
    				" AND cjv.fromCashNameId=coa.id AND v.id=vd.voucherHeaderId AND vd.creditAmount>0 AND v.type='Contra' AND v.name='CashToBank' "+
    				condition1 + condition2 + condition3  + condition4 + condition5 + condition6+
    				" union "+
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", coa.name AS \"colD\", b.name||' '||bb.branchName||' '||ba.accountNumber AS \"colE\", vd.creditAmount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
    				" FROM voucherHeader v, bank b, bankBranch bb, bankAccount ba,contraJournalVoucher cjv, voucherDetail vd, chartOfAccounts coa "+
    				" WHERE v.status!=4 and  v.originalvcid=cjv.voucherHeaderId AND cjv.toBankId=b.id AND cjv.toBankBranchId=bb.id AND cjv.toBankAccountId=ba.id "+ 
    				" AND cjv.fromCashNameId=coa.id AND v.id=vd.voucherHeaderId AND vd.creditAmount>0 AND v.type='Contra' AND v.name='CashToBank' "+
    				  condition1 + condition2 + condition3 + condition4 + condition5 + condition6 +"  ORDER BY \"dateForSort\",\"colB\" ";
    		}
    		else if(bean.getJournalType()!=null && bean.getContraType()!=null && 
    				bean.getJournalType().equalsIgnoreCase("Contra") && bean.getContraType().equalsIgnoreCase("Inter Fund Transfer"))
    		{LOGGER.info("inside contra interfundtransfer");
    			recordSetCondition=1;
    			String colNamesTemp[]={"slNo","H","B","D","E","F","G"};
    			strfield=colNamesTemp;
    			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number", "From Fund","To Fund","Amount", "Status"};
        		tableHeader=tableHeader1;
    			qryString= 
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", f.name AS \"colD\", fs.name AS \"colE\", vd.creditAmount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
    				" FROM voucherHeader v,contraJournalVoucher cjv, voucherDetail vd, fund f, fund fs "+
    				" WHERE  v.status!=4 and v.id=cjv.voucherHeaderId "+  
    				" AND cjv.fromFundId=f.id AND cjv.toFundId=fs.id AND v.id=vd.voucherHeaderId AND vd.creditAmount>0 AND v.type='Payment' AND v.name='FundToFund' "+
    				condition1 + condition2 + condition3 + condition4 + condition5 + condition6 +
    				" union "+
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",  v.voucherDate AS \"dateForSort\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colH\", f.name AS \"colD\", fs.name AS \"colE\", vd.creditAmount AS \"colF\" , decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal','')))  AS \"colG\" "+
    				" FROM voucherHeader v,contraJournalVoucher cjv, voucherDetail vd, fund f, fund fs "+
    				" WHERE  v.status!=4 and v.originalvcid=cjv.voucherHeaderId  AND cjv.fromFundId=f.id AND cjv.toFundId=fs.id AND v.id=vd.voucherHeaderId AND vd.creditAmount>0 AND v.type='Payment' AND v.name='FundToFund' "+ 
    				condition1 + condition2 + condition3 + condition4 + condition5 + condition6 +
    				" ORDER BY \"dateForSort\",\"colB\" ";
    		}
    		else if(bean.getJournalType()!=null && bean.getContraType()!=null && 
    				bean.getJournalType().equalsIgnoreCase("Contra") && bean.getContraType().equalsIgnoreCase("PayInSlip"))
    		{LOGGER.info("inside contra payinslip");
    			recordSetCondition=1;
			String colNamesTemp[]={"slNo","C","B", "D","E","F","G"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Pay-In Slip Date","Pay-In Slip Number", "Cheque Numbers", "Bank Name","Amount","Status"};
        		tableHeader=tableHeader1;
    			qryString= 
    				"SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\",v.voucherDate AS \"dateForSort\",to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colC\",rowconcat('select chequenumber from chequedetail cd where cd.payinslipdate=' ||'''' ||v.voucherdate || ''' and cd.payinslipnumber=' ||'''' ||v.vouchernumber || '''') as \"colD\", "+
					" (SELECT b.name||' '||bb.branchName||' A/cNo.'||ba.accountNumber FROM "+
    				" BANK b, BANKBRANCH bb, BANKACCOUNT ba WHERE b.id=bb.bankid AND bb.id=ba.branchid "+
    				" AND ba.id=(SELECT MAX(accountnumberid) FROM CHEQUEDETAIL WHERE payinslipnumber=v.vouchernumber))       AS \"colE\", gl.creditAmount \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+      
    				" FROM VOUCHERHEADER v,GENERALLEDGER gl "+
    				" WHERE  v.status!=4 and gl.voucherheaderid=v.id AND gl.creditamount !=0 AND v.vouchernumber         IN(SELECT payinslipnumber FROM CHEQUEDETAIL GROUP BY          payinslipnumber) "+
    				" and v.cgn like 'PYS%' " +
    				condition1 + condition2 + condition3 + condition4 + condition5 + condition6 +
    				" UNION "+
    				" SELECT v.cgn AS \"colA\", v.voucherNumber AS \"colB\", v.voucherDate AS \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') AS \"colC\",rowconcat('select chequenumber from chequedetail cd where  cd.payinslipdate=' ||'''' ||v.voucherdate || ''' and cd.payinslipnumber=' ||'''' ||v.vouchernumber || '''') as \"colD\", "+
    				" (SELECT b.name||' '||bb.branchName||' A/cNo.'||ba.accountNumber FROM "+
    				" BANK b, BANKBRANCH bb, BANKACCOUNT ba WHERE b.id=bb.bankid AND bb.id=ba.branchid "+
    				" AND ba.id=(SELECT MAX(accountnumberid) FROM CHEQUEDETAIL WHERE payinslipnumber=vh1.vouchernumber))    AS \"colE\", "+
    				" gl.creditAmount \"colF\",decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'),decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"colG\" "+ 
    				" FROM VOUCHERHEADER V, VOUCHERHEADER vh1,GENERALLEDGER gl "+ 
    				" WHERE v.status!=4 and v.ORIGINALVCID=vh1.id "+  
    				" AND gl.voucherheaderid=v.id AND gl.creditamount !=0 AND "+ 
    				" vh1.vouchernumber IN(SELECT payinslipnumber FROM CHEQUEDETAIL where isdeposited='0' GROUP BY payinslipnumber) "+
    				" and v.cgn like 'PYS%' " +
    				 condition1 + condition2 + condition3  + condition4  + condition6 +" AND v.status <= 0   ORDER BY \"dateForSort\", \"colB\" ";
    		}	
// Payments starts
//jobsid=bankPaymentSearch    		
    		else if(bean.getPaymentType()!=null &&  bean.getPaymentType().equalsIgnoreCase("Bank Payment"))
    		{LOGGER.info("inside payment bank");
    			recordSetCondition=2;
			String colNamesTemp[]={"slNo","voucherDate","voucherNumber", "paidTo", "passedAmount","billAmount","chequeNumber", "status"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number", "PaidTo", "PassedAmt","BillAmt", "Cheque No.", "Status"};
    		tableHeader=tableHeader1;
			qryString= 
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\", "+ 
				" max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\", sum(m.passedamount) as \"passedAmount\", "+
				" m.paidto as \"paidTo\", 0 as \"billAmount\" "+
				" from voucherHeader v, paymentheader p, chequeDetail c,miscbilldetail m "+  
				" where v.status!=4 and m.id=p.miscbilldetailid and v.id=p.voucherHeaderId "+ 
				" AND c.id=p.chequeid AND   p.chequeId >0  "+
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6 + 
				" group by cgn,voucherNumber,voucherDate,v.status,isConfirmed,m.paidto "+ 
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\", "+ 
				" max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\", sum(m.passedamount) as \"passedAmount\", "+
				" m.paidto as \"paidTo\", 0 as \"billAmount\" "+ 
				" from voucherHeader v,voucherheader mv,paymentheader p, chequeDetail c,miscbilldetail m "+  
				" where mv.status!=4 and m.id=p.miscbilldetailid and mv.id=p.voucherHeaderId "+ 
				" AND c.id=p.chequeid AND   p.chequeId >0 and  v.originalvcid=mv.id  "+ 
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6 + 
				" group by v.cgn,v.voucherNumber,v.voucherDate,v.status,v.isConfirmed,m.paidto  ORDER BY \"dateForSort\",\"voucherNumber\" ";
    		}
// jobsid=cashPaymentSearch
    		else if(bean.getPaymentType()!=null && bean.getPaymentType().equalsIgnoreCase("Cash Payment"))
    		{LOGGER.info("inside payment cash");
    			recordSetCondition=2;
			String colNamesTemp[]={"slNo","voucherDate","voucherNumber", "paidTo", "passedAmount","billAmount","status"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number", "PaidTo", "PassedAmt","BillAmt", "Status"};
    		tableHeader=tableHeader1;
			qryString=
    		" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(m.amount) as "+ 
			" \"billAmount\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
			" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\", sum(m.passedamount) as \"passedAmount\", "+
			" m.paidto as \"paidTo\" "+ 
			" from voucherHeader v,paymentheader p, miscbilldetail m "+  
			" where  v.status!=4 and m.id=p.miscbilldetailid and v.id=p.voucherHeaderId  "+ 
			  condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
			" group by cgn,voucherNumber,voucherDate,v.status,isConfirmed,m.paidto "+ 
			" union "+
			" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(m.amount) as "+ 
			" \"billAmount\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
			" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\", sum(m.passedamount) as \"passedAmount\", "+
			" m.paidto as \"paidTo\" "+ 
			" from voucherHeader v,voucherheader mv,paymentheader p, miscbilldetail m "+  
			" where mv.status!=4 and m.id=p.miscbilldetailid and mv.id=p.voucherHeaderId and  v.originalvcid=mv.id  "+ 
			 condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+  
			" group by v.cgn,v.voucherNumber,v.voucherDate,v.status,v.isConfirmed,m.paidto ORDER BY \"dateForSort\",\"voucherNumber\" ";
    		}
//jobsid=advancePaymentSearch
    		else if(bean.getPaymentType()!=null && bean.getPaymentType().equalsIgnoreCase("Advance") )
    		{LOGGER.info("inside payment advance");
    			recordSetCondition=2;
			String colNamesTemp[]={"slNo","voucherDate","voucherNumber", "chequeNumber", "paidAmount","status"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number", "Cheque No.", "Paid Amount", "Status"};
    		tableHeader=tableHeader1;
			qryString=
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(p.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,subledgerpaymentheader p, chequeDetail c "+  
				" where  v.status!=4 and v.id=p.voucherHeaderId "+ 
				" AND c.id=p.chequeid AND   p.chequeId >0  "+
				 condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" group by cgn,voucherNumber,voucherDate,v.status,isConfirmed "+ 
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(p.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v, voucherheader mv,subledgerpaymentheader p, chequeDetail c "+  
				" where mv.status!=4 and  mv.id=p.voucherHeaderId "+ 
				" AND c.id=p.chequeid AND   p.chequeId >0 and v.originalvcid=mv.id  "+ 
				 condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" group by v.cgn,v.voucherNumber,v.voucherDate,v.status,v.isConfirmed ORDER BY \"dateForSort\",\"voucherNumber\" ";		
    		}
//jobsid=subledgerPaymentSearch
    		else if(bean.getPaymentType()!=null && bean.getPaymentType().equalsIgnoreCase("Subledger Payment"))
    		{LOGGER.info("inside payment subledger");
    			recordSetCondition=2;
			String colNamesTemp[]={"slNo","voucherDate","voucherNumber", "chequeNumber", "paidAmount","status"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number", "Cheque No.", "Paid Amount", "Status"};
    		tableHeader=tableHeader1;
			qryString=
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(m.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,  subledgerpaymentheader m , chequeDetail c "+  
				" where  v.status!=4 and v.id=m.voucherHeaderId   and c.id=m.chequeid AND c.id=m.chequeid  and m.chequeId >0  "+
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" group by cgn,voucherNumber,voucherDate,v.status,isConfirmed "+
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\", sum(m.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,  subledgerpaymentheader m , chequeDetail c "+  
				" where v.status!=4 and v.id=m.voucherHeaderId  and c.id=m.chequeid AND c.id=m.chequeid  and m.chequeId >0 "+ 
				condition11 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" group by cgn,voucherNumber,voucherDate,v.status,isConfirmed "+ 
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(m.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,voucherHeader mv,  subledgerpaymentheader m , chequeDetail c "+  
				" where mv.status!=4 and mv.id=m.voucherHeaderId   and c.id=m.chequeid AND c.id=m.chequeid  and m.chequeId >0 and  v.originalvcid=mv.id "+	
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" group by v.cgn,v.voucherNumber,v.voucherDate,v.status,v.isConfirmed "+
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(m.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,voucherHeader mv,  subledgerpaymentheader m , chequeDetail c "+  
				" where mv.status!=4 and mv.id=m.voucherHeaderId  and c.id=m.chequeid AND c.id=m.chequeid  and m.chequeId >0 and v.originalvcid=mv.id "+ 
				condition11 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+
				" group by v.cgn,v.voucherNumber,v.voucherDate,v.status,v.isConfirmed ORDER BY \"dateForSort\",\"voucherNumber\" ";		
    		}
//    		jobsid=salaryPaymentSearch
    		else if(bean.getPaymentType()!=null && bean.getPaymentType().equalsIgnoreCase("Salary"))
    		{ LOGGER.info("inside payment salary");
    		recordSetCondition=2;
			String colNamesTemp[]={"slNo","voucherDate","voucherNumber", "chequeNumber", "paidAmount","status"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number", "Cheque No.", "Paid Amount", "Status"};
    		tableHeader=tableHeader1;
			qryString=
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(p.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,subledgerpaymentheader p, chequeDetail c "+  
				" where  v.status!=4 and v.id=p.voucherHeaderId  AND c.id=p.chequeid AND   p.chequeId >0  "+
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+
				" group by cgn,voucherNumber,voucherDate,v.status,isConfirmed "+
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(p.paidamount) as "+ 
				" \"paidAmount\", max('Cash') as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,subledgerpaymentheader p "+  
				" where v.status!=4 and  v.id=p.voucherHeaderId  and  p.chequeId = 0  "+
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+
				" group by cgn,voucherNumber,voucherDate,v.status,isConfirmed "+
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(p.paidamount) as "+ 
				" \"paidAmount\", max(c.chequeNumber) as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v,voucherHeader mv,subledgerpaymentheader p, chequeDetail c "+  
				" where mv.status!=4 and  mv.id=p.voucherHeaderId  AND c.id=p.chequeid AND   p.chequeId >0 and v.originalvcid=mv.id "+
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" group by v.cgn,v.voucherNumber,v.voucherDate,v.status,v.isConfirmed "+
				" union "+
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\",sum(p.paidamount) as "+ 
				" \"paidAmount\", max('Cash') as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\" "+
				" from voucherHeader v, voucherHeader mv,subledgerpaymentheader p "+  
				" where mv.status!=4 and  mv.id=p.voucherHeaderId  and  p.chequeId = 0 and v.originalvcid=mv.id "+
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" group by v.cgn,v.voucherNumber,v.voucherDate,v.status,v.isConfirmed "+
				" ORDER BY \"dateForSort\",\"voucherNumber\" ";		
    		}    		
//jobsid=bankEntrySearch
    		else if(bean.getPaymentType()!=null && bean.getPaymentType().equalsIgnoreCase("Bank Entry"))
    		{LOGGER.info("inside payment bank entry");
    			recordSetCondition=2;
			String colNamesTemp[]={"slNo","voucherDate","voucherNumber", "paidTo", "passedAmount", "billAmount", "chequeNumber","status"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Voucher Date","Voucher Number", "Paid To", "Passed Amt","Bill Amt", "Ref No.", "Status"};
    		tableHeader=tableHeader1;
			qryString=
				" select v.cgn as \"cgNumber\",v.voucherNumber as \"voucherNumber\",v.voucherDate as \"dateForSort\", to_char(v.voucherDate,'dd-Mon-yyyy') as \"voucherDate\", "+ 
				" rec.chequeNumber as \"chequeNumber\", decode(v.status,0,decode(v.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(v.status,1,'Reversed',decode(v.status,2,'Reversal',''))) AS \"status\", m.passedamount as \"passedAmount\", "+
				" m.paidto as \"paidTo\", 0 as \"billAmount\" "+ 
				" from voucherHeader v,paymentheader p, bankreconciliation rec,miscbilldetail m "+  
				" where v.status!=4 and m.id=p.miscbilldetailid and v.id=p.voucherHeaderId  and v.id=rec.voucherHeaderId AND  p.chequeId is null  "+
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" union "+
				" select mv.cgn as \"cgNumber\",mv.voucherNumber as \"voucherNumber\",mv.voucherDate as \"dateForSort\", to_char(mv.voucherDate,'dd-Mon-yyyy') as \"voucherDate\", "+ 
				" rec.chequeNumber as \"chequeNumber\", decode(mv.status,0,decode(mv.isconfirmed,0,'UnConfirmed','confirmed'), "+
				" decode(mv.status,1,'Reversed',decode(mv.status,2,'Reversal',''))) AS \"status\", m.passedamount as \"passedAmount\", "+
				" m.paidto as \"paidTo\", 0 as \"billAmount\" "+ 
				" from voucherHeader v,voucherheader mv,paymentheader p, bankreconciliation rec,miscbilldetail m "+  
				" where v.status!=4 and m.id=p.miscbilldetailid and v.id=p.voucherHeaderId "+ 
				" and v.id=rec.voucherHeaderId AND  p.chequeId is null and  mv.originalvcid=v.id  "+ 
				condition10 + condition1 + condition2 + condition3 + condition4 + condition5 + condition6+ 
				" ORDER BY \"dateForSort\",\"voucherNumber\" ";		
    		}
//    		jobsid=subledgerPaymentSearchNew
    		else if(bean.getShowMode()!=null && bean.getShowMode().equals("searchPay"))
    		{ LOGGER.info("inside payment create subledgerpayment");
    			recordSetCondition=2;
			String colNamesTemp[]={"slNo","contractor", "wocode", "fundname", "fundsource", "amount","hiddenField"};
			strfield=colNamesTemp;
			String tableHeader1[]={"Sl No","Supplier/Contractor Name","PO/WO", "Fund", "FinancingSource","Amount"};
    		tableHeader=tableHeader1;
			qryString=
				" select distinct (sum(billamount) over(PARTITION BY b.WORKSDETAILID)-sum(b.paidamount) over(PARTITION BY b.WORKSDETAILID)) as \"amount\", "+
				" r.name as \"contractor\",r.id as \"id\",a.id as \"worksdetailid\",a.code as \"wocode\", "+
				" f.id as \"fundid\",f.name as \"fundname\",decode(a.fundsourceid,null,' ',(select name from fundsource where id=a.fundsourceid)) as \"fundsourceid\",decode(a.fundsourceid,null,' ',(select name from fundsource where id=a.fundsourceid)) as \"fundsource\",rtype.name as \"type\" "+
				" from worksdetail  a,contractorbilldetail  b,relation r,fund f, voucherheader vh,relationtype rtype,egw_works_mis mis "+
				" where a.id = b.worksdetailid and a.relationid=r.id  AND mis.worksdetailid (+)=a.ID AND  (a.fundid=f.ID OR mis.fundid=f.ID) "+
				" and b.voucherheaderid=vh.id and  r.relationtypeid=rtype.id "+
				" and b.passedamount>(b.paidamount+b.tdsamount+b.advadjamt+b.otherrecoveries) and vh.status=0 "+
				condition12s0+condition12s1+condition12s2+condition12s3+condition12s4+
				" union "+
				" select distinct (sum(billamount) over(PARTITION BY b.WORKSDETAILID)-sum(b.paidamount) over(PARTITION BY b.WORKSDETAILID)) as \"amount\",r.name as \"contractor\",r.id as \"id\",a.id as \"worksdetailid\", "+
				" a.code as \"wocode\",f.id as \"fundid\", "+
				" f.name as \" fundname\",decode(a.fundsourceid,null,' ',(select name from fundsource where id=a.fundsourceid)) as \"fundsourceid\",decode(a.fundsourceid,null,' ',(select name from fundsource where id=a.fundsourceid)) as \"fundsource\",rtype.name as \"type\" "+
				" from worksdetail  a,supplierbilldetail  b,relation r,fund f,voucherheader vh,relationtype rtype "+
				" where a.id = b.worksdetailid and a.relationid=r.id and  a.fundid=f.id "+
				" and b.voucherheaderid=vh.id and  r.relationtypeid=rtype.id "+
				" and b.passedamount>(b.paidamount+b.tdsamount+b.advadjamt+b.otherrecoveries) and vh.status=0 "+
				condition12s0+condition12s1+condition12s2+condition12s3+condition12s4+
				" ORDER BY \"id\",\"wocode\" ";
    		}
    	
    }catch(Exception e){ 
    						LOGGER.debug("Exp="+e.getMessage());
    					}
    return qryString;
    }
    private String formatDate(String date)throws TaskFailedException
    {
    	try{
        	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			date=formatter.format(sdf.parse(date));
			}catch(Exception e){
				LOGGER.error("Exp="+e.getMessage());
				throw new TaskFailedException(e.getMessage());
			}
	        return date;
    }

}
