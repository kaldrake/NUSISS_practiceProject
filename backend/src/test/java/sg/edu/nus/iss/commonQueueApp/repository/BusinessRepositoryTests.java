/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import sg.edu.nus.iss.commonQueueApp.entity.Business;
import sg.edu.nus.iss.commonQueueApp.entity.BusinessType;
/**
 *
 * @author junwe
 */
@DataJpaTest
@ActiveProfiles("test")
public class BusinessRepositoryTests {
    @Autowired
    private BusinessRepository businessRepository;

    private Business activeVerifiedBusiness;
    private Business inactiveBusiness;

    @BeforeEach
    void setUp() {
        activeVerifiedBusiness = new Business();
        activeVerifiedBusiness.setBusinessName("Coffee Haven");
        activeVerifiedBusiness.setEmail("coffee@haven.com");
        activeVerifiedBusiness.setPassword("password123");
        activeVerifiedBusiness.setAddress("123 Main St");
        activeVerifiedBusiness.setBusinessType(BusinessType.RESTAURANT);
        activeVerifiedBusiness.setIsActive(true);
        activeVerifiedBusiness.setIsVerified(true);

        inactiveBusiness = new Business();
        inactiveBusiness.setBusinessName("Old Store");
        inactiveBusiness.setEmail("old@store.com");
        inactiveBusiness.setPassword("testpass");
        inactiveBusiness.setAddress("456 Market Rd");
        inactiveBusiness.setBusinessType(BusinessType.RETAIL);
        inactiveBusiness.setIsActive(false);
        inactiveBusiness.setIsVerified(false);

        businessRepository.save(activeVerifiedBusiness);
        businessRepository.save(inactiveBusiness);
    }

    @Test
    void testFindByEmail() {
        Optional<Business> result = businessRepository.findByEmail("coffee@haven.com");
        assertThat(result).isPresent();
        assertThat(result.get().getBusinessName()).isEqualTo("Coffee Haven");
    }
    
    @Test
    void testFindByBusinessTypeAndIsActiveTrue() {
        List<Business> result = businessRepository.findByBusinessTypeAndIsActiveTrue(BusinessType.RESTAURANT);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBusinessName()).isEqualTo("Coffee Haven");
    }

    @Test
    void testFindByIsActiveTrueOrderByBusinessNameAsc() {
        List<Business> result = businessRepository.findByIsActiveTrueOrderByBusinessNameAsc();
        assertThat(result).extracting(Business::getBusinessName)
                .containsExactly("Coffee Haven");
    }

    @Test
    void testFindActiveAndVerifiedBusinesses() {
        List<Business> result = businessRepository.findActiveAndVerifiedBusinesses();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsActive()).isTrue();
        assertThat(result.get(0).getIsVerified()).isTrue();
    }

    @Test
    void testFindByBusinessNameContainingIgnoreCaseAndIsActiveTrue() {
        List<Business> result = businessRepository.findByBusinessNameContainingIgnoreCaseAndIsActiveTrue("coffee");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsActive()).isTrue();
        assertThat(result.get(0).getEmail()).isEqualTo("coffee@haven.com");
    }

    @Test
    void testFindByLocationAndIsActiveTrue() {
        List<Business> result = businessRepository.findByLocationAndIsActiveTrue("Main");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsActive()).isTrue();
        assertThat(result.get(0).getBusinessName()).isEqualTo("Coffee Haven");
    }

    @Test
    void testExistsByEmail() {
        boolean exists = businessRepository.existsByEmail("coffee@haven.com");
        boolean notExists = businessRepository.existsByEmail("unknown@nope.com");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
