import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class EmailComsumer {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-rabbitmq-comsumer.xml");
        ctx.start();
        System.in.read();
    }
}
