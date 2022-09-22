package com.ironhack.ironbank;

import com.ironhack.ironbank.configuration.PopulateConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PopulateConfig.class)
class IronbankApplicationTests {

    @Test
    void contextLoads() {
    }

}
