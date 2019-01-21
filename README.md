# eshop-cache
基于kafka+ehcache+redis完成&lt;缓存数据处理服务>开发 
1 、将kafka整合到spring boot中 
`
<dependency> 
    <groupId>org.apache.kafka</groupId> 
    <artifactId>kafka_2.9.2</artifactId> 
    <version>0.8.1</version> 
</dependency> 
`
```
@Bean 
public ServletListenerRegistrationBean servletListenerRegistrationBean() { 
     ServletListenerRegistrationBean servletListenerRegistrationBean =  
               new ServletListenerRegistrationBean(); 
     servletListenerRegistrationBean.setListener(new InitListener());   
     return servletListenerRegistrationBean; 
} 

public class KafkaConcusmer implements Runnable { 

    private final ConsumerConnector consumer; 
    private final String topic; 
      
    public ConsumerGroupExample(String topic) { 
        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig()); 
        this.topic = topic; 
    } 

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) { 
        Properties props = new Properties(); 
        props.put("zookeeper.connect", "192.168.31.187:2181,192.168.31.19:2181,192.168.31.227:2181"); 
        props.put("group.id", "eshop-cache-group"); 
        props.put("zookeeper.session.timeout.ms", "400"); 
        props.put("zookeeper.sync.time.ms", "200"); 
        props.put("auto.commit.interval.ms", "1000"); 
        return new ConsumerConfig(props); 
    } 
      
    public void run() { 
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>(); 
        topicCountMap.put(topic, 1); 
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap); 
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic); 

        for (final KafkaStream stream : streams) { 
            new Thread(new KafkaMessageProcessor(stream)).start(); 
        } 
    } 

} 

public class KafkaMessageProcessor implements Runnable { 

    private KafkaStream kafkaStream; 
  
    public ConsumerTest(KafkaStream kafkaStream) { 
         this.kafkaStream = kafkaStream; 
    } 
  
    public void run() { 
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator(); 
        while (it.hasNext()) { 
             String message = new String(it.next().message()); 
        } 
    } 

} 

ServletContext sc = sce.getServletContext(); 
ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc); 
```
2 、编写业务逻辑 

（1）两种服务会发送来数据变更消息：商品信息服务，商品店铺信息服务，每个消息都包含服务名以及商品id 

（2）接收到消息之后，根据商品id到对应的服务拉取数据，这一步，我们采取简化的模拟方式，就是在代码里面写死，会获取到什么数据，不去实际再写其他的服务去调用了 

（3）商品信息：id，名称，价格，图片列表，商品规格，售后信息，颜色，尺寸 

（4）商品店铺信息：其他维度，用这个维度模拟出来缓存数据维度化拆分，id，店铺名称，店铺等级，店铺好评率 

（5）分别拉取到了数据之后，将数据组织成json串，然后分别存储到ehcache中，和redis缓存中 

3 、测试业务逻辑 

（1）创建一个kafka topic 

（2）在命令行启动一个kafka producer 

（3）启动系统，消费者开始监听kafka topic 

C:\Windows\System32\drivers\etc\hosts 

（4）在producer中，分别发送两条消息，一个是商品信息服务的消息，一个是商品店铺信息服务的消息 

（5）能否接收到两条消息，并模拟拉取到两条数据，同时将数据写入ehcache中，并写入redis缓存中 

（6）ehcache通过打印日志方式来观察，redis通过手工连接上去来查询 


