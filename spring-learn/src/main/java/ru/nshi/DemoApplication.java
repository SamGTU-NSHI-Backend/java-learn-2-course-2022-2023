package ru.nshi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        System.out.println("Context up");

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName + " = " + context.getBean(beanDefinitionName)
                .getClass()
                .getSimpleName());
        }
    }
}
