package test;

import exceptions.InvalidPackException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PackTest {

    private static Pack pack;
    private static String path = "src/test/test_pack_1";
    private static int players = 4;

    @BeforeAll
    public static void setUp() throws InvalidPackException, FileNotFoundException {
        pack = new Pack(path, players);
    }

    @Test
    public void getCardsTest(){
        assertEquals(pack.getCards().size(), 32);
    }
    @Test
    public void getTopCardTest(){
        assertEquals(pack.getCards().peek().getFaceValue(), 5);
        assertEquals(pack.getTopCard().getFaceValue(), 5);
    }
    @Test
    public void isValidPack(){
        String methodName = "isValidPack";

        File file = new File("src/test/test_pack_1");
        Object[] args = new Object[]{file};
        Class<File> argClass = File.class;

        assertDoesNotThrow(() -> {
            Method packIsValid = Pack.class.getDeclaredMethod(methodName, argClass);
            packIsValid.setAccessible(true);
            boolean result = (boolean) packIsValid.invoke(pack, args);
            assertTrue(result);
        });
    }
    @Test
    public void isInvalidPack(){
        String methodName = "isValidPack";

        File file = new File("src/test/test_pack_2");
        Object[] args = new Object[]{file};
        Class<File> argClass = File.class;

        assertDoesNotThrow(() -> {
            Method packIsValid = Pack.class.getDeclaredMethod(methodName, argClass);
            packIsValid.setAccessible(true);
            boolean result = (boolean) packIsValid.invoke(pack, args);
            assertFalse(result);
        });
    }
    @Test
    public void createPackTest(){
        String methodName = "create";

        File file = new File("src/test/test_pack_1");
        Object[] args = new Object[]{file};
        Class<File> argClass = File.class;

        assertDoesNotThrow(() -> {
            Method createPack = Pack.class.getDeclaredMethod(methodName, argClass);
            createPack.setAccessible(true);
            createPack.invoke(pack, args);
        });
    }
}
