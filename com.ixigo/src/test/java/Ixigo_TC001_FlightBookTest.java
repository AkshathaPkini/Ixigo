import Pages.FlightsPage;
import Pages.HomePage;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;

public class Ixigo_TC001_FlightBookTest extends BaseTest{

    @Test(dataProvider="getTestData")
    public void flightBookTest(HashMap<String,String> data) throws InterruptedException{
        HomePage homePage = new HomePage(driver,test);
        FlightsPage flightsPage;
        homePage.validateHomePage(data.get("title"), Arrays.asList(data.get("navLinks").split(",")));
        flightsPage=homePage.bookRndFlightTicket(data.get("from"),data.get("to"),data.get("departure"),data.get("return"),data.get("travellerType"),data.get("noOfTraveller"));
        flightsPage.validateFlightsPage(data.get("from"),data.get("to"),data.get("departure"),data.get("return"),data.get("noOfTraveller"),Arrays.asList(data.get("stopFilter").split(",")),Arrays.asList(data.get("airlines").split(",")),data.get("stopName"),data.get("flighFare"),data.get("filterHeaders").split(","));
    }
}
