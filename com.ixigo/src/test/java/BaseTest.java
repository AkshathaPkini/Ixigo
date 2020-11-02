import Common.Config;
import Common.Constants;
import Common.DataSheetConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    WebDriver driver;
    String currentRunningTCName,moduleName,loggedInUser;
    Robot robot;
    protected Config config;
    ExtentHtmlReporter reporter;
    ExtentReports extent;
    ExtentTest test;
    ExcelReadWrite excelReadWrite=new ExcelReadWrite();


    @BeforeTest
    @Parameters({"browser"})
    protected void setup(@Optional(Constants.BROWSER_TYPE) String browserToExecute) throws AWTException, IOException {
        currentRunningTCName = super.getClass().getSimpleName();
        moduleName = currentRunningTCName.substring(0,currentRunningTCName.indexOf("_"));
        loggedInUser=System.getProperty("user.name");
        robot = new Robot();
        config=new Config();
        setURL();
        switch (browserToExecute) {
            case "chrome":
                WebDriverManager.chromedriver().clearPreferences();
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "ie":
                WebDriverManager.iedriver().setup();
                DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
                capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
                InternetExplorerOptions ieoptions = new InternetExplorerOptions(capabilities);
                driver = new InternetExplorerDriver(ieoptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                firefoxProfile.setPreference("browser.privatebrowsing.autostart", true);
                FirefoxOptions fireFoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(fireFoxOptions);
                break;
            case "Edge":
                WebDriverManager.edgedriver().setup();

                break;
            case "opera":
                WebDriverManager.operadriver().setup();
        }
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get(config.getUrl());
        driver.manage().timeouts().pageLoadTimeout(Constants.PAGELOADTIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Constants.IMPLICITWAIT, TimeUnit.SECONDS);
    }


/*
 considering url will vary based on the diff environments we test on
 */
public  Properties readPropertiesFile(String fileName) throws IOException {
    FileInputStream fis = null;
    Properties prop = null;
    try {
        fis = new FileInputStream(fileName);
        prop = new Properties();
        prop.load(fis);
    } catch(FileNotFoundException fnfe) {
        test.fail(fnfe.toString());
    } catch(IOException ioe) {
        test.fail(ioe.toString());
    } finally {
        fis.close();
    }
    return prop;
}

    public void setURL() throws IOException {
        Properties prop = readPropertiesFile(Constants.PROPERTY_FILE_NAME);
        config.setEnv(prop.getProperty(Constants.ENVIRONMENT));
        System.out.println(prop.getProperty(config.getEnv()+Constants.ENV_URL));
        config.setUrl(prop.getProperty(config.getEnv()+Constants.ENV_URL));
    }

    @DataProvider
    public Object[][] getTestData() throws IOException {
        int dataHeader = 0,colNum = 0,dataRows=1;
        String columnName="",columnData="";Object testData[][] ;
        HashMap<String,String> hm = new HashMap<String,String>();
        String fileName = moduleName +"_"+DataSheetConstants.TESTDATA_WORKSHEET;
        String filePath =DataSheetConstants.TESTDATA_FOLDER_PATH+config.getEnv()+ File.separator+fileName+".xlsx";
        String sheetName = DataSheetConstants.TESTDATA_WORKSHEET;
        excelReadWrite.getSheet(filePath,sheetName);
        if (excelReadWrite.isSheetExist(sheetName)) {
            for (int row=0;row<excelReadWrite.getRowCount(); row++) {
                if (currentRunningTCName.equalsIgnoreCase(excelReadWrite.getCellData(DataSheetConstants.TESTCASEID, row))) {
                    dataHeader = row+1;
                    colNum = excelReadWrite.getColumnCount(dataHeader);
                    break;
                }
            }
            if (dataHeader<1) {
                throw new SkipException("The test class Name "+ currentRunningTCName +" doesn't exist in Test Data sheet");
            }
            while (excelReadWrite.getCellData(DataSheetConstants.TESTCASEID, dataHeader+dataRows)!="") {
                dataRows++;
            }
            testData = new Object[dataRows-1][1];
            for (int dataRownum=1;dataRownum<dataRows;dataRownum++) {
                for(int col=0;col<colNum;col++) {
                    columnName=excelReadWrite.getCellData(col, dataHeader);
                    columnData=excelReadWrite.getCellData(col, dataHeader+dataRownum);
                    hm.put(columnName, columnData);
                    testData [dataRownum-1][0]=hm;
                }
            }
            return testData;
        }
        else
            throw new SkipException("Sheet "+sheetName+" doesn't exist");

    }

    @DataProvider
    public Object[][] multipleTestData() throws IOException {
        int dataHeader = 0,colNum = 0,dataRows=1;
        String columnData="";Object testData[][] ;
        String fileName = moduleName +"_"+DataSheetConstants.TESTDATA_WORKSHEET;
        String filePath =DataSheetConstants.TESTDATA_FOLDER_PATH+config.getEnv()+File.separator+fileName+".xlsx";
        String sheetName = DataSheetConstants.TESTDATA_WORKSHEET;
        excelReadWrite.getSheet(filePath,sheetName);
        if (excelReadWrite.isSheetExist(sheetName)) {
            for (int row=0;row<excelReadWrite.getRowCount(); row++) {
                if (currentRunningTCName.equalsIgnoreCase(excelReadWrite.getCellData(DataSheetConstants.TESTCASEID, row))) {
                    dataHeader = row+1;
                    colNum = excelReadWrite.getColumnCount(dataHeader);
                    break;
                }
            }
            if (dataHeader<1) {
                throw new SkipException("The test class Name "+ currentRunningTCName +" doesn't exist in Test Data sheet");
            }
            while (excelReadWrite.getCellData(DataSheetConstants.TESTCASEID, dataHeader+dataRows)!="") {
                dataRows++;
            }
            testData = new Object[dataRows-1][colNum];
            for (int dataRownum=0;dataRownum<dataRows-1;dataRownum++) {
                for(int col=0;col<colNum;col++) {
                    columnData=excelReadWrite.getCellData(col, dataHeader+dataRownum);
                    testData[dataRownum][col] = columnData;
                }
            }
            return testData;
        }
        else
            throw new SkipException("Sheet "+sheetName+" doesn't exist");

    }

    @BeforeClass
    public void initializeExtentReport(){
        reporter = new ExtentHtmlReporter(new File(Constants.EXTENTREPORT_PATH)+File.separator+ currentRunningTCName +new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())+".html");
        reporter.config().enableTimeline(true);
        reporter.config().setDocumentTitle("Ixigo-Flight");
        reporter.config().setReportName(currentRunningTCName);
        reporter.config().setTheme(Theme.STANDARD);
        extent = new ExtentReports();
        extent.setSystemInfo("SeleniumVersion", "3.12"); //check from maven Pom
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("User", loggedInUser);
        extent.setSystemInfo("Environment", config.getEnv());
        extent.setSystemInfo("BrowserName", driver.getClass().getSimpleName());
        extent.attachReporter(reporter);
        test = extent.createTest(currentRunningTCName);
    }
    @AfterMethod
    public void TakeScreenshotOnFailure(ITestResult testResult) throws IOException{
        if(testResult.getStatus()==ITestResult.FAILURE) {
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage img = robot.createScreenCapture(screenSize);
            File dir = new File(Constants.TESTREPORT_SCREENSHOT_PATH+config.getEnv()+File.separator+ moduleName +File.separator+"Week"+Constants.WEEK_NUMBER);
            dir.mkdirs();
            String path =Constants.TESTREPORT_SCREENSHOT_PATH+config.getEnv()+File.separator+ moduleName +File.separator+"Week"+Constants.WEEK_NUMBER+File.separator+ currentRunningTCName +"_"+Constants.BROWSER_TYPE+System.currentTimeMillis()+".Jpg";
            test.fail("The Failure stack trace: "+testResult.getThrowable().getMessage());
            //System.out.println(path);
            ImageIO.write(img, "png",new File(path));
            test.log(Status.FAIL, "Screenshot below: ");
            test.addScreenCaptureFromPath(path);
        }
        // driver.quit();
        extent.flush();
    }

    @AfterSuite
    public void teardown() throws IOException {
/*
        if(driver!=null) {
            Runtime rt = Runtime.getRuntime();
            rt.exec("taskkill /F /IM chromedriver.exe");
            rt.exec("taskkill /F /IM chrome.exe");


        }
*/
    }



}
