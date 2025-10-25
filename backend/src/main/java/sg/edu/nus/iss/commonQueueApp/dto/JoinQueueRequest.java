package sg.edu.nus.iss.commonQueueApp.dto;

/**
 * Join Queue Request DTO with Builder Pattern
 * 
 * Usage Example:
 * JoinQueueRequest request = JoinQueueRequest.builder()
 *     .customerId(123L)
 *     .build();
 */
public class JoinQueueRequest {
    
    private Long customerId;

    // Default Constructor (required for Jackson deserialization)
    public JoinQueueRequest() {}

    // Private constructor for Builder
    private JoinQueueRequest(Builder builder) {
        this.customerId = builder.customerId;
    }

    // Static method to create a Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {
        private Long customerId;

        private Builder() {}

        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public JoinQueueRequest build() {
            return new JoinQueueRequest(this);
        }
    }

    // Getters and Setters (maintained for backward compatibility)
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "JoinQueueRequest{" +
                "customerId=" + customerId +
                '}';
    }
}