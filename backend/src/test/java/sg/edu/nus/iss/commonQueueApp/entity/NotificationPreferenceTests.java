/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junwe
 */
public class NotificationPreferenceTests {
    @Test
    void testEnumValues() {
        NotificationPreference email = NotificationPreference.EMAIL;
        NotificationPreference sms = NotificationPreference.SMS;
        NotificationPreference both = NotificationPreference.BOTH;
        NotificationPreference none = NotificationPreference.NONE;

        assertEquals("Email Only", email.getDisplayName());
        assertEquals("SMS Only", sms.getDisplayName());
        assertEquals("Email and SMS", both.getDisplayName());
        assertEquals("No Notifications", none.getDisplayName());
    }

    @Test
    void testEnumValueOf() {
        assertEquals(NotificationPreference.EMAIL, NotificationPreference.valueOf("EMAIL"));
        assertEquals(NotificationPreference.SMS, NotificationPreference.valueOf("SMS"));
        assertEquals(NotificationPreference.BOTH, NotificationPreference.valueOf("BOTH"));
        assertEquals(NotificationPreference.NONE, NotificationPreference.valueOf("NONE"));
    }

    @Test
    void testEnumValuesArray() {
        NotificationPreference[] prefs = NotificationPreference.values();
        assertEquals(4, prefs.length);
        assertArrayEquals(new NotificationPreference[]{
                NotificationPreference.EMAIL,
                NotificationPreference.SMS,
                NotificationPreference.BOTH,
                NotificationPreference.NONE
        }, prefs);
    }
}
