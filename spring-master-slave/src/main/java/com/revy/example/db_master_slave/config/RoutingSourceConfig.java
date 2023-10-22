package com.revy.example.db_master_slave.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableJpaRepositories(
        basePackages = "com.revy.example",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@Configuration
public class RoutingSourceConfig {

    // routing dataSource Bean
    @Bean(DataSourceConstant.ROUTING_DATA_SOURCE)
    @DependsOn({DataSourceConstant.MASTER_DATA_SOURCE, DataSourceConstant.SLAVE_DATA_SOURCE})
    public DataSource routingDataSource (
            @Qualifier(DataSourceConstant.MASTER_DATA_SOURCE) DataSource masterDataSource,
            @Qualifier(DataSourceConstant.SLAVE_DATA_SOURCE) DataSource slaveDataSource) {
        Map<Object, Object> dataSourceMap = new HashMap<Object, Object>() {
            {
                put(DataSourceConstant.MASTER_DATA_SOURCE, masterDataSource);
                put(DataSourceConstant.SLAVE_DATA_SOURCE, slaveDataSource);
            }
        };

        RoutingDataSource routingDatasource = new RoutingDataSource();
        routingDatasource.setTargetDataSources(dataSourceMap);
        routingDatasource.setDefaultTargetDataSource(masterDataSource);
        return routingDatasource;
    }

    // setting lazy connection
    @DependsOn(DataSourceConstant.ROUTING_DATA_SOURCE)
    @Bean(DataSourceConstant.DATA_SOURCE)
    public LazyConnectionDataSourceProxy dataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean(DataSourceConstant.ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(DataSourceConstant.DATA_SOURCE) DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory
                = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.revy.example");
        entityManagerFactory.setJpaVendorAdapter(this.jpaVendorAdapter());
        entityManagerFactory.setPersistenceUnitName("entityManager");
        return entityManagerFactory;
    }

    private JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        hibernateJpaVendorAdapter.setShowSql(false);
//        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        return hibernateJpaVendorAdapter;
    }

    @Bean("transactionManager")
    public PlatformTransactionManager platformTransactionManager(
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return jpaTransactionManager;
    }

}
