package sg.edu.nus.iss.commonQueueApp.dto;

import sg.edu.nus.iss.commonQueueApp.entity.QueueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Queue Timing Request DTO with Builder Pattern
 * 
 * Usage Example:
 * QueueTimingRequest request = QueueTimingRequest.builder()
 *     .queueName("VIP Queue")
 *     .description("Priority service queue")
 *     .queueType(QueueType.VIP)
 *     .avgServiceTimeMinutes(15)
 *     .maxCapacity(20)
 *     .colorCode("#FF5733")
 *     .build();
 */
public class QueueTimingRequest {

    @NotBlank(message = "Queue name is required")
    @Size(max = 100, message = "Queue name must not exceed 100 characters")
    private String queueName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private QueueType queueType;

    @Positive(message = "Average service time must be positive")
    private Integer avgServiceTimeMinutes;

    private Integer maxCapacity;

    private String colorCode;

    // Default Constructor (required for Jackson deserialization)
    public QueueTimingRequest() {}

    // Constructor for backward compatibility
    public QueueTimingRequest(String queueName, QueueType queueType, Integer avgServiceTimeMinutes) {
        this.queueName = queueName;
        this.queueType = queueType;
        this.avgServiceTimeMinutes = avgServiceTimeMinutes;
    }

    // Private constructor for Builder
    private QueueTimingRequest(Builder builder) {
        this.queueName = builder.queueName;
        this.description = builder.description;
        this.queueType = builder.queueType;
        this.avgServiceTimeMinutes = builder.avgServiceTimeMinutes;
        this.maxCapacity = builder.maxCapacity;
        this.colorCode = builder.colorCode;
    }

    // Static method to create a Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {
        private String queueName;
        private String description;
        private QueueType queueType;
        private Integer avgServiceTimeMinutes;
        private Integer maxCapacity;
        private String colorCode;

        private Builder() {}

        public Builder queueName(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder queueType(QueueType queueType) {
            this.queueType = queueType;
            return this;
        }

        public Builder avgServiceTimeMinutes(Integer avgServiceTimeMinutes) {
            this.avgServiceTimeMinutes = avgServiceTimeMinutes;
            return this;
        }

        public Builder maxCapacity(Integer maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public Builder colorCode(String colorCode) {
            this.colorCode = colorCode;
            return this;
        }

        public QueueTimingRequest build() {
            return new QueueTimingRequest(this);
        }
    }

    // Getters and Setters (maintained for backward compatibility)
    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QueueType getQueueType() {
        return queueType;
    }

    public void setQueueType(QueueType queueType) {
        this.queueType = queueType;
    }

    public Integer getAvgServiceTimeMinutes() {
        return avgServiceTimeMinutes;
    }

    public void setAvgServiceTimeMinutes(Integer avgServiceTimeMinutes) {
        this.avgServiceTimeMinutes = avgServiceTimeMinutes;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return "QueueTimingRequest{" +
                "queueName='" + queueName + '\'' +
                ", description='" + description + '\'' +
                ", queueType=" + queueType +
                ", avgServiceTimeMinutes=" + avgServiceTimeMinutes +
                ", maxCapacity=" + maxCapacity +
                ", colorCode='" + colorCode + '\'' +
                '}';
    }
}