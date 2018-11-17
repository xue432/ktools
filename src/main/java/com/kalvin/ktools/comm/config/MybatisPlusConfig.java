package com.kalvin.ktools.comm.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * mybatisPlus配置
 */
@Configuration
@MapperScan(basePackages = "com.kalvin.ktools.dao.*")
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * druid注入
     * @return dataSource
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * MP SqlSessionFactory配置
     * @param dataSource d
     * @return SqlSessionFactory
     */
    @Bean(name = "sqlSessionFactory")
    @Primary
    @ConfigurationProperties(prefix = "mybatis-plus")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        return sessionFactoryBean.getObject();
    }

    /**
     * 配置事物管理器
     * @return DataSourceTransactionManager
     */
    @Bean(name="transactionManager")
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * 配置分页插件
     * @return page
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

    @Bean
    @Profile(value = "dev")
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        // SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长
        performanceInterceptor.setMaxTime(1000);
        // SQL是否格式化 默认false
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }
}
