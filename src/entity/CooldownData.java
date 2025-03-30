package entity;

import Assets.TagTracker;

public class CooldownData {
    int cooldown;
    TagTracker tag;

    public CooldownData(int Cooldown, TagTracker tag) {
        this.tag = tag;
        this.cooldown = Cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cd) {
        cooldown = cd;
    }

    public TagTracker getTag() {
        return tag;
    }

    public void updateCooldown() {
        cooldown--;
    }
}
