package org.example.sonification.midi;

import lombok.extern.slf4j.Slf4j;
import org.example.sonification.config.SonificationProperties;

import javax.sound.midi.*;

@Slf4j
public class MidiGenerator {

    private final Sequence sequence;
    private final Track track;
    private final SonificationProperties properties;

    public MidiGenerator(SonificationProperties properties) {
        try {
            this.sequence = new Sequence(Sequence.PPQ, 4);
            this.track = sequence.createTrack();
            this.properties = properties;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MIDI sequence", e);
        }
    }

    /**
     * Adds a single MIDI note to the track.
     *
     * @param pitch MIDI note number (0–127)
     * @param duration duration of the note in ticks
     * @param tick start time (position in track)
     */
    public void addNote(int pitch, int duration, int tick) {
        try {
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, properties.getChannel(), pitch, properties.getVelocityBase());
            track.add(new MidiEvent(noteOn, tick));

            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, properties.getChannel(), pitch, 0);
            track.add(new MidiEvent(noteOff, tick + duration));
        } catch (Exception e) {
            log.error("Failed to add note", e);
            throw new RuntimeException("Error adding note: " + pitch, e);
        }
    }

    /**
     * Saves the generated sequence into file.
     *
     * @param path output file path (e.g., "output.mid")
     */
    public void saveToFile(String path) {
        try {
            MidiSystem.write(sequence, 1, new java.io.File(path));
            log.info("MIDI file saved: {}", path);
        } catch (Exception e) {
            log.error("Error saving MIDI file: {}", path, e);
            throw new RuntimeException("Failed to save MIDI file", e);
        }
    }
}
