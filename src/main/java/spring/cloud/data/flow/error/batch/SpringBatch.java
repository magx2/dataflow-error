package spring.cloud.data.flow.error.batch;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import spring.cloud.data.flow.error.db.InEntity;
import spring.cloud.data.flow.error.db.OutEntity;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Calendar;

@SuppressWarnings("ConstantConditions")
@Configuration
public class SpringBatch {

    @Bean
    Job testJob(JobBuilderFactory jobBuilderFactory) throws Exception {
        return jobBuilderFactory.get("test job")
                       .flow(testStep(null, null, null))
                       .end()
                       .build();
    }

    @Bean
    Step testStep(StepBuilderFactory stepBuilderFactory,
                  JobRepository jobRepository,
                  PlatformTransactionManager transactionManager) throws Exception {
        return stepBuilderFactory.get("test")
                       .<InEntity, OutEntity>chunk(5)
                       .reader(reader(null))
                       .processor(processor())
                       .writer(writer(null))
                       .transactionManager(transactionManager)
                       .repository(jobRepository)
                       .build();
    }

    @Bean
    ItemReader<InEntity> reader(@Qualifier("inputDatasource") DataSource inputDatasource) throws Exception {
        val reader = new JdbcPagingItemReader<InEntity>();

        reader.setDataSource(inputDatasource);
        reader.setQueryProvider(readerQueryProvider(null));
        reader.setPageSize(10);
        reader.setRowMapper(new BeanPropertyRowMapper<>(InEntity.class));

        return reader;
    }

    @Bean
    PagingQueryProvider readerQueryProvider(@Qualifier("inputDatasource") DataSource inputDatasource) throws Exception {
        val provider = new SqlPagingQueryProviderFactoryBean();

        provider.setDataSource(inputDatasource);
        provider.setSelectClause("select * ");
        provider.setFromClause("from in_entity");
        provider.setSortKey("property");

        return provider.getObject();
    }

    @Bean
    ItemProcessor<InEntity, OutEntity> processor() {
        return inEntity -> {
            sleep(500);
           return new OutEntity(0, inEntity.getProperty());
        };
    }

    @SuppressWarnings("SameParameterValue")
    @SneakyThrows
    private void sleep(long duration) {
        Thread.sleep(duration);
    }

    @Bean
    ItemWriter<OutEntity> writer(EntityManagerFactory entityManagerFactory) {
        val writer = new JpaItemWriter<OutEntity>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
