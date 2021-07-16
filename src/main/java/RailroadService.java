import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import exeptions.WrongInputException;

public class RailroadService {

    public static final String NO_ROUTE = "NO ROUTE";

    List<String> graphList;

    public RailroadService(String graph) {
        graphList = parseGraph(graph);
    }

    public int countDistanceOfRoute(String route) throws WrongInputException {
        List<String> routeList = parseRoute(route);
        StringBuilder edge = new StringBuilder();
        int distance = 0;
        for (int i = 0; i < routeList.size() - 1; i++) {
            edge.append(routeList.get(i)).append(routeList.get(i + 1));
            boolean found = false;
            for (String graphEdge : graphList) {
                if (graphEdge.contains(edge)) {
                    distance += Integer.parseInt(graphEdge.substring(2));
                    found = true;
                }
            }
            if (!found) return -1;
            edge.delete(0, edge.length());
        }
        return distance;
    }

    public Map<String, Integer> givePossibleTripsWithMaxStops(String startNode, String finishNode, int maxStops) {
        return givePossibleTripsWithMaxOrExactStops(startNode, finishNode, maxStops, false);
    }

    public Map<String, Integer> givePossibleTripsWithExactStops(String startNode, String finishNode, int stops) {
        return givePossibleTripsWithMaxOrExactStops(startNode, finishNode, stops, true);
    }

    private Map<String, Integer> givePossibleTripsWithMaxOrExactStops(String startNode, String finishNode, int maxStops,
                                                                      boolean exactStops) {
        int numberOfTrips = 0;
        List<String> route = new ArrayList<>();
        Map<String, Integer> possibleTrips = new TreeMap<>();
        findFinishNode(startNode, finishNode, maxStops, numberOfTrips, route, possibleTrips, exactStops);
        return possibleTrips;
    }

    public int countNumberOfPossibleTrips(String startNode, String finishNode, int maxStops) {
        return givePossibleTripsWithMaxStops(startNode, finishNode, maxStops).size();
    }

    private int findFinishNode(String startNode, String finishNode, int maxStops, int numberOfTrips, List<String> route,
                               Map<String, Integer> possibleTrips, boolean exactStops) {
        int localMaxStops = maxStops;

        for (String graphEdge : graphList) {
            if (graphEdge.substring(0, 1).equals(startNode) && localMaxStops > 0) {
                String nextNode = String.valueOf(graphEdge.charAt(1));
                localMaxStops--;
                route.add(graphEdge);
                if (exactStops) {
                    if (localMaxStops == 0 && finishNode.equals(nextNode)) {
                        numberOfTrips++;
                        buildPossibleTrip(route, possibleTrips);
                        return numberOfTrips;
                    } else {
                        numberOfTrips = findFinishNode(nextNode, finishNode, localMaxStops, numberOfTrips, route, possibleTrips, true);
                        localMaxStops = maxStops;
                    }
                } else {
                    if (finishNode.equals(nextNode)) {
                        numberOfTrips++;
                        buildPossibleTrip(route, possibleTrips);
                        return numberOfTrips;
                    } else {
                        numberOfTrips = findFinishNode(nextNode, finishNode, localMaxStops, numberOfTrips, route, possibleTrips, false);
                        localMaxStops = maxStops;
                    }
                }
                route.clear();
            }
        }
        return numberOfTrips;
    }

    private void buildPossibleTrip(List<String> route, Map<String, Integer> possibleTrips) {
        StringBuilder trip = new StringBuilder(route.get(0).substring(0, 2));
        for (int i = 1; i < route.size(); i++) {
            trip.append(route.get(i).charAt(1));
        }
        possibleTrips.put(trip.toString(), route.size());
    }

    private List<String> parseGraph(String graph) { //todo: think about validation
        return Arrays.stream(graph.split(",")).map(String::strip).collect(Collectors.toList());
    }

    private List<String> parseRoute(String route) throws WrongInputException {
        List<String> routeList = Arrays.stream(route.split("->")).map(String::strip).collect(Collectors.toList());
        validateRoute(routeList);//todo: think about validation
        return routeList;
    }

    private void validateRoute(List<String> routeList) throws WrongInputException {
        if (routeList.size() < 2) {
            throw new WrongInputException("It should be at least 2 nodes");
        }
    }

    public String findShortestWay(String startNode, String finishNode) {
        Map<String, Integer> map = givePossibleTripsWithMaxStops(startNode, finishNode, 4);

        map.keySet().forEach(rout -> {
            int routeValue = 0;
            for (int index = 0; index < rout.length() - 1; index++) {
                String routeEdge = rout.substring(index, index + 1);
                String graphEdge = graphList.stream().filter(edge -> edge.contains(routeEdge)).findFirst().orElse("");
                int graphEdgeValue = Integer.parseInt(graphEdge.substring(2));
                routeValue += graphEdgeValue;
            }
            map.put(rout, routeValue);
        });
        String shortestRoute = "";
        int shortestValue = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() < shortestValue) {
                shortestValue = entry.getValue();
                shortestRoute = entry.getKey();
            }
        }
        return shortestRoute;
    }
}
