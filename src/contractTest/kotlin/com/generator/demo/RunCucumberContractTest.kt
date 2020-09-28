package com.generator.demo

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = ["pretty", "html:target/cucumber/contract", "json:target/cucumber/contract/report.json"],
    features = ["src/contractTest/resources/features"]
)
class RunCucumberContractTest
