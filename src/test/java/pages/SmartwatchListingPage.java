package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SmartwatchListingPage extends BasePage {

    @FindBy(css = "div[data-qa='LST_ProductCard0']")
    private WebElement firstProductCard;

    public SmartwatchListingPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage clickFirstProductCard() {
        wait.until(ExpectedConditions.elementToBeClickable(firstProductCard)).click();
        wait.until(ExpectedConditions.urlContains("apple-watch-s9-lte"));
        return new ProductPage(driver);
    }

    public boolean isOnSmartwatchListingPage() {
        return wait.until(ExpectedConditions.urlContains("sklep/kategoria/telefony/lista"));
    }
}