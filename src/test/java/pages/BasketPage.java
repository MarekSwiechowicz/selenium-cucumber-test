package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BasketPage extends BasePage {

    @FindBy(xpath = "//*[@id=\"basketSummary\"]/div/div[2]/div[1]/div/div/div[1]/span[2]/div/span[1]")
    private WebElement basketFirstPriceElement;

    @FindBy(xpath = "//*[@id=\"basketSummary\"]/div/div[2]/div[1]/section/div/div[1]/article/span/div/span[1]")
    private WebElement basketSecondPriceElement;

    @FindBy(xpath = "//*[@id='osAppInnerContainer']/div[2]/div[1]/a/span/div/img")
    private WebElement homeButton;

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    public int getBasketFirstPrice() {
        wait.until(ExpectedConditions.visibilityOf(basketFirstPriceElement));
        String priceText = basketFirstPriceElement.getText().replace(" zł", "").trim();
        return Integer.parseInt(priceText);
    }

    public int getBasketSecondPrice() {
        wait.until(ExpectedConditions.visibilityOf(basketSecondPriceElement));
        String priceText = basketSecondPriceElement.getText().replace(" zł", "").trim();
        return Integer.parseInt(priceText);
    }

    public HomePage navigateBackToHomepage() {
        wait.until(ExpectedConditions.elementToBeClickable(homeButton)).click();
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
        return new HomePage(driver);
    }
}