package spring.cloud.data.flow.error.dataflow;

import lombok.val;
import org.springframework.cloud.task.configuration.TaskConfigurer;
import org.springframework.cloud.task.repository.TaskExplorer;
import org.springframework.cloud.task.repository.TaskRepository;
import org.springframework.cloud.task.repository.support.SimpleTaskExplorer;
import org.springframework.cloud.task.repository.support.SimpleTaskRepository;
import org.springframework.cloud.task.repository.support.TaskExecutionDaoFactoryBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
class DataFlowTaskConfigurer implements TaskConfigurer {
    private final DataSource dataSource;
    private final TaskRepository taskRepository;
    private final TaskExplorer taskExplorer;
    private final PlatformTransactionManager platformTransactionManager;

    DataFlowTaskConfigurer(final DataSource dataSource) {
        this.dataSource = dataSource;

        this.platformTransactionManager = new DataSourceTransactionManager(dataSource);
        val taskExecutionDaoFactoryBean = new TaskExecutionDaoFactoryBean(dataSource);
        taskRepository = new SimpleTaskRepository(taskExecutionDaoFactoryBean);
        taskExplorer = new SimpleTaskExplorer(taskExecutionDaoFactoryBean);
    }

    @Override
    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return platformTransactionManager;
    }

    @Override
    public TaskExplorer getTaskExplorer() {
        return taskExplorer;
    }

    @Override
    public DataSource getTaskDataSource() {
        return dataSource;
    }
}
