package alex.lopez.vega.util;

import java.io.File;

public class FileUtils {

	public static String getWorkingDirectory() {
		String workingDirectory;
		// here, we assign the name of the OS, according to Java, to a variable...
		String OS = (System.getProperty("os.name")).toUpperCase();
		// to determine what the workingDirectory is. If it is some version of Windows
		if (OS.contains("WIN")) {
			// it is simply the location of the "AppData" folder
			workingDirectory = System.getenv("AppData");
		}
		// Otherwise, we assume Linux or Mac
		else {
			// in either case, we would start in the user's home directory
			workingDirectory = System.getProperty("user.home");
			// if we are on a Mac, we are not done, we look for "Application Support"
			workingDirectory += "/Library/Application Support";
		}
		// we are now free to set the workingDirectory to the subdirectory that is our
		// folder.

		return workingDirectory;
	}

	public static void setupWorkingDirectory() {
		String workDir = getWorkingDirectory();

		File data = new File(workDir + "/template-batch-mailer/data");

		data.mkdirs();
	}

}
