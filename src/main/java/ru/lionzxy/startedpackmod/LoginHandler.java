package ru.lionzxy.startedpackmod;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class LoginHandler {
    private List<ItemStack> startedItems;

    public LoginHandler(List<ItemStack> startedItems) {
        this.startedItems = new ArrayList<>(startedItems);
    }

    public void invalidate(List<ItemStack> startedItems) {
        this.startedItems = new ArrayList<>(startedItems);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final StartedPackWorldSavedData worldSavedData = StartedPackWorldSavedData.getInstance(event.player.world);
        if (worldSavedData.checkFirstLogin(event.player)) {
            for (ItemStack stack : startedItems) {
                event.player.inventory.addItemStackToInventory(stack.copy());
            }
            worldSavedData.markLogged(event.player);
            if (!TextUtils.isEmpty(StartedPack.welcomeMessage)) {
                event.player.sendMessage(new TextComponentString(StartedPack.welcomeMessage));
            }
        }
    }
}
