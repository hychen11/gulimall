package com.hychen11.order.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import io.seata.rm.datasource.DataSourceProxy;
import javax.sql.DataSource;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeataConfig
 * @date ：2025/8/22 12:40
 */
@Configuration
public class SeataConfig {
    @Autowired
    DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties){
        //得到原来的数据源
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (StringUtils.hasText(dataSourceProperties.getName())){
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        //创建Seata的数据源，把老数据源传入进行包装
        return new DataSourceProxy(dataSource);
    }
}
