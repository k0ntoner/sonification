package org.example.sonification.visual;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class MidiVisualizer extends JPanel {

    private final List<NoteModel> notes = new CopyOnWriteArrayList<>();
    private final long startTime;
    private final int height;
    private NotePlayListener listener;

    public MidiVisualizer(int height) {
        this.height = height;
        setPreferredSize(new Dimension(900, height));
        setBackground(Color.BLACK);
        startTime = System.currentTimeMillis();
        new Timer(16, e -> repaint()).start();
    }

    public void addNote(NoteModel note) {
        notes.add(note);
    }

    public void setNotePlayListener(NotePlayListener listener) {
        this.listener = listener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long now = System.currentTimeMillis();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(0, 0, new Color(15, 10, 40),
                0, height, new Color(40, 15, 60));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(255, 255, 255, 50));
        int lineY = height - 30;
        g2.fillRect(0, lineY, getWidth(), 4);

        for (NoteModel note : notes) {
            long elapsed = now - note.startTime;
            int y = (int) (elapsed * 0.25);
            int noteHeight = 20;
            int noteWidth = 18;
            int x = (note.pitch - 40) * noteWidth % getWidth();


            if (!note.played && y >= lineY - noteHeight) {
                note.played = true;
                if (listener != null) listener.onNotePlay(note);
            }

            if (y > height + 100) {
                notes.remove(note);
                continue;
            }

            float hue = (note.pitch % 12) / 12f;
            g2.setColor(Color.getHSBColor(hue, 1f, 1f));
            g2.fillRect(x, y, noteWidth, noteHeight);
        }
    }

    public static MidiVisualizer createWindow() {
        JFrame frame = new JFrame("Piano Roll Visualizer");
        MidiVisualizer panel = new MidiVisualizer(600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        return panel;
    }

    public interface NotePlayListener {
        void onNotePlay(NoteModel note);
    }
}
