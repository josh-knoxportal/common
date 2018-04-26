package com.nemustech.common;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nemustech.common.model.Address;
import com.nemustech.common.model.Member;
import com.nemustech.common.model.Order;

public class OptionalTests {
    public OptionalTests() {
        test1();
        System.out.println();
        test2();
        System.out.println();
        test3();
        System.out.println();
        test4();
        System.out.println();
        test5();
        System.out.println();
    }

    void test1() {
        Member aMember = new Member();

        Optional<Member> maybeMember = Optional.empty();
        System.out.println(maybeMember);
        maybeMember = Optional.of(aMember);
        System.out.println(maybeMember);
        maybeMember = Optional.ofNullable(aMember);
        System.out.println(maybeMember);
        Optional maybeNotMember = Optional.ofNullable(null);
        System.out.println(maybeNotMember);
    }

    void test2() {
        String text = null;

        int length;
        if (text != null) {
            length = text.length();
        } else {
            length = 0;
        }
        System.out.println(length);

        Optional<String> maybeText = Optional.ofNullable(text);
        if (maybeText.isPresent()) {
            length = maybeText.get().length();
        } else {
            length = 0;
        }
        System.out.println(length);

        length = Optional.ofNullable(text).map(String::length).orElse(0);
        System.out.println(length);
    }

    void test3() {
        Map<Integer, String> cities = new HashMap<>();
        cities.put(1, "Seoul");
        cities.put(2, "Busan");
        cities.put(3, "Daejeon");

        String city = cities.get(4); // returns null
        int length = city == null ? 0 : city.length(); // null check
        System.out.println(length);

        Optional<String> maybeCity = Optional.ofNullable(cities.get(4)); // Optional
        length = maybeCity.map(String::length).orElse(0); // null-safe
        System.out.println(length);
    }

    void test4() {
        List<String> cities = Arrays.asList("Seoul", "Busan", "Daejeon");

        String city = null;
        try {
            city = cities.get(3); // throws exception
        } catch (ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
        }

        int length = city == null ? 0 : city.length(); // null check
        System.out.println(length);

        Optional<String> maybeCity = getAsOptional(cities, 3); // Optional
        length = maybeCity.map(String::length).orElse(0); // null-safe
        System.out.println("length: " + length);

        maybeCity = getAsOptional(cities, 3); // Optional
        maybeCity.ifPresent(city1 -> {
            System.out.println("length: " + city1.length());
        });
    }

    void test5() {
        Order order = new Order();
        order.setDate(new Date());

        try {
            System.out.println(getCityOfMemberFromOrder1(order));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        System.out.println(getCityOfMemberFromOrder2(order));
        System.out.println(getCityOfMemberFromOrder3(order));
        System.out.println(getCityOfMemberFromOrder4(order));
        System.out.println(getCityOfMemberFromOrder5(order));
        System.out.println(getMemberIfOrderWithin1(order, 1));
        System.out.println(getMemberIfOrderWithin2(order, 1));
    }

    public static <T> Optional<T> getAsOptional(List<T> list, int index) {
        try {
            return Optional.of(list.get(index));
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    String getCityOfMemberFromOrder1(Order order) {
        return order.getMember().getAddress().getCity();
    }

    String getCityOfMemberFromOrder2(Order order) {
        if (order != null) {
            Member member = order.getMember();
            if (member != null) {
                Address address = member.getAddress();
                if (address != null) {
                    String city = address.getCity();
                    if (city != null) {
                        return city;
                    }
                }
            }
        }
        return "Seoul"; // default
    }

    String getCityOfMemberFromOrder3(Order order) {
        if (order == null) {
            return "Seoul";
        }
        Member member = order.getMember();
        if (member == null) {
            return "Seoul";
        }
        Address address = member.getAddress();
        if (address == null) {
            return "Seoul";
        }
        String city = address.getCity();
        if (city == null) {
            return "Seoul";
        }
        return city;
    }

    String getCityOfMemberFromOrder4(Order order) {
        Optional<Order> maybeOrder = Optional.ofNullable(order);
        if (maybeOrder.isPresent()) {
            Optional<Member> maybeMember = Optional.ofNullable(maybeOrder.get().getMember());
            if (maybeMember.isPresent()) {
                Optional<Address> maybeAddress = Optional.ofNullable(maybeMember.get().getAddress());
                if (maybeAddress.isPresent()) {
                    Optional<String> maybeCity = Optional.ofNullable(maybeAddress.get().getCity());
                    if (maybeCity.isPresent()) {
                        return maybeCity.get();
                    }
                }
            }
        }
        return "Seoul";
    }

    /* 주문을 한 회원이 살고 있는 도시를 반환한다 */
    String getCityOfMemberFromOrder5(Order order) {
        return Optional.ofNullable(order)
                       .map(Order::getMember)
                       .map(Member::getAddress)
                       .map(Address::getCity)
                       .orElse("Seoul");
    }

    Member getMemberIfOrderWithin1(Order order, int min) {
        if (order != null && order.getDate().getTime() > System.currentTimeMillis() - min * 1000) {
            return order.getMember();
        }
        return null;
    }

    Optional<Member> getMemberIfOrderWithin2(Order order, int min) {
        return Optional.ofNullable(order)
                       .filter(o -> o.getDate().getTime() > System.currentTimeMillis() - min * 1000)
                       .map(Order::getMember);
    }

    public static void main(String[] args) {
        new OptionalTests();
    }
}
