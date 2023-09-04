package pl.britenet.services;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ScannerService {

    private static final Scanner INSTANCE = null;

    public static Scanner getInstance(){
        if(INSTANCE == null){
            return new Scanner(System.in);
        }
        return INSTANCE;
    }
}
