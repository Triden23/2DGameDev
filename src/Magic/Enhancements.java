package Magic;

public class Enhancements {


    //------------------------------------------------------------------
    // Core Enhancements
    public float manaCostReduction = 0.0f;// percent of mana reduction

    public float healthCostReduction = 0.0f;

    public float cooldownReduction = 0.0f;// percent of cool down reduction

    public float castSpeedReduction = 0.0f;// percent of how much faster it is to cast if the spell has an animation

    public float spellPowerBuff = 0.0f;// percent of how much extra damage you do with spells

    public float durationBuff = 0.0f;// percent of how long buff/debuff spells last or until a spell fizzles

    public float rangeBuff = 0.0f;// increases travel distance




    // Projectile-Based Enhancements
    public float projectileSpeedBuff = 0.0f; // How much faster a spell travels %

    public float projectileSizeBuff = 0.0f; // How much bigger a projectile is
    public int projectileCountBuff = 0; // Amount of projectile increase

    public float chanceToPierceBuff = 0.0f; // % Chance for non-piercing spells to pierce
    public int amountOfPierceBuff = 0; // amount of entities/terrain it can pierce


    // Area of Effect (AoE) Enhancements
    public float aoeRadiusBuff = 0.0f; // % increase to the size of a AOE
    public float damageOverTimeBuff = 0.0f; // % increase to aoe dots damage
    public float damageOverTimeDurationBuff = 0;
    public int damageOverTimeDuration = 0;

    // Utility & Status Effects
    public float knockbackStrengthBuff = 0.0f; // increase the amount of knock back
    public float slowEffectBuff = 0.0f; // increase to the amount of slowing
    public float critChanceBuff = 0.0f; // % chance for a spell to crit
    public float homingChanceBuff = 0.0f; // % chance for a spell to home;


    //Player stats

    //Spell stats
    public float knockbackStrength = 0.0f;
    public float slowEffect = 0.0f;
    public float critChance = 0.0f;
    public float homingChance = 0.0f;
    public int damageOverTime = 0;
    public float chanceToPierce = 0.0f;
    public int amountOfPierce = 0;
    public int aoeRadius = 0;
    public int projectileCount = 0;
    public int projectileSpeed = 0;
    public float pierceDamagePercent = 0.5f;
    public int spellPower = 0;
    public int cooldown = 0;
    public int duration = 0;
    public int range = 0; //How far I want it to go in units(pixels)
    public int castSpeed = 0;
    public int healthCost = 0;
    public int manaCost = 0;

    public float percentOfStat = 0;
    public int baseDamage = 0;
    String scalingType = "";

    public int projectileSizeX = 0;
    public int projectileSizeY = 0;
    public int effectSizeX = 0;
    public int effectSizeY = 0;
}
