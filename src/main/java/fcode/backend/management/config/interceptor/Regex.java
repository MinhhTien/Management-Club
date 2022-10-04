package fcode.backend.management.config.interceptor;


import net.bytebuddy.utility.RandomString;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Regex {
    public static final String STUDENT_ID_PATTERN = "^[A-Z][A-Z][0-9]\\d{5}$";
    public static final String MAIL_PATTERN = "^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]{1,}(\\.[a-zA-Z]{1,})+$";

//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        Pattern p = Pattern.compile(MAIL_PATTERN);
//        String mail;
//        while (true) {
//            System.out.print("Input mail: ");
//            mail = sc.nextLine();
//            if (p.matcher(mail).find() && mail.endsWith("@fpt.edu.vn")) {
//                System.out.println("Oke");
//            } else System.err.println("NOT");
//        }
//    }
//    public static void main(String[] args) {
//        String randomCode = RandomString.make(3);
//        String code = null;
//        System.out.println("Chuỗi ban đầu: "+code);
//        if(code == null) {
//            code = "&";
//            System.out.println(code);
//        }
//        String subs = code.substring(code.indexOf("&") +1);
//        System.out.println("Chuỗi con: "+ subs);
//        if(subs == null) {
//            System.out.println("Chuỗi null");
//        }
//        if(subs.isEmpty()) {
//            System.out.println("Chuỗi empty");
//        }
//        if(!subs.isEmpty()) {
//            code = code.replace(subs, randomCode);
//            System.out.println("Test2: "+code);
//        } else {
//            code = code + randomCode;
//            System.out.println("Test3: "+code);
//        }
//    }

}
