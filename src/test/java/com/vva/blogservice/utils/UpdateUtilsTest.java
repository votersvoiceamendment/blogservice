package com.vva.blogservice.utils;

import org.junit.jupiter.api.Test;
import java.util.function.Consumer;
import java.util.function.Supplier;
import static org.mockito.Mockito.*;

public class UpdateUtilsTest {

    // Test for String type
    @Test
    void shouldUpdateFieldIfChangedForString() {
        // Arrange
        Supplier<String> getter = mock(Supplier.class); // Avoid unchecked warning with explicit cast
        Consumer<String> setter = mock(Consumer.class);
        String existingValue = "Old Value";
        String newValue = "New Value";

        when(getter.get()).thenReturn(existingValue);

        // Act
        UpdateUtils.updateFieldIfChanged(getter, setter, newValue);

        // Assert
        verify(setter).accept(newValue); // Verify that setter was called with new value
    }

    // Test for Boolean type
    @Test
    void shouldUpdateFieldIfChangedForBoolean() {
        // Arrange
        Supplier<Boolean> getter = mock(Supplier.class); // Avoid unchecked warning with explicit cast
        Consumer<Boolean> setter = mock(Consumer.class);
        Boolean existingValue = false;
        Boolean newValue = true;

        when(getter.get()).thenReturn(existingValue);

        // Act
        UpdateUtils.updateFieldIfChanged(getter, setter, newValue);

        // Assert
        verify(setter).accept(newValue); // Verify that setter was called with new value
    }

    // Additional test to ensure no update if the new value is the same
    @Test
    void shouldNotUpdateFieldIfSameValue() {
        // Arrange
        Supplier<String> getter = mock(Supplier.class);
        Consumer<String> setter = mock(Consumer.class);
        String existingValue = "Same Value";
        String newValue = "Same Value";

        when(getter.get()).thenReturn(existingValue);

        // Act
        UpdateUtils.updateFieldIfChanged(getter, setter, newValue);

        // Assert
        verify(setter, never()).accept(anyString()); // Verify that setter was never called
    }

    // Additional test to ensure no update if the new value is null
    @Test
    void shouldNotUpdateFieldIfNullValue() {
        // Arrange
        Supplier<String> getter = mock(Supplier.class);
        Consumer<String> setter = mock(Consumer.class);
        String existingValue = "Some Value";

        when(getter.get()).thenReturn(existingValue);

        // Act
        UpdateUtils.updateFieldIfChanged(getter, setter, null);

        // Assert
        verify(setter, never()).accept(anyString()); // Verify that setter was never called
    }
}
