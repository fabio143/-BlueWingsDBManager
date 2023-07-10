# Blue Wings DB Manager

Este é um aplicativo Java chamado "Blue Wings DB Manager" que gerencia um banco de dados de produtos, clientes e vendas. Ele fornece uma interface gráfica do usuário (GUI) para visualizar, adicionar, atualizar e excluir registros no banco de dados.

## Descrição

O "Blue Wings DB Manager" é uma aplicação Java que permite a gestão de um banco de dados relacionado a produtos, clientes e vendas. O aplicativo possui uma interface gráfica intuitiva que facilita a interação com o banco de dados, permitindo visualizar, adicionar, atualizar e excluir registros em tempo real.

## Funcionalidades

- Visualizar tabelas de produtos, clientes e vendas.
- Adicionar novos produtos, clientes e vendas ao banco de dados.
- Atualizar informações de produtos, clientes e vendas existentes.
- Excluir produtos, clientes e vendas do banco de dados.

## Dependências

Este aplicativo requer as seguintes dependências:

- Java Development Kit (JDK)
- Pacote javax.swing
- Pacote javax.imageio
- Pacote java.sql
- Driver de banco de dados HypersonicSQL (org.hsql.jdbcDriver)
- hsql.jar

## Configuração

Siga as etapas abaixo para configurar o aplicativo:

1. Certifique-se de ter o JDK instalado em seu sistema.
2. Faça o download do driver de banco de dados HypersonicSQL (hsql.jar) e coloque-o na pasta do projeto.
3. Compile o código-fonte Java para gerar o arquivo executável Blue_Wings.class.
4. Certifique-se de ter uma instância do banco de dados HypersonicSQL em execução no localhost na porta 8080. Caso não tenha, execute o seguinte comando no terminal: `java -classpath .;hsql.jar org.hsql.Server`.
5. Execute as instruções SQL fornecidas no método `criarTabelas()` da classe Blue_Wings para criar as tabelas necessárias (Produto, Cliente, Venda) no banco de dados.

## Uso

1. Execute a classe Blue_Wings para iniciar o aplicativo.
2. A interface gráfica será exibida, com opções para visualizar as tabelas de produtos, clientes e vendas.
3. Selecione a tabela desejada para visualizar os registros existentes.
4. Use os botões "Adicionar", "Atualizar" e "Excluir" para manipular os registros no banco de dados.
5. Ao selecionar a opção "Adicionar" ou "Atualizar", uma nova janela será exibida para inserir ou atualizar as informações do registro.
6. Após executar qualquer ação, como adicionar ou atualizar registros, uma mensagem de confirmação será exibida.


## Contribuidores 
-Fabio Carciofi Junior 

-Leonardo Vieira Maurino 
