package com.itsmite.novels.core.boot;

import com.itsmite.novels.core.util.CollectionUtil;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NAssert extends Assert {

    public static void isEmpty(Collection<?> collection) {
        isEmpty(collection, "The collection is not empty");
    }

    public static void isEmpty(Collection<?> collection, String message) {
        if (!collection.isEmpty()) {
            failNotEquals(message, Collections.emptyList(), collection);
        }
    }

    public static<T> void assertNonSortedCollectionEquals(Collection<T> expected, Collection<T> actual) {
        assertNonSortedCollectionEquals(expected, actual, null);
    }

    public static<T> void assertNonSortedCollectionEquals(Collection<T> expected, Collection<T> actual, String message) {
        if (!areBothNullOrBothNot(expected, actual)) {
            fail(format(message, expected, actual));
        }
        List expectedList = toList(expected);
        List actualList = toList(expected);
        expectedList.sort(Comparator.comparingInt(NAssert::hashCode));
        actualList.sort(Comparator.comparingInt(NAssert::hashCode));
        assertEquals(message, expectedList, actualList);
    }

    private static void failNotEquals(String message, Object expected, Object actual) {
        fail(format(message, expected, actual));
    }

    static String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && !"".equals(message)) {
            formatted = message + " ";
        }

        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        return equalsRegardingNull(expectedString, actualString) ?
               formatted + "expected: " + formatClassAndValue(expected, expectedString) + " but was: " + formatClassAndValue(actual, actualString) :
               formatted + "expected:<" + expectedString + "> but was:<" + actualString + ">";
    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }

    private static boolean equalsRegardingNull(Object expected, Object actual) {
        if (expected == null) {
            return actual == null;
        } else {
            return isEquals(expected, actual);
        }
    }

    private static boolean isEquals(Object expected, Object actual) {
        return expected.equals(actual);
    }

    private static boolean areBothNullOrBothNot(Object expected, Object actual) {
        return (expected == null && actual == null) || (expected != null && actual != null);
    }

    private static <T> List<T> toList(Collection<T> collection) {
        if (CollectionUtil.isNullOrEmpty(collection)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(collection);
    }

    private static int hashCode(Object object) {
        if (object == null) {
            return 0;
        }
        return object.hashCode();
    }
}
