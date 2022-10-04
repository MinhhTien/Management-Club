package fcode.backend.management.config.interceptor;


import net.bytebuddy.utility.RandomString;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Regex {
    public static final String STUDENT_ID_PATTERN = "^[A-Z][A-Z][0-9]\\d{5}$";
    public static final String MAIL_PATTERN = "^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]{1,}(\\.[a-zA-Z]{1,})+$";

}
