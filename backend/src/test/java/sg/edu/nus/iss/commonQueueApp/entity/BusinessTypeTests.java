/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.BusinessType;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junwe
 */
public class BusinessTypeTests {
    @Test
    void testEnumValuesAndDisplayName() {
        assertEquals("Clinic", BusinessType.CLINIC.getDisplayName());
        assertEquals("Restaurant", BusinessType.RESTAURANT.getDisplayName());
        assertEquals("Retail Store", BusinessType.RETAIL.getDisplayName());
        assertEquals("Service Center", BusinessType.SERVICE_CENTER.getDisplayName());
        assertEquals("Pharmacy", BusinessType.PHARMACY.getDisplayName());
        assertEquals("Bank", BusinessType.BANK.getDisplayName());
        assertEquals("Government Office", BusinessType.GOVERNMENT.getDisplayName());
        assertEquals("Other", BusinessType.OTHER.getDisplayName());
    }

    @Test
    void testEnumOrdinalAndName() {
        BusinessType type = BusinessType.CLINIC;
        assertEquals("CLINIC", type.name());
        assertEquals(0, type.ordinal());

        type = BusinessType.PHARMACY;
        assertEquals("PHARMACY", type.name());
        assertEquals(4, type.ordinal());
    }
}
