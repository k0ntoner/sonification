package org.example.sonification.visual;

public class NoteModel {
    public final int pitch;
    public final int velocity;
    public final long startTime;
    public final long duration;
    public boolean played = false;

    public NoteModel(int pitch, int velocity, long startTime, long duration) {
        this.pitch = pitch;
        this.velocity = velocity;
        this.startTime = startTime;
        this.duration = duration;
    }

    public long endTime() {
        return startTime + duration;
    }
}
