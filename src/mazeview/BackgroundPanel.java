package mazeview;

import java.awt.Paint;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

// ** THIS CODE WAS FOUND AT: https://tips4java.wordpress.com/2008/10/12/background-panel/ ** //

/**
 *  Support custom painting on a panel in the following forms.
 *  a) images - that can be scaled, tiled or painted at original size.
 *  b) non solid painting - that can be done by using a Paint object.
 *  Also, any component added directly to this panel will be made
 *  non-opaque so that the custom painting can show through.
 */
public class BackgroundPanel extends JPanel {
  public static final int SCALED = 0;
  public static final int TILED = 1;
  public static final int ACTUAL = 2;

  private Paint painter;
  private Image image;
  private int style = SCALED;
  private float alignmentX = 0.5f;
  private float alignmentY = 0.5f;
  private boolean isTransparentAdd = true;

  /**
   * Set image as the background with the SCALED style.
   * @param image the image to use
   */
  public BackgroundPanel(Image image) {
    this(image, SCALED);
  }

  /**
   * Set image as the background with the specified style.
   * @param image the image to use
   * @param style the style to use
   */
  public BackgroundPanel(Image image, int style) {
    setImage( image );
    setStyle( style );
    setLayout( new BorderLayout() );
  }

  /**
   * Set the image used as the background.
   * @param image the image to set
   */
  public void setImage(Image image) {
    this.image = image;
    repaint();
  }

  /**
   * Set the style used to paint the background image.
   * @param style the style to set
   */
  public void setStyle(int style) {
    this.style = style;
    repaint();
  }

  /**
   * Override method so we can make the component transparent.
   * @param component the component to add
   */
  public void add(JComponent component) {
    add(component, null);
  }

  @Override
  public Dimension getPreferredSize() {
    if (image == null) {
      return super.getPreferredSize();
    }
    else {
      return new Dimension(image.getWidth(null), image.getHeight(null));
    }
  }

  /**
   * Override method so we can make the component transparent.
   * @param component the component to add
   * @param constraints the constraints to obey
   */
  public void add(JComponent component, Object constraints) {
    if (isTransparentAdd) {
      makeComponentTransparent(component);
    }

    super.add(component, constraints);
  }

  /*
   * Try to make the component transparent.
   * For components that use renderers, like JTable, you will also need to
   * change the renderer to be transparent. An easy way to do this it to
   * set the background of the table to a Color using an alpha value of 0.
   */
  private void makeComponentTransparent(JComponent component) {
    component.setOpaque( false );

    if (component instanceof JScrollPane) {
      JScrollPane scrollPane = (JScrollPane)component;
      JViewport viewport = scrollPane.getViewport();
      viewport.setOpaque( false );
      Component c = viewport.getView();

      if (c instanceof JComponent) {
        ((JComponent)c).setOpaque( false );
      }
    }
  }

  /*
   * Add custom painting
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    //  Invoke the painter for the background

    if (painter != null) {
      Dimension d = getSize();
      Graphics2D g2 = (Graphics2D) g;
      g2.setPaint(painter);
      g2.fill( new Rectangle(0, 0, d.width, d.height) );
    }

    //  Draw the image

    if (image == null ) {
      return;
    }

    switch (style) {
      case SCALED :
        drawScaled(g);
        break;

      case TILED  :
        drawTiled(g);
        break;

      case ACTUAL :
        drawActual(g);
        break;

      default:
        drawScaled(g);
    }
  }

  /*
   * Custom painting code for drawing a SCALED image as the background
   */
  private void drawScaled(Graphics g) {
    Dimension d = getSize();
    g.drawImage(image, 0, 0, d.width, d.height, null);
  }

  /*
   * Custom painting code for drawing TILED images as the background
   */
  private void drawTiled(Graphics g) {
    Dimension d = getSize();
    int width = image.getWidth( null );
    int height = image.getHeight( null );

    for (int x = 0; x < d.width; x += width) {
      for (int y = 0; y < d.height; y += height) {
        g.drawImage( image, x, y, null, null );
      }
    }
  }

  /*
   * Custom painting code for drawing the ACTUAL image as the background.
   * The image is positioned in the panel based on the horizontal and
   * vertical alignments specified.
   */
  private void drawActual(Graphics g) {
    Dimension d = getSize();
    Insets insets = getInsets();
    int width = d.width - insets.left - insets.right;
    int height = d.height - insets.top - insets.left;
    float x = (width - image.getWidth(null)) * alignmentX;
    float y = (height - image.getHeight(null)) * alignmentY;
    g.drawImage(image, (int)x + insets.left, (int)y + insets.top, this);
  }
}
