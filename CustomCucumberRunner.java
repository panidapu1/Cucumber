//Custom Runner
package com.automation.cucumber.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import com.automation.cucumber.helper.BeforeSuite;
import cucumber.api.junit.Cucumber;


public class CustomCucumberRunner extends Runner {

    private Class<Cucumber> classValue;
    private Cucumber cucumber;

    public CustomCucumberRunner(Class<Cucumber> classValue) throws Exception {
        this.classValue = classValue;
        cucumber = new Cucumber(classValue);
    }

    @Override
    public Description getDescription() {
        return cucumber.getDescription();
    }

    private void runAnnotatedMethods(Class<?> annotation) throws Exception {
        if (!annotation.isAnnotation()) {
            return;
        }
        Method[] methods = this.classValue.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation item : annotations) {
                if (item.annotationType().equals(annotation)) {
                    method.invoke(null);
                    break;
                }
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            runAnnotatedMethods(BeforeSuite.class);
            cucumber = new Cucumber(classValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cucumber.run(notifier);
    }
}