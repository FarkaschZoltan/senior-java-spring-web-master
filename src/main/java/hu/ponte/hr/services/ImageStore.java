package hu.ponte.hr.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.exceptions.SignException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStore {
  Logger log = LoggerFactory.getLogger(ImageStore.class);
  private final File imageFolder = new File(System.getProperty("user.dir") + "\\upload");
  private final File uploadDataJson = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\uploadData.json");

  @Autowired
  private SignService signService;

  @PostConstruct
  private void init(){
    if(!imageFolder.exists()){ //If the upload folder does not exist, we create it after construction
      imageFolder.mkdirs();
    }

    if(!uploadDataJson.exists()){ //If the uploadData.json does not exist, we create it after construction
      try {
        uploadDataJson.createNewFile();
      } catch (IOException e) {
        log.error("JSON file could not be created", e);
      }
    }
  }

  public List<ImageMeta> getImages() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    if(uploadDataJson.length() != 0){
      return mapper.readerForListOf(ImageMeta.class).readValue(uploadDataJson);
    }
    return new ArrayList<>();
  }

  public ImageMeta getImage(String id) {
    try{
      for(ImageMeta image : getImages()){
        if(image.getId().equals(id)){
          return image;
        }
      }
      return null;
    } catch(IOException e) {
      return null;
    }
  }

  //We store the file with the name of its UUID. Its "name" in ImageMeta stays the same, but this way we can have multiple images with the same name,
  //without the new image overwriting the old one.
  public void storeNewImage(MultipartFile upload) throws IOException, SignException {
    String id = UUID.randomUUID().toString();
    ImageMeta newImage = ImageMeta.builder()
      .id(id) //this is the id of the image, and its "actual name" in storage
      .name(upload.getOriginalFilename()) //this will be displayed in thr frontend
      .path(imageFolder + "\\" + id + "." + upload.getOriginalFilename().substring(upload.getOriginalFilename().lastIndexOf("."))) //path to the renamed file
      .mimeType(upload.getContentType())
      .size(upload.getSize())
      .digitalSign(signService.signImage(upload.getBytes()))
      .build();

    List<ImageMeta> images = getImages();
    images.add(newImage);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(uploadDataJson, images);

    upload.transferTo(new File(newImage.getPath()));
  }
}
