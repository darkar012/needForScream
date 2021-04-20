package com.example.needforscreamcliente;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class SoundMeter {
    private AudioRecord ar = null;
    private int minSize;

    //Inicia la grabación de sonido. Configura la entrada de sonido.
    public void start() {
        minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
        ar.startRecording();
    }

    //Detiene la grabación de sonido.
    public void stop() {
        if (ar != null) {
            ar.stop();
        }
    }

    //Calcula la amplitud de la onda de sonido que entra por el micrófono.
    public double getAmplitude() {
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);
        int max = 0;
        for (int i = 0; i < buffer.length; i++) {
            short value = buffer[i];
            int absValue = Math.abs(buffer[i]);
            if (absValue > max) {
                max = Math.abs(value);
            }
        }
        return max;
    }
}
