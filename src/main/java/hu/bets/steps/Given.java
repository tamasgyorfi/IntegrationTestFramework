package hu.bets.steps;

import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.Channel;
import hu.bets.common.messaging.MessageListener;
import hu.bets.steps.util.ApplicationContextHolder;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;
import org.bson.Document;
import org.eclipse.jetty.server.Server;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static hu.bets.steps.util.Constants.*;

public class Given {

    private static final Broker BROKER = new Broker();
    private static Server webServer;

    public static MongoCollection<Document> aDataSource() {
        return ApplicationContextHolder.getBean(MongoCollection.class);
    }

    public static void environmentIsUpAndRunning(Class<?>... configs) throws Exception {
        setupBroker();
        setEnvironment();
        ApplicationContextHolder.startApplicationContext(configs);
        setupQueues();
        startListening();
        startServer();
    }

    public static void environmentIsShutDown() throws Exception {

        BROKER.shutdown();
        webServer.stop();

        ApplicationContextHolder.stopApplicationContext();
        TimeUnit.SECONDS.sleep(1);
    }

    private static void startListening() throws Exception {
        MessageListener messageListener = ApplicationContextHolder.getBean(MessageListener.class);

        messageListener.receive();
    }

    private static void setupQueues() throws Exception {
        Channel channel = ApplicationContextHolder.getBean(Channel.class);

        channel.queueDeclare("BETS_TO_SCORES", true, false, false, null);
        channel.queueDeclare("SCORES_TO_BETS", true, false, false, null);
    }

    private static void setupBroker() {

        String config = Thread.currentThread().getContextClassLoader().getResource("amqp_config.json").toExternalForm();
        String keystore = Thread.currentThread().getContextClassLoader().getResource("clientkeystore").toExternalForm();

        final BrokerOptions brokerOptions = new BrokerOptions();
        brokerOptions.setConfigProperty("qpid.amqp_port", "11000");
        brokerOptions.setConfigProperty("store.uri", keystore);
        brokerOptions.setInitialConfigurationLocation(config);

        try {
            BROKER.startup(brokerOptions);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static void startServer() {
        webServer = ApplicationContextHolder.getBean(Server.class);
        new Thread(() -> {
            try {
                webServer.start();
                webServer.join();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }).start();
    }

    private static void setEnvironment() {
        Properties properties = System.getProperties();
        properties.setProperty("HOST", HOST);
        properties.setProperty("PORT", PORT);

        properties.setProperty("CLOUDAMQP_URL", CLOUDAMQP_URL);
    }
}
