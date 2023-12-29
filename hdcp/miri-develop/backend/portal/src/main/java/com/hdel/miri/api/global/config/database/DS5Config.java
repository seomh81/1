package com.hdel.miri.api.global.config.database;

import com.hdel.miri.api.global.config.database.annotation.DS5Annotation;
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
 * HRTS 연결 정보
 * */
@Slf4j
@Configuration
@MapperScan(basePackages = {
        "com.hdel.miri.api.domain.hrts"
}
        , sqlSessionFactoryRef = "ds5SqlSessionFactory"
        , annotationClass= DS5Annotation.class)
@RequiredArgsConstructor
@EnableTransactionManagement
public class DS5Config {
    @Value("${spring.app.ds5.auto-commit}")
    private String autoCommit;
    @Value("${spring.app.ds5.connection-test-query}")
    private String testQuery;
    @Value("${spring.app.ds5.connection-timeout}")
    private String timeOut;
    @Value("${spring.app.ds5.driver-class-name}")
    private String driverClassName;
    @Value("${spring.app.ds5.jdbc-url}")
    private String jdbcUrl;
    @Value("${spring.app.ds5.maximum-pool-size}")
    private String maxPoolSize;
    @Value("${spring.app.ds5.minimum-idle}")
    private String minIDLE;
    @Value("${spring.app.ds5.username}")
    private String username;
    @Value("${spring.app.ds5.password}")
    private String password;

    @Bean(name = "ds5HiakriConfig")
    //@Primary
    public HikariConfig ds5HiakriConfig() {
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

    @Bean(name = "ds5DataSource")
    //@Primary
    public DataSource ds5DataSource(@Qualifier("ds5HiakriConfig") HikariConfig hikariConfig) {
        DataSource ds = new HikariDataSource(hikariConfig);
        return ds;
    }

    //@Primary
    @Bean(name = "ds5SqlSessionFactory")
    public SqlSessionFactory ds5SqlSessionFactory(@Qualifier("ds5DataSource") DataSource ds,
                                                  ApplicationContext ac ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(ds);
        sqlSessionFactoryBean.setMapperLocations(ac.getResources("classpath:mapper/ds5/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(ac.getResource("classpath:database/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    //@Primary
    @Bean(name = "ds5SqlSessionTemplate")
    public SqlSessionTemplate ds5SqlSessionTemplate(@Qualifier("ds5SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //@Primary
    @Bean(name = "db5TxManager")
    public PlatformTransactionManager db5TxManager(@Qualifier("ds5DataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        dataSourceTransactionManager.setNestedTransactionAllowed(true);
        return dataSourceTransactionManager;
    }
}
