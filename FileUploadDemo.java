package mfjobftp;

import org.apache.commons.net.ftp.FTPClient;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUploadDemo {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;

        try {
            client.connect("ca31");
            client.login("pansr01", "srirama1");
            // Create an InputStream of the file to be uploaded
            String filename = "sample.txt";
            fis = new FileInputStream(filename);
            // Store file to server
            client.storeFile("'PUBLIC.AUTOWSVC.R15.MEM'", fis);
            client.logout();
            System.out.println("Completed");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
