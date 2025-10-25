package sg.edu.nus.iss.commonQueueApp.dto;

import jakarta.validation.constraints.*;

/**
 * Customer Registration Request DTO with Builder Pattern
 * 
 * Usage Example:
 * CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
 *     .name("John Doe")
 *     .email("john.doe@example.com")
 *     .phone("+65 1234 5678")
 *     .languagePreference("en")
 *     .build();
 */
public class CustomerRegistrationRequest {
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Email(message = "Please provide a valid email")
    private String email;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    private String languagePreference = "en";

    // Default Constructor (required for Jackson deserialization)
    public CustomerRegistrationRequest() {}

    // Private constructor for Builder
    private CustomerRegistrationRequest(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.languagePreference = builder.languagePreference != null ? builder.languagePreference : "en";
    }

    // Static method to create a Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {
        private String name;
        private String email;
        private String phone;
        private String languagePreference;

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder languagePreference(String languagePreference) {
            this.languagePreference = languagePreference;
            return this;
        }

        public CustomerRegistrationRequest build() {
            return new CustomerRegistrationRequest(this);
        }
    }

    // Getters and Setters (maintained for backward compatibility)
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPhone() { 
        return phone; 
    }
    
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public String getLanguagePreference() { 
        return languagePreference; 
    }
    
    public void setLanguagePreference(String languagePreference) { 
        this.languagePreference = languagePreference; 
    }

    @Override
    public String toString() {
        return "CustomerRegistrationRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", languagePreference='" + languagePreference + '\'' +
                '}';
    }
}