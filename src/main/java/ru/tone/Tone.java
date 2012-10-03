package ru.tone;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * User: dima
 * Date: 03/10/2012
 */
public class Tone {

    public static void main(String[] args) throws LineUnavailableException {
        final AudioFormat af =
                new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, Note.SAMPLE_RATE);
        line.start();

        play(line, Note.A4, 2500);
//        play(line, new NoteFreq(), 2500);
//        play(line, Note.REST, 10);

/*
        for  (Note n : Note.values()) {
            play(line, n, 500);
            play(line, Note.REST, 10);
        }
*/
        line.drain();
        line.close();
    }

    private static void play(SourceDataLine line, Playable playable, int ms) {
        ms = Math.min(ms, Note.SECONDS * 1000);
        int length = Note.SAMPLE_RATE * ms / 1000;
        int count = line.write(playable.data(), 0, length);
    }
}

class NoteFreq implements Playable {
    private final byte[] sin;

    NoteFreq(int frequency) {
        sin = new byte[Note.SECONDS * frequency];

        for (int i = 0; i < sin.length; i++) {
            double f = 440d * Math.pow(2d, 1 - 1);
            double period = (double)frequency / f;
            double angle = 2.0 * Math.PI * i / period;
            sin[i] = (byte)(Math.sin(angle) * 127f);
        }
    }

    @Override public byte[] data() {
        return sin;
    }
}

interface Playable {
    byte[] data();
}

enum Note implements Playable {
    REST, A4, A4$, B4, C4, C4$, D4, D4$, E4, F4, F4$, G4, G4$, A5;
    public static final int SAMPLE_RATE = 16 * 1024; // ~16KHz
    public static final int SECONDS = 2;
    private byte[] sin = new byte[SECONDS * SAMPLE_RATE];

    Note() {
        int n = this.ordinal();
        if (n > 0) {
            double exp = ((double) n - 1) / 12d;
            double f = 440d * Math.pow(2d, exp);
            for (int i = 0; i < sin.length; i++) {
                double period = (double)SAMPLE_RATE / f;
                double angle = 2.0 * Math.PI * i / period;
                sin[i] = (byte)(Math.sin(angle) * 127f);
            }
        }
    }

    @Override
    public byte[] data() {
        return sin;
    }
}
