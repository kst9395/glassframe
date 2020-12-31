package com.ethopia.tradecabinet.lifecycle;


public enum AppConfiguration {
    APP_JDBC_URL("jdbc.url"),
    APP_JDBC_USERNAME("jdbc.username"),
    APP_JDBC_PASSWORD("jdbc.password"),
    APP_JDBC_DRIVER_CLASS("jdbc.driverclass"),
    APP_CONNECTION_POOL_SIZE("jdbc.pool.size"),
    APP_CONNECTION_POOL_TEST_QUERY("jdbc.pool.testquery"),
    APP_DATABASE_MIGRATION_MASTER_FILE("database.migration.masterfile"),
    APP_WORKER_EXECUTOR_POOL_NAME("executor.pool.name"),
    APP_WORKER_EXECUTOR_POOL_SIZE("executor.pool.size");
    private String key;

    public String key() {
        return key;
    }

    AppConfiguration(String key) {
        this.key = key;
    }

}
