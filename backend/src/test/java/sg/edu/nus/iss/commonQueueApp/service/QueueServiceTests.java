/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sg.edu.nus.iss.commonQueueApp.dto.QueueTimingRequest;
import sg.edu.nus.iss.commonQueueApp.dto.QueueStatusResponse;
import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
/**
 *
 * @author junwe
 */
public class QueueServiceTests {
    @Mock private QueueRepository queueRepository;
    @Mock private QueueEntryRepository queueEntryRepository;
    @Mock private BusinessRepository businessRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private NotificationService notificationService;

    @InjectMocks private QueueService queueService;

    private Business business;
    private Queue queue;
    private Customer customer;
    private QueueEntry entry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        business = new Business();
        business.setId(1L);
        business.setBusinessName("Test Business");

        queue = new Queue();
        queue.setId(100L);
        queue.setQueueName("Main Queue");
        queue.setBusiness(business);
        queue.setIsActive(true);
        queue.setAvgServiceTimeMinutes(10);
        queue.setMaxCapacity(2);
        queue.setCurrentNumber(0);
        queue.setNextNumber(0);

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setPhone("91234567");
        customer.setNotificationPreference(NotificationPreference.BOTH);

        entry = new QueueEntry(queue, customer, 0);
    }

    // -------------------- createQueue --------------------
    @Test
    void createQueue_success() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setQueueName("VIP Queue");
        request.setAvgServiceTimeMinutes(5);
        request.setQueueType(QueueType.GENERAL);

        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));
        when(queueRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Queue created = queueService.createQueue(1L, request);

        assertEquals("VIP Queue", created.getQueueName());
        assertEquals(business, created.getBusiness());
        assertEquals(QueueType.GENERAL, created.getQueueType());
        verify(queueRepository).save(any());
    }

    @Test
    void createQueue_businessNotFound_throws() {
        when(businessRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> queueService.createQueue(1L, new QueueTimingRequest()));
    }

    // -------------------- updateQueueTiming --------------------
    @Test
    void updateQueueTiming_success() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setQueueName("Updated Queue");
        request.setAvgServiceTimeMinutes(15);

        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(queueRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(queueEntryRepository.findByQueueAndStatusInOrderByQueueNumberAsc(any(), any()))
                .thenReturn(List.of());

        Queue updated = queueService.updateQueueTiming(100L, request);
        assertEquals("Updated Queue", updated.getQueueName());
        assertEquals(15, updated.getAvgServiceTimeMinutes());
    }

    @Test
    void updateQueueTiming_notFound_throws() {
        when(queueRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> queueService.updateQueueTiming(100L, new QueueTimingRequest()));
    }

    // -------------------- deleteQueue --------------------
    @Test
    void deleteQueue_success() {
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findActiveEntriesByQueueId(100L)).thenReturn(List.of(entry));
        when(queueEntryRepository.save(any())).thenReturn(entry);
        when(queueRepository.save(any())).thenReturn(queue);

        queueService.deleteQueue(100L);

        assertFalse(queue.getIsActive());
        verify(notificationService).sendQueueCancellationNotification(entry);
        verify(queueEntryRepository).save(entry);
        verify(queueRepository).save(queue);
    }

    @Test
    void deleteQueue_notFound_throws() {
        when(queueRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> queueService.deleteQueue(100L));
    }

    // -------------------- getQueueStatus --------------------
    @Test
    void getQueueStatus_success() {
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findActiveEntriesByQueueId(100L)).thenReturn(List.of(entry));

        QueueStatusResponse status = queueService.getQueueStatus(100L);

        assertEquals("Main Queue", status.getQueueName());
        assertEquals(1, status.getTotalWaiting());
    }

    // -------------------- joinQueue --------------------
    @Test
    void joinQueue_success() {
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(queueEntryRepository.findByQueueAndCustomerAndStatusIn(any(), any(), any())).thenReturn(Optional.empty());
        when(queueEntryRepository.save(any())).thenReturn(entry);
        when(queueRepository.save(any())).thenReturn(queue);

        QueueEntry joined = queueService.joinQueue(100L, 1L);

        assertEquals(customer, joined.getCustomer());
        verify(notificationService).sendQueueJoinedNotification(joined);
    }

    @Test
    void joinQueue_alreadyInQueue_throws() {
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(queueEntryRepository.findByQueueAndCustomerAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(entry));

        assertThrows(RuntimeException.class, () -> queueService.joinQueue(100L, 1L));
    }

    @Test
    void joinQueue_queueFull_throws() {
        queue.setMaxCapacity(0);
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(queueEntryRepository.findByQueueAndCustomerAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> queueService.joinQueue(100L, 1L));
    }

    // -------------------- callNextCustomer --------------------
    @Test
    void callNextCustomer_success() {
        entry.setStatus(QueueEntryStatus.WAITING);
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findByQueueAndStatusInOrderByQueueNumberAsc(any(), any()))
                .thenReturn(List.of(entry));
        when(queueEntryRepository.save(any())).thenReturn(entry);
        when(queueRepository.save(any())).thenReturn(queue);

        QueueEntry called = queueService.callNextCustomer(100L);
        assertEquals(QueueEntryStatus.CALLED, called.getStatus());
        verify(notificationService).sendTurnReadyNotification(called);
    }

    @Test
    void callNextCustomer_emptyQueue_throws() {
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findByQueueAndStatusInOrderByQueueNumberAsc(any(), any())).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> queueService.callNextCustomer(100L));
    }

    // -------------------- markAsServed --------------------
    @Test
    void markAsServed_success() {
        entry.setQueue(queue);
        entry.setStatus(QueueEntryStatus.CALLED);
        when(queueEntryRepository.findById(1L)).thenReturn(Optional.of(entry));
        when(queueEntryRepository.save(any())).thenReturn(entry);

        QueueEntry served = queueService.markAsServed(1L);
        assertEquals(QueueEntryStatus.SERVED, served.getStatus());
        verify(notificationService).sendFeedbackRequestNotification(served);
    }

    // -------------------- cancelQueueEntry --------------------
    @Test
    void cancelQueueEntry_success() {
        entry.setQueue(queue);
        entry.setStatus(QueueEntryStatus.WAITING);
        when(queueEntryRepository.findById(1L)).thenReturn(Optional.of(entry));
        when(queueEntryRepository.save(any())).thenReturn(entry);

        QueueEntry cancelled = queueService.cancelQueueEntry(1L);
        assertEquals(QueueEntryStatus.CANCELLED, cancelled.getStatus());
    }

    // -------------------- markAsNoShow --------------------
    @Test
    void markAsNoShow_success() {
        entry.setQueue(queue);
        entry.setStatus(QueueEntryStatus.WAITING);
        when(queueEntryRepository.findById(1L)).thenReturn(Optional.of(entry));
        when(queueEntryRepository.save(any())).thenReturn(entry);

        QueueEntry noShow = queueService.markAsNoShow(1L);
        assertEquals(QueueEntryStatus.NO_SHOW, noShow.getStatus());
    }

    // -------------------- getCustomerQueuePosition --------------------
    @Test
    void getCustomerQueuePosition_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findByQueueAndCustomerAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(entry));

        QueueEntry pos = queueService.getCustomerQueuePosition(1L, 100L);
        assertEquals(entry, pos);
    }

    // -------------------- getActiveQueuesByBusinessId --------------------
    @Test
    void getActiveQueuesByBusinessId_success() {
        when(queueRepository.findByBusinessIdAndIsActiveTrue(1L)).thenReturn(List.of(queue));

        List<Queue> activeQueues = queueService.getActiveQueuesByBusinessId(1L);
        assertEquals(1, activeQueues.size());
    }

    // -------------------- getQueueEntries --------------------
    @Test
    void getQueueEntries_success() {
        when(queueEntryRepository.findActiveEntriesByQueueId(100L)).thenReturn(List.of(entry));

        List<QueueEntry> entries = queueService.getQueueEntries(100L);
        assertEquals(1, entries.size());
    }

    // -------------------- resetQueue --------------------
    @Test
    void resetQueue_success() {
        when(queueRepository.findById(100L)).thenReturn(Optional.of(queue));
        when(queueRepository.save(any())).thenReturn(queue);

        queueService.resetQueue(100L);
        assertEquals(1, queue.getNextNumber());
        assertEquals(0, queue.getCurrentNumber());
    }

    // -------------------- getQueueStatistics --------------------
    @Test
    void getQueueStatistics_success() {
        entry.setStatus(QueueEntryStatus.SERVED);
        entry.setJoinedAt(LocalDateTime.now().minusMinutes(5));
        entry.setCalledAt(LocalDateTime.now());
        
        when(queueEntryRepository.findEntriesByBusinessIdAndDateRange(eq(1L), any(), any()))
                .thenReturn(List.of(entry));

        QueueService.QueueStatistics stats = queueService.getQueueStatistics(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        assertEquals(1, stats.getTotalCustomers());
        assertEquals(1, stats.getServedCustomers());
        assertEquals(5, (int) stats.getAverageWaitTimeMinutes());
    }
}
