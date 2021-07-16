import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exeptions.WrongInputException;

class RailroadServiceTest {

    private RailroadService railroadService;
    private static final String graph = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7"; //todo: make the same on the txt file

    @BeforeEach
    public void init() {
        railroadService = new RailroadService(graph);
    }

    @Test
    void countDistanceABC() throws WrongInputException {
        String route = "A->B->C";
        int distanceResult = railroadService.countDistanceOfRoute(route);
        assertEquals(9, distanceResult);
    }

    @Test
    void countDistanceAD() throws WrongInputException {
        String route = "A->D";
        int distanceResult = railroadService.countDistanceOfRoute(route);
        assertEquals(5, distanceResult);
    }

    @Test
    void countDistanceADC() throws WrongInputException {
        String route = "A->D->C";
        int distanceResult = railroadService.countDistanceOfRoute(route);
        assertEquals(13, distanceResult);
    }

    @Test
    void countDistanceAEBCD() throws WrongInputException {
        String route = "A->E->B->C->D";
        int distanceResult = railroadService.countDistanceOfRoute(route);
        assertEquals(22, distanceResult);
    }

    @Test
    void countDistanceAED() throws WrongInputException {
        String route = "A->E->D";
        int distanceResult = railroadService.countDistanceOfRoute(route);
        assertEquals(-1, distanceResult);
    }

    @Test
    void testCountNumberOfPossibleTripsWithMaxStops() {
        Map<String, Integer> result = railroadService.givePossibleTripsWithMaxStops("C", "C", 3);
        result.entrySet().forEach(System.out::println);
        assertEquals(2, railroadService.countNumberOfPossibleTrips("C", "C", 3));
    }

    @Test
    void testCountNumberOfPossibleTripsWithExactStops() {
        Map<String, Integer> result = railroadService.givePossibleTripsWithExactStops("A", "C", 4);
        result.entrySet().forEach(System.out::println);
        assertEquals(2, railroadService.countNumberOfPossibleTrips("C", "C", 4));
    }

    @Test
    void testFindShortestWayAC() {
        assertEquals("ABC", railroadService.findShortestWay("A", "C"));
    }

    @Test
    void testFindShortestWayBB() {
        assertEquals("BCEB", railroadService.findShortestWay("B", "B"));
    }

}