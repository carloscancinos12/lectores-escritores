/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

/**
 *
 * @author Miguel Matul <https://github.com/MigueMat4>
 */
public class frmMain extends javax.swing.JFrame {
    
    // Región crítica
    //String[][] biblioteca = new String[2][30];
    //int siguiente_espacio = 5; // habrán 5 libros por defecto, por lo que irá agregando libros según el próximo espacio disponible
    //int cl = 0; // contador de lectores durante la región crítica
    //int ce = 0; // contador de escritores durante la región crítica
    
    // para uso del programa principal
    Escritor hilo_escritor; // hilo que creara escritores
    Lector hilo_lector; // hilo que creara lectores
    int escritores = 0, lectores = 0; // para la cuenta de lectores-escritores de inicio a fin
    Monitor monitor;
    

    /**
     * Creates new form frmMain
     */
    public frmMain() {
        initComponents();
        monitor = new Monitor();
    }
    
    public class Monitor {
        //Region critica
        String[][] biblioteca = new String[2][30];
        int siguiente_espacio = 5; // habrán 5 libros por defecto, por lo que irá agregando libros según el próximo espacio disponible
        int cl = 0; // contador de lectores durante la región crítica
        int ce = 0; // contador de escritores durante la región crítica

        public Monitor() {
            for (int i=0; i<2; i++) {
                for (int j=0; j<30; j++) {
                    biblioteca[i][j] = "*"; // espacios vacíos
                }
            }
            // llenar biblioteca con 5 libros iniciales
            biblioteca[0][0] = "Harry Potter";
            biblioteca[1][0] = "J. K. Rowling";
            biblioteca[0][1] = "Game of Thrones";
            biblioteca[1][1] = "George R. R. Martin";
            biblioteca[0][2] = "El señor de los anillos";
            biblioteca[1][2] = "J. R. R. Tolkien";
            biblioteca[0][3] = "Las cronicas de Narnia";
            biblioteca[1][3] = "C.S. Lewis";
            biblioteca[0][4] = "Hamlet";
            biblioteca[1][4] = "William Shakespeare";
            // mostrar biblioteca en el JTextArea
            for (int j=0; j<5; j++) {
                String textoActual = txaBiblioteca.getText();
                txaBiblioteca.setText(textoActual + "\n"+ biblioteca[0][j] + " por: " + biblioteca[1][j]);
            }
        }
        
        public void actualizarBiblioteca() {
            txaBiblioteca.setText("");
            for (int j=0; j<siguiente_espacio; j++) {
                String textoActual = txaBiblioteca.getText();
                txaBiblioteca.setText(textoActual + "\n"+ biblioteca[0][j] + " por: " + biblioteca[1][j]);
            }
        }
        
        public synchronized void Leer(String lector){
            if ((cl==1) || (ce>5)){
                try {
                    System.out.println(lector + ": me voy a dormir");
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(lector + ": me desperté");
            }
            ce++;
            int tope = siguiente_espacio - 1;
            int libroLeido = (int) (Math.random() * (tope - 0)) + 0;
            System.out.println(lector + " leyendo " + biblioteca[0][libroLeido] + " por: " + biblioteca[1][libroLeido]);
            try {
                // tarda entre 5 y 7 segundos en escribir un libro
                int tiempo_lectura = (int) (Math.random() * (7 - 5)) + 5;
                tiempo_lectura *= 1000; // para obtener los milisegundos
                Thread.sleep(tiempo_lectura);
            } catch (InterruptedException ex) {
                Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(lector + " terminó de leer el libro");
            System.out.println(lector + " abandonando la biblioteca");
            ce--;
        }
        
        public synchronized void Escribir(String Autor, String libro){
            if ((cl==1) || (ce>5)) {
                try {
                    System.out.println(Autor + ": me voy a dormir");
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(Autor + ": me desperté");
            }
            cl++;
            System.out.println(Autor + " escribiendo su libro");
            try {
                // tarda 5 segundos en escribir un libro
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            biblioteca[0][siguiente_espacio] = libro;
            biblioteca[1][siguiente_espacio] = Autor;
            siguiente_espacio++;
            actualizarBiblioteca();
            System.out.println(Autor + " abandonando la biblioteca");
            cl--;
            notify();
        }
    }
    
    public class Escritor extends Thread {
        
        private int noAutor;
        private String libro = "";
        private String[] librosPorEscribir = {"Libro1", "Libro2", "Libro3", "Libro4", "Libro5",
        "Libro6", "Libro7", "Libro8", "Libro9", "Libro10", "Libro11", "Libro12", "Libro13",
        "Libro14", "Libro15", "Libro16", "Libro17", "Libro18", "Libro19", "Libro20"};
        private LocalDateTime horaInicio, horaFin;

        public Escritor(int noAutor) {
            this.noAutor = noAutor;
        }
        
        @Override
        public void run() {
            int numLibro = (int) (Math.random() * (19 - 0)) + 0;
            libro = librosPorEscribir[numLibro];
            horaInicio = LocalDateTime.now();
            System.out.println("Autor No. " + noAutor + " quiere entrar a la biblioteca");
            //System.out.println("Autor No. " + noAutor + " escribiendo su libro");
            //try {
            //    // tarda 5 segundos en escribir un libro
            //    Thread.sleep(5000);
            //} catch (InterruptedException ex) {
            //    Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
            //}
            //biblioteca[0][siguiente_espacio] = libro;
            //biblioteca[1][siguiente_espacio] = this.getAutor();
            //siguiente_espacio++;
            //actualizarBiblioteca();
            monitor.Escribir(getAutor(), libro);
            horaFin = LocalDateTime.now();
            //System.out.println(getAutor() + " abandonando la biblioteca");
        }
        
        public String getAutor() {
            return "Autor No. " + String.valueOf(noAutor);
        }
    }
    
    public class Lector extends Thread {
        
        private int noLector;
        private String libroLeido = "";
        private LocalDateTime horaInicio, horaFin;

        public Lector(int noLector) {
            this.noLector = noLector;
        }
        
        @Override
        public void run() {
            
            System.out.println("Lector No. " + noLector + " quiere entrar a la biblioteca");
            /*
            // escoge un libro al azar de los disponibles
            int tope = siguiente_espacio - 1;
            int libroLeido = (int) (Math.random() * (tope - 0)) + 0;
            System.out.println(getLector() + " leyendo " + biblioteca[0][libroLeido] + " por: " + biblioteca[1][libroLeido]);
            try {
                // tarda entre 5 y 7 segundos en escribir un libro
                int tiempo_lectura = (int) (Math.random() * (7 - 5)) + 5;
                tiempo_lectura *= 1000; // para obtener los milisegundos
                Thread.sleep(tiempo_lectura);
            } catch (InterruptedException ex) {
                Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Lector No. " + noLector + " terminó de leer el libro");
            System.out.println(getLector() + " abandonando la biblioteca");
            */
            horaInicio = LocalDateTime.now();
            monitor.Leer(getLector());
            horaFin = LocalDateTime.now();
        }
        
        public String getLector() {
            return "Lector No. " + String.valueOf(noLector);
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnAgregarLector = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnAgregarEscritor = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtRegionCritica = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaBiblioteca = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("2a Evaluación Parcial");

        jLabel2.setText("Problema de los lectores-escritores");

        jLabel3.setText("Lectores");

        btnAgregarLector.setText("+ Lector");
        btnAgregarLector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarLectorActionPerformed(evt);
            }
        });

        jLabel4.setText("Escritores");

        btnAgregarEscritor.setText("+ Escritor");
        btnAgregarEscritor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEscritorActionPerformed(evt);
            }
        });

        jLabel5.setText("Región Crítica:");

        txaBiblioteca.setColumns(20);
        txaBiblioteca.setRows(5);
        jScrollPane1.setViewportView(txaBiblioteca);

        jLabel6.setText("Biblioteca:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(109, 109, 109)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(190, 190, 190)
                                .addComponent(jLabel2)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAgregarLector)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(jLabel5))
                                    .addComponent(txtRegionCritica, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(112, 112, 112)
                                .addComponent(btnAgregarEscritor))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(31, 31, 31))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAgregarLector)
                        .addComponent(btnAgregarEscritor))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRegionCritica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarEscritorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEscritorActionPerformed
        // TODO add your handling code here:
        for (int i=0; i<3; i++) {
            escritores++; // nuevo escritor
            hilo_escritor = new Escritor(escritores);
            hilo_escritor.start();
        }
    }//GEN-LAST:event_btnAgregarEscritorActionPerformed

    private void btnAgregarLectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarLectorActionPerformed
        // TODO add your handling code here:
        lectores++; // nuevo lector
        hilo_lector = new Lector(lectores);
        hilo_lector.start();
    }//GEN-LAST:event_btnAgregarLectorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarEscritor;
    private javax.swing.JButton btnAgregarLector;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txaBiblioteca;
    private javax.swing.JTextField txtRegionCritica;
    // End of variables declaration//GEN-END:variables
}
