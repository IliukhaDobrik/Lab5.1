package MainPackege;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Main{
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class MainFrame extends JFrame {
    private static final int WIDTH = 700;

    private static final int HEIGHT = 500;

    private JFileChooser fileChooser = null;

    private JMenuItem resetGraphicsMenuItem;

    private GraphicsDisplay display = new GraphicsDisplay();

    private boolean fileLoaded = false;

    public MainFrame() {
        super("Мышь");
        setSize(700, 500);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(((kit.getScreenSize()).width - 700) / 2, ((kit.getScreenSize()).height - 500) / 2);
        setExtendedState(6);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком") {
        public void actionPerformed(ActionEvent event) {
            if (MainFrame.this.fileChooser == null) {
                MainFrame.this.fileChooser = new JFileChooser();
                MainFrame.this.fileChooser.setCurrentDirectory(new File("."));
            }
            MainFrame.this.fileChooser.showOpenDialog(MainFrame.this);
            MainFrame.this.openGraphics(MainFrame.this.fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        Action resetGraphicsAction = new AbstractAction("Отменить все изменения") {
            public void actionPerformed(ActionEvent event) {
                MainFrame.this.display.reset();
            }
        };
        setJMenuBar(menuBar);
        resetGraphicsMenuItem = fileMenu.add(resetGraphicsAction);
        resetGraphicsMenuItem.setEnabled(false);
        add(display, BorderLayout.CENTER);
    }

    protected void openGraphics(File selectedFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
            ArrayList<Double[]> graphicsData = new ArrayList<>();
            String line;
            String[] strings = null;

            while ((line = reader.readLine()) != null) {
                strings = line.split(" ");
            }

            Double x = 0d;
            Double y = 0d;
            for (int i = 0; i < strings.length; i++) {
                if (i % 2 == 0) {
                    x = Double.parseDouble(strings[i]);
                }
                if (i % 2 != 0) {
                    y = Double.parseDouble(strings[i]);
                    graphicsData.add(new Double[]{x, y});
                }
            }

            display.displayGraphics(graphicsData);

            reader.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Указанный файл не найден",
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка чтения координат точек из файла",
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка чтения координат точек из файла",
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
}