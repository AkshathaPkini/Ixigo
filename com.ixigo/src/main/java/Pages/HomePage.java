package pages;

import common.Constants;
import common.GenericMethods;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends GenericMethods {
    WebDriver driver;
    ExtentTest test;
    FlightsPage flightsPage;

    public HomePage(WebDriver driver, ExtentTest test) {
        super(driver, test);
        this.driver = driver;
        this.test = test;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "ixiLogoImg")
    private WebElement ixigoLogo;

    @FindBy(xpath = "//nav[@class='nav-list']//a")
    private List<WebElement> navLinks;

    @FindBy(xpath = "//h1[text()='Best Flight, Train & Hotel Deals']")
    private WebElement headerText;

    @FindBy(xpath = "//span[text()='Round Trip']")
    private WebElement roundTrip;

    @FindBy(xpath = "//div[@class='form-cntr flight']//div[contains(text(),'From')]/following-sibling::input")
    private WebElement fromInput;

    @FindBy(xpath = "//div[contains(@class,'search')]/button")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@class='feature-banner active-link']")
    private WebElement featureBanner;

    @FindBy(xpath = "//div[contains(@class,'carousel-item shaded')]/a/img")
    private WebElement partnerImageLinks;

    @FindBy(xpath = "//h2[contains(text(),'Best Flight Deals')]")
    private WebElement DealDayheaderText;

    @FindBy(xpath = "//div[contains(@class,'filter-section-items')]//span")
    private List<WebElement> filterSection;

    @FindBy(xpath = "//button[contains(@class,'next')]")
    private WebElement nextMonthCalendarBtn;

    @FindBy(xpath = "(//td[contains(@class,'rd-day-body trip-date')])[1]")
    private WebElement dateData;

    @FindBy(xpath = "//div[@class='flight-origin-fliter']//div[@class='city']")
    private WebElement getFromPlaceText;

    @FindBy(xpath = "//div[@class='form-cntr flight']//div[contains(@class,'clear-input')]")
    private WebElement clearFromPlace;

    @FindBy(xpath = " (//div[contains(@class,'dstn')]//div[@class='city'])[1]")
    private WebElement getToPlaceText;

    @FindBy(xpath = "//td[contains(@class,'selected')]")
    private WebElement selectedStartDate;

    @FindBy(xpath = "//td[contains(@class,'selected')][contains(@class,'trip-endDate')]")
    private WebElement selectedEndDate;

    @FindBy(xpath = "//div[contains(text(),'Travellers')]/following-sibling::input")
    private WebElement travellersInputText;

    @FindBy(xpath = "//div[contains(@class,'flight-dep-cal')]//div[@class='rd-month-label']")
    private List<WebElement> MonthYearNameCalendar;

    private String calendarDepdate="//div[contains(@class,'flight-dep-cal')]//td[contains(@class,'rd-day-body') and not(contains(@class,'prev')) and @data-date='";
    private String calendarRetdate="//div[contains(@class,'flight-ret-cal')]//td[contains(@class,'rd-day-body') and not(contains(@class,'prev')) and @data-date='";
    private String part1="']";
    private String part2="')]";

    private String inputTextbox = "//div[@class='form-cntr flight']//div[contains(text(),'inputText')]/following-sibling::input";
    private String selectPlace="//div[@class='form-cntr flight']//div[(@class='city') and contains(text(),'";

    private String calendarInput = "//div[text()='inputText']";

    private String travellerPart1="//div[(@class='main') and contains(text(),'";
    private String travellerPart2="')]/parent::div/following-sibling::div/span[@data-val='";





    public WebElement inputTextBox(String commonXpath, String inputTextbox) {
        return byXpathAction(commonXpath.replace("inputText", inputTextbox));
    }


    public void validateLogoHeaderTextAndTitle(String title) {
        validateExpectedText(ixigoLogo.getAttribute(Constants.TITLE), title, "The actual logo title is: " + ixigoLogo.getAttribute(Constants.TITLE) + "Expected title is: '" + title);
        validateExpectedText(driver.getTitle(), Constants.FLIGHT_PAGE_TITLE,
                "The Ixigo page actual title is: '" + driver.getTitle() + "Expected title is: '" + Constants.FLIGHT_PAGE_TITLE); // Verify titlevalidateLinks()
    }

    public void validateLinks(List<String> navigatelink) {
        List <String> navigationLinkText = new ArrayList<>();
        for (WebElement navigationLinks : navLinks) {
            navigationLinkText.add(navigationLinks.getText().trim());
        }
        if (!navigationLinkText.retainAll(navigatelink)) {
            test.fail("The Actual Navigation links are: "+navigationLinkText+" \n Expected links are : "+navigatelink);
        }
    }

    public void SelectFromToPlace(String from, String to) throws InterruptedException {
        clickOnElement(roundTrip);
        clickOnElement(clearFromPlace);
        sendKeysToElement(inputTextBox(inputTextbox, Constants.FROM), from);
        clickOnElement(byXpathAction(selectPlace+from+part2));
        Thread.sleep(1500L);
        containsText(getFromPlaceText.getAttribute(Constants.TEXT_CONTENT),from,"The place selected in from is: "+from);
        sendKeysToElement(inputTextBox(inputTextbox, Constants.TO), to);
        clickOnElement(byXpathAction(selectPlace+to+part2));
        Thread.sleep(1500L);
        containsText(getToPlaceText.getAttribute(Constants.TEXT_CONTENT),to,"The place selected in to is: "+to);
    }
    private String getMonth(String dateData){
        return dateData.substring(2,4);
    }

    private String getYear(String dateData){
        return dateData.substring(4,8);
    }


    private boolean calendarNxtBtn(String expectedDate) {
        int Yearvalue=0, monthValue=0;
        int month=Integer.parseInt(getMonth(expectedDate));
        String monthString = new DateFormatSymbols().getMonths()[month-1];
        for (WebElement monthName : MonthYearNameCalendar) {
            String monthYear = monthName.getAttribute(Constants.TEXT_CONTENT);
            if (!monthYear.contains(getYear(expectedDate))) {
                Yearvalue++;
            }
            if (!monthYear.contains(monthString)) {
                monthValue++;
            }

        }
        return (Yearvalue==2|monthValue==2);
    }

    public void calendarSelect(String inputDate,String dateType,String expectedDate) {
        clickOnElement(inputTextBox(calendarInput,inputDate));
            if (calendarNxtBtn(expectedDate)){
                do {
                    clickOnElement(nextMonthCalendarBtn);
                }while(calendarNxtBtn(expectedDate));
            }
        jscriptExecutorClick(byXpathAction(dateType+expectedDate+part1));
    }

    public void enterTravellersDetails(String travellerType,String noOfTraveller){
        clickOnElement(travellersInputText);
        jscriptExecutorClick(byXpathAction(travellerPart1+travellerType+travellerPart2+noOfTraveller+part1));

        //can consider class selection code or override using another method( economy) - for now will go with default
    }

    public void validateHomePage(String title,List<String> navigatelink){
        validateLogoHeaderTextAndTitle(title);
        validateLinks(navigatelink);
    }

    private FlightsPage clickSearchbtn(){
        clickOnElement(searchButton);
        return new FlightsPage(driver,test);
    }
    public FlightsPage bookRndFlightTicket(String from, String to,String departure,String returnDate,String travellerType,String noOfTraveller) throws InterruptedException {
        SelectFromToPlace(from,to);
        enterRoundTrip(departure,returnDate);
        enterTravellersDetails(travellerType,noOfTraveller);
        clickOnElement(searchButton);
        flightsPage=clickSearchbtn();
        return flightsPage;
    }

    public void enterRoundTrip(String departure,String returnDate) {
        calendarSelect(Constants.DEPARTURE,calendarDepdate,departure);
        validateExpectedText(selectedStartDate.getAttribute(Constants.DATA_DATE),departure,"The selected departure date is: "+departure);
        calendarSelect(Constants.RETURN,calendarRetdate,returnDate);
        validateExpectedText(selectedEndDate.getAttribute(Constants.DATA_DATE),returnDate,"The selected return date is: "+returnDate);

    }


}
