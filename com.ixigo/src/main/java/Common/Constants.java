package Common;

import java.io.File;
import java.text.SimpleDateFormat;

public interface Constants {
   int PAGELOADTIMEOUT = 60;
   int SLEEP_WAIT=3000;
   int IMPLICITWAIT = 3;
   String WEEK_NUMBER=new SimpleDateFormat("w").format(new java.util.Date());
   String BROWSER_TYPE="chrome";
   String fs= File.separator;
   String TESTREPORT_SCREENSHOT_PATH=System.getProperty("user.dir")+fs+"ExtentReports"+fs;
   String EXTENTREPORT_PATH=TESTREPORT_SCREENSHOT_PATH+fs+"Week-"+WEEK_NUMBER;
   String FLIGHT_PAGE_TITLE="ixigo - Flights, Train Reservation, Hotels, Air Tickets, Bus Booking";
   String TITLE="title";
   String PROPERTY_FILE_NAME=System.getProperty("user.dir")+fs+"TestData"+fs+"config.properties";
   String USERNAME="UserName";
   String ENVIRONMENT = "Environment";
   String ENV_URL ="_Url";

   String FROM="From";
   String TO="To";
   String DEPARTURE="Departure";
   String RETURN="Return";
   String DATA_DATE="data-date";
   String TRAVELLERS="Travellers";
   String TEXT_CONTENT="textContent";
   String VALUE="value";
   int WAIT_TIME=180;
   String CLASS="class";
   String SELECTED="selected";

}

