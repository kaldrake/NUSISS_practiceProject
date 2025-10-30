package sg.edu.nus.iss.commonQueueApp.dto;

/**
 * Queue Status Response DTO with Builder Pattern
 * 
 * Usage Example:
 * QueueStatusResponse response = QueueStatusResponse.builder()
 *     .queueId(1L)
 *     .queueName("VIP Queue")
 *     .currentNumber(5)
 *     .totalWaiting(10)
 *     .build();
 */
public class QueueStatusResponse {
    private Long queueId;
    private String queueName;
    private Integer currentNumber;
    private Integer nextNumber;
    private Integer totalWaiting;
    private Integer estimatedWaitTime;
    private Boolean isActive;
    private Integer maxCapacity;
    private Integer avgServiceTime;
    private Long servedToday;

    // Default Constructor (required for Jackson serialization)
    public QueueStatusResponse() {}

    // Private constructor for Builder
    private QueueStatusResponse(Builder builder) {
        this.queueId = builder.queueId;
        this.queueName = builder.queueName;
        this.currentNumber = builder.currentNumber;
        this.nextNumber = builder.nextNumber;
        this.totalWaiting = builder.totalWaiting;
        this.estimatedWaitTime = builder.estimatedWaitTime;
        this.isActive = builder.isActive;
        this.maxCapacity = builder.maxCapacity;
        this.avgServiceTime = builder.avgServiceTime;
        this.servedToday = builder.servedToday;
    }

    // Static method to create a Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {
        private Long queueId;
        private String queueName;
        private Integer currentNumber;
        private Integer nextNumber;
        private Integer totalWaiting;
        private Integer estimatedWaitTime;
        private Boolean isActive;
        private Integer maxCapacity;
        private Integer avgServiceTime;
        private Long servedToday;

        private Builder() {}

        public Builder queueId(Long queueId) {
            this.queueId = queueId;
            return this;
        }

        public Builder queueName(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public Builder currentNumber(Integer currentNumber) {
            this.currentNumber = currentNumber;
            return this;
        }

        public Builder nextNumber(Integer nextNumber) {
            this.nextNumber = nextNumber;
            return this;
        }

        public Builder totalWaiting(Integer totalWaiting) {
            this.totalWaiting = totalWaiting;
            return this;
        }

        public Builder estimatedWaitTime(Integer estimatedWaitTime) {
            this.estimatedWaitTime = estimatedWaitTime;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder maxCapacity(Integer maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public Builder avgServiceTime(Integer avgServiceTime) {
            this.avgServiceTime = avgServiceTime;
            return this;
        }

        public Builder servedToday(Long servedToday) {
            this.servedToday = servedToday;
            return this;
        }

        public QueueStatusResponse build() {
            return new QueueStatusResponse(this);
        }
    }

    // Getters and Setters (maintained for backward compatibility)
    public Long getQueueId() { 
        return queueId; 
    }
    
    public void setQueueId(Long queueId) { 
        this.queueId = queueId; 
    }

    public String getQueueName() { 
        return queueName; 
    }
    
    public void setQueueName(String queueName) { 
        this.queueName = queueName; 
    }

    public Integer getCurrentNumber() { 
        return currentNumber; 
    }
    
    public void setCurrentNumber(Integer currentNumber) { 
        this.currentNumber = currentNumber; 
    }

    public Integer getNextNumber() { 
        return nextNumber; 
    }
    
    public void setNextNumber(Integer nextNumber) { 
        this.nextNumber = nextNumber; 
    }

    public Integer getTotalWaiting() { 
        return totalWaiting; 
    }
    
    public void setTotalWaiting(Integer totalWaiting) { 
        this.totalWaiting = totalWaiting; 
    }

    public Integer getEstimatedWaitTime() { 
        return estimatedWaitTime; 
    }
    
    public void setEstimatedWaitTime(Integer estimatedWaitTime) { 
        this.estimatedWaitTime = estimatedWaitTime; 
    }

    public Boolean getIsActive() { 
        return isActive; 
    }
    
    public void setIsActive(Boolean isActive) { 
        this.isActive = isActive; 
    }

    public Integer getMaxCapacity() { 
        return maxCapacity; 
    }
    
    public void setMaxCapacity(Integer maxCapacity) { 
        this.maxCapacity = maxCapacity; 
    }

    public Integer getAvgServiceTime() { 
        return avgServiceTime; 
    }
    
    public void setAvgServiceTime(Integer avgServiceTime) { 
        this.avgServiceTime = avgServiceTime; 
    }

    public Long getServedToday() { 
        return servedToday; 
    }
    
    public void setServedToday(Long servedToday) { 
        this.servedToday = servedToday; 
    }

    @Override
    public String toString() {
        return "QueueStatusResponse{" +
                "queueId=" + queueId +
                ", queueName='" + queueName + '\'' +
                ", currentNumber=" + currentNumber +
                ", nextNumber=" + nextNumber +
                ", totalWaiting=" + totalWaiting +
                ", estimatedWaitTime=" + estimatedWaitTime +
                ", isActive=" + isActive +
                ", maxCapacity=" + maxCapacity +
                ", avgServiceTime=" + avgServiceTime +
                ", servedToday=" + servedToday +
                '}';
    }
}