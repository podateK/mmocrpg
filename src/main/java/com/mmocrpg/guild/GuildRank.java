package com.mmocrpg.guild;

public enum GuildRank {
    MASTER("Guild Master", 5, true, true, true, true),
    OFFICER("Officer", 4, true, true, true, false),
    VETERAN("Veteran", 3, true, true, false, false),
    MEMBER("Member", 2, true, false, false, false),
    RECRUIT("Recruit", 1, false, false, false, false);

    private final String displayName;
    private final int hierarchy;
    private final boolean canInvite;
    private final boolean canKick;
    private final boolean canPromote;
    private final boolean canManageBank;

    GuildRank(String displayName, int hierarchy, boolean canInvite, boolean canKick, boolean canPromote, boolean canManageBank) {
        this.displayName = displayName;
        this.hierarchy = hierarchy;
        this.canInvite = canInvite;
        this.canKick = canKick;
        this.canPromote = canPromote;
        this.canManageBank = canManageBank;
    }

    public String getDisplayName() { return displayName; }
    public int getHierarchy() { return hierarchy; }
    public boolean canInvite() { return canInvite; }
    public boolean canKick() { return canKick; }
    public boolean canPromote() { return canPromote; }
    public boolean canManageBank() { return canManageBank; }

    public boolean isHigherThan(GuildRank other) {
        return hierarchy > other.hierarchy;
    }
}