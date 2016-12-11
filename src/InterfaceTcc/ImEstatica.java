package InterfaceTcc;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import static java.lang.System.in;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public class ImEstatica extends JFrame {

    private static final long serialVersionUID = 1L;

    private JButton btnAvancar, btnVoltar, btnAbrir, btnReconhece, btnRealT;
    private JPanel pnlBotoes;
    private VisualizaImgem pnlImagem;
    private int indice = 0;
    private String imagens[];

    private int faceX = 0;
    private int faceY = 0;
    private int eyeX = 0;
    private int eyeY = 0;
    private int noseX = 0;
    private int noseY = 0;
    private int eyeX1 = 0;
    private int eyeY1 = 0;
    private int mouthX = 0;
    private int mouthY = 0;
    private double J1;
    private double J2;
    private double J3;
    private double J4;
    private double J5;
    private double J6;
    private double J7;
    private double J8;
    private boolean norm[];
    String s1;
    String out1;

    public ImEstatica(String imagensUrls[]) {
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 10));
        setSize(550, 550);

        btnAvancar = new JButton(">>");

        btnAbrir = new JButton("Abrir");
        btnVoltar = new JButton("<<");

        btnReconhece = new JButton("Reconhece");

        btnRealT = new JButton("Grava Dados");
        btnRealT.setEnabled(false);

        pnlBotoes = new JPanel(new GridLayout(1, 3, 30, 5));
        pnlBotoes.add(btnVoltar);
        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnAvancar);
        pnlBotoes.add(btnReconhece);
        pnlBotoes.add(btnRealT);

        add(pnlBotoes, BorderLayout.NORTH);

        pnlImagem = new VisualizaImgem();
        add(pnlImagem);

        FileWriter arq = null;
        try {
            arq = new FileWriter("d:\\rostosDetectEstatica.txt");
            // PrintWriter gravarArquivo = new PrintWriter(arq);
        } catch (IOException ex) {
            Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (imagensUrls == null || imagensUrls.length == 0) {
            String imgs[] = escolherImagens();
            if (imgs == null || imgs.length == 0) {
                System.exit(0);
            } else {
                setImagens(imgs);
            }
        } else {
            setImagens(imagensUrls);
        }

        btnAvancar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (indice < imagens.length - 1) {
                    indice++;
                } else {
                    indice = 0;
                }
                setTitle(imagens[indice]);
                pnlImagem.setImg(imagens[indice]);

            }
        });

        btnReconhece.addActionListener(new ActionListener() {
            private Object gravarArq;

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello, OpenCV");
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                CascadeClassifier faceDetector = new CascadeClassifier("C:\\OpenCV\\opencv\\build\\share\\OpenCV\\lbpcascades\\lbpcascade_frontalface.xml");
                CascadeClassifier eyeDetector = new CascadeClassifier("C:\\OpenCV\\opencv\\build\\share\\OpenCV\\haarcascades\\haarcascade_eye.xml");
                CascadeClassifier noseDetector = new CascadeClassifier("C:\\OpenCV\\opencv\\build\\share\\OpenCV\\lbpcascades\\haarcascade_mcs_nose.xml");
                CascadeClassifier mouthDetector = new CascadeClassifier("C:\\OpenCV\\opencv\\build\\share\\OpenCV\\haarcascades\\haarcascade_mcs_mouth.xml");

                Mat image = Highgui.imread(imagens[indice]);
                // Detect faces in the image.
                // MatOfRect is a special container class for Rect.
                MatOfRect faceDetections = new MatOfRect();
                faceDetector.detectMultiScale(image, faceDetections);

                MatOfRect eyeDetections = new MatOfRect();
                eyeDetector.detectMultiScale(image, eyeDetections);

                MatOfRect noseDetections = new MatOfRect();
                noseDetector.detectMultiScale(image, noseDetections);

                MatOfRect mouthDetections = new MatOfRect();
                mouthDetector.detectMultiScale(image, mouthDetections);
//                FileWriter arq = null;

                System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
                // Draw a bounding box around each face.
                for (Rect rect : faceDetections.toArray()) {
                    Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(155, 255, 155));

                    faceX = ((rect.x + rect.width) + rect.x) / 2;
                    faceY = ((rect.y + rect.height) + rect.y) / 2;
                    System.out.printf("meio do rosto =  %d %d ", faceX, faceY);
                    System.out.println();

                    Core.drawMarker(image, new Point(faceX, faceY), new Scalar(255, 255, 255));

                }
                int i = 0;
                for (Rect rect : eyeDetections.toArray()) {
                    Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));

                    int X = ((rect.x + rect.width) + rect.x) / 2;
                    int Y = ((rect.y + rect.height) + rect.y) / 2;
                    System.out.println();

                    Core.drawMarker(image, new Point(X, Y), new Scalar(255, 255, 255));

                    if (i == 0) {
                        eyeX1 = ((rect.x + rect.width) + rect.x) / 2;
                        eyeY1 = ((rect.y + rect.height) + rect.y) / 2;
                        System.out.printf("olho esquerdo i= %d %d %d ", i, eyeX1, eyeY1);
                        System.out.println();
                        i = i + 1;
                    } else {
                        eyeX = ((rect.x + rect.width) + rect.x) / 2;
                        eyeY = ((rect.y + rect.height) + rect.y) / 2;
                        System.out.printf("olho direito i= %d %d %d ", i, eyeX, eyeY);
                        System.out.println();

                    }

                }

                for (Rect rect : noseDetections.toArray()) {
                    Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(155, 255, 155));
                    noseX = ((rect.x + rect.width) + rect.x) / 2;
                    noseY = ((rect.y + rect.height) + rect.y) / 2;
                    System.out.printf("meio do nariz = %d %d ", noseX, noseY);
                    System.out.println();

                    Core.drawMarker(image, new Point(noseX, noseY), new Scalar(255, 255, 255));
                }
                for (Rect rect : mouthDetections.toArray()) {
                    Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(155, 255, 155));
                    mouthX = ((rect.x + rect.width) + rect.x) / 2;
                    mouthY = ((rect.y + rect.height) + rect.y) / 2;
                    System.out.printf("meio da boca =  %d %d ", mouthX, mouthY);
                    System.out.println();

                    Core.drawMarker(image, new Point(mouthX, mouthY), new Scalar(255, 255, 255));
                }

//**************************Dados Normalizados
                J1 = Math.sqrt((Math.pow((eyeY1 - eyeY), 2)) + (Math.pow((eyeX1 - eyeX), 2)));
                J2 = Math.sqrt((Math.pow((eyeY1 - faceY), 2)) + (Math.pow((eyeX1 - faceX), 2)));
                J3 = Math.sqrt((Math.pow((eyeY - faceY), 2)) + (Math.pow((eyeX - faceX), 2)));
                J4 = Math.sqrt((Math.pow((faceY - mouthY), 2)) + (Math.pow((faceX - mouthX), 2)));
                J5 = Math.sqrt((Math.pow((eyeY1 - mouthY), 2)) + (Math.pow((eyeX1 - mouthX), 2)));
                J6 = Math.sqrt((Math.pow((eyeY - mouthY), 2)) + (Math.pow((eyeX - mouthX), 2)));

                if (J1 < J2) {
                    J7 = J1;
                } else {
                    J7 = J2;
                }
                if (J3 < J7) {
                    J7 = J3;
                }
                if (J4 < J7) {
                    J7 = J4;
                }
                if (J5 < J7) {
                    J7 = J5;
                }
                if (J6 < J7) {
                    J7 = J6;
                }

                if (J1 > J2) {
                    J8 = J1;
                } else {
                    J8 = J2;
                }
                if (J3 > J8) {
                    J8 = J3;
                }
                if (J4 > J8) {
                    J8 = J4;
                }
                if (J5 > J8) {
                    J8 = J5;
                }
                if (J6 > J8) {
                    J8 = J6;
                }

                String path = "C:/squid.conf - Cópia.txt";
//Praparo o arquivo original para leitura			
                BufferedReader in1 = null;
                try {
                    in1 = new BufferedReader(new FileReader(path));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
//Crio o arquivo temporario temp.txt
                File temp = null;
                try {
                    temp = File.createTempFile("temp", ".txt", new File(System.getProperty("user.dir")));
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
//Digo que vou escrever no temporario
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new FileWriter(temp));
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    //Varro o arquivo original linha por linha e vou copiando pro temporario
                    while ((s1 = in1.readLine()) != null) {
                        out.write(s1 + "\r\n");
//Condição onde encontro a linha que eu quero fazer as alterações

                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
//Leio o arquivo temporario
                BufferedReader temporario = null;
                try {
                    temporario = new BufferedReader(new FileReader(temp));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
//Preparo para escrever no original
                FileWriter arq = null;
                try {
                    arq = new FileWriter(new File(path));
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    //Aqui nada mais que copio todo o conteudo do temporario pro original
                    while ((out1 = temporario.readLine()) != null) {
                        arq.write(out1 + "\r\n");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
//Excluo o temporario e fecho os demais.
                temp.deleteOnExit();
                try {
                    in1.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //FileWriter arq = null;
            //               try ( //System.out.println(faceX);
            //System.out.println(faceY);
//                        FileWriter arq = new FileWriter("d:\\rostosDetectEstatica.txt")) {
            //BufferedReader arq = new BufferedReader(new FileReader("d:\\rostosDetectEstatica.txt"));
             //gravar.printf("centro da face: %d %d %d %d %d %d %n", faceX, faceY, (faceX - faceY), eyeX, eyeY, (eyeX - eyeY));

            arq."%f %f %f %f %f %f %f %f %n", J1, J2, J3, J4, J5, J6, J7, J8);
                    J1  = (J1 - J7) / (J8 - J7);
            J2  = (J2 - J7) / (J8 - J7);
            J3  = (J3 - J7) / (J8 - J7);
            J4  = (J4 - J7) / (J8 - J7);
            J5  = (J5 - J7) / (J8 - J7);
            J6  = (J6 - J7) / (J8 - J7);

            gravarArq.printf (

            "%f %f %f %f %f %f %n", J1, J2, J3, J4, J5, J6);
                    
//                    norm[indice] = true;
//                    btnReconhece.setEnabled(false);
            btnRealT.setEnabled (

            true);

            btnRealT.addActionListener (
                     new ActionListener() {

                        

            public void actionPerformed(ActionEvent e
            ) {
                try {
                    arq.close();
                    btnRealT.setEnabled(false);
                } catch (IOException ex) {
                    Logger.getLogger(ImEstatica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        );

//                    BufferedWriter buffWrite = new BufferedWriter(new FileWriter(arq));
//                    String linha = "";
//                    linha = in.nextLine();
//                    buffWrite.append(linha + "\n");
//                    buffWrite.close();
        // Save the visualized detection.
        String filename = "faceDetection.png";

        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, image);

        pnlImagem.setImg(
                "faceDetection.png");

    }
}
);

        btnVoltar.addActionListener(
                new ActionListener() {

            @Override
        public void actionPerformed(ActionEvent e
            ) {
                if (indice > 0) {
                    indice--;
                } else {
                    indice = imagens.length - 1;
                }
                setTitle(imagens[indice]);
                pnlImagem.setImg(imagens[indice]);

            }
        }
        );
        btnAbrir.addActionListener(new ActionListener() {

            @Override
        public void actionPerformed(ActionEvent e) {
                setImagens(escolherImagens());
            }
        });

    }

    ImEstatica() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String[] escolherImagens() throws NullPointerException {
        String imagens[];
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);

        fc.showOpenDialog(this);

        File selFile[] = fc.getSelectedFiles();
        imagens = new String[selFile.length];

        for (int i = 0; i < selFile.length; i++) {
            imagens[i] = selFile[i].getAbsolutePath();
        }
        return imagens;
    }

    public void setImagens(String imagens[]) {
        indice = 0;
        pnlImagem.setImg(imagens[0]);
        this.imagens = imagens;

    

}

    public class VisualizaImgem extends JPanel {

    private static final long serialVersionUID = 1L;
    private Image img;

    public VisualizaImgem() {
    }

    public VisualizaImgem(Image img) {
        setImg(img);
    }

    public VisualizaImgem(String url) {
        setImg(img);
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void setImg(Image img) {
        this.img = img;
        this.repaint();
    }

    public void setImg(String url) {
        setImg(Toolkit.getDefaultToolkit().createImage(url));
    }
}

public static void main(String[] args) {
        new ImEstatica(args).setVisible(true);

    }

}
