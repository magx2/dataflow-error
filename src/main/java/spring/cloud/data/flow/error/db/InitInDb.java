package spring.cloud.data.flow.error.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
@Slf4j
class InitInDb {
    private final DataSource dataSource;

    InitInDb(@Qualifier("inputDatasource") final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    void init() throws SQLException, IOException, URISyntaxException {
        log.info("Init InDb...");
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement()) {
            statement.execute(readInitSql());
        }
    }

    private String readInitSql() throws URISyntaxException, IOException {
        return Files.readAllLines(
                Paths.get(this.getClass().getResource("/initInDb.sql").toURI()), Charset.defaultCharset())
                       .stream().collect(Collectors.joining("\n"));
    }
}
