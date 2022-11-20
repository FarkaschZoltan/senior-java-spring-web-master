package hu.ponte.hr.controller;


import hu.ponte.hr.exceptions.SignException;
import hu.ponte.hr.services.ImageStore;
import hu.ponte.hr.services.SignService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequestMapping("api/images")
public class ImagesController {

  Logger log = LoggerFactory.getLogger(ImagesController.class);

  @Autowired
  private ImageStore imageStore;

  @Autowired
  private SignService signService;

  @GetMapping("meta")
  public List<ImageMeta> listImages() {
    try {
      return imageStore.getImages();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return new ArrayList<>();
    }
  }

  @GetMapping("preview/{id}")
  public void getImage(@PathVariable("id") String id, HttpServletResponse response) {
    try {
      ImageMeta image = imageStore.getImage(id);
      if(image != null){
        byte[] fileBytes = Files.readAllBytes(Paths.get(image.getPath()));

        if (signService.verifySignature(fileBytes, image.getDigitalSign())) {
          response.setContentType(image.getMimeType());
          response.getOutputStream().write(fileBytes, 0, fileBytes.length);
          response.getOutputStream().close();
        } else {
          response.sendError(500);
        }
      } else {
        response.sendError(500);
      }
    } catch (IOException | SignException e) {
      log.error(e.getMessage(), e);
    }
  }

}
