# T-Mobile Product Purchase Automation

This project demonstrates automated testing of the T-Mobile website's product purchase flow using Selenium WebDriver with Java and Cucumber.

## Project Description

The automation script performs the following actions:
1. Opens a Chrome browser
2. Navigates to the T-Mobile Poland website
3. Handles cookie consent
4. Navigates to the Smartwatch category
5. Selects the first product (Apple Watch S9 LTE)
6. Compares prices on the product page
7. Adds the product to the cart
8. Verifies cart contents and prices
9. Returns to the homepage
10. Confirms the cart contains one item

## Prerequisites

- Java JDK 8 or higher
- Maven
- Chrome browser
- ChromeDriver (matching your Chrome version)

## Dependencies

- Selenium WebDriver
- Cucumber
- JUnit
- SLF4J (for logging)
- Apache Commons IO (for screenshot capture)

## Running the Tests

1. Clone this repository
2. Navigate to the project directory
3. Run the following command:
mvn test
