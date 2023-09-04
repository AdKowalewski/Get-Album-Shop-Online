package pl.britenet.services;

import java.util.Scanner;

public class ScannerService {

    private static final Scanner INSTANCE = null;

    public static Scanner getInstance(){
        if(INSTANCE == null){
            return new Scanner(System.in);
        }
        return INSTANCE;
    }
}
