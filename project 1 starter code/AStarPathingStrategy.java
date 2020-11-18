import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();
        HashMap<Point, Node> openList = new HashMap<>();
        //List<Node> filterOpen = new ArrayList<>();
        PriorityQueue<Node> filterOpen = new PriorityQueue<Node>(Comparator.comparingInt(Node::getF));
        HashMap<Point, Node> closedList = new HashMap<>();
        Node startNode = new Node(0, calculateH(end, start), calculateH(end, start), null,  start);
        openList.put(start,startNode);
        Node currentNode;
        filterOpen.add(startNode);

        while(filterOpen.size() != 0){
            // change current node to one with the lowest f-value
            //Comparator<Node> compareF = Comparator.comparingInt(Node::getF);
            //filterOpen.sort(compareF);
            if(!openList.isEmpty()) {
                currentNode = filterOpen.poll();
            }
            else{
                return path;
            }
            if (withinReach.test(currentNode.getCurrent(), end)){
                return completePath(path, currentNode);
            }
            //step 3
            List<Point> neighbors = potentialNeighbors.apply(currentNode.getCurrent())
                    .filter(canPassThrough)
                    .filter(p -> !closedList.containsKey(p))
                    .collect(Collectors.toList());
            //step3a
            for (Point neighbor : neighbors) {
                //put neighbor point into a Node
                int tempG = currentNode.getG() + 1;
                Node neighborNode = new Node(tempG, calculateH(neighbor, end), calculateH(neighbor, end) + tempG, currentNode, neighbor);
                if (openList.containsKey(neighbor)) {
                    if (openList.get(neighbor).getG() > tempG) {
                        filterOpen.remove(openList.get(neighbor));
                        filterOpen.add(neighborNode);
                        openList.replace(neighbor, neighborNode);
                    }
                }
                else {
                    openList.put(neighbor, neighborNode);
                    filterOpen.add(neighborNode);
                }
                openList.remove(currentNode.getCurrent());
                closedList.put(currentNode.getCurrent(), currentNode);
            }
        }
        return path;
    }

    public List<Point> completePath(List<Point> computedPath, Node finished){
        computedPath.add(finished.getCurrent());
        if(finished.getPrevious() == null){
            computedPath.remove(finished.getCurrent());
            Collections.reverse(computedPath);
            return computedPath;
        }
        return completePath(computedPath, finished.getPrevious());
    }

    public int calculateH(Point current, Point end){
        return Math.abs((end.y - current.y)) + Math.abs((end.x - current.x));
    }
}
