/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.NotificationChannel;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 *
 * @author junwe
 */
public class NotificationStrategyFactoryTests {
    private NotificationStrategy emailStrategy;
    private NotificationStrategy smsStrategy;
    private NotificationStrategyFactory factory;

    @BeforeEach
    void setUp() {
        emailStrategy = mock(NotificationStrategy.class);
        smsStrategy = mock(NotificationStrategy.class);

        when(emailStrategy.getChannel()).thenReturn(NotificationChannel.EMAIL);
        when(smsStrategy.getChannel()).thenReturn(NotificationChannel.SMS);

        factory = new NotificationStrategyFactory(List.of(emailStrategy, smsStrategy));
    }

    @Test
    void constructor_ShouldRegisterAllStrategies() {
        Map<NotificationChannel, NotificationStrategy> strategies = factory.getAllStrategies();
        assertThat(strategies).hasSize(2)
                .containsEntry(NotificationChannel.EMAIL, emailStrategy)
                .containsEntry(NotificationChannel.SMS, smsStrategy);
    }

    @Test
    void getStrategy_ShouldReturnCorrectStrategy() {
        NotificationStrategy result = factory.getStrategy(NotificationChannel.EMAIL);
        assertThat(result).isEqualTo(emailStrategy);

        result = factory.getStrategy(NotificationChannel.SMS);
        assertThat(result).isEqualTo(smsStrategy);
    }

    @Test
    void getStrategy_ShouldThrowExceptionForUnknownChannel() {
        assertThatThrownBy(() -> factory.getStrategy(NotificationChannel.PUSH))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No strategy found for channel");
    }

    @Test
    void getAllStrategies_ShouldReturnCopyOfMap() {
        Map<NotificationChannel, NotificationStrategy> strategies1 = factory.getAllStrategies();
        Map<NotificationChannel, NotificationStrategy> strategies2 = factory.getAllStrategies();

        assertThat(strategies1).isNotSameAs(strategies2); // ensure copy, not reference
        assertThat(strategies1).containsEntry(NotificationChannel.EMAIL, emailStrategy);
        assertThat(strategies1).containsEntry(NotificationChannel.SMS, smsStrategy);
    }

    @Test
    void hasStrategy_ShouldReturnTrueIfExists() {
        assertThat(factory.hasStrategy(NotificationChannel.EMAIL)).isTrue();
        assertThat(factory.hasStrategy(NotificationChannel.SMS)).isTrue();
    }

    @Test
    void hasStrategy_ShouldReturnFalseIfNotExists() {
        assertThat(factory.hasStrategy(NotificationChannel.PUSH)).isFalse();
    }
}
