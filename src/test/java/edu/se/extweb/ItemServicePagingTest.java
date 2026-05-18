package edu.se.extweb;

import edu.se.extweb.model.Item;
import edu.se.extweb.request.ItemPageRequest;
import edu.se.extweb.response.ApiResponse;
import edu.se.extweb.response.BaseMetaData;
import edu.se.extweb.response.PaginationMetaData;
import edu.se.extweb.service.ItemService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServicePagingTest {


    @Autowired
    private ItemService underTest;

    List<Item> items = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
        TestItems.reset(underTest);
    }
   @AfterEach
    void tearsDown(){
    }

    @Test
    void whenHappyPathThenOk(){
        // given
        ItemPageRequest request = new ItemPageRequest(0,5);
        // when
            ApiResponse<PaginationMetaData, Item> response = underTest.getItemsPage(request);
        //then
         assertNotNull(response);
         assertNotNull(response.getMeta());

         assertEquals(200, response.getMeta().getCode());
         assertTrue(response.getMeta().isSuccess());
         assertNull(response.getMeta().getErrorMessage());

         assertEquals(0, response.getMeta().getNumber());
         assertEquals(5, response.getMeta().getSize());
         assertEquals(30, response.getMeta().getTotalElements());
         assertEquals(6, response.getMeta().getTotalPages());
         assertTrue(response.getMeta().isFirst());
         assertFalse(response.getMeta().isLast());

         assertNotNull(response.getData());
         assertFalse(response.getData().isEmpty());
         assertEquals(5, response.getData().size());
         assertEquals(TestItems.LAST_ID, response.getData().get(0).getId());
    }

    @Test
    void whenSizeIs_7_AndPageIs_4_ThenIsLast_TrueAndSizeEquals_2(){
        // given
        ItemPageRequest request = new ItemPageRequest(4,7);
        // when
        ApiResponse<PaginationMetaData, Item> response = underTest.getItemsPage(request);
        // then
        assertNotNull(response);
        assertNotNull(response.getMeta());

        assertEquals(200, response.getMeta().getCode());
        assertTrue(response.getMeta().isSuccess());
        assertNull(response.getMeta().getErrorMessage());

        assertEquals(4, response.getMeta().getNumber());
        assertEquals(7, response.getMeta().getSize());
        assertEquals(30, response.getMeta().getTotalElements());
        assertEquals(5, response.getMeta().getTotalPages());
        assertFalse(response.getMeta().isFirst());
        assertTrue(response.getMeta().isLast());

        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
    }


    @Test
    void whenTheListIsEmptyThenErrorMessageHasTheWarning(){
        // given
        underTest.deleteAll();
        ItemPageRequest request = new ItemPageRequest(0,5);
        // when
        ApiResponse<PaginationMetaData, Item> response = underTest.getItemsPage(request);
        // then
        assertNotNull(response);
        assertNotNull(response.getMeta());
        assertEquals(404, response.getMeta().getCode());
        assertFalse(response.getMeta().isSuccess());
        assertEquals("Items list is empty", response.getMeta().getErrorMessage());
    }

    @Test
    void whenTheListIsEmptyThenMetadataAndDataAreNotNull(){
        // given
        underTest.deleteAll();
        ItemPageRequest request = new ItemPageRequest(0,5);
        // when
        ApiResponse<PaginationMetaData, Item> response = underTest.getItemsPage(request);
        // then
        assertNotNull(response);
        assertNotNull(response.getMeta());
        assertNotNull(response.getData());
        assertTrue(response.getData().isEmpty());
        assertEquals(0, response.getMeta().getTotalElements());
        assertEquals(0, response.getMeta().getTotalPages());
    }


    @Test
    void whenPageValueIsOutOfRangeThenErrorMessageHasTheWarning(){
        // given
        ItemPageRequest request = new ItemPageRequest(9,4);
        // when
        ApiResponse<PaginationMetaData, Item> response = underTest.getItemsPage(request);
        // then
        assertNotNull(response);
        assertNotNull(response.getMeta());
        assertEquals(404, response.getMeta().getCode());
        assertFalse(response.getMeta().isSuccess());
        assertNotNull(response.getMeta().getErrorMessage());
        assertTrue(response.getMeta().getErrorMessage().contains("Maximal page for the size is 7"));
        assertTrue(response.getMeta().getErrorMessage().contains("Last full page is 6"));
    }
//-------------------------------------------------------

    @Test
    void whenRequestIsIncorrectThenGiveTheLastPage(){
        // given
        ItemPageRequest request = new ItemPageRequest(9,4);
        // when
        ApiResponse<PaginationMetaData, Item> response = underTest.getItemsPage(request);
        //then
        assertNotNull(response);
        assertNotNull(response.getMeta());

        assertEquals(404, response.getMeta().getCode());
        assertFalse(response.getMeta().isSuccess());
        assertNotNull(response.getMeta().getErrorMessage());
        assertTrue(response.getMeta().getErrorMessage()
                .contains("Maximal page for the size is 7"));
        assertTrue(response.getMeta().getErrorMessage()
                .contains("Last full page is 6"));

        assertEquals(6, response.getMeta().getNumber());
        assertEquals(4, response.getMeta().getSize());
        assertEquals(30, response.getMeta().getTotalElements());
        assertEquals(8, response.getMeta().getTotalPages());
        assertFalse(response.getMeta().isFirst());
        assertFalse(response.getMeta().isLast());

        assertNotNull(response.getData());
        assertFalse(response.getData().isEmpty());
        assertEquals(4, response.getData().size());
        assertEquals("69aeefcbe5c3dbd26376b0ad", response.getData().get(0).getId());
        assertEquals("69aeefcbe5c3dbd26376b0aa", response.getData().get(3).getId());
    }

    @Test
    void testLogging(CapturedOutput output){
        ItemPageRequest request = new ItemPageRequest(9,4);
        underTest.getItemsPage(request);

        assertTrue(output.toString().contains("Out of range"));
    }




}
