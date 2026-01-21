package lol.vedant.neptunecore.module;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.cache.CacheModule;
import lol.vedant.neptunecore.module.chat.ChatModule;
import lol.vedant.neptunecore.module.maintenance.MaintenanceModule;
import lol.vedant.neptunecore.module.message.MessageModule;
import lol.vedant.neptunecore.module.spy.SpyModule;
import lol.vedant.neptunecore.module.staff.StaffModule;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ModuleManager {

    public final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        register(
                new ChatModule(),
                new MaintenanceModule(),
                new SpyModule(),
                new CacheModule(),
                new StaffModule(),
                new MessageModule()
        );
    }


    public void register(Module... m) {
        for (Module module : m) {
            modules.add(module);
            Logger.getLogger("Neptune Core").info("Loaded module: " + module.getName());
            module.enable();
        }
    }

    public void shutdown() {
        modules.forEach(Module::disable);
    }
}
