Feature: Main activity testing
  Load app for first time and retrieve goal list


  Scenario: First app launching
    Given I am in the main view screen
    When I wait for a moment
    Then I see all the goals in a list