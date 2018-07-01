//Custom Annotation :
//package com.automation.cucumber.helper;
package com.automation.cucumber.helper;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface BeforeSuite {

}
