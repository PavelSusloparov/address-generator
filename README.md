# Address Generator

The web service generates random physical addresses.

The address output limits country set to
* US
* Canada
* Mexico
* Netherlands

## Local environment setup

```bash
./gradlew bootRun
```

## API Specification

### Healtcheck

```
curl -X GET http://localhost:8080/actuator/health
```

### Get address

Get randomized address
```bash
curl -X GET -H "Content-Type: application/json" http://localhost:8080/randomizer/address
```

Response:
```bash
{
  "house": "3007",
  "street": "Omer Courts",
  "postalCode": "14447",
  "city": "Karmenview",
  "county": "Autauga",
  "state": "South Carolina",
  "stateCode": "TN",
  "country": "United States",
  "countryCode": "USA"
}
```

Get randomized address for a given country code:
USA:
```bash
curl -X GET -H "Content-Type: application/json" http://localhost:8080/randomizer/address?countryAbbr=US
```
Canada:
```bash
curl -X GET -H "Content-Type: application/json" http://localhost:8080/randomizer/address?countryAbbr=CN
```
Mexico:
```bash
curl -X GET -H "Content-Type: application/json" http://localhost:8080/randomizer/address?countryAbbr=MX
```
Netherlands
```bash
curl -X GET -H "Content-Type: application/json" http://localhost:8080/randomizer/address?countryAbbr=NL
```

## Data source

JavaFaker is a data source for the application.
It has a set of limitations:

- JavaFaker does not have data sets per country, it has it per locale.
Locale is based on the language and region and brings many countries to it's umbrella.

For instance Mexican locale "es-MX" contains countries, other then Mexico, for example Australia.
You can find the full list [here](https://github.com/DiUS/java-faker/tree/master/src/main/resources)

- JavaFaker does not have "county" data
This has been mitigated with hardcoding a "county" value per country.

- JavaFaker generates countryCode in different comparing with expected alpha3 format.
This has been mitigated with additional conversion with i18n library.

If JavaFaker produces an unexpected according to specification data, then application defaults address to hardcoded address.

## DevOps

Application uses Spring Boot/Kotlin/Java 11.
For bundling dependencies and task definitions it uses gradle with Kotlin syntax *.kts extension
Find available tasks with
```bash
./gradlew tasks
```

### Code coverage

`build` or `tests` gradle tasks automatically generate Jacoco test coverage report.
The report is based on `com.generator` namespace.
The code coverage threshold is 100%. It means that the build will fail if ccode coverage metric drops below this value.

### Linter

Ktlint is used for static code analysis.
`build` or `tests` gradle tasks automatically run linter check.

To format code automatically, run
```bash
./gradlew ktlintFormat
```

To check, if code satisfy the linter criteria
```bash
./gradlew ktlintCheck
```

## Testing

There are two type of tests:
1. Unit
2. Contract

Tests are located in separate source sets to avoid mix of libraries dependencies.
Unit tests use `test` source set.
Unit tests use Junit5 for assertions libraries and test runner.
Also, Unit tests use Mockito for mocking and controlling functions, classes behavior.

Contract tests use `contractTest` source set.
Contract tests use Junit4 for assertions libraries and test runner.
The testing framework is Cucumber. It provides Test Driven Development capabilities and user scenarios abstractions.

Contract tests run in-memory application instance and make calls from a testing process with http client.
Calls go through the network from the localhost(testing process) to localhost(in-memory application instance).

Run tests with
```bash
./gradlew test
```
The command runs kotlin check, unit and contract tests and check jacoco test coverage.

