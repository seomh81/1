package com.hdel.miri.concurrent.global.config.database;

import com.hdel.miri.concurrent.global.config.database.annotation.DS1Annotation;
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
 * DS1 - Oracle DB
 * */
@Slf4j
@Configuration
@MapperScan(
        basePackages = {
            "com.hdel.miri.concurrent.domain.auth"
            ,"com.hdel.miri.concurrent.domain.user"
            ,"com.hdel.miri.concurrent.domain.masterdata.repository"
            ,"com.hdel.miri.concurrent.domain.contract"
            ,"com.hdel.miri.concurrent.domain.dgk"
        }
        , sqlSessionFactoryRef = "ds1SqlSessionFactory"
        , annotationClass= DS1Annotation.class)
@RequiredArgsConstructor
@EnableTransactionManagement
public class DS1Config {
    @Value("${spring.app.ds1.auto-commit}")
    private String autoCommit;
    @Value("${spring.app.ds1.connection-test-query}")
    private String testQuery;
    @Value("${spring.app.ds1.connection-timeout}")
    private String timeOut;
    @Value("${spring.app.ds1.driver-class-name}")
    private String driverClassName;
    @Value("${spring.app.ds1.jdbc-url}")
    private String jdbcUrl;
    @Value("${spring.app.ds1.maximum-pool-size}")
    private String maxPoolSize;
    @Value("${spring.app.ds1.minimum-idle}")
    private String minIDLE;
    @Value("${spring.app.ds1.username}")
    private String username;
    @Value("${spring.app.ds1.password}")
    private String password;

    @Bean(name = "ds1HiakriConfig")
    //@Primary
    public HikariConfig ds1HiakriConfig() {
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

    @Bean(name = "ds1DataSource")
    //@Primary
    public DataSource ds1DataSource(@Qualifier("ds1HiakriConfig") HikariConfig hikariConfig) {
        DataSource ds = new HikariDataSource(hikariConfig);
        return ds;
    }

    //@Primary
    @Bean(name = "ds1SqlSessionFactory")
    public SqlSessionFactory ds1SqlSessionFactory( @Qualifier("ds1DataSource") DataSource ds,
                                                ApplicationContext ac ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(ds);
        sqlSessionFactoryBean.setMapperLocations(ac.getResources("classpath:mapper/ds1/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(ac.getResource("classpath:database/mybatis-config.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    //@Primary
    @Bean(name = "ds1SqlSessionTemplate")
    public SqlSessionTemplate ds1SqlSessionTemplate( @Qualifier("ds1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //@Primary
    @Bean(name = "db1TxManager")
    public PlatformTransactionManager db1TxManager(@Qualifier("ds1DataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        dataSourceTransactionManager.setNestedTransactionAllowed(true);
        return dataSourceTransactionManager;
    }
}
