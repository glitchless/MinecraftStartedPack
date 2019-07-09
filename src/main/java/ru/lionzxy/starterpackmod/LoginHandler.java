package ru.lionzxy.starterpackmod;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class LoginHandler {
    private List<ItemStack> starterItems;

    public LoginHandler(List<ItemStack> starterItems) {
        this.starterItems = new ArrayList<>(starterItems);
    }

    public void invalidate(List<ItemStack> starterItems) {
        this.starterItems = new ArrayList<>(starterItems);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final StarterPackWorldSavedData worldSavedData = StarterPackWorldSavedData.getInstance(event.player.world);
        if (worldSavedData.checkFirstLogin(event.player)) {
            for (ItemStack stack : starterItems) {
                event.player.inventory.addItemStackToInventory(stack.copy());
            }
            worldSavedData.markLogged(event.player);
            if (!TextUtils.isEmpty(StarterPack.welcomeMessage)) {
                event.player.sendMessage(new TextComponentString(StarterPack.welcomeMessage));
            }
        }
    }
}
