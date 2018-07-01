//Cucumber Runner
package com.automation.cucumber.runner;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.runner.RunWith;
import com.automation.cucumber.helper.BeforeSuite;
import com.automation.cucumber.helper.FeatureOverright;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;

@CucumberOptions(plugin = { "html:target/cucumber-html-report", "json:target/cucumber.json" }, 
                features = {"./src/sample.feature" }, 
                glue = {"com/automation/cucumber/steps" }, 
                monochrome = true, snippets = SnippetType.CAMELCASE)

@RunWith(CustomCucumberRunner.class)
public class CucumberRunnerTest {
    @BeforeSuite
    public static void test() throws InvalidFormatException, IOException {
        FeatureOverright.overrideFeatureFiles("./src/test/resources");
    }
}

