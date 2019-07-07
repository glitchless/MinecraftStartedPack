package ru.lionzxy.startedpackmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StartedPackWorldSavedData extends WorldSavedData {
    private static final String DATA_NAME = StartedPackMod.MODID + "_startpack";
    private static final String KEY_NBTLIST = "knownedPlayers";
    private final Set<String> knowedPlayers = new HashSet<>();

    public StartedPackWorldSavedData() {
        super(DATA_NAME);
    }

    public StartedPackWorldSavedData(String name) {
        super(name);
    }

    public boolean checkFirstLogin(EntityPlayer entityPlayer) {
        String key = entityPlayer.getGameProfile().getId().toString();
        return !knowedPlayers.contains(key);
    }

    public void markLogged(EntityPlayer entityPlayer) {
        String key = entityPlayer.getGameProfile().getId().toString();
        knowedPlayers.add(key);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey(KEY_NBTLIST)) {
            return;
        }
        final NBTTagList nbtTagList = (NBTTagList) nbt.getTag(KEY_NBTLIST);
        for (Iterator<NBTBase> it = nbtTagList.iterator(); it.hasNext(); ) {
            NBTTagString base = (NBTTagString) it.next();
            knowedPlayers.add(base.getString());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        final NBTTagList nbtTagList = new NBTTagList();
        for (String playerId : knowedPlayers) {
            nbtTagList.appendTag(new NBTTagString(playerId));
        }
        compound.setTag(KEY_NBTLIST, nbtTagList);
        return compound;
    }

    public static StartedPackWorldSavedData getInstance(World world) {
        final MapStorage mapStorage = world.getMapStorage();
        StartedPackWorldSavedData instance = (StartedPackWorldSavedData) mapStorage.getOrLoadData(StartedPackWorldSavedData.class, DATA_NAME);

        if (instance == null) {
            instance = new StartedPackWorldSavedData();
            mapStorage.setData(DATA_NAME, instance);
        }
        return instance;
    }
}
