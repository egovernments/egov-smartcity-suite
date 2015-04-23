package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.database.utils.EgovDatabaseManager;

import com.exilant.exility.common.TaskFailedException;


// Referenced classes of package com.exilant.eGov.src.reports:
//            ConSupReportBean

public class ConSupReportList 
{

    Connection connection;
    Statement statement;
    ResultSet resultset;
    private static TaskFailedException taskExc;
    private static final Logger LOGGER = Logger.getLogger(ConSupReportList.class);
    public ConSupReportList()
    {
        connection = null;
        statement = null;
        resultset = null;
    }

    public LinkedList getConSupReportList(ConSupReportBean reportBean)throws TaskFailedException
    {
        LinkedList dataList;
        int totalCount;
        ArrayList data;
        String prevCode;
        String prevWorkOrderNo;
        dataList = new LinkedList();
        NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat("###############.00");
        try
        {
            connection = EgovDatabaseManager.openConnection();
        }
        catch(Exception exception)
        {
            throw new TaskFailedException();
        }
        String code = reportBean.getCode();
        int conSupType = reportBean.getConSupType();
        String fundId =reportBean.getFundId();
        if(LOGGER.isInfoEnabled())     LOGGER.info("fund name:"+fundId);
        String workOrderNo = "";
        String query = getQuery(code, conSupType,reportBean.getFromDate(),reportBean.getToDate(),fundId);
        if(LOGGER.isDebugEnabled())     LOGGER.debug((new StringBuilder("query:")).append(query).toString());
        try
        {
            statement = connection.createStatement(1004, 1007);
            resultset = statement.executeQuery(query);
        }
        catch(Exception e)
        {
            LOGGER.error((new StringBuilder("Exception in creating statement:")).append(statement).toString());
            throw taskExc;
        }
        totalCount = 0;
        data = new ArrayList();
        String curCode = "";
        prevCode = "";
        String curWorkOrderNo = "";
        prevWorkOrderNo = "";
        String relCode = "";
        String relId="";
        String orderDate = "";
        String worksDetailId = "";
        String relationType = "";
        String name="",workname="";
        //String curWorkOrderNo="";
        BigDecimal orderValue,maxAdv,advPaid,advAdj,amtPaid,billAmount,dedAmount ;
        try
        {
            while(resultset.next())
            {
                String arr[] = new String[15];
                 totalCount++;

                curCode = resultset.getString("name");
                curWorkOrderNo = resultset.getString("workOrderNo");
                for(int i = 0; i < arr.length; i++)
                {
                    arr[i] = "";
                }



                if(!curWorkOrderNo.equalsIgnoreCase(prevWorkOrderNo))
                {
                	if(!curCode.equalsIgnoreCase(prevCode))
                    	name=resultset.getString("name"); else name="";
                	if(name != null && !name.trim().equals("")) arr[0]=name;
    				workOrderNo=resultset.getString("workOrderNo"); if(workOrderNo != null && !workOrderNo.trim().equals("")) arr[1]=workOrderNo; else arr[1]="&nbsp;";
                    workname=resultset.getString("workname"); if(workname != null && !workname.trim().equals("")) arr[2]=workname;else arr[2]="&nbsp;";
                    orderDate=resultset.getString("orderdate"); if(orderDate != null && !orderDate.trim().equals("")) arr[3]=orderDate;else arr[3]="&nbsp;";
                    orderValue=resultset.getBigDecimal("orderValue"); if( orderValue != null && orderValue.doubleValue()>0) arr[4]=formatter.format(orderValue); else arr[3]="&nbsp;";
                    maxAdv=resultset.getBigDecimal("maxAdv"); if( maxAdv != null && maxAdv.doubleValue()>0) arr[5]=formatter.format(maxAdv); else arr[5]="&nbsp;";
                    advPaid=resultset.getBigDecimal("advPaid"); if( advPaid != null && advPaid.doubleValue()>0) arr[6]=formatter.format(advPaid); else arr[6]="&nbsp;";
                    advAdj=resultset.getBigDecimal("advAdj"); if( advAdj != null && advAdj.doubleValue()>0) arr[7]=formatter.format(advAdj); else arr[7]="&nbsp;";
                    amtPaid=resultset.getBigDecimal("amtPaid"); if( amtPaid != null && amtPaid.doubleValue()>0) arr[8]=formatter.format(amtPaid);  else arr[8]="&nbsp;";
                    billAmount=resultset.getBigDecimal("billtotal"); if( billAmount != null && billAmount.doubleValue()>0) arr[9]=formatter.format(billAmount); else arr[9]="&nbsp;";
                    relCode=resultset.getString("relcode"); if( relCode != null && !relCode.trim().equals("")) arr[10]=relCode; else arr[10]="&nbsp;";
                    worksDetailId=resultset.getString("worksdetailid"); if( worksDetailId != null && !worksDetailId.trim().equals("")) arr[11]=worksDetailId; else arr[11]="&nbsp;";
                    relationType=resultset.getString("relType"); if( relationType != null && !relationType.trim().equals("")) arr[12]=relationType; else arr[12]="&nbsp;";
                   dedAmount=resultset.getBigDecimal("dedAmount"); if( dedAmount != null && dedAmount.doubleValue()>0) arr[13]=formatter.format(dedAmount); else arr[13]="&nbsp;";
                   relId=resultset.getString("relId"); if( relId != null && !relId.trim().equals("")) arr[14]=relId; else arr[14]="&nbsp;";
                   data.add(arr);
                }
                prevCode = resultset.getString("name");
                prevWorkOrderNo = resultset.getString("workOrderNo");
            }
            String gridData[][] = new String[data.size() + 1][14];
            gridData[0][0] = "Con/Sup name";
            gridData[0][1] = "code";
            gridData[0][2] = "Work Name";
            gridData[0][3] = "Order Date";
            gridData[0][4] = "Order Value";
            gridData[0][5] = "Advance Payable";
            gridData[0][6] = "Advance Paid";
            gridData[0][7] = "Advance adjusted";
            gridData[0][6] = "Amount Paid";
            gridData[0][9] = "Billed Amount";
            gridData[0][10] = "relation Code";
            gridData[0][11] = "workdetailid";
            for(int i = 1; i <= data.size(); i++)
            {
                gridData[i] = (String[])(String[])data.get(i - 1);
            }

            for(int i = 1; i <= data.size(); i++)
            {
                ConSupReportBean reportBean1 = new ConSupReportBean();
                reportBean1.setName(gridData[i][0]);
                reportBean1.setWorkOrderNo(gridData[i][1]);
                reportBean1.setWorkname(gridData[i][2]);
                reportBean1.setOrderDate(gridData[i][3]);
                reportBean1.setOrderValue(gridData[i][4]);
                reportBean1.setMaxAdv(gridData[i][5]);
                reportBean1.setAdvPaid(gridData[i][6]);
                reportBean1.setAdvAdj(gridData[i][7]);
                reportBean1.setAmtPaid(gridData[i][8]);
                reportBean1.setBillAmount(gridData[i][9]);
                reportBean1.setRelCode(gridData[i][10]);
                reportBean1.setWorksDetailId(gridData[i][11]);
                reportBean1.setRelationTypeId(gridData[i][12]);
                reportBean1.setDedAmount(gridData[i][13]);
                reportBean1.setRelId(gridData[i][14]);
                reportBean.setTotalCount(Integer.toString(totalCount));
                dataList.add(reportBean1);
            }

            if(LOGGER.isInfoEnabled())     LOGGER.info("Datalist is filled");
        }
        catch(SQLException ex)
        {
            LOGGER.error((new StringBuilder("ERROR: ")).append(ex.toString()).toString());
            throw taskExc;
        }

        if(LOGGER.isInfoEnabled())     LOGGER.info("returning from ConSupReportList.java file");
        return dataList;
    }

    private String getQuery(String code, int conSupType,String fromDate,String toDate,String fundId1)
    {
        String codeCondition = "";
        String tblName = "",fundTable=" ",fundCondition=" ";
        if(LOGGER.isInfoEnabled())     LOGGER.info((new StringBuilder("Code:")).append(code).append("contrctor/supplier type").append(conSupType).toString());
        if(!code.trim().equalsIgnoreCase("") && !code.trim().equals(""))
        {
            codeCondition = (new StringBuilder(" AND rel.code='")).append(code).append("'").toString();
        }
        if(conSupType == 1)
        {
            tblName = "supplierbilldetail cd";
        } else
        {
            tblName = "contractorbilldetail cd";
        }
        if(fundId1!=null && !fundId1.trim().equalsIgnoreCase("")){
        	fundTable=" , fund f ";
        	fundCondition=" and wd.fundid=f.id and f.id="+fundId1+ " ";
        }
        String ConSupPayCode="'"+EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes")+"','"+EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes")+"'";
        return "SELECT  rel.name AS \"name\",rel.code as \"relcode\",rel.id as \"relId\",rel.relationtypeid as \"relType\", wd.id as \"worksdetailid\",wd.code AS \"workOrderNo\","+
        "wd.name AS \"workname\",to_char(orderdate, 'dd-Mon-yyyy') AS \"orderdate\",  wd.TOTALVALUE  AS  \"orderValue\",  wd.ADVANCEPAYABLE AS \"maxAdv\" ,"+
        "wd.ADVANCEAMOUNT AS \"advPaid\",  wd.ADVANCEADJ AS \"advAdj\", wd.PAIDAMOUNT-wd.ADVANCEAMOUNT AS \"amtPaid\",0 as \"billtotal\",  '' as  \"dedAmount\"  FROM WORKSDETAIL wd, RELATION rel  " +fundTable +
        "WHERE wd.relationid=rel.id  and wd.orderdate>=to_date('"+fromDate+"','dd/MM/yyyy') and wd.orderdate<=to_date('"+toDate+"','dd/MM/yyyy')  AND   rel.RELATIONTYPEID=" +conSupType+ codeCondition +fundCondition +
        " union SELECT distinct rel.name AS \"name\", rel.code as \"relcode\",rel.id as \"relId\",rel.relationtypeid as \"relType\",wd.id as \"worksdetailid\", wd.code AS \"workOrderNo\","+
        "wd.name AS \"workname\",to_char(orderdate, 'dd-Mon-yyyy') AS \"orderdate\",  wd.TOTALVALUE  AS  \"orderValue\",  wd.ADVANCEPAYABLE AS \"maxAdv\" , " +
        "wd.ADVANCEAMOUNT AS \"advPaid\",  wd.ADVANCEADJ AS \"advAdj\", wd.PAIDAMOUNT-wd.ADVANCEAMOUNT AS \"amtPaid\", sum(cd.passedamount)" +
        " over(PARTITION BY cd.WORKSDETAILID) as \"billtotal\"	,decode((select sum(creditamount) from generalledger where voucherheaderid=vh.id and glcode not in("+ConSupPayCode+")),null,'',"+
        "(select sum(creditamount) from generalledger where voucherheaderid=vh.id and glcode not in("+ConSupPayCode+"))) as \"dedAmount\" "+
        " FROM WORKSDETAIL wd, RELATION rel, " +tblName+ fundTable+ ",voucherheader vh  WHERE wd.relationid=rel.id  AND wd.id= cd.WORKSDETAILID " + fundCondition+
        "and vh.ID=cd.VOUCHERHEADERID and vh.status<>4 and vh.status=0  and wd.orderdate>=to_date('"+fromDate+"','dd/MM/yyyy') and wd.orderdate<=to_date('"+toDate+"','dd/MM/yyyy') and  rel.RELATIONTYPEID=" +conSupType+codeCondition+" order by  \"relcode\",\"orderdate\",\"orderValue\",\"workOrderNo\" asc ," +
        "\"billtotal\" desc ";
    }

}
