import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(BrowserStepDefinitions.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @Given("the browser is set up")
    public void the_browser_is_set_up() {
        logger.info("Setting up the browser");
        // You might initialize WebDriver options here if needed.
    }

    @When("I start the browser")
    public void i_start_the_browser() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
        logger.info("Browser started and maximized");
    }

    @Then("the browser should be open")
    public void the_browser_should_be_open() {
        assertNotNull("Browser did not open", driver);
        logger.info("Browser is open");
    }

    @When("I navigate to {string}")
    public void i_navigate_to(String url) {
        driver.get(url);
        logger.info("Navigated to URL: {}", url);
    }

    @Then("I should see the cookie consent popup")
    public void i_should_see_the_cookie_consent_popup() {
        try {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("didomi-notice-agree-button")));
            assertTrue("Cookie consent popup is not visible", popup.isDisplayed());
            logger.info("Cookie consent popup is visible");
        } catch (Exception e) {
            takeScreenshot("cookie_popup_error");
            throw e;
        }
    }

    @When("I accept all cookies")
    public void i_accept_all_cookies() {
        WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("didomi-notice-agree-button")));
        acceptButton.click();
        logger.info("Accepted all cookies");
        
        // Wait until the popup disappears
        wait.until(ExpectedConditions.invisibilityOf(acceptButton));
    }

    @Then("I should be on the T-Mobile homepage")
    public void i_should_be_on_the_t_mobile_homepage() {
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
        String actualTitle = driver.getTitle();
        logger.info("Actual page title: {}", actualTitle);
        assertTrue("Page title does not contain 'T-Mobile'", actualTitle.contains("T-Mobile"));
        logger.info("Title verification passed");
    }
    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed");
        }
    }

    private void takeScreenshot(String fileName) {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File("target/screenshots/" + fileName + ".png");
            FileUtils.copyFile(scrFile, destFile);
            logger.info("Screenshot saved as " + destFile.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
        }
    }
}
