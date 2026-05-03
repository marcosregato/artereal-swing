package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Painel de gestão de Irmãos - Layout padrão Header → Busca → Formulário → Tabela
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
    private JTextField registroGrandeLojaField;
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField telefoneField;
    private JTextField empresaField;
    private JTextField telefoneEmpresaField;
    private JTextField enderecoEmpresaField;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    
    public IrmaosPanel() {
        try {
            logger.info("Inicializando IrmaosPanel");
            initializeComponents();
            setupLayout();
            setupEvents();
            logger.info("IrmaosPanel inicializado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao inicializar IrmaosPanel: " + e.getMessage(), e);
            // Criar componentes básicos mesmo se houver erro
            if (irmaoDAO == null) {
                initializeComponents();
                setupLayout();
                setupEvents();
            }
        }
    }
    
    private void initializeComponents() {
        try {
            // Inicializar DAO de forma segura
            if (irmaoDAO == null) {
                irmaoDAO = new IrmaoDAO();
            }
        } catch (Exception e) {
            logger.error("Erro ao inicializar IrmaoDAO: " + e.getMessage(), e);
        }

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
        
        // Formulário - Campos com tamanhos ideais usando PadraoLayout
        codigoField = new JTextField();
        nomeField = new JTextField();
        nascimentoField = new JTextField();
        estadoCivilField = new JTextField();
        naturalField = new JTextField();
        identidadeField = new JTextField();
        tipoSanguineoField = new JTextField();
        cargoLojaField = new JTextField();
        grauField = new JTextField();
        cargoGrandeLojaField = new JTextField();
        registroGrandeLojaField = new JTextField();
        enderecoField = new JTextField();
        bairroField = new JTextField();
        cidadeField = new JTextField();
        estadoField = new JTextField();
        telefoneField = new JTextField();
        empresaField = new JTextField();
        telefoneEmpresaField = new JTextField();
        
        // Aplicar máscara de telefone aos campos
        aplicarMascaraTelefone(telefoneField);
        aplicarMascaraTelefone(telefoneEmpresaField);
        
        // Botões
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesPrincipais(salvarButton, novoButton, editarButton, excluirButton, limparButton);
        
        pesquisarField = new JTextField(20);
    }
    
    /**
     * Aplica máscara de telefone no formato (XX) XXXX-XXXX
     */
    private void aplicarMascaraTelefone(JTextField telefoneField) {
        try {
            // Remove qualquer formatação existente primeiro
            PlainDocument document = new PlainDocument();
            telefoneField.setDocument(document);
            
            // Define o tamanho máximo e formatação
            document.setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
                    // Remove caracteres não numéricos
                    String cleaned = str.replaceAll("[^0-9]", "");
                    
                    // Limita a 15 caracteres
                    if (document.getLength() + cleaned.length() > 15) {
                        return;
                    }
                    
                    // Aplica formatação: (XX) XXXX-XXXX
                    String currentText = document.getText(0, document.getLength());
                    int digits = currentText.replaceAll("[^0-9]", "").length();
                    
                    String formatted = cleaned;
                    if (digits == 0) {
                        // Primeiro dígito
                        formatted = "(" + cleaned;
                    } else if (digits <= 2) {
                        // DDD
                        formatted = "(" + cleaned + ") ";
                    } else if (digits <= 6) {
                        // DDD + primeiros 4 dígitos
                        formatted = "(" + cleaned.substring(0, 2) + ") " + cleaned.substring(2);
                    } else if (digits <= 10) {
                        // DDD + prefixo + 4 dígitos
                        formatted = "(" + cleaned.substring(0, 2) + ") " + cleaned.substring(2, 6) + "-" + cleaned.substring(6);
                    } else {
                        // DDD + prefixo + 4 dígitos + hífen + dígitos restantes
                        formatted = "(" + cleaned.substring(0, 2) + ") " + cleaned.substring(2, 6) + "-" + cleaned.substring(6, 10);
                    }
                    
                    super.insertString(fb, offset, formatted, attr);
                }
                
                @Override
                public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                    // Permite remoção normal
                    super.remove(fb, offset, length);
                }
            });
            
            // Adiciona foco perdido para formatar quando o campo perder o foco
            telefoneField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    String text = telefoneField.getText();
                    if (text != null && !text.trim().isEmpty()) {
                        String digits = text.replaceAll("[^0-9]", "");
                        if (digits.length() > 0) {
                            String formatted = formatarTelefone(digits);
                            telefoneField.setText(formatted);
                        }
                    }
                }
            });
            
        } catch (Exception e) {
            System.err.println("Erro ao aplicar máscara de telefone: " + e.getMessage());
        }
    }
    
    /**
     * Formata uma string de dígitos no formato (XX) XXXX-XXXX
     */
    private String formatarTelefone(String digits) {
        if (digits == null || digits.trim().isEmpty()) {
            return "";
        }
        
        digits = digits.replaceAll("[^0-9]", "");
        
        if (digits.length() <= 2) {
            // Apenas DDD
            return "(" + digits;
        } else if (digits.length() <= 6) {
            // DDD + 4 dígitos
            return "(" + digits.substring(0, 2) + ") " + digits.substring(2);
        } else if (digits.length() <= 10) {
            // DDD + prefixo + 4 dígitos
            return "(" + digits.substring(0, 2) + ") " + digits.substring(2, 6) + "-" + digits.substring(6);
        } else {
            // DDD + prefixo + 4 dígitos + hífen + restante
            return "(" + digits.substring(0, 2) + ") " + digits.substring(2, 6) + "-" + digits.substring(6);
        }
    }
    
    
    private void setupLayout() {
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("👥 Gestão de Irmãos", "Cadastro e administração de membros da loja");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando BoxLayout vertical para organizar JPanel um de baixo do outro
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Espaço entre painéis
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados do Irmão");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Formulário usando BoxLayout vertical para organizar JPanel um de baixo do outro
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados Pessoais Básicos
        JPanel dadosBasicosPanel = new JPanel(new BorderLayout());
        dadosBasicosPanel.setBackground(Color.WHITE);
        dadosBasicosPanel.setBorder(BorderFactory.createTitledBorder("👤 Dados Pessoais"));
        
        JPanel dadosBasicosContent = new JPanel();
        dadosBasicosContent.setLayout(new BoxLayout(dadosBasicosContent, BoxLayout.Y_AXIS));
        dadosBasicosContent.setBackground(Color.WHITE);
        
        // Primeira linha: Código e Data Nascimento em painel separado para controle total
        JPanel primeiraLinhaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaPanel.setBackground(Color.WHITE);
        
        // Campo Código
        JPanel codigoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        codigoPanel.setBackground(Color.WHITE);
        codigoPanel.add(PadraoLayout.criarLabelFormularioCodigo("Código:"));
        PadraoLayout.estilizarCampoCodigo(codigoField);
        codigoField.setColumns(8); // Reduzido para controle visual
        codigoField.setPreferredSize(new Dimension(60, 25)); // Tamanho fixo pequeno
        codigoField.setMaximumSize(new Dimension(60, 25)); // Limita tamanho máximo
        codigoPanel.add(codigoField);
        
        // Campo Data Nascimento
        JPanel nascimentoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nascimentoPanel.setBackground(Color.WHITE);
        nascimentoPanel.add(PadraoLayout.criarLabelFormulario("Nascimento:"));
        PadraoLayout.estilizarCampoData(nascimentoField);
        nascimentoField.setColumns(12); // Exatamente 12 caracteres
        nascimentoField.setPreferredSize(new Dimension(90, 25)); // Tamanho para 12 caracteres
        nascimentoField.setMaximumSize(new Dimension(90, 25)); // Limita tamanho máximo
        nascimentoPanel.add(nascimentoField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaPanel.add(codigoPanel);
        primeiraLinhaPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaPanel.add(nascimentoPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        dadosBasicosContent.add(primeiraLinhaPanel);
        dadosBasicosContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Nome em painel separado para garantir mesma linha (linha de baixo)
        JPanel nomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nomePanel.setBackground(Color.WHITE);
        nomePanel.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoNome(nomeField);
        nomePanel.add(nomeField);
        
        // Adicionar o painel do Nome ao conteúdo (linha de baixo do Código e Data Nascimento)
        dadosBasicosContent.add(nomePanel);
        
        dadosBasicosPanel.add(dadosBasicosContent, BorderLayout.CENTER);
        formContent.add(dadosBasicosPanel);
        formContent.add(Box.createVerticalStrut(10)); // Espaço entre seções
        
        // SEÇÃO 2: Documentação
        JPanel documentacaoPanel = new JPanel(new BorderLayout());
        documentacaoPanel.setBackground(Color.WHITE);
        documentacaoPanel.setBorder(BorderFactory.createTitledBorder("📄 Documentação"));
        
        JPanel documentacaoContent = new JPanel();
        documentacaoContent.setLayout(new BoxLayout(documentacaoContent, BoxLayout.Y_AXIS));
        documentacaoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Estado Civil e Natural
        JPanel primeiraLinhaDocPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaDocPanel.setBackground(Color.WHITE);
        
        // Campo Estado Civil
        JPanel estadoCivilPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        estadoCivilPanel.setBackground(Color.WHITE);
        estadoCivilPanel.add(PadraoLayout.criarLabelFormulario("Estado Civil:"));
        PadraoLayout.estilizarCampoEstadoCivil(estadoCivilField);
        estadoCivilPanel.add(estadoCivilField);
        
        // Campo Natural
        JPanel naturalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        naturalPanel.setBackground(Color.WHITE);
        naturalPanel.add(PadraoLayout.criarLabelFormulario("Natural:"));
        PadraoLayout.estilizarCampoNaturalidade(naturalField);
        naturalPanel.add(naturalField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaDocPanel.add(estadoCivilPanel);
        primeiraLinhaDocPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaDocPanel.add(naturalPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        documentacaoContent.add(primeiraLinhaDocPanel);
        documentacaoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Identidade
        JPanel identidadePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        identidadePanel.setBackground(Color.WHITE);
        identidadePanel.add(PadraoLayout.criarLabelFormulario("Identidade:"));
        PadraoLayout.estilizarCampoRG(identidadeField);
        identidadePanel.add(identidadeField);
        
        // Adicionar o painel do Identidade ao conteúdo (linha de baixo)
        documentacaoContent.add(identidadePanel);
        
        documentacaoPanel.add(documentacaoContent, BorderLayout.CENTER);
        formContent.add(documentacaoPanel);
        formContent.add(Box.createVerticalStrut(10)); // Espaço entre seções
        
        // SEÇÃO 3: Dados Maçônicos
        JPanel dadosMasonicosPanel = new JPanel(new BorderLayout());
        dadosMasonicosPanel.setBackground(Color.WHITE);
        dadosMasonicosPanel.setBorder(BorderFactory.createTitledBorder("🔷 Dados Maçônicos"));
        
        JPanel dadosMasonicosContent = new JPanel();
        dadosMasonicosContent.setLayout(new BoxLayout(dadosMasonicosContent, BoxLayout.Y_AXIS));
        dadosMasonicosContent.setBackground(Color.WHITE);
        
        // Primeira linha: Cargo Loja e Grau
        JPanel primeiraLinhaMasonicosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaMasonicosPanel.setBackground(Color.WHITE);
        
        // Campo Cargo Loja
        JPanel cargoLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cargoLojaPanel.setBackground(Color.WHITE);
        cargoLojaPanel.add(PadraoLayout.criarLabelFormulario("Cargo Loja:"));
        PadraoLayout.estilizarCampoCargoMaconico(cargoLojaField);
        cargoLojaPanel.add(cargoLojaField);
        
        // Campo Grau
        JPanel grauPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        grauPanel.setBackground(Color.WHITE);
        grauPanel.add(PadraoLayout.criarLabelFormulario("Grau:"));
        PadraoLayout.estilizarCampoGrauMaconico(grauField);
        grauPanel.add(grauField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaMasonicosPanel.add(cargoLojaPanel);
        primeiraLinhaMasonicosPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaMasonicosPanel.add(grauPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        dadosMasonicosContent.add(primeiraLinhaMasonicosPanel);
        dadosMasonicosContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Cargo Grande Loja
        JPanel cargoGrandeLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cargoGrandeLojaPanel.setBackground(Color.WHITE);
        cargoGrandeLojaPanel.add(PadraoLayout.criarLabelFormulario("Cargo Grande Loja:"));
        PadraoLayout.estilizarCampoCargoMaconico(cargoGrandeLojaField);
        cargoGrandeLojaPanel.add(cargoGrandeLojaField);
        
        // Adicionar o painel do Cargo Grande Loja ao conteúdo (linha de baixo)
        dadosMasonicosContent.add(cargoGrandeLojaPanel);
        dadosMasonicosContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Terceira linha: Registro Grande Loja
        JPanel registroGrandeLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registroGrandeLojaPanel.setBackground(Color.WHITE);
        registroGrandeLojaPanel.add(PadraoLayout.criarLabelFormulario("Registro Grande Loja:"));
        PadraoLayout.estilizarCampoRegistroMaconico(registroGrandeLojaField);
        registroGrandeLojaPanel.add(registroGrandeLojaField);
        
        // Adicionar o painel do Registro Grande Loja ao conteúdo (linha de baixo)
        dadosMasonicosContent.add(registroGrandeLojaPanel);
        
        dadosMasonicosPanel.add(dadosMasonicosContent, BorderLayout.CENTER);
        formContent.add(dadosMasonicosPanel);
        formContent.add(Box.createVerticalStrut(10)); // Espaço entre seções
        
        // SEÇÃO 4: Endereço
        JPanel enderecoPanel = new JPanel(new BorderLayout());
        enderecoPanel.setBackground(Color.WHITE);
        enderecoPanel.setBorder(BorderFactory.createTitledBorder("🏠 Endereço"));
        
        JPanel enderecoContent = new JPanel();
        enderecoContent.setLayout(new BoxLayout(enderecoContent, BoxLayout.Y_AXIS));
        enderecoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Endereço (ocupa linha inteira)
        JPanel enderecoPrincipalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        enderecoPrincipalPanel.setBackground(Color.WHITE);
        enderecoPrincipalPanel.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoEndereco(enderecoField);
        enderecoPrincipalPanel.add(enderecoField);
        
        // Adicionar o painel do Endereço ao conteúdo
        enderecoContent.add(enderecoPrincipalPanel);
        enderecoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Bairro e Cidade
        JPanel segundaLinhaEnderecoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaEnderecoPanel.setBackground(Color.WHITE);
        
        // Campo Bairro
        JPanel bairroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bairroPanel.setBackground(Color.WHITE);
        bairroPanel.add(PadraoLayout.criarLabelFormulario("Bairro:"));
        PadraoLayout.estilizarCampoBairro(bairroField);
        bairroPanel.add(bairroField);
        
        // Campo Cidade
        JPanel cidadePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cidadePanel.setBackground(Color.WHITE);
        cidadePanel.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoCidade(cidadeField);
        cidadePanel.add(cidadeField);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaEnderecoPanel.add(bairroPanel);
        segundaLinhaEnderecoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaEnderecoPanel.add(cidadePanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        enderecoContent.add(segundaLinhaEnderecoPanel);
        enderecoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Terceira linha: Estado e Telefone
        JPanel terceiraLinhaEnderecoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        terceiraLinhaEnderecoPanel.setBackground(Color.WHITE);
        
        // Campo Estado
        JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        estadoPanel.setBackground(Color.WHITE);
        estadoPanel.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoEstado(estadoField);
        estadoPanel.add(estadoField);
        
        // Campo Telefone
        JPanel telefonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        telefonePanel.setBackground(Color.WHITE);
        telefonePanel.add(PadraoLayout.criarLabelFormulario("Telefone:"));
        PadraoLayout.estilizarCampoTelefone(telefoneField);
        telefonePanel.add(telefoneField);
        
        // Adicionar os painéis à terceira linha
        terceiraLinhaEnderecoPanel.add(estadoPanel);
        terceiraLinhaEnderecoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        terceiraLinhaEnderecoPanel.add(telefonePanel);
        
        // Adicionar o painel da terceira linha ao conteúdo
        enderecoContent.add(terceiraLinhaEnderecoPanel);
        
        enderecoPanel.add(enderecoContent, BorderLayout.CENTER);
        formContent.add(enderecoPanel);
        formContent.add(Box.createVerticalStrut(10)); // Espaço entre seções
        
        // SEÇÃO 5: Dados Profissionais
        JPanel profissionaisPanel = new JPanel(new BorderLayout());
        profissionaisPanel.setBackground(Color.WHITE);
        profissionaisPanel.setBorder(BorderFactory.createTitledBorder("💼 Dados Profissionais"));
        
        JPanel profissionaisContent = new JPanel();
        profissionaisContent.setLayout(new BoxLayout(profissionaisContent, BoxLayout.Y_AXIS));
        profissionaisContent.setBackground(Color.WHITE);
        
        // Primeira linha: Empresa (ocupa linha inteira)
        JPanel empresaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        empresaPanel.setBackground(Color.WHITE);
        empresaPanel.add(PadraoLayout.criarLabelFormulario("Empresa:"));
        PadraoLayout.estilizarCampoNome(empresaField);
        empresaPanel.add(empresaField);
        
        // Adicionar o painel da Empresa ao conteúdo
        profissionaisContent.add(empresaPanel);
        profissionaisContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Telefone Empresa e Endereço Empresa
        JPanel segundaLinhaProfissionaisPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaProfissionaisPanel.setBackground(Color.WHITE);
        
        // Campo Telefone Empresa
        JPanel telefoneEmpresaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        telefoneEmpresaPanel.setBackground(Color.WHITE);
        telefoneEmpresaPanel.add(PadraoLayout.criarLabelFormulario("Telefone Empresa:"));
        PadraoLayout.estilizarCampoTelefone(telefoneEmpresaField);
        telefoneEmpresaPanel.add(telefoneEmpresaField);
        
        // Campo Endereço Empresa
        JPanel enderecoEmpresaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        enderecoEmpresaPanel.setBackground(Color.WHITE);
        enderecoEmpresaPanel.add(PadraoLayout.criarLabelFormulario("Endereço Empresa:"));
        PadraoLayout.estilizarCampoEndereco(enderecoEmpresaField);
        enderecoEmpresaPanel.add(enderecoEmpresaField);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaProfissionaisPanel.add(telefoneEmpresaPanel);
        segundaLinhaProfissionaisPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaProfissionaisPanel.add(enderecoEmpresaPanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        profissionaisContent.add(segundaLinhaProfissionaisPanel);
        
        profissionaisPanel.add(profissionaisContent, BorderLayout.CENTER);
        formContent.add(profissionaisPanel);
        formContent.add(Box.createVerticalStrut(10)); // Espaço entre seções
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        
        formContainer.add(formContent, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        // Adicionar barra de rolagem no formulário para melhor organização
        JScrollPane formScrollPane = new JScrollPane(formContainer);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScrollPane.setBorder(null);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        formPanel.add(formScrollPane, BorderLayout.CENTER);
        
        // Adicionar painel de formulário ao mainPanel
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Espaço entre painéis
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("👥 Irmãos Cadastrados");
        PadraoLayout.configurarTabela(irmaosTable);
        JScrollPane tableScrollPane = new JScrollPane(irmaosTable);
        
        // Definir altura preferida para a tabela
        irmaosTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        tableScrollPane.setPreferredSize(new Dimension(800, 250));
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Adicionar painel de tabela ao mainPanel
        mainPanel.add(tabelaPanel);
        
        add(mainPanel, BorderLayout.CENTER);
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
            try {
                if (nascimentoField.getText() != null && !nascimentoField.getText().trim().isEmpty()) {
                    irmao.setNascimento(LocalDate.parse(nascimentoField.getText().trim()));
                }
            } catch (Exception e) {
                logger.warn("Erro ao converter data de nascimento: " + nascimentoField.getText());
            }
            irmao.setEstadoCivil(estadoCivilField.getText());
            irmao.setNatural(naturalField.getText());
            irmao.setIdentidade(identidadeField.getText());
            irmao.setTipoSanguineo(tipoSanguineoField.getText());
            irmao.setCargoLoja(cargoLojaField.getText());
            irmao.setGrau(grauField.getText());
            irmao.setCargoGrandeLoja(cargoGrandeLojaField.getText());
            irmao.setRegistroGrandeLoja(registroGrandeLojaField.getText());
            irmao.setEndereco(enderecoField.getText());
            irmao.setBairro(bairroField.getText());
            irmao.setCidade(cidadeField.getText());
            irmao.setEstado(estadoField.getText());
            irmao.setTelefone(telefoneField.getText());
            irmao.setEmpresa(empresaField.getText());
            irmao.setTelefoneEmpresa(telefoneEmpresaField.getText());
            irmao.setEnderecoEmpresa(enderecoEmpresaField.getText());
            
            if (irmaoAtual == null) {
                irmaoDAO.save(irmao);
            } else {
                irmao.setId(irmaoAtual.getId());
                irmaoDAO.save(irmao);
            }
            
            JOptionPane.showMessageDialog(this, "Irmão salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar irmão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirIrmao() {
        if (irmaoAtual != null) {
            int option = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este irmão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    irmaoDAO.delete(irmaoAtual.getId());
                    JOptionPane.showMessageDialog(this, "Irmão excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
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
        registroGrandeLojaField.setText("");
        enderecoField.setText("");
        bairroField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        telefoneField.setText("");
        empresaField.setText("");
        telefoneEmpresaField.setText("");
        enderecoEmpresaField.setText("");
        irmaosTable.clearSelection();
    }
    
    private void carregarIrmaoSelecionado() {
        int selectedRow = irmaosTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = (Integer) tableModel.getValueAt(selectedRow, 0);
                Irmao irmao = irmaoDAO.findById((long) id);
                if (irmao != null) {
                    irmaoAtual = irmao;
                    codigoField.setText(String.valueOf(irmao.getId()));
                    nomeField.setText(irmao.getNome());
                    if (irmao.getNascimento() != null) {
                    nascimentoField.setText(irmao.getNascimento().toString());
                }
                    estadoCivilField.setText(irmao.getEstadoCivil());
                    naturalField.setText(irmao.getNatural());
                    identidadeField.setText(irmao.getIdentidade());
                    tipoSanguineoField.setText(irmao.getTipoSanguineo());
                    cargoLojaField.setText(irmao.getCargoLoja());
                    grauField.setText(irmao.getGrau());
                    cargoGrandeLojaField.setText(irmao.getCargoGrandeLoja());
                    registroGrandeLojaField.setText(irmao.getRegistroGrandeLoja());
                    enderecoField.setText(irmao.getEndereco());
                    bairroField.setText(irmao.getBairro());
                    cidadeField.setText(irmao.getCidade());
                    estadoField.setText(irmao.getEstado());
                    telefoneField.setText(irmao.getTelefone());
                    empresaField.setText(irmao.getEmpresa());
                    telefoneEmpresaField.setText(irmao.getTelefoneEmpresa());
                    enderecoEmpresaField.setText(irmao.getEnderecoEmpresa());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar irmão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarIrmaos() {
        String termo = pesquisarField.getText().trim();
        if (termo.isEmpty()) {
            refreshData();
        } else {
            try {
                // Busca direta no banco sem cache
                List<Irmao> irmaos = irmaoDAO.findByNome(termo);
                logger.info("Busca realizada: {} irmãos encontrados", irmaos.size());
                atualizarTabela(irmaos);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        try {
            logger.info("Iniciando refreshData() - Carregando dados da tabela de irmãos");
            
            // Busca direta do banco sem cache
            List<Irmao> irmaos = irmaoDAO.findAll();
            logger.info("Encontrados {} irmãos no banco de dados", irmaos.size());
            
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
     * Carrega dados de forma síncrona simplificada
     */
    public void refreshDataAsync() {
        logger.info("Iniciando refreshDataAsync() - Carregamento de dados");
        
        try {
            // Busca direta do banco sem cache para simplificar
            List<Irmao> irmaos = irmaoDAO.findAll();
            logger.info("Encontrados {} irmãos no banco", irmaos.size());
            
            // Atualizar tabela na UI thread
            SwingUtilities.invokeLater(() -> {
                atualizarTabela(irmaos);
                logger.info("refreshDataAsync() concluído com sucesso - {} irmãos exibidos", irmaos.size());
            });
            
        } catch (Exception e) {
            logger.error("Erro ao carregar dados: {}", e.getMessage(), e);
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            });
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
}
