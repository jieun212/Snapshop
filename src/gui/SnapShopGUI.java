/*
 * TCSS 305 - Winter 2016 
 * Assignment 4 - Snap Shop
 * 
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;

import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The graphical user interface for the SnapShop program.
 * 
 * @author Jieun Lee
 * @version 1.1 (02-05-2016)
 */
public class SnapShopGUI extends JPanel {

    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = -2144739668936849562L;

    /**
     * A ToolKit.
     */
    private static final Toolkit KIT = Toolkit.getDefaultToolkit();

    /**
     * The Dimension of the screen.
     */
    private static final Dimension SCREEN_SIZE = KIT.getScreenSize();

    /**
     * The frame.
     */
    private final JFrame myFrame;

    /**
     * The list of the filter buttons.
     */
    private final List<JButton> myFilterButtons;

    /**
     * The image label.
     */
    private final JLabel myImageLabel;

    /**
     * The Open... button.
     */
    private final JButton myOpenButton;

    /**
     * The Save As... button.
     */
    private final JButton mySaveButton;

    /**
     * The Close Image button.
     */
    private final JButton myCloseButton;

    /**
     * The file chooser.
     */
    private final JFileChooser myChooser;

    /**
     * The pixel image.
     */
    private PixelImage myImage;


    /**
     * Constructs a new SnapShopGUI.
     */
    public SnapShopGUI() {
        super();
        myFrame = new JFrame("TCSS 305 SnapShop");
        myFilterButtons = new ArrayList<JButton>();
        myImageLabel = new JLabel();
        myOpenButton = new JButton("Open...");
        mySaveButton = new JButton("Save As...");
        myCloseButton = new JButton("Close Image");
        myChooser = new JFileChooser();
    }

    /**
     * Sets up the GUI.
     */
    public void start() {
        // Set default close operation
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Call createPanel() and add the panels to JFrame
        myFrame.add(createPanel());

        // Set current directory to this project folder
        myChooser.setCurrentDirectory(new java.io.File("."));

        // Add a OpenButtonListener (An Inner Class defined below) to myOpenButton
        myOpenButton.addActionListener(new OpenButtonListener());

        // Add a SaveButtonListener (An Inner Class defined below) to mySaveButton
        mySaveButton.addActionListener(new SaveButtonListener());

        // Add addActionlistener() to myCloseButton with an anonymous Inner Class
        myCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                enableButton(false);
                clearImage();
                myFrame.pack();
            }
        });
        
        // Adjust size to fit
        myFrame.pack();
        
        // Set minimum size of the frame
        myFrame.setMinimumSize(myFrame.getPreferredSize());
        
        // Position the frame in the center of the screen
        myFrame.setLocation(SCREEN_SIZE.width / 2 - myFrame.getWidth(),
                            (int) (SCREEN_SIZE.getHeight() / 2 - myFrame.getHeight()));
        
        // Set true for visible of the frame 
        myFrame.setVisible(true);
    }

    /**
     * Creates JPanels for the filters, open, save as, and close buttons, and
     * the image label. Returns the main panel that contains those buttons and
     * label.
     * 
     * @return the main panel that contains regular three button panel, seven
     *         filter buttons panel, and image label panel.
     */
    private JPanel createPanel() {
        
        // Create a main panel that would contain the button panels and image panel
        final JPanel mainPanel = new JPanel(new BorderLayout());

        // Create filter buttons and add these buttons to myFilterButtons ArrayList        
        myFilterButtons.add(createButton(new EdgeDetectFilter()));
        myFilterButtons.add(createButton(new EdgeHighlightFilter()));
        myFilterButtons.add(createButton(new FlipHorizontalFilter()));
        myFilterButtons.add(createButton(new FlipVerticalFilter()));
        myFilterButtons.add(createButton(new GrayscaleFilter()));
        myFilterButtons.add(createButton(new SharpenFilter()));
        myFilterButtons.add(createButton(new SoftenFilter()));

        // Set disable the button
        enableButton(false);

        // Create JPanel for filter button with grid and place north side of the main panel
        final JPanel filterButtonPanel = new JPanel(new GridLayout(7, 1));

        // Add filter buttons to the filter button panel
        for (int i = 0; i < myFilterButtons.size(); i++) {
            filterButtonPanel.add(myFilterButtons.get(i));
        }
        
        // Create bottom button panel for myOpenButton, mySaveButton and myCloseButton
        // with grid layout and place south side of the main panel
        final JPanel bottomButtonPanel = new JPanel(new GridLayout(3, 1));
        
        // Add the myOpenButton, mySaveButton and myCloseButton to the regular button panel
        bottomButtonPanel.add(myOpenButton);
        bottomButtonPanel.add(mySaveButton);
        bottomButtonPanel.add(myCloseButton);
        bottomButtonPanel.setSize(bottomButtonPanel.getPreferredSize());

        // Create button panel that would contain the both filter and regular
        // button panels with border layout
        final JPanel buttonPanel = new JPanel(new BorderLayout());

        // Add two sub button panels to this button panel
        buttonPanel.add(filterButtonPanel, BorderLayout.NORTH);
        buttonPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        // Create label panel with default layout, flow layout
        final JPanel labelPanel = new JPanel();

        // Add myImgLabel to the label panel
        labelPanel.add(myImageLabel);
        
        // Create image panel with border layout
        final JPanel imagePanel = new JPanel(new BorderLayout());
        
        // Add labelPanel to the imagePanel
        imagePanel.add(labelPanel, BorderLayout.WEST);

        // Add buttonPanel on the west side and imagePanel on the center 
        // to the mainPanel
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Creates the filter buttons that takes the Filter interface as a parameter
     * and returns JButton type filter buttons. Adds addActionlistener() to the
     * each filterButton with an anonymous Inner Class and when the event
     * happens, the function of the filter would be applied to the myImage.
     * 
     * @param theFilter the filter.
     * @return returns filter buttons.
     */
    private JButton createButton(final Filter theFilter) {
        final JButton button = new JButton(theFilter.getDescription());
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                theFilter.filter(myImage);
                myImageLabel.setIcon(new ImageIcon(myImage));
            }
        });
        return button;
    }

    /**
     * Enable or disable of the function of the buttons with the given truth
     * value.
     * 
     * @param theValue truth value.
     */
    private void enableButton(final boolean theValue) {
        // default = disable to click
        for (int i = 0; i < myFilterButtons.size(); i++) {
            myFilterButtons.get(i).setEnabled(theValue);
        }
        mySaveButton.setEnabled(theValue);
        myCloseButton.setEnabled(theValue);
    }

    /**
     * Remove the loaded image and adjust the frame.
     */
    private void clearImage() {
        myImageLabel.setIcon(null);
        myFrame.pack();
        myFrame.setMinimumSize(myFrame.getPreferredSize());
    }

    /**
     * An Inner class that is an ActionListener for Open button clicks.
     * 
     * @author Jieun Lee
     * @version 1.0 (02-04-2016)
     */
    class OpenButtonListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            final int result = myChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    myImage = PixelImage.
                                    load(new File(myChooser.getSelectedFile().getPath()));
                } catch (final IOException exception) {
                    JOptionPane.showMessageDialog(null,
                                                   "The selected file did not contain an "
                                                   + "image!", "Error!", 
                                                   JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (myImage != null) {
                    clearImage();
                }
                enableButton(true);
                myImageLabel.setIcon(new ImageIcon(myImage));
                myFrame.pack();
                myFrame.setMinimumSize(myFrame.getPreferredSize());

            }
        }
    }
    
    /**
     * An Inner class that is an ActionListener for Save button clicks.
     * 
     * @author Jieun Lee
     * @version 1.0 (02-05-2016)
     */
    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            final int result = myChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION
                && myChooser.getSelectedFile().exists()) {
                final Object[] option = {"Cancle", "Replace"};
                final int optionResult = JOptionPane.
                                 showOptionDialog(null,
                                                  '"' + myChooser.getSelectedFile().getName()
                                                        + '"' + " arleady exist. Do you want"
                                                        + " to replace it?",
                                                  "Error", JOptionPane.OK_CANCEL_OPTION,
                                                  JOptionPane.ERROR_MESSAGE, null, option,
                                                  option[0]);
                if (optionResult == 0) {
                    myChooser.cancelSelection();
                } else {
                    try {
                        myImage.save(new File(myChooser.getSelectedFile().getPath()));
                    } catch (final IOException exception) {
                        exception.printStackTrace();
                        return;
                    }
                }
            }
        }
    }
}
