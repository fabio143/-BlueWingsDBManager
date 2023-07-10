import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;

public class Blue_Wings {
    private Connection connection;

    public Blue_Wings() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("org.hsql.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:HypersonicSQL:hsql://localhost:8080", "sa", "");
            criarTabelas();

            iniciarGUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void criarTabelas() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE Produto (" + "id INT PRIMARY KEY, " + "nome VARCHAR(50), "
                    + "descricao VARCHAR(100), " + "preco DECIMAL(10, 2), " + "quantidade INT)");

            stmt.executeUpdate("CREATE TABLE Cliente (" + "id INT PRIMARY KEY, " + "nome VARCHAR(50), "
                    + "endereco VARCHAR(100), " + "email VARCHAR(50), " + "telefone VARCHAR(20))");

            stmt.executeUpdate("CREATE TABLE Venda (" + "id INT PRIMARY KEY, " + "id_cliente INT, " + "data DATE, "
                    + "valor_total DECIMAL(10, 2), " + "status VARCHAR(20), "
                    + "FOREIGN KEY (id_cliente) REFERENCES Cliente(id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void iniciarGUI() throws SQLException {

        JFrame frame = new JFrame("Blue Wings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(false);
        ImagePanel backgroundPanel = new ImagePanel();
        backgroundPanel.setLayout(new GridLayout(3, 1));

        JMenuBar menuBar = new JMenuBar();
        JMenu Menu = new JMenu("Opções");

        JMenuItem ShowProductItem = new JMenuItem("Ver Tabela de Produtos");
        JMenuItem ShowClientesItem = new JMenuItem("Ver Tabela de Clientes");
        JMenuItem ShowVendasItem = new JMenuItem("Ver Tabela de Vendas");

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        ShowProductItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Produto");
                    updateTable(table, rs);

                    StringBuilder builder = new StringBuilder();
                    while (rs.next()) {
                        builder.append(rs.getInt("id") + " " + rs.getString("nome") + " " + rs.getString("descricao")
                                + " " + rs.getBigDecimal("preco") + " " + rs.getInt("quantidade") + "\n");
                    }
                    rs.close();
                    stmt.close();

                    exibirRegistros("Produto");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ShowClientesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = connection.createStatement();
                    StringBuilder builder = new StringBuilder();
                    ResultSet rs2 = stmt.executeQuery("SELECT * FROM Cliente");
                    updateTable(table, rs2);

                    while (rs2.next()) {
                        builder.append(rs2.getInt("id") + " " + rs2.getString("nome") + " " + rs2.getString("endereco")
                                + " " + rs2.getString("email") + " " + rs2.getString("telefone") + "\n");
                    }
                    rs2.close();
                    stmt.close();

                    exibirRegistros("Cliente");
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        });
        ShowVendasItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = connection.createStatement();
                    StringBuilder builder = new StringBuilder();
                    ResultSet rs3 = stmt.executeQuery("SELECT * FROM Venda");
                    updateTable(table, rs3);
                    while (rs3.next()) {
                        builder.append(rs3.getInt("id") + " " + rs3.getInt("id_cliente") + " " + rs3.getDate("data")
                                + " " + rs3.getBigDecimal("valor_total") + " " + rs3.getString("status") + "\n");
                    }
                    rs3.close();
                    stmt.close();

                    exibirRegistros("Venda");
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Sair");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        Menu.add(exitMenuItem);
        Menu.add(ShowProductItem);
        Menu.add(ShowClientesItem);
        Menu.add(ShowVendasItem);

        menuBar.add(Menu);
        frame.add(backgroundPanel);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton produtoButton = new JButton("Produto");
        JButton clienteButton = new JButton("Cliente");
        JButton vendaButton = new JButton("Venda");

        produtoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarJanela("Produto", "Adicionar", "Alterar", "Apagar");
            }
        });

        clienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarJanela("Cliente", "Adicionar", "Alterar", "Apagar");
            }
        });

        vendaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarJanela("Venda", "Adicionar", "Alterar", "Apagar");
            }
        });

        buttonPanel.add(produtoButton);
        buttonPanel.add(clienteButton);
        buttonPanel.add(vendaButton);
        frame.add(buttonPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private void criarJanela(String tabela, String btnLabel1, String btnLabel2, String btnLabel3) {
        String Campo1 = "";
        String Campo2 = "";
        String Campo3 = "";
        String Campo4 = "";
        String Campo5 = "";
        JFrame janela = new JFrame(tabela);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janela.setSize(300, 200);
        janela.setResizable(false);
        janela.setLayout(new BorderLayout());

        if (tabela.equals("Produto")) {
            Campo1 = "ID:";
            Campo2 = "Nome:";
            Campo3 = "Descrição:";
            Campo4 = "Preço:";
            Campo5 = "Quantidade:";

        } else if (tabela.equals("Cliente")) {
            Campo1 = "ID:";
            Campo2 = "Nome:";
            Campo3 = "Endereço:";
            Campo4 = "E-mail:";
            Campo5 = "Telefone:";

        } else if (tabela.equals("Venda")) {
            Campo1 = "ID:";
            Campo2 = "ID Cliente:";
            Campo3 = "Data:";
            Campo4 = "Valor Total:";
            Campo5 = "Status:";
        }

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel idLabel = new JLabel(Campo1);
        JTextField idTextField = new JTextField();
        idTextField.setEditable(true);

        JLabel nameLabel = new JLabel(Campo2);
        JTextField nameTextField = new JTextField();
        nameTextField.setEditable(true);

        JLabel descLabel = new JLabel(Campo3);
        JTextField descTextField = new JTextField();
        descTextField.setEditable(true);

        JLabel priceLabel = new JLabel(Campo4);
        JTextField priceTextField = new JTextField();
        priceTextField.setEditable(true);

        JLabel quantityLabel = new JLabel(Campo5);
        JTextField quantityTextField = new JTextField();
        quantityTextField.setEditable(true);

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + tabela + " WHERE id = ?")) {
            // Defina o valor do ID desejado
            int desiredId = 1;
            stmt.setInt(1, desiredId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");
                BigDecimal preco = resultSet.getBigDecimal("preco");
                int quantidade = resultSet.getInt("quantidade");

                idTextField.setText(Integer.toString(id));
                nameTextField.setText(nome);
                descTextField.setText(descricao);
                priceTextField.setText(preco.toString());
                quantityTextField.setText(Integer.toString(quantidade));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        panel.add(idLabel);
        panel.add(idTextField);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(descLabel);
        panel.add(descTextField);
        panel.add(priceLabel);
        panel.add(priceTextField);
        panel.add(quantityLabel);
        panel.add(quantityTextField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton addButton = new JButton(btnLabel1);
        JButton updateButton = new JButton(btnLabel2);
        JButton deleteButton = new JButton(btnLabel3);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabela.equals("Produto")) {
                    adicionarProduto(idTextField.getText(), nameTextField.getText(), descTextField.getText(),
                            priceTextField.getText(), quantityTextField.getText());
                } else if (tabela.equals("Cliente")) {
                    adicionarCliente(idTextField.getText(), nameTextField.getText(), descTextField.getText(),
                            priceTextField.getText(), quantityTextField.getText());
                } else if (tabela.equals("Venda")) {
                    adicionarVenda(idTextField.getText(), nameTextField.getText(), descTextField.getText(),
                            priceTextField.getText(), quantityTextField.getText());
                }

                JOptionPane.showMessageDialog(janela, "Registro adicionado com sucesso.");
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabela.equals("Produto")) {
                    atualizarProduto(idTextField.getText(), nameTextField.getText(), descTextField.getText(),
                            priceTextField.getText(), quantityTextField.getText());
                } else if (tabela.equals("Cliente")) {
                    alterarCliente(idTextField.getText(), nameTextField.getText(), descTextField.getText(),
                            priceTextField.getText(), quantityTextField.getText());
                } else if (tabela.equals("Venda")) {
                    alterarVenda(idTextField.getText(), nameTextField.getText(), descTextField.getText(),
                            priceTextField.getText(), quantityTextField.getText());
                }

                JOptionPane.showMessageDialog(janela, "Registro atualizado com sucesso.");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabela.equals("Produto")) {
                    apagarProduto(idTextField.getText());
                } else if (tabela.equals("Cliente")) {
                    excluirCliente(idTextField.getText());
                } else if (tabela.equals("Venda")) {
                    excluirVenda(idTextField.getText());
                }

                JOptionPane.showMessageDialog(janela, "Registro excluído com sucesso.");
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        janela.add(panel, BorderLayout.CENTER);
        janela.add(buttonPanel, BorderLayout.SOUTH);

        janela.setVisible(true);
    }

    // Produto
    private void adicionarProduto(String id, String nome, String descricao, String preco, String quantidade) {
        try {
            int produtoId = Integer.parseInt(id);
            BigDecimal produtoPreco = new BigDecimal(preco);
            int produtoQuantidade = Integer.parseInt(quantidade);

            String query = "INSERT INTO Produto (id, nome, descricao, preco, quantidade) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, produtoId);
            stmt.setString(2, nome);
            stmt.setString(3, descricao);
            stmt.setBigDecimal(4, produtoPreco);
            stmt.setInt(5, produtoQuantidade);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void atualizarProduto(String id, String nome, String descricao, String preco, String quantidade) {
        try {
            int produtoId = Integer.parseInt(id);
            BigDecimal produtoPreco = new BigDecimal(preco);
            int produtoQuantidade = Integer.parseInt(quantidade);

            String query = "UPDATE Produto SET nome = ?, descricao = ?, preco = ?, quantidade = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setBigDecimal(3, produtoPreco);
            stmt.setInt(4, produtoQuantidade);
            stmt.setInt(5, produtoId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void apagarProduto(String id) {
        try {
            int produtoId = Integer.parseInt(id);

            String query = "DELETE FROM Produto WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, produtoId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cliente
    private void adicionarCliente(String id, String nome, String endereco, String email, String telefone) {
        try {
            int clienteId = Integer.parseInt(id);

            String query = "INSERT INTO Cliente (id, nome, endereco, email, telefone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, clienteId);
            stmt.setString(2, nome);
            stmt.setString(3, endereco);
            stmt.setString(4, email);
            stmt.setString(5, telefone);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void alterarCliente(String id, String nome, String endereco, String email, String telefone) {
        try {
            int clienteId = Integer.parseInt(id);

            String query = "UPDATE Cliente SET nome = ?, endereco = ?, email = ?, telefone = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, endereco);
            stmt.setString(3, email);
            stmt.setString(4, telefone);
            stmt.setInt(5, clienteId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void excluirCliente(String id) {
        try {
            int clienteId = Integer.parseInt(id);

            String query = "DELETE FROM Cliente WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, clienteId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Venda
    private void adicionarVenda(String id, String idCliente, String data, String valorTotal, String status) {
        try {
            int vendaId = Integer.parseInt(id);
            int vendaIdCliente = Integer.parseInt(idCliente);
            BigDecimal vendaValorTotal = new BigDecimal(valorTotal);
            java.sql.Date vendaData = java.sql.Date.valueOf(data); // Converte
                                                                   // a
                                                                   // String
                                                                   // para
                                                                   // java.sql.Date

            String query = "INSERT INTO Venda (id, id_cliente, data, valor_total, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, vendaId);
            stmt.setInt(2, vendaIdCliente);
            stmt.setDate(3, vendaData); // Define
                                        // a
                                        // data
                                        // usando
                                        // setDate
            stmt.setBigDecimal(4, vendaValorTotal);
            stmt.setString(5, status);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void alterarVenda(String id, String idCliente, String data, String valorTotal, String status) {
        try {
            int vendaId = Integer.parseInt(id);
            int vendaIdCliente = Integer.parseInt(idCliente);
            BigDecimal vendaValorTotal = new BigDecimal(valorTotal);
            java.sql.Date vendaData = java.sql.Date.valueOf(data);

            String query = "UPDATE Venda SET id_cliente = ?, data = ?, valor_total = ?, status = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, vendaIdCliente);
            stmt.setDate(2, vendaData);
            stmt.setBigDecimal(3, vendaValorTotal);
            stmt.setString(4, status);
            stmt.setInt(5, vendaId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void excluirVenda(String id) {
        try {
            int vendaId = Integer.parseInt(id);

            String query = "DELETE FROM Venda WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, vendaId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void atualizarRegistro(String tabela, String id, String nome, String descricao, String preco,
            String quantidade) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE " + tabela + " SET nome = ?, descricao = ?, preco = ?, quantidade = ? WHERE id = ?")) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setBigDecimal(3, new BigDecimal(preco));
            stmt.setInt(4, Integer.parseInt(quantidade));
            stmt.setInt(5, Integer.parseInt(id));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void apagarRegistro(String tabela, String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tabela + " WHERE id = ?")) {
            stmt.setInt(1, Integer.parseInt(id));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exibirRegistros(String tabela) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + tabela);

            // Obtém os metadados do ResultSet
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Obtém o número de colunas da tabela
            int columnCount = metaData.getColumnCount();

            // Cria um vetor de nomes de coluna
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Cria uma lista de arrays de objetos para armazenar os dados da tabela
            List<Object[]> data = new ArrayList<>();

            // Preenche a lista de dados com os registros da tabela
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                data.add(rowData);
            }

            // Cria um modelo de tabela personalizado
            DefaultTableModel tableModel = new DefaultTableModel(data.toArray(new Object[0][0]), columnNames);

            // Cria um JTable com o modelo de tabela personalizado
            JTable table = new JTable(tableModel);

            // Cria um JScrollPane para exibir a tabela com barras de rolagem
            JScrollPane scrollPane = new JScrollPane(table);

            // Cria um JFrame para exibir a tabela
            JFrame tableFrame = new JFrame(tabela);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.add(scrollPane);
            tableFrame.setSize(500, 300);
            tableFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class ImagePanel extends JPanel {
        private BufferedImage image;

        public ImagePanel() {
            try {
                InputStream arquivo = getClass().getResourceAsStream("Imagem_Fundo.jpg");
                image = ImageIO.read(arquivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    private JTable createTable(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        DefaultTableModel model = new DefaultTableModel();

        // Adicionar os nomes das colunas ao modelo
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            model.addColumn(metaData.getColumnLabel(columnIndex));
        }

        // Adicionar as linhas ao modelo
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                rowData[columnIndex - 1] = rs.getObject(columnIndex);
            }
            model.addRow(rowData);
        }

        return new JTable(model);
    }

    private void showTableFrame(JTable table, String title) {
        JFrame tableFrame = new JFrame(title);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.add(new JScrollPane(table));
        tableFrame.setSize(500, 300);
        tableFrame.setVisible(true);

    }

    private void updateTable(JTable table, ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        DefaultTableModel model = new DefaultTableModel();

        // Adicionar os nomes das colunas ao modelo
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            model.addColumn(metaData.getColumnLabel(columnIndex));
        }

        // Adicionar as linhas ao modelo
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                rowData[columnIndex - 1] = rs.getObject(columnIndex);
            }
            model.addRow(rowData);
        }

        table.setModel(model);
    }

    private void exibirTabela(String title, ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        DefaultTableModel model = new DefaultTableModel();

        // Adicionar os nomes das colunas ao modelo
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            model.addColumn(metaData.getColumnName(columnIndex));
        }

        // Adicionar os dados ao modelo
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                rowData[columnIndex - 1] = rs.getObject(columnIndex);
            }
            model.addRow(rowData);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame tableFrame = new JFrame(title);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.add(scrollPane);
        tableFrame.setSize(500, 300);
        tableFrame.setVisible(true);
    }
    // Atualizar dados

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Blue_Wings();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
