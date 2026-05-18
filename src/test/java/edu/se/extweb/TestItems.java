package edu.se.extweb;

import edu.se.extweb.model.Item;
import edu.se.extweb.service.ItemService;

import java.util.ArrayList;
import java.util.List;

final class TestItems {

    static final String FIRST_ID = "69aeefcbe5c3dbd26376b0a8";
    static final String LAST_ID = "69aeefcbe5c3dbd26376b0c5";

    private TestItems() {
    }

    static void reset(ItemService itemService) {
        itemService.deleteAll();
        itemService.createAll(thirtyItems());
    }

    static List<Item> thirtyItems() {
        List<Item> items = new ArrayList<>();
        for (int suffix = 0xa8; suffix <= 0xc5; suffix++) {
            String hexSuffix = Integer.toHexString(suffix);
            String id = "69aeefcbe5c3dbd26376b0" + hexSuffix;
            String name = suffix == 0xa8 ? "Iggy Pop" : "Item " + hexSuffix;
            String code = suffix == 0xa8 ? "Iggy" : "CODE-" + hexSuffix;
            String description = suffix == 0xa8 ? "vocal" : "Generated test item " + hexSuffix;
            items.add(new Item(id, name, code, description));
        }
        return items;
    }
}
