package com.mmocrpg.guild;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Guild {
    private final String id;
    private final String name;
    private final String tag;
    private final UUID owner;
    private final Map<UUID, GuildRank> members = new HashMap<>();
    private final Set<UUID> invites = new HashSet<>();
    private long createdAt;
    private long bank;

    public Guild(String id, String name, String tag, UUID owner) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.owner = owner;
        this.createdAt = System.currentTimeMillis();
        this.bank = 0;
        members.put(owner, GuildRank.MASTER);
    }

    public void addMember(UUID uuid) {
        members.put(uuid, GuildRank.RECRUIT);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
        invites.remove(uuid);
    }

    public void invite(UUID uuid) {
        invites.add(uuid);
    }

    public boolean hasInvite(UUID uuid) {
        return invites.contains(uuid);
    }

    public void acceptInvite(UUID uuid) {
        invites.remove(uuid);
        addMember(uuid);
    }

    public void setRank(UUID uuid, GuildRank rank) {
        members.put(uuid, rank);
    }

    public GuildRank getRank(UUID uuid) {
        return members.get(uuid);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getTag() { return tag; }
    public UUID getOwner() { return owner; }
    public Map<UUID, GuildRank> getMembers() { return members; }
    public int getMemberCount() { return members.size(); }
    public long getCreatedAt() { return createdAt; }
    public long getBank() { return bank; }
    public void addBank(long amount) { bank += amount; }
    public boolean withdrawBank(long amount) { if (bank >= amount) { bank -= amount; return true; } return false; }
    public Set<UUID> getInvites() { return invites; }
}