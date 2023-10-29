package com.summer.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

@EnableCaching
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "spring.datasource")
@Getter
@Setter
public class DruidDatsSourceConfig {
    private String type = "com.alibaba.druid.pool.DruidDataSource";
    private String url = "jdbc:mysql://rm-bp18b6k555400fpo8.mysql.rds.aliyuncs.com:3306/product?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    private String username = "root";
    private String password = "gqf/dK2O9YaOEXVxviQUQUfbLP3/VyeyuLM1d4GAp03tbKuZkituEhyTbpC/ODC0GARE3/vwdnhXDRdz24K2yw==";
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Integer maxWait;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;
    private String validationQuery;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    // private String connectionProperties;
    private String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ/7tNa0xFyWWXDw5RkYd7qMWN8S9N8bI3snuWr7kXxLTnfxagrXKOJOXOwA3PXolb4wSwpAjmlqTm/YIrRwltkCAwEAAQ==";


    @Bean
    @Primary
    public DataSource druidDataSource() throws Exception {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(MoreObjects.firstNonNull(initialSize, 0));
        datasource.setMinIdle(MoreObjects.firstNonNull(minIdle, 0));
        datasource.setMaxActive(MoreObjects.firstNonNull(maxActive, 8));
        datasource.setMaxWait(MoreObjects.firstNonNull(maxWait, -1));
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        // datasource.setConnectionProperties(connectionProperties);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            log.error("========druid configuration initialization filter========", e);
        }
        return datasource;
    }

    /**
     * 配置监控服务器
     * @return 返回监控注册的servlet对象
     * @author SimpleWu
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加IP白名单
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        servletRegistrationBean.addInitParameter("deny", "127.0.0.1");
        // 添加控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin888");
        // 是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }
}
