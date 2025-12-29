package smartattendance.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class QRUtil {

    // ---------------------------------------------------
    // GENERATE QR CODE
    // ---------------------------------------------------
    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {

        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", new File(filePath).toPath());
    }

    // ---------------------------------------------------
    // READ QR FROM IMAGE FILE (optional)
    // ---------------------------------------------------
    public static String readQRCode(BufferedImage qrImage) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(qrImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------------------------------------------
    // MAT → FX IMAGE
    // ---------------------------------------------------
    public static Image mat2Image(Mat frame) {
        try {
            Mat converted = new Mat();
            Imgproc.cvtColor(frame, converted, Imgproc.COLOR_BGR2RGB);

            int width = converted.width();
            int height = converted.height();
            int channels = converted.channels();

            byte[] sourcePixels = new byte[width * height * channels];
            converted.get(0, 0, sourcePixels);

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            img.getRaster().setDataElements(0, 0, width, height, sourcePixels);

            return SwingFXUtils.toFXImage(img, null);

        } catch (Exception e) {
            System.out.println("mat2Image error: " + e.getMessage());
            return null;
        }
    }

    // ---------------------------------------------------
    // DETECT QR CODE FROM IMAGE
    // ---------------------------------------------------
    public static String detectQRCode(Image fxImage) {
        try {
            BufferedImage buffered = SwingFXUtils.fromFXImage(fxImage, null);

            LuminanceSource source = new BufferedImageLuminanceSource(buffered);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();

        } catch (Exception e) {
            return null;
        }
    }
}
