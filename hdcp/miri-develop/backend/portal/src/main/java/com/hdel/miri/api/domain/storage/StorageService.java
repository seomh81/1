package com.hdel.miri.api.domain.storage;

import ch.qos.logback.core.rolling.helper.FileNamePattern;
import com.hdel.miri.api.domain.ad.section.SectionAD;
import com.hdel.miri.api.domain.ad.signin.SignInAD;
import com.hdel.miri.api.domain.scrm.SCRMRepository;
import com.hdel.miri.api.util.file.FileService;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.digester.DocumentProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final FileService fileService;
    private final StorageRepository storageRepository;
    private final ResponseTemplate responseTemplate;
    private final SCRMRepository scrmRepository;
    private final Message message;

    public List<Storage.FileInfo> save(List<MultipartFile> files) throws IOException {
        String savePath = fileService.existSub();
        List<Storage.FileInfo> fileInfos = new ArrayList<>();
        for( MultipartFile file : files ){
            String origin = file.getOriginalFilename();
            String key = fileService.generateKey();
            String ext = fileService.extension(origin);
            Path target = fileService.fullPath(savePath,key,ext);
            if(fileService.save(file,target)) {
                fileInfos.add(Storage.FileInfo.builder().physicalFileName(key).fullFilePath(target.toString()).originalFileName(origin).requester("").build());
            }
        }
        return fileInfos;
    }
    public int saveStoage(Storage.ImageSave request){
        return storageRepository.insert(request);
    }

    /** 공통 파일 저장 */
    @Transactional(value="db2TxManager")
    public ResponseEntity save(Storage.ImageSave request, List<MultipartFile> files) {
        try {
            request.setFiles(save(files));
            if(0 < saveStoage(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(request.getFiles().stream().map(fileInfo -> fileService.viewPath(fileInfo.getPhysicalFileName())).collect(Collectors.toList()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());

        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureTransferFile(request.getUserLocale()));
        }
    }
    /**
     * Save Section Ad
     * */
    @Transactional(value = "db2TxManager",propagation = Propagation.NESTED)
    public SectionAD.SectionADDetailCreate save(SectionAD.SectionADDetailCreate request, List<MultipartFile> files) throws IOException {
            request.setFiles(save(files));
            saveStoage(request);
            return request;
    }
    /**
     * Save SignIn Ad
     * */
    public SignInAD.SignInADDetailCreate save(SignInAD.SignInADDetailCreate request, List<MultipartFile> files) throws IOException {
        request.setFiles(save(files));
        saveStoage(request);
        return request;
    }

    /** Image View */
    public ResponseEntity<byte[]> imageView(String key) {
        try {
            Storage.View info = storageRepository.selectImage(key);
            String ext = fileService.extension(info.getFullFilePath());
            File file = new File(info.getFullFilePath());
            InputStream in = new FileInputStream(file);
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
                    .contentType(MediaType.parseMediaType(fileService.getMimeTypes(ext)))
                    .body(IOUtils.toByteArray(in));
        } catch (Exception e) {
            return (ResponseEntity<byte[]>) ResponseEntity.noContent().build().getBody();
        }
    }

    /** Contract File Download */
    public ResponseEntity<Resource> contractDownload(String key, HttpServletRequest request){
        List<Storage.CAFileInfo> fileInfos =scrmRepository.selectCAFiles(key);
        return (0 == fileInfos.size()? ResponseEntity.ok(new InputStreamResource(null))
                :(1 < fileInfos.size()? filesDownload(fileInfos,key,request)
                : fileDownload(fileInfos.get(0).getSavedFilePath(),fileInfos.get(0).getCaFileNm(),request)));
    }
    private ResponseEntity<Resource> filesDownload(List<Storage.CAFileInfo> paths, String key, HttpServletRequest request){

        String originName = key.concat("_").concat(String.valueOf(System.currentTimeMillis())).concat(FilenameUtils.EXTENSION_SEPARATOR_STR).concat("zip");
        Path currentTempPath = Paths.get(fileService.getTempPath(),originName);
        byte[] buffer = new byte[1024];
        File requestZip = currentTempPath.toFile();

        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(requestZip));
            for(int i=0,len=paths.size(); i<len; i++){
                Path targetPath = Paths.get(fileService.getContractNasPath(),paths.get(i).getSavedFilePath());
                File targetFile = targetPath.toFile();
                FileInputStream fis = new FileInputStream(targetFile);
                zout.putNextEntry(new ZipEntry(paths.get(i).getCaFileNm()));
                int fLen = 0;
                while((fLen = fis.read(buffer)) > 0){zout.write(buffer);}
                zout.closeEntry();
                fis.close();
            }
            zout.close();

            HttpHeaders headers = new HttpHeaders();
            String encodedFilename = getEncodedFilename(request, originName);
            headers.setCacheControl(CacheControl.noCache());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ContentDisposition contentDisposition =ContentDisposition.attachment()
                    .filename(encodedFilename)
                    .filename(encodedFilename,Charset.forName("UTF-8"))
                    .build();
            headers.setContentDisposition(contentDisposition);
            headers.setContentDispositionFormData("attachment",contentDisposition.getFilename());

            InputStreamResource in = new InputStreamResource(new FileInputStream(requestZip){
                @Override
                public void close() throws IOException {
                    super.close();
                    requestZip.delete();
                }
            });
            return  ResponseEntity.ok().headers(headers).body(in);
        } catch (FileNotFoundException e) {
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseNotExistFile("ko_kr"));
        } catch (IOException e) {
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureTransferFile("ko_kr"));
        }
    }
    private ResponseEntity<Resource> fileDownload(String path,String originName, HttpServletRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            String encodedFilename = getEncodedFilename(request, originName);
            headers.setCacheControl(CacheControl.noCache());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ContentDisposition contentDisposition =ContentDisposition.attachment()
                    .filename(encodedFilename)
                    .filename(encodedFilename,Charset.forName("UTF-8"))
                    .build();
            headers.setContentDisposition(contentDisposition);
            headers.setContentDispositionFormData("attachment",new String(contentDisposition.getFilename().getBytes(StandardCharsets.UTF_8).toString()));

            InputStreamResource  in = new InputStreamResource(new FileInputStream(Paths.get(fileService.getContractNasPath().concat(path)).toFile()));
            return ResponseEntity.ok().headers(headers).body(in);
        } catch (FileNotFoundException e ){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseNotExistFile("ko_kr"));
        } catch (IOException e) {
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureTransferFile("ko_kr"));
        }
    }

    private String getEncodedFilename(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");
        String encodedFilename;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else {
            encodedFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        return encodedFilename;
    }
}
