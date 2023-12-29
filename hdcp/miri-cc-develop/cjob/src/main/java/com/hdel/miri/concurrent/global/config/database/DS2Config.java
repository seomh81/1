package com.hdel.miri.concurrent.global.config.database;

import com.hdel.miri.concurrent.global.config.database.annotation.DS2Annotation;
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

@Slf4j
@Configuration
@MapperScan(basePackages = {"com.hdel.miri.concurrent.domain.scrm"}
        , sqlSessionFactoryRef = "ds2SqlSessionFactory"
        , annotationClass= DS2Annotation.class)
@RequiredArgsConstructor
@EnableTransactionManagement
public class DS2Config {
    @Value("${spring.app.ds2.auto-commit}")
    private String autoCommit;
    @Value("${spring.app.ds2.connection-test-query}")
    private String testQuery;
    @Value("${spring.app.ds2.connection-timeout}")
    private String timeOut;
    @Value("${spring.app.ds2.driver-class-name}")
    private String driverClassName;
    @Value("${spring.app.ds2.jdbc-url}")
    private String jdbcUrl;
    @Value("${spring.app.ds2.maximum-pool-size}")
    private String maxPoolSize;
    @Value("${spring.app.ds2.minimum-idle}")
    private String minIDLE;
    @Value("${spring.app.ds2.username}")
    private String username;
    @Value("${spring.app.ds2.password}")
    private String password;
    
    @Bean(name = "ds2HiakriConfig")
    //@Primary
    public HikariConfig ds2HiakriConfig() {
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

    @Bean(name = "ds2DataSource")
    //@Primary
    public DataSource ds2DataSource(@Qualifier("ds2HiakriConfig") HikariConfig hikariConfig) {
        DataSource ds = new HikariDataSource(hikariConfig);
        return ds;
    }

    //@Primary
    @Bean(name = "ds2SqlSessionFactory")
    public SqlSessionFactory ds2SqlSessionFactory(@Qualifier("ds2DataSource") DataSource ds,
                                                ApplicationContext ac ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(ds);
        sqlSessionFactoryBean.setMapperLocations(ac.getResources("classpath:mapper/ds2/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(ac.getResource("classpath:database/mybatis-config.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    //@Primary
    @Bean(name = "ds2SqlSessionTemplate")
    public SqlSessionTemplate ds2SqlSessionTemplate(@Qualifier("ds2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //@Primary
    @Bean(name = "db2TxManager")
    public PlatformTransactionManager db2TxManager(@Qualifier("ds2DataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        dataSourceTransactionManager.setNestedTransactionAllowed(true);
        return dataSourceTransactionManager;
    }
}
