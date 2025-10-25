package sg.edu.nus.iss.commonQueueApp.dto;

import sg.edu.nus.iss.commonQueueApp.entity.BusinessType;
import jakarta.validation.constraints.*;
import java.time.LocalTime;

/**
 * Business Registration Request DTO with Builder Pattern
 * 
 * Usage Example:
 * BusinessRegistrationRequest request = BusinessRegistrationRequest.builder()
 *     .businessName("Joe's Coffee Shop")
 *     .email("joe@coffeeshop.com")
 *     .password("securePass123")
 *     .phone("+65 9876 5432")
 *     .address("123 Main Street")
 *     .description("Artisanal coffee and pastries")
 *     .businessType(BusinessType.RESTAURANT)
 *     .openingTime(LocalTime.of(8, 0))
 *     .closingTime(LocalTime.of(22, 0))
 *     .build();
 */
public class BusinessRegistrationRequest {
    
    @NotBlank(message = "Business name is required")
    @Size(max = 100, message = "Business name must not exceed 100 characters")
    private String businessName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private BusinessType businessType;
    private LocalTime openingTime;
    private LocalTime closingTime;

    // Default Constructor (required for Jackson deserialization)
    public BusinessRegistrationRequest() {}

    // Private constructor for Builder
    private BusinessRegistrationRequest(Builder builder) {
        this.businessName = builder.businessName;
        this.email = builder.email;
        this.password = builder.password;
        this.phone = builder.phone;
        this.address = builder.address;
        this.description = builder.description;
        this.businessType = builder.businessType;
        this.openingTime = builder.openingTime;
        this.closingTime = builder.closingTime;
    }

    // Static method to create a Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {
        private String businessName;
        private String email;
        private String password;
        private String phone;
        private String address;
        private String description;
        private BusinessType businessType;
        private LocalTime openingTime;
        private LocalTime closingTime;

        private Builder() {}

        public Builder businessName(String businessName) {
            this.businessName = businessName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder businessType(BusinessType businessType) {
            this.businessType = businessType;
            return this;
        }

        public Builder openingTime(LocalTime openingTime) {
            this.openingTime = openingTime;
            return this;
        }

        public Builder closingTime(LocalTime closingTime) {
            this.closingTime = closingTime;
            return this;
        }

        public BusinessRegistrationRequest build() {
            return new BusinessRegistrationRequest(this);
        }
    }

    // Getters and Setters (maintained for backward compatibility)
    public String getBusinessName() { 
        return businessName; 
    }
    
    public void setBusinessName(String businessName) { 
        this.businessName = businessName; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getPhone() { 
        return phone; 
    }
    
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public String getAddress() { 
        return address; 
    }
    
    public void setAddress(String address) { 
        this.address = address; 
    }

    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }

    public BusinessType getBusinessType() { 
        return businessType; 
    }
    
    public void setBusinessType(BusinessType businessType) { 
        this.businessType = businessType; 
    }

    public LocalTime getOpeningTime() { 
        return openingTime; 
    }
    
    public void setOpeningTime(LocalTime openingTime) { 
        this.openingTime = openingTime; 
    }

    public LocalTime getClosingTime() { 
        return closingTime; 
    }
    
    public void setClosingTime(LocalTime closingTime) { 
        this.closingTime = closingTime; 
    }

    @Override
    public String toString() {
        return "BusinessRegistrationRequest{" +
                "businessName='" + businessName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", businessType=" + businessType +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }
}