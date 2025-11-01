/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.NotificationRepository;
import sg.edu.nus.iss.commonQueueApp.service.notification.EmailNotificationStrategy;
import sg.edu.nus.iss.commonQueueApp.service.notification.NotificationStrategy;
import sg.edu.nus.iss.commonQueueApp.service.notification.NotificationStrategyFactory;
import sg.edu.nus.iss.commonQueueApp.service.notification.SmsNotificationStrategy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 *
 * @author junwe
 */
public class NotificationServiceTests {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationStrategyFactory strategyFactory;

    @Mock
    private EmailNotificationStrategy emailStrategy;

    @Mock
    private SmsNotificationStrategy smsStrategy;

    @InjectMocks
    private NotificationService notificationService;

    private QueueEntry queueEntry;
    private Customer customer;
    private Queue queue;
    private Business business;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        business = new Business();
        business.setBusinessName("Cafe Java");

        queue = new Queue();
        queue.setQueueName("Main Queue");
        queue.setBusiness(business);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");

        queueEntry = new QueueEntry();
        queueEntry.setCustomer(customer);
        queueEntry.setQueue(queue);
        queueEntry.setQueueNumber(5);
        queueEntry.setEstimatedWaitTimeMinutes(10);

        // Mock strategy factory with 2 strategies
        Map<NotificationChannel, NotificationStrategy> strategies = new HashMap<>();
        strategies.put(NotificationChannel.EMAIL, emailStrategy);
        strategies.put(NotificationChannel.SMS, smsStrategy);

        when(strategyFactory.getAllStrategies()).thenReturn(strategies);
        when(emailStrategy.canSend(any())).thenReturn(true);
        when(smsStrategy.canSend(any())).thenReturn(true);
    }

    @Test
    void testSendQueueJoinedNotification_ShouldDelegateToStrategies() {
        notificationService.sendQueueJoinedNotification(queueEntry);

        verify(emailStrategy, times(1)).send(
                eq(customer), anyString(), contains("joined the queue"), eq(NotificationType.QUEUE_JOINED), eq(queueEntry)
        );

        verify(smsStrategy, times(1)).send(
                eq(customer), anyString(), contains("joined the queue"), eq(NotificationType.QUEUE_JOINED), eq(queueEntry)
        );
    }

    @Test
    void testProcessPendingNotifications_ShouldResendEmailAndSms() throws Exception {
        Notification emailNotif = mock(Notification.class);
        Notification smsNotif = mock(Notification.class);

        when(emailNotif.getChannel()).thenReturn(NotificationChannel.EMAIL);
        when(smsNotif.getChannel()).thenReturn(NotificationChannel.SMS);
        when(notificationRepository.findByStatusOrderByCreatedAtAsc(NotificationStatus.PENDING))
                .thenReturn(List.of(emailNotif, smsNotif));

        when(strategyFactory.getStrategy(NotificationChannel.EMAIL)).thenReturn(emailStrategy);
        when(strategyFactory.getStrategy(NotificationChannel.SMS)).thenReturn(smsStrategy);

        notificationService.processPendingNotifications();

        verify(emailStrategy, times(1)).resend(emailNotif);
        verify(smsStrategy, times(1)).resend(smsNotif);
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testProcessPendingNotifications_WhenStrategyThrows_ShouldMarkFailed() {
        Notification notif = mock(Notification.class);
        when(notif.getChannel()).thenReturn(NotificationChannel.EMAIL);
        when(notificationRepository.findByStatusOrderByCreatedAtAsc(NotificationStatus.PENDING))
                .thenReturn(List.of(notif));
        when(strategyFactory.getStrategy(NotificationChannel.EMAIL)).thenThrow(new RuntimeException("boom"));

        notificationService.processPendingNotifications();

        verify(notif, times(1)).markAsFailed(anyString());
        verify(notificationRepository, times(1)).save(notif);
    }

    @Test
    void testRetryFailedNotifications_ShouldResetStatusAndIncrementRetryCount() {
        Notification notif = mock(Notification.class);
        when(notificationRepository.findFailedNotificationsForRetry()).thenReturn(List.of(notif));

        notificationService.retryFailedNotifications();

        verify(notif).incrementRetryCount();
        verify(notif).setStatus(NotificationStatus.PENDING);
        verify(notificationRepository).save(notif);
    }

    @Test
    void testMarkNotificationAsRead_ShouldUpdateAndSave() {
        Notification notif = mock(Notification.class);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notif));

        notificationService.markNotificationAsRead(1L);

        verify(notif).markAsRead();
        verify(notificationRepository).save(notif);
    }

    @Test
    void testGetUnreadNotificationsCount_ShouldReturnCount() {
        when(notificationRepository.countUnreadNotificationsByCustomerId(1L)).thenReturn(3L);

        Long result = notificationService.getUnreadNotificationsCount(1L);

        assertThat(result).isEqualTo(3L);
        verify(notificationRepository).countUnreadNotificationsByCustomerId(1L);
    }

    @Test
    void testCleanupOldNotifications_ShouldCancelAndSave() {
        Notification oldNotif = new Notification();
        oldNotif.setStatus(NotificationStatus.PENDING);

        when(notificationRepository.findPendingNotificationsOlderThan(any(LocalDateTime.class)))
                .thenReturn(List.of(oldNotif));

        notificationService.cleanupOldNotifications();

        assertThat(oldNotif.getStatus()).isEqualTo(NotificationStatus.CANCELLED);
        verify(notificationRepository).saveAll(anyList());
    }

    @Test
    void testGetCustomerNotifications_ShouldDelegateToRepository() {
        List<Notification> mockList = List.of(new Notification());
        when(notificationRepository.findByCustomerOrderByCreatedAtDesc(any(Customer.class)))
                .thenReturn(mockList);

        List<Notification> result = notificationService.getCustomerNotifications(5L);

        assertThat(result).hasSize(1);
        verify(notificationRepository).findByCustomerOrderByCreatedAtDesc(any(Customer.class));
    }
    
    @Test
    void testSendTurnApproachingNotification_ShouldDelegateAndMarkSent() {
        notificationService.sendTurnApproachingNotification(queueEntry);

        verify(emailStrategy).send(
                eq(customer),
                eq("Your Turn is Approaching"),
                contains("approaching"),
                eq(NotificationType.TURN_APPROACHING),
                eq(queueEntry)
        );
        verify(smsStrategy).send(any(), anyString(), anyString(), eq(NotificationType.TURN_APPROACHING), any());
        assertThat(queueEntry.getNotificationSent()).isTrue();
    }

    @Test
    void testSendTurnReadyNotification_ShouldDelegateToStrategies() {
        notificationService.sendTurnReadyNotification(queueEntry);

        verify(emailStrategy).send(
                eq(customer),
                eq("Your Turn is Ready!"),
                contains("Please proceed"),
                eq(NotificationType.TURN_READY),
                eq(queueEntry)
        );
        verify(smsStrategy).send(any(), anyString(), anyString(), eq(NotificationType.TURN_READY), any());
    }

    @Test
    void testSendQueueCancellationNotification_ShouldDelegateToStrategies() {
        notificationService.sendQueueCancellationNotification(queueEntry);

        verify(emailStrategy).send(
                eq(customer),
                eq("Queue Cancelled"),
                contains("cancelled"),
                eq(NotificationType.QUEUE_CANCELLED),
                eq(queueEntry)
        );
        verify(smsStrategy).send(any(), anyString(), anyString(), eq(NotificationType.QUEUE_CANCELLED), any());
    }

    @Test
    void testSendFeedbackRequestNotification_ShouldDelegateToStrategies() {
        notificationService.sendFeedbackRequestNotification(queueEntry);

        verify(emailStrategy).send(
                eq(customer),
                eq("How was your experience?"),
                contains("rate your experience"),
                eq(NotificationType.FEEDBACK_REQUEST),
                eq(queueEntry)
        );
        verify(smsStrategy).send(any(), anyString(), anyString(), eq(NotificationType.FEEDBACK_REQUEST), any());
    }

    @Test
    void testSendReminderNotification_ShouldDelegateAndMarkReminderSent() {
        notificationService.sendReminderNotification(queueEntry);

        verify(emailStrategy).send(
                eq(customer),
                eq("Reminder: Your Turn is Coming Soon"),
                contains("5 minutes"),
                eq(NotificationType.REMINDER),
                eq(queueEntry)
        );
        verify(smsStrategy).send(any(), anyString(), anyString(), eq(NotificationType.REMINDER), any());
        assertThat(queueEntry.getReminderSent()).isTrue();
    }

    @Test
    void testSendQueueDelayNotification_ShouldDelegateToStrategies() {
        notificationService.sendQueueDelayNotification(queueEntry, 8);

        verify(emailStrategy).send(
                eq(customer),
                eq("Queue Delay Notice"),
                contains("increased by 8 minutes"),
                eq(NotificationType.QUEUE_DELAYED),
                eq(queueEntry)
        );
        verify(smsStrategy).send(any(), anyString(), anyString(), eq(NotificationType.QUEUE_DELAYED), any());
    }
}
