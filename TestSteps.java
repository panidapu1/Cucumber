package com.automation.cucumber.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TestSteps {
    
    @Given("^scenario data$")
    public void scenarioData() throws Throwable {
        System.out.println("Scenario Have Some Data");
    }

    @When("^executed from Runner Class \"([^\"]*)\"\\.$")
    public void executed_from_Runner_Class(DataTable arg2) throws Throwable {
        System.out.println("Executed From Runner Class");
    }
    
    @When("^executed from Runner Class\\.$")
    public void executedFromRunnerClass() throws Throwable {
        System.out.println("Executed From Runner Class");
    }
    
    @Then("^UserName and Password shows on console from Examples \"([^\"]*)\" and \"([^\"]*)\"$")
    public void usernameAndPasswordShowsOnConsoleFromExamplesAnd(String userName, String password) throws Throwable {
        System.out.println("UserName : " + userName + " Password : " + password);
    }
}
