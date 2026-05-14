package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.relatorios.RelatoriosFormPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Painel de Relatórios
 */
public class RelatoriosPanel extends JPanel {
    
    private RelatoriosFormPanel relatoriosFormPanel;
    
    public RelatoriosPanel() {
        initializeComponents();
        setupLayout();
        setupEvents();
    }
    
    private void initializeComponents() {
        // Inicializar formulário otimizado
        relatoriosFormPanel = new RelatoriosFormPanel();
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
        
        // Painel de formulário otimizado usando RelatoriosFormPanel
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Parâmetros do Relatório");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Usar o formulário otimizado
        formContainer.add(relatoriosFormPanel, BorderLayout.CENTER);
        
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
        relatorioComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");
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
        
        // Adicionar ActionListener para o botão Gerar Relatório
        // Como o botão é criado com PadraoLayout.criarBotao(), precisamos encontrar o botão no painel
        for (Component comp : this.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel mainPanel = (JPanel) comp;
                
                for (Component mainComp : mainPanel.getComponents()) {
                    if (mainComp instanceof JPanel) {
                        JPanel contentPanel = (JPanel) mainComp;
                        
                        for (Component contentComp : contentPanel.getComponents()) {
                            if (contentComp instanceof JSplitPane) {
                                JSplitPane splitPane = (JSplitPane) contentComp;
                                
                                Component topComponent = splitPane.getTopComponent();
                                if (topComponent instanceof JPanel) {
                                    JPanel formPanel = (JPanel) topComponent;
                                    
                                    for (Component formComp : formPanel.getComponents()) {
                                        if (formComp instanceof JPanel) {
                                            JPanel formContainer = (JPanel) formComp;
                                            
                                            for (Component containerComp : formContainer.getComponents()) {
                                                if (containerComp instanceof JPanel) {
                                                    JPanel botoesPanel = (JPanel) containerComp;
                                                    
                                                    for (Component buttonComp : botoesPanel.getComponents()) {
                                                        if (buttonComp instanceof JButton) {
                                                            JButton button = (JButton) buttonComp;
                                                            String buttonText = button.getText();
                                                            
                                                            if (buttonText.contains("Gerar Relatório")) {
                                                                button.addActionListener(e -> {
                                                                    gerarRelatorio();
                                                                });
                                                                
                                                            } else if (buttonText.contains("Enviar por E-mail")) {
                                                                button.addActionListener(e -> {
                                                                    enviarEmailRelatorio();
                                                                });
                                                                
                                                            } else if (buttonText.contains("WhatsApp")) {
                                                                button.addActionListener(e -> {
                                                                    enviarRelatorioPorWhatsApp();
                                                                });
                                                                
                                                            } else if (buttonText.contains("Agendar")) {
                                                                
                                                            } else {
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
        
    }
    
    private void gerarRelatorio() {
        
        try {
            // Obter valores dos campos do formulário otimizado
            String tipoRelatorio = (String) relatoriosFormPanel.getTipoComboBox().getSelectedItem();
            String formato = (String) relatoriosFormPanel.getFormatoComboBox().getSelectedItem();
            String dataInicio = relatoriosFormPanel.getDataInicioField().getText().trim();
            String dataFim = relatoriosFormPanel.getDataFimField().getText().trim();
            
            
            // Simular processamento do relatório
            Thread.sleep(500); // Simula processamento
            
            // Exibir mensagem de sucesso
            JOptionPane.showMessageDialog(this, 
                "Relatório '" + tipoRelatorio + "' gerado com sucesso!\n" +
                "Formato: " + formato + "\n" +
                "Data Início: " + dataInicio + "\n" +
                "Data Fim: " + dataFim + "\n" +
                "Status: Concluído", 
                "Relatório Gerado", 
                JOptionPane.INFORMATION_MESSAGE);
            
            
            // Adicionar na tabela de histórico (simplificado)
            // Em uma implementação real, isso viria do banco de dados
            
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao gerar relatório: Processamento interrompido", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao gerar relatório: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarEmailRelatorio() {
        
        try {
            // Obter valores dos campos do formulário otimizado
            String tipoRelatorio = (String) relatoriosFormPanel.getTipoComboBox().getSelectedItem();
            String formato = (String) relatoriosFormPanel.getFormatoComboBox().getSelectedItem();
            
            
            // Simular diálogo de envio de email
            JTextField emailField = new JTextField("admin@artereal.com", 30);
            JTextField assuntoField = new JTextField("Relatório " + tipoRelatorio + " - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 30);
            JTextArea mensagemField = new JTextArea("Prezado(a),\\n\\nSegue em anexo o relatório solicitado.\\n\\nAtenciosamente,\\nSistema ArteReal", 5, 30);
            
            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            panel.add(new JLabel("Para:"));
            panel.add(emailField);
            panel.add(new JLabel("Assunto:"));
            panel.add(assuntoField);
            panel.add(new JLabel("Mensagem:"));
            panel.add(new JScrollPane(mensagemField));
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Enviar Relatório por E-mail", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String email = emailField.getText().trim();
                String assunto = assuntoField.getText().trim();
                String mensagem = mensagemField.getText().trim();
                
                
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "O campo 'Para' é obrigatório!", 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Enviar e-mail real usando JavaMail com anexo
                
                // Verificar configurações de e-mail
                com.artereal.swing.service.EmailService emailService = new com.artereal.swing.service.EmailService();
                if (!emailService.validarConfiguracoes()) {
                    JOptionPane.showMessageDialog(this, 
                        "Configurações de e-mail inválidas!\n" +
                        "Verifique as configurações SMTP no EmailService.\n" +
                        "Usuário: " + emailService.getEmailRemetente(), 
                        "Erro de Configuração", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Gerar arquivo de relatório para anexar
                java.io.File arquivoRelatorio = com.artereal.swing.service.RelatorioService.gerarArquivoRelatorio(tipoRelatorio, formato);
                
                if (arquivoRelatorio == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao gerar arquivo de relatório para anexo!", 
                        "Erro de Geração", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                
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
                    
            } else {
            }
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao enviar e-mail: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarRelatorioPorWhatsApp() {
        
        // Obter valores dos campos (simplificado por enquanto)
        String tipoRelatorio = "Todos"; // Seria obtido do ComboBox
        String formato = "PDF"; // Seria obtido do ComboBox
        
        
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
            
            
            // Gerar arquivo de relatório para anexar
            java.io.File arquivoRelatorio = com.artereal.swing.service.RelatorioService.gerarArquivoRelatorio(tipoRelatorio, formato);
            
            if (arquivoRelatorio == null) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao gerar arquivo de relatório para anexo!", 
                    "Erro de Geração", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
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
