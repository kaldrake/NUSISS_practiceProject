/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.*;

import static org.assertj.core.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueEntryFactoryTests {
    private QueueEntryFactory factory;
    private Queue queue;
    private Customer customer;

    @BeforeEach
    void setUp() {
        factory = new QueueEntryFactory();

        queue = new Queue();
        queue.setQueueName("Main Queue");

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");
    }

    @Test
    void createEntry_ShouldReturnValidQueueEntry() {
        QueueEntry entry = factory.createEntry(queue, customer, 3);

        assertThat(entry).isNotNull();
        assertThat(entry.getQueue()).isEqualTo(queue);
        assertThat(entry.getCustomer()).isEqualTo(customer);
        assertThat(entry.getQueueNumber()).isEqualTo(3);
    }

    @Test
    void createEntry_ShouldThrow_WhenQueueIsNull() {
        assertThatThrownBy(() -> factory.createEntry(null, customer, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Queue cannot be null");
    }

    @Test
    void createEntry_ShouldThrow_WhenCustomerIsNull() {
        assertThatThrownBy(() -> factory.createEntry(queue, null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer cannot be null");
    }

    @Test
    void createEntry_ShouldThrow_WhenQueueNumberIsNullOrNonPositive() {
        assertThatThrownBy(() -> factory.createEntry(queue, customer, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Queue number must be positive");

        assertThatThrownBy(() -> factory.createEntry(queue, customer, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Queue number must be positive");

        assertThatThrownBy(() -> factory.createEntry(queue, customer, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Queue number must be positive");
    }
}
