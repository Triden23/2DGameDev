package Magic;

import entity.Entity;
import main.GamePanel;
import tools.Circle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//RULE - Damage will tick once every 10 frames for dots
public class Spell {
    //TODO Make a method to rotate image and hitbox based on orientation FUUUUUUUUUUUUUUCK
    //TODO sound FX (easy)
    //First test bug counter: 39
    //Lines of code edited: 135
    //Hours spent wondering why I wanted to add this: 14

    //GAME VARIABLES
    GamePanel gp;
    public Entity caster;
    public String name;
    //Can i trick this?
    //Think of a way to reduce this to one
    public Enhancements baseStats = new Enhancements();

    //Array list used to track what entities its hit by tag so it does not hit them again
    ArrayList<String> hittags;

    //LOCATION OBJECT
    public Coord coord; //Stores the spells x,y and if its a projectile its targetX and its targetY

    //DEBUG VARIABLES
    int spellID;

    //IMAGE DRAWN
    BufferedImage currentSprite;

    //CORE SPELL STATES
    public boolean casting = false;
    boolean trigger = false;
    boolean done = false;

    //STATE TO PREVENT PROBLEMS
    boolean isChild = false;

    //ATTRIBUTE STATES
    public boolean canHome = false;
    public boolean isHoming = false;
    public boolean canPeirce = false;
    public boolean canCast = true; //Unsure if doing silence here or on entity


    //PROJECTILE VARIABLES
    int currentProjectileSprite = 0;
    int projectileFrameCounter = 0;
    int projectileSpriteCounter = 0;
    int projectileSpriteIncrement = 0;
    BufferedImage projectileSprite[];

    //EFFECT VARIABLES
    int currentEffectSprite = 0;
    int effectFrameCounter = 0;
    int effectSpriteCounter = 0;
    int effectSpriteIncrement = 0;
    BufferedImage effectSprite[];

    //HITBOX VARIABLES
    public String projectileHitBoxType;
    public String effectHitBoxType;
    public String currentHitBox;
    public Circle circleHitBox;
    public Rectangle rectangleHitBox;

    //CHILDREN VARIABLES - think of a russian nesting doll
    Coord[] childrenCoord;
    int childrenAmount;

    //PERSISTANT VARAIBLES FOR LOGIC
    public double spiralProgress;
    int tempX,tempY;
    int lifeTime = 0;

    //DAMAGE RELATED VARIABLES
    String type; //used to calculate damage

    //What we change is travelLogic() cast() and hitLogic()
    public Spell(GamePanel gp,boolean isChild, Entity caster){
        this.gp = gp;
        this.name = name;
        this.caster = caster;
        hittags = new ArrayList<>();
        coord = new Coord(caster.worldX,caster.worldY,caster.worldX,caster.worldY);//Default value will be adjusted
        setVariables();
        updateStats();
        createHitBoxs();
        setSprites();
    }

    //---------------------------------------------------------------------------------------------------------------
    //                  Value setup
    //---------------------------------------------------------------------------------------------------------------

    //Update values to reflect what you want the spells to do, refer to Enhancements
    public void setVariables(){
        //Set the mana cost of the spell
        baseStats.manaCost = 0; //Affects entity
        //Set the health cost of the spell
        baseStats.healthCost = 0; //Affects entity
        //set the cooldown of the spell
        baseStats.cooldown = 0; //used in entity
        //set the cast speed of the spell (something like animationlock = spell.castspeed) most wont use it
        baseStats.castSpeed = 0; //used in the entity class
        //set the spellPower use intelligence from the entity to affect this
        baseStats.spellPower = 0;
        //set the duration of the spell (ground effect, dot, etc)
        baseStats.duration = 0;
        //set the range of the spell for projectiles
        baseStats.range = 0;

        //sets the speed of a projectile
        baseStats.projectileSpeed = 0;
        //sets the size of a projectile (used in rect hitbox and circle(radius) hitbox)
        baseStats.projectileSizeX = 0;
        //sets the size of a projectile (used in rect hitbox)
        baseStats.projectileSizeY = 0;

        baseStats.effectSizeX = 0;

        baseStats.effectSizeY = 0;
        //theAmount of projectiles, handled from the entity
        baseStats.projectileCount = 0;
        //the chance to peirce
        baseStats.chanceToPierce = 0.0f; //%chance to pierce
        //Amount of solid hitboxes or entities a projectile can pierce before effect
        baseStats.amountOfPierce = 0;

        //sets the size of the aoe radius
        baseStats.aoeRadius = 0;
        //sets the DOT damage
        baseStats.damageOverTime = 0;
        //sets the damage over time duration
        baseStats.damageOverTimeDuration = 0;
        //sets the strength of a knockback.
        baseStats.knockbackStrength = 0;
        //sets the slow stenght
        baseStats.slowEffect = 0;
        //sets the crit chance
        baseStats.critChance = 0.0f;
        //chance for it to home
        baseStats.homingChance = 0.0f;
    }

    public void updateStats() {
        baseStats.manaCost = (int) (baseStats.manaCost + (baseStats.manaCost * caster.enhancements.manaCostReduction));
        baseStats.healthCost = (int) (baseStats.healthCost + (baseStats.healthCost * caster.enhancements.healthCostReduction));
        baseStats.cooldown = (int) (baseStats.cooldown - (baseStats.cooldown * caster.enhancements.cooldownReduction))*60;
        baseStats.castSpeed = (int) (baseStats.castSpeed - (baseStats.castSpeed * caster.enhancements.castSpeedReduction));
        baseStats.spellPower = (int) (baseStats.spellPower + (baseStats.spellPower * caster.enhancements.spellPowerBuff));
        baseStats.duration = (int) (baseStats.duration + (baseStats.duration * caster.enhancements.durationBuff));
        baseStats.range = (int) (baseStats.range + (baseStats.range * caster.enhancements.rangeBuff));

        // Projectile-Based Enhancements
        baseStats.projectileSpeed = (int) (baseStats.projectileSpeed + (baseStats.projectileSpeed * caster.enhancements.projectileSpeedBuff));
        //System.out.println((int) (baseStats.projectileSizeX + (baseStats.projectileSizeX * caster.enhancements.projectileSizeBuff)));
        baseStats.projectileSizeX = (int) (baseStats.projectileSizeX + (baseStats.projectileSizeX * caster.enhancements.projectileSizeBuff));
        baseStats.projectileSizeY = (int) (baseStats.projectileSizeY + (baseStats.projectileSizeY * caster.enhancements.projectileSizeBuff));
        baseStats.effectSizeX = (int) (baseStats.effectSizeX + (baseStats.effectSizeX * caster.enhancements.effectSizeX));
        baseStats.effectSizeY = (int) (baseStats.effectSizeY + (baseStats.effectSizeY * caster.enhancements.effectSizeY));
        baseStats.projectileCount = baseStats.projectileCount + caster.enhancements.projectileCountBuff;
        baseStats.chanceToPierce = baseStats.chanceToPierce + caster.enhancements.chanceToPierceBuff;
        baseStats.amountOfPierce = baseStats.amountOfPierce + caster.enhancements.amountOfPierceBuff;

        // Area of Effect (AoE) Enhancements
        baseStats.aoeRadius = (int) (baseStats.aoeRadius + (baseStats.aoeRadius * caster.enhancements.aoeRadiusBuff));
        baseStats.damageOverTime = (int) (baseStats.damageOverTime + (baseStats.damageOverTime * caster.enhancements.damageOverTimeBuff));
        baseStats.damageOverTimeDuration = (int) (baseStats.damageOverTimeDuration +
                (baseStats.damageOverTimeDuration * caster.enhancements.damageOverTimeDurationBuff));

        // Utility & Status Effects
        baseStats.knockbackStrength = baseStats.knockbackStrength + (baseStats.knockbackStrength * caster.enhancements.knockbackStrengthBuff);
        baseStats.slowEffect = baseStats.slowEffect + (baseStats.slowEffect * caster.enhancements.slowEffectBuff);
        baseStats.critChance = baseStats.critChance + caster.enhancements.critChanceBuff;
        baseStats.homingChance = baseStats.homingChance + caster.enhancements.homingChanceBuff;

    }

    public void createHitBoxs(){
        //sets the y so it can be drawn properly
        if(projectileHitBoxType.equals("circle")){
            baseStats.projectileSizeY = baseStats.projectileSizeX;
        }
        //Sets the y so it can be drawn properly
        if(effectHitBoxType.equals("circle")){
            baseStats.effectSizeY = baseStats.effectSizeX;
        }
        circleHitBox = new Circle(0,0,(int)(baseStats.projectileSizeX));
        rectangleHitBox = new Rectangle(0,0,baseStats.projectileSizeX,baseStats.projectileSizeY);
        updateHitBox();//Calls to set correct size after initialization;
    }

    //---------------------------------------------------------------------------------------------------------------
    //                  HitBox Related
    //---------------------------------------------------------------------------------------------------------------

    public void updateHitBox(){
        switch(currentHitBox){
            case "projectile":
                switch(projectileHitBoxType){
                    case "circle":
                        updateCircleHitbox();
                        break;
                    case "rectangle":
                        updateRectangleHitBox();
                        break;
                }
                break;
            case "effect":
                switch(effectHitBoxType){
                    case "circle":
                        updateCircleHitbox();
                        break;
                    case "rectangle":
                        updateRectangleHitBox();
                        break;
                }
        }
    }

    public void updateCircleHitbox() {
        circleHitBox.radius = baseStats.projectileSizeX; // Keep radius unchanged

        // Adjust for correct centering when drawn
        circleHitBox.x = coord.x + (gp.tileSize / 2) - circleHitBox.radius;
        circleHitBox.y = coord.y + (gp.tileSize / 2) - circleHitBox.radius;
    }

    public void updateRectangleHitBox(){
        rectangleHitBox.width = baseStats.projectileSizeX;
        rectangleHitBox.height = baseStats.projectileSizeY;

        rectangleHitBox.x = coord.x - (rectangleHitBox.width / 2) + (gp.tileSize / 2);
        rectangleHitBox.y = coord.y - (rectangleHitBox.height / 2) + (gp.tileSize / 2);

        // Calculate expected center position
        //float expectedCenterX = coord.x + (gp.tileSize / 2.0f);
        //float expectedCenterY = coord.y + (gp.tileSize / 2.0f);

        // Calculate actual center position based on rectangleHitBox
        //float actualCenterX = rectangleHitBox.x + (rectangleHitBox.width / 2.0f);
        //float actualCenterY = rectangleHitBox.y + (rectangleHitBox.height / 2.0f);

        // Print debugging info
        //System.out.println("Expected Center: (" + expectedCenterX + ", " + expectedCenterY + ")");
        //System.out.println("Actual Center: (" + actualCenterX + ", " + actualCenterY + ")");
        //System.out.println("Difference: (" + (expectedCenterX - actualCenterX) + ", " + (expectedCenterY - actualCenterY) + ")");

    }
    public void setSprites(){}

    //---------------------------------------------------------------------------------------------------------------
    //                  Update Related
    //---------------------------------------------------------------------------------------------------------------

    //Core update logic - shouldnt be overwritten unless your making a bonkers spell
    public void update(){
        if(trigger){
            currentHitBox = "effect";
            effectFrameCounter++;
            effectLogic();
        }else{
            currentHitBox = "projectile";
            projectileFrameCounter++;
            projectileLogic();
        }
        spriteLogic();
        updateHitBox();
    }

    //used to update the sprite -- override if you need to make a bonkers spell but base code should work in most cases
    public void spriteLogic(){
        if(trigger){
            //Resets the sprite counter and sets it back to 0 once it reaches the increment amount
            if(effectSpriteCounter == effectSpriteIncrement){
                effectSpriteCounter = 0;
                currentEffectSprite++;
            }
            //Checks the current sprite index and resets it to 0 if it reaches the end the array size to prevent error
            if(currentEffectSprite == effectSprite.length-1){
                currentEffectSprite = 0;
            }
            //Upticks the counter
            effectSpriteCounter++;
        }else{
            if(projectileSpriteCounter == projectileSpriteIncrement){
                projectileSpriteCounter = 0;
                currentProjectileSprite++;
            }
            if(currentProjectileSprite == projectileSprite.length-1){
                currentProjectileSprite = 0;
            }
            projectileSpriteCounter++;
        }
    }

    //Override in each spell - projectile behavior
    public void projectileLogic(){}//Empty used to update logic for projectile IE in a new spell call travelStraightLine();

    //Override in each spell - once a spell hits something or ends its flight path, its turn on trigger, then this is called from update - controls effect logic(think karma q)
    public void effectLogic(){}//Custom effects for spells IE, createChildren(int amount,String type, int travelDistance);

    //---------------------------------------------------------------------------------------------------------------
    //                  Drawing or graphics Related
    //---------------------------------------------------------------------------------------------------------------

    //Core draw logic - Shouldnt update unless your making a bonkers spell
    public void draw(Graphics2D g2){
        getCurrentSprite();

        if(trigger){
            effectDraw(g2);
        }else{
            projectileDraw(g2);
        }
    }

    //sets the currentSprite to a sprite from either the projectile sprites or effect sprites depending on trigger state and spriteLogic() - shouldnt override unless making a bonkers spell
    public void getCurrentSprite(){
        //Empty, over
        if(trigger){
            currentSprite = effectSprite[currentEffectSprite];
        }else{
            currentSprite = projectileSprite[currentProjectileSprite];
        }
    }

    //Draws the projectile if trigger = false
    public void projectileDraw(Graphics2D g2){
        int screenX;
        int screenY;

        Circle temp;
        Rectangle emp;

        if(projectileHitBoxType.equals("circle")){
            screenX = circleHitBox.x - gp.player.worldX + gp.player.screenX;
            screenY = circleHitBox.y - gp.player.worldY + gp.player.screenY;

            //System.out.println("Drawing Projectile at: " + screenX + ", " + screenY);
            //System.out.println("Coord: " + coord.x + ", " + coord.y);
            //System.out.println("Hitbox: " + circleHitBox.x + ", " + circleHitBox.y);

            g2.setColor(Color.red);
            g2.setStroke(new BasicStroke(3));
            temp = new Circle(tempX,tempY, circleHitBox.radius);
            g2.draw(temp.getBounds());
            g2.setColor(Color.YELLOW);
            g2.draw(circleHitBox.getBounds());

            g2.drawImage(currentSprite,screenX,screenY,null);
        }else{
            //Center on coord
            //Draw on coord
            screenX = rectangleHitBox.x - gp.player.worldX + gp.player.screenX;
            screenY = rectangleHitBox.y - gp.player.worldY + gp.player.screenY;

            //System.out.println("Drawing Projectile at: " + screenX + ", " + screenY);
            //System.out.println("Coord: " + coord.x + ", " + coord.y);
            //System.out.println("Hitbox: " + rectangleHitBox.x + ", " + rectangleHitBox.y);

            g2.drawImage(currentSprite,screenX,screenY,null);

            emp = new Rectangle(screenX,screenY, rectangleHitBox.width,rectangleHitBox.height);
            g2.draw(emp.getBounds());
            g2.setColor(Color.YELLOW);
            g2.draw(rectangleHitBox.getBounds());

        }
    }

    //Draws the effect if trigger = true;
    public void effectDraw(Graphics2D g2){
        int screenX;
        int screenY;
        Circle temp;
        Rectangle emp;
        if(effectHitBoxType.equals("circle")){
            //adjust values for camera
            screenX = circleHitBox.x - gp.player.worldX + gp.player.screenX;
            screenY = circleHitBox.y - gp.player.worldY + gp.player.screenY;

            //System.out.println("Drawing Effect at: " + screenX + ", " + screenY);
            //System.out.println("Coord: " + coord.x + ", " + coord.y);
            //System.out.println("Hitbox: " + circleHitBox.x + ", " + circleHitBox.y);

            g2.drawImage(currentSprite, screenX, screenY, null);

            // Draw the circle's hitbox for debugging
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3));
            temp = new Circle(screenX + circleHitBox.radius, screenY + circleHitBox.radius, circleHitBox.radius);
            g2.draw(temp.getBounds());
        }else{
            //Adjust values for camera
            screenX = rectangleHitBox.x - gp.player.worldX + gp.player.screenX;
            screenY = rectangleHitBox.y - gp.player.worldY + gp.player.screenY;

            g2.drawImage(currentSprite,rectangleHitBox.x,rectangleHitBox.y,null);

            emp = new Rectangle(screenX,screenY, rectangleHitBox.width,rectangleHitBox.height);
            g2.draw(emp.getBounds());

        }
    }


    //---------------------------------------------------------------------------------------------------------------
    //                  Methods Called to affect behavior
    //---------------------------------------------------------------------------------------------------------------

    //Travels in a straight line between where the caster is coords.x coords.y and the target coords.targetX coords.targetY
    public void travelPathStraight(){

        // Calculate direction
        coord.dx = coord.targetX - coord.x;
        coord.dy = coord.targetY - coord.y;
        Double distance = Math.sqrt((coord.dx * coord.dx + coord.dy * coord.dy));

        if (distance > 0)
        {
            // Normalize direction and apply speed per frame
            int moveX = (int)Math.round((coord.dx / distance) * baseStats.projectileSpeed);
            int moveY = (int)Math.round((coord.dy / distance) * baseStats.projectileSpeed);

            // Update position
            //System.out.println(coord.x +" Should be ajusted by " + moveX + " making it " + (coord.x+moveX));
            coord.x += moveX;
            //System.out.println("actual " +  coord.x);
            coord.y += moveY;
            // Stop if close enough
            if (Math.abs(coord.targetX - coord.x) < baseStats.projectileSpeed && Math.abs(coord.targetY - coord.y) < baseStats.projectileSpeed)
            {
                coord.x = coord.targetX;
                coord.y = coord.targetY;
                trigger = true;//update trigger so it can apply end of travel logic aka effectLogic()
            }
            if(trigger){
                coord.endX = coord.x;
                coord.endY = coord.y;
            }
        }
    }

    //Spiral travel path for projectiles WIP
    public void travelPathSpiral(double spiralRadius, double spiralFrequency, double spiralSpeed, double maxSpiralRadius) {
        // Calculate direction
        coord.dx = coord.targetX - coord.x;
        coord.dy = coord.targetY - coord.y;
        double distance = Math.sqrt(coord.dx * coord.dx + coord.dy * coord.dy);

        if (distance > 0) {
            // Normalize direction
            double dirX = coord.dx / distance;
            double dirY = coord.dy / distance;

            // Increment progress along the path
            spiralProgress += baseStats.projectileSpeed;

            if (spiralProgress > distance) {
                spiralProgress = distance; // Stop at the target
            }

            // Compute base position along the straight path
            double baseX = coord.x + dirX * spiralProgress;
            double baseY = coord.y + dirY * spiralProgress;

            // Apply spiral effect using sine and cosine
            double angle = spiralProgress * spiralFrequency; // Control frequency of spiral (tightness)

            // Smoothly increase the radius over time or limit it to max radius
            double dynamicRadius = Math.min(spiralRadius + spiralSpeed * spiralProgress, maxSpiralRadius);

            // Perpendicular direction for spiral effect
            double perpX = -dirY * dynamicRadius * Math.cos(angle);
            double perpY = dirX * dynamicRadius * Math.sin(angle);

            // Update position with spiral effect
            coord.x = (int) Math.round(baseX + perpX);
            coord.y = (int) Math.round(baseY + perpY);

            // Stop if close enough to target
            if (Math.abs(coord.targetX - coord.x) < baseStats.projectileSpeed &&
                    Math.abs(coord.targetY - coord.y) < baseStats.projectileSpeed) {
                coord.x = coord.targetX;
                coord.y = coord.targetY;
                currentHitBox = "effect";
                trigger = true; // Update trigger for effect logic
            }

            if (trigger) {
                coord.endX = coord.x;
                coord.endY = coord.y;
            }
        }
    }

    //Returns the targeting object for each child spell
    public Coord[] getChildCoords(String dispersionType, int amount, int minDistance) {
        switch (dispersionType) {
            case "cone":
                return generateConeCoords(amount,minDistance,90);
            case "circle":
                return generateCircleCoords(amount, minDistance);
            case "horizontal":
                return generateHorizontalCoords(amount,minDistance);
            default:
                return new Coord[0]; // Return empty array if no valid type
        }
    }

    //Generate children spell targeting in a cone shape, away from where the caster cast the parent spell eg (caster....endpoint...child<) WIP
    public Coord[] generateConeCoords(int amount, int radius, double spreadAngle) {
        // Step size based on spread angle and number of projectiles
        double angleStep = spreadAngle / (amount - 1);  // Even spread of projectiles

        Coord[] coords = new Coord[amount];
        // Get the direction vector from the original coordinates
        double[] direction = coord.getDirectionVector();
        double dx = direction[0], dy = direction[1];

        // Loop through the number of projectiles
        for (int i = 0; i < amount; i++) {
            // Calculate the offset angle for this specific projectile
            double angleOffset = -spreadAngle / 2 + angleStep * i; // Spread angle from -spreadAngle/2 to +spreadAngle/2

            // Apply the angle offset to the original direction to form the cone
            double cosOffset = Math.cos(angleOffset); // X axis change based on angle
            double sinOffset = Math.sin(angleOffset); // Y axis change based on angle

            // Adjust the direction vector by the offset angle
            int childDx = (int) (dx * cosOffset - dy * sinOffset); // Modified direction for X
            int childDy = (int) (dx * sinOffset + dy * cosOffset); // Modified direction for Y

            // Apply the radius to scale the movement
            childDx = (int) (childDx * radius);
            childDy = (int) (childDy * radius);

            // Create the Coord object with the adjusted positions
            coords[i] = new Coord(coord.x + childDx, coord.y + childDy, coord.x + childDx * 2, coord.y + childDy * 2);
        }

        return coords;
    }

    //Generate children spell in a circle equal distance from eachother around where the parent spell was Functional
    public Coord[] generateCircleCoords(int amount, int radius) {
        double angleStep = (2 * Math.PI) / amount;  // Angle between each point
        Coord[] coords = new Coord[amount];         // Array to hold the coordinates

        for (int i = 0; i < amount; i++) {
            double angle = angleStep * i; // Calculate the angle for this point

            // Calculate the x and y coordinates based on the given radius
            int childDx = (int) Math.round(Math.cos(angle) * radius);
            int childDy = (int) Math.round(Math.sin(angle) * radius);

            // Create the Coord object with the calculated values and add it to the array
            coords[i] = new Coord(coord.x + childDx, coord.y + childDy, coord.x + childDx * 2, coord.y + childDy * 2);
        }

        return coords;
    }
    //WIP make children spell coordinates in a horizontal line from where the parent spell was
    public Coord[] generateHorizontalCoords(int amount, int radius) {
        Coord[] coords = new Coord[amount];

        // Horizontal spread: Moving along the X-axis with a fixed radius along Y
        for (int i = 0; i < amount; i++) {
            int offset = (i - amount / 2) * 10;  // Adjust the spacing between points

            // Horizontal offset will be spread along X
            int childDx = offset;
            int childDy = 0;  // No vertical movement

            coords[i] = new Coord(coord.x + childDx, coord.y + childDy, coord.x + childDx, coord.y + childDy);
        }

        return coords;
    }


    //---------------------------------------------------------------------------------------------------------------
    //                  DAMAGE RELATED
    //---------------------------------------------------------------------------------------------------------------

    //Multiplicitve
    //Sets spell power depending on its scaling type and % of scaling + baseDamage
    private int getSpellPower(){
        int power = baseStats.baseDamage;
        switch(baseStats.scalingType){
            case "intelligence": //based on entity intelligence
                power = (int)( baseStats.baseDamage * (1 + (caster.intelligence * baseStats.percentOfStat)));
                break;
            case "strength": //based on entity strength
                power = (int)( baseStats.baseDamage * (1 + (caster.strength * baseStats.percentOfStat)));
                break;
            case "dexterity": //based on entity dexterity
                power = (int)( baseStats.baseDamage * (1 + (caster.dexterity * baseStats.percentOfStat)));
                break;
            case "agility": //based on entity move speed
                power = (int)( baseStats.baseDamage * (1 + (caster.speed * baseStats.percentOfStat)));
                break;
            case "vitality": //based on entity health
                power = (int)( baseStats.baseDamage * (1 + (caster.maxLife * baseStats.percentOfStat)));
                break;
            case "defense": //based on entity defense
                power = (int)( baseStats.baseDamage * (1 + (caster.defense * baseStats.percentOfStat)));
                break;
        }
        return power;
    }

    //eventually will return damage dependent on different types
    public int getDamage(){
        int x = 0;
        switch(type){
            case "dot":
                x = dotDamage();
                break;
            case "basic":
                x = basicSpellDamage();
                break;
        }
        return x;
    }

    //Damage over time logic and return
    public int dotDamage(){
        int x = 0;
        if(lifeTime<=baseStats.damageOverTime){
            x = baseStats.damageOverTime;
            lifeTime++;
        }else{
            done = true;
        }
        return x;
    }

    //Pierce damage
    public int pierceDamage(){
        return (int)(getSpellPower()+(getSpellPower()*baseStats.pierceDamagePercent));
    }

    //Basic damage most used
    public int basicSpellDamage(){
        return getSpellPower();
    }

    //---------------------------------------------------------------------------------------------------------------
    //                  Called from outside spells, probably SpellHandler or Entity
    //---------------------------------------------------------------------------------------------------------------

    //replaces the coord object with the new one passed
    public void updateTracking(Coord coord){
        this.coord = coord;
    }

}
