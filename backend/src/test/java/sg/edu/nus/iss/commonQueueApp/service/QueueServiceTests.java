/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.*;
import sg.edu.nus.iss.commonQueueApp.dto.QueueTimingRequest;
import sg.edu.nus.iss.commonQueueApp.service.factory.QueueEntryFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import sg.edu.nus.iss.commonQueueApp.dto.QueueStatusResponse;
/**
 *
 * @author junwe
 */
public class QueueServiceTests {
    @Mock
    private QueueRepository queueRepository;

    @Mock
    private QueueEntryRepository queueEntryRepository;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private QueueEntryFactory queueEntryFactory;

    @InjectMocks
    private QueueService queueService;

    private Business business;
    private Queue queue;
    private Customer customer;
    private QueueEntry entry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        business = new Business();
        business.setId(1L);
        business.setBusinessName("Test Biz");

        queue = new Queue();
        queue.setId(10L);
        queue.setBusiness(business);
        queue.setQueueName("Main Queue");
        queue.setIsActive(true);
        queue.setAvgServiceTimeMinutes(5);
        queue.setMaxCapacity(10);
        queue.setCurrentNumber(1);
        queue.setNextNumber(2);

        customer = new Customer();
        customer.setId(100L);
        customer.setName("Alice");

        entry = new QueueEntry();
        entry.setId(1000L);
        entry.setQueue(queue);
        entry.setCustomer(customer);
        entry.setQueueNumber(5);
        entry.setStatus(QueueEntryStatus.WAITING);
    }

    // ---------- CREATE / UPDATE / DELETE ----------

    @Test
    void testCreateQueue_Success() {
        QueueTimingRequest req = new QueueTimingRequest();
        req.setQueueName("Q1");
        req.setDescription("desc");
        req.setQueueType(QueueType.CASHIER);
        req.setAvgServiceTimeMinutes(5);
        req.setMaxCapacity(20);
        req.setColorCode("#FFF");

        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));
        when(queueRepository.save(any())).thenReturn(queue);

        Queue created = queueService.createQueue(1L, req);

        assertEquals("Main Queue", created.getQueueName());
        verify(queueRepository).save(any());
    }

    @Test
    void testUpdateQueueTiming_Success() {
        QueueTimingRequest req = new QueueTimingRequest();
        req.setQueueName("Updated Q");
        req.setDescription("new desc");
        req.setQueueType(QueueType.CASHIER);
        req.setAvgServiceTimeMinutes(10);
        req.setMaxCapacity(15);

        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(queueRepository.save(any())).thenReturn(queue);
        when(queueEntryRepository.findByQueueAndStatusInOrderByQueueNumberAsc(any(), any()))
                .thenReturn(List.of(entry));

        Queue updated = queueService.updateQueueTiming(10L, req);

        assertEquals("Updated Q", updated.getQueueName());
        verify(queueRepository).save(queue);
        verify(queueEntryRepository).saveAll(anyList());
    }

    @Test
    void testDeleteQueue_CancelsEntriesAndSendsNotification() {
        QueueEntry e1 = new QueueEntry();
        e1.setQueue(queue);
        e1.setCustomer(customer);
        e1.setStatus(QueueEntryStatus.WAITING);

        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findActiveEntriesByQueueId(10L)).thenReturn(List.of(e1));

        queueService.deleteQueue(10L);

        assertFalse(queue.getIsActive());
        verify(notificationService).sendQueueCancellationNotification(e1);
        verify(queueRepository).save(queue);
    }

    // ---------- JOIN / CALL / SERVE ----------

    @Test
    void testJoinQueue_Success() {
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(customerRepository.findById(100L)).thenReturn(Optional.of(customer));
        when(queueEntryRepository.findByQueueAndCustomerAndStatusIn(eq(queue), eq(customer), anyList()))
                .thenReturn(Optional.empty());
        when(queueEntryFactory.createEntry(eq(queue), eq(customer), anyInt())).thenReturn(entry);
        when(queueRepository.save(queue)).thenReturn(queue);
        when(queueEntryRepository.save(entry)).thenReturn(entry);

        QueueEntry result = queueService.joinQueue(10L, 100L);

        assertThat(result).isEqualTo(entry);
        verify(notificationService).sendQueueJoinedNotification(entry);
    }

    @Test
    void testCallNextCustomer_Success() {
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findByQueueAndStatusInOrderByQueueNumberAsc(eq(queue), anyList()))
                .thenReturn(List.of(entry));
        when(queueEntryRepository.save(entry)).thenReturn(entry);

        QueueEntry called = queueService.callNextCustomer(10L);

        assertEquals(QueueEntryStatus.CALLED, called.getStatus());
        verify(notificationService).sendTurnReadyNotification(entry);
    }

    @Test
    void testMarkAsServed_Success() {
        when(queueEntryRepository.findById(1000L)).thenReturn(Optional.of(entry));
        when(queueEntryRepository.save(entry)).thenReturn(entry);

        QueueEntry served = queueService.markAsServed(1000L);

        assertEquals(QueueEntryStatus.SERVED, served.getStatus());
        verify(notificationService).sendFeedbackRequestNotification(entry);
    }

    // ---------- CANCEL / NO-SHOW ----------

    @Test
    void testCancelQueueEntry_Success() {
        when(queueEntryRepository.findById(1000L)).thenReturn(Optional.of(entry));
        when(queueEntryRepository.save(entry)).thenReturn(entry);

        QueueEntry cancelled = queueService.cancelQueueEntry(1000L);

        assertEquals(QueueEntryStatus.CANCELLED, cancelled.getStatus());
        verify(queueEntryRepository).save(entry);
    }

    @Test
    void testMarkAsNoShow_Success() {
        when(queueEntryRepository.findById(1000L)).thenReturn(Optional.of(entry));
        when(queueEntryRepository.save(entry)).thenReturn(entry);

        QueueEntry noShow = queueService.markAsNoShow(1000L);

        assertEquals(QueueEntryStatus.NO_SHOW, noShow.getStatus());
    }

    // ---------- STATUS / GETTERS ----------

    @Test
    void testGetCustomerQueuePosition_Success() {
        when(customerRepository.findById(100L)).thenReturn(Optional.of(customer));
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findByQueueAndCustomerAndStatusIn(eq(queue), eq(customer), anyList()))
                .thenReturn(Optional.of(entry));

        QueueEntry result = queueService.getCustomerQueuePosition(100L, 10L);
        assertThat(result).isEqualTo(entry);
    }

    @Test
    void testGetActiveQueuesByBusinessId() {
        when(queueRepository.findByBusinessIdAndIsActiveTrue(1L)).thenReturn(List.of(queue));

        List<Queue> result = queueService.getActiveQueuesByBusinessId(1L);

        assertThat(result).contains(queue);
    }

    @Test
    void testGetQueueEntries() {
        when(queueEntryRepository.findActiveEntriesByQueueId(10L)).thenReturn(List.of(entry));

        List<QueueEntry> result = queueService.getQueueEntries(10L);

        assertThat(result).contains(entry);
    }

    @Test
    void testGetQueueStatus() {
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findActiveEntriesByQueueId(10L)).thenReturn(List.of(entry));

        QueueStatusResponse response = queueService.getQueueStatus(10L);

        assertEquals(queue.getQueueName(), response.getQueueName());
        assertTrue(response.getIsActive());
    }

    // ---------- RESET / STATS ----------

    @Test
    void testResetQueue_CallsSave() {
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));

        queueService.resetQueue(10L);

        verify(queueRepository).save(queue);
    }

    @Test
    void testGetQueueStatistics_ComputesCorrectly() {
        QueueEntry e1 = new QueueEntry();
        e1.setStatus(QueueEntryStatus.SERVED);
        e1.setJoinedAt(LocalDateTime.now().minusMinutes(5));
        e1.setCalledAt(LocalDateTime.now());

        QueueEntry e2 = new QueueEntry();
        e2.setStatus(QueueEntryStatus.CANCELLED);

        QueueEntry e3 = new QueueEntry();
        e3.setStatus(QueueEntryStatus.NO_SHOW);

        when(queueEntryRepository.findEntriesByBusinessIdAndDateRange(anyLong(), any(), any()))
                .thenReturn(List.of(e1, e2, e3));

        QueueService.QueueStatistics stats =
                queueService.getQueueStatistics(1L, LocalDateTime.now(), LocalDateTime.now());

        assertEquals(3, stats.getTotalCustomers());
        assertEquals(1, stats.getServedCustomers());
        assertEquals(1, stats.getCancelledCustomers());
        assertEquals(1, stats.getNoShowCustomers());
        assertEquals(5.0, stats.getAverageWaitTimeMinutes());
    }

    // ---------- NEGATIVE CASES ----------

    @Test
    void testJoinQueue_InactiveQueue_Throws() {
        queue.setIsActive(false);
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(customerRepository.findById(100L)).thenReturn(Optional.of(customer));

        assertThrows(RuntimeException.class, () -> queueService.joinQueue(10L, 100L));
    }

    @Test
    void testJoinQueue_AlreadyInQueue_Throws() {
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(customerRepository.findById(100L)).thenReturn(Optional.of(customer));
        when(queueEntryRepository.findByQueueAndCustomerAndStatusIn(eq(queue), eq(customer), anyList()))
                .thenReturn(Optional.of(entry));

        assertThrows(RuntimeException.class, () -> queueService.joinQueue(10L, 100L));
    }

    @Test
    void testCallNextCustomer_NoWaiting_Throws() {
        when(queueRepository.findById(10L)).thenReturn(Optional.of(queue));
        when(queueEntryRepository.findByQueueAndStatusInOrderByQueueNumberAsc(eq(queue), anyList()))
                .thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> queueService.callNextCustomer(10L));
    }

    @Test
    void testCancelQueueEntry_NotActive_Throws() {
        entry.markAsCancelled();
        when(queueEntryRepository.findById(1000L)).thenReturn(Optional.of(entry));

        assertThrows(RuntimeException.class, () -> queueService.cancelQueueEntry(1000L));
    }

    @Test
    void testGetQueue_NotFound_Throws() {
        when(queueRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> queueService.getQueueStatus(99L));
    }
}
