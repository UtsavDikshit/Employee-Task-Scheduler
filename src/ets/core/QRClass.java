package ets.core;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.Exchanger;
import javax.swing.JPanel;

public class QRClass implements AutoCloseable{

    public QRClass(JPanel panel) {

        camera = Webcam.getDefault();
        camera.setViewSize(new Dimension(320, 240));
        camera.open();

        panel.add(new WebcamPanel(camera));
        panel.revalidate();
        panel.invalidate();
        panel.repaint();

        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (panel.isVisible()) {
                    readCamera();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private BufferedImage image = null;
    private final Exchanger<String> exchanger = new Exchanger<String>();
    private Result result = null;
    private Webcam camera = null;

    private void readCamera() {

        if (!camera.isOpen()) {
            return;
        }
        if ((image = camera.getImage()) == null) {
            return;
        }

        try {
            result = new MultiFormatReader().decode(toBinaryBitmap(image));
        } catch (NotFoundException e) {
            return;
        }

        if (result != null) {
            try {
                exchanger.exchange(result.getText());
            } catch (InterruptedException e) {
                return;
            } finally {
                camera.close();
            }
        }
    }

    private static BinaryBitmap toBinaryBitmap(BufferedImage image) {
        return new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
    }

    public String getResult() throws InterruptedException {
        return exchanger.exchange(null);
    }

    @Override
    public void close() {
        camera.close();
    }

}
