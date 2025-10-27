package org.example.sonification.config;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SonificationProperties {
    private final int tempo;
    private final int channel;
    private final int noteDuration;
    private final int velocityBase;

}
