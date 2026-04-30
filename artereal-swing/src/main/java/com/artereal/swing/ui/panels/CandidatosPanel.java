package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CandidatoDAO;
import com.artereal.swing.model.Candidato;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de Gestão de Candidatos e Profanos
 */
public class CandidatosPanel extends JPanel {
    
    private CandidatoDAO candidatoDAO;
    private DefaultTableModel tableModel;
    private JTable candidatosTable;
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField enderecoField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField profissaoField;
    private JTextField foneField;
    private JTextArea informacoesArea;
    private JComboBox<String> statusComboBox;
    private JTextField pesquisarField;
    private JButton pesquisarButton;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton aprovarButton;
    private JButton rejeitarButton;
    private JButton iniciarButton;
    private Candidato candidatoAtual;
    
    public CandidatosPanel() {
        candidatoDAO = new CandidatoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Cidade/Estado", "Profissão", "Status", "Data Cadastro"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        candidatosTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        nomeField = new JTextField(30);
        enderecoField = new JTextField(40);
        cidadeField = new JTextField(20);
        estadoField = new JTextField(10);
        profissaoField = new JTextField(25);
        foneField = new JTextField(15);
        informacoesArea = new JTextArea(4, 30);
        informacoesArea.setLineWrap(true);
        informacoesArea.setWrapStyleWord(true);
        
        statusComboBox = new JComboBox<>(new String[]{"CANDIDATO", "APROVADO", "REJEITADO", "INICIADO"});
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        aprovarButton = PadraoLayout.criarBotao("Aprovar", PadraoLayout.COR_BOTAO_APROVAR);
        rejeitarButton = PadraoLayout.criarBotao("Rejeitar", PadraoLayout.COR_BOTAO_REJEITAR);
        iniciarButton = PadraoLayout.criarBotao("Iniciar", PadraoLayout.COR_BOTAO_INICIAR);
        
        candidatoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO CRÍTICA
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("👥 Gestão de Candidatos e Profanos", "Controle de admissões e iniciações maçônicas");
        add(headerPanel, BorderLayout.NORTH);
        
                
        // Painel esquerdo - Tabela e estatísticas usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PadraoLayout.COR_FUNDO);
        leftPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de pesquisa usando PadraoLayout
        JPanel pesquisaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        
        // Painel de estatísticas melhorado
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBackground(new Color(240, 240, 245));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        try {
            List<Object[]> estatisticas = candidatoDAO.getEstatisticas();
            for (Object[] est : estatisticas) {
                String status = (String) est[0];
                int quantidade = (Integer) est[1];
                
                JPanel statusPanel = new JPanel(new BorderLayout());
                statusPanel.setBorder(BorderFactory.createEtchedBorder());
                statusPanel.add(new JLabel(status, SwingConstants.CENTER), BorderLayout.NORTH);
                statusPanel.add(new JLabel(String.valueOf(quantidade), SwingConstants.CENTER), BorderLayout.CENTER);
                
                // Colorir baseado no status
                Color color = Color.BLACK;
                switch (status) {
                    case "CANDIDATO": color = Color.BLUE; break;
                    case "APROVADO": color = Color.GREEN; break;
                    case "REJEITADO": color = Color.RED; break;
                    case "INICIADO": color = new Color(128, 0, 128); break;
                }
                
                JLabel statusLabel = new JLabel(status, SwingConstants.CENTER);
                statusLabel.setForeground(color);
                statusPanel.add(statusLabel, BorderLayout.NORTH);
                
                statsPanel.add(statusPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de formulário usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        System.out.println("[CANDIDATOS_PANEL] Iniciando setupLayout do formulário CandidatosPanel");
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        System.out.println("[CANDIDATOS_PANEL] Grupo formulário criado: Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        System.out.println("[CANDIDATOS_PANEL] Layout de formulário aplicado com criarLayoutFormulario()");
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Código:"));
        PadraoLayout.estilizarCampoTexto(codigoField);
        dadosBasicosContent.add(codigoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoTexto(nomeField);
        dadosBasicosContent.add(nomeField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Profissão:"));
        PadraoLayout.estilizarCampoTexto(profissaoField);
        dadosBasicosContent.add(profissaoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Status:"));
        PadraoLayout.estilizarComboBox(statusComboBox);
        dadosBasicosContent.add(statusComboBox);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Endereço
        JPanel enderecoPanel = createFormGroup("📍 Endereço");
        JPanel enderecoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoTexto(enderecoField);
        enderecoContent.add(enderecoField);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoTexto(cidadeField);
        enderecoContent.add(cidadeField);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoTexto(estadoField);
        enderecoContent.add(estadoField);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Profissão:"));
        PadraoLayout.estilizarCampoTexto(profissaoField);
        enderecoContent.add(profissaoField);
        
        enderecoPanel.add(enderecoContent);
        
        // Grupo 3: Contato
        JPanel contatoPanel = createFormGroup("📞 Contato");
        JPanel contatoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        contatoContent.setBackground(Color.WHITE);
        
        contatoContent.add(new JLabel("Telefone:"));
        contatoContent.add(foneField);
        
        contatoPanel.add(contatoContent);
        
        // Grupo 4: Conteúdo do Candidato
        JPanel conteudoPanel = createFormGroup("📝 Conteúdo do Candidato");
        JPanel conteudoContent = new JPanel(new BorderLayout());
        conteudoContent.setBackground(Color.WHITE);
        
        // Painel de TextAreas usando PadraoLayout
        JPanel obsPanel = PadraoLayout.criarPainelTextArea("📄 Informações Adicionais:", informacoesArea);
        
        JPanel textAreasPanel = new JPanel(new BorderLayout());
        textAreasPanel.setBackground(Color.WHITE);
        textAreasPanel.add(obsPanel, BorderLayout.CENTER);
        conteudoContent.add(textAreasPanel, BorderLayout.CENTER);
        conteudoPanel.add(conteudoContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, enderecoPanel, contatoPanel, conteudoPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesFormPanel.setBackground(Color.WHITE);
        botoesFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        // Botões de ações
        JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        acoesPanel.setBackground(Color.WHITE);
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações do Processo"));
        acoesPanel.add(aprovarButton);
        acoesPanel.add(rejeitarButton);
        acoesPanel.add(iniciarButton);
        
        // Combinar todos os painéis
        JPanel allPanels = new JPanel(new BorderLayout());
        allPanels.setBackground(Color.WHITE);
        allPanels.add(formContainer, BorderLayout.CENTER);
        allPanels.add(botoesFormPanel, BorderLayout.SOUTH);
        
        JPanel completeForm = new JPanel(new BorderLayout());
        completeForm.setBackground(Color.WHITE);
        completeForm.add(allPanels, BorderLayout.CENTER);
        completeForm.add(acoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(completeForm, BorderLayout.CENTER);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(candidatosTable);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tabelaPanel.add(new JScrollPane(candidatosTable), BorderLayout.CENTER);
        
        // Painel principal com split (padrão SessoesPanel)
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(650);
        mainSplitPane.setResizeWeight(0.6);
        
        // Painel direito - Formulário (padrão SessoesPanel)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Dados do Candidato", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(70, 130, 180));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("📝 Dados do Candidato");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel mainFormContainer = new JPanel(new BorderLayout());
        mainFormContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel basicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel basicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        basicosContent.setBackground(Color.WHITE);
        
        basicosContent.add(PadraoLayout.criarLabelFormulario("Código:"));
        PadraoLayout.estilizarCampoTexto(codigoField);
        basicosContent.add(codigoField);
        
        basicosContent.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoTexto(nomeField);
        basicosContent.add(nomeField);
        
        basicosPanel.add(basicosContent);
        
        // Grupo 2: Endereço
        JPanel enderecoGroupPanel = createFormGroup("📍 Endereço");
        JPanel enderecoGroupContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        enderecoGroupContent.setBackground(Color.WHITE);
        
        enderecoGroupContent.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoTexto(enderecoField);
        enderecoGroupContent.add(enderecoField);
        
        enderecoGroupContent.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoTexto(cidadeField);
        enderecoGroupContent.add(cidadeField);
        
        enderecoGroupContent.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoTexto(estadoField);
        enderecoGroupContent.add(estadoField);
        
        enderecoGroupPanel.add(enderecoGroupContent);
        
// Grupo 3: Contato
JPanel contatoGroupPanel = createFormGroup(" Contato");
JPanel contatoGroupContent = new JPanel(PadraoLayout.criarLayoutFormulario());
contatoGroupContent.setBackground(Color.WHITE);
        
contatoGroupContent.add(PadraoLayout.criarLabelFormulario("Telefone:"));
PadraoLayout.estilizarCampoTexto(foneField);
contatoGroupContent.add(foneField);
        
contatoGroupContent.add(PadraoLayout.criarLabelFormulario("Profissão:"));
PadraoLayout.estilizarCampoTexto(profissaoField);
contatoGroupContent.add(profissaoField);
        
// Grupo 4: Conteúdo do Candidato
JPanel conteudoGroupPanel = createFormGroup(" Conteúdo do Candidato");
JPanel conteudoGroupContent = new JPanel(new BorderLayout());
conteudoGroupContent.setBackground(Color.WHITE);
        
        informacoesArea.setLineWrap(true);
        informacoesArea.setWrapStyleWord(true);
        informacoesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        conteudoGroupContent.add(new JLabel("Informações Adicionais:"), BorderLayout.NORTH);
        conteudoGroupContent.add(new JScrollPane(informacoesArea), BorderLayout.CENTER);
        
        conteudoGroupPanel.add(conteudoGroupContent);
        
        // Organizar grupos verticalmente (padrão SessoesPanel)
        JPanel layoutGroupsPanel = new JPanel(new BorderLayout());
        layoutGroupsPanel.setBackground(Color.WHITE);
        
        JPanel upperGroups = new JPanel(new BorderLayout());
        upperGroups.setBackground(Color.WHITE);
        upperGroups.add(basicosPanel, BorderLayout.NORTH);
        upperGroups.add(enderecoGroupPanel, BorderLayout.CENTER);
        
        JPanel centerGroups = new JPanel(new BorderLayout());
        centerGroups.setBackground(Color.WHITE);
        centerGroups.add(contatoGroupPanel, BorderLayout.NORTH);
        centerGroups.add(conteudoGroupPanel, BorderLayout.CENTER);
        
        JPanel combinedGroups = new JPanel(new BorderLayout());
        combinedGroups.setBackground(Color.WHITE);
        combinedGroups.add(upperGroups, BorderLayout.NORTH);
        combinedGroups.add(centerGroups, BorderLayout.CENTER);
        
        // Adicionar scroll ao formulário para garantir visibilidade
        JScrollPane formScrollPane = new JScrollPane(combinedGroups);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        mainFormContainer.add(formScrollPane, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Aprovar", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Rejeitar", new Color(255, 218, 185)));
        botoesPanel.add(createStyledButton("Iniciar", new Color(221, 160, 221)));
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(mainFormContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
        // Painel esquerdo - Tabela e pesquisa
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(tabelaPanel, BorderLayout.CENTER);
        
        tablePanel.add(topPanel, BorderLayout.CENTER);
        
        mainSplitPane.setLeftComponent(tablePanel);
        mainSplitPane.setRightComponent(rightPanel);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarCandidato());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirCandidato());
        aprovarButton.addActionListener(e -> aprovarCandidato());
        rejeitarButton.addActionListener(e -> rejeitarCandidato());
        iniciarButton.addActionListener(e -> iniciarCandidato());
        
        // Seleção na tabela
        candidatosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarCandidatoSelecionado();
            }
        });
    }
    
    private void salvarCandidato() {
        try {
            Candidato candidato = new Candidato();
            if (candidatoAtual != null) {
                candidato.setId(candidatoAtual.getId());
            }
            
            candidato.setNome(nomeField.getText().trim());
            candidato.setEndereco(enderecoField.getText().trim());
            candidato.setCidade(cidadeField.getText().trim());
            candidato.setEstado(estadoField.getText().trim());
            candidato.setProfissao(profissaoField.getText().trim());
            candidato.setFoneResidencial(foneField.getText().trim());
            candidato.setInformacoes(informacoesArea.getText().trim());
            candidato.setStatus((String) statusComboBox.getSelectedItem());
            
            candidatoDAO.save(candidato);
            
            JOptionPane.showMessageDialog(this, "Candidato salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        candidatoAtual = null;
        codigoField.setText("");
        nomeField.setText("");
        enderecoField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        profissaoField.setText("");
        foneField.setText("");
        informacoesArea.setText("");
        statusComboBox.setSelectedItem("CANDIDATO");
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o candidato " + candidatoAtual.getNome() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.delete(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void aprovarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para aprovar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            candidatoDAO.aprovar(candidatoAtual.getId());
            JOptionPane.showMessageDialog(this, "Candidato aprovado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarCandidatoSelecionado();
            refreshData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao aprovar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejeitarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para rejeitar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente rejeitar o candidato " + candidatoAtual.getNome() + "?", 
            "Confirmar Rejeição", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.rejeitar(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato rejeitado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarCandidatoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao rejeitar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void iniciarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para iniciar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente iniciar o candidato " + candidatoAtual.getNome() + " como irmão?", 
            "Confirmar Iniciação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.iniciar(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato iniciado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarCandidatoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao iniciar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void carregarCandidatoSelecionado() {
        int selectedRow = candidatosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                candidatoAtual = candidatoDAO.findById(id);
                if (candidatoAtual != null) {
                    codigoField.setText(candidatoAtual.getId() != null ? candidatoAtual.getId().toString() : "");
                    nomeField.setText(candidatoAtual.getNome());
                    enderecoField.setText(candidatoAtual.getEndereco());
                    cidadeField.setText(candidatoAtual.getCidade());
                    estadoField.setText(candidatoAtual.getEstado());
                    profissaoField.setText(candidatoAtual.getProfissao());
                    foneField.setText(candidatoAtual.getFoneResidencial());
                    informacoesArea.setText(candidatoAtual.getInformacoes());
                    statusComboBox.setSelectedItem(candidatoAtual.getStatus());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        return PadraoLayout.criarBotao(text, bgColor);
    }
    
    private JPanel createFormGroup(String title) {
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBackground(Color.WHITE);
        groupPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        groupPanel.add(titleLabel, BorderLayout.NORTH);
        return groupPanel;
    }
    
    private void atualizarBotoesAcao() {
        String status = (String) statusComboBox.getSelectedItem();
        
        // Habilitar/desabilitar botões baseado no status
        aprovarButton.setEnabled("CANDIDATO".equals(status));
        rejeitarButton.setEnabled("CANDIDATO".equals(status) || "APROVADO".equals(status));
        iniciarButton.setEnabled("APROVADO".equals(status));
    }
    
    public void refreshData() {
        try {
            List<Candidato> candidatos = candidatoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Candidato candidato : candidatos) {
                Object[] row = {
                    candidato.getId(),
                    candidato.getNome(),
                    candidato.getCidade() + "/" + candidato.getEstado(),
                    candidato.getProfissao(),
                    candidato.getStatus(),
                    candidato.getDataCadastro() != null ? candidato.getDataCadastro().toString() : ""
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar candidatos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
