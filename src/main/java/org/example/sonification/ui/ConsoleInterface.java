package org.example.sonification.ui;

import org.example.sonification.config.SonificationProperties;
import org.example.sonification.midi.MidiPlayer;
import org.example.sonification.service.DataSonificationService;
import org.example.sonification.strategy.MappingStrategy;

import java.util.*;

public class ConsoleInterface {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Starts the interactive CLI session.
     * Prompts the user to configure properties, data range, and values,
     * then generates and optionally plays the resulting MIDI file.
     */
    public void start() {
        System.out.println("Welcome to the Data Sonification System!");
        System.out.println("Please enter parameters for MIDI file generation:");

        int tempo = readInt("Tempo (BPM, e.g., 120): ");
        int channel = readInt("Channel (0–15): ");
        int duration = readInt("Note duration (e.g., 4): ");
        int velocity = readInt("Base velocity (0–127): ");

        SonificationProperties properties = new SonificationProperties(tempo, channel, duration, velocity);

        System.out.println("\n--- Note Range Configuration ---");
        int minPitch = readInt("Minimum pitch (e.g., 60 for C4): ");
        int maxPitch = readInt("Maximum pitch (e.g., 84 for C6): ");
        int maxValue = readInt("Maximum data value (e.g., 100): ");

        MappingStrategy mappingStrategy = new MappingStrategy(minPitch, maxPitch, maxValue);

        System.out.println("\nEnter numeric data separated by spaces:");
        String[] dataTokens = scanner.nextLine().trim().split("\\s+");
        List<Integer> data = new ArrayList<>();
        for (String token : dataTokens) {
            try {
                data.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                System.out.println("Skipped invalid value: " + token);
            }
        }

        DataSonificationService service = new DataSonificationService(properties, mappingStrategy);
        String filePath = "output.mid";
        service.generateMidiFromData(data, filePath);

        System.out.println("File saved successfully: " + filePath);

        System.out.print("Would you like to play it now? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (answer.equals("y") || answer.equals("yes")) {
            MidiPlayer.playMidiFile(filePath);
        } else {
            System.out.println("You can open the file in any MIDI player.");
        }

        System.out.println("Thank you for using the Data Sonification System!");
    }

    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
}
