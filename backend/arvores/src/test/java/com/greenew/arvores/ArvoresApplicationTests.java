package com.greenew.arvores;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Testes para a classe principal da aplicação")
class ArvoresApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    @DisplayName("O método main deve iniciar a aplicação sem erros")
    void main() {
        // Cobre a execução da main
        ArvoresApplication.main(new String[]{});
    }
}
