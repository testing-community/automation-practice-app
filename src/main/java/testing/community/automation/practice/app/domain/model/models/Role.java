package testing.community.automation.practice.app.domain.model.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class Role {
    private Integer id;

    private String name;

    public Role() {

    }

    public Role(String name) {
        this.id = new Random().nextInt();
        this.name = name;
    }
}
