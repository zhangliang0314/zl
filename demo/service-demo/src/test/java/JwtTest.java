import com.yonyou.einvoice.demo.utils.JwtInnerUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
@Service
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JwtTest.class)
public class JwtTest {


  @Test
  public void test(){
    String sign = JwtInnerUtils.sign(null);
    System.out.println(sign);
  }

  @Test
  public void testDate(){
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    StringBuilder sb = new StringBuilder("["+LocalDateTime.now().minusDays(1).format(format)+"]");
    sb.append("    ").append("123");
    System.out.println(sb.toString());
  }
}
