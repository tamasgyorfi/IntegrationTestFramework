package hu.bets.steps.util;

public class Constants {

    public static final String HOST = "localhost";
    public static final String PORT = "10000";

    public static final String CLOUDAMQP_URL = "amqp://guest:guest@localhost:11000";
    public static final String EXCHANGE_NAME = "amq.direct";

    public static final String AGGREGATE_REQUEST_QUEUE_NAME = "BETS_REQUEST";
    public static final String AGGREGATE_REPLY_QUEUE_NAME = "BETS_REPLY";

    public static final String AGGREGATE_REQUEST_ROUTING_KEY = "in.bets.request";
    public static final String AGGREGATE_RESPONSE_ROUTING_KEY = "out.bets.reply";

}
