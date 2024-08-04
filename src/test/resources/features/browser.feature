Feature: T-Mobile Shopping Process

  Scenario: Navigate to Smartwatches section
    Given the browser is set up
    When I start the browser
    Then the browser should be open
    When I navigate to "https://www.t-mobile.pl/"
    Then I should see the cookie consent popup
    When I accept all cookies
    Then I should be on the T-Mobile homepage
    When I hover over the "Urządzenia" dropdown menu
    Then The list is visible