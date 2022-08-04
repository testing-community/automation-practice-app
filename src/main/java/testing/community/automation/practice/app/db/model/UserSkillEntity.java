package testing.community.automation.practice.app.db.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "userskill")
@Table(name = "userskill", schema = "test_schema")
@Data
@NoArgsConstructor
public class UserSkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "userid")
    private Long userId;
    @Column(name = "skillid")
    private Long skillId;

    public UserSkillEntity(Long userid, Long skillId) {
        this.userId = userid;
        this.skillId = skillId;
    }
}
