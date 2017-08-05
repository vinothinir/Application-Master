/*Class to handle all scripts Data externalization*/

package LicenseUsage.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.experitest.client.InternalException;
import LicenseUsage.ObjectRepository.ObjectRepository;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class TestBase extends ObjectRepository{
	private int port = 8889;
	private String host = "localhost";
	public MyClient client = null;
	//protected AndroidApplication application = null;
	protected String usedDeviceName = null;
	protected Map<String,String> data = null;
	protected int dataIndex = 0;
	protected boolean didFail = false;
	protected Throwable exception = null;
	protected String devicelog ="";
	private static Logger log=Logger.getLogger(TestBase.class);

	protected Map<String, String> dataMap = null;
	String reportName = "";
	String reportslocation = getProperty("user.dir") + "\\temp_reports\\";
	String reportslocationpdf = reportslocation + "pdf\\";
	File teststatusfile = null;
	protected String teststatus = "";

	@Before
	public void setUp() throws NumberFormatException, BiffException, IOException {
		System.out.println("Inside Setup");
		setLogger();
		//initProjectBaseDirectory();
		System.out.println("TestName"+ getTestName());
		getTestData(getTestName());
		/*initClientAndDevice();
		client.setDevice(getProperty("device.name"));
		clearCache();
		client.assertTestTrue("Preconditions Satisfied");*/
	}
	
public void createteststatusfile() throws IOException {
		teststatusfile = new File(reportslocation + "teststatus.txt");
		teststatusfile.createNewFile();
		BufferedWriter output = null;
		output = new BufferedWriter(new FileWriter(teststatusfile));
		output.write(teststatus);
		output.close();
	}
public void tearDown()throws IOException{
        
        if (didFail) {
            System.out.println("<--- FAIL --- " + exception.toString() + " --->");
            devicelog = client.getDeviceLog(); 
	teststatus = "Failed";
	createteststatusfile();
                
        }
        else {
			teststatus = "Passed";
			createteststatusfile();
            devicelog = client.getDeviceLog();
            System.out.println("<--- SUCCESS --- " + usedDeviceName + " --- " + getTestName() + " --- data (" + dataIndex + ") --->");
        }
        if((didFail) && (exception == null)) {
            client.report("Test failed with null exception", false);
                        
        }
        if((didFail) && (exception instanceof InternalException)) {
            String cause = ((InternalException)exception).getCauseType();
		teststatus="Failed";
            if (cause != null) {
                switch (cause) {
                    case ("UNKNOWN"):
                                    //
                        break;
                    case ("INTERNAL_ERROR"):
                                    //
                        break;
                    case ("STOP_BY_USER"):
                                    //
                        break;
                    case ("USER_INPUT_ERROR"):
                                    //
                        break;
                    case ("DEVICE_INTERACTION"):
                                    //
                        break;
                    case ("ELEMENT_IDENTIFICATION"):
                                    //
                        break;
                    case ("OPERATION_FAILURE"):
                                    //
                        break;
                    }
            }
        }
        if ((didFail) && (!(exception instanceof AssertionError))) {
				teststatus="Failed";
                        client.report(exception.toString(), false);
        }

        
        client.generateReport();
	this.renamefile();
}
	
	
	
	public void initClientAndDevice() throws NumberFormatException, BiffException, IOException {
		initHost();
		initPort();
		client = new MyClient(host,port);
		initDevice();
		initProjectBaseDirectory();
		getTestData(getTestName());
		initReport();
	}
	
	
	private void initHost(){
		if (host == null) {
			host = getProperty("host");
		}
	}
	
	private void initPort(){
		if (port == 0) {
			port = Integer.valueOf(getProperty("port"));
		}
	}
	
	private void initDevice(){
		if (usedDeviceName != null) {
			return;
		}
		usedDeviceName = getProperty("device.name"); 
			//usedDeviceName = getProperty("device.name").contains(":") ? getProperty("device.name") : client.waitForDevice(getProperty("device.name"), 300000); 

	}
	
	private void initProjectBaseDirectory(){
		String projectBaseDirectory = getProperty("user.dir") + getProperty("project.base.directory");
		client.setProjectBaseDirectory(projectBaseDirectory);
		System.out.println("Exit Project Setup");
	}
	
	private void initReport()throws IOException{
//		String mainFolder = getProperty("execution.start.time");
//		String suiteName = "";
//		if (mainFolder == null) {
//			mainFolder = "Single Tests\\";
//		}
//		else {
//			mainFolder += "\\";
//			suiteName = getProperty("suite.name") + " ";
//		}
//		String reportFolder = getProperty("user.dir") + "\\reports\\" + mainFolder + suiteName + usedDeviceName.split(":")[1];
//		String reportName = getTestName() + " (data " + dataIndex + ")";
//		client.setReporter("xml", reportFolder, reportName);
		
		String reportName = generateFileName(getTestName());
		System.out.println("mainFolder::"+reportName);

		client.setReporter("pdf", getProperty("user.dir") + "\\reports\\pdf\\"+reportName, reportName);
		client.setReporter("pdf", getProperty("user.dir") + "\\temp_reports\\pdf");
	}
	
	/**
     * Generate the file name
     * @param suitname
     * @return newFile name
     */
	public String generateFileName(String suitName){

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");
		Date date= new Date();
		
		String now=sdf.format(date);
		String newName=suitName.concat("_").concat(now);


		return newName;
	} 
	
	private void getTestData(String testName) throws BiffException, IOException {
		if (dataIndex == -1) {
			return;
		}
		
		data = new HashMap<String,String>();
		Sheet dataSheet = Workbook.getWorkbook(new File(getProperty("data.spreadsheet.name"))).getSheet(getProperty("opco.name"));
		dataIndex=dataSheet.findCell(testName).getRow();
		for (int i=1; i<dataSheet.getColumns(); i++) {
			String key = dataSheet.getCell(i,0).getContents();
			String value = dataSheet.getCell(i,dataIndex).getContents();
			data.put(key, value);
		}
		
	}
	
	public static String getProperty(String property){
		if(System.getProperty(property) != null){
			return System.getProperty(property);
		}
		File setupPropFile = new File("setup.properties");
		if(setupPropFile.exists()){
			Properties prop = new Properties();
			FileReader reader;
			try {
				reader = new FileReader(setupPropFile);
				prop.load(reader);
				reader.close();
				return prop.getProperty(property);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String getTestName() {
		String[] klassNameSplit = this.getClass().getName().split("\\.");
		return klassNameSplit[klassNameSplit.length-1];
	}
	
	public void clearCache(){
		
		client.launchWebApplication(data.get("URL"));
		client.closePopUp();
		 client.hybridClearCache(true, true);
//		client.click("NATIVE", native_chrome_moreoptions_link, 0, 1);
//        client.click("NATIVE", native_chrome_moreoptions_history_link, 0, 1);
//        client.click("WEB", web_chrome_moreoptions_clearbrowsing_btn, 0, 1);
//        client.click("NATIVE", web_chrome_moreoptions_ok_btn, 0, 1);
		client.applicationClose(getProperty("chrome.package"));
		
	}
	@Rule
	public TestWatcher rule = new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			didFail = true;
			exception = e;
			try {
				tearDown();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		@Override
		protected void succeeded(Description description) {
			try {
				tearDown();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	public void setUpFamily(String familyMember) throws NumberFormatException, BiffException, IOException {
		initClientAndDevice();
		if(familyMember.equalsIgnoreCase("spouse"))
		{
		client.setDevice(getProperty("device.name.spouse"));
		}
		else if(familyMember.equalsIgnoreCase("child"))
		{
			client.setDevice(getProperty("device.name.child"));
			}
		else
		{
			client.setDevice(getProperty("device.name"));
		}
		
		clearCache();
		client.assertTestTrue("Preconditions Satisfied");
	}
	/**
	 * configures log4j for logging purposes
	 */
	
   public void setLogger(){
		
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		
		 ConsoleAppender console = new ConsoleAppender();
	
		  String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		  console.setLayout(new PatternLayout(PATTERN)); 
		  console.setThreshold(Level.INFO);
		  console.activateOptions();
		  
		  Logger.getRootLogger().addAppender(console);

		  FileAppender fa = new FileAppender();
		  fa.setFile(getProperty("securenet.logs"));
		  fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		  fa.setThreshold(Level.INFO);
		  fa.setAppend(false);
		  fa.activateOptions();

		  Logger.getRootLogger().addAppender(fa);
		  System.out.println("ExitLogSetup");
	}
   public void clearBrowsingData(MyClient client)
   {
   	
   		  client.applicationClearData("com.android.chrome");
   		        client.launch("chrome:http://google.com", true, false);
   		        if (client.waitForElement("NATIVE", "xpath=//*[@id='terms_accept']", 0, 20000)){
   		        	client.click("NATIVE", "xpath=//*[@id='terms_accept']", 0, 1);
   		        }
   		        if (client.waitForElement("NATIVE", "xpath=//*[@id='negative_button']", 0, 20000)){
   		        	client.click("NATIVE", "xpath=//*[@id='negative_button']", 0, 1); 
   		        }
   	
   }
	public void renamefile(){
		File oldName = new File(getProperty("user.dir") + "\\reports\\pdf\\" + reportName);
		File newName = new File(getProperty("user.dir") + "\\reports\\pdf\\" + reportName + "_" + teststatus);
		if (oldName.renameTo(newName)) {
			System.out.println("renamed");
		} else {
			System.out.println("Error");
		}
	}

	public Map<String, String> createMap() {
		dataMap = new HashMap<>();
		String fileData = readFile("hpqcmap.txt");
		String[] dataArray = fileData.split(";;");
		for (String data : dataArray) {
			dataMap.put(data.split("~~")[0], data.split("~~")[1]);
		}
		return dataMap;
	}

	public String readFile(String filename) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	        

   } 
		