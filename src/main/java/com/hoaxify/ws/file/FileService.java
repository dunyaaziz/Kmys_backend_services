package com.hoaxify.ws.file;

import com.hoaxify.ws.configuration.AppConfiguration;
import com.hoaxify.ws.error.NotFoundException;
import org.apache.tika.Tika;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {

    AppConfiguration appConfiguration;
    Tika tika;

    public FileService(AppConfiguration appConfiguration) {
        super();
        this.appConfiguration = appConfiguration;
        this.tika = new Tika();
    }

    public String writeBase64EncodedStringToFile(String image) throws IOException {

        String fileName= generateRandomUser();
        File target =new File(appConfiguration.getUploadPath() +"/"+fileName);
        OutputStream outputStream = new FileOutputStream(target);
        byte[] base64Encoded = Base64.getDecoder().decode(image);

        outputStream.write(base64Encoded);
        outputStream.close();
        return fileName;
    }
    public String generateRandomUser() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public void deleteFile(String oldImageName) {
        if(oldImageName == null){
            return;
        }
        try {
            Files.deleteIfExists(Paths.get( appConfiguration.getUploadPath() + "/" + oldImageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String detectType(String value) {
        byte[] base64Encoded = Base64.getDecoder().decode(value);
        return tika.detect(base64Encoded);
    }

    public void downloadFile(HttpServletResponse response) throws FileNotFoundException {
        try {
            File file = new File("file-download" +"/"+ "kmys.zip");

//            // get pid from mId
//            Material material = materialService.getMaterialByMId(mId);
//            String pId = material.getPid();
//
//            Query query = Query.query(Criteria.where("_id").is(pId));
//            // Query a single file
//            GridFSFile gridFSFile = gridFsTemplate.findOne(query);
//            if (gridFSFile == null) {
//                return new ReturnInfo("0", "Failed to download file", null, 0);
//            }
            String fileName = "ali";
            String contentType = URLConnection.guessContentTypeFromName(file.getName());
            if (contentType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                contentType = "application/octet-stream";
            }


            // logger.info("fileName:" + fileName);

            // Notify the browser to download the file
            response.setContentType(contentType);
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

//            try{
                OutputStream outputStream = response.getOutputStream();
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
                outputStream.close();
                inputStream.close();
        //}
//            catch (Exception e) {
//                throw new NotFoundException("File Not Found",e);
 //           }

        } catch (Exception e) {
            //logger.error("Download file abnormal", e);
            throw new NotFoundException("File Download Failed");
        }
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body("File Downloaded Succesfully");
    }
}