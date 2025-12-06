import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@Tag("regression")
@DisplayName("ProductStock Full Test Class")
public class ProductStockTest {

    ProductStock stock;



    @BeforeAll
    static void beforeAll() {
        System.out.println("Starting ProductStock Tests");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finished ProductStock Tests");
    }

    @BeforeEach
    void setUp() {
        stock = new ProductStock("P1", "WH-1", 50, 10, 100);
    }

    @AfterEach
    void tearDown() {
        stock = null;
    }



    @Test
    @Tag("sanity")
    @DisplayName("Valid constructor")
    void testValidConstructor() {
        assertAll(
                () -> assertEquals("P1", stock.getProductId()),
                () -> assertEquals(50, stock.getOnHand()),
                () -> assertEquals(0, stock.getReserved())
        );
    }

    @Test
    @DisplayName("Invalid constructor parameters")
    void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductStock("", "WH", 10, 5, 50));
    }




    @Test
    @DisplayName("Add stock over capacity")
    void testAddStockOverCapacity() {
        assertThrows(IllegalStateException.class, () ->
                stock.addStock(100));
    }

    @Test
    @Timeout(1)
    @DisplayName("Add stock with negative amount")
    void testAddStockNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                stock.addStock(-5));
    }



    @Test

    void testReserve() {
        stock.reserve(10);
        assertEquals(10, stock.getReserved());
        assertEquals(40, stock.getAvailable());
    }

    @Test
    @DisplayName("Reserve more than available")
    void testReserveTooMuch() {
        assertThrows(IllegalStateException.class, () ->
                stock.reserve(100));
    }

    @Test

    void testReleaseReservation() {
        stock.reserve(20);
        stock.releaseReservation(10);
        assertEquals(10, stock.getReserved());
    }



    @Test
    @DisplayName("Ship reserved stock")
    void testShipReserved() {
        stock.reserve(15);
        stock.shipReserved(10);

        assertAll(
                () -> assertEquals(40, stock.getOnHand()),
                () -> assertEquals(5, stock.getReserved())
        );
    }

    @Test
    @DisplayName("Ship more than reserved")
    void testShipMoreThanReserved() {
        stock.reserve(5);
        assertThrows(IllegalStateException.class, () ->
                stock.shipReserved(10));
    }



    @Test

    void testRemoveDamaged() {
        stock.removeDamaged(10);
        assertEquals(40, stock.getOnHand());
    }

    @Test

    void testRemoveDamagedTooMuch() {
        assertThrows(IllegalStateException.class, () ->
                stock.removeDamaged(60));
    }



    @Test
    @DisplayName("Reorder needed when available below threshold")
    void testReorderNeeded() {
        stock.reserve(45);
        assertTrue(stock.isReorderNeeded());
    }

    @Test

    void testReorderNotNeeded() {
        assertFalse(stock.isReorderNeeded());
    }



    @Test

    void testUpdateReorderThreshold() {
        stock.updateReorderThreshold(20);
        assertEquals(20, stock.getReorderThreshold());
    }

    @Test
    @DisplayName("Update max capacity")
    void testUpdateMaxCapacity() {
        stock.updateMaxCapacity(120);
        assertEquals(120, stock.getMaxCapacity());
    }

    @Test

    void testInvalidMaxCapacity() {
        assertThrows(IllegalStateException.class, () ->
                stock.updateMaxCapacity(10));
    }



    @Test

    void testChangeLocation() {
        stock.changeLocation("WH-2-B3");
        assertEquals("WH-2-B3", stock.getLocation());
    }

    @Test
    @DisplayName("Change location with invalid value")
    void testChangeLocationInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                stock.changeLocation(""));
    }
    @Test

    void testGetAvailable() {
        stock.reserve(15);
        assertEquals(35, stock.getAvailable());
    }
    @Test

    void testReleaseMoreThanReserved() {
        stock.reserve(10);
        assertThrows(IllegalStateException.class, () ->
                stock.releaseReservation(20));
    }
    @Test
    @DisplayName("Reserve exactly available stock")
    void testReserveExactAvailable() {
        stock.reserve(50);
        assertEquals(0, stock.getAvailable());
    }
    @Test
    @DisplayName("Remove damaged adjusts reserved if needed")
    void testRemoveDamagedAdjustsReserved() {
        stock.reserve(40);
        stock.removeDamaged(30);

        // onHand = 20 → reserved لازم تنقص
        assertEquals(20, stock.getOnHand());
        assertEquals(20, stock.getReserved());
    }
    @Test

    void testUpdateReorderThresholdNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                stock.updateReorderThreshold(-1));
    }

    @Test

    void testUpdateReorderThresholdTooHigh() {
        assertThrows(IllegalArgumentException.class, () ->
                stock.updateReorderThreshold(200));
    }
    @Test

    void testUpdateMaxCapacityEqualOnHand() {
        stock.updateMaxCapacity(50);
        assertEquals(50, stock.getMaxCapacity());
    }
    @Test

    void testUpdateMaxCapacityInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                stock.updateMaxCapacity(0));
    }
    @Test
    @DisplayName("toString should contain productId")
    void testToString() {
        String result = stock.toString();
        assertTrue(result.contains("P1"));
    }

    @Disabled("Future feature: automatic reorder")
    @Test
    void testAutoReorderFeature() {
        fail("Not implemented yet");
    }
}
