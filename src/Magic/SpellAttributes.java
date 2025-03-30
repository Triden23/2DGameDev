package Magic;

import java.util.EnumMap;
import java.util.Map;

public class SpellAttributes {


    // Use a map to store only necessary values
    private final Map<EnhancementType, Float> floatValues = new EnumMap<>(EnhancementType.class);
    private final Map<EnhancementType, Integer> intValues = new EnumMap<>(EnhancementType.class);
    private String scalingType = "";

    private void setFloat(EnhancementType type, float value) {
        floatValues.put(type, value);
    }

    private float getFloat(EnhancementType type) {
        return floatValues.getOrDefault(type, 0.0f);
    }

    private void setInt(EnhancementType type, int value) {
        intValues.put(type, value);
    }

    private int getInt(EnhancementType type) {
        return intValues.getOrDefault(type, 0);
    }

    public String getScalingType() {
        return this.scalingType;
    }

    public void setScalingType(String type) {
        this.scalingType = type;
    }

    public float getKnockbackStrength() {
        return getFloat(EnhancementType.KNOCKBACK_STRENGTH);
    }

    public void setKnockbackStrength(float value) {
        setFloat(EnhancementType.KNOCKBACK_STRENGTH, value);
    }

    public float getSlowEffect() {
        return getFloat(EnhancementType.SLOW_EFFECT);
    }

    public void setSlowEffect(float value) {
        setFloat(EnhancementType.SLOW_EFFECT, value);
    }

    public float getCritChance() {
        return getFloat(EnhancementType.CRIT_CHANCE);
    }

    public void setCritChance(float value) {
        setFloat(EnhancementType.CRIT_CHANCE, value);
    }

    public float getHomingChance() {
        return getFloat(EnhancementType.HOMING_CHANCE);
    }

    public void setHomingChance(float value) {
        setFloat(EnhancementType.HOMING_CHANCE, value);
    }

    public int getDamageOverTime() {
        return getInt(EnhancementType.DAMAGE_OVER_TIME);
    }

    public void setDamageOverTime(int value) {
        setInt(EnhancementType.DAMAGE_OVER_TIME, value);
    }

    public float getChanceToPierce() {
        return getFloat(EnhancementType.CHANCE_TO_PIERCE);
    }

    public void setChanceToPierce(float value) {
        setFloat(EnhancementType.CHANCE_TO_PIERCE, value);
    }

    public int getAmountOfPierce() {
        return getInt(EnhancementType.AMOUNT_OF_PIERCE);
    }

    public void setAmountOfPierce(int value) {
        setInt(EnhancementType.AMOUNT_OF_PIERCE, value);
    }

    public int getAoeRadius() {
        return getInt(EnhancementType.AOE_RADIUS);
    }

    public void setAoeRadius(int value) {
        setInt(EnhancementType.AOE_RADIUS, value);
    }

    public int getProjectileCount() {
        return getInt(EnhancementType.PROJECTILE_COUNT);
    }

    public void setProjectileCount(int value) {
        setInt(EnhancementType.PROJECTILE_COUNT, value);
    }

    public int getProjectileSpeed() {
        return getInt(EnhancementType.PROJECTILE_SPEED);
    }

    public void setProjectileSpeed(int value) {
        setInt(EnhancementType.PROJECTILE_SPEED, value);
    }

    public float getPierceDamagePercent() {
        return getFloat(EnhancementType.PIERCE_DAMAGE_PERCENT);
    }

    public void setPierceDamagePercent(float value) {
        setFloat(EnhancementType.PIERCE_DAMAGE_PERCENT, value);
    }

    public int getSpellPower() {
        return getInt(EnhancementType.SPELL_POWER);
    }

    public void setSpellPower(int value) {
        setInt(EnhancementType.SPELL_POWER, value);
    }

    public int getCooldown() {
        return getInt(EnhancementType.COOLDOWN);
    }

    public void setCooldown(int value) {
        setInt(EnhancementType.COOLDOWN, value);
    }

    public int getDuration() {
        return getInt(EnhancementType.DURATION);
    }

    public void setDuration(int value) {
        setInt(EnhancementType.DURATION, value);
    }

    public int getRange() {
        return getInt(EnhancementType.RANGE);
    }

    public void setRange(int value) {
        setInt(EnhancementType.RANGE, value);
    }

    public int getCastSpeed() {
        return getInt(EnhancementType.CAST_SPEED);
    }

    public void setCastSpeed(int value) {
        setInt(EnhancementType.CAST_SPEED, value);
    }

    public int getHealthCost() {
        return getInt(EnhancementType.HEALTH_COST);
    }

    public void setHealthCost(int value) {
        setInt(EnhancementType.HEALTH_COST, value);
    }

    public int getManaCost() {
        return getInt(EnhancementType.MANA_COST);
    }

    public void setManaCost(int value) {
        setInt(EnhancementType.MANA_COST, value);
    }

    public float getPercentOfStat() {
        return getFloat(EnhancementType.PERCENT_OF_STAT);
    }

    public void setPercentOfStat(float value) {
        setFloat(EnhancementType.PERCENT_OF_STAT, value);
    }

    public int getBaseDamage() {
        return getInt(EnhancementType.BASE_DAMAGE);
    }

    public void setBaseDamage(int value) {
        setInt(EnhancementType.BASE_DAMAGE, value);
    }

    public int getProjectileSizeX() {
        return getInt(EnhancementType.PROJECTILE_SIZE_X);
    }

    public void setProjectileSizeX(int value) {
        setInt(EnhancementType.PROJECTILE_SIZE_X, value);
    }

    public int getProjectileSizeY() {
        return getInt(EnhancementType.PROJECTILE_SIZE_Y);
    }

    public void setProjectileSizeY(int value) {
        setInt(EnhancementType.PROJECTILE_SIZE_Y, value);
    }

    public int getEffectSizeX() {
        return getInt(EnhancementType.EFFECT_SIZE_X);
    }

    public void setEffectSizeX(int value) {
        setInt(EnhancementType.EFFECT_SIZE_X, value);
    }

    public int getEffectSizeY() {
        return getInt(EnhancementType.EFFECT_SIZE_Y);
    }

    public void setEffectSizeY(int value) {
        setInt(EnhancementType.EFFECT_SIZE_Y, value);
    }

    // Define categories of enhancements
    public enum EnhancementType {
        KNOCKBACK_STRENGTH,
        SLOW_EFFECT,
        CRIT_CHANCE,
        HOMING_CHANCE,
        DAMAGE_OVER_TIME,
        CHANCE_TO_PIERCE,
        AMOUNT_OF_PIERCE,
        AOE_RADIUS,
        PROJECTILE_COUNT,
        PROJECTILE_SPEED,
        PIERCE_DAMAGE_PERCENT,
        SPELL_POWER,
        COOLDOWN,
        DURATION,
        RANGE,
        CAST_SPEED,
        HEALTH_COST,
        MANA_COST,
        PERCENT_OF_STAT,
        BASE_DAMAGE,
        PROJECTILE_SIZE_X,
        PROJECTILE_SIZE_Y,
        EFFECT_SIZE_X,
        EFFECT_SIZE_Y
    }

}
