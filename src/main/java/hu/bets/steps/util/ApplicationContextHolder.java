package hu.bets.steps.util;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextHolder {
    private static AnnotationConfigApplicationContext context;

    public static void startApplicationContext(Class<?>... configurations) {
        context = new AnnotationConfigApplicationContext(configurations);
    }

    public static void stopApplicationContext() {
        context.stop();
    }

    public static <T> T getBean(Class<T> classType) {
        return context.getBean(classType);
    }

}
