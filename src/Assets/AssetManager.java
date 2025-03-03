package Assets;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AssetManager {

    private static final int MAX_CACHE_SIZE = 100;

    HashMap<String, Asset> assets = new HashMap<>();
    private final Map<String, BufferedImage> resizedCache = new LinkedHashMap<String, BufferedImage>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, BufferedImage> eldest) {
            return size() > MAX_CACHE_SIZE; // Removes the oldest entry if size exceeds limit
        }
    };

    String currentScene = "Basic";

    public AssetManager(GamePanel gp){
        loadAssets(gp);
        loadSceneAssets("All");
        switchScene("Basic");
    }

    public void loadAssets(GamePanel gp){
        playerSprites(gp);
        npcSprites(gp);
        monsterSprites(gp);
        objectSprites(gp);
        spellSprites(gp);
    }

    public void switchScene(String scene){
        currentScene = scene;
    }
    public void loadSceneAssets(String scene) {
        currentScene = scene;
        for (Asset asset : assets.values()) {
            if (asset.isUsedInScene(scene)) {
                asset.getImage(); // Lazy loading applied
            }
        }
    }
    public void unloadSceneAssets(String scene) {
        for (Asset asset : assets.values()) {
            if (asset.isUsedInScene(scene)||!asset.isUsedInScene("All")) {
                asset.clearImage(); // Free up memory
            }
        }
    }
    private void playerSprites(GamePanel gp){
        //Player Sprites
        assets.put("PlayerUp1",new Asset("/player/boy_up_1",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerUp2",new Asset("/player/boy_up_2",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerDown1",new Asset("/player/boy_down_1",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerDown2",new Asset("/player/boy_down_2",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerLeft1",new Asset("/player/boy_left_1",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerLeft2",new Asset("/player/boy_left_2",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerRight1",new Asset("/player/boy_right_1",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerRight2",new Asset("/player/boy_right_2",gp.tileSize, gp.tileSize, "All"));
        assets.put("PlayerAttackUp1",new Asset("/player/boy_attack_up_1",gp.tileSize,gp.tileSize*2, "All"));
        assets.put("PlayerAttackUp2",new Asset("/player/boy_attack_up_2",gp.tileSize,gp.tileSize*2, "All"));
        assets.put("PlayerAttackDown1",new Asset("/player/boy_attack_down_1",gp.tileSize,gp.tileSize*2, "All"));
        assets.put("PlayerAttackDown2",new Asset("/player/boy_attack_Down_2",gp.tileSize,gp.tileSize*2, "All"));
        assets.put("PlayerAttackLeft1",new Asset("/player/boy_attack_left_1",gp.tileSize*2,gp.tileSize, "All"));
        assets.put("PlayerAttackLeft2",new Asset("/player/boy_attack_left_2",gp.tileSize*2,gp.tileSize, "All"));
        assets.put("PlayerAttackRight1",new Asset("/player/boy_attack_right_1",gp.tileSize*2,gp.tileSize, "All"));
        assets.put("PlayerAttackRight2",new Asset("/player/boy_attack_right_2",gp.tileSize*2,gp.tileSize, "All"));

    }
    private void npcSprites(GamePanel gp){
        //Old wise wizard
        assets.put("NPC_OldManUp1", new Asset("/npc/oldman_up_1",gp.tileSize, gp.tileSize,"Testing"));
        assets.put("NPC_OldManUp2", new Asset("/npc/oldman_up_2",gp.tileSize, gp.tileSize,"Testing"));
        assets.put("NPC_OldManDown1", new Asset("/npc/oldman_down_1",gp.tileSize, gp.tileSize,"Testing"));
        assets.put("NPC_OldManDown2", new Asset("/npc/oldman_down_2",gp.tileSize, gp.tileSize,"Testing"));
        assets.put("NPC_OldManLeft1", new Asset("/npc/oldman_left_1",gp.tileSize, gp.tileSize,"Testing"));
        assets.put("NPC_OldManLeft2", new Asset("/npc/oldman_left_2",gp.tileSize, gp.tileSize,"Testing"));
        assets.put("NPC_OldManRight1", new Asset("/npc/oldman_right_1",gp.tileSize, gp.tileSize,"Testing"));
        assets.put("NPC_OldManRight2", new Asset("/npc/oldman_right_2",gp.tileSize, gp.tileSize,"Testing"));
    }
    private void monsterSprites(GamePanel gp){
        assets.put("MON_BlueSlimeUp1", new Asset("/monster/blueslime_down_1",gp.tileSize, gp.tileSize,"All"));
        assets.put("MON_BlueSlimeUp2", new Asset("/monster/blueslime_down_2",gp.tileSize, gp.tileSize,"All"));
        assets.put("MON_BlueSlimeDown1", new Asset("/monster/blueslime_down_1",gp.tileSize, gp.tileSize,"All"));
        assets.put("MON_BlueSlimeDown2", new Asset("/monster/blueslime_down_2",gp.tileSize, gp.tileSize,"All"));
        assets.put("MON_BlueSlimeLeft1", new Asset("/monster/blueslime_down_1",gp.tileSize, gp.tileSize,"All"));
        assets.put("MON_BlueSlimeLeft2", new Asset("/monster/blueslime_down_2",gp.tileSize, gp.tileSize,"All"));
        assets.put("MON_BlueSlimeRight1", new Asset("/monster/blueslime_down_1",gp.tileSize, gp.tileSize,"All"));
        assets.put("MON_BlueSlimeRight2", new Asset("/monster/blueslime_down_2",gp.tileSize, gp.tileSize,"All"));
    }
    private void objectSprites(GamePanel gp){
        assets.put("OBJ_Boots", new Asset("/objects/boots",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_Chest", new Asset("/objects/Chest",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_Door", new Asset("/objects/door",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_FullHeart", new Asset("/objects/full_heart",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_QuarterMissingHeart", new Asset("/objects/quarter_missing_heart",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_HalfHeart", new Asset("/objects/half_heart",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_QuarterHeart", new Asset("/objects/quarter_heart",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_EmptyHeart", new Asset("/objects/empty_heart",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_Key", new Asset("/objects/key",gp.tileSize, gp.tileSize,"All"));
        assets.put("OBJ_Shield", new Asset("/objects/shield",gp.tileSize,gp.tileSize,"All"));
        assets.put("OBJ_Sword", new Asset("/objects/Sword", gp.tileSize,gp.tileSize,"All"));
    }
    private void spellSprites(GamePanel gp){
        //Fire Ball Projectile
        assets.put("SPE_PRO_FireBall1", new Asset("/attacks/flamecircle_1",gp.tileSize,gp.tileSize,"All"));
        assets.put("SPE_PRO_FireBall2", new Asset("/attacks/flamecircle_2",gp.tileSize,gp.tileSize,"All"));
        //Fire Ball Effect
        assets.put("SPE_EFF_FireBall1", new Asset("/attacks/flamecircle_4",gp.tileSize,gp.tileSize,"All"));
        assets.put("SPE_EFF_FireBall2", new Asset("/attacks/flameCircle_5",gp.tileSize,gp.tileSize,"All"));
    }
    public BufferedImage resizeAndGetAsset(String tag, int width, int height) {
        String key = tag + "_" + width + "x" + height; // Unique cache key

        // Return cached version if it exists
        if (resizedCache.containsKey(key)) {
            return resizedCache.get(key);
        }

        Asset asset = assets.get(tag);
        if (asset == null) {
            return null; // Handle missing assets
        }

        // Resize and cache it
        BufferedImage resized = scaleImage(asset.getImage(), width, height);
        resizedCache.put(key, resized);
        return resized;
    }
    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null); // No rendering hint for speed
        g2.dispose();
        return scaledImage;
    }
    public BufferedImage getAsset(String tag){
        return assets.get(tag).getImage();
    }
    public void clearResizedCache() {
        resizedCache.clear(); // Clears the cache when memory needs to be freed
    }

}
