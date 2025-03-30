package Navigation;

import entity.Entity;
import main.GamePanel;
import tools.Transform;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PathFinding {
    GamePanel gp;
    private NavMesh navMesh;
    private Entity entity;
    private List<Transform> pathPoints;  // List of points to reach
    private boolean pathPossible;

    public PathFinding(GamePanel gp, Entity entity) {
        this.gp = gp;
        navMesh = gp.navMesh;
        this.entity = entity;
        this.pathPoints = new LinkedList<>();
        this.pathPossible = true;
    }

    // Recalculate the path to the target
    public void recalculatePath(Transform targetLocation) {
        clear();  // Clear previous path
        pathPossible = true; // Reset path possible status
        aStarPathfinding(entity.location, targetLocation);
    }

    private void clear(){
        for(Transform t: pathPoints){
            t.markDone();
        }
        pathPoints.clear();
    }

    // A* pathfinding algorithm (simplified)
    private void aStarPathfinding(Transform start, Transform goal) {
        int tempX = start.getWorldX();
        int tempY = start.getWorldY();

        NavCube startCube = navMesh.getNavCubeFromWorldCoords(tempX, tempY);
        NavCube goalCube = navMesh.getNavCubeFromWorldCoords(goal.getWorldX(), goal.getWorldY());

        if (startCube == null || goalCube == null) {
            pathPossible = false;
            return;  // No valid path if the start or goal are invalid
        }

        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Set<Node> closedList = new HashSet<>();
        Map<NavCube, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startCube, null, 0, calcHeuristic(start, goal));
        openList.add(startNode);
        allNodes.put(startCube, startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            closedList.add(currentNode);

            if (currentNode.navCube == goalCube) {
                // Goal reached, reconstruct path
                reconstructPath(currentNode);
                return;
            }

            // Use the cube's neighbors directly
            List<NavCube> neighbors = currentNode.navCube.getNeighbors();
            for (NavCube neighbor : neighbors) {
                if (closedList.contains(allNodes.get(neighbor))) continue;

                int tentativeG = currentNode.g + 1; // Assume cost of moving to any neighboring tile is 1
                Node neighborNode = allNodes.get(neighbor);
                if (neighborNode == null || tentativeG < neighborNode.g) {
                    Transform temp = gp.objectP.getTransform(neighbor.rect.x, neighbor.rect.y);
                    int h = calcHeuristic(temp, goal);
                    temp.markDone();
                    if (neighborNode == null) {
                        neighborNode = new Node(neighbor, currentNode, tentativeG, h);
                        allNodes.put(neighbor, neighborNode);
                    } else {
                        neighborNode.g = tentativeG;
                        neighborNode.h = h;
                        neighborNode.parent = currentNode;
                    }
                    neighborNode.f = neighborNode.g + neighborNode.h;
                    openList.add(neighborNode);
                }
            }
        }

        pathPossible = false;  // No path found
    }

    // Reconstruct the path from the goal to the start
    private void reconstructPath(Node currentNode) {
        List<Transform> path = new ArrayList<>();
        while (currentNode != null) {
            path.add(gp.objectP.getTransform(currentNode.navCube.rect.x, currentNode.navCube.rect.y));
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);  // Reverse path to get the correct order from start to goal
        pathPoints = path;  // Store the path as a list of transforms
    }

    // Calculate the heuristic (Manhattan distance) from a node to the goal
    private int calcHeuristic(Transform start, Transform goal) {
        return Math.abs(start.getWorldX() - goal.getWorldX()) + Math.abs(start.getWorldY() - goal.getWorldY());
    }

    // Return the next direction based on the current position
    public String getNextDirection() {
        if (pathPoints.isEmpty()) return null; // No more path points

        Transform nextPoint = pathPoints.get(0);
        return calcDirection(entity.location, nextPoint);
    }

    // Return the next location to move to
    public Transform getNextLocation() {
        if (pathPoints.isEmpty()) return null; // No more path points
        return pathPoints.getFirst();  // Return the first point in the path
    }

    // Mark the current point as done and remove it from the path
    public void markCurrentPointDone() {
        if (!pathPoints.isEmpty()) {
            pathPoints.getFirst().markDone();
            pathPoints.removeFirst();
        }
    }

    // Calculate direction from the current position to the target
    private String calcDirection(Transform current, Transform target) {
        if (target.getWorldX() < current.getWorldX()) return "left";
        if (target.getWorldX() > current.getWorldX()) return "right";
        if (target.getWorldY() < current.getWorldY()) return "up";
        return "down";  // target.getWorldY() > current.getWorldY()
    }

    // Check if path is still valid, if not, recalculate the path
    public boolean isPathPossible() {
        return pathPossible;
    }

    // Node class for A* algorithm
    private static class Node {
        NavCube navCube;
        Node parent;
        int g;  // Cost from start to this node
        int h;  // Heuristic cost to the goal
        int f;  // Total cost (g + h)

        Node(NavCube navCube, Node parent, int g, int h) {
            this.navCube = navCube;
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }
}
