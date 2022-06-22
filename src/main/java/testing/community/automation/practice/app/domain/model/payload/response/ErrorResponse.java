package testing.community.automation.practice.app.domain.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String type;
    private String message;
}
