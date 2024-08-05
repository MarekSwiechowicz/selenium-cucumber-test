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
    private int firstPrice;
    private int secondPrice;

    @Given("I have opened an appropriate browser")
    public void the_browser_is_set_up() {
        logger.info("Setting up the browser");
    }

    @When("I start the browser")
    public void i_start_the_browser() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

        this.firstPrice = firstPrice;
        this.secondPrice = secondPrice;
        
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

@Then("I compare the basket prices with the previously retrieved prices")
public void i_compare_the_basket_prices_with_the_previously_retrieved_prices() {
    try {
        // Wait for the basket summary to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("basketSummary")));

        // Retrieve the first price from the basket summary
        WebElement basketFirstPriceElement = driver.findElement(By.xpath("//*[@id=\"basketSummary\"]/div/div[2]/div[1]/div/div/div[1]/span[2]/div/span[1]"));
        String basketFirstPriceText = basketFirstPriceElement.getText().replace(" zł", "").trim();
        int basketFirstPrice = Integer.parseInt(basketFirstPriceText);

        // Retrieve the second price from the basket summary
        WebElement basketSecondPriceElement = driver.findElement(By.xpath("//*[@id=\"basketSummary\"]/div/div[2]/div[1]/section/div/div[1]/article/span/div/span[1]"));
        String basketSecondPriceText = basketSecondPriceElement.getText().replace(" zł", "").trim();
        int basketSecondPrice = Integer.parseInt(basketSecondPriceText);

        // Compare with the previously retrieved prices
        assertEquals("First price in basket doesn't match the previously retrieved price", firstPrice, basketFirstPrice);
        assertEquals("Second price in basket doesn't match the previously retrieved price", secondPrice, basketSecondPrice);

        logger.info("Basket prices match the previously retrieved prices");

    } catch (Exception e) {
        takeScreenshot("basket_price_comparison_error");
        logger.error("Failed to compare basket prices with previously retrieved prices", e);
        throw e;
    }
}

@When("I navigate back to the homepage")
public void i_navigate_back_to_the_homepage() {
    try {
        // Locate the home button or link using its XPath
        WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[@id='osAppInnerContainer']/div[2]/div[1]/a/span/div/img")
        ));

        // Click the home button
        homeButton.click();
        logger.info("Navigated back to the homepage");

        // Optionally, wait for some expected condition to verify the homepage load
        wait.until(ExpectedConditions.titleContains("T-Mobile")); // Replace with actual homepage title or URL part

    } catch (Exception e) {
        takeScreenshot("navigate_home_error");
        logger.error("Failed to navigate back to the homepage", e);
        throw e;
    }
}

@Then("the cart should contain one item")
public void the_cart_should_contain_at_least_one_item() {
    try {
        // Locate the cart item indicator element using XPath
        WebElement cartIndicator = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[@id='__next']/div/div/header/div/div[1]/div/div/a[1]/div")
        ));

        // Retrieve the text content from the element
        String cartItemCountText = cartIndicator.getText().trim();

        // Log the text for debugging
        logger.info("Cart item count text: {}", cartItemCountText);

        // Check if the cart item count text is not empty and represents a number greater than zero
        assertFalse("Cart is empty, no items found.", cartItemCountText.isEmpty());

        // Attempt to parse the text as an integer to check the number of items
        int itemCount = Integer.parseInt(cartItemCountText);
        assertTrue("Cart should contain at least one item, but it doesn't.", itemCount > 0);

        // Log success message
        logger.info("Cart contains {} items.", itemCount);

    } catch (NoSuchElementException e) {
        takeScreenshot("cart_item_error");
        logger.error("Cart item indicator not found", e);
        throw new AssertionError("Expected cart item indicator not found.");
    } catch (NumberFormatException e) {
        takeScreenshot("cart_item_format_error");
        logger.error("Cart item count is not a valid number", e);
        throw new AssertionError("Cart item count is not a valid number.");
    } catch (Exception e) {
        takeScreenshot("cart_check_error");
        logger.error("Error checking cart contents", e);
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
