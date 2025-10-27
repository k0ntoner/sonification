package org.example.sonification.service;

import lombok.AllArgsConstructor;
import org.example.sonification.config.SonificationProperties;
import org.example.sonification.strategy.MappingStrategy;
import org.example.sonification.midi.MidiGenerator;
import java.util.List;

@AllArgsConstructor
public class DataSonificationService {

    private final SonificationProperties properties;
    private final MappingStrategy pitchMapper;

    /**
     * Generates a MIDI file based on numeric data.
     * Each data value is converted into a note according to the mapping strategy.
     *
     * @param data list of integer values to sonify
     * @param filePath target file for output (.mid)
     */
    public void generateMidiFromData(List<Integer> data, String filePath) {
        MidiGenerator generator = new MidiGenerator(properties);
        for (int i = 0; i < data.size(); i++) {
            int pitch = pitchMapper.map(data.get(i));
            generator.addNote(pitch, properties.getNoteDuration(), i * properties.getNoteDuration());
        }
        generator.saveToFile(filePath);
    }
}
