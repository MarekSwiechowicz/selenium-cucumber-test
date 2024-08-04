import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(BrowserStepDefinitions.class);
    private WebDriver driver;

    @Given("the browser is set up")
    public void the_browser_is_set_up() {
        // Ustaw ścieżkę do sterownika Chrome, jeśli to konieczne
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        logger.info("Setting up the browser");
    }

    @When("I start the browser")
    public void i_start_the_browser() {
        driver = new ChromeDriver();
        logger.info("Browser started");
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

    @Then("I should be on the T-Mobile homepage")
    public void i_should_be_on_the_t_mobile_homepage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
        
        String actualTitle = driver.getTitle();
        logger.info("Actual page title: {}", actualTitle);
        
        assertEquals("Oczekiwany tytuł strony nie zgadza się", 
                     "Telefony, Tablety, Laptopy, Szybki Internet - Dołącz do T-Mobile", 
                     actualTitle);
        logger.info("Title verification passed");

        // Zamknij przeglądarkę po zakończeniu testu
        driver.quit();
        logger.info("Browser closed");
    }
}