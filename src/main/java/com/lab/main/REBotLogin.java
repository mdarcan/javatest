package com.lab.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.lab.beans.ComBean;
import com.lab.beans.HomeWorksBean;
import com.lab.conf.RELoadConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class REBotLogin extends TimerTask {
	
private ObjectOutputStream oLista;
private ObjectInputStream iLista;
private ObjectOutputStream oListaHomeW;
private ObjectInputStream iListaHomeW;
private ObjectOutputStream oListaClassA;
private ObjectInputStream iListaClassA;
private List<ComBean> comBeanArray;
private List<ComBean> comBeanArrayToSend;
private List<HomeWorksBean> hWBeanArray;
private Date dateLimit;
private Date today;
private RELoadConfig config;
private String msgParameter2;
private String msgParameter3;
private String msgParameter4;

private String msgHomeWParameter2;
private String msgHomeWParameter3;
private String msgHomeWParameter4;

private String msgClassAParameter2;
private String msgClassAParameter3;
private String msgClassAParameter4;

private String botUrl;
private String botHWUrl;
private String botCAUrl;
private String botUrlAlive;
private String results;
private SimpleDateFormat dateFormat;
private SimpleDateFormat dateFormathhmmss;
private Integer threadIndex;
private String threadName;
private boolean errorDetected=false;

public REBotLogin(Integer tIdx, String tName) {
	threadIndex = tIdx;
	threadName = tName;
}

public REBotLogin() {
	threadIndex = 1;
	threadName = "[Thread number 1]";
}

private void printLog(String inMessage) throws IOException {
	Calendar logDateTime = Calendar.getInstance();
	System.out.println( dateFormathhmmss.format(logDateTime.getTime()) + " [" + threadIndex.toString() + ":" + threadName + "] - " + inMessage);
}

public void MainThread() throws UnsupportedEncodingException {
		
    try {

		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		dateFormathhmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
    	today = new Date();
    	
    	config = RELoadConfig.getInstance();

		Integer silentZoneHour = Integer.valueOf(((config.getParameterValue("silentZoneStart")).split(":"))[0]);
		Integer silentZoneMinute = Integer.valueOf(config.getParameterValue("silentZoneStart").split(":")[1]);
		Integer silentZoneSecond = Integer.valueOf(config.getParameterValue("silentZoneStart").split(":")[2]);
		LocalTime silentZone = LocalTime.of(silentZoneHour,silentZoneMinute,silentZoneSecond,0);
		Calendar silentZoneTZ = Calendar.getInstance(TimeZone.getTimeZone(config.getParameterValue("timezone")));
		silentZoneTZ.set(Calendar.HOUR_OF_DAY, silentZone.getHour());
		silentZoneTZ.set(Calendar.MINUTE, silentZone.getMinute());
		silentZoneTZ.set(Calendar.SECOND, silentZone.getSecond());
		silentZoneTZ.set(Calendar.MILLISECOND, 0);
    			
		Date silentZoneTZStart = silentZoneTZ.getTime();
		silentZoneTZ.set(Calendar.HOUR_OF_DAY, silentZone.getHour() + Integer.parseInt(config.getParameterValue("silentZoneDuration")));
		Date silentZoneTZEnd = silentZoneTZ.getTime();
		
		
		Calendar todayTZ = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone"))); 
		todayTZ.setTime(today);
		System.out.println(todayTZ.getTime().toString());
    	if (todayTZ.getTime().after(silentZoneTZStart) && todayTZ.getTime().before(silentZoneTZEnd)) {
    		System.out.println("Zona silenziosa attiva...");
    		return;
    	}
		
		Integer silentTopicHour = Integer.valueOf(((config.getParameterValue("silentTopicsStart")).split(":"))[0]);
		Integer silentTopicMinute = Integer.valueOf(config.getParameterValue("silentTopicsStart").split(":")[1]);
		Integer silentTopicSecond = Integer.valueOf(config.getParameterValue("silentTopicsStart").split(":")[2]);
		LocalTime silentTopic = LocalTime.of(silentTopicHour,silentTopicMinute,silentTopicSecond,0);
		Calendar silentTopicTZ = Calendar.getInstance(TimeZone.getTimeZone(config.getParameterValue("timezone")));
		silentTopicTZ.set(Calendar.HOUR_OF_DAY, silentTopic.getHour());
		silentTopicTZ.set(Calendar.MINUTE, silentTopic.getMinute());
		silentTopicTZ.set(Calendar.SECOND, silentTopic.getSecond());
		silentTopicTZ.set(Calendar.MILLISECOND, 0);
    			
		Date silentTopicTZStart = silentTopicTZ.getTime();
		silentTopicTZ.set(Calendar.HOUR_OF_DAY, silentTopic.getHour() + Integer.parseInt(config.getParameterValue("silentTopicsDuration")));
		Date silentTopicTZEnd = silentTopicTZ.getTime();
    	
    	if (threadIndex == 1) {
	    	String AliveMsg = dateFormathhmmss.format(today) + " (local Timezone) [javaREbot:1.2] Service Alive";
	    	botUrlAlive = config.getParameterValue("boturlalive") + URLEncoder.encode(AliveMsg, "UTF-8");
	    	//results = doHttpUrlConnectionAction(botUrlAlive);
    	}
    	
    	//Sottraggo xx giorni alla data di oggi
    	//dateLimit = dateFormat.parse(dateFormat.format(new Date()));
    	Calendar c = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone"))); 
    	c.setTime(today);
    	c.add(Calendar.DATE, -Integer.parseInt(config.getParameterValue("retainrange")));
    	dateLimit = c.getTime();    	
    	
    	// Definizione datafile delle comunicazioni
		comBeanArray = new ArrayList<ComBean>();    	  	
		comBeanArrayToSend = new ArrayList<ComBean>();
      	String fileName = "/archive/ListaCom.ser";
    		
    	File serFile = new File(fileName);
    	if (!serFile.exists()) serFile.createNewFile();    	
    	try {
    		FileInputStream in_streamLista = new FileInputStream(serFile);
			iLista = new ObjectInputStream(in_streamLista);
			comBeanArray = (List<ComBean>) iLista.readObject();
			iLista.close();
		} catch (Exception e) {}
    			
    	
    	// Definizione datafile del registro di classe
		hWBeanArray = new ArrayList<HomeWorksBean>();    	  	
      	String hWfileName = "/archive/ListaHomeWorks.ser";
    		
    	File hWserFile = new File(hWfileName);
    	if (!hWserFile.exists()) hWserFile.createNewFile();    	
    	try {
    		FileInputStream in_streamListaHomeW = new FileInputStream(hWserFile);
			iListaHomeW = new ObjectInputStream(in_streamListaHomeW);
			hWBeanArray = (List<HomeWorksBean>) iListaHomeW.readObject();
			iListaHomeW.close();
		} catch (Exception e) {}
    	// fine definizione file registro di classe
		   	
		   	    	
		msgParameter2 = new String();
		msgParameter3 = new String();
		msgParameter4 = new String();
		
		msgParameter2 = "In data ";
	    msgParameter3 = " e' stato pubblicato il seguente messaggio sul registro elettronico: '" ; 
		msgParameter4 = "'. Per il dettaglio accedi alla tua area personale. " ; 
		
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		//java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		  
		final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.waitForBackgroundJavaScript(30 * 1000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		long startTime = System.nanoTime();
		printLog("--- INIZIO SESSIONE ---");
		printLog("Connessione Registro Elettronico...");
		final HtmlPage page1 = webClient.getPage(config.getParameterValue("reurl"));
   
		final List<HtmlForm> form = page1.getForms();
		
		HtmlPage page2=null;
		for (HtmlForm frm : form){
		   final HtmlImageInput Imagebutton = frm.getInputByName("ibtnRE");
		   page2 = (HtmlPage) Imagebutton.click();
		}

		printLog("Login al sistema...");
		HtmlPage page3=null;    
		for (HtmlForm frm2 : page2.getForms()){
		    final HtmlTextInput userInput = frm2.getInputByName("txtUser");
		    userInput.setValueAttribute(config.getParameterValue("reuser"));
		    final HtmlPasswordInput passwordInput = frm2.getInputByName("txtPassword");
		    passwordInput.setValueAttribute(config.getParameterValue("repwd"));
		    final HtmlSubmitInput button = frm2.getInputByName("btnLogin");
		    page3 = button.click();
		 }
		
		printLog("Leggo comunicazioni...");
		HtmlImage comImg = page3.getHtmlElementById("IdComunicazioni");    
		HtmlPage page4 = (HtmlPage) comImg.click();

		
		final List<HtmlTable> tables = page4.getByXPath("//table[contains(@class, 'TableRegistroDocente')]");
		for (HtmlTable table : tables)  {
			List<HtmlTableRow> rows = table.getRows();
			if (rows.size() > 1) {
				List<HtmlTableRow> slrows = (List<HtmlTableRow>) rows.subList(1, rows.size());
			    for (final HtmlTableRow row : slrows) {

		        	String data = row.getCell(0).asText();
		        	if (!data.equalsIgnoreCase("Nessuna comunicazione presente")) { 
			        	String comunicazione = row.getCell(2).asText();		       
			        	
			        	if (dateFormat.parse(data).after(dateLimit)) {
				        	if (!IsFound(dateFormat.parse(data), comunicazione)) {
				        		comBeanArray.add(new ComBean(dateFormat.parse(data), comunicazione));
				        		comBeanArrayToSend.add(new ComBean(dateFormat.parse(data), comunicazione));
				        	}
			        	
			        	}
			        }

		        }

		        
			}    
		}

		comBeanArray = SortingComms(comBeanArray, "asc");
		comBeanArrayToSend = SortingComms(comBeanArrayToSend, "asc");
		
		SendComms(comBeanArrayToSend);
		comBeanArray = ShrinkBeans(comBeanArray);		
    	serFile.delete();
    	serFile.createNewFile();
    	FileOutputStream out_streamLista = new FileOutputStream(serFile);
		oLista = new ObjectOutputStream(out_streamLista);
		oLista.writeObject(comBeanArray);
		oLista.flush();
		oLista.close();
		
		
		msgHomeWParameter2 = new String();
		msgHomeWParameter3 = new String();
		msgHomeWParameter4 = new String();
		
		msgHomeWParameter2 = "COMPITI per il ";
		msgHomeWParameter3 = "" ; 
		msgHomeWParameter4 = "" ; 
		
		msgClassAParameter2 = new String();
		msgClassAParameter3 = new String();
		msgClassAParameter4 = new String();
		
		msgClassAParameter2 = "ARGOMENTI lezioni del ";
		msgClassAParameter3 = "" ; 
		msgClassAParameter4 = "" ; 		
		
		printLog("Leggo registro di classe...");
		HtmlImage hWImg = page4.getHtmlElementById("IdREC");    
		HtmlPage recPage = (HtmlPage) hWImg.click();

		
		final List<HtmlTable> hWtables = recPage.getByXPath("//table[contains(@class, 'TableRegistroClasseGenitori')]");
		for (HtmlTable hWtable : hWtables)  {
			List<HtmlTableRow> hWrows = hWtable.getRows();
			if (hWrows.size() > 1) {
				int testsize = hWrows.size();
				List<HtmlTableRow> hWslrows = (List<HtmlTableRow>) hWrows.subList(2, hWrows.size());
			    for (final HtmlTableRow hWrow : hWslrows) {
			    	String hWdata = hWrow.getCell(0).asText().substring(0, 10);
			    			
		        	if (!hWdata.equalsIgnoreCase("xxxxxxxxxx da definire xxxxxxxxx")) {
		        		HtmlTableCell al = hWrow.getCell(1);
		        		HtmlTableCell co = hWrow.getCell(2);		        		
			        	//String compiti = co.asXml().replaceAll("<td.*>((.|\\s)*)<\\/td>", "$1").trim().replaceAll("<br\\/>\\s*", ""); // prende tutto il testo contenuto nel tag <td..></td>
		        		//String compiti = co.asXml().replaceAll("<td.*>((.|\\s)*)<\\/td>", "$1").trim().replaceAll("<br\\/>\\s*", ""); // prende tutto il testo contenuto nel tag <td..></td>
			        	//compiti = compiti.replaceAll("^\\s*/gm", "");
			        	//System.out.println(compiti);
		        		String attivita = new String();
		            	if (todayTZ.getTime().after(silentTopicTZStart) && todayTZ.getTime().before(silentTopicTZEnd)) {
		            		attivita = "";
		            	} else attivita = al.asText();
		        		String compiti = co.asText();
		        		if ((!attivita.isEmpty() || !compiti.isEmpty())) { 
				        	if (dateFormat.parse(hWdata).after(dateLimit)) {
	
			        			HomeWorksBean tempReadBean = IsFoundHomeWork(dateFormat.parse(hWdata));
						       	if (tempReadBean == null) {
						       	  HomeWorksBean tempWriteBean = new HomeWorksBean(dateFormat.parse(hWdata), compiti, attivita);
						       	  if (!compiti.isEmpty()) tempWriteBean.setFlgHomeWSend(true);
						       	  if (!attivita.isEmpty()) tempWriteBean.setFlgClassASend(true);
						          hWBeanArray.add(tempWriteBean);
				        		} else {
				        			int idx = hWBeanArray.indexOf(tempReadBean);
				        			if (!compiti.isEmpty() && tempReadBean.getHWork().compareTo(compiti) != 0) {
				        				tempReadBean.setHWork(compiti);
				        				tempReadBean.setFlgHomeWSend(true);			        				
				        			}
				        			if (!attivita.isEmpty() && tempReadBean.getClassActivity().compareTo(attivita) != 0) {
				        				tempReadBean.setClassActivity(attivita);
				        				tempReadBean.setFlgClassASend(true);		        				
				        			}			        			
				        			hWBeanArray.set(idx, tempReadBean);
				        		}
				        	
				        	}
		        		}
			        }

		        }

		        
			}    
		}

		hWBeanArray = SortingHomeWorks(hWBeanArray, "asc");
		
		SendHomeWorks(hWBeanArray);
		
		hWBeanArray = ShrinkHomeWorksBeans(hWBeanArray);
		
		hWserFile.delete();
		hWserFile.createNewFile();
		
    	FileOutputStream out_streamListaHomeW = new FileOutputStream(hWserFile);
		oListaHomeW = new ObjectOutputStream(out_streamListaHomeW);
		oListaHomeW.writeObject(hWBeanArray);
		oListaHomeW.flush();
		oListaHomeW.close();
		
		webClient.close();		
		
		long difference = System.nanoTime() - startTime;
		printLog("--- FINE SESSIONE ( elapsed : " + 
	                        TimeUnit.NANOSECONDS.toHours(difference) %24 + "h " +
	                        TimeUnit.NANOSECONDS.toMinutes(difference) %60 + "m " + 
	                        TimeUnit.NANOSECONDS.toSeconds(difference) %60 + "s" + " ) ---");
		
	} catch (SecurityException e) {
		e.printStackTrace();

    	
	} catch (Exception e) {
		e.printStackTrace();
		errorDetected=true;
	}
    
    if (errorDetected) {
		String ErrorMsg = dateFormathhmmss.format(today) + " (local Timezone) : javaREbot - PROCEDURE ERROR DETECTED (see logs)";
		try {
			botUrlAlive = config.getParameterValue("boturlalive") + URLEncoder.encode(ErrorMsg, "UTF-8");
			results = doHttpUrlConnectionAction(botUrlAlive);
			errorDetected=false;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
            
  }
  
  
  private void SendComms(List<ComBean> arrayToSend) throws Exception {
	  	
  	if (config.getParameterValue("testComFlag").equalsIgnoreCase("1")) 
  		botUrl=config.getParameterValue("boturltest");
  	else botUrl=config.getParameterValue("boturl");
  	
	for (ComBean cb : arrayToSend) {
	  	String msgParameter = msgParameter2 + dateFormat.format(cb.getData()) + msgParameter3 + cb.getCom() + msgParameter4;
	  	String botUrltmp = botUrl.concat(URLEncoder.encode(msgParameter,"UTF-8"));
        //results = doHttpUrlConnectionAction(botUrltmp);
        printLog("Inviata nuova comunicazione del " + cb.getData().toString());		
	}
	
  }

  
	private void SendHomeWorks(List<HomeWorksBean> arrayToSend) throws Exception {
		  	
	if (config.getParameterValue("testRegFlag").equalsIgnoreCase("1")) {
		botHWUrl=config.getParameterValue("boturltest");
	    botCAUrl=config.getParameterValue("boturltest");
	}
	else { 
		botHWUrl=config.getParameterValue("bothomeworkurl");
		botCAUrl=config.getParameterValue("bottopicsurl");
	}
	
	for (HomeWorksBean cb : arrayToSend) {
	  int idxObj = arrayToSend.indexOf(cb);
	  if (cb.getFlgClassASend()) {
		String msgClassAParameter = "<b>" + msgClassAParameter2 + dateFormat.format(cb.getData()) + "</b> \n" + msgClassAParameter3 + cb.getClassActivity() + msgClassAParameter4;
	  	String botUrltmp = botCAUrl.concat(URLEncoder.encode(msgClassAParameter,"UTF-8"));
	    //results = doHttpUrlConnectionAction(botUrltmp);
	    printLog("Inviato nuovo argomento del " + cb.getData().toString());		
	    cb.setFlgClassASend(false);
	  }	  
	  if (cb.getFlgHomeWSend()) {
		String msgHomeWParameter = "<b>" + msgHomeWParameter2 + dateFormat.format(cb.getData()) + "</b> \n" + msgHomeWParameter3 + cb.getHWork() + msgHomeWParameter4;
	  	String botUrltmp = botHWUrl.concat(URLEncoder.encode(msgHomeWParameter,"UTF-8"));
	    //results = doHttpUrlConnectionAction(botUrltmp);
	    printLog("Inviato nuovo compito del " + cb.getData().toString());
	    cb.setFlgHomeWSend(false);
	  }
	  arrayToSend.set(idxObj, cb);
	}
		
	}
  

  private List<ComBean> ShrinkBeans(List<ComBean> cbarray) {
	  
	List<ComBean> cbtemp = new ArrayList<ComBean>();
    for (ComBean cb : cbarray) {
    	if (cb.getData().after(dateLimit)) {
    		cbtemp.add(cb);
    	}
    }
    
    return cbtemp;
	
  }

  private List<HomeWorksBean> ShrinkHomeWorksBeans(List<HomeWorksBean> cbarray) {
	  
	List<HomeWorksBean> cbtemp = new ArrayList<HomeWorksBean>();
    for (HomeWorksBean cb : cbarray) {
    	if (cb.getData().after(dateLimit)) {
    		cbtemp.add(cb);
    	}
    }
    
    return cbtemp;
	
  }
  
@Override
public boolean cancel() {
	return super.cancel();
}

private boolean IsFound(Date in_Date, String in_comunicazione) throws ClassNotFoundException, IOException {
	   for (ComBean cb : comBeanArray) {
	     if (cb.getData().compareTo(in_Date) == 0 && cb.getCom().compareTo(in_comunicazione) == 0) 
		    return true;
	   }
	   return false;
  }

private HomeWorksBean IsFoundHomeWork(Date in_Date) throws ClassNotFoundException, IOException {
	   for (HomeWorksBean cb : hWBeanArray) {
	     if (cb.getData().compareTo(in_Date) == 0) return cb;
	   }
	   return null;
}



  private static String doHttpUrlConnectionAction(String desiredUrl)
  throws Exception
  {
    URL url = null;
    BufferedReader reader = null;
    StringBuilder stringBuilder;

    try
    {

      url = new URL(desiredUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();     
      connection.setRequestMethod("POST");     
      connection.setDoOutput(true);     
      connection.setReadTimeout(15*1000);
      connection.connect();
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      stringBuilder = new StringBuilder();

      String line = null;
      while ((line = reader.readLine()) != null)
      {
        stringBuilder.append(line + "\n");
      }
      return stringBuilder.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException ioe)
        {
          ioe.printStackTrace();
        }
      }
    }
  }


  
@Override
public void run() {
  try {
	Thread.sleep(8000);
	MainThread();
} catch (Exception e) {
	e.printStackTrace();
}
}


public boolean isAlive() {
	return true;
}

private List<ComBean> SortingComms(List<ComBean> in_cb, String in_mode) 
{    
  int i,j;
  Date key;
  List<ComBean> inputArray= in_cb;

  for (j=1; j<inputArray.size(); j++) 
    {
        key = inputArray.get(j).getData();
        i = j - 1;
        while (i >= 0)
        {
        	if (in_mode.equalsIgnoreCase("asc")) {
              if (key.compareTo(inputArray.get(i).getData()) > 0) {break;}
        	} else {if (key.compareTo(inputArray.get(i).getData()) < 0) {break;}}
            ComBean element=inputArray.get(i+1);      
            inputArray.set(i+1,inputArray.get(i));       
            inputArray.set(i,element);                  
            i--;
        }
        key=inputArray.get(i+1).getData();
   }
   return inputArray;
}

private List<HomeWorksBean> SortingHomeWorks(List<HomeWorksBean> in_cb, String in_mode) 
{    
  int i,j;
  Date key;
  List<HomeWorksBean> inputArray= in_cb;

  for (j=1; j<inputArray.size(); j++) 
    {
        key = inputArray.get(j).getData();
        i = j - 1;
        while (i >= 0)
        {
        	if (in_mode.equalsIgnoreCase("asc")) {
              if (key.compareTo(inputArray.get(i).getData()) > 0) {break;}
        	} else {if (key.compareTo(inputArray.get(i).getData()) < 0) {break;}}
            HomeWorksBean element=inputArray.get(i+1);      
            inputArray.set(i+1,inputArray.get(i));       
            inputArray.set(i,element);                  
            i--;
        }
        key=inputArray.get(i+1).getData();
   }
   return inputArray;
}


}  
  
