package spring.cloud.data.flow.error.batch;

import lombok.val;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
class BatchConfigurer implements org.springframework.batch.core.configuration.annotation.BatchConfigurer {
    private final DataSource dataSource;

    private JobRepository jobRepository;
    private PlatformTransactionManager platformTransactionManager;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;
    private TaskExecutor jobTaskExecutor;

    BatchConfigurer(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    void init() throws Exception {
        platformTransactionManager = new DataSourceTransactionManager(dataSource);

        {
            val jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
            jobRepositoryFactoryBean.setDataSource(dataSource);
            jobRepositoryFactoryBean.setTransactionManager(platformTransactionManager);
            jobRepositoryFactoryBean.afterPropertiesSet();
            jobRepository = jobRepositoryFactoryBean.getObject();
        }

        jobTaskExecutor = new SyncTaskExecutor();

        {
            val simpleJobLauncher = new SimpleJobLauncher();
            simpleJobLauncher.setJobRepository(jobRepository);
            simpleJobLauncher.setTaskExecutor(jobTaskExecutor);
            simpleJobLauncher.afterPropertiesSet();
            jobLauncher = simpleJobLauncher;
        }

        {
            val jobExplorerFactoryBean = new JobExplorerFactoryBean();
            jobExplorerFactoryBean.setDataSource(dataSource);
            jobExplorerFactoryBean.afterPropertiesSet();
            this.jobExplorer = jobExplorerFactoryBean.getObject();
        }
    }

    @Override
    public JobRepository getJobRepository()  {
        return jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager()  {
        return platformTransactionManager;
    }

    @Override
    public JobLauncher getJobLauncher()  {
        return jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() {
        return jobExplorer;
    }
}
