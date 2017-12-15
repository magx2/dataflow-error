package spring.cloud.data.flow.error;

import lombok.val;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import spring.cloud.data.flow.error.db.OutEntity;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
@EnableTask
@EnableBatchProcessing
@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManger")
public class ErrorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErrorApplication.class, args);
	}

	@Bean
	DataSource inputDatasource() {
		val datasource  = new DriverManagerDataSource();
		datasource.setDriverClassName("org.h2.Driver");
		datasource.setUrl("jdbc:h2:mem:input;DB_CLOSE_DELAY=-1;");
		return datasource;
	}

	@Primary
	@Bean
	DataSource outputDatasource() {
		val datasource  = new DriverManagerDataSource();
		datasource.setDriverClassName("org.h2.Driver");
		datasource.setUrl("jdbc:h2:./bin/output;AUTO_SERVER=TRUE");
		datasource.setUsername("u");
		datasource.setPassword("p");
		return datasource;
	}

	@Bean
	@Primary
	JpaTransactionManager jpaTransactionManger(EntityManagerFactory entityManagerFactory) {
		val manager = new JpaTransactionManager(entityManagerFactory);
		manager.setJpaDialect(new HibernateJpaDialect());
		return manager;
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Properties hibernateProperties) {
		val em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(OutEntity.class.getPackage().getName());
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties);

		return em;
	}

	@Bean
	Properties hibernateProperties() {
		val p = new Properties();
		p.setProperty("hibernate.hbm2ddl.auto", "create");
		p.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		return p;
	}
}
