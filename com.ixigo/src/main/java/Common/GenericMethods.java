package common;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;


import java.util.List;

import static org.testng.Assert.assertEquals;

public class GenericMethods {
    protected WebDriver driver;
    protected WebDriverWait conditionalWait ;
    Actions action;
    ExtentTest test;


    protected GenericMethods(WebDriver driver, ExtentTest test) {
        this.driver = driver;
        this.test = test;
        conditionalWait = new WebDriverWait(driver,60);
        action = new Actions(driver);
    }



    protected boolean checkElementExistance(String xpath) {
        try {
            driver.findElement(By.xpath(xpath));
            return true;
        }
        catch(NoSuchElementException |NullPointerException e) {
            return false;
        }
    }

    protected boolean checkElementExistance(By element) {
        try {
            driver.findElement(element);
            return true;
        }
        catch(NoSuchElementException |NullPointerException e) {
            return false;
        }
    }
    public void clickOnElementAndSendKeys(WebElement ele,String inputText) {
        clickOnElement(ele);
        ele.sendKeys(inputText);
    }
    public void waitForElementToDisappear(By ele,WebDriverWait conditionalWait) {
        conditionalWait.until(ExpectedConditions.invisibilityOfElementLocated(ele));
    }
    protected boolean waitForVisibilityOfElement(String xpath, WebDriverWait conditionalWait) {
        try {
            conditionalWait.until(ExpectedConditions.visibilityOf(byXpathAction(xpath)));
            return true;
        }
        catch(StaleElementReferenceException e) {
            conditionalWait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(byXpathAction(xpath))));
            return true;
        }
        catch(ElementNotVisibleException| TimeoutException e) {
            return false;
        }
    }
    protected void clickOnElement(WebElement ele) {
        try {
            conditionalWait.until(ExpectedConditions.elementToBeClickable(ele));
            ele.click();
        }
        catch(ElementClickInterceptedException ec) {
            // ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+ele.getLocation().x+")");
            // ele.click();
            action.moveToElement(ele).click().build().perform();

        }
        catch(StaleElementReferenceException e) {
            conditionalWait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(ele)));
            ele.click();
        }
        catch (Exception e) {
            test.fail(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
    }


    protected void jscriptExecutorClick(WebElement ele) {
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();",ele);
    }

    protected void actionClick(WebElement ele) {
        action.moveToElement(ele).click().build().perform();
    }
    protected void sendKeysToElement(WebElement ele,String text) {
        conditionalWait.until(ExpectedConditions.elementToBeClickable(ele));
        ele.sendKeys(text);
    }
    protected void validateExpectedText(WebElement ele,String expectedText, String message) {
        assertEquals(ele.getText().trim(), expectedText,message);
    }
    protected void validateExpectedText(String actualText,String expectedText, String message) {
        assertEquals(actualText, expectedText,message);
    }
    protected WebElement byXpathAction(String xpath) {
        WebElement element=null;
        try {
            element = driver.findElement(By.xpath(xpath));
        }catch(StaleElementReferenceException e) {
            conditionalWait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
        }
        return element;
    }
    protected List<WebElement>  multipleEleXpath(String xpath) {
     List<WebElement> webElementList=driver.findElements(By.xpath(xpath)) ;
     return webElementList;
    }

    protected void SelectDate(WebElement calendar,WebElement nextBtn,String year, String monthName, String day, WebDriver driver) {
        calendar.click();



    }
    protected void assertEquals(Object actual,Object expected, String message) {
        if (actual != null && expected != null && actual.equals(expected)) {
            Reporter.log("The actual value is as expcected: " + expected + " " + message);
            test.log(Status.PASS, message);
        } else {
            Reporter.log("The expected value was: " + expected + "/n the actual value is: " + actual);
            test.log(Status.FAIL, "The expected value was: " + expected + " the actual value is: " + actual);
        }
    }

    protected void containsText(String actual,String expected, String message){
        if(actual != null && expected != null && actual.contains(expected)){
            Reporter.log("The actual value is as expcected: " + expected + " " + message);
            test.log(Status.PASS, message);
        }
        else {
            Reporter.log("The expected value was: " + expected + "/n the actual value is: " + actual);
            test.log(Status.FAIL, "The expected value was: " + expected + " the actual value is: " + actual);
        }
    }
}
