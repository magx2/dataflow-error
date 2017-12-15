package spring.cloud.data.flow.error.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OutEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    private long id;
    @Column
    private String name;
}
