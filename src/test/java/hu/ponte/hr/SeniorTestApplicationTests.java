package hu.ponte.hr;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.exceptions.SignException;
import hu.ponte.hr.services.ImageStore;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SeniorTestApplicationTests {

  @Autowired
  private ImageStore imageStore;

  @Before
  public void createTestFiles() {
    try{
      imageStore.initTest();
      File uploadDataJson = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\uploadData.json");
      ImageMeta testData = ImageMeta.builder()
        .id("d9c33c7e-a6ce-4d51-ba50-58391e8b7527")
        .size(347226)
        .path(System.getProperty("user.dir") + "\\src\\test\\resources\\upload\\d9c33c7e-a6ce-4d51-ba50-58391e8b7527.png")
        .mimeType("image/jpg")
        .name("cat.jpg")
        .digitalSign("XYZ+wXKNd3Hpnjxy4vIbBQVD7q7i0t0r9tzpmf1KmyZAEUvpfV8AKQlL7us66rvd6eBzFlSaq5HGVZX2DYTxX1C5fJlh3T3QkVn2zKOfPHDWWItdXkrccCHVR5HFrpGuLGk7j7XKORIIM+DwZKqymHYzehRvDpqCGgZ2L1Q6C6wjuV4drdOTHps63XW6RHNsU18wHydqetJT6ovh0a8Zul9yvAyZeE4HW7cPOkFCgll5EZYZz2iH5Sw1NBNhDNwN2KOxrM4BXNUkz9TMeekjqdOyyWvCqVmr5EgssJe7FAwcYEzznZV96LDkiYQdnBTO8jjN25wlnINvPrgx9dN/Xg==")
        .build();

      List<ImageMeta> images = new ArrayList<>();
      images.add(testData);

      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(uploadDataJson, images);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @After
  public void deleteTestFiles() {
    File uploadDataJson = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\uploadData.json");
    File imageFolder = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\upload");

    uploadDataJson.delete();
    for(File file : Objects.requireNonNull(imageFolder.listFiles())){
      file.delete();
    }
  }

  @Test
  public void getImageTest(){
    ImageMeta expected = ImageMeta.builder()
      .id("d9c33c7e-a6ce-4d51-ba50-58391e8b7527")
      .size(347226)
      .path(System.getProperty("user.dir") + "\\src\\test\\resources\\upload\\d9c33c7e-a6ce-4d51-ba50-58391e8b7527.png")
      .mimeType("image/jpg")
      .name("cat.jpg")
      .digitalSign("XYZ+wXKNd3Hpnjxy4vIbBQVD7q7i0t0r9tzpmf1KmyZAEUvpfV8AKQlL7us66rvd6eBzFlSaq5HGVZX2DYTxX1C5fJlh3T3QkVn2zKOfPHDWWItdXkrccCHVR5HFrpGuLGk7j7XKORIIM+DwZKqymHYzehRvDpqCGgZ2L1Q6C6wjuV4drdOTHps63XW6RHNsU18wHydqetJT6ovh0a8Zul9yvAyZeE4HW7cPOkFCgll5EZYZz2iH5Sw1NBNhDNwN2KOxrM4BXNUkz9TMeekjqdOyyWvCqVmr5EgssJe7FAwcYEzznZV96LDkiYQdnBTO8jjN25wlnINvPrgx9dN/Xg==")
      .build();
    ImageMeta actual = imageStore.getImage("d9c33c7e-a6ce-4d51-ba50-58391e8b7527");

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getSize(), actual.getSize());
    Assert.assertEquals(expected.getPath(), actual.getPath());
    Assert.assertEquals(expected.getMimeType(), actual.getMimeType());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getDigitalSign(), actual.getDigitalSign());
  }

  @Test
  public void storeNewImageTest(){
    try {
      MultipartFile file = new MockMultipartFile("file", "rnd.jpg", "image/jpeg", Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\test\\resources\\images\\rnd.jpg")));
      String id = imageStore.storeNewImage(file);

      ImageMeta expected = ImageMeta.builder()
        .id(id)
        .size(198870)
        .path(System.getProperty("user.dir") + "\\src\\test\\resources\\upload\\" +  id + ".jpg")
        .mimeType("image/jpeg")
        .name("rnd.jpg")
        .digitalSign("lM6498PalvcrnZkw4RI+dWceIoDXuczi/3nckACYa8k+KGjYlwQCi1bqA8h7wgtlP3HFY37cA81ST9I0X7ik86jyAqhhc7twnMUzwE/+y8RC9Xsz/caktmdA/8h+MlPNTjejomiqGDjTGvLxN9gu4qnYniZ5t270ZbLD2XZbuTvUAgna8Cz4MvdGTmE3MNIA5iavI1p+1cAN+O10hKwxoVcdZ2M3f7/m9LYlqEJgMnaKyI/X3m9mW0En/ac9fqfGWrxAhbhQDUB0GVEl7WBF/5ODvpYKujHmBAA0ProIlqA3FjLTLJ0LGHXyDgrgDfIG/EDHVUQSdLWsM107Cg6hQg==")
        .build();
      ImageMeta actual = imageStore.getImages().get(1);

      Assert.assertEquals(expected.getId(), actual.getId());
      Assert.assertEquals(expected.getSize(), actual.getSize());
      Assert.assertEquals(expected.getPath(), actual.getPath());
      Assert.assertEquals(expected.getMimeType(), actual.getMimeType());
      Assert.assertEquals(expected.getName(), actual.getName());
      Assert.assertEquals(expected.getDigitalSign(), actual.getDigitalSign());
      Assert.assertTrue(new File(actual.getPath()).exists());
    } catch (IOException | SignException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void getImagesTest() {
    try {
      deleteTestFiles();
      imageStore.initTest();

      ImageMeta expected1 = ImageMeta.builder()
        .id("d9c33c7e-a6ce-4d51-ba50-58391e8b7527")
        .size(347226)
        .path(System.getProperty("user.dir") + "\\src\\test\\resources\\upload\\d9c33c7e-a6ce-4d51-ba50-58391e8b7527.png")
        .mimeType("image/jpg")
        .name("cat.jpg")
        .digitalSign("XYZ+wXKNd3Hpnjxy4vIbBQVD7q7i0t0r9tzpmf1KmyZAEUvpfV8AKQlL7us66rvd6eBzFlSaq5HGVZX2DYTxX1C5fJlh3T3QkVn2zKOfPHDWWItdXkrccCHVR5HFrpGuLGk7j7XKORIIM+DwZKqymHYzehRvDpqCGgZ2L1Q6C6wjuV4drdOTHps63XW6RHNsU18wHydqetJT6ovh0a8Zul9yvAyZeE4HW7cPOkFCgll5EZYZz2iH5Sw1NBNhDNwN2KOxrM4BXNUkz9TMeekjqdOyyWvCqVmr5EgssJe7FAwcYEzznZV96LDkiYQdnBTO8jjN25wlnINvPrgx9dN/Xg==")
        .build();
      ImageMeta expected2 = ImageMeta.builder()
        .id("3054a6c9-7874-434f-8040-c7b8bae84f92")
        .size(198870)
        .path(System.getProperty("user.dir") + "\\src\\test\\resources\\upload\\3054a6c9-7874-434f-8040-c7b8bae84f92.jpg")
        .mimeType("image/jpeg")
        .name("rnd.jpg")
        .digitalSign("lM6498PalvcrnZkw4RI+dWceIoDXuczi/3nckACYa8k+KGjYlwQCi1bqA8h7wgtlP3HFY37cA81ST9I0X7ik86jyAqhhc7twnMUzwE/+y8RC9Xsz/caktmdA/8h+MlPNTjejomiqGDjTGvLxN9gu4qnYniZ5t270ZbLD2XZbuTvUAgna8Cz4MvdGTmE3MNIA5iavI1p+1cAN+O10hKwxoVcdZ2M3f7/m9LYlqEJgMnaKyI/X3m9mW0En/ac9fqfGWrxAhbhQDUB0GVEl7WBF/5ODvpYKujHmBAA0ProIlqA3FjLTLJ0LGHXyDgrgDfIG/EDHVUQSdLWsM107Cg6hQg==")
        .build();

      File uploadDataJson = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\uploadData.json");
      List<ImageMeta> imagesToAdd = new ArrayList<>();
      imagesToAdd.add(expected1);
      imagesToAdd.add(expected2);

      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(uploadDataJson, imagesToAdd);

      List<ImageMeta> images = imageStore.getImages();

      Assert.assertEquals(images.size(), 2);
      Assert.assertEquals(expected1.getId(), images.get(0).getId());
      Assert.assertEquals(expected1.getSize(),  images.get(0).getSize());
      Assert.assertEquals(expected1.getPath(),  images.get(0).getPath());
      Assert.assertEquals(expected1.getMimeType(),  images.get(0).getMimeType());
      Assert.assertEquals(expected1.getName(),  images.get(0).getName());
      Assert.assertEquals(expected1.getDigitalSign(),  images.get(0).getDigitalSign());
      Assert.assertEquals(expected2.getId(), images.get(1).getId());
      Assert.assertEquals(expected2.getSize(),  images.get(1).getSize());
      Assert.assertEquals(expected2.getMimeType(),  images.get(1).getMimeType());
      Assert.assertEquals(expected2.getName(),  images.get(1).getName());
      Assert.assertEquals(expected2.getPath(),  images.get(1).getPath());
      Assert.assertEquals(expected2.getDigitalSign(),  images.get(1).getDigitalSign());


    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

