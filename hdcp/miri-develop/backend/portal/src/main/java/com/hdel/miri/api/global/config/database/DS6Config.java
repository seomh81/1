package com.hdel.miri.api.global.config.database;

import com.hdel.miri.api.global.config.database.annotation.DS6Annotation;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * HCCC 연결 정보
 * */
@Slf4j
@Configuration
@MapperScan(basePackages = {
        "com.hdel.miri.api.domain.hccc"
}
        , sqlSessionFactoryRef = "ds6SqlSessionFactory"
        , annotationClass= DS6Annotation.class)
@RequiredArgsConstructor
@EnableTransactionManagement
public class DS6Config {
    @Value("${spring.app.ds6.auto-commit}")
    private String autoCommit;
    @Value("${spring.app.ds6.connection-test-query}")
    private String testQuery;
    @Value("${spring.app.ds6.connection-timeout}")
    private String timeOut;
    @Value("${spring.app.ds6.driver-class-name}")
    private String driverClassName;
    @Value("${spring.app.ds6.jdbc-url}")
    private String jdbcUrl;
    @Value("${spring.app.ds6.maximum-pool-size}")
    private String maxPoolSize;
    @Value("${spring.app.ds6.minimum-idle}")
    private String minIDLE;
    @Value("${spring.app.ds6.username}")
    private String username;
    @Value("${spring.app.ds6.password}")
    private String password;

    @Bean(name = "ds6HiakriConfig")
    //@Primary
    public HikariConfig ds6HiakriConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setAutoCommit(Boolean.parseBoolean(autoCommit));
        hikariConfig.setConnectionTestQuery(testQuery);
        hikariConfig.setConnectionTimeout(Long.parseLong(timeOut));
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        hikariConfig.setMinimumIdle(Integer.parseInt(minIDLE));
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        return hikariConfig;
    }

    @Bean(name = "ds6DataSource")
    //@Primary
    public DataSource ds6DataSource(@Qualifier("ds6HiakriConfig") HikariConfig hikariConfig) {
        DataSource ds = new HikariDataSource(hikariConfig);
        return ds;
    }

    //@Primary
    @Bean(name = "ds6SqlSessionFactory")
    public SqlSessionFactory ds6SqlSessionFactory(@Qualifier("ds6DataSource") DataSource ds,
                                                  ApplicationContext ac ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(ds);
        sqlSessionFactoryBean.setMapperLocations(ac.getResources("classpath:mapper/ds6/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(ac.getResource("classpath:database/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    //@Primary
    @Bean(name = "ds6SqlSessionTemplate")
    public SqlSessionTemplate ds6SqlSessionTemplate(@Qualifier("ds6SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //@Primary
    @Bean(name = "db6TxManager")
    public PlatformTransactionManager db6TxManager(@Qualifier("ds6DataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        dataSourceTransactionManager.setNestedTransactionAllowed(true);
        return dataSourceTransactionManager;
    }
}
