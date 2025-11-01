/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.NotificationRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 *
 * @author junwe
 */
public class EmailNotificationStrategyTests {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationStrategy emailNotificationStrategy;

    private Customer customer;
    private QueueEntry queueEntry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setName("Alice");

        queueEntry = new QueueEntry();
        queueEntry.setId(10L);
    }

    @Test
    void send_ShouldCreateAndSendNotificationSuccessfully() {
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // echo behavior

        Notification result = emailNotificationStrategy.send(
                customer,
                "Queue Update",
                "Your turn is coming soon!",
                NotificationType.TURN_APPROACHING,
                queueEntry
        );

        assertThat(result).isNotNull();
        assertThat(result.getChannel()).isEqualTo(NotificationChannel.EMAIL);
        assertThat(result.getStatus()).isEqualTo(NotificationStatus.SENT);
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void send_ShouldHandleExceptionAndMarkAsFailed() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Notification result = emailNotificationStrategy.send(
                customer,
                "Test Failure",
                "This should fail",
                NotificationType.REMINDER,
                queueEntry
        );

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(NotificationStatus.FAILED);
        assertThat(result.getErrorMessage()).contains("SMTP error");
        verify(notificationRepository, atLeastOnce()).save(any(Notification.class));
    }

    @Test
    void getChannel_ShouldReturnEmail() {
        assertThat(emailNotificationStrategy.getChannel())
                .isEqualTo(NotificationChannel.EMAIL);
    }

    @Test
    void canSend_ShouldReturnTrueIfCustomerAllowsEmails() {
        // Mock customer permission
        Customer c = mock(Customer.class);
        when(c.canReceiveEmailNotifications()).thenReturn(true);

        assertThat(emailNotificationStrategy.canSend(c)).isTrue();
        verify(c, times(1)).canReceiveEmailNotifications();
    }

    @Test
    void canSend_ShouldReturnFalseIfCustomerDisallowsEmails() {
        Customer c = mock(Customer.class);
        when(c.canReceiveEmailNotifications()).thenReturn(false);

        assertThat(emailNotificationStrategy.canSend(c)).isFalse();
        verify(c, times(1)).canReceiveEmailNotifications();
    }

    @Test
    void resend_ShouldSendEmailAndMarkAsSent() throws Exception {
        Notification notification = new Notification(
                customer,
                NotificationType.REMINDER,
                NotificationChannel.EMAIL,
                "Resend Test",
                "Resend Message",
                customer.getEmail()
        );

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        emailNotificationStrategy.resend(notification);

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(notificationRepository, times(1)).save(notification);
    }
}
