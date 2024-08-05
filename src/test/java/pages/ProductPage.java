package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {

    @FindBy(xpath = "//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[1]/div[1]/div/div[2]/div/div")
    private WebElement firstPriceElement;

    @FindBy(xpath = "//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[1]/div[2]/div/div/div[2]/div/div")
    private WebElement secondPriceElement;

    @FindBy(xpath = "//*[@id=\"osAppInnerContainer\"]/main/section/section/div/span/div/div[2]/div/div/div/div[2]/section[1]/button")
    private WebElement specificButton;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public int getFirstPrice() {
        wait.until(ExpectedConditions.visibilityOf(firstPriceElement));
        String priceText = firstPriceElement.getText().replace(" zł", "").trim();
        return Integer.parseInt(priceText);
    }

    public int getSecondPrice() {
        wait.until(ExpectedConditions.visibilityOf(secondPriceElement));
        String priceText = secondPriceElement.getText().replace(" zł", "").trim();
        return Integer.parseInt(priceText);
    }

    public BasketPage clickSpecificButton() {
        wait.until(ExpectedConditions.elementToBeClickable(specificButton)).click();
        wait.until(ExpectedConditions.urlContains("basket"));
        return new BasketPage(driver);
    }
}