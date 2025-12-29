package smartattendance.qr;

import com.google.zxing.WriterException;
import smartattendance.util.QRUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class QRGenerator {

    	public static void generateUserQR(String userId, String saveDir) {
        String qrText = "USER:" + userId; // Simple format, can add more info if needed
        String filePath = saveDir + File.separator + userId + "_qr.png";
       
        

        try {
            QRUtil.generateQRCodeImage(qrText, 200, 200, filePath);
            System.out.println("QR Code generated at: " + filePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
   

    public static String readUserQR(String filePath) {
        try {
            BufferedImage qrImage = ImageIO.read(new File(filePath));
            return QRUtil.readQRCode(qrImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
