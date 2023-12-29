package com.hdel.miri.api.util.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.UUID;

@Slf4j
@Component
public class FileService {
    private static final LinkedHashMap<String,String> mimeTypes = new LinkedHashMap<>(){{
        put("bmp","image/bmp");
        put("gif","image/gif");
        put("jpg","image/jpeg");
        put("jpeg","image/jpeg");
        put("png","image/png");
        put("svg","image/svg+xml");
        put("tif","image/tiff");
        put("tiff","image/tiff");
        put("webp","image/webp");
    }};
    @Value("${spring.app.win.upload}")
    private String window;
    @Value("${spring.app.mac.upload}")
    private String mac;
    @Value("${spring.app.other.upload}")
    private String other;

    @Value("${spring.app.win.temp}")
    private String windowTemp;
    @Value("${spring.app.mac.temp}")
    private String macTemp;
    @Value("${spring.app.other.temp}")
    private String otherTemp;

    @Value("${spring.app.contract-nas}")
    private String CONTRACT_NAS;



    private final String os = System.getProperty("os.name").toLowerCase();
    private static String root;
    private static String tempRoot;


    private boolean exist(File folder){
        if (!folder.exists()) {
            folder.setExecutable(true);
            folder.setReadable(true);
            folder.setWritable(true);
            return folder.mkdirs();
        }else return true;
    }
    /**
     * Initialize Upload Root Path.
     * */
    public boolean existRoot(){
        root = (os.contains("win")? window : (os.contains("mac")? mac : other));
        log.info("Root Path : {}",root);
        return this.exist(new File(root));
    }
    /**
     * Initialize Temp Path.
     * */
    public boolean existTemp(){
        tempRoot = (os.contains("win")? windowTemp : (os.contains("mac")? macTemp : otherTemp));
        log.info("Root Path : {}",tempRoot);
        return this.exist(new File(tempRoot));
    }
    /**
     * Contract Nas Path
     * */
    public boolean existContactNas(){
        if (!new File(CONTRACT_NAS).exists()) {
            return false;
        }else return true;
    }
    public String getContractNasPath(){
        return this.CONTRACT_NAS;
    }
    public String getTempPath(){
        return this.tempRoot;
    }

    public File[] testContractNas(){
        return new File(CONTRACT_NAS).listFiles();
    }

    /**
     * Upload Sub Path.
     * */
    public String existSub(){
        Path path = Paths.get(root, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        exist(path.toFile());
        return FilenameUtils.separatorsToSystem(path.toString());
    }

    /**
     * Generate Key.
     * */
    public String generateKey(){
        return UUID.randomUUID().toString()
                .concat(UUID.randomUUID().toString())
                .concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).toString())
                .replace("-","");
    }

    /**
     * Get Extension.
     * */
    public String extension(String filename){
        return FilenameUtils.getExtension(filename);
    }

    public Path fullPath(String base,String key,String ext){
        return Paths.get(FilenameUtils.concat(base,key.concat(FilenameUtils.EXTENSION_SEPARATOR_STR).concat(ext)));
    }

    public Path view(String fullPath){
        return Paths.get(FilenameUtils.getFullPath(fullPath));
    }

    public String viewPath(String key){
        return "/v2/storage/images/viewer/".concat(key);
    }
    public boolean save(MultipartFile from,Path to) throws IOException {
            from.transferTo(to);
            return true;
    }
    public String getMimeTypes(String ext){
        return mimeTypes.get(ext);
    }
}
