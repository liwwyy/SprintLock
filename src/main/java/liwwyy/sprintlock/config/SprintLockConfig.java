package liwwyy.sprintlock.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import liwwyy.sprintlock.SprintLock;

/** OneConfig settings for SprintLock. */
public class SprintLockConfig extends Config {
    @Switch(
            name = "Enable Sprint Lock",
            size = OptionSize.SINGLE
    )
    public static boolean sprintLockEnabled = true;

    public SprintLockConfig() {
        super(new Mod(SprintLock.NAME, ModType.UTIL_QOL), SprintLock.MODID + ".json");
        initialize();
    }
}
