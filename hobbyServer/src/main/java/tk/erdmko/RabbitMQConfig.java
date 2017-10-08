package tk.erdmko;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.UnsupportedEncodingException;

import tk.erdmko.models.SocketResponseModel;

@Configuration
public class RabbitMQConfig {
    final private static String defaultQueueName = "hello";

    private Queue defaultQueue = new Queue(defaultQueueName);

    private @Value("${spring.amqp.host}") String host;

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
     CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(host);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        AmqpAdmin out = new RabbitAdmin(connectionFactory());
        out.declareQueue(defaultQueue);
        return out;
    }

    @RabbitListener(queues = RabbitMQConfig.defaultQueueName)
    public void processMobileMessages(String message) throws UnsupportedEncodingException {
        webSocket.convertAndSend("/wsOut", new SocketResponseModel(message));
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

}
