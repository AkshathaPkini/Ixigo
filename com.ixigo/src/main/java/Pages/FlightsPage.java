package Pages;

import Common.Constants;
import Common.GenericMethods;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightsPage extends GenericMethods {
    WebDriver driver;
    ExtentTest test;

    protected FlightsPage(WebDriver driver, ExtentTest test) {
        super(driver, test);
        this.driver = driver;
        this.test = test;
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "//div[@class='stops']//div[@class='stop-info']")
    private List<WebElement> stopOptionNames;

    @FindBy(xpath = "//div[contains(text(),'Airlines')]/parent::div//div[@class='arln-info']/div[@class='arln-nm']")
    private List<WebElement> airlineNames;




    private String headerText="//div[@class='fltr-hdr'][contains(text(),'";
    private String part1="')]";
    private String part2="']";
    private String departureTogglePart="//div[text()='";
    private String departureTimeTogglePart="']/parent::div//div[@class='tmng-btn']//button";
    private String departureTimeNameTogglePart="/parent::div//div[@class='tmng-btn']//div[@class='lbl']";
    private String inputTextBoxPart1="//div[contains(text(),'";
    private String inputTextBoxPart2="')]/following-sibling::input";
    private String selectStops="//div[@class='stops']//div[@class='checkbox-list-item ']//div[text()='";
    private String selectStopsPart2="']/../preceding-sibling::span[contains(@class,'checkbox-button')]";
    private String flightFarePart1="//div[@class='price-group']//span[text()<'";
    private String airLineText="']/ancestor::div[@class='price-group']/preceding-sibling::div[@class='time-group']/div[@class='airline-text']//div";
    private String departureTime="']/ancestor::div[@class='price-group']/preceding-sibling::div[@class='time-group']/div[@class='time' and position()='1']";
    By checkPageLoad=By.xpath("//i[contains(@class,'plane-icon')]");
    By searchBtn=By.xpath("//button[text()='Search']");


    public void validateFlightsPage(String from, String to,String departure,String returnDate,String noOfTraveller,List<String>stopOptions,List<String>airlineOptions,String[] filterHeaderNames){
        String placeAbr;
        String[] placeName=byXpathAction(inputTextBoxPart1+Constants.FROM+inputTextBoxPart2).getAttribute(Constants.VALUE).split("-");
        placeAbr=placeName[0].trim();
        containsText(byXpathAction(inputTextBoxPart1+Constants.FROM+inputTextBoxPart2).getAttribute(Constants.VALUE),from,"The place selected in 'from' is: "+from);
        containsText(byXpathAction(inputTextBoxPart1+Constants.TO+inputTextBoxPart2).getAttribute(Constants.VALUE),to,"The place selected in 'to' is: "+to);
        System.out.println("TRAVELLERS:"+noOfTraveller);
        containsText(byXpathAction(inputTextBoxPart1+Constants.TRAVELLERS+inputTextBoxPart2).getAttribute(Constants.VALUE),noOfTraveller,"The number of travellers are: "+noOfTraveller);
        waitForElementToDisappear(checkPageLoad,new WebDriverWait(driver,Constants.WAIT_TIME));
        validateFilterHeaders(filterHeaderNames);
        if(! checkElementExistance(searchBtn)){
            test.fail("The search button is not shown on page");
        }
        validateFilteroptions(stopOptionNames,stopOptions);
        validateFilteroptions(airlineNames,airlineOptions);
    }

    public void validateFilterHeaders(String[] filterHeaders){
        for(String filterHeaderNames:filterHeaders) {
            if (!checkElementExistance(headerText + filterHeaderNames + part1)) {
                test.fail("The filter header: "+filterHeaderNames+" is missing");
            }
        }

    }

    public void selectStops(String stopName){
        clickOnElement(byXpathAction(selectStops+stopName+selectStopsPart2));
        if(!byXpathAction(selectStops+stopName+selectStopsPart2).getAttribute(Constants.CLASS).contains(Constants.SELECTED)){
            test.fail("The stop name "+stopName+"was not selected");
        }
    }

    public void printFareBasedData( String farePrice){
        List<WebElement> flightFare = multipleEleXpath(flightFarePart1+farePrice+part2);
        List<WebElement> airlineNames = multipleEleXpath(flightFarePart1+farePrice+airLineText);
        List<WebElement> flightdepartureTime = multipleEleXpath(flightFarePart1+farePrice+departureTime);
        for(int flights=0;flights<flightFare.size();flights++){
            System.out.println("Flight fare: "+flightFare.get(flights).getText());
            test.info("Flight fare: "+flightFare.get(flights).getText());
            System.out.println("airline names based on flight fare: "+airlineNames.get(flights).getText());
            test.info("airline names based on flight fare: "+airlineNames.get(flights).getText());
            System.out.println(flightdepartureTime.get(flights).getText());
            test.info("airline departure time based on flight fare: "+flightdepartureTime.get(flights).getText());
        }
    }

    public void validateFilteroptions(List<WebElement> actualfilterOptions,List<String> expectedFilterOptions){
        List <String> filterOptionNames= new ArrayList<>();
        for(WebElement filterOption:actualfilterOptions){
            filterOptionNames.add(filterOption.getAttribute(Constants.TEXT_CONTENT));
        }
        if(!filterOptionNames.equals(expectedFilterOptions)){
            test.fail("The Actual values are: "+filterOptionNames+" \n Expected values are : "+expectedFilterOptions);
        }
    }

    public void validateFlightsPage(String from, String to,String departure,String returnDate,String noOfTraveller,List<String>stopOptions,List<String>airlineOptions,String stopName,String flightFare,String[] filterHeaders){
        validateFlightsPage(from,to,departure,returnDate,noOfTraveller,stopOptions,airlineOptions,filterHeaders);
        selectStops(stopName);
        printFareBasedData(flightFare);
    }



}
