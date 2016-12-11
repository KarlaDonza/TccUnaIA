//Karla Caroline Donza - 31427299
package InterfaceTcc;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Taha Emara
 */
public class FaceDetection extends javax.swing.JFrame {

    private BufferedImage image;

    int faceX, faceY, eyeX, eyeY, eyeX1, eyeY1, noseX, noseY, mouthX, mouthY;

    public void paintComponent(Graphics g) {
        paintComponent(g);
        if (this.image == null) {
            return;
        }
        g.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
    }

    private DaemonThread myThread = null;
    int count = 0;
    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();

    CascadeClassifier faceDetector = new CascadeClassifier("lbpcascade_frontalface.xml");
    MatOfRect faceDetections = new MatOfRect();
    CascadeClassifier eyeDetector = new CascadeClassifier("haarcascade_eye.xml");
    MatOfRect eyeDetections = new MatOfRect();

    CascadeClassifier noseDetector = new CascadeClassifier("haarcascade_mcs_nose.xml");
    MatOfRect noseDetections = new MatOfRect();
    CascadeClassifier mouthDetector = new CascadeClassifier("haarcascade_mcs_mouth.xml");
    MatOfRect mouthDetections = new MatOfRect();

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frame);
                            Graphics g = jPanel1.getGraphics();
                            faceDetector.detectMultiScale(frame, faceDetections);
                            eyeDetector.detectMultiScale(frame, eyeDetections);
                            noseDetector.detectMultiScale(frame, noseDetections);
                            mouthDetector.detectMultiScale(frame, mouthDetections);
                            FileWriter arq = new FileWriter("d:\\rostosDetectados.txt");
                            PrintWriter gravarArq = new PrintWriter(arq);
                            for (Rect rect : faceDetections.toArray()) {
                                Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(125, 125, 255));
                                faceX = ((rect.x + rect.width) + rect.x) / 2;
                                faceY = ((rect.y + rect.height) + rect.y) / 2;
                                System.out.printf("meio do nariz = %d %d ", faceX, faceY);
                                System.out.println();
                                Core.drawMarker(frame, new Point(faceX, faceY), new Scalar(255, 255, 255));

                            }
                            int i = 0;
                            for (Rect rect : eyeDetections.toArray()) {

                                Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(255, 255, 125));
                                int X = ((rect.x + rect.width) + rect.x) / 2;
                                int Y = ((rect.y + rect.height) + rect.y) / 2;
                                System.out.println();

                                Core.drawMarker(frame, new Point(X, Y), new Scalar(255, 255, 255));

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
                                Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(255, 155, 225));
                                noseX = ((rect.x + rect.width) + rect.x) / 2;
                                noseY = ((rect.y + rect.height) + rect.y) / 2;
                                System.out.printf("meio do rosto =  %d %d ", noseX, noseY);
                                System.out.println();

                                Core.drawMarker(frame, new Point(noseX, noseY), new Scalar(255, 255, 255));

                            }
                            for (Rect rect : mouthDetections.toArray()) {

                                Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(100, 255, 100));

                                mouthX = ((rect.x + rect.width) + rect.x) / 2;
                                mouthY = ((rect.y + rect.height) + rect.y) / 2;
                                System.out.printf("meio do rosto =  %d %d ", mouthX, mouthY);
                                System.out.println();

                                Core.drawMarker(frame, new Point(mouthX, mouthY), new Scalar(255, 255, 255));

                            }

                            gravarArq.printf(" %d %d %d %d %d %d %n", faceX, faceY, (faceX - faceY), eyeX, eyeY, (eyeX - eyeY));
                            System.out.print("RostoX - olhoX: ");
                            System.out.println(faceX - faceY);

                            System.out.print("RostoY - olhoY: ");
                            System.out.println(faceY);
                            arq.close();

                            Highgui.imencode(".bmp", frame, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
                            if (g.drawImage(buff, 0, 0, getWidth(), getHeight() - 150, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                if (runnable == false) {
                                    System.out.println("Paused ..... ");
                                    this.wait();
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error");
                        }
                    }
                }
            }
        }
    }

/////////
    /**
     * Creates new form FaceDetection
     */
    public FaceDetection() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Pause");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Imagem Estática");
        jButton3.setToolTipText("");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addComponent(jButton1)
                .addGap(53, 53, 53)
                .addComponent(jButton3)
                .addGap(85, 85, 85)
                .addComponent(jButton2)
                .addContainerGap(182, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        myThread.runnable = false;            // stop thread
        jButton2.setEnabled(false);   // activate start button 
        jButton1.setEnabled(true);     // deactivate stop button

        webSource.release();  // stop caturing fron cam


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        webSource = new VideoCapture(0); // video capture from default cam
        myThread = new DaemonThread(); //create object of threat class
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();                 //start thrad
        jButton1.setEnabled(false);  // deactivate start button
        jButton2.setEnabled(true);  //  activate stop button
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ImEstatica foto = new ImEstatica(null);
        foto.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FaceDetection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FaceDetection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FaceDetection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FaceDetection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FaceDetection().setVisible(true);

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
