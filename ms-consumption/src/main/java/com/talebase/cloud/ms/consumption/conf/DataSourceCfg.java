package com.talebase.cloud.ms.consumption.conf;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by suntree.xu on 2016-12-7.
 */
@Configuration
public class DataSourceCfg {

    @Bean(name="consumeDs")
//    @Primary
    @ConfigurationProperties(prefix="datasource.consume")
    public DataSource userDataSource(@Value("${datasource.consume.type}") String clazName) throws ClassNotFoundException {
        ClassLoader currentLoader = null;
        DataSourceBuilder dsBuilder = null;

        try{
            currentLoader = Thread.currentThread().getContextClassLoader();
            dsBuilder = new DataSourceBuilder(currentLoader);
            return dsBuilder.type((Class<? extends DataSource>) Class.forName(clazName)).build();
        }finally {
            Thread.currentThread().setContextClassLoader(currentLoader);
        }

    }

//    @Qualifier("userDs")
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactoryBean.getObject();
    }

}
