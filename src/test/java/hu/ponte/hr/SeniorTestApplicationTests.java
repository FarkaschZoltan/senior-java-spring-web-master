package hu.ponte.hr;

import hu.ponte.hr.services.ImageStore;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeniorTestApplicationTests {

  @Autowired
  private ImageStore imageStore;

  @BeforeClass
  public static void test() {

  }

  @Test
  public void contextLoads() {
  }

}

