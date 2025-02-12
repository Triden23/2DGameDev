package Magic;

public class Enhancements {
    //--USED ONLY IN CHILDREN OF SPELL AND HAVE TO BE USED--
    public float percentOfStat = 0;
    public int baseDamage = 0;
    String scalingType = "";
    //HitBox's
    public int projectileSizeX = 0;
    public int projectileSizeY = 0;
    public int effectSizeX = 0;
    public int effectSizeY = 0;

    //------------------------------------------------------------------
    // Core Enhancements
    public float manaCostReduction = 0.0f;// percent of mana reduction
    public int manaCost = 0;
    public float healthCostReduction = 0.0f;
    public int healthCost = 0;
    public float cooldownReduction = 0.0f;// percent of cool down reduction
    public int cooldown = 0;
    public float castSpeedReduction = 0.0f;// percent of how much faster it is to cast if the spell has an animation
    public int castSpeed = 0;
    public float spellPowerBuff = 0.0f;// percent of how much extra damage you do with spells
    public int spellPower = 0;
    public float durationBuff = 0.0f;// percent of how long buff/debuff spells last or until a spell fizzles
    public int duration = 0;
    public float rangeBuff = 0.0f;// increases travel distance
    public int range = 0; //How far I want it to go in units(pixels)



    // Projectile-Based Enhancements
    public float projectileSpeedBuff = 0.0f; // How much faster a spell travels %
    public int projectileSpeed = 0;
    public float projectileSizeBuff = 0.0f; // How much bigger a projectile is

    public int projectileCountBuff = 0; // Amount of projectile increase
    public int projectileCount = 0;
    public float pierceDamagePercent = 0.5f;
    public float chanceToPierceBuff = 0.0f; // % Chance for non-piercing spells to pierce
    public float chanceToPierce = 0.0f;
    public int amountOfPierceBuff = 0; // amount of entities/terrain it can pierce
    public int amountOfPierce = 0;

    // Area of Effect (AoE) Enhancements
    public float aoeRadiusBuff = 0.0f; // % increase to the size of a AOE
    public int aoeRadius = 0;
    public float damageOverTimeBuff = 0.0f; // % increase to aoe dots damage
    public int damageOverTime = 0;
    public float damageOverTimeDurationBuff = 0;
    public int damageOverTimeDuration = 0;

    // Utility & Status Effects
    public float knockbackStrengthBuff = 0.0f; // increase the amount of knock back
    public float knockbackStrength = 0.0f;
    public float slowEffectBuff = 0.0f; // increase to the amount of slowing
    public float slowEffect = 0.0f;
    public float critChanceBuff = 0.0f; // % chance for a spell to crit
    public float critChance = 0.0f;
    public float homingChanceBuff = 0.0f; // % chance for a spell to home;
    public float homingChance = 0.0f;

}
