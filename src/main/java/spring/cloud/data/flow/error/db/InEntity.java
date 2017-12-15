package spring.cloud.data.flow.error.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
@Table
public class InEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    private long id;
    @Column
    private String property;
}
