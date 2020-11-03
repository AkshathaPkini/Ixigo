package common;

import java.io.File;

public interface DataSheetConstants {
    /**
     * All the variables in the Interface is public static final and hence Interface is created
     * to avoid the duplication of public static final declaration for variables
     */
    String fs= File.separator;
    // create Custom path based on users using method.
    String TESTDATA_FOLDER_PATH = System.getProperty("user.dir")+fs+"TestData"+fs;
    String TESTCASEID ="TestCaseID";
    String TESTDATA_WORKSHEET = "TestData";
}
