Feature: T-Mobile Website Navigation and Shopping Cart

  Scenario: Navigate T-Mobile website and add a smartwatch to cart
    Given I have opened an appropriate browser
    When I start the browser
    Then the browser should be open

    When I navigate to "https://www.t-mobile.pl"
    Then I should see the cookie consent popup
    When I accept all cookies
    Then I should be on the T-Mobile homepage

    When I hover over the "Urządzenia" dropdown menu
    Then The list is visible
    And I click on the "Smartwatche" option in the dropdown

    Then I click on the first product card in the grid
    When I retrieve the prices for comparison using XPath
    And I click on the specific button
    Then I compare the basket prices with the previously retrieved prices

    When I navigate back to the homepage
    Then the cart should contain one item