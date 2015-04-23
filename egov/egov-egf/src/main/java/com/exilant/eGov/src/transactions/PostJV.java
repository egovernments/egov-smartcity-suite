/*
 * Created on Apr 30, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;

import com.exilant.GLEngine.Transaxtion;
import com.exilant.eGov.src.common.EGovernCommon;

/**
 * @author Tilak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostJV 
{
	private static  final Logger LOGGER = Logger.getLogger(PostJV.class);
	Connection connection;
	Statement statement;
	ResultSet rset;
	  CommonMethodsI cm=new CommonMethodsImpl();
	  EGovernCommon ecm = new EGovernCommon();
	  String modeOfColl,functionId,fundId,fundSourceId,fiscalPeriodId,returnVal,mainCgvn,cgn;
	  int recordId,bankAccId,boundaryId;
		  ArrayList createCessRec;
	  ArrayList createMainRec;
	 SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
	 SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	 SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
	 Date dt=new Date();
	 //gets Values from Voucher object and calls Appropiate methods for receipt transaction or Journal transaction
	/*public String generateVoucher(Voucher v,Connection connection) throws EGOVException,Exception
	{
		*//**
		 * implement using create voucher api
		 *//*
		return returnVal;
	}*/
/*	private void getValuesForJV() throws EGOVException,Exception
	{
		if(LOGGER.isInfoEnabled())     LOGGER.info(" inside perform main posting ");
		createMainRec=new ArrayList();
		GeneralLedgerPosting gle=null;
		double amount=0;
   		for(int i=0;i<glList.length;i++)
		{
			gle=(GeneralLedgerPosting)glList[i];
			if(gle != null)
			{
				if(LOGGER.isInfoEnabled())     LOGGER.info(" gle.getCess()  "+gle.getCess());
				if(LOGGER.isInfoEnabled())     LOGGER.info(" gle.getCredit()  "+gle.getCredit());
				if(LOGGER.isInfoEnabled())     LOGGER.info(" gle.getDebit()  "+gle.getDebit());
				if(LOGGER.isInfoEnabled())     LOGGER.info(" gle.getFunction()  "+gle.getFunction());
				if(LOGGER.isInfoEnabled())     LOGGER.info(" gle.getGlcode()  "+gle.getGlcode());
				if(gle.getCredit() > 0 && gle.getDebit() > 0 )
						throw new EGOVException(" GeneralLedgerPosting entry has both debit and credit amount ");
				if(gle.getCredit() > 0)
					amount=gle.getCredit();
				if(gle.getDebit() > 0)
					amount=gle.getDebit();		
				if(LOGGER.isInfoEnabled())     LOGGER.info(">>>>>> amount "+amount);
				TrnDetail crDetail=new TrnDetail();
			  	String glCode=cm.getGlCode(gle.getGlcode(),connection);
		  		if(LOGGER.isInfoEnabled())     LOGGER.info(">>>> code "+glCode);
				crDetail.setGlCode(glCode);
				crDetail.setCrAmount(""+gle.getCredit());
				crDetail.setDrAmount(""+gle.getDebit());
				crDetail.setFunctionId(""+gle.getFunction());
				createMainRec.add(crDetail);
				//totalAmount=totalAmount+ptAmount;
			}
		}
   		if(createMainRec.size()>0)
   		{
			GeneralLedgerPosting glPost[]=new GeneralLedgerPosting[createMainRec.size()];
			for(int i=0;i<createMainRec.size();i++)
			{
				glPost[i]=new GeneralLedgerPosting();
				Transaxtion txans=(Transaxtion)createMainRec.get(i);
				glPost[i].setCredit(Double.parseDouble(txans.getCrAmount()));
				glPost[i].setDebit(Double.parseDouble(txans.getDrAmount()));
				glPost[i].setFunction(Integer.parseInt(txans.getFunctionId()));
				glPost[i].setGlcode(txans.getGlCode());

			}
			GeneralLedgerEnteries glenties=new GeneralLedgerEnteries();
			glenties.setGeneralLedgerPosting(glPost);
			v.setGeneralLedgerEnteries(glenties);
  		}
		setVoucherNumberCGN();
		FinancialTransactions ft=new FinancialTransactions();
		Voucherheader vouHeader=(Voucherheader)ft.postTransaction(v,connection,null);
		returnVal=vouHeader.getCgvn();
	   		
	}*/
	
	/*public void setVoucherNumberCGN() throws Exception
	{
		if(LOGGER.isInfoEnabled())     LOGGER.info(" client NN  ");
		//String vNum="";
   		String cgvn="";
//		vNum=ecm.maxVoucherNumber("J",connection,Integer.toString(vh.getFund()));
		if(LOGGER.isInfoEnabled())     LOGGER.info(">>>> vNum "+ecm.maxVoucherNumber("J",connection,Integer.toString(vh.getFund())));
		if(!ecm.isUniqueVN((ecm.maxVoucherNumber("J",connection,Integer.toString(vh.getFund()))),vh.getVoucherdate(),connection))
			throw new Exception();
		String vType=ecm.maxVoucherNumber("J",connection,Integer.toString(vh.getFund())).substring(0,2);
		cgvn=ecm.getEg_Voucher(vType,fiscalPeriodId,connection);
		if(LOGGER.isInfoEnabled())     LOGGER.info(">>>> cgvn "+cgvn);
		for(int i=cgvn.length();i<5;i++)
		{
			cgvn="0"+cgvn;
		}
		cgvn=vType+cgvn;
		cgn="JVG"+ecm.getCGNumber();
		vh.setVouchernumber(ecm.maxVoucherNumber("J",connection,Integer.toString(vh.getFund())));
		vh.setCgvn(cgvn);
		vh.setCgn(cgn);
	}*/
}
