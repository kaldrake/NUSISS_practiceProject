/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.NotificationRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 *
 * @author junwe
 */
public class SmsNotificationStrategyTests {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private SmsNotificationStrategy smsStrategy;

    private Customer customer;
    private QueueEntry queueEntry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setPhone("+6512345678");
        customer.setName("Alice");

        queueEntry = new QueueEntry();
        queueEntry.setId(1L);
    }

    @Test
    void send_ShouldCreateAndMarkNotificationSent() {
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Notification notification = smsStrategy.send(
                customer,
                "Queue Update",
                "Your turn is coming soon!",
                NotificationType.TURN_APPROACHING,
                queueEntry
        );

        assertThat(notification).isNotNull();
        assertThat(notification.getChannel()).isEqualTo(NotificationChannel.SMS);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);

        verify(notificationRepository, times(2)).save(any(Notification.class)); // once before sending, once after marking as sent
    }

    @Test
    void send_ShouldHandleExceptionAndMarkAsFailed() throws Exception {
        SmsNotificationStrategy spyStrategy = spy(smsStrategy);

        // Force exception inside the try block
        doThrow(new RuntimeException("SMS failed")).when(spyStrategy).sendSms(any(Notification.class));

        when(notificationRepository.save(any(Notification.class)))
            .thenAnswer(i -> i.getArgument(0));

        Notification notification = spyStrategy.send(customer, "Title", "Message",
                NotificationType.REMINDER, queueEntry);

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
        assertThat(notification.getErrorMessage()).contains("SMS failed");
    }

    @Test
    void getChannel_ShouldReturnSms() {
        assertThat(smsStrategy.getChannel()).isEqualTo(NotificationChannel.SMS);
    }

    @Test
    void canSend_ShouldReturnTrueIfCustomerAllowsSms() {
        Customer c = mock(Customer.class);
        when(c.canReceiveSmsNotifications()).thenReturn(true);

        assertThat(smsStrategy.canSend(c)).isTrue();
        verify(c, times(1)).canReceiveSmsNotifications();
    }

    @Test
    void canSend_ShouldReturnFalseIfCustomerDisallowsSms() {
        Customer c = mock(Customer.class);
        when(c.canReceiveSmsNotifications()).thenReturn(false);

        assertThat(smsStrategy.canSend(c)).isFalse();
        verify(c, times(1)).canReceiveSmsNotifications();
    }

    @Test
    void resend_ShouldMarkAsSentAndSave() throws Exception {
        Notification notification = new Notification(
                customer,
                NotificationType.REMINDER,
                NotificationChannel.SMS,
                "Resend Test",
                "Resend Message",
                customer.getPhone()
        );

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        smsStrategy.resend(notification);

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        verify(notificationRepository, times(1)).save(notification);
    }
}
