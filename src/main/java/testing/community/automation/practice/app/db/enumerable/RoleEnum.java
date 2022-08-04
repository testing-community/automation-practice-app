package testing.community.automation.practice.app.db.enumerable;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN ("admin"),
    MODERATOR ("mod"),
    USER ("user");

    private String value;

    RoleEnum(String value) {
        this.value = value;
    }
}
