package org.apache.activemq.artemis.core.management.impl.view;

import org.apache.activemq.artemis.api.core.JsonUtil;
import org.apache.activemq.artemis.core.management.impl.AbstractControl;
import org.apache.activemq.artemis.core.management.impl.view.predicate.ActiveMQFilterPredicate;
import org.apache.activemq.artemis.core.management.impl.view.predicate.PredicateFilterPart;
import org.apache.activemq.artemis.json.JsonArray;
import org.apache.activemq.artemis.json.JsonObject;
import org.apache.activemq.artemis.json.JsonObjectBuilder;
import org.apache.activemq.artemis.json.JsonValue;
import org.apache.activemq.artemis.utils.JsonLoader;
import org.jspecify.annotations.NonNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotCompliantMBeanException;
import java.util.*;

public class MultipleFilterTest {

    //equals equals tests
    @Test
    public void testLongEqualsIntEquals() {
        test2Filters("aLong", "EQUALS", "1", "anInt", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLongEqualsFloatEquals() {
        test2Filters("aLong", "EQUALS", "1", "aFloat", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLongEqualsBooleanEquals() {
        test2Filters("aLong", "EQUALS", "1", "anObject", "EQUALS", "false", 1, 1L);
    }

    @Test
    public void testLongEqualsStringEquals() {
        test2Filters("aLong", "EQUALS", "1", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testIntEqualsFloatEquals() {
        test2Filters("anInt", "EQUALS", "1", "aFloat", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testIntEqualsBooleanEquals() {
        test2Filters("anInt", "EQUALS", "1", "anObject", "EQUALS", "false", 1, 1L);
    }

    @Test
    public void testIntEqualsStringEquals() {
        test2Filters("anInt", "EQUALS", "1", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testFloatEqualsBooleanEquals() {
        test2Filters("aFloat", "EQUALS", "1", "anObject", "EQUALS", "false", 1, 1L);
    }

    @Test
    public void testFloatEqualsStringEquals() {
        test2Filters("aFloat", "EQUALS", "1", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testBooleanEqualsStringEquals() {
        test2Filters("anObject", "EQUALS", "false", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    //Not Equals equals tests
    @Test
    public void testIntNotEqualsLongEquals() {
        test2Filters("anInt", "NOT_EQUALS", "1000", "aLong", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testIntNotEqualsFloatEquals() {
        test2Filters("anInt", "NOT_EQUALS", "1000", "aFloat", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testIntNotEqualsBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(0, 999);
        test2Filters("anInt", "NOT_EQUALS", "1000", "anObject", "EQUALS", "true", 500, returnedIDs);
    }

    @Test
    public void testIntNotEqualsStringEquals() {
        test2Filters("anObject", "NOT_EQUALS", "true", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLongNotEqualsFloatEquals() {
        test2Filters("aLong", "NOT_EQUALS", "1000", "aFloat", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLongNotEqualsBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(0, 999);
        test2Filters("aLong", "NOT_EQUALS", "1000", "anObject", "EQUALS", "true", 500, returnedIDs);
    }

    @Test
    public void testLongNotEqualsStringEquals() {
        test2Filters("aLong", "NOT_EQUALS", "1000", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLFloatNotEqualsBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(0, 999);
        test2Filters("aFloat", "NOT_EQUALS", "1000", "anObject", "EQUALS", "true", 500, returnedIDs);
    }

    @Test
    public void testFloatNotEqualsStringEquals() {
        test2Filters("aFloat", "NOT_EQUALS", "1000", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testBooleanNotEqualstringEquals() {
        test2Filters("anObject", "NOT_EQUALS", "true", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    //Greater Than equals tests
    @Test
    public void testIntGreaterThanLongEquals() {
        test2Filters("anInt", "GREATER_THAN", "500", "aLong", "EQUALS", "501", 1, 501L);
    }

    @Test
    public void testIntGreaterThanFloatEquals() {
        test2Filters("anInt", "GREATER_THAN", "500", "aFloat", "EQUALS", "501", 1, 501L);
    }

    @Test
    public void testIntGreaterThanBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(501, 999);
        test2Filters("anInt", "GREATER_THAN", "500", "anObject", "EQUALS", "true", 249, returnedIDs);
    }

    @Test
    public void testIntGreaterThanStringEquals() {
        test2Filters("anInt", "GREATER_THAN", "500", "anotherObject", "EQUALS", "501", 1, 501L);
    }

    @Test
    public void testLongGreaterThanFloatEquals() {
        test2Filters("aLong", "GREATER_THAN", "500", "aFloat", "EQUALS", "501", 1, 501L);
    }

    @Test
    public void testLongGreaterThanBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(501, 999);
        test2Filters("aLong", "GREATER_THAN", "500", "anObject", "EQUALS", "true", 249, returnedIDs);
    }

    @Test
    public void testLongGreaterThanStringEquals() {
        test2Filters("aLong", "GREATER_THAN", "500", "anotherObject", "EQUALS", "501", 1, 501L);
    }

    @Test
    public void testLFloatGreaterThanBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(501, 999);
        test2Filters("aFloat", "GREATER_THAN", "500", "anObject", "EQUALS", "true", 249, returnedIDs);
    }

    @Test
    public void testFloatGreaterThanStringEquals() {
        test2Filters("aFloat", "GREATER_THAN", "500", "anotherObject", "EQUALS", "600", 1, 600L);
    }

    //Less Than equals tests
    @Test
    public void testIntLessThanLongEquals() {
        test2Filters("anInt", "LESS_THAN", "500", "aLong", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testIntLessThanFloatEquals() {
        test2Filters("anInt", "LESS_THAN", "500", "aFloat", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testIntLessThanBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(0, 500);
        test2Filters("anInt", "LESS_THAN", "500", "anObject", "EQUALS", "true", 250, returnedIDs);
    }

    @Test
    public void testIntLessThanStringEquals() {
        test2Filters("anInt", "LESS_THAN", "500", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLongLessThanFloatEquals() {
        test2Filters("aLong", "LESS_THAN", "500", "aFloat", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLongLessThanBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(0, 500);
        test2Filters("aLong", "LESS_THAN", "500", "anObject", "EQUALS", "true", 250, returnedIDs);
    }

    @Test
    public void testLongLessThanStringEquals() {
        test2Filters("aLong", "LESS_THAN", "500", "anotherObject", "EQUALS", "1", 1, 1L);
    }

    @Test
    public void testLFloatLessThanBooleanEquals() {
        Long[] returnedIDs = getEvenIDs(0, 500);
        test2Filters("aFloat", "LESS_THAN", "500", "anObject", "EQUALS", "true", 250, returnedIDs);
    }

    @Test
    public void testFloatLessThanStringEquals() {
        test2Filters("aFloat", "LESS_THAN", "500", "anotherObject", "EQUALS", "60", 1, 60L);
    }

    //equals contains tests
    @Test
    public void testAFloatEqualsAStringContains() {
        test2Filters("aFloat", "EQUALS", "500", "anotherObject", "CONTAINS", "5", 1, 500L);
    }

    @Test
    public void testAnIntEqualsAStringContains() {
        test2Filters("anInt", "EQUALS", "500", "anotherObject", "CONTAINS", "5", 1, 500L);
    }

    @Test
    public void testALongEqualsAStringContains() {
        test2Filters("aLong", "EQUALS", "500", "anotherObject", "CONTAINS", "5", 1, 500L);
    }

    @Test
    public void testABooleanEqualsAStringContains() {
        test2Filters("anObject", "EQUALS", "true", "anotherObject", "CONTAINS", "10", 15, 10L, 100L, 102L, 104L, 106L, 108L, 110L, 210L, 310L, 410L, 510L, 610L, 710L, 810L, 910L);
    }


    //not equals contains tests
    @Test
    public void testAFloatEqualsAStringNotContains() {
        test2Filters("aFloat", "EQUALS", "500", "anotherObject", "NOT_CONTAINS", "1", 1, 500L);
    }

    @Test
    public void testAnIntEqualsAStringNotContains() {
        test2Filters("anInt", "EQUALS", "500", "anotherObject", "NOT_CONTAINS", "1", 1, 500L);
    }

    @Test
    public void testALongEqualsAStringNotContains() {
        test2Filters("aLong", "EQUALS", "500", "anotherObject", "NOT_CONTAINS", "1", 1, 500L);
    }

    @Test
    public void testABooleanEqualsAStringNotContains() {
        Long[] allIDs = getEvenIDs(0, 999);
        test2Filters("anObject", "EQUALS", "true", "anotherObject", "NOT_CONTAINS", "998", 499, allIDs);
    }

    //Greater than Not Equals tests
    @Test
    public void testAnIntGreaterThanAStringNotContains() {
        Long[] allIDs = getAllIDs(501, 998);
        test2Filters("anInt", "GREATER_THAN", "500", "anotherObject", "NOT_CONTAINS", "999", 498, allIDs);
    }

    @Test
    public void testALongGreaterThanAStringNotContains() {
        Long[] allIDs = getAllIDs(501, 998);
        test2Filters("aLong", "GREATER_THAN", "500", "anotherObject", "NOT_CONTAINS", "999", 498, allIDs);
    }

    @Test
    public void testAFloatGreaterThanAStringNotContains() {
        Long[] allIDs = getAllIDs(501, 998);
        test2Filters("aFloat", "GREATER_THAN", "500", "anotherObject", "NOT_CONTAINS", "999", 498, allIDs);
    }


    //Less than Not Equals tests
    @Test
    public void testAnIntGreaterThanAStringNotContains() {
        Long[] allIDs = getAllIDs(501, 998);
        test2Filters("anInt", "GREATER_THAN", "500", "anotherObject", "NOT_CONTAINS", "999", 498, allIDs);
    }

    @Test
    public void testALongGreaterThanAStringNotContains() {
        Long[] allIDs = getAllIDs(501, 998);
        test2Filters("aLong", "GREATER_THAN", "500", "anotherObject", "NOT_CONTAINS", "999", 498, allIDs);
    }

    @Test
    public void testAFloatGreaterThanAStringNotContains() {
        Long[] allIDs = getAllIDs(501, 998);
        test2Filters("aFloat", "GREATER_THAN", "500", "anotherObject", "NOT_CONTAINS", "999", 498, allIDs);
    }

    public void test2Filters(String field1, String OP1, String value1, String field2, String OP2, String value2, int resultCount, Long... returnedIDs) {
        TestView myView = null;
        try {
            myView = getTestView(field1, OP1, value1, field2, OP2, value2);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        JsonObject jsonObject = JsonUtil.readJsonObject(myView.getResultsAsJson(1, 1000));
        Assert.assertEquals(resultCount, jsonObject.getInt("count"));
        JsonArray data = jsonObject.getJsonArray("data");
        for (JsonValue value : data) {
            long aLong = Long.parseLong(value.asJsonObject().getString("aLong"));
            Assert.assertTrue("ID " + aLong + " returned", Arrays.stream(returnedIDs).anyMatch(id -> id == aLong));
        }

    }

    private @NonNull TestView getTestView(String field1, String OP1, String value1, String field2, String OP2, String value2) throws Exception {
        TestView myView = new TestView();
        List<TestControl> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new TestControlImpl(i, i, i, i % 2 == 0, "" + i));
        }
        myView.setCollection(list);
        myView.setOptions(createJsonArrayFilter(field1, OP1, value1, field2, OP2, value2));
        return myView;
    }

    public String createJsonArrayFilter(String fieldName, String operationName, String value, String fieldName2, String operationName2, String value2) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("field", fieldName);
        filterMap.put("operation", operationName);
        filterMap.put("value", value);
        Map<String, Object> filterMap2 = new HashMap<>();
        filterMap2.put("field", fieldName2);
        filterMap2.put("operation", operationName2);
        filterMap2.put("value", value2);
        Map<String, Object>[] filtersArray = new HashMap[2];
        filtersArray[0] = filterMap;
        filtersArray[1] = filterMap2;
        Map<String, Object> filtersMap = new HashMap<>();
        filtersMap.put("searchFilters", filtersArray);
        JsonObject jsonFiltersObject = JsonUtil.toJsonObject(filtersMap);
        return jsonFiltersObject.toString();
    }

    public String createJsonArrayFilter(String fieldName, String operationName, String value, String fieldName2, String operationName2, String value2, String fieldName3, String operationName3, String value3) throws Exception {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("field", fieldName);
        filterMap.put("operation", operationName);
        filterMap.put("value", value);
        Map<String, Object> filterMap2 = new HashMap<>();
        filterMap2.put("field", fieldName2);
        filterMap2.put("operation", operationName2);
        filterMap2.put("value", value2);
        Map<String, Object> filterMap3 = new HashMap<>();
        filterMap3.put("field", fieldName3);
        filterMap3.put("operation", operationName3);
        filterMap3.put("value", value3);
        Map<String, Object>[] filtersArray = new HashMap[3];
        filtersArray[0] = filterMap;
        filtersArray[1] = filterMap2;
        filtersArray[2] = filterMap3;
        Map<String, Object> filtersMap = new HashMap<>();
        filtersMap.put("searchFilters", filtersArray);
        JsonObject jsonFiltersObject = JsonUtil.toJsonObject(filtersMap);
        return jsonFiltersObject.toString();
    }

    private static Long @NonNull [] getEvenIDs(int start, int end) {
        List<Long> longs = new ArrayList<>();
        for(int i = start; i <= end; i++) {
            if((i % 2) == 0) {
                longs.add((long) i);
            }
        }
        Long[] retLongs = new Long[longs.size()];
        return longs.toArray(retLongs);
    }

    private static Long @NonNull [] getAllIDs(int start, int end) {
        List<Long> longs = new ArrayList<>();
        for(int i = start; i <= end; i++) {
            longs.add((long) i);
        }
        Long[] retLongs = new Long[longs.size()];
        return longs.toArray(retLongs);
    }


    static class TestView extends ActiveMQAbstractView<TestControl, TestFilterPart> {
        public TestView() {
            super();
            this.predicate = new TestFilterPredicate();
        }

        @Override
        Object getField(TestControl testControl, String fieldName) {
            return null;
        }

        @Override
        public Class getClassT() {
            return null;
        }

        @Override
        public JsonObjectBuilder toJson(TestControl test) {
            JsonObjectBuilder obj = JsonLoader.createObjectBuilder()
                    .add("anInt", toString(test.getAnInt()))
                    .add("aLong", toString(test.getALong()))
                    .add("anInt", toString(test.getAFloat()))
                    .add("anObject", toString(test.getAnObject()))
                    .add("anotherObject", toString(test.getAnotherObject()));
              
            return obj;
        }

        @Override
        public String getDefaultOrderColumn() {
            return "";
        }
    }

    static class TestFilterPredicate extends ActiveMQFilterPredicate<TestControl, TestFilterPart>
    {
        @Override
        protected boolean filter(TestControl input, TestFilterPart filterPart) throws Exception {
            return filterPart.filterPart(input);
        }

        @Override
        public TestFilterPart createFilterPart(String field, String operation, String value) {
            return new TestFilterPart(field, operation, value);
        }
    }
    static class TestFilterPart extends PredicateFilterPart<TestControl> {
        private String field;

        public TestFilterPart(String field, String operation, String value) {
            super(operation, value);
            this.field=field;
        }
        public TestFilterPart(String operation, String value) {
            super(operation, value);
        }

        @Override
        public boolean filterPart(TestControl testControl) throws Exception {
            if (field.equals("aLong"))
                return matchesLong(testControl.getALong());
            if (field.equals("anInt"))
                return matchesInt(testControl.getAnInt());
            if (field.equals("aFloat"))
                return matchesFloat(testControl.getAFloat());
            if (field.equals("anObject"))
                return matches(testControl.getAnObject());
            if (field.equals("anotherObject"))
                return matches(testControl.getAnotherObject());

            return false;
        }
    }

    public static interface TestControl {
        long getALong();
        int getAnInt();
        float getAFloat();
        Object getAnObject();
        Object getAnotherObject();
    }

    static class TestControlImpl extends AbstractControl implements TestControl {

        private final long aLong;
        private final int anInt;
        private final float aFloat;
        private final Object anObject;
        private final Object anotherObject;

        public TestControlImpl(long aLong, int anInt, float aFloat, Object anObject, Object anotherObject) throws NotCompliantMBeanException {
            super(TestControl.class, null);
            this.aLong = aLong;
            this.anInt = anInt;
            this.aFloat = aFloat;
            this.anObject = anObject;
            this.anotherObject = anotherObject;
        }

        @Override
        protected MBeanOperationInfo[] fillMBeanOperationInfo() {
            return new MBeanOperationInfo[0];
        }

        @Override
        protected MBeanAttributeInfo[] fillMBeanAttributeInfo() {
            return new MBeanAttributeInfo[0];
        }

        public long getALong() {
            return aLong;
        }

        @Override
        public int getAnInt() {
            return anInt;
        }

        @Override
        public float getAFloat() {
            return aFloat;
        }

        @Override
        public Object getAnObject() {
            return anObject;
        }

        @Override
        public Object getAnotherObject() {
            return anotherObject;
        }
    }
}
