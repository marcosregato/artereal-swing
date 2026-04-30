package com.artereal.swing.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Componente visual para exibir o logo maçônico da ArteReal
 * Baseado no símbolo tradicional com esquadro, compassos e letra "G"
 */
public class LogoMaconaria extends JPanel {

    private static final int DEFAULT_SIZE = 40;
    private int size;
    private Color primaryColor;
    private Color secondaryColor;

    public LogoMaconaria() {
        this(DEFAULT_SIZE);
    }

    public LogoMaconaria(int size) {
        this.size = size;
        this.primaryColor = new Color(70, 130, 180); // Azul maçônico
        this.secondaryColor = new Color(192, 192, 192); // Prata
        setPreferredSize(new Dimension(size, size));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Melhorar a qualidade do desenho
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Calcular centro e escala
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double scale = Math.min(getWidth(), getHeight()) / (double) DEFAULT_SIZE;
        
        // Aplicar transformações
        g2.translate(centerX, centerY);
        g2.scale(scale, scale);
        
        // Configurar stroke para linhas elegantes
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(primaryColor);
        
        int baseX = 0; // Centro já está na origem após translate
        int baseY = 0;
        
        // 1. Desenhar o Compasso (A forma de 'V' invertido no topo)
        Path2D compasso = new Path2D.Double();
        compasso.moveTo(baseX - 12, baseY + 9);  // Escala reduzida para o tamanho do logo
        compasso.lineTo(baseX, baseY - 12);       // Topo
        compasso.lineTo(baseX + 12, baseY + 9);
        g2.draw(compasso);
        
        // 2. Desenhar o Esquadro (A forma de 'V' na base)
        Path2D esquadro = new Path2D.Double();
        esquadro.moveTo(baseX - 15, baseY - 3);   // Escala reduzida
        esquadro.lineTo(baseX, baseY + 12);      // Ponta de baixo
        esquadro.lineTo(baseX + 15, baseY - 3);
        g2.draw(esquadro);
        
        // 3. Desenhar a Letra 'G' no centro
        g2.setFont(new Font("Serif", Font.BOLD, 16));
        g2.setColor(secondaryColor);
        FontMetrics fm = g2.getFontMetrics();
        String text = "G";
        int textX = baseX - (fm.stringWidth(text) / 2);
        int textY = baseY + (fm.getAscent() / 2) - 1;
        g2.drawString(text, textX, textY);
    }

    /**
     * Cria um JLabel com o logo maçônico
     * @param size Tamanho do logo
     * @return JLabel com o logo
     */
    public static JLabel createLogoLabel(int size) {
        JLabel logoLabel = new JLabel();
        logoLabel.setLayout(new BorderLayout());
        
        LogoMaconaria logo = new LogoMaconaria(size);
        logo.setPreferredSize(new Dimension(size, size));
        
        logoLabel.add(logo, BorderLayout.CENTER);
        logoLabel.setSize(size, size);
        logoLabel.setOpaque(false);
        
        return logoLabel;
    }

    /**
     * Cria uma imagem do logo para uso em outros componentes
     * @param size Tamanho da imagem
     * @param backgroundColor Cor de fundo (null para transparente)
     * @return BufferedImage com o logo
     */
    public static java.awt.image.BufferedImage createLogoImage(int size, Color backgroundColor) {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Preencher fundo se especificado
        if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, size, size);
        }
        
        // Configurar qualidade
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Desenhar logo
        LogoMaconaria logo = new LogoMaconaria(size);
        logo.setSize(size, size);
        logo.paint(g2d);
        
        g2d.dispose();
        return image;
    }

    // Getters e Setters
    public int getLogoSize() {
        return size;
    }

    public void setLogoSize(int size) {
        this.size = size;
        setPreferredSize(new Dimension(size, size));
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    // Método main para teste
    public static void main(String[] args) {
        JFrame frame = new JFrame("Logo Maçonaria ArteReal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        // Adicionar diferentes tamanhos para teste
        frame.add(new JLabel("16x16:"));
        frame.add(createLogoLabel(16));
        
        frame.add(new JLabel("32x32:"));
        frame.add(createLogoLabel(32));
        
        frame.add(new JLabel("48x48:"));
        frame.add(createLogoLabel(48));
        
        frame.add(new JLabel("64x64:"));
        frame.add(createLogoLabel(64));
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
