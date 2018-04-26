package com.nemustech.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test03 {
    Test test;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Test {
//    static class Test {
        Test02 t;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        class Test02 {
//        static class Test02 {
            String s;
        }
    }

    public void test02() throws Exception {
        Test test = new Test();
        Test.Test02 test2 = test.new Test02("1");
//        Test.Test02 test2 = new Test.Test02("1");
        test.setT(test2);
        System.out.println(new Test03(test));

        test = new Test();
        test2 = test.new Test02("2");
//        test2 = new Test.Test02("2");
        test.setT(test2);
        System.out.println(new Test03(test));
    }

    public static void main(String[] args) throws Exception {
        new Test03().test02();
    }
}
