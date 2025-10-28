package com.greenew.produtores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Testes para a classe principal da aplicação")
class ProdutoresApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("O método main deve iniciar a aplicação sem erros")
    void main() {
        // Cobre a execução da main
        ProdutoresApplication.main(new String[]{});
    }

}
