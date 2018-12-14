package cn.jeelearn.house.biz.service.base;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/12
 * @Version:v1.0
 */
@Service
public class FileService {

    @Value("${file.path:}")
    private String filePath;

    public List<String> getImgPaths(List<MultipartFile> files) {
        if (Strings.isNullOrEmpty(filePath)){
            filePath = getResourcePath();
        }
        List<String> paths = Lists.newArrayList();
        files.forEach(file -> {
            File localFile = null;
            try {
                localFile =  saveToLocal(file, filePath);
                // localFile  G:\item_idea\springboot-house\1544590457\32600038.jpg
                //filePath  G:/item_idea/springboot-house
                String localPath = localFile.getAbsolutePath();
                //windows会有问题，通过replaceAll解决
                if (localPath.contains("\\")){
                    localPath = localPath.replaceAll("\\\\", "/");
                }
                String path = StringUtils.substringAfterLast(localPath, filePath);
                paths.add(path);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        });
        return paths;
    }

    public static String getResourcePath(){
        File file = new File(".");
        String absolutePath = file.getAbsolutePath();
        return absolutePath;
    }

    private File saveToLocal(MultipartFile file, String filePath2) throws IOException {
        File newFile = new File(filePath2 + "/" + Instant.now().getEpochSecond() + "/" + file.getOriginalFilename());
        if (!newFile.exists()){
            newFile.getParentFile().mkdirs(); //G:\item_idea\springboot-house\1544590147\32600044.jpg 建1544590147文件夹
            newFile.createNewFile(); //创建空文件32600044.jpg
        }
        Files.write(file.getBytes(), newFile);
        return newFile;
    }

    /**
     * 如果用户注册后未激活，则删除头像
     * @auther: lyd
     * @date: 2018/12/13
     */
    public void remove(String avatar){
        File file = new File(filePath + avatar);
        if (file.exists() && file.isFile()){
            File parentFile = file.getParentFile();
            if (parentFile.exists() && parentFile.isDirectory()){
                file.delete();
                if (parentFile.length()==0){
                    parentFile.delete();
                }
            }
        }
    }


    public static void main(String[] args) {
        //G:\item_idea\springboot-house\housev1.0\house\.
        System.out.println(getResourcePath());
        String localFile = "G:\\item_idea\\springboot-house \\1544590457\\32600038.jpg";
        String filePath = "G:/item_idea/springboot-house";
        localFile = localFile.replaceAll("\\\\", "/");
        String path = StringUtils.substringAfterLast(localFile, filePath);
        System.out.println(path);

        File file = new File("G:/item_idea/springboot-house/1544590457/32600038.jpg");

        new FileService().remove("/1544594370/32600041.jpg");
    }
}

