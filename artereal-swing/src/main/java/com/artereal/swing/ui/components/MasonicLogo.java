package com.artereal.swing.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Componente visual para exibir o logo maçônico da ArteReal
 * Logo discreto e elegante com símbolos maçônicos tradicionais
 */
public class MasonicLogo extends JComponent {
    
    private static final int DEFAULT_SIZE = 40;
    private int size;
    private Color primaryColor;
    private Color secondaryColor;
    private boolean showText;
    
    public MasonicLogo() {
        this(DEFAULT_SIZE, true);
    }
    
    public MasonicLogo(int size) {
        this(size, true);
    }
    
    public MasonicLogo(int size, boolean showText) {
        this.size = size;
        this.showText = showText;
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
        
        // Calcular centro
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        // Calcular escala baseada no tamanho
        double scale = Math.min(getWidth(), getHeight()) / (double) DEFAULT_SIZE;
        
        g2.translate(centerX, centerY);
        g2.scale(scale, scale);
        
        // Desenhar logo maçônico usando o novo design
        drawMasonicSymbol(g2);
        
        g2.dispose();
    }
    
    private void drawMasonicSymbol(Graphics2D g2) {
        // Configurar stroke para linhas mais finas e elegantes
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(primaryColor);
        
        int centerX = 0; // Centro já está na origem após translate
        int centerY = 0;
        
        // 1. Desenhar o Compasso (A forma de 'V' invertido no topo)
        Path2D compasso = new Path2D.Double();
        compasso.moveTo(centerX - 12, centerY + 9);  // Escala reduzida para o tamanho do logo
        compasso.lineTo(centerX, centerY - 12);       // Topo
        compasso.lineTo(centerX + 12, centerY + 9);
        g2.draw(compasso);
        
        // 2. Desenhar o Esquadro (A forma de 'V' na base)
        Path2D esquadro = new Path2D.Double();
        esquadro.moveTo(centerX - 15, centerY - 3);   // Escala reduzida
        esquadro.lineTo(centerX, centerY + 12);      // Ponta de baixo
        esquadro.lineTo(centerX + 15, centerY - 3);
        g2.draw(esquadro);
        
        // 3. Desenhar a Letra 'G' no centro
        g2.setFont(new Font("Serif", Font.BOLD, 16));
        g2.setColor(secondaryColor);
        FontMetrics fm = g2.getFontMetrics();
        String text = "G";
        int textX = centerX - (fm.stringWidth(text) / 2);
        int textY = centerY + (fm.getAscent() / 2) - 1;
        g2.drawString(text, textX, textY);
    }
    
    private void drawSquareAndCompasses(Graphics2D g2d) {
        int offset = size / 8;
        
        // Desenhar Compassos (superior)
        g2d.setColor(primaryColor);
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Braço esquerdo do compasso
        int compassWidth = size / 3;
        int compassHeight = size / 2;
        
        // Linha esquerda do compasso
        Line2D leftCompass = new Line2D.Double(
            -compassWidth/2, -compassHeight/2,
            -offset, offset
        );
        g2d.draw(leftCompass);
        
        // Linha direita do compasso
        Line2D rightCompass = new Line2D.Double(
            compassWidth/2, -compassHeight/2,
            offset, offset
        );
        g2d.draw(rightCompass);
        
        // Círculo central do compasso
        Ellipse2D compassCenter = new Ellipse2D.Double(
            -4, -4, 8, 8
        );
        g2d.fill(compassCenter);
        
        // Desenhar Esquadro (inferior)
        g2d.setColor(secondaryColor);
        
        // Linha horizontal do esquadro
        Line2D squareHorizontal = new Line2D.Double(
            -size/3, offset,
            size/3, offset
        );
        g2d.draw(squareHorizontal);
        
        // Linha vertical do esquadro
        Line2D squareVertical = new Line2D.Double(
            -size/3, offset,
            -size/3, size/3
        );
        g2d.draw(squareVertical);
    }
    
    private void drawLetterG(Graphics2D g2d) {
        g2d.setColor(primaryColor);
        g2d.setFont(new Font("Serif", Font.BOLD, size / 4));
        
        String letterG = "G";
        FontMetrics fm = g2d.getFontMetrics();
        int x = -fm.stringWidth(letterG) / 2;
        int y = fm.getAscent() / 3;
        
        g2d.drawString(letterG, x, y);
    }
    
    private void drawArteRealText(Graphics2D g2d) {
        g2d.setColor(secondaryColor);
        g2d.setFont(new Font("SansSerif", Font.BOLD, size / 8));
        
        String text = "AR";
        FontMetrics fm = g2d.getFontMetrics();
        int x = -fm.stringWidth(text) / 2;
        int y = size / 3;
        
        g2d.drawString(text, x, y);
    }
    
    /**
     * Cria uma imagem do logo para uso em outros componentes
     */
    public static BufferedImage createLogoImage(int size, Color backgroundColor) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Preencher fundo se especificado
        if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, size, size);
        }
        
        // Desenhar logo
        MasonicLogo logo = new MasonicLogo(size, false);
        logo.setSize(size, size);
        logo.paint(g2d);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * Cria um JLabel com o logo
     */
    public static JLabel createLogoLabel(int size) {
        BufferedImage logoImage = createLogoImage(size, null);
        ImageIcon icon = new ImageIcon(logoImage);
        JLabel label = new JLabel(icon);
        label.setToolTipText("ArteReal - Sistema Maçônico");
        return label;
    }
    
    /**
     * Cria um JLabel com o logo e texto
     */
    public static JLabel createLogoWithText(String text, int logoSize) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        
        JLabel logoLabel = createLogoLabel(logoSize);
        panel.add(logoLabel);
        
        if (text != null && !text.trim().isEmpty()) {
            JLabel textLabel = new JLabel(text);
            textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            textLabel.setForeground(new Color(70, 130, 180));
            panel.add(textLabel);
        }
        
        return new JLabel(new ImageIcon(createPanelImage(panel)));
    }
    
    private static BufferedImage createPanelImage(JPanel panel) {
        panel.setSize(panel.getPreferredSize());
        BufferedImage image = new BufferedImage(
            panel.getWidth(), 
            panel.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = image.createGraphics();
        panel.paint(g2d);
        g2d.dispose();
        return image;
    }
    
    // Getters e Setters
    public void setSize(int size) {
        this.size = size;
        setPreferredSize(new Dimension(size, size));
        revalidate();
        repaint();
    }
    
    public void setPrimaryColor(Color color) {
        this.primaryColor = color;
        repaint();
    }
    
    public void setSecondaryColor(Color color) {
        this.secondaryColor = color;
        repaint();
    }
    
    public void setShowText(boolean show) {
        this.showText = show;
        repaint();
    }
}
