Feature: Browser Initialization and Navigation

  Scenario: Open the browser and navigate to T-Mobile website
    Given the browser is set up
    When I start the browser
    Then the browser should be open
    When I navigate to "https://www.t-mobile.pl/"
    Then I should be on the T-Mobile homepage