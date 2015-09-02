

<%@ page import="
  org.egov.infstr.utils.DateUtils,
  java.text.DecimalFormat,
  java.util.*,
  javax.naming.*,
  javax.ejb.*,
  javax.rmi.PortableRemoteObject,
  java.rmi.RemoteException,
  java.text.SimpleDateFormat,
  java.net.*,
  java.net.URLEncoder,
  java.io.*,
  java.sql.*,
  org.egov.infstr.utils.HibernateUtil,
  javax.ejb.CreateException,
  org.egov.EGOVRuntimeException"
%>

<%!

 // return float rounded to 2 places


 DecimalFormat decimalformat = new DecimalFormat("#########0.00");

  public float getRoundOfFloat(double parameter)
   {
      parameter = (double)Math.round(parameter);
      return (float)parameter;

   }

  public double getRoundOfDouble(double parameter)
   {
      return Math.round(parameter);

   }


  public float getRoundOfFloat(float parameter)
  {
	 return Math.round(parameter);

   }
/* public float formatdecimal(float parameter)
 {

    DecimalFormat decimalformat = new DecimalFormat("###,##,##,##0.00");
    return(decimalformat.format(parameter);
 }*/



 public String getRoundOfString(double parameter)
 {
         parameter = (double)Math.round(parameter);
        return decimalformat.format(parameter);

 }
  public String getRoundOfString(float parameter)
  {
         // parameter = (float)Math.round(parameter);
         return decimalformat.format(parameter);

  }

//////////////// return text for a float value


  String Suffix[] = {"units","thousand", "lakh", "Crore"};
  String Name[] = {"zero", "one", "two", "three", "four", "five", "six","seven", "eight", "nine", "ten", "eleven", "twelve","thirteen", "fourteen", "fifteen",  "sixteen", "seventeen","eighteen", "nineteen"};
  String Namety[] = {"twenty", "thirty", "forty","fifty", "sixty", "seventy", "eighty", "ninety"};
 // int count=0,devideby =1000;

public String getCurrencyInWords(float parameter)
{
    int count=0,devideby =1000;
    String strpara = decimalformat.format(parameter);
    int intpart = Integer.parseInt(strpara.substring(0,strpara.indexOf(".")));
    int floatpart= Integer.parseInt(strpara.substring(strpara.indexOf(".")+1,strpara.length()));
    System.out.println("Int part ::"+intpart);
    System.out.println("Int part ::"+floatpart);
    String curncyinwords= " Rupee" + (intpart==1 ? " ":"s ") + TextCash(intpart, 0, count, devideby) ;
    curncyinwords += " paise "+ TextCash(floatpart, 0, count, devideby) + " only";
    return curncyinwords;
}

private String TextCash(int L, int K,int count,int devideby)
{
  if (L==0)
    return (K>0 ? "" : " zero");
  if(count>0)
        devideby =100;
  count++;
  return  Small(TextCash(Math.round(L/devideby), K+1, count, devideby), L%devideby, K, count, devideby);
}

private String Small(String TC,int J,int K,int count,int devideby)
{
    if (J==0)
        return TC;
    String retStr = TC;
    if (J>99)
    {
        retStr += Name[Math.round(J/100)] + " hundred " ;
        J %= 100;
        if(J>0)
            retStr += " and ";
    }
    else if ((!retStr.equals("")) && (J>0) && (K==0))
            retStr += " and ";
    if (J>19)
    {
        retStr += Namety[Math.round(J/10)-2] ;
        J %= 10;
        retStr += " ";
    }
  if (J>0)
    retStr += Name[J] + " ";
  if (K>0)
        retStr += Suffix[K] + " ";
  count--;
  if(count==1)
        devideby =1000;
  return retStr;
 }

/**This method encodes the given string
*** to be used in all the URLs wherever the parameter may contain reserved chars
**/
public String encodeStr(String str)
{

    try
    {
        str= URLEncoder.encode(str,"UTF-8");

    }
    catch(Exception e)
    {
        System.out.println("Error while Encoding In helper.jsp");
    }
    return str;
}


 public String getNumberInWords(int parameter)
 {
    int count=0,devideby =1000;
    String strpara = decimalformat.format(parameter);
    int intpart = Integer.parseInt(strpara.substring(0,strpara.indexOf(".")));
    int floatpart= Integer.parseInt(strpara.substring(strpara.indexOf(".")+1,strpara.length()));
    System.out.println("Int part ::"+intpart);
    System.out.println("Int part ::"+floatpart);
    String curncyinwords= "" + (intpart==1 ? " ":"") + TextNumber(intpart, 0, count, devideby) + " only";
    //curncyinwords += "paise "+ TextNumber(floatpart, 0, count, devideby) + " only";
    return curncyinwords;
 }

 private String TextNumber(int L, int K,int count,int devideby)
 {
   if (L==0)
    return (K>0 ? "" : "Zero");
   if(count>0)
        devideby =100;
   count++;
   return  Small(TextNumber(Math.round(L/devideby), K+1, count, devideby), L%devideby, K, count, devideby);
 }


SimpleDateFormat oSdf = new SimpleDateFormat("dd-MM-yyyy h:mm a");
 SimpleDateFormat sdf        = new SimpleDateFormat("dd-MM-yyyy");

/* This function takes the date as a string and returns the Date with time as 0 hours
*/
private java.util.Date getStartDate(String strStartDate) throws java.text.ParseException
{
	java.util.Date oFromdate = null;
	Calendar fromDateCalendar = new GregorianCalendar();

	fromDateCalendar.setTime(new java.util.Date(sdf.parse(strStartDate).getTime()));
	fromDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
	fromDateCalendar.set(Calendar.MINUTE, 0);
	fromDateCalendar.set(Calendar.SECOND, 0);
    oFromdate = fromDateCalendar.getTime();

    return oFromdate;
}


/* This function takes the date as a string and returns the Date with time as 24 hours
*/

private java.util.Date getEndDate(String endDate) throws java.text.ParseException
{
	java.util.Date oToDate = null;
	Calendar toDateCalendar = new GregorianCalendar();

	toDateCalendar.setTime(new java.util.Date(sdf.parse(endDate).getTime()));
	toDateCalendar.set(Calendar.HOUR_OF_DAY, 24);
	toDateCalendar.set(Calendar.MINUTE, 0);
	toDateCalendar.set(Calendar.SECOND, 0);
	oToDate   = toDateCalendar.getTime();

	return oToDate;
}

/* This function returns today's Date with time as 0 hours
*/
private java.util.Date getStartDate(java.util.Date strStartDate)
{
	java.util.Date oFromdate = null;
	Calendar fromDateCalendar = new GregorianCalendar();

	fromDateCalendar.setTime(new java.util.Date(strStartDate.getTime()));
	fromDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
	fromDateCalendar.set(Calendar.MINUTE, 0);
	fromDateCalendar.set(Calendar.SECOND, 0);
    oFromdate = fromDateCalendar.getTime();

    return oFromdate;
}


/* This function returns the today's Date with time as 24 hours
*/

private java.util.Date getEndDate(java.util.Date endDate)
{
	java.util.Date oToDate = null;
	Calendar toDateCalendar = new GregorianCalendar();

	toDateCalendar.setTime(new java.util.Date(endDate.getTime()));
	toDateCalendar.set(Calendar.HOUR_OF_DAY, 24);
	toDateCalendar.set(Calendar.MINUTE, 0);
	toDateCalendar.set(Calendar.SECOND, 0);
	oToDate   = toDateCalendar.getTime();

	return oToDate;
}

/*
public String getCurrFiscalYear()
{
	 FinancialYear finYr = DateUtils.getFinancialYear();
	 Calendar calendar = new GregorianCalendar();
	 calendar.setTimeInMillis(finYr.getStartOnDate().getTime());


	 int currFiscalYr=calendar.get(calendar.YEAR);
	 int nextFiscalYr= currFiscalYr + 1;


	 return String.valueOf(currFiscalYr)+"-"+String.valueOf(nextFiscalYr);

}
*/
public String getProperCase(String s)
	{
	    String f = "";
	    String s1 = "";
	        String s2 = "";
	      String s0 = "";
 		String f1 = "";

	    if(s!=null)
	    {

	    	if(!s.trim().equals(""))
	    	{
			 f = s.trim();
			 f = f.toLowerCase();
                    StringTokenizer stringTokenizer =new StringTokenizer(f);
                    StringBuffer stringBuffer = new StringBuffer();
                    while(stringTokenizer.hasMoreTokens())
                    {

			s0 = (String)stringTokenizer.nextToken();
			s2 = s0.substring(0,1).toUpperCase();
			f1 = s0.substring(1);
			s1 = s2.concat(f1) + " ";
			stringBuffer.append(s1);
		   }


			 return stringBuffer.toString();
		}
		else
		{
			return f;

	    	}
	    }
	    else
	    {
	    	return f;

	    }

	}

	public String removeNull(String s)
    {
        if(s != null)
            return s;
        else
            return "";
    }


%>