package testing.community.automation.practice.app.db.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "userrole")
@Table(name = "userrole", schema = "test_schema")
@Data
@NoArgsConstructor
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "userid")
    private Long userId;
    @Column(name = "roleid")
    private Long roleId;

    public UserRoleEntity(Long userid, Long roleid) {
        this.userId = userid;
        this.roleId = roleid;
    }
}
