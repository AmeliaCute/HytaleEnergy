package xyz.amycute.energy;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validator;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyComponent implements Component<EntityStore>, IEnergyStorage
{
    private long energyStored;
    private long maxEnergy;

    @Nonnull
    public static final BuilderCodec<EnergyComponent> CODEC =
        BuilderCodec.builder(EnergyComponent.class, EnergyComponent::new)
            .append(new KeyedCodec<>("EnergyStored", Codec.LONG),
                    (c, v) -> c.energyStored = v, c -> c.energyStored)
            .addValidator(Validators.greaterThanOrEqual(0L))
            .documentation("Current quantity of energy stored")
            .add()
            .append(new KeyedCodec<>("MaxEnergy", Codec.LONG),
                    (c, v) -> c.maxEnergy = v, c -> c.maxEnergy)
            .addValidator(Validators.greaterThan(0L))
            .documentation("Maximum quantity of energy stored")
            .add()
            .build();

    @Nonnull
    public static ComponentType<EntityStore, EnergyComponent> getComponentType()
    {
        return EnergyModule.get().getEnergyComponentType();
    }

    public EnergyComponent(long energyStored, long maxEnergy)
    {
        if(maxEnergy <= 0) throw new IllegalArgumentException("maxEnergy should be above 0");
        if(energyStored < 0) throw new IllegalArgumentException("energyStored should be above or equals to 0");

        this.energyStored = Math.min(energyStored, maxEnergy);;
        this.maxEnergy = maxEnergy;
    }

    public EnergyComponent()
    {
        this(0L, 10000L);
    }

    public EnergyComponent(long maxEnergy)
    {
        this(0L, maxEnergy);
    }

    @Override
    public long getEnergyStored()
    {
        return energyStored;
    }

    @Override
    public long getMaxEnergyStored()
    {
        return maxEnergy;
    }

    @Override
    public long receiveEnergy(long amount, boolean simulate) {
        if(amount <= 0) return 0L;
        long energyReceived = Math.min(maxEnergy - energyStored, amount);
        if(!simulate && energyReceived > 0) energyStored += energyReceived;

        return energyReceived;
    }

    @Override
    public long extractEnergy(long amount, boolean simulate) {
        if(amount <= 0) return 0L;
        long energyExtracted = Math.min(energyStored, amount);
        if(!simulate && energyExtracted > 0) energyStored -= energyExtracted;

        return energyExtracted;
    }

    public void setEnergyStored(long amount)
    {
        this.energyStored = Math.max(0L, Math.min(amount, maxEnergy));
    }

    public void setMaxEnergy(long amount)
    {
        if(maxEnergy <= 0) throw new IllegalArgumentException("amount should be above 0");

        this.maxEnergy = amount;
        this.energyStored = Math.max(this.energyStored, amount);
    }

    /**
     * @return a value between 0.0 and 1.0 (50% = 0.5)
     */
    public float getFillRatio()
    {
        return (float) energyStored / (float) maxEnergy;
    }

    public boolean isFull()
    {
        return energyStored >= maxEnergy;
    }

    public boolean isEmpty()
    {
        return energyStored <= 0;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone()
    {
        return new EnergyComponent(this.energyStored, this.maxEnergy);
    }

    @Override
    public String toString()
    {
        return String.format("EnergyComponent{%d/%d HE (%.3f%%)}", energyStored, maxEnergy, getFillRatio() * 100);
    }
}
