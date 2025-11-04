package socialmediaspringboot.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * This class is for returning the normalized data when the api endpoints are called
 *
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    @Builder.Default
    private int code = 1; //successful pais get a code 1
    private String message;
    private T result;
}
