package testing.community.automation.practice.app.db.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "skill")
@Table(name = "skill", schema = "test_schema")
@Data
@NoArgsConstructor
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(max = 100)
    @Column(name = "name")
    private String name;

    public SkillEntity(String name) {
        this.name = name;
    }
}
