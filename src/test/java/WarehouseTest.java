import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {

    private Warehouse warehouse;
    private Product product;
    private Location location;

    @BeforeEach
    public void setUp() {
        location = new Location("Төв агуулах");
        warehouse = new Warehouse(1, location, 100);
        product = new Product(1, "Гар утас", "Электрон", 999999.9, 10, "123456789");
    }

    @Test
    public void testAddProduct() {
        warehouse.addProduct(product);
        assertEquals(1, warehouse.getProducts().size());
        assertEquals(10, warehouse.getProducts().get(0).getQuantity());
        assertEquals(1, warehouse.getStockMoves().size());
        assertEquals("Орсон", warehouse.getStockMoves().get(0).getMoveType());
    }

    @Test
    public void testRemoveProduct() {
        warehouse.addProduct(product);
        warehouse.removeProduct(product, 5);
        assertEquals(5, warehouse.getProducts().get(0).getQuantity());
        assertEquals(2, warehouse.getStockMoves().size());
        assertEquals("Гарсан", warehouse.getStockMoves().get(1).getMoveType());
    }

   @Test
public void testTransferProduct() {
    // Arrange
    Product product = new Product(1, "TestProduct", "TestCategory", 10.0, 10, "1234567890");
    Warehouse warehouse = new Warehouse(1, new Location("Үндсэн агуулах"), 100);
    Warehouse secondWarehouse = new Warehouse(2, new Location("Салбар агуулах"), 50);

    // Act
    warehouse.addProduct(product); // adds 10 units
    warehouse.transferProduct(product, 4, secondWarehouse); // transfer 4 units

    // Assert product quantities
    assertEquals(6, warehouse.getProducts().get(0).getQuantity());      // 10 - 4 = 6
    assertEquals(4, secondWarehouse.getProducts().get(0).getQuantity()); // received 4

    // Assert stockMoves count
    assertEquals(2, warehouse.getStockMoves().size());      // 1 add + 1 remove
    assertEquals(1, secondWarehouse.getStockMoves().size()); // 1 add

    // Assert move types
    assertEquals("Гарсан", warehouse.getStockMoves().get(1).getMoveType());
    assertEquals("Орсон", secondWarehouse.getStockMoves().get(0).getMoveType());
}


    @Test
    public void testAddProductWithInvalidQuantity() {
        Product invalidProduct = new Product(2, "Буруу тоо", "Категори", 1000, -5, "000000");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouse.addProduct(invalidProduct);
        });
        assertEquals("Нэмэх бүтээгдэхүүний тоо 0-ээс их байх ёстой.", exception.getMessage());
    }

    @Test
    public void testRemoveProductWithExcessQuantity() {
        warehouse.addProduct(product);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouse.removeProduct(product, 20);
        });
        assertEquals("Агуулахад хүрэлцэхгүй тоо хасах гэж байна.", exception.getMessage());
    }

    @Test
    public void testRemoveProductWithNegativeQuantity() {
        warehouse.addProduct(product);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouse.removeProduct(product, -1);
        });
        assertEquals("Хасах тоо 0-ээс их байх ёстой.", exception.getMessage());
    }

    @Test
    public void testAddProductExceedsCapacity() {
        warehouse = new Warehouse(1, location, 10); // Багтаамж бага
        Product bigProduct = new Product(3, "Том бараа", "Категори", 5000, 15, "12345");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            warehouse.addProduct(bigProduct);
        });

        assertEquals("Агуулахын багтаамжаас хэтэрсэн бүтээгдэхүүн нэмэх гэж байна.", exception.getMessage());
    }
}
