package edu.se.extweb;


/*
  @author   george
  @project   ext-web
  @class  LoggingTest
  @version  1.0.0 
  @since 02.04.26 - 21.31
*/

import edu.se.extweb.model.Item;
import edu.se.extweb.request.ItemPageRequest;
import edu.se.extweb.response.ApiResponse;
import edu.se.extweb.response.PaginationMetaData;
import edu.se.extweb.service.ItemService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoggingTest {

    @Autowired
    private ItemService underTest;

    @BeforeEach
    void setUp() {
        TestItems.reset(underTest);
    }

    @Test
    void testLoggingOutputBeforeMethodGetById(CapturedOutput output) {
        // given
       Item item =  underTest.getById(TestItems.FIRST_ID);
       //when
       assertNotNull(item);
       // then
       assertTrue(output.toString().contains("Entering method:"));
       assertTrue(output.toString().contains("ItemService.getById"));
       assertTrue(output.toString().contains(TestItems.FIRST_ID));
    }

    @Test
    void testLoggingOutputAfterMethodGetById(CapturedOutput output) {
        Item item =  underTest.getById(TestItems.FIRST_ID);
        assertNotNull(item);
        assertTrue(output.toString().contains("ItemService.getById"));
        assertTrue(output.toString().contains("completed successfully"));
        assertTrue(output.toString().contains(TestItems.FIRST_ID));
        assertTrue(output.toString().contains("Iggy"));
    }


    @Test
    void testLoggingOutputBeforeMethodGetItemsPage(CapturedOutput output) {
        ItemPageRequest request = new ItemPageRequest(0,5);
        ApiResponse<PaginationMetaData, Item> page =  underTest.getItemsPage(request);
       assertNotNull(page);
       assertTrue(output.toString().contains("ItemService.getItemsPage"));
       assertTrue(output.toString().contains("0"));
       assertTrue(output.toString().contains("5"));
    }







}
