package sg.edu.nus.iss.commonQueueApp.service.factory;

import sg.edu.nus.iss.commonQueueApp.entity.*;
import org.springframework.stereotype.Component;

/**
 * Factory for creating QueueEntry instances
 * 
 * Centralizes QueueEntry creation logic and makes it easier to:
 * - Add validation rules
 * - Calculate initial values consistently
 * - Modify creation logic in one place
 * 
 * Phase 3: Factory Pattern - No new features, just cleaner code
 */
@Component
public class QueueEntryFactory {
    
    /**
     * Create a standard QueueEntry
     * 
     * @param queue The queue to join
     * @param customer The customer joining
     * @param queueNumber The assigned queue number
     * @return A new QueueEntry instance ready to save
     */
    public QueueEntry createEntry(Queue queue, Customer customer, Integer queueNumber) {
        validateInputs(queue, customer, queueNumber);
        
        QueueEntry entry = new QueueEntry(queue, customer, queueNumber);
        entry.setEstimatedWaitTimeMinutes(queue.getEstimatedWaitTimeMinutes());
        
        return entry;
    }
    
    /**
     * Validate inputs before creating entry
     */
    private void validateInputs(Queue queue, Customer customer, Integer queueNumber) {
        if (queue == null) {
            throw new IllegalArgumentException("Queue cannot be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (queueNumber == null || queueNumber <= 0) {
            throw new IllegalArgumentException("Queue number must be positive");
        }
    }
}