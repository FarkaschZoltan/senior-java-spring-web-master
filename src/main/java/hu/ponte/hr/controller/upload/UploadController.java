package hu.ponte.hr.controller.upload;

import hu.ponte.hr.exceptions.SignException;
import hu.ponte.hr.services.ImageStore;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Component
@RequestMapping("api/file")
public class UploadController {

    Logger log = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private ImageStore imageStore;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) {
        try {
            imageStore.storeNewImage(file);
            return "ok";
        } catch (IOException | SignException e) {
            log.error("File could not be uploaded", e);
            return "error";
        }

    }
}
