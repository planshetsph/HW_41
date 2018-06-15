package core;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Firefox {

	private static final String DRIVER_PATH = "./resources/windows/geckodriver.exe";
	private static final String URL = "https://www.facebook.com/";
	private static String email;
	private static String password;

	public static void main(String[] args) {

		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter facebook email:");
		email = scanner.nextLine();
		System.out.println("Enter facebook password:");
		password = scanner.nextLine();
		scanner.close();

		String os = System.getProperty("os.name");

		if (!os.toUpperCase().contains("WINDOWS")) {
			throw new IllegalArgumentException("The OS is " + os + ", but requered WINDOWS");
		}

		System.setProperty("webdriver.gecko.driver", DRIVER_PATH);

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("dom.webnotifications.enabled", false);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		FirefoxOptions options = new FirefoxOptions();
		options.merge(capabilities);

		WebDriver driver = new FirefoxDriver(options);
		driver.manage().window().maximize();
		driver.get(URL);

		WebDriverWait wait = new WebDriverWait(driver, 15);

		String title = driver.getTitle();

		String copyright = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\'pageFooter\']/div[3]/div/span")))
				.getText();

		wait.until(ExpectedConditions.titleIs("Facebook — войдите или зарегистрируйтесь"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email"))).clear();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email"))).sendKeys(email);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pass"))).clear();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pass"))).sendKeys(password);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='u_0_2' or @id='u_0_8']"))).click();

		WebDriverWait wait2 = new WebDriverWait(driver, 20);
		wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(@class,'_1vp5')]"))).click();

		String friends = wait2
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@data-tab-key='friends']"))).getText();

		friends = friends.replaceAll("\\D+", "");

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userNavigationLabel"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(.,'Log Out')]"))).click();

		driver.quit();

		System.out.println("Browser is: Firefox");
		System.out.println("Title of the page: " + title);
		System.out.println("Copyright: " + copyright);
		System.out.println("You have " + friends + " friends");

	}
}
