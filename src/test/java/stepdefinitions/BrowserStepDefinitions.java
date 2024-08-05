package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.HomePage;
import pages.SmartwatchListingPage;
import pages.ProductPage;
import pages.BasketPage;
import static org.junit.Assert.*;

import java.io.File;

public class BrowserStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(BrowserStepDefinitions.class);
    private WebDriver driver;
    private HomePage homePage;
    private SmartwatchListingPage smartwatchListingPage;
    private ProductPage productPage;
    private BasketPage basketPage;
    private int firstPrice;
    private int secondPrice;

    @Given("I have opened an appropriate browser")
    public void setupBrowser() {
        logger.info("Setting up the browser");
    }

    @When("I start the browser")
    public void startBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        logger.info("Browser started and maximized");
    }

    @Then("the browser should be open")
    public void verifyBrowserIsOpen() {
        assertNotNull("Browser did not open", driver);
        logger.info("Browser is open");
    }

    @When("I navigate to {string}")
    public void navigateTo(String url) {
        homePage.navigateTo(url);
        logger.info("Navigated to URL: {}", url);
    }

    @Then("I should see the cookie consent popup")
    public void verifyCookieConsentPopup() {
        try {
            assertTrue("Cookie consent popup is not visible", homePage.isCookieConsentPopupVisible());
            logger.info("Cookie consent popup is visible");
        } catch (AssertionError e) {
            takeScreenshot("cookie_popup_error");
            logger.error("Cookie consent popup is not visible", e);
            throw e;
        }
    }

    @When("I accept all cookies")
    public void acceptAllCookies() {
        try {
            homePage.acceptAllCookies();
            logger.info("Accepted all cookies");
        } catch (Exception e) {
            takeScreenshot("accept_cookies_error");
            logger.error("Failed to accept cookies", e);
            throw e;
        }
    }

    @Then("I should be on the T-Mobile homepage")
    public void verifyHomePage() {
        try {
            assertTrue("Not on T-Mobile homepage", homePage.isOnHomePage());
            logger.info("Successfully verified T-Mobile homepage");
        } catch (AssertionError e) {
            takeScreenshot("homepage_verification_error");
            logger.error("Failed to verify T-Mobile homepage", e);
            throw e;
        }
    }

    @When("I hover over the {string} dropdown menu")
    public void hoverOverDropdownMenu(String menuName) {
        try {
            if ("Urządzenia".equals(menuName)) {
                homePage.hoverOverDevicesDropdown();
                logger.info("Hovered over the '{}' dropdown menu", menuName);
            } else {
                throw new IllegalArgumentException("Unsupported menu: " + menuName);
            }
        } catch (Exception e) {
            takeScreenshot("hover_dropdown_error");
            logger.error("Failed to hover over the '{}' dropdown menu", menuName, e);
            throw e;
        }
    }

    @Then("Dropdown menu is visible")
    public void verifyDropdownListVisible() {
        try {
            assertTrue("Devices dropdown is not visible", homePage.isDevicesDropdownVisible());
            logger.info("Devices dropdown is visible");
        } catch (AssertionError e) {
            takeScreenshot("dropdown_visibility_error");
            logger.error("Devices dropdown is not visible", e);
            throw e;
        }
    }

    @Then("I click on the {string} option in the dropdown")
    public void clickDropdownOption(String optionName) {
        try {
            if ("Smartwatche".equals(optionName)) {
                smartwatchListingPage = homePage.clickSmartwatcheOption();
                assertTrue("Not on Smartwatch listing page", smartwatchListingPage.isOnSmartwatchListingPage());
                logger.info("Clicked on the '{}' option in the dropdown and navigated to Smartwatch listing page", optionName);
            } else {
                throw new IllegalArgumentException("Unsupported option: " + optionName);
            }
        } catch (Exception e) {
            takeScreenshot("click_dropdown_option_error");
            logger.error("Failed to click on the '{}' option in the dropdown", optionName, e);
            throw e;
        }
    }

    @Then("I click on the first product card in the grid")
    public void clickFirstProductCard() {
        try {
            productPage = smartwatchListingPage.clickFirstProductCard();
            logger.info("Clicked on the first product card in the grid");
        } catch (Exception e) {
            takeScreenshot("click_first_product_card_error");
            logger.error("Failed to click on the first product card in the grid", e);
            throw e;
        }
    }

    @When("I retrieve \"Do zapłaty na start\" and \"Do zapłaty miesięcznie\" prices")
    public void retrievePricesForComparison() {
        try {
            firstPrice = productPage.getFirstPrice();
            secondPrice = productPage.getSecondPrice();
            logger.info("Retrieved prices: First price = {} zł, Second price = {} zł", firstPrice, secondPrice);
            assertTrue("First price should be less than the second price", firstPrice < secondPrice);
        } catch (Exception e) {
            takeScreenshot("price_retrieval_error");
            logger.error("Failed to retrieve prices for comparison", e);
            throw e;
        }
    }

    @When("I click on the Dodaj do koszyka button")
    public void clickSpecificButton() {
        try {
            basketPage = productPage.clickSpecificButton();
            logger.info("Clicked on the specific button");
        } catch (Exception e) {
            takeScreenshot("click_specific_button_error");
            logger.error("Failed to click on the specific button", e);
            throw e;
        }
    }

    @Then("I compare the basket prices with the retrieved prices")
    public void compareBasketPrices() {
        try {
            int basketFirstPrice = basketPage.getBasketFirstPrice();
            int basketSecondPrice = basketPage.getBasketSecondPrice();
            
            assertEquals("First price in basket doesn't match the previously retrieved price", firstPrice, basketFirstPrice);
            assertEquals("Second price in basket doesn't match the previously retrieved price", secondPrice, basketSecondPrice);
            
            logger.info("Basket prices match the previously retrieved prices");
        } catch (AssertionError e) {
            takeScreenshot("basket_price_comparison_error");
            logger.error("Basket prices do not match the previously retrieved prices", e);
            throw e;
        } catch (Exception e) {
            takeScreenshot("basket_price_retrieval_error");
            logger.error("Failed to retrieve or compare basket prices", e);
            throw e;
        }
    }

    @When("I navigate back to the homepage")
    public void navigateBackToHomepage() {
        try {
            homePage = basketPage.navigateBackToHomepage();
            assertTrue("Failed to navigate back to the homepage", homePage.isOnHomePage());
            logger.info("Successfully navigated back to the homepage");
        } catch (Exception e) {
            takeScreenshot("navigate_home_error");
            logger.error("Failed to navigate back to the homepage", e);
            throw e;
        }
    }

    @Then("the cart should contain one item")
    public void verifyCartContainsOneItem() {
        try {
            int itemCount = homePage.getCartItemCount();
            assertEquals("Cart should contain one item", 1, itemCount);
            logger.info("Cart contains {} item(s)", itemCount);
        } catch (AssertionError e) {
            takeScreenshot("cart_item_count_error");
            logger.error("Cart does not contain the expected number of items", e);
            throw e;
        } catch (Exception e) {
            takeScreenshot("cart_item_count_error");
            logger.error("Failed to verify cart item count", e);
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