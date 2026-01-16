package xyz.amycute.energy;

public interface IEnergyStorage
{
    /**
     * @return current stored energy
     */
    long getEnergyStored();

    /**
     * @return maximum energy capacity
     */
    long getMaxEnergyStored();

    /**
     * @param amount   amount of energy to transfer
     * @param simulate if true, simulate the operation without modifying state
     * @return actual amount received (could be less if the storage is at maximum capacity)
     */
    long receiveEnergy(long amount, boolean simulate);

    /**
     * @param amount   amount of energy to transfer
     * @param simulate if true, simulate the operation without modifying state
     * @return actual amount extracted (could be less if not enough energy is available)
     */
    long extractEnergy(long amount, boolean simulate);


    default boolean canReceive()
    {
        return getEnergyStored() < getMaxEnergyStored();
    }

    default boolean canExtract()
    {
        return getEnergyStored() > 0;
    }


}
