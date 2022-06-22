package testing.community.automation.practice.app.shared.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }
}
