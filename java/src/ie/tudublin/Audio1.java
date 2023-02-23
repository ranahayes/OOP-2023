package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Audio1 extends PApplet {
    Minim minim;
    AudioPlayer ap;
    AudioBuffer ab;

    int mode = 0;

    float smoothedAmplitude = 0;

    public void keyPressed() {
        if (key >= '0' && key <= '5') {
            mode = key - '0';
        }
        if (keyCode == ' ') {
            if (ap.isPlaying()) {
                ap.pause();
            } else {
                ap.rewind();
                ap.play();
            }
        }
    }

    public void settings() {
        size(1024, 1000, P3D);
    }

    public void setup() {
        minim = new Minim(this);
        ap = minim.loadFile("heroplanet.mp3", 1024);
        ap.play();
        ab = ap.mix;
        colorMode(HSB);

        // Initialize the array to the same size as the audio buffer
        lerpedBuffer = new float[ab.size()];

        for (int i = 0; i < ab.size(); i++) {
            lerpedBuffer[i] = 0;
        }
    }

    float[] lerpedBuffer;

    public void draw() {
        float halfH = height / 2;
        float average = 0;
        float sum = 0;

        // Calculate sum and average of the samples
        // Also lerp each element of buffer;
        for (int i = 0; i < ab.size(); i++) {
            lerpedBuffer[i] = lerp(lerpedBuffer[i], ab.get(i), 0.1f);
            sum += abs(lerpedBuffer[i]);
        }
        average = sum / (float) ab.size();

        smoothedAmplitude = lerp(smoothedAmplitude, average, 0.5f);

        switch (mode) {
            case 0:
                background(0);
                for (int i = 0; i < ab.size(); i++) {
                    float c = map(i, 0, ab.size(), 0, 255);
                    stroke(c, 255, 255);
                    float f = lerpedBuffer[i] * halfH;
                    line(i, halfH + f, i, halfH - f);
                }
                break;
            case 1:
                background(0);
                float x = 0;
                float dx = (float) width / (float) ab.size();
                for (int i = 0; i < ab.size(); i++) {
                    float y = map(ab.get(i), -1, 1, height, 0);
                    line(x, y, x + dx, y);
                    x += dx;
                }
                break;
        }
    }
}
