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
import java.util.List;
import org.openqa.selenium.NoSuchElementException;

import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.interactions.Actions;

public class BrowserStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(BrowserStepDefinitions.class);
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;


    @Given("the browser is set up")
    public void the_browser_is_set_up() {
        logger.info("Setting up the browser");
        // You might initialize WebDriver options here if needed.
    }

    @When("I start the browser")
    public void i_start_the_browser() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new Actions(driver);
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

    @When("I hover over the \"Urządzenia\" dropdown menu")
public void i_hover_over_the_devices_dropdown_menu() {
    // Locate the dropdown toggle button
    WebElement dropdownToggle = driver.findElement(By.xpath("//button[contains(text(), 'Urządzenia')]"));

    // Perform hover action using Actions class
    actions.moveToElement(dropdownToggle).perform();
    logger.info("Hovered over the devices dropdown menu button");
}


@Then("The list is visible")
public void the_smartwatche_option_should_be_visible_in_the_dropdown() {
    try {
        WebElement smartwatcheOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//span[text()='Smartwatche']")
        ));

        assertTrue("\"Smartwatche\" option is not visible in the dropdown", smartwatcheOption.isDisplayed());
        logger.info("\"Smartwatche\" option is visible in the dropdown");
    } catch (Exception e) {
        takeScreenshot("smartwatche_option_error");
        logger.error("\"Smartwatche\" option is not visible in the dropdown", e);
        throw e;
    }
}

@Then("I click on the \"Smartwatche\" option in the dropdown")
public void i_click_on_the_smartwatche_option_in_the_dropdown() {
    try {
        // Wait for the second "Smartwatche" text to become visible
        List<WebElement> smartwatcheOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.xpath("//span[text()='Smartwatche']")
        ));

        // Ensure there are at least two options and click the second one
        if (smartwatcheOptions.size() >= 2) {
            smartwatcheOptions.get(1).click();
            logger.info("Clicked on the second \"Smartwatche\" option in the dropdown");

            // Optionally, wait for a new page or section to load to ensure the click was successful
            // For example, wait until a new page title or URL is loaded
            wait.until(ExpectedConditions.urlContains("sklep/kategoria/telefony/lista")); // Assuming the URL contains "Smartwatche" after click
        } else {
            logger.error("Less than two \"Smartwatche\" options found in the dropdown");
            throw new NoSuchElementException("Less than two \"Smartwatche\" options found in the dropdown");
        }

    } catch (Exception e) {
        takeScreenshot("smartwatche_click_error");
        logger.error("Failed to click on the second \"Smartwatche\" option in the dropdown", e);
        throw e;
    }
}

@Then("I click on the first product card in the grid")
public void i_click_on_the_first_product_card_in_the_grid() {
    try {
        // Wait for the first product card to become visible
        WebElement firstProductCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("div[data-qa='LST_ProductCard0']")
        ));

        // Click the first product card
        firstProductCard.click();
        logger.info("Clicked on the first product card in the grid");

        // Wait for the page to load by checking for the partial URL
        wait.until(ExpectedConditions.urlContains("apple-watch-s9-lte"));

        // Verify that the current URL contains "apple-watch-s9-lte"
        String currentUrl = driver.getCurrentUrl();
        assertTrue("The page did not navigate to the expected URL containing 'apple-watch-s9-lte'.", currentUrl.contains("apple-watch-s9-lte"));
        logger.info("Successfully navigated to the Apple Watch S9 LTE product page");

    } catch (Exception e) {
        takeScreenshot("product_card_click_error");
        logger.error("Failed to click on the first product card or navigate correctly", e);
        throw e;
    }
}
@When("I retrieve the prices for comparison using XPath")
public void i_retrieve_the_prices_for_comparison_using_xpath() {
    try {
        // Locate the first price element using the provided XPath
        WebElement firstPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[1]/div[1]/div/div[2]/div/div")
        ));
        
        // Locate the second price element using the provided XPath
        WebElement secondPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[1]/div[2]/div/div/div[2]/div/div")
        ));

        // Retrieve and parse the text values, assuming they are in the format "{number} zł"
        String firstPriceText = firstPriceElement.getText().replace(" zł", "").trim();
        String secondPriceText = secondPriceElement.getText().replace(" zł", "").trim();
        
        int firstPrice = Integer.parseInt(firstPriceText);
        int secondPrice = Integer.parseInt(secondPriceText);

        // Log the retrieved values
        logger.info("First price: {} zł", firstPrice);
        logger.info("Second price: {} zł", secondPrice);

        // Optionally, you can assert some conditions here if needed
        assertTrue("First price should be less than the second price", firstPrice < secondPrice);

    } catch (Exception e) {
        takeScreenshot("price_retrieval_error");
        logger.error("Failed to retrieve prices for comparison using XPath", e);
        throw e;
    }
}



@When("I click on the specific button")
public void i_click_on_the_specific_button() {
    try {
        // Use WebDriverWait to wait for the button to become clickable
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[2]/section[1]/button")
        ));

        // Click the button
        button.click();
        logger.info("Clicked on the specific button");

        // Optionally, wait for some expected condition after the button click
        // e.g., a URL change, an element becoming visible, etc.
        wait.until(ExpectedConditions.urlContains("basket")); 

    } catch (Exception e) {
        takeScreenshot("button_click_error");
        logger.error("Failed to click on the specific button", e);
        throw e;
    }
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
