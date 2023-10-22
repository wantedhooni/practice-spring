package com.revy.example.db_master_slave.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSourceConstant {

    public static final String MASTER_DATA_SOURCE = "master";
    public static final String SLAVE_DATA_SOURCE = "slave";

    public static final String DATA_SOURCE = "dataSource";
    public static final String ROUTING_DATA_SOURCE = "routingDataSource";
    public static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";

}
