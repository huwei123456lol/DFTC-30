package weaver.dfqcgsjszx.util;

import org.apache.axis.utils.ByteArrayOutputStream;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.io.File;
import java.io.FileInputStream;

public class FileUtil {
//将图片转为byte数组
    public static byte[] file2Byte(String imgId) {
        File file = getFile(imgId);
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            data = baos.toByteArray();
            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    //将图片id为imgId的的图片实例化
    public static File getFile(String imgId) {
        RecordSet rs = new RecordSet();
        rs.execute("select filerealpath from imagefile where imagefileid = '" + imgId + "'");
        File file = null;
        if (rs.next()){
            try {
                String filerealpath = Util.null2String(rs.getString("filerealpath").trim());
                file = new File(filerealpath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
