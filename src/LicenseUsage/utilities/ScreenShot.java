package LicenseUsage.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShot {
	
	/**
	 * takes screenshot of the present screen
	 * @param name of the saved screenshot
	 * @param driver for the current session
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void takeScreenShot(WebDriver driver,String name) throws Exception{

			Thread.sleep(1000);
			File scrnshot;

			scrnshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			
			FileUtils.copyFile(scrnshot,new File((System.getProperty("user.dir")+"\\screenshots\\")+ name
							+ ".png"));
		
	}

}
