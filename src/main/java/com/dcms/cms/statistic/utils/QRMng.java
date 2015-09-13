package com.dcms.cms.statistic.utils;

import com.dcms.common.upload.FileRepository;
import com.dcms.common.upload.UploadUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import static com.google.zxing.client.j2se.MatrixToImageWriter.writeToFile;

/**
 * Created by Daily on 2015/8/28.
 */
@Service
public class QRMng {
    public static final Integer WIDTH = 400;
    public static final Integer HEIGHT = 400;
    public static final String FORMAT = "png";

    public String stringToQR(String path,String url){

        String fileName = UploadUtils.generateFilename(path, FORMAT);
        Hashtable hints= new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, WIDTH, HEIGHT,hints);
            File f = new File(fileRepository.getRealPath(fileName));
            UploadUtils.checkDirAndCreate(f.getParentFile());
            writeToFile(bitMatrix, FORMAT, f);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public static void main(String[] args) {
        new QRMng().stringToQR("D:/","http://www.baidu.com");
    }

    @Autowired
    protected FileRepository fileRepository;

}
