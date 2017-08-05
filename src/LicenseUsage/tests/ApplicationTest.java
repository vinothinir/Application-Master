package LicenseUsage.tests;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.ui.Select;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import LicenseUsage.utilities.ScreenShot;
import LicenseUsage.utilities.TestBase;



public class ApplicationTest extends TestBase{
@Test
public void ApplicationTesting(){
		
   ScreenShot img = new ScreenShot();
   //private static Logger log = Logger.getLogger(CCScript1.class);
	//Logger log = Logger.getLogger(LicenseUsage_Report.class);		
	System.setProperty("webdriver.chrome.driver", "C:/ITS/V2.5.2B/workstation/driver/chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    try {
    	System.out.println(data.get("URL"));
	   driver.get(data.get("URL"));
    	
    	//log.info("inside Chrome navigation");
		driver.navigate().to(data.get("URL"));
		driver.manage().window().maximize();
		
		int row,row_count,sortitems=5,i;
		row=1;
		for(i=0;i<sortitems;i++)
		{
		String moviename;
		Select dropdown=new Select(driver.findElement(By.xpath(sort_list)));
		
		dropdown.selectByIndex(i);
		Thread.sleep(2000);
		row_count=driver.findElements(By.xpath(table_link)).size();
		System.out.println("row count:" +row_count);
		int flag;
		if (row_count>=1)
		{
		moviename=driver.findElement(By.xpath("//table[@class='chart full-width']/tbody/tr["+row+"]/td[2]")).getText();
		System.out.println("MovieName:"+moviename);
		flag=1;
		
		}
		else
			flag =0;
		if (flag==1)
			System.out.println("Pass");
		else
			System.out.println("Fail");
		
		}	
      driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
	}	
}



