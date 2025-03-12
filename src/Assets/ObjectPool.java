package Assets;

import main.GamePanel;
import tools.Transform;
import java.util.LinkedList;

public class ObjectPool {
    private final LinkedList<Transform> TransformPool;
    GamePanel gp;
    // You can add more pools for other types here (e.g., Entity, Projectile, etc.)
    // private final LinkedList<Entity> entityPool;

    // Constructor to initialize the pools
    public ObjectPool(GamePanel gp) {
        this.gp = gp;
        TransformPool = new LinkedList<>();
        // Initialize other pools if needed:
        // entityPool = new LinkedList<>();
    }

    // Get a Transform object from the pool (reused or newly created)
    public Transform getTransform(int x, int y) {
        Transform Transform = null;

        // Check if there's a reusable Transform object
        for (Transform t : TransformPool) {
            if (t.getDone()) {
                Transform = t;  // Found one that is done, so we can reuse it
                break;
            }
        }

        if (Transform == null) {
            // If no reusable object is found, create a new one
            Transform = new Transform(x, y);
            TransformPool.add(Transform);  // Add the new object to the pool
        } else {
            // Reinitialize the reusable object with new data
            Transform.reUse(x, y);
        }

        return Transform;
    }

}
