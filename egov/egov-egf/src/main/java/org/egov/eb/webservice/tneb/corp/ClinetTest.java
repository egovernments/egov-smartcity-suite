package org.egov.eb.webservice.tneb.corp;

import java.rmi.RemoteException;
import java.util.Date;

public class ClinetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		try {
			System.out.println(new Date());
			FetchDataProxy fb=new FetchDataProxy();
			System.out.println(new Date());
			TempPostRecords3[] fetchData = fb.fetchData("09242074225","CORPTANGEDCO","TanXBill1@3");
			System.out.println(new Date()); 
			System.out.println(fetchData);
			System.out.println(fetchData[0]);
			System.out.println(fetchData[0].getCuscode()+"-----"+fetchData[0].getDuedate()+"-----"+fetchData[0].getMessage()+"------"+fetchData[0].getAmount());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
