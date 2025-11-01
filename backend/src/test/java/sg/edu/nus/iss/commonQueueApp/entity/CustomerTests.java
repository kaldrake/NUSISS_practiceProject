/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class CustomerTests {
    @Test
    void testConstructorsAndSettersGetters() {
        // Using default constructor
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("+6512345678");
        customer.setNotificationPreference(NotificationPreference.BOTH);
        customer.setLanguagePreference("en");
        customer.setIsActive(true);
        customer.setAvatarUrl("http://avatar.url");
        LocalDateTime now = LocalDateTime.now();
        customer.setLastLogin(now);
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        List<QueueEntry> entries = new ArrayList<>();
        customer.setQueueEntries(entries);

        List<Feedback> feedbacks = new ArrayList<>();
        customer.setFeedbacks(feedbacks);

        List<Business> favorites = new ArrayList<>();
        customer.setFavoriteBusinesses(favorites);

        // Assertions
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals("+6512345678", customer.getPhone());
        assertEquals(NotificationPreference.BOTH, customer.getNotificationPreference());
        assertEquals("en", customer.getLanguagePreference());
        assertTrue(customer.getIsActive());
        assertEquals("http://avatar.url", customer.getAvatarUrl());
        assertEquals(now, customer.getLastLogin());
        assertEquals(now, customer.getCreatedAt());
        assertEquals(now, customer.getUpdatedAt());
        assertSame(entries, customer.getQueueEntries());
        assertSame(feedbacks, customer.getFeedbacks());
        assertSame(favorites, customer.getFavoriteBusinesses());

        // Test constructor with name, email, phone
        Customer customer2 = new Customer("Alice", "alice@example.com", "+6598765432");
        assertEquals("Alice", customer2.getName());
        assertEquals("alice@example.com", customer2.getEmail());
        assertEquals("+6598765432", customer2.getPhone());

        // Test constructor with name, phone only
        Customer customer3 = new Customer("Bob", "+6587654321");
        assertEquals("Bob", customer3.getName());
        assertEquals("+6587654321", customer3.getPhone());
        assertNull(customer3.getEmail());
    }

    @Test
    void testNotificationPreferences() {
        Customer customer = new Customer();
        customer.setEmail("a@b.com");
        customer.setPhone("+65 1234 5678");

        customer.setNotificationPreference(NotificationPreference.EMAIL);
        assertTrue(customer.canReceiveEmailNotifications());
        assertFalse(customer.canReceiveSmsNotifications());

        customer.setNotificationPreference(NotificationPreference.SMS);
        assertFalse(customer.canReceiveEmailNotifications());
        assertTrue(customer.canReceiveSmsNotifications());

        customer.setNotificationPreference(NotificationPreference.BOTH);
        assertTrue(customer.canReceiveEmailNotifications());
        assertTrue(customer.canReceiveSmsNotifications());
    }

    @Test
    void testFavoriteBusinesses() {
        Customer customer = new Customer();
        Business business1 = new Business("Biz1", "b1@mail.com", "pass");
        Business business2 = new Business("Biz2", "b2@mail.com", "pass");

        customer.addFavoriteBusiness(business1);
        customer.addFavoriteBusiness(business2);
        assertEquals(2, customer.getFavoriteBusinesses().size());

        customer.removeFavoriteBusiness(business1);
        assertEquals(1, customer.getFavoriteBusinesses().size());
        assertSame(business2, customer.getFavoriteBusinesses().get(0));
    }

    @Test
    void testQueueMethods() {
        Customer customer = new Customer();
        Queue queue = new Queue();
        QueueEntry entry = new QueueEntry();
        entry.setCustomer(customer);
        entry.setQueue(queue);
        entry.setQueueNumber(1);
        entry.setStatus(QueueEntryStatus.WAITING);

        List<QueueEntry> entries = new ArrayList<>();
        entries.add(entry);
        customer.setQueueEntries(entries);

        assertTrue(customer.isCurrentlyInQueue());
        assertEquals(entry, customer.getCurrentQueueEntry());
        assertEquals(1, customer.getActiveQueueEntries().size());
    }

    @Test
    void testVisitsCount() {
        Customer customer = new Customer();
        Business business = new Business("Biz1", "b@mail.com", "pass");
        Queue queue = new Queue();
        queue.setBusiness(business);
        QueueEntry entry1 = new QueueEntry();
        entry1.setCustomer(customer);
        entry1.setQueue(queue);
        entry1.setStatus(QueueEntryStatus.SERVED);

        QueueEntry entry2 = new QueueEntry();
        entry2.setCustomer(customer);
        entry2.setQueue(queue);
        entry2.setStatus(QueueEntryStatus.WAITING);

        customer.setQueueEntries(List.of(entry1, entry2));

        assertEquals(1, customer.getTotalVisitsCount());
        assertEquals(1, customer.getVisitsToBusinessCount(business));
    }
}
