package com.example.demo.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.ttddyy.dsproxy.listener.ChainListener;
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Configuration
@PropertySource({ "/jdbc-postgresql.properties" })
@ComponentScan(basePackages = "com.example.demo")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class JPATransactionManagerConfiguration {

	public static final String DATA_SOURCE_PROXY_NAME = DataSourceProxyType.DATA_SOURCE_PROXY.name();

	@Value("${jdbc.dataSourceClassName}")
	private String dataSourceClassName;

	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.username}")
	private String jdbcUser;

	@Value("${jdbc.password}")
	private String jdbcPassword;

	@Value("${hibernate.dialect}")
	private String hibernateDialect;

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(destroyMethod = "close")
	public DataSource actualDataSource() {
		Properties driverProperties = new Properties();
		driverProperties.setProperty("url", jdbcUrl);
		driverProperties.setProperty("user", jdbcUser);
		driverProperties.setProperty("password", jdbcPassword);

		Properties properties = new Properties();
		properties.put("dataSourceClassName", dataSourceClassName);
		properties.put("dataSourceProperties", driverProperties);
		properties.setProperty("maximumPoolSize", String.valueOf(3));
		return new HikariDataSource(new HikariConfig(properties));
	}

	@Bean
	public DataSource dataSource() {
		ChainListener listener = new ChainListener();
		listener.addListener(new SLF4JQueryLoggingListener());
		listener.addListener(new DataSourceQueryCountListener());
		
		return ProxyDataSourceBuilder.create(actualDataSource()).name(DATA_SOURCE_PROXY_NAME).listener(listener)
				.build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setPersistenceUnitName(getClass().getSimpleName());
		localContainerEntityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
		localContainerEntityManagerFactoryBean.setDataSource(dataSource());
		localContainerEntityManagerFactoryBean.setPackagesToScan(packagesToScan());

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		localContainerEntityManagerFactoryBean.setJpaProperties(additionalProperties());
		return localContainerEntityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	@Bean
	public TransactionTemplate transactionTemplate(EntityManagerFactory entityManagerFactory) {
		return new TransactionTemplate(transactionManager(entityManagerFactory));
	}

	protected Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.hbm2ddl.auto", "none");
		return properties;
	}

	protected String[] packagesToScan() {
		return new String[] { "com.example.demo" };
	}
}
