package com.kafkaconsumer.kafkaconsumer.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kafkaconsumer.kafkaconsumer.model.Users;

import org.apache.catalina.mapper.Mapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;



@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String,Users> consumerFactory(){
       
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "134.122.109.128:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "groud_id");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES,"*" );

        return new DefaultKafkaConsumerFactory<>(config,new StringDeserializer(),new JsonDeserializer<>(Users.class));

    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Users> kafkaListenerContainerFactory()  {

        ConcurrentKafkaListenerContainerFactory<String, Users> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setErrorHandler(new ErrorHandler()    
        {
            @Override
            public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
                String s = thrownException.getMessage().split("Error deserializing key/value for partition ")[1].split(". If needed, please seek past the record to continue consumption.")[0];
                String topics = s.split("-")[0];
                int offset = Integer.valueOf(s.split("offset ")[1]);
                int partition = Integer.valueOf(s.split("-")[1].split(" at")[0]);

                TopicPartition topicPartition = new TopicPartition(topics, partition);
                //log.info("Skipping " + topic + "-" + partition + " offset " + offset);
                consumer.seek(topicPartition, offset + 1);
                System.out.println("OKKKKKwew");
            }

            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord) {

            }

            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord, Consumer<?,?> consumer) {
                String s = e.getMessage().split("Error deserializing key/value for partition ")[1].split(". If needed, please seek past the record to continue consumption.")[0];
                System.err.println(s);
                String topics = s.split("-")[0];
                int offset = Integer.valueOf(s.split("offset ")[1]);
                int partition = Integer.valueOf(s.split("-")[1].split(" at")[0]);

                TopicPartition topicPartition = new TopicPartition(topics, partition);
                //log.info("Skipping " + topic + "-" + partition + " offset " + offset);
                consumer.seek(topicPartition, offset + 1);
                System.out.println("OKKKKK");


            }
        });


        return factory;
    }

    
}