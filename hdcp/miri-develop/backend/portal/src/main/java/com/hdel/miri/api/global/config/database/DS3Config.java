package com.hdel.miri.api.global.config.database;

import com.hdel.miri.api.global.config.database.annotation.DS3Annotation;
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
 * SCRM 연결 정보
 * */
@Slf4j
@Configuration
@MapperScan(basePackages = {"com.hdel.miri.api.domain.scrm"}
        , sqlSessionFactoryRef = "ds3SqlSessionFactory"
        , annotationClass= DS3Annotation.class)
@RequiredArgsConstructor
@EnableTransactionManagement
public class DS3Config {
    @Value("${spring.app.ds3.auto-commit}")
    private String autoCommit;
    @Value("${spring.app.ds3.connection-test-query}")
    private String testQuery;
    @Value("${spring.app.ds3.connection-timeout}")
    private String timeOut;
    @Value("${spring.app.ds3.driver-class-name}")
    private String driverClassName;
    @Value("${spring.app.ds3.jdbc-url}")
    private String jdbcUrl;
    @Value("${spring.app.ds3.maximum-pool-size}")
    private String maxPoolSize;
    @Value("${spring.app.ds3.minimum-idle}")
    private String minIDLE;
    @Value("${spring.app.ds3.username}")
    private String username;
    @Value("${spring.app.ds3.password}")
    private String password;
    
    @Bean(name = "ds3HiakriConfig")
    //@Primary
    public HikariConfig ds3HiakriConfig() {
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

    @Bean(name = "ds3DataSource")
    //@Primary
    public DataSource ds3DataSource(@Qualifier("ds3HiakriConfig") HikariConfig hikariConfig) {
        DataSource ds = new HikariDataSource(hikariConfig);
        return ds;
    }

    //@Primary
    @Bean(name = "ds3SqlSessionFactory")
    public SqlSessionFactory ds3SqlSessionFactory(@Qualifier("ds3DataSource") DataSource ds,
                                                  ApplicationContext ac ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(ds);
        sqlSessionFactoryBean.setMapperLocations(ac.getResources("classpath:mapper/ds3/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(ac.getResource("classpath:database/mybatis-config.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    //@Primary
    @Bean(name = "ds3SqlSessionTemplate")
    public SqlSessionTemplate ds3SqlSessionTemplate(@Qualifier("ds3SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //@Primary
    @Bean(name = "db3TxManager")
    public PlatformTransactionManager db3TxManager(@Qualifier("ds3DataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        dataSourceTransactionManager.setNestedTransactionAllowed(true);
        return dataSourceTransactionManager;
    }
}
