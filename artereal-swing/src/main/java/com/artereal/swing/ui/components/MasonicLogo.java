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
    
    public MasonicLogo() {
        this(DEFAULT_SIZE);
    }
    
    public MasonicLogo(int size) {
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
        
        // Centro do componente
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        // Salvar o estado original
        AffineTransform originalTransform = g2.getTransform();
        
        // Mover para o centro
        g2.translate(centerX, centerY);
        
        // Desenhar o símbolo maçônico simplificado
        drawMasonicSymbol(g2);
        
        // Restaurar o estado original
        g2.setTransform(originalTransform);
        
        g2.dispose();
    }
    
    private void drawMasonicSymbol(Graphics2D g2) {
        // Configurar stroke para linhas mais finas e elegantes
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(primaryColor);
        
        // 1. Desenhar o círculo externo
        int radius = size / 3;
        g2.drawOval(-radius, -radius, radius * 2, radius * 2);
        
        // 2. Desenhar o esquadro (triângulo retângulo)
        int squareSize = size / 4;
        g2.setColor(secondaryColor);
        int[] xPoints = {-squareSize/2, squareSize/2, -squareSize/2};
        int[] yPoints = {squareSize/2, squareSize/2, -squareSize/2};
        g2.drawPolygon(xPoints, yPoints, 3);
        
        // 3. Desenhar a Letra 'G' no centro
        g2.setFont(new Font("Serif", Font.BOLD, 16));
        g2.setColor(secondaryColor);
        FontMetrics fm = g2.getFontMetrics();
        String text = "G";
        int textX = -fm.stringWidth(text) / 2;
        int textY = fm.getAscent() / 3;
        g2.drawString(text, textX, textY);
    }
    
    // Getters e Setters
    public int getLogoSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
        setPreferredSize(new Dimension(size, size));
        repaint();
    }
    
    public Color getPrimaryColor() {
        return primaryColor;
    }
    
    public void setPrimaryColor(Color color) {
        this.primaryColor = color;
        repaint();
    }
    
    public Color getSecondaryColor() {
        return secondaryColor;
    }
    
    public void setSecondaryColor(Color color) {
        this.secondaryColor = color;
        repaint();
    }
    
    /**
     * Cria um JLabel contendo o logo maçônico
     * @param size Tamanho do logo
     * @return JLabel com o logo
     */
    public static JLabel createLogoLabel(int size) {
        JLabel label = new JLabel();
        MasonicLogo logo = new MasonicLogo(size);
        label.setIcon(new ImageIcon(logo.createImage(size, size)));
        return label;
    }
    
    /**
     * Cria uma imagem do logo para uso em componentes
     * @param width Largura da imagem
     * @param height Altura da imagem
     * @return BufferedImage do logo
     */
    public BufferedImage createImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Configurar qualidade
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Salvar tamanho original
        int originalSize = this.size;
        this.size = Math.min(width, height);
        
        // Desenhar o logo
        paintComponent(g2d);
        
        // Restaurar tamanho original
        this.size = originalSize;
        
        g2d.dispose();
        return image;
    }
}
