package smartattendance.qr;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.QRCodeDetector;

public class QRScanner {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private boolean scanning = false;

    public String scanAndGetUsername() {

        VideoCapture camera = new VideoCapture(0); 
        if (!camera.isOpened()) {
            System.out.println("❌ Camera not detected!");
            return null;
        }

        scanning = true;
        Mat frame = new Mat();
        QRCodeDetector detector = new QRCodeDetector();

        try {
            while (scanning) {

                if (!camera.read(frame)) continue;

                String decodedText = detector.detectAndDecode(frame);

                if (decodedText != null && !decodedText.isEmpty()) {

                    scanning = false;
                    camera.release();

                    System.out.println("QR Raw Text = " + decodedText);

                   
                    if (decodedText.startsWith("USER:")) {
                        return decodedText.substring(5); 
                    }

                    return decodedText;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (camera.isOpened()) camera.release();
        }

        return null;
    }

   
    public void stopScanning() {
        scanning = false;
    }
}
