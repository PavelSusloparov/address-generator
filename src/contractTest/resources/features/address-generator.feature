Feature: Generate address

  Scenario Outline: Generate address
    When user requests address generation for <countryAbbr>
    Then address information returns with <httpCode> http code

    Examples:
      | countryAbbr | httpCode  |
      | null        | 200       |
      | US          | 200       |
      | CN          | 200       |
      | MX          | 200       |
      | NL          | 200       |
      | Random      | 400       |