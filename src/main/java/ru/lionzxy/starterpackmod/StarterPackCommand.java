package ru.lionzxy.starterpackmod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

public class StarterPackCommand extends CommandBase {
    @Override
    public String getName() {
        return "starterpack";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/starterpack hand";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1 || !args[0].equals("hand")) {
            throw new WrongUsageException(this.getUsage(sender));
        }

        if (!(sender instanceof EntityPlayer)) {
            throw new WrongUsageException("This command can use only player");
        }

        final ItemStack itemStack = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);

        if (itemStack.isEmpty()) {
            throw new WrongUsageException("In hand must be item");
        }

        final String itemName = itemStack.getItem().getRegistryName() + ":" + itemStack.getMetadata();
        final String finalString = itemName + ", " + itemStack.getCount();
        StarterPack.starterItems = addToArray(StarterPack.starterItems, finalString);
        final StringBuilder allItems = new StringBuilder();
        for (String item : StarterPack.starterItems) {
            allItems.append("- ").append(item).append('\n');
        }
        sender.sendMessage(new TextComponentString("Add item \"" + finalString + "\" to starter pack! All list: \n"
                + allItems.toString()));
        StarterPackMod.getInstance().invalidate();
    }

    private static String[] addToArray(String[] array, String item) {
        final String[] toReturn = new String[array.length + 1];
        System.arraycopy(array, 0, toReturn, 0, array.length);
        toReturn[array.length] = item;
        return toReturn;
    }
}
