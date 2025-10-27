package org.example.sonification.strategy;

public class MappingStrategy {

    private final int minPitch;
    private final int maxPitch;
    private final int maxValue;

    public MappingStrategy(int minPitch, int maxPitch, int maxValue) {
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        this.maxValue = maxValue;
    }

    /**
     * Converts a numeric value to a corresponding MIDI note number.
     * @param value input numeric value (e.g., 0–maxValue)
     * @return mapped pitch number (MIDI note 0–127)
     */
    public int map(int value) {
        double ratio = Math.min(1.0, Math.max(0, value / (double) maxValue));
        return (int) (minPitch + ratio * (maxPitch - minPitch));
    }
}
