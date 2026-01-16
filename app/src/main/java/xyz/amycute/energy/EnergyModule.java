package xyz.amycute.energy;

import com.hypixel.hytale.component.ComponentRegistry;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import xyz.amycute.HytaleEnergyApi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyModule
{
    @Nullable
    private static EnergyModule INSTANCE;

    @Nonnull
    private final ComponentType<EntityStore, EnergyComponent> energyComponentType;

    private EnergyModule(@Nonnull ComponentRegistryProxy<EntityStore> registry)
    {
        this.energyComponentType = registry.registerComponent(
                EnergyComponent.class,
                "hytale:energy",
                EnergyComponent.CODEC
        );

        HytaleEnergyApi.getInstance().getLogger().atInfo().log("[Energy Module] Energy Component registered " + energyComponentType);
    }

    public ComponentType<EntityStore, EnergyComponent> getEnergyComponentType()
    {
        return energyComponentType;
    }

    public static void init(@Nonnull ComponentRegistryProxy<EntityStore> registry)
    {
        if(INSTANCE != null) throw new IllegalStateException("Energy Module is already initialized");
        INSTANCE = new EnergyModule(registry);
    }

    @Nonnull
    public static EnergyModule get()
    {
        if(INSTANCE == null) throw new IllegalStateException("Energy module is not initialized, try to call EnergyModule.init() before");
        return INSTANCE;
    }

    public static void reset()
    {
        INSTANCE = null;
    }
}
