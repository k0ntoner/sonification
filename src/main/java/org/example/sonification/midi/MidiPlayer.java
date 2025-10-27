package org.example.sonification.midi;

import lombok.extern.slf4j.Slf4j;
import org.example.sonification.visual.MidiVisualizer;
import org.example.sonification.visual.NoteModel;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MidiPlayer {

    public static void playMidiFile(String filePath) {
        try {
            File midiFile = new File(filePath);
            if (!midiFile.exists()) {
                log.error("MIDI file not found: {}", filePath);
                return;
            }

            Sequence sequence = MidiSystem.getSequence(midiFile);
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            Receiver synthReceiver = synth.getReceiver();

            List<NoteModel> notes = new ArrayList<>();
            long tickLength = sequence.getTickLength();
            double microPerTick = 500000.0 / sequence.getResolution();

            for (Track track : sequence.getTracks()) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();
                    if (message instanceof ShortMessage msg) {
                        if (msg.getCommand() == ShortMessage.NOTE_ON && msg.getData2() > 0) {
                            long startTime = (long) (event.getTick() * microPerTick / 1000);
                            long duration = 400;
                            notes.add(new NoteModel(msg.getData1(), msg.getData2(), System.currentTimeMillis() + startTime, duration));
                        }
                    }
                }
            }

            MidiVisualizer visualizer = MidiVisualizer.createWindow();

            visualizer.setNotePlayListener(note -> {
                try {
                    ShortMessage sm = new ShortMessage();
                    sm.setMessage(ShortMessage.NOTE_ON, 0, note.pitch, note.velocity);
                    synthReceiver.send(sm, -1);

                    new Thread(() -> {
                        try {
                            Thread.sleep(note.duration);
                            ShortMessage off = new ShortMessage();
                            off.setMessage(ShortMessage.NOTE_OFF, 0, note.pitch, 0);
                            synthReceiver.send(off, -1);
                        } catch (Exception ignored) {}
                    }).start();

                } catch (InvalidMidiDataException e) {
                    log.error("Failed to play note {}", note.pitch, e);
                }
            });

            for (NoteModel n : notes) {
                visualizer.addNote(n);
            }

            log.info("Piano roll playback started.");

        } catch (Exception e) {
            log.error("Error during MIDI visualization playback", e);
        }
    }
}
