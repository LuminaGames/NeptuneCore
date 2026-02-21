package lol.vedant.core.module;

public interface ModuleManager {

    //Register models
    void register(Module... m);

    //Shutdown all models
    void shutdown();

}
