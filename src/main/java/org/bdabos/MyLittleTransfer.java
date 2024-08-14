package org.bdabos;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class MyLittleTransfer {

    public static void main(String[] args) {
        SpringApplication.run(MyLittleTransfer.class, args);
    }
}