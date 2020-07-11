package org.coding.webclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.channel.BootstrapHandlers;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class Config {
    @Bean
    public WebClient webClient () {
        return WebClient
                .builder()
                .baseUrl("https://jsonplaceholder.typicode.com/posts")
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    private static class HttpLoggingHandler extends LoggingHandler {
        private MessageParser messageParser;
        public HttpLoggingHandler(MessageParser messageParser) {
            this.messageParser = messageParser;
        }

        @Override
        protected String format(ChannelHandlerContext ctx, String event, Object arg) {
            if (arg instanceof ByteBuf) {
                ByteBuf msg = (ByteBuf) arg;

                return "LALALA: " + messageParser.parse(event, msg.toString(StandardCharsets.UTF_8));
            }
            log.debug("#OTHER CLASS: {}", arg.getClass().getName());
            return super.format(ctx, event, arg);
        }
    }

    public MessageParser messageParser () {
        return new MessageParser(new ObjectMapper());
    }

    public HttpClient httpClient() {
        HttpClient client = HttpClient.create()
                .tcpConfiguration(tcpClient ->
                        tcpClient.bootstrap(bootstrap ->
                                BootstrapHandlers.updateLogSupport(bootstrap, new HttpLoggingHandler(messageParser ()))));
        client.wiretap(true);
        return client;
    }

}
