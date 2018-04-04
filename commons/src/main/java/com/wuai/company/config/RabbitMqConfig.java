package com.wuai.company.config;

import com.wuai.company.enums.RabbitTypeEnum;
import com.wuai.company.message.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * rabbitMq的配置
 * Created by Ness on 2017/5/30.
 */
@Configuration
public class RabbitMqConfig {

    private final String TRYST_QUEUE = "wuai.queue.fanout.tryst";
    private final String TRYST_EXCHANGE = "wuai.exchange.fanout.tryst";
    private final String TRYST_ROUTINGKEY = "wuai.routingKey.fanout.tryst";

    private final String ORDER_QUEUE = "wuai.queue.fanout.order";
    private final String ORDER_EXCHANGE = "wuai.exchange.fanout.order";
    private final String ORDER_ROUTINGKEY = "wuai.routingKey.fanout.order";

    private final String STORE_QUEUE = "wuai.queue.fanout.store";
    private final String STORE_EXCHANGE = "wuai.exchange.fanout.store";
    private final String STORE_ROUTINGKEY = "wuai.routingKey.fanout.store";

    private final String PARTY_QUEUE = "wuai.queue.fanout.party";
    private final String PARTY_EXCHANGE = "wuai.exchange.fanout.party";
    private final String PARTY_ROUTINGKEY = "wuai.routingKey.fanout.party";

    private final String TIMETASK_QUEUE = "wuai.queue.fanout.timeTask";
    private final String TIMETASK_EXCHANGE = "wuai.exchange.fanout.timeTask";
    private final String TIMETASK_ROUTINGKEY = "wuai.routingKey.fanout.timeTask";

    private final String TASK_QUEUE = "wuai.queue.fanout.task";
    private final String TASK_EXCHANGE = "wuai.exchange.fanout.task";
    private final String TASK_ROUTINGKEY = "wuai.routingKey.fanout.task";

    @Autowired
    private ConnectionFactory factory;

    @Bean
    public ConnectionFactory createConnectionFactory(@Value("${rabbit.host}") String host, @Value("${rabbit.name}") String name, @Value("${rabbit.pass}") String pass) {

        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setUsername(name);
        factory.setPassword(pass);
        return factory;

    }

    /*
     *=====================================================tryst========================================================
     * */
    @Resource
    private Queue trystQueue;

    @Bean("trystQueue")
    public Queue createTrystQueue() {

        return new Queue(TRYST_QUEUE);
    }

    //交换机
    @Resource
    private FanoutExchange trystExchange;

    @Bean("trystExchange")
    public FanoutExchange createTrystExchange() {

        return new FanoutExchange(TRYST_EXCHANGE, Boolean.TRUE, false);
    }

    //绑定
    @Bean
    public Binding createTrystBind() {

        return BindingBuilder.bind(trystQueue).to(trystExchange);
    }

    //监听
    @Resource
    private MessageListenerAdapter trystAdaptor;

    @Bean("trystAdaptor")
    public MessageListenerAdapter createTrystAdaptor(TrystSubscriber trystSubscriber) {

        return createAdaptor(trystSubscriber, new TransferData());
    }


    @Bean(name = "trystListener")
    public SimpleMessageListenerContainer createTrystMessageListenerContainer() {

        return createContainer(RabbitTypeEnum.TRYST);
    }


    @Bean("trystRabbitTemplate")
    public RabbitTemplate createTrystRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.TRYST);

    }


    /*
    *=====================================================order========================================================
    * */

    //队列
    @Resource
    private Queue orderQueue;

    @Bean("orderQueue")
    public Queue createOrderQueue() {

        return new Queue(ORDER_QUEUE);
    }

    //交换机
    @Resource
    private FanoutExchange orderExchange;

    @Bean("orderExchange")
    public FanoutExchange createOrderExchange() {

        return new FanoutExchange(ORDER_EXCHANGE, Boolean.TRUE, false);
    }

    //绑定
    @Bean
    public Binding createOrderBind() {

        return BindingBuilder.bind(orderQueue).to(orderExchange);
    }

    //监听
    @Resource
    private MessageListenerAdapter orderAdaptor;

    @Bean("orderAdaptor")
    public MessageListenerAdapter createOrderAdaptor(OrderSubscriber orderSubscriber) {

        return createAdaptor(orderSubscriber, new TransferData());
    }


    @Bean(name = "orderListener")
    public SimpleMessageListenerContainer createMessageListenerContainer() {

        return createContainer(RabbitTypeEnum.ORDER);
    }


    @Bean("orderRabbitTemplate")
    public RabbitTemplate createRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.ORDER);

    }

    /*
      *=====================================================timeTask========================================================
      * */
    @Resource
    private Queue timeTaskQueue;

    @Bean("timeTaskQueue")
    public Queue createTimeTaskQueue() {

        return new Queue(TIMETASK_QUEUE);
    }

    @Resource
    private FanoutExchange timeTaskExchange;

    @Bean("timeTaskExchange")
    public FanoutExchange createTimeTaskExchange() {

        return new FanoutExchange(TIMETASK_EXCHANGE, true, false);
    }


    @Bean
    public Binding createTimeTaskBind() {

        return BindingBuilder.bind(timeTaskQueue).to(timeTaskExchange);
    }

    @Resource
    private MessageListenerAdapter timeTaskAdaptor;

    @Bean("timeTaskAdaptor")
    public MessageListenerAdapter createTimeTaskAdaptor(TimeTaskSubscriber timeTaskSubscriber) {


        return createAdaptor(timeTaskSubscriber, new TransferData());
    }


    @Bean(name = "timeTaskListener")
    public SimpleMessageListenerContainer createTimeTaskMessageListenerContainer() {
        return createContainer(RabbitTypeEnum.TIME_TASK);
    }


    @Bean("timeTaskRabbitTemplate")
    public RabbitTemplate createTimeTaskRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.TIME_TASK);

    }

    /*
     *=====================================================STORE========================================================
     * */
    @Resource
    private Queue storeQueue;

    @Bean("storeQueue")
    public Queue createStroeQueue() {

        return new Queue(STORE_QUEUE);
    }

    @Resource
    private FanoutExchange storeExchange;

    @Bean("storeExchange")
    public FanoutExchange createStoreExchange() {

        return new FanoutExchange(STORE_EXCHANGE, true, false);
    }


    @Bean
    public Binding createStoreBind() {

        return BindingBuilder.bind(storeQueue).to(storeExchange);
    }

    @Resource
    private MessageListenerAdapter storeAdaptor;

    @Bean("storeAdaptor")
    public MessageListenerAdapter createStoreAdaptor(StoreSubscriber storeSubscriber) {


        return createAdaptor(storeSubscriber, new TransferData());
    }


    @Bean(name = "storeListener")
    public SimpleMessageListenerContainer createStoreMessageListenerContainer() {
        return createContainer(RabbitTypeEnum.STORE);
    }


    @Bean("storeRabbitTemplate")
    public RabbitTemplate createStoreRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.STORE);

    }
    /*
    *=====================================================PARTY========================================================
    * */
    @Resource
    private Queue partyQueue;

    @Bean("partyQueue")
    public Queue createPartyQueue() {

        return new Queue(PARTY_QUEUE);
    }

    @Resource
    private FanoutExchange partyExchange;

    @Bean("partyExchange")
    public FanoutExchange createPartyExchange() {

        return new FanoutExchange(PARTY_EXCHANGE, true, false);
    }


    @Bean
    public Binding createPartyBind() {

        return BindingBuilder.bind(partyQueue).to(partyExchange);
    }

    @Resource
    private MessageListenerAdapter partyAdaptor;

    @Bean("partyAdaptor")
    public MessageListenerAdapter createPartyAdaptor(PartySubscriber partySubscriber) {


        return createAdaptor(partySubscriber, new TransferData());
    }


    @Bean(name = "partyListener")
    public SimpleMessageListenerContainer createPartyMessageListenerContainer() {
        return createContainer(RabbitTypeEnum.PARTY);
    }


    @Bean("partyRabbitTemplate")
    public RabbitTemplate createPartyRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.PARTY);

    }

    /*
         *=====================================================task========================================================
         * */
    @Resource
    private Queue taskQueue;

    @Bean("taskQueue")
    public Queue createTaskQueue() {

        return new Queue(TASK_QUEUE);
    }

    @Resource
    private FanoutExchange taskExchange;

    @Bean("taskExchange")
    public FanoutExchange createTaskExchange() {

        return new FanoutExchange(TASK_EXCHANGE, true, false);
    }


    @Bean
    public Binding createTaskBind() {

        return BindingBuilder.bind(taskQueue).to(taskExchange);
    }

    @Resource
    private MessageListenerAdapter taskAdaptor;

    @Bean("taskAdaptor")
    public MessageListenerAdapter createTaskAdaptor(TaskSubscriber taskSubscriber) {


        return createAdaptor(taskSubscriber, new TransferData());
    }


    @Bean(name = "taskListener")
    public SimpleMessageListenerContainer createTaskMessageListenerContainer() {
        return createContainer(RabbitTypeEnum.TASK);
    }


    @Bean("taskRabbitTemplate")
    public RabbitTemplate createTaskRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.TASK);

    }

    private MessageListenerAdapter createAdaptor(Object object, TransferData data) {

        MessageListenerAdapter adapter = new MessageListenerAdapter(object, "subscribe");


        adapter.setMessageConverter(new MessageConverter() {
            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {


                return null;
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {

                String msg = null;
                try {
                    msg = new String(message.getBody(), "utf-8");
                } catch (UnsupportedEncodingException e) {

                }
                if (StringUtils.isEmpty(msg)) {
                    return null;
                }
                data.setData(msg);
                return data;
            }
        });
        return adapter;
    }

    private SimpleMessageListenerContainer createContainer(RabbitTypeEnum RabbitTypeEnum) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        switch (RabbitTypeEnum) {
            case ORDER:
                container.setQueues(orderQueue);
                container.setMessageListener(orderAdaptor);
                break;
            case STORE:
                container.setQueues(storeQueue);
                container.setMessageListener(storeAdaptor);
                break;
            case TIME_TASK:
                container.setQueues(timeTaskQueue);
                container.setMessageListener(timeTaskAdaptor);
                break;
            case TASK:
                container.setQueues(taskQueue);
                container.setMessageListener(taskAdaptor);
                break;
            case PARTY:
                container.setQueues(partyQueue);
                container.setMessageListener(partyAdaptor);
                break;
            case TRYST:
                container.setQueues(trystQueue);
                container.setMessageListener(trystAdaptor);
                break;
            default:
                return container;
        }
        container.setMessageConverter(new Jackson2JsonMessageConverter());
        return container;
    }

    private RabbitTemplate createRabbitTemplate(RabbitTypeEnum RabbitTypeEnum) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(factory);
        switch (RabbitTypeEnum) {
            case ORDER:
                rabbitTemplate.setExchange(ORDER_EXCHANGE);
                rabbitTemplate.setQueue(ORDER_QUEUE);
                rabbitTemplate.setRoutingKey(ORDER_ROUTINGKEY);

                break;
            case STORE:
                rabbitTemplate.setExchange(STORE_EXCHANGE);
                rabbitTemplate.setQueue(STORE_QUEUE);
                rabbitTemplate.setRoutingKey(STORE_ROUTINGKEY);

                break;
            case TIME_TASK:
                rabbitTemplate.setExchange(TIMETASK_EXCHANGE);
                rabbitTemplate.setQueue(TIMETASK_QUEUE);
                rabbitTemplate.setRoutingKey(TIMETASK_ROUTINGKEY);
                break;
            case TASK:
                rabbitTemplate.setExchange(TASK_EXCHANGE);
                rabbitTemplate.setQueue(TASK_QUEUE);
                rabbitTemplate.setRoutingKey(TASK_ROUTINGKEY);
                break;
            case PARTY:
                rabbitTemplate.setExchange(PARTY_EXCHANGE);
                rabbitTemplate.setQueue(PARTY_QUEUE);
                rabbitTemplate.setRoutingKey(PARTY_ROUTINGKEY);
                break;
            case TRYST:
                rabbitTemplate.setExchange(TRYST_EXCHANGE);
                rabbitTemplate.setQueue(TRYST_QUEUE);
                rabbitTemplate.setRoutingKey(TRYST_ROUTINGKEY);
                break;
            default:
                return rabbitTemplate;
        }


        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.afterPropertiesSet();
        return rabbitTemplate;
    }


}
