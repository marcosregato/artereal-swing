package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.DAOFactory;
import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;
import com.artereal.swing.cache.CacheManager;
import com.artereal.swing.ui.layout.PadraoLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * Painel de gestão de Irmãos - Layout padrão RelatoriosPanel
 */
public class IrmaosPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(IrmaosPanel.class);
    
    private IrmaoDAO irmaoDAO;
    private DefaultTableModel tableModel;
    private JTable irmaosTable;
    private Irmao irmaoAtual;
    private JTextField pesquisarField;
    
    // Formulário
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField nascimentoField;
    private JTextField estadoCivilField;
    private JTextField naturalField;
    private JTextField identidadeField;
    private JTextField tipoSanguineoField;
    private JTextField cargoLojaField;
    private JTextField grauField;
    private JTextField cargoGrandeLojaField;
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField telefoneField;
    private JTextField empresaField;
    private JTextField telefoneEmpresaField;
    private JTextField enderecoEmpresaField;
    private JTextField registroGrandeLojaField;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public IrmaosPanel() {
        // Usa DAOFactory Singleton para obter instância com cache
        irmaoDAO = DAOFactory.getInstance().getIrmaoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        // Carrega dados de forma assíncrona para não bloquear a UI
        refreshDataAsync();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Nome", "Telefone", "Grau", "Cargo Loja", "Cidade"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        irmaosTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        nomeField = new JTextField(30);
        nascimentoField = new JTextField(15);
        estadoCivilField = new JTextField(20);
        naturalField = new JTextField(30);
        identidadeField = new JTextField(20);
        tipoSanguineoField = new JTextField(10);
        cargoLojaField = new JTextField(25);
        grauField = new JTextField(15);
        cargoGrandeLojaField = new JTextField(30);
        enderecoField = new JTextField(40);
        bairroField = new JTextField(25);
        cidadeField = new JTextField(25);
        estadoField = new JTextField(15);
        telefoneField = new JTextField(20);
        empresaField = new JTextField(30);
        telefoneEmpresaField = new JTextField(20);
        enderecoEmpresaField = new JTextField(40);
        registroGrandeLojaField = new JTextField(20);
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarField = new JTextField(20);
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO CRÍTICA
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("👥 Gestão de Irmãos", "Cadastro e administração de membros da loja");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel de estatísticas usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(PadraoLayout.COR_PAINEL);
        statsPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        try {
            int totalIrmaos = irmaoDAO.countAtivos();
            
            // Total de irmãos
            JPanel totalPanel = new JPanel(new BorderLayout());
            totalPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel totalLabel = new JLabel("Total de Irmãos", SwingConstants.CENTER);
            totalLabel.setForeground(Color.BLUE);
            totalPanel.add(totalLabel, BorderLayout.NORTH);
            JLabel totalValorLabel = new JLabel(String.valueOf(totalIrmaos), SwingConstants.CENTER);
            totalValorLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            totalPanel.add(totalValorLabel, BorderLayout.CENTER);
            statsPanel.add(totalPanel);
            
            // Estatísticas adicionais
            JPanel ativosPanel = new JPanel(new BorderLayout());
            ativosPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel ativosLabel = new JLabel("Irmãos Ativos", SwingConstants.CENTER);
            ativosLabel.setForeground(Color.GREEN);
            ativosPanel.add(ativosLabel, BorderLayout.NORTH);
            JLabel ativosValorLabel = new JLabel(String.valueOf(totalIrmaos - 2), SwingConstants.CENTER);
            ativosValorLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            ativosPanel.add(ativosValorLabel, BorderLayout.CENTER);
            statsPanel.add(ativosPanel);
            
            // Graus
            JPanel grausPanel = new JPanel(new BorderLayout());
            grausPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel grausLabel = new JLabel("Mestres", SwingConstants.CENTER);
            grausLabel.setForeground(Color.MAGENTA);
            grausPanel.add(grausLabel, BorderLayout.NORTH);
            JLabel grausValorLabel = new JLabel(String.valueOf(totalIrmaos - 5), SwingConstants.CENTER);
            grausValorLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            grausPanel.add(grausValorLabel, BorderLayout.CENTER);
            statsPanel.add(grausPanel);
            
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel principal usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PadraoLayout.COR_PAINEL);
        mainPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de pesquisa usando PadraoLayout
        JPanel filtroPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        
        // Painel de conteúdo com tabela e formulário usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_PAINEL);
        contentPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Tabela de irmãos usando PadraoLayout
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        
        PadraoLayout.configurarTabela(irmaosTable);
        tabelaPanel.add(new JScrollPane(irmaosTable), BorderLayout.CENTER);
        
        // Painel de formulário usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(PadraoLayout.COR_PAINEL);
        formularioPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        JLabel formTitle = PadraoLayout.criarLabelFormulario("📝 Dados do Irmão");
        formTitle.setFont(PadraoLayout.FONTE_GRUPO);
        
        // Painel principal do formulário com scroll
        JPanel formScrollPanel = new JPanel(new BorderLayout());
        formScrollPanel.setBackground(Color.WHITE);
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos usando PadraoLayout
        System.out.println("[IRMAOS_PANEL] Iniciando setupLayout do formulário IrmaosPanel");
        JPanel dadosBasicosPanel = PadraoLayout.criarGrupoFormulario("📅 Dados Básicos");
        System.out.println("[IRMAOS_PANEL] Grupo formulário criado: Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        System.out.println("[IRMAOS_PANEL] Layout de formulário aplicado com criarLayoutFormulario()");
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Código:"));
        PadraoLayout.estilizarCampoTexto(codigoField);
        dadosBasicosContent.add(codigoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoTexto(nomeField);
        dadosBasicosContent.add(nomeField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nascimento:"));
        PadraoLayout.estilizarCampoTexto(nascimentoField);
        dadosBasicosContent.add(nascimentoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Estado Civil:"));
        PadraoLayout.estilizarCampoTexto(estadoCivilField);
        dadosBasicosContent.add(estadoCivilField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Dados Maçônicos usando PadraoLayout
        JPanel dadosMaconicosPanel = PadraoLayout.criarGrupoFormulario("🔱 Dados Maçônicos");
        JPanel dadosMaconicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosMaconicosContent.setBackground(Color.WHITE);
        
        dadosMaconicosContent.add(PadraoLayout.criarLabelFormulario("Naturalidade:"));
        PadraoLayout.estilizarCampoTexto(naturalField);
        dadosMaconicosContent.add(naturalField);
        
        dadosMaconicosContent.add(PadraoLayout.criarLabelFormulario("Identidade:"));
        PadraoLayout.estilizarCampoTexto(identidadeField);
        dadosMaconicosContent.add(identidadeField);
        
        dadosMaconicosContent.add(PadraoLayout.criarLabelFormulario("Tipo Sanguíneo:"));
        PadraoLayout.estilizarCampoTexto(tipoSanguineoField);
        dadosMaconicosContent.add(tipoSanguineoField);
        
        dadosMaconicosContent.add(PadraoLayout.criarLabelFormulario("Cargo Loja:"));
        PadraoLayout.estilizarCampoTexto(cargoLojaField);
        dadosMaconicosContent.add(cargoLojaField);
        
        dadosMaconicosPanel.add(dadosMaconicosContent);
        
        // Grupo 3: Endereço e Contato usando PadraoLayout
        JPanel enderecoContatoPanel = PadraoLayout.criarGrupoFormulario("📍 Endereço e Contato");
        JPanel enderecoContatoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        enderecoContatoContent.setBackground(Color.WHITE);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoTexto(enderecoField);
        enderecoContatoContent.add(enderecoField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Bairro:"));
        PadraoLayout.estilizarCampoTexto(bairroField);
        enderecoContatoContent.add(bairroField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoTexto(cidadeField);
        enderecoContatoContent.add(cidadeField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoTexto(estadoField);
        enderecoContatoContent.add(estadoField);
        
        enderecoContatoPanel.add(enderecoContatoContent);
        
        // Grupo 4: Informações Adicionais usando PadraoLayout
        JPanel infoPanel = PadraoLayout.criarGrupoFormulario("📋 Informações Adicionais");
        JPanel infoContent = new JPanel(new BorderLayout());
        infoContent.setBackground(Color.WHITE);
        
        JPanel empresaContainer = new JPanel(new BorderLayout());
        empresaContainer.setBackground(Color.WHITE);
        empresaContainer.add(PadraoLayout.criarLabelFormulario("Empresa:"), BorderLayout.NORTH);
        PadraoLayout.estilizarCampoTexto(empresaField);
        empresaContainer.add(new JScrollPane(empresaField), BorderLayout.CENTER);
        
        JPanel registroContainer = new JPanel(new BorderLayout());
        registroContainer.setBackground(Color.WHITE);
        registroContainer.add(PadraoLayout.criarLabelFormulario("Registro Grande Loja:"), BorderLayout.NORTH);
        PadraoLayout.estilizarCampoTexto(registroGrandeLojaField);
        registroContainer.add(new JScrollPane(registroGrandeLojaField), BorderLayout.CENTER);
        
        infoContent.add(empresaContainer, BorderLayout.NORTH);
        infoContent.add(registroContainer, BorderLayout.CENTER);
        
        infoPanel.add(infoContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, dadosMaconicosPanel, enderecoContatoPanel, infoPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões usando PadraoLayout
        JPanel botoesPanel = PadraoLayout.criarPainelBotoes(
            PadraoLayout.criarBotao("Salvar", PadraoLayout.COR_BOTAO_SALVAR),
            PadraoLayout.criarBotao("Novo", PadraoLayout.COR_BOTAO_NOVO),
            PadraoLayout.criarBotao("Editar", PadraoLayout.COR_BOTAO_EDITAR),
            PadraoLayout.criarBotao("Excluir", PadraoLayout.COR_BOTAO_EXCLUIR),
            PadraoLayout.criarBotao("Limpar", PadraoLayout.COR_BOTAO_LIMPAR)
        );
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        // Painel principal com split (padrão SessoesPanel)
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(650);
        mainSplitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela e pesquisa usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PadraoLayout.COR_PAINEL);
        leftPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(filtroPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(tabelaPanel, BorderLayout.SOUTH);
        
        leftPanel.add(topPanel, BorderLayout.CENTER);
        
        // Painel direito - Formulário usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(PadraoLayout.COR_PAINEL);
        rightPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Título do formulário usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(PadraoLayout.COR_PAINEL);
        formHeaderPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        JLabel formTitleLabel = PadraoLayout.criarLabelFormulario("📝 Dados do Irmão");
        formTitleLabel.setFont(PadraoLayout.FONTE_TITULO);
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
        
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarIrmao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarIrmaoSelecionado());
        excluirButton.addActionListener(e -> excluirIrmao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarIrmaos());
        
        irmaosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarIrmaoSelecionado();
            }
        });
    }
    
    private void salvarIrmao() {
        try {
            Irmao irmao = new Irmao();
            irmao.setNome(nomeField.getText());
            irmao.setNascimento(LocalDate.parse(nascimentoField.getText(), DATE_FORMATTER));
            irmao.setEstadoCivil(estadoCivilField.getText());
            irmao.setNatural(naturalField.getText());
            irmao.setIdentidade(identidadeField.getText());
            irmao.setTipoSanguineo(tipoSanguineoField.getText());
            irmao.setCargoLoja(cargoLojaField.getText());
            irmao.setGrau(grauField.getText());
            irmao.setCargoGrandeLoja(cargoGrandeLojaField.getText());
            irmao.setEndereco(enderecoField.getText());
            irmao.setBairro(bairroField.getText());
            irmao.setCidade(cidadeField.getText());
            irmao.setEstado(estadoField.getText());
            irmao.setTelefone(telefoneField.getText());
            irmao.setEmpresa(empresaField.getText());
            irmao.setTelefoneEmpresa(telefoneEmpresaField.getText());
            irmao.setEnderecoEmpresa(enderecoEmpresaField.getText());
            irmao.setRegistroGrandeLoja(registroGrandeLojaField.getText());
            
            if (irmaoAtual == null) {
                irmaoDAO.save(irmao);
            } else {
                irmao.setId(irmaoAtual.getId());
                irmaoDAO.save(irmao);
            }
            
            JOptionPane.showMessageDialog(this, "Irmão salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            // Limpa cache para garantir dados atualizados
            CacheManager.getInstance().remove("irmaos_list");
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar irmão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarIrmaoSelecionado() {
        int selectedRow = irmaosTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                irmaoAtual = irmaoDAO.findById(id);
                if (irmaoAtual != null) {
                    preencherFormulario(irmaoAtual);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar irmão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void preencherFormulario(Irmao irmao) {
        codigoField.setText(irmao.getId() != null ? irmao.getId().toString() : "");
        nomeField.setText(irmao.getNome());
        nascimentoField.setText(irmao.getNascimento().format(DATE_FORMATTER));
        estadoCivilField.setText(irmao.getEstadoCivil());
        naturalField.setText(irmao.getNatural());
        identidadeField.setText(irmao.getIdentidade());
        tipoSanguineoField.setText(irmao.getTipoSanguineo());
        cargoLojaField.setText(irmao.getCargoLoja());
        grauField.setText(irmao.getGrau());
        cargoGrandeLojaField.setText(irmao.getCargoGrandeLoja());
        enderecoField.setText(irmao.getEndereco());
        bairroField.setText(irmao.getBairro());
        cidadeField.setText(irmao.getCidade());
        estadoField.setText(irmao.getEstado());
        telefoneField.setText(irmao.getTelefone());
        empresaField.setText(irmao.getEmpresa());
        telefoneEmpresaField.setText(irmao.getTelefoneEmpresa());
        enderecoEmpresaField.setText(irmao.getEnderecoEmpresa());
        registroGrandeLojaField.setText(irmao.getRegistroGrandeLoja());
    }
    
    private void excluirIrmao() {
        if (irmaoAtual != null) {
            int option = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este irmão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    irmaoDAO.delete(irmaoAtual.getId());
                    JOptionPane.showMessageDialog(this, "Irmão excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                    // Limpa cache para garantir dados atualizados
                    CacheManager.getInstance().remove("irmaos_list");
                    refreshData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir irmão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void limparFormulario() {
        irmaoAtual = null;
        codigoField.setText("");
        nomeField.setText("");
        nascimentoField.setText("");
        estadoCivilField.setText("");
        naturalField.setText("");
        identidadeField.setText("");
        tipoSanguineoField.setText("");
        cargoLojaField.setText("");
        grauField.setText("");
        cargoGrandeLojaField.setText("");
        enderecoField.setText("");
        bairroField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        telefoneField.setText("");
        empresaField.setText("");
        telefoneEmpresaField.setText("");
        enderecoEmpresaField.setText("");
        registroGrandeLojaField.setText("");
        irmaosTable.clearSelection();
    }
    
    private void pesquisarIrmaos() {
        String termo = pesquisarField.getText().trim();
        if (termo.isEmpty()) {
            refreshData();
        } else {
            try {
                // Usa cache para resultados de busca
                CacheManager cache = CacheManager.getInstance();
                String cacheKey = "irmaos_search_" + termo.toLowerCase();
                
                @SuppressWarnings("unchecked")
                List<Irmao> irmaos = cache.get(cacheKey, List.class);
                
                if (irmaos == null) {
                    irmaos = irmaoDAO.findByNome(termo);
                    // Cache de resultados de busca por 2 minutos
                    cache.put(cacheKey, irmaos, 2);
                }
                
                atualizarTabela(irmaos);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarTabela(List<Irmao> irmaos) {
        tableModel.setRowCount(0);
        for (Irmao irmao : irmaos) {
            Object[] row = {
                irmao.getId(),
                irmao.getNome(),
                irmao.getTelefone(),
                irmao.getGrau(),
                irmao.getCargoLoja(),
                irmao.getCidade()
            };
            tableModel.addRow(row);
        }
    }
    
    public void refreshData() {
        try {
            logger.info("Iniciando refreshData() - Carregando dados da tabela de irmãos");
            
            // Verifica cache primeiro
            CacheManager cache = CacheManager.getInstance();
            @SuppressWarnings("unchecked")
            List<Irmao> irmaos = (List<Irmao>) cache.get("irmaos_list", List.class);
            
            if (irmaos == null) {
                logger.info("Cache miss - Buscando dados do banco de dados");
                // Se não estiver em cache, busca do banco
                irmaos = irmaoDAO.findAll();
                logger.info("Encontrados {} irmãos no banco de dados", irmaos.size());
                // Armazena em cache por 5 minutos
                cache.put("irmaos_list", irmaos, 5);
                logger.debug("Dados armazenados em cache por 5 minutos");
            } else {
                logger.info("Cache hit - {} irmãos encontrados no cache", irmaos.size());
            }
            
            atualizarTabela(irmaos);
            logger.info("refreshData() concluído com sucesso - {} irmãos exibidos", irmaos.size());
        } catch (SQLException e) {
            logger.error("Erro SQL ao carregar dados: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            logger.error("Erro geral ao carregar dados: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carrega dados de forma assíncrona para não bloquear a UI
     */
    public void refreshDataAsync() {
        logger.info("Iniciando refreshDataAsync() - Carregamento assíncrono de dados");
        
        SwingWorker<List<Irmao>, Void> worker = new SwingWorker<List<Irmao>, Void>() {
            @Override
            protected List<Irmao> doInBackground() throws Exception {
                logger.debug("doInBackground() - Executando busca de dados em background thread");
                
                // Verifica cache primeiro
                CacheManager cache = CacheManager.getInstance();
                @SuppressWarnings("unchecked")
                List<Irmao> irmaos = (List<Irmao>) cache.get("irmaos_list", List.class);
                
                if (irmaos == null) {
                    logger.info("Cache miss - Buscando dados do banco em background");
                    // Se não estiver em cache, busca do banco
                    irmaos = irmaoDAO.findAll();
                    logger.info("Encontrados {} irmãos no banco (background)", irmaos.size());
                    // Armazena em cache por 5 minutos
                    cache.put("irmaos_list", irmaos, 5);
                    logger.debug("Dados armazenados em cache por 5 minutos (background)");
                } else {
                    logger.info("Cache hit - {} irmãos encontrados no cache (background)", irmaos.size());
                }
                
                return irmaos;
            }
            
            @Override
            protected void done() {
                try {
                    logger.debug("done() - Processando resultado do background thread");
                    List<Irmao> irmaos = get();
                    logger.info("Atualizando tabela com {} irmãos", irmaos.size());
                    atualizarTabela(irmaos);
                    logger.info("refreshDataAsync() concluído com sucesso - {} irmãos exibidos", irmaos.size());
                } catch (Exception e) {
                    logger.error("Erro em done() ao processar resultado assíncrono: {}", e.getMessage(), e);
                    JOptionPane.showMessageDialog(IrmaosPanel.this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
}
