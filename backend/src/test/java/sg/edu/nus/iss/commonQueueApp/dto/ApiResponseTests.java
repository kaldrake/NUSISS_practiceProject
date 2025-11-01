/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author junwe
 */
public class ApiResponseTests {
    @Test
    void testConstructorWithoutData() {
        ApiResponse response = new ApiResponse(true, "Success");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isNull();
    }

    @Test
    void testConstructorWithData() {
        String payload = "payload";
        ApiResponse response = new ApiResponse(true, "Success", payload);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isEqualTo(payload);
    }

    @Test
    void testSettersAndGetters() {
        ApiResponse response = new ApiResponse(false, "Initial");

        response.setSuccess(true);
        response.setMessage("Updated");
        response.setData(123);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Updated");
        assertThat(response.getData()).isEqualTo(123);
    }
}
