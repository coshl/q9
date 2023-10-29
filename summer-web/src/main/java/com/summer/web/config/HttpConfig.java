package com.summer.web.config;

import org.springframework.context.annotation.Configuration;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @Decription: 配置同时支持 HTTP 与 HTTPS 访问
 **/
@Configuration
public class HttpConfig {
    @Value("${server.custom.httpPort}")
    private Integer httpPort;
    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

}
