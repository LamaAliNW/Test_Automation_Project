# Swag Labs — Test Automation Project

A Selenium WebDriver automation project for SauceDemo using Java, TestNG, Maven, Jenkins, and Allure.

## How to Run

**Requirements:** Java JDK, Maven, and Google Chrome.

Clone the repository:

```bash
git clone https://github.com/LamaAliNW/Test_Automation_Project.git
cd Test_Automation_Project/starter
```

Run the regression suite:

```bash
mvn clean test
```

Run the smoke suite:

```bash
mvn clean test -DsuiteXmlFile=testng-smoke.xml
```

Run the parallel suite:

```bash
mvn clean test -DsuiteXmlFile=testng-parallel.xml
```

View the Allure report:

```bash
mvn allure:serve
```

## Test Execution Evidence

### Jenkins — Successful Pipeline

![Jenkins Pipeline](docs/evidence/jenkins-pipeline-success.png)

### Jenkins — Smoke Tests

![Jenkins Smoke Tests](docs/evidence/jenkins-smoke-tests.png)

### Allure — Smoke Results

![Allure Smoke Results](docs/evidence/allure-smoke-success.png)

### Allure — Screenshot on Failure

![Allure Failure Screenshot](docs/evidence/allure-failure-screenshot.png)

## What I Would Automate Next

- Cross-browser testing with Firefox and Edge.
- Additional negative and boundary test cases.
- API testing for backend validation.
- Automatic Jenkins execution after GitHub updates.
