package liwwyy.sprintlock;

import liwwyy.sprintlock.config.SprintLockConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(
        modid = SprintLock.MODID,
        name = SprintLock.NAME,
        version = SprintLock.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class SprintLock {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    public static SprintLockConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new SprintLockConfig();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !SprintLockConfig.sprintLockEnabled) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null || minecraft.gameSettings == null || minecraft.thePlayer == null) {
            return;
        }

        KeyBinding sprint = minecraft.gameSettings.keyBindSprint;
        if (sprint != null && !sprint.isKeyDown()) {
            KeyBinding.setKeyBindState(sprint.getKeyCode(), true);
        }
    }
}
