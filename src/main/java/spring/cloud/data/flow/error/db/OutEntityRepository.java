package spring.cloud.data.flow.error.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutEntityRepository extends JpaRepository<InEntity, Long> {
}
