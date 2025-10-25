package sg.edu.nus.iss.commonQueueApp.service.notification;

import sg.edu.nus.iss.commonQueueApp.entity.NotificationChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory for managing notification strategies
 * Automatically discovers all available strategies and routes requests to appropriate ones
 */
@Component
public class NotificationStrategyFactory {
    
    private final Map<NotificationChannel, NotificationStrategy> strategies;
    
    @Autowired
    public NotificationStrategyFactory(List<NotificationStrategy> strategyList) {
        this.strategies = new HashMap<>();
        
        // Auto-register all strategies
        for (NotificationStrategy strategy : strategyList) {
            strategies.put(strategy.getChannel(), strategy);
        }
    }
    
    /**
     * Get strategy for specific channel
     * 
     * @param channel The notification channel
     * @return The strategy for that channel
     * @throws IllegalArgumentException if no strategy exists for the channel
     */
    public NotificationStrategy getStrategy(NotificationChannel channel) {
        NotificationStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for channel: " + channel);
        }
        return strategy;
    }
    
    /**
     * Get all available strategies
     * 
     * @return Map of all registered strategies
     */
    public Map<NotificationChannel, NotificationStrategy> getAllStrategies() {
        return new HashMap<>(strategies);
    }
    
    /**
     * Check if a strategy exists for the given channel
     * 
     * @param channel The notification channel
     * @return true if strategy exists, false otherwise
     */
    public boolean hasStrategy(NotificationChannel channel) {
        return strategies.containsKey(channel);
    }
}