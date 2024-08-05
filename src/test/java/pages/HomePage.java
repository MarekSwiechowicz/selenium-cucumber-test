package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import java.util.List;

public class HomePage extends BasePage {
    @FindBy(id = "didomi-notice-agree-button")
    private WebElement cookieConsentButton;

    @FindBy(xpath = "//button[contains(text(), 'Urządzenia')]")
    private WebElement devicesDropdown;

    @FindBy(xpath = "//span[text()='Smartwatche']")
    private List<WebElement> smartwatcheOptions;

    @FindBy(xpath = "//*[@id='__next']/div/div/header/div/div[1]/div/div/a[1]/div")
    private WebElement cartIndicator;

    private Actions actions;

    public HomePage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public boolean isCookieConsentPopupVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(cookieConsentButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void acceptAllCookies() {
        wait.until(ExpectedConditions.elementToBeClickable(cookieConsentButton)).click();
        wait.until(ExpectedConditions.invisibilityOf(cookieConsentButton));
    }

    public boolean isOnHomePage() {
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
        String actualTitle = driver.getTitle();
        return actualTitle.contains("T-Mobile");
    }

    public void hoverOverDevicesDropdown() {
        wait.until(ExpectedConditions.visibilityOf(devicesDropdown));
        actions.moveToElement(devicesDropdown).perform();
    }

    public boolean isDevicesDropdownVisible() {
        return devicesDropdown.isDisplayed();
    }

    public SmartwatchListingPage clickSmartwatcheOption() {
        wait.until(ExpectedConditions.visibilityOfAllElements(smartwatcheOptions));
        if (smartwatcheOptions.size() >= 2) {
            smartwatcheOptions.get(1).click();
        } else {
            throw new IndexOutOfBoundsException("Less than two 'Smartwatche' options found");
        }
        return new SmartwatchListingPage(driver);
    }

    public int getCartItemCount() {
        wait.until(ExpectedConditions.visibilityOf(cartIndicator));
        String cartItemCountText = cartIndicator.getText().trim();
        return cartItemCountText.isEmpty() ? 0 : Integer.parseInt(cartItemCountText);
    }
}