package cn.lxk.utils;

import java.io.*;

/**
 * @author Liu Xukai
 * @Description
 * @Date 2021/02/20 16:29
 */
public class VideoToBase64 {
    /**
     *
     * @param videofilePath 视频文件路径带文件名
     * @return base64
     */
    public static String videoToBase64(File videofilePath) {
        long size = videofilePath.length();
        byte[] imageByte = new byte[(int) size];
        FileInputStream fs = null;
        BufferedInputStream bis = null;
        try {
            fs = new FileInputStream(videofilePath);
            bis = new BufferedInputStream(fs);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                }
            }
        }
        return (new sun.misc.BASE64Encoder()).encode(imageByte);
    }
}
