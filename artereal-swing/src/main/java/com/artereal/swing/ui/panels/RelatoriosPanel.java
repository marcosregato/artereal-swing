package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Painel de Relatórios
 */
public class RelatoriosPanel extends JPanel {
    
    public RelatoriosPanel() {
        initializeComponents();
        setupLayout();
        setupEvents();
    }
    
    private void initializeComponents() {
        // Componentes serão adicionados no setupLayout
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📈 Sistema de Relatórios", "Geração e visualização de relatórios");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(new JTextField(20), new JButton("🔍 Buscar"));
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Parâmetros do Relatório");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Período do Relatório
        JPanel periodoPanel = new JPanel(new BorderLayout());
        periodoPanel.setBackground(Color.WHITE);
        periodoPanel.setBorder(BorderFactory.createTitledBorder("📅 Período do Relatório"));
        
        // Layout manual para garantir Data Início e Data Fim na mesma linha
        JPanel periodoContent = new JPanel(new GridLayout(1, 4, 10, 5)); // 1 linha, 4 colunas
        periodoContent.setBackground(Color.WHITE);
        
        // Adicionar componentes na mesma linha
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Início:"));
        JTextField dataInicioField = new JTextField(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        PadraoLayout.estilizarCampoPeriodoRelatorio(dataInicioField);
        dataInicioField.setColumns(12);
        periodoContent.add(dataInicioField);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Fim:"));
        JTextField dataFimField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        PadraoLayout.estilizarCampoPeriodoRelatorio(dataFimField);
        dataFimField.setColumns(12);
        periodoContent.add(dataFimField);
        
        periodoPanel.add(periodoContent, BorderLayout.CENTER);
        formContainer.add(periodoPanel, BorderLayout.NORTH);
        
        // SEÇÃO 2: Configurações do Relatório
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setBackground(Color.WHITE);
        configPanel.setBorder(BorderFactory.createTitledBorder("⚙️ Configurações do Relatório"));
        
        JPanel configContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        configContent.setBackground(Color.WHITE);
        
        configContent.add(PadraoLayout.criarLabelFormulario("Tipo:"));
        JComboBox<String> tipoComboBox = new JComboBox<>(new String[]{
            "Todos", "Irmãos", "Financeiro", "Sessões", "Biblioteca", "Visitantes", "Eventos", "Documentos"
        });
        PadraoLayout.estilizarComboBox(tipoComboBox);
        tipoComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX"); // Tamanho ideal
        configContent.add(tipoComboBox);
        
        configContent.add(PadraoLayout.criarLabelFormulario("Formato:"));
        JComboBox<String> formatoComboBox = new JComboBox<>(new String[]{
            "PDF", "Excel", "HTML", "CSV"
        });
        PadraoLayout.estilizarComboBox(formatoComboBox);
        configContent.add(formatoComboBox);
        
        configPanel.add(configContent, BorderLayout.CENTER);
        formContainer.add(configPanel, BorderLayout.CENTER);
        
        // SEÇÃO 3: Seleção de Relatórios
        JPanel relatoriosPanel = new JPanel(new BorderLayout());
        relatoriosPanel.setBackground(Color.WHITE);
        relatoriosPanel.setBorder(BorderFactory.createTitledBorder("📋 Relatórios Disponíveis"));
        
        JPanel relatoriosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        relatoriosContent.setBackground(Color.WHITE);
        
        relatoriosContent.add(PadraoLayout.criarLabelFormulario("Relatório:"));
        JComboBox<String> relatorioComboBox = new JComboBox<>(new String[]{
            "👥 Relatório de Irmãos - Lista completa de todos os irmãos cadastrados",
            "💰 Relatório Financeiro - Movimentações financeiras e balanço",
            "📝 Relatório de Sessões - Histórico de sessões maçônicas",
            "📚 Relatório da Biblioteca - Livros e empréstimos",
            "🚪 Relatório de Visitantes - Controle de acesso de visitantes",
            "🎉 Relatório de Eventos - Eventos e participações",
            "📄 Relatório de Documentos - Documentos digitais e assinaturas",
            "📊 Relatório Geral - Resumo completo do sistema",
            "⚙️ Relatório Customizado - Parâmetros personalizados"
        });
        PadraoLayout.estilizarComboBox(relatorioComboBox);
        relatorioComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX"); // Tamanho menor e mais adequado
        relatoriosContent.add(relatorioComboBox);
        
        relatoriosPanel.add(relatoriosContent, BorderLayout.CENTER);
        
        // Painel de botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(PadraoLayout.criarBotao("🔍 Gerar Relatório", new Color(40, 167, 69)));
        botoesPanel.add(PadraoLayout.criarBotao("📧 Enviar por E-mail", new Color(70, 130, 180)));
        botoesPanel.add(PadraoLayout.criarBotao("📱 WhatsApp", new Color(129, 230, 217)));
        botoesPanel.add(PadraoLayout.criarBotao("⚙️ Agendar", new Color(255, 193, 7)));
        
        formContainer.add(relatoriosPanel, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📊 Histórico de Relatórios Gerados");
        
        // Tabela de histórico de relatórios
        String[] colunas = {"Data", "Tipo", "Formato", "Status", "Usuário", "Ações"};
        Object[][] dados = {
            {"15/04/2024", "Irmãos", "PDF", "Concluído", "Admin", "Visualizar"},
            {"14/04/2024", "Financeiro", "Excel", "Concluído", "Admin", "Visualizar"},
            {"13/04/2024", "Sessões", "PDF", "Processando", "Admin", "Aguardar"},
            {"12/04/2024", "Biblioteca", "HTML", "Concluído", "João", "Visualizar"},
            {"11/04/2024", "Geral", "PDF", "Concluído", "Admin", "Visualizar"}
        };
        
        DefaultTableModel tableModel = new DefaultTableModel(dados, colunas);
        JTable relatoriosTable = new JTable(tableModel);
        PadraoLayout.configurarTabela(relatoriosTable);
        
        JScrollPane tableScrollPane = new JScrollPane(relatoriosTable);
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (60%), Tabela abaixo (40%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(400);
        verticalSplitPane.setResizeWeight(0.6);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        System.out.println("[RelatoriosPanel] setupEvents() - Configurando ActionListeners dos botões");
        
        // Adicionar ActionListener para o botão Gerar Relatório
        // Como o botão é criado com PadraoLayout.criarBotao(), precisamos encontrar o botão no painel
        int botoesEncontrados = 0;
        
        for (Component comp : this.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel mainPanel = (JPanel) comp;
                System.out.println("[RelatoriosPanel] setupEvents() - Painel principal encontrado");
                
                for (Component mainComp : mainPanel.getComponents()) {
                    if (mainComp instanceof JPanel) {
                        JPanel contentPanel = (JPanel) mainComp;
                        System.out.println("[RelatoriosPanel] setupEvents() - Painel de conteúdo encontrado");
                        
                        for (Component contentComp : contentPanel.getComponents()) {
                            if (contentComp instanceof JSplitPane) {
                                JSplitPane splitPane = (JSplitPane) contentComp;
                                System.out.println("[RelatoriosPanel] setupEvents() - JSplitPane encontrado");
                                
                                Component topComponent = splitPane.getTopComponent();
                                if (topComponent instanceof JPanel) {
                                    JPanel formPanel = (JPanel) topComponent;
                                    System.out.println("[RelatoriosPanel] setupEvents() - Painel de formulário encontrado");
                                    
                                    for (Component formComp : formPanel.getComponents()) {
                                        if (formComp instanceof JPanel) {
                                            JPanel formContainer = (JPanel) formComp;
                                            System.out.println("[RelatoriosPanel] setupEvents() - Container de formulário encontrado");
                                            
                                            for (Component containerComp : formContainer.getComponents()) {
                                                if (containerComp instanceof JPanel) {
                                                    JPanel botoesPanel = (JPanel) containerComp;
                                                    System.out.println("[RelatoriosPanel] setupEvents() - Painel de botões encontrado com " + botoesPanel.getComponentCount() + " componentes");
                                                    
                                                    for (Component buttonComp : botoesPanel.getComponents()) {
                                                        if (buttonComp instanceof JButton) {
                                                            JButton button = (JButton) buttonComp;
                                                            String buttonText = button.getText();
                                                            System.out.println("[RelatoriosPanel] setupEvents() - Botão encontrado: '" + buttonText + "'");
                                                            
                                                            if (buttonText.contains("Gerar Relatório")) {
                                                                button.addActionListener(e -> {
                                                                    System.out.println("[RelatoriosPanel] ActionListener - Botão 'Gerar Relatório' clicado");
                                                                    gerarRelatorio();
                                                                });
                                                                botoesEncontrados++;
                                                                System.out.println("[RelatoriosPanel] setupEvents() - ActionListener adicionado ao botão 'Gerar Relatório'");
                                                                
                                                            } else if (buttonText.contains("Enviar por E-mail")) {
                                                                button.addActionListener(e -> {
                                                                    System.out.println("[RelatoriosPanel] ActionListener - Botão 'Enviar por E-mail' clicado");
                                                                    enviarEmailRelatorio();
                                                                });
                                                                botoesEncontrados++;
                                                                System.out.println("[RelatoriosPanel] setupEvents() - ActionListener adicionado ao botão 'Enviar por E-mail'");
                                                                
                                                            } else if (buttonText.contains("WhatsApp")) {
                                                                button.addActionListener(e -> {
                                                                    System.out.println("[RelatoriosPanel] ActionListener - Botão 'WhatsApp' clicado");
                                                                    enviarRelatorioPorWhatsApp();
                                                                });
                                                                botoesEncontrados++;
                                                                System.out.println("[RelatoriosPanel] setupEvents() - ActionListener adicionado ao botão 'WhatsApp'");
                                                                
                                                            } else if (buttonText.contains("Agendar")) {
                                                                System.out.println("[RelatoriosPanel] setupEvents() - Botão 'Agendar' identificado mas sem ActionListener implementado");
                                                                
                                                            } else {
                                                                System.out.println("[RelatoriosPanel] setupEvents() - Botão desconhecido: '" + buttonText + "'");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("[RelatoriosPanel] setupEvents() - Configuração concluída. " + botoesEncontrados + " ActionListeners adicionados");
    }
    
    private void gerarRelatorio() {
        System.out.println("[RelatoriosPanel] gerarRelatorio() - Iniciando geração de relatório");
        
        try {
            // Obter valores dos campos (simplificado por enquanto)
            String tipoRelatorio = "Todos"; // Seria obtido do ComboBox
            String formato = "PDF"; // Seria obtido do ComboBox
            
            System.out.println("[RelatoriosPanel] gerarRelatorio() - Tipo: " + tipoRelatorio + ", Formato: " + formato);
            System.out.println("[RelatoriosPanel] gerarRelatorio() - Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            // Simular processamento do relatório
            System.out.println("[RelatoriosPanel] gerarRelatorio() - Processando dados do relatório...");
            Thread.sleep(500); // Simula processamento
            System.out.println("[RelatoriosPanel] gerarRelatorio() - Relatório processado com sucesso");
            
            // Exibir mensagem de sucesso
            JOptionPane.showMessageDialog(this, 
                "Relatório '" + tipoRelatorio + "' gerado com sucesso!\n" +
                "Formato: " + formato + "\n" +
                "Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "Status: Concluído", 
                "Relatório Gerado", 
                JOptionPane.INFORMATION_MESSAGE);
                
            System.out.println("[RelatoriosPanel] gerarRelatorio() - Mensagem de sucesso exibida ao usuário");
            System.out.println("[RelatoriosPanel] gerarRelatorio() - Geração de relatório concluída com sucesso");
                
            // Adicionar na tabela de histórico (simplificado)
            // Em uma implementação real, isso viria do banco de dados
            
        } catch (InterruptedException ex) {
            System.err.println("[RelatoriosPanel] gerarRelatorio() - Erro de interrupção: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Erro ao gerar relatório: Processamento interrompido", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.err.println("[RelatoriosPanel] gerarRelatorio() - Erro ao gerar relatório: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao gerar relatório: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarEmailRelatorio() {
        System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Iniciando envio de relatório por e-mail");
        
        try {
            // Obter valores dos campos (simplificado por enquanto)
            String tipoRelatorio = "Todos"; // Seria obtido do ComboBox
            String formato = "PDF"; // Seria obtido do ComboBox
            
            System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Tipo: " + tipoRelatorio + ", Formato: " + formato);
            
            // Simular diálogo de envio de email
            System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Abrindo diálogo de envio de e-mail");
            JTextField emailField = new JTextField("admin@artereal.com", 30);
            JTextField assuntoField = new JTextField("Relatório " + tipoRelatorio + " - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 30);
            JTextArea mensagemField = new JTextArea("Prezado(a),\n\nSegue em anexo o relatório solicitado.\n\nAtenciosamente,\nSistema ArteReal", 5, 30);
            
            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            panel.add(new JLabel("Para:"));
            panel.add(emailField);
            panel.add(new JLabel("Assunto:"));
            panel.add(assuntoField);
            panel.add(new JLabel("Mensagem:"));
            panel.add(new JScrollPane(mensagemField));
            
            System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Diálogo configurado, exibindo para usuário");
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Enviar Relatório por E-mail", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String email = emailField.getText().trim();
                String assunto = assuntoField.getText().trim();
                String mensagem = mensagemField.getText().trim();
                
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Usuário confirmou envio");
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Para: " + email);
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Assunto: " + assunto);
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Mensagem: " + mensagem.substring(0, Math.min(50, mensagem.length())) + "...");
                
                if (email.isEmpty()) {
                    System.err.println("[RelatoriosPanel] enviarEmailRelatorio() - Erro: campo e-mail vazio");
                    JOptionPane.showMessageDialog(this, 
                        "O campo 'Para' é obrigatório!", 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Enviar e-mail real usando JavaMail com anexo
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Enviando e-mail real com anexo...");
                
                // Verificar configurações de e-mail
                com.artereal.swing.service.EmailService emailService = new com.artereal.swing.service.EmailService();
                if (!emailService.validarConfiguracoes()) {
                    System.err.println("[RelatoriosPanel] enviarEmailRelatorio() - Configurações de e-mail inválidas");
                    JOptionPane.showMessageDialog(this, 
                        "Configurações de e-mail inválidas!\n" +
                        "Verifique as configurações SMTP no EmailService.\n" +
                        "Usuário: " + emailService.getEmailRemetente(), 
                        "Erro de Configuração", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Gerar arquivo de relatório para anexar
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Gerando arquivo de relatório...");
                java.io.File arquivoRelatorio = com.artereal.swing.service.RelatorioService.gerarArquivoRelatorio(tipoRelatorio, formato);
                
                if (arquivoRelatorio == null) {
                    System.err.println("[RelatoriosPanel] enviarEmailRelatorio() - Erro ao gerar arquivo de relatório");
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao gerar arquivo de relatório para anexo!", 
                        "Erro de Geração", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Arquivo gerado: " + arquivoRelatorio.getName());
                
                // Construir mensagem completa
                String mensagemCompleta = mensagem + "\n\n" +
                    "---\n" +
                    "Relatório gerado pelo Sistema ArteReal\n" +
                    "Tipo: " + tipoRelatorio + "\n" +
                    "Formato: " + formato + "\n" +
                    "Data: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
                    "Arquivo anexo: " + arquivoRelatorio.getName() + "\n" +
                    "---";
                
                // Enviar e-mail com anexo usando EmailService
                boolean enviado = emailService.enviarEmailComAnexo(
                    email, assunto, mensagemCompleta, arquivoRelatorio);
                
                if (enviado) {
                    System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - E-mail com anexo enviado com sucesso");
                    JOptionPane.showMessageDialog(this, 
                        "Relatório '" + tipoRelatorio + "' enviado com sucesso!\n" +
                        "Para: " + email + "\n" +
                        "Assunto: " + assunto + "\n" +
                        "Formato: " + formato + "\n" +
                        "Arquivo anexo: " + arquivoRelatorio.getName() + "\n" +
                        "Data: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
                        "Status: Enviado com anexo", 
                        "E-mail Enviado", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.err.println("[RelatoriosPanel] enviarEmailRelatorio() - Falha ao enviar e-mail");
                    JOptionPane.showMessageDialog(this, 
                        "Falha ao enviar o relatório por e-mail!\n" +
                        "Verifique:\n" +
                        "1. Conexão com a internet\n" +
                        "2. Configurações SMTP\n" +
                        "3. Autenticação do servidor\n" +
                        "4. Endereço de e-mail do destinatário", 
                        "Erro de Envio", 
                        JOptionPane.ERROR_MESSAGE);
                }
                    
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Mensagem de sucesso exibida ao usuário");
            } else {
                System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Usuário cancelou envio de e-mail");
            }
            
            System.out.println("[RelatoriosPanel] enviarEmailRelatorio() - Operação de envio de e-mail concluída");
            
        } catch (Exception ex) {
            System.err.println("[RelatoriosPanel] enviarEmailRelatorio() - Erro ao enviar e-mail: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao enviar e-mail: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarRelatorioPorWhatsApp() {
        System.out.println("[RelatoriosPanel] enviarRelatorioPorWhatsApp() - Iniciando envio de relatório por WhatsApp");
        
        // Obter valores dos campos (simplificado por enquanto)
        String tipoRelatorio = "Todos"; // Seria obtido do ComboBox
        String formato = "PDF"; // Seria obtido do ComboBox
        
        System.out.println("[RelatoriosPanel] enviarRelatorioPorWhatsApp() - Tipo: " + tipoRelatorio + ", Formato: " + formato);
        
        // Criar diálogo para envio via WhatsApp
        JTextField telefoneField = new JTextField("", 30);
        JTextArea mensagemField = new JTextArea(
            "📊 *Relatório ArteReal*\n\n" +
            "*Tipo:* " + tipoRelatorio + "\n" +
            "*Formato:* " + formato + "\n" +
            "*Data:* " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
            "*Hora:* " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) + "\n\n" +
            "📎 *Anexo:* O relatório será gerado e anexado manualmente\n\n" +
            "_Enviado pelo Sistema ArteReal v2.0.0_", 5, 30);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Telefone (com DDD):"));
        panel.add(telefoneField);
        panel.add(new JLabel("Mensagem:"));
        panel.add(new JScrollPane(mensagemField));
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Enviar Relatório por WhatsApp", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String telefone = telefoneField.getText().trim();
            
            if (telefone.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "O campo 'Telefone' é obrigatório!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar número de telefone
            if (!com.artereal.swing.service.WhatsAppService.validarNumeroTelefone(telefone)) {
                JOptionPane.showMessageDialog(this, 
                    "Número de telefone inválido!\n" +
                    "Formato esperado: DDD + número (ex: 11999998888)", 
                    "Erro de Validação", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("[RelatoriosPanel] enviarRelatorioPorWhatsApp() - Enviando para: " + telefone);
            
            // Gerar arquivo de relatório para anexar
            System.out.println("[RelatoriosPanel] enviarRelatorioPorWhatsApp() - Gerando arquivo de relatório...");
            java.io.File arquivoRelatorio = com.artereal.swing.service.RelatorioService.gerarArquivoRelatorio(tipoRelatorio, formato);
            
            if (arquivoRelatorio == null) {
                System.err.println("[RelatoriosPanel] enviarRelatorioPorWhatsApp() - Erro ao gerar arquivo de relatório");
                JOptionPane.showMessageDialog(this, 
                    "Erro ao gerar arquivo de relatório para anexo!", 
                    "Erro de Geração", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("[RelatoriosPanel] enviarRelatorioPorWhatsApp() - Arquivo gerado: " + arquivoRelatorio.getName());
            
            // Construir mensagem completa com informações do arquivo
            String mensagemCompleta = mensagemField.getText() + "\n\n" +
                "---\n" +
                "📄 *Arquivo Gerado*\n" +
                "*Nome:* " + arquivoRelatorio.getName() + "\n" +
                "*Local:* " + arquivoRelatorio.getAbsolutePath() + "\n" +
                "*Tamanho:* " + (arquivoRelatorio.length() / 1024) + " KB\n" +
                "---";
            
            // Enviar via WhatsApp
            boolean enviado = com.artereal.swing.service.WhatsAppService.enviarMensagemWhatsApp(telefone, mensagemCompleta);
            
            if (enviado) {
                JOptionPane.showMessageDialog(this, 
                    "WhatsApp Web aberto com sucesso!\n" +
                    "Telefone: " + telefone + "\n" +
                    "Relatório: " + tipoRelatorio + "\n" +
                    "Arquivo: " + arquivoRelatorio.getName() + "\n" +
                    "Status: Anexe o relatório manualmente", 
                    "WhatsApp Aberto", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Falha ao abrir WhatsApp Web!\n" +
                    "Verifique:\n" +
                    "1. Conexão com a internet\n" +
                    "2. Navegador padrão configurado\n" +
                    "3. Número de telefone válido", 
                    "Erro de Envio", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Método removido - substituído por PadraoLayout.criarBotao() direto
    
    public void refreshData() {
        revalidate();
        repaint();
    }
}
