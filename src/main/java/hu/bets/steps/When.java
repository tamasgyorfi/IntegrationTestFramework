package hu.bets.steps;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import hu.bets.steps.util.ApplicationContextHolder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Map;

import static hu.bets.steps.util.Constants.EXCHANGE_NAME;

public class When {

    public static void iRepeateAPostRequest(String uri, String payload, int nrOfTimes) throws Exception {
        for (int i = 0; i < nrOfTimes; i++) {
            iMakeAPostRequest(uri, payload);
        }
    }

    public static HttpResponse iMakeAPostRequest(String uri, String payload) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(uri);
        HttpEntity entity = new StringEntity(payload);

        postRequest.setEntity(entity);
        postRequest.addHeader("Content-Type", "application/json");
        return client.execute(postRequest);
    }

    public static HttpResponse iMakeAGetRequest(String uri) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(uri);

        return client.execute(getRequest);
    }

    public static void iSendAMessage(String payload, Map<String, Object> headers, String queue, String routingKey) throws Exception {
        Channel channel = ApplicationContextHolder.getBean(Channel.class);
        channel.queueDeclare(queue, true, false, false, null);

        channel.basicPublish(EXCHANGE_NAME, routingKey,
                new AMQP.BasicProperties.Builder()
                        .headers(headers)
                        .build(),
                payload.getBytes());
    }
}
