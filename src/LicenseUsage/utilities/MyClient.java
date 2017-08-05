package LicenseUsage.utilities;

//import java.util.Date;

import org.junit.Assert;

import com.experitest.client.Client;

public class MyClient extends Client {
	
	
	private int index=Integer.valueOf(TestBase.getProperty("index"));
	private int timeout=Integer.valueOf(TestBase.getProperty("time.out"));
	private boolean testCondition;
	private int clickCount=Integer.valueOf(TestBase.getProperty("click.count"));
	
	public MyClient(String host, int port){
		super(host, port, true);
	}
		
	public void assertTestReport(boolean condition, String messageIfTrue, String messageIfFalse) {
		if (condition) {
			this.report("TestPass: " + messageIfTrue, true);
		} 
		else {
			this.report("TestFail: " + messageIfFalse, false);
		}
		Assert.assertTrue("TestFail: " + messageIfFalse, condition);
	}
	public void assertTestFail(String messageFalse){
		this.report("TestFail: " + messageFalse, false);
	}
	public void assertTestTrue(String messageTrue){
		this.report("TestPass: " + messageTrue, true);
	}
	public void assertTestPass(String messageTrue){
		this.report("TestPass: " + messageTrue, true);
	}
	
	/*Method to verify web Element
	 * */
	public boolean verifyWebElementFound(String element){
		try{
			if(this.waitForElement("WEB", element, index, timeout)){
				testCondition=true;
				assertTestTrue("TestPass: Element Found as expected");
			}else{
				testCondition=false;
				assertTestFail("TestFail :Element not found");
			}
		}catch(Exception e){
			assertTestFail("Exception occured"+e);
		}
		return testCondition;
	}
	
	/*Method to validate Element not found
	 * */
	public boolean verifyWebElementNotFound(String element){
		try{
			if(this.waitForElement("WEB", element, index, timeout)){
				testCondition=false;
				assertTestFail("TestFail: ElementFound");
			}else{
				testCondition=true;
				assertTestTrue("TestPass: Element not Found as expected");
				
			}
		}catch(Exception e){
			assertTestFail("Exception occured"+e);
		}
		return testCondition;
	}
	
	/*Method to verify Native Element
	 * */
	public void verifyNativeElementFound(String element){
		try{
			if(this.waitForElement("NATIVE", element, index, timeout)){
				assertTestTrue("TestPass: Element Found as expected");
			}else{
				assertTestFail("TestFail :Element not found");
			}
		}catch(Exception e){
			assertTestFail("Exception occured"+e);
		}
	}
	
	/* Method to verify overlay icon in WEB zone
	 * @param zone element index time to wait for the element
	 * */
	
//	public void verifyWebOverLayICON(String element){
//		try{
//			if(this.waitForElement("WEB", element, index, timeout)){
//				assertTestTrue("TestPass: Overlay icon exist Secure Net is active in this MSISDN");
//			}else{
//				assertTestFail("TestFail: Overlay icon does not exist Please activate Secure Net in this MSISDN");
//			}
//		}catch(Exception e){
//			this.assertTestFail("Exceptions occured:"+e);
//		}
//	}
//	
	/*Method to launch the application
	 * */
	
	public void launchWebApplication(String url){
		try{
			String mainurl="chrome:"+url;
		this.launch(mainurl, true, false);
		}catch(Exception e){
			this.assertTestFail("Exceptions occured:"+e);
		}
	}
	
	/*Method to Close web application
	 * */
	public void exitWebApplication(){
		try{
			this.applicationClose(TestBase.getProperty("chrome.package"));
		}catch(Exception e){
			assertTestFail("Exception occured :"+e);
		}
	}
	
	/*Method to validate MSISDN
	 * @param 
	 * */
	public void validateMSISDN(String actualMSISDN, String expectedMSISDN){
		long l_actual_msisdn, l_expected_msisdn;
		try{
		    if (expectedMSISDN.equals("")) {
				this.assertTestFail("Missing MSISDN in Excel!");
		    } else {
		    	l_actual_msisdn=Long.valueOf(actualMSISDN.replaceAll("\\D", ""));
		    	l_expected_msisdn=Long.valueOf(expectedMSISDN.replaceAll("\\D", ""));
		    	if(l_actual_msisdn==l_expected_msisdn){
		    		this.assertTestTrue("Correct MSISDN");
		    	}else{
		    		this.assertTestFail("Wrong MSISDN");
		    	}
		    }
		}catch(Exception e){
			this.assertTestFail("TestFail: Exception occured during MSISDN validation");
		}
	}
	
	/*Method to validate Expected and Actual Text and return boolean value
	 * */
	public boolean validateText(String expected,String actual){
		try{
			if(actual.trim().contains(expected)){
				testCondition=true;
			}else if(actual.trim().equalsIgnoreCase(expected)){
				testCondition=true;
			}else{
				testCondition=false;
			}
		}catch(Exception e){
			assertTestFail("Exception Occured :"+e); 		
		}
		return testCondition;
	}
	
	/*Method to get Web Element Text propertise
	 * @param zone, element, index, 
	 * */
	public String getWebText(String element){
		
		if (verifyWebElementFound(element))
			return this.elementGetText("WEB", element, index).trim();
		else
			return "";
	}
	
	/*Method to get Native Element Text propertise
	 * @param zone, element, index, 
	 * */
	public String getNativeText(String element){
		return this.elementGetText("NATIVE", element, index);
	}
	
	/*Method to set text in text box or field
	 * */
	public void setWebText(String element, String text){
		try{
			verifyWebElementFound(element);
			this.elementSendText("WEB", element, index, text);
		}catch(Exception e){
			assertTestFail("Exception Occured: "+e);
		}
	}
	
	/*Method to find property of a web element
	 * */
	public String getWebProperty(String element,String property){
		return this.elementGetProperty("WEB", element, index, property);
	}
	
	/*Method to click on web element
	 * */
	public void webClick(String element) {
		try{
			verifyWebElementFound(element);
			this.click("WEB", element, index, clickCount);
			//long before = new Date().getTime();
			//this.hybridWaitForPageLoad(30000);
			//long after = new Date().getTime();
			//return after - before;
		}catch(Exception e){
			assertTestFail("Exception Occured:"+e);
		}
	}
	
	/*Method to close native Google Chrome pop-up boxes like asking for translation
	 */
	public void closePopUp() {
		
		if (this.waitForElement("NATIVE", "xpath=//*[@contentDescription='Close']", 0, 3000)) {
			// closing first pop-up
			this.click("NATIVE", "xpath=//*[@contentDescription='Close']", 0, 1);
			// closing possible second pop-up
			if (this.waitForElement("NATIVE", "xpath=//*[@contentDescription='Close']", 0, 2000)) {
				// closing second pop-up
				this.click("NATIVE", "xpath=//*[@contentDescription='Close']", 0, 1);
			}
			
		}
		if(this.waitForElement("NATIVE", "xpath=//*[@id='infobar_close_button']", 0, 3000)){
			this.click("NATIVE", "xpath=//*[@id='infobar_close_button']", 0, 1);
		}
		if(this.waitForElement("NATIVE", "xpath=//*[@id='infobar_close_button']", 0, 2000)){
			this.click("NATIVE", "xpath=//*[@id='infobar_close_button']", 0, 1);
		}
	}
	
}