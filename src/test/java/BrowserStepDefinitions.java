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
    public void setupBrowser() {
        logger.info("Setting up the browser");
    }

    @When("I start the browser")
    public void startBrowser() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        driver.manage().window().maximize();
        logger.info("Browser started and maximized");
    }

    @Then("the browser should be open")
    public void verifyBrowserIsOpen() {
        assertNotNull("Browser did not open", driver);
        logger.info("Browser is open");
    }

    @When("I navigate to {string}")
    public void navigateTo(String url) {
        driver.get(url);
        logger.info("Navigated to URL: {}", url);
    }

    @Then("I should see the cookie consent popup")
    public void verifyCookieConsentPopup() {
        try {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("didomi-notice-agree-button")));
            assertTrue("Cookie consent popup is not visible", popup.isDisplayed());
            logger.info("Cookie consent popup is visible");
        } catch (Exception e) {
            takeScreenshot("cookie_popup_error");
            logger.error("Cookie consent popup is not visible", e);
            throw e;
        }
    }

    @When("I accept all cookies")
    public void acceptAllCookies() {
        WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("didomi-notice-agree-button")));
        acceptButton.click();
        logger.info("Accepted all cookies");
        
        // Wait until the popup disappears
        wait.until(ExpectedConditions.invisibilityOf(acceptButton));
    }

    @Then("I should be on the T-Mobile homepage")
    public void verifyHomePage() {
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
        String actualTitle = driver.getTitle();
        logger.info("Actual page title: {}", actualTitle);
        assertTrue("Page title does not contain 'T-Mobile'", actualTitle.contains("T-Mobile"));
        logger.info("Title verification passed");
    }

    @When("I hover over the \"Urządzenia\" dropdown menu")
    public void hoverOverDevicesDropdown() {
        try {
            WebElement dropdownToggle = driver.findElement(By.xpath("//button[contains(text(), 'Urządzenia')]"));
            actions.moveToElement(dropdownToggle).perform();
            logger.info("Hovered over the devices dropdown menu button");
        } catch (NoSuchElementException e) {
            takeScreenshot("dropdown_hover_error");
            logger.error("Dropdown hover failed", e);
            throw e;
        }
    }

    @Then("The list is visible")
    public void verifyDropdownListVisible() {
        try {
            WebElement smartwatcheOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Smartwatche']")));
            assertTrue("\"Smartwatche\" option is not visible in the dropdown", smartwatcheOption.isDisplayed());
            logger.info("\"Smartwatche\" option is visible in the dropdown");
        } catch (Exception e) {
            takeScreenshot("smartwatche_option_error");
            logger.error("\"Smartwatche\" option is not visible in the dropdown", e);
            throw e;
        }
    }

    @Then("I click on the \"Smartwatche\" option in the dropdown")
    public void clickSmartwatcheOption() {
        try {
            List<WebElement> smartwatcheOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//span[text()='Smartwatche']")));

            if (smartwatcheOptions.size() >= 2) {
                smartwatcheOptions.get(1).click();
                logger.info("Clicked on the second \"Smartwatche\" option in the dropdown");
                wait.until(ExpectedConditions.urlContains("sklep/kategoria/telefony/lista"));
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
    public void clickFirstProductCard() {
        try {
            WebElement firstProductCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-qa='LST_ProductCard0']")));
            firstProductCard.click();
            logger.info("Clicked on the first product card in the grid");
            wait.until(ExpectedConditions.urlContains("apple-watch-s9-lte"));
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
    public void retrievePricesForComparison() {
        try {
            WebElement firstPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[1]/div[1]/div/div[2]/div/div")
            ));
            WebElement secondPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[1]/div[2]/div/div/div[2]/div/div")
            ));

            String firstPriceText = firstPriceElement.getText().replace(" zł", "").trim();
            String secondPriceText = secondPriceElement.getText().replace(" zł", "").trim();
            
            this.firstPrice = Integer.parseInt(firstPriceText);
            this.secondPrice = Integer.parseInt(secondPriceText);
            
            logger.info("First price: {} zł", firstPrice);
            logger.info("Second price: {} zł", secondPrice);
            
            assertTrue("First price should be less than the second price", firstPrice < secondPrice);

        } catch (Exception e) {
            takeScreenshot("price_retrieval_error");
            logger.error("Failed to retrieve prices for comparison using XPath", e);
            throw e;
        }
    }

    @When("I click on the specific button")
    public void clickSpecificButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[2]/section[1]/button")
            ));
            button.click();
            logger.info("Clicked on the specific button");
            wait.until(ExpectedConditions.urlContains("basket")); 

        } catch (Exception e) {
            takeScreenshot("button_click_error");
            logger.error("Failed to click on the specific button", e);
            throw e;
        }
    }

    @Then("I compare the basket prices with the previously retrieved prices")
    public void compareBasketPrices() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("basketSummary")));

            WebElement basketFirstPriceElement = driver.findElement(By.xpath("//*[@id=\"basketSummary\"]/div/div[2]/div[1]/div/div/div[1]/span[2]/div/span[1]"));
            String basketFirstPriceText = basketFirstPriceElement.getText().replace(" zł", "").trim();
            int basketFirstPrice = Integer.parseInt(basketFirstPriceText);

            WebElement basketSecondPriceElement = driver.findElement(By.xpath("//*[@id=\"basketSummary\"]/div/div[2]/div[1]/section/div/div[1]/article/span/div/span[1]"));
            String basketSecondPriceText = basketSecondPriceElement.getText().replace(" zł", "").trim();
            int basketSecondPrice = Integer.parseInt(basketSecondPriceText);

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
    public void navigateBackToHomepage() {
        try {
            WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='osAppInnerContainer']/div[2]/div[1]/a/span/div/img")
            ));
            homeButton.click();
            logger.info("Navigated back to the homepage");
            wait.until(ExpectedConditions.titleContains("T-Mobile"));

        } catch (Exception e) {
            takeScreenshot("navigate_home_error");
            logger.error("Failed to navigate back to the homepage", e);
            throw e;
        }
    }

    @Then("the cart should contain one item")
    public void verifyCartContainsOneItem() {
        try {
            WebElement cartIndicator = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='__next']/div/div/header/div/div[1]/div/div/a[1]/div")
            ));
            String cartItemCountText = cartIndicator.getText().trim();
            logger.info("Cart item count text: {}", cartItemCountText);

            assertFalse("Cart is empty, no items found.", cartItemCountText.isEmpty());
            int itemCount = Integer.parseInt(cartItemCountText);
            assertTrue("Cart should contain at least one item, but it doesn't.", itemCount > 0);
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
