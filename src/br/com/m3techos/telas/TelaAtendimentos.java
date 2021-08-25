/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.m3techos.telas;

import java.sql.*;
import br.com.m3techos.dal.ModuloConexao;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author m3tech
 */
public class TelaAtendimentos extends javax.swing.JInternalFrame {

    Connection conexao = null;//recebe a conexao
    PreparedStatement pst = null;//usada para executar instrucoes sql
    ResultSet rs = null;//usada para executar instrucoes sql

    private String tipo;

    /**
     * Creates new form TelaAtendimentos
     */
    public TelaAtendimentos() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void pesquisar_cliente() {
        String sql = "select idcliente as ID, nomecliente as Nome, telcliente as Fone from tbclientes where nomecliente like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeCli.getText() + "%");
            rs = pst.executeQuery();
            tblCliente.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_campos() {
        int setar = tblCliente.getSelectedRow();
        txtIdCli.setText(tblCliente.getModel().getValueAt(setar, 0).toString());
        //btnAlterar.setEnabled(true);
        //btnExcluir.setEnabled(true);
        // btnAdicionar.setEnabled(false);
    }

    private void atendimento() {
        String sql = "insert into tbatendimentos (idcliente,descatendimento,dtatendimento,hrinicial,hrfinal,statusatendimento,vlratendimento) values (?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdCli.getText());
            pst.setString(2, txtDesAtendimento.getText());
            pst.setString(3, txtDtAtendimento.getText());
            pst.setString(4, txtHrIni.getText());
            pst.setString(5, txtHrFin.getText());
            pst.setString(6, tipo);
            pst.setString(7, txtValor.getText().replace(",", "."));

            if ((txtIdCli.getText().isEmpty() || txtDesAtendimento.getText().isEmpty() || txtDtAtendimento.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios.");
            } else {
                int retorno = pst.executeUpdate();
                if (retorno > 0) {
                    JOptionPane.showMessageDialog(null, "Atendimento cadastrado com sucesso.");
                    txtNomeCli.setText(null);
                    txtIdCli.setText(null);
                    txtDesAtendimento.setText(null);
                    txtDtAtendimento.setText(null);
                    txtHrIni.setText(null);
                    txtHrFin.setText(null);
                    txtNumHoras.setText(null);
                    txtValor.setText("0");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pesquisar_atendimento() {
        String num_os = JOptionPane.showInputDialog("Numero do atendimento:");
        String sql = "select * from tbatendimentos where idatendimento= " + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdAtendimento.setText(rs.getString(1));
                txtIdCli.setText(rs.getString(2));
                txtDesAtendimento.setText(rs.getString(3));
                String radio = rs.getString(7);
                if (radio.equals("Aberta")) {
                    rdbAberta.setSelected(true);
                    tipo = "Aberta";
                } else {
                    rdbFechada.setSelected(true);
                    tipo = "Fechada";
                }
                txtDtAtendimento.setText(rs.getString(4));
                txtHrIni.setText(rs.getString(5));
                txtHrFin.setText(rs.getString(6));
                txtValor.setText(rs.getString(8));
                String aux = txtIdAtendimento.getText();
                String sql2 = "select * from tbrecebiveisatend where idatendimento=" + aux;

                rs = pst.executeQuery(sql2);
                if (rs.next() || radio.equals("Aberta")) {
                    btnNota.setEnabled(false);
                } else {
                    btnNota.setEnabled(true);
                }
                try {
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                    java.util.Date hrf = df.parse(txtHrFin.getText());
                    java.util.Date hri = df.parse(txtHrIni.getText());

                    long hrt = ((hrf.getTime() - hri.getTime()) / 60000) / 60;
                    txtNumHoras.setText("0" + Long.toString(hrt) + "00");
                    //System.out.println(hri);
                    //System.out.println(hrf);
                    //System.out.println(hrt);

                } catch (Exception e) {

                }

                btnExcluir.setEnabled(true);
                btnAlterar.setEnabled(true);
                btnImprimir.setEnabled(true);
                btnAdicionar.setEnabled(false);
                txtNomeCli.setEnabled(false);
                tblCliente.setVisible(false);
                txtIdCli.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Nº atendimento não encontrado.");
                txtNomeCli.setText(null);
                txtIdCli.setText(null);
                txtIdAtendimento.setText(null);
                txtDesAtendimento.setText(null);
                txtDtAtendimento.setText(null);
                txtHrIni.setText(null);
                txtHrFin.setText(null);
                txtValor.setText("0");
                txtNumHoras.setText(null);
                btnExcluir.setEnabled(false);
                btnAlterar.setEnabled(false);
                btnImprimir.setEnabled(false);
                btnAdicionar.setEnabled(true);
                txtNomeCli.setEnabled(true);
                tblCliente.setVisible(true);
                txtIdCli.setEnabled(true);

            }

        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "Nº atendimento invalido");
            txtNomeCli.setText(null);
            txtIdCli.setText(null);
            txtIdAtendimento.setText(null);
            txtDesAtendimento.setText(null);
            txtDtAtendimento.setText(null);
            txtHrIni.setText(null);
            txtHrFin.setText(null);
            txtValor.setText("0");
            txtNumHoras.setText(null);
            btnExcluir.setEnabled(false);
            btnAlterar.setEnabled(false);
            btnImprimir.setEnabled(false);
            btnAdicionar.setEnabled(true);
            txtNomeCli.setEnabled(true);
            tblCliente.setVisible(true);
            txtIdCli.setEnabled(true);
            //System.out.println(e);
        } catch (Exception x) {
            JOptionPane.showMessageDialog(null, x);
        }
    }

    private void alterar() {
        String sql = "update tbatendimentos set idcliente=?,descatendimento=?,dtatendimento=?,hrinicial=?,hrfinal=?,statusatendimento=?,vlratendimento=? where idatendimento=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdCli.getText());
            pst.setString(2, txtDesAtendimento.getText());
            pst.setString(3, txtDtAtendimento.getText());
            pst.setString(4, txtHrIni.getText());
            pst.setString(5, txtHrFin.getText());
            pst.setString(6, tipo);
            pst.setString(7, txtValor.getText().replace(",", "."));
            pst.setString(8, txtIdAtendimento.getText());

            if ((txtIdCli.getText().isEmpty() || txtDesAtendimento.getText().isEmpty()) || txtDtAtendimento.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios.");
            } else {
                int retorno = pst.executeUpdate();
                if (retorno > 0) {
                    JOptionPane.showMessageDialog(null, "Atendimento atualizado com sucesso.");
                    txtNomeCli.setText(null);
                    txtIdAtendimento.setText(null);
                    txtIdCli.setText(null);
                    txtDesAtendimento.setText(null);
                    txtDtAtendimento.setText(null);
                    txtHrIni.setText(null);
                    txtHrFin.setText(null);
                    txtValor.setText("0");
                    txtNumHoras.setText(null);
                    btnExcluir.setEnabled(false);
                    btnAlterar.setEnabled(false);
                    btnImprimir.setEnabled(false);
                    btnAdicionar.setEnabled(true);
                    txtNomeCli.setEnabled(true);
                    tblCliente.setVisible(true);
                    txtIdCli.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o atendimento!", "Atençao!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbatendimentos where idatendimento=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdAtendimento.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Atendimento excluido com sucesso!");
                    txtNomeCli.setText(null);
                    txtIdAtendimento.setText(null);
                    txtIdCli.setText(null);
                    txtDesAtendimento.setText(null);
                    txtDtAtendimento.setText(null);
                    txtHrIni.setText(null);
                    txtHrFin.setText(null);
                    txtValor.setText("0");
                    txtNumHoras.setText(null);
                    btnExcluir.setEnabled(false);
                    btnAlterar.setEnabled(false);
                    btnImprimir.setEnabled(false);
                    btnAdicionar.setEnabled(true);
                    txtNomeCli.setEnabled(true);
                    tblCliente.setVisible(true);
                    txtIdCli.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }
    
    public void recebimento_atendimento() {
        int confirma = JOptionPane.showConfirmDialog(null, "Gerar fatura para esse atendimento?", "Atençao!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "insert into tbrecebiveisatend (idatendimento,statusdoc) values (?,?)";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdAtendimento.getText());
                pst.setString(2, "Aberta");
                pst.executeUpdate();
                btnNota.setEnabled(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdAtendimento = new javax.swing.JTextField();
        rdbAberta = new javax.swing.JRadioButton();
        rdbFechada = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        txtNomeCli = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtIdCli = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCliente = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtHrIni = new javax.swing.JFormattedTextField();
        txtHrFin = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDesAtendimento = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnAdicionar = new javax.swing.JButton();
        btnPesquisar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtDtAtendimento = new javax.swing.JFormattedTextField();
        txtNumHoras = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        btnNota = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro atendimentos");
        setPreferredSize(new java.awt.Dimension(840, 695));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel1.setText("Nº Atendimento");

        txtIdAtendimento.setEditable(false);

        buttonGroup1.add(rdbAberta);
        rdbAberta.setText("Aberta");
        rdbAberta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbAbertaActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdbFechada);
        rdbFechada.setText("Fechada");
        rdbFechada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbFechadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtIdAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdbAberta)
                        .addGap(18, 18, 18)
                        .addComponent(rdbFechada)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbAberta)
                    .addComponent(rdbFechada))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N

        txtNomeCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeCliKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeCliKeyReleased(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/search.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setText("ID*");

        txtIdCli.setEditable(false);

        tblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCliente);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtNomeCli)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel4.setText("Hora Inicial*");

        try {
            txtHrIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtHrFin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtHrFin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHrFinFocusLost(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel5.setText("Hora Final*");

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel6.setText("Descrição*");

        jLabel7.setText("Os campos com * sao obrigatórios.");

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/add.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/read.png"))); // NOI18N
        btnPesquisar.setToolTipText("Pesquisar");
        btnPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/update.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.setEnabled(false);
        btnAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir");
        btnExcluir.setEnabled(false);
        btnExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/print.png"))); // NOI18N
        btnImprimir.setToolTipText("Imprimir atendimento.");
        btnImprimir.setEnabled(false);
        btnImprimir.setPreferredSize(new java.awt.Dimension(80, 80));

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel8.setText("Data*");

        try {
            txtDtAtendimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txtNumHoras.setEditable(false);
        try {
            txtNumHoras.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel9.setText("Nº Horas");

        jLabel10.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel10.setText("Valor");

        txtValor.setText("0");

        btnNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/recebimento.png"))); // NOI18N
        btnNota.setToolTipText("Gerar recebimento.");
        btnNota.setEnabled(false);
        btnNota.setPreferredSize(new java.awt.Dimension(80, 80));
        btnNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(btnNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtDtAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtNumHoras, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtHrFin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                            .addComponent(txtHrIni, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(txtDesAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(26, 26, 26)
                                            .addComponent(jLabel10)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDtAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHrIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtHrFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDesAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(216, Short.MAX_VALUE))
        );

        setBounds(0, 0, 840, 695);
    }// </editor-fold>//GEN-END:initComponents

    private void rdbAbertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbAbertaActionPerformed
        // Tipo aberta
        tipo = "Aberta";
    }//GEN-LAST:event_rdbAbertaActionPerformed

    private void rdbFechadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbFechadaActionPerformed
        // Tipo fechada
        tipo = "Fechada";
    }//GEN-LAST:event_rdbFechadaActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // Setar Aberta como default
        rdbAberta.setSelected(true);
        tipo = "Aberta";
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtNomeCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeCliKeyPressed
       
    }//GEN-LAST:event_txtNomeCliKeyPressed

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
        // Selecionar campos
        setar_campos();
    }//GEN-LAST:event_tblClienteMouseClicked

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // Novo atendimento
        atendimento();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // Pesquisar atendimento
        pesquisar_atendimento();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // Aterar
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // Excluir
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotaActionPerformed
        // nota atendimento
        recebimento_atendimento();
    }//GEN-LAST:event_btnNotaActionPerformed

    private void txtNomeCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeCliKeyReleased
       // Pesquisar cliente
        pesquisar_cliente();
    }//GEN-LAST:event_txtNomeCliKeyReleased

    private void txtHrFinFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHrFinFocusLost
        // TODO add your handling code here:
        try {
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                    java.util.Date hrf = df.parse(txtHrFin.getText());
                    java.util.Date hri = df.parse(txtHrIni.getText());

                    long hrt = ((hrf.getTime() - hri.getTime()) / 60000) / 60;
                    txtNumHoras.setText("0" + Long.toString(hrt) + "00");
                    //System.out.println(hri);
                    //System.out.println(hrf);
                    //System.out.println(hrt);

                } catch (Exception e) {

                }
    }//GEN-LAST:event_txtHrFinFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNota;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdbAberta;
    private javax.swing.JRadioButton rdbFechada;
    private javax.swing.JTable tblCliente;
    private javax.swing.JTextField txtDesAtendimento;
    private javax.swing.JFormattedTextField txtDtAtendimento;
    private javax.swing.JFormattedTextField txtHrFin;
    private javax.swing.JFormattedTextField txtHrIni;
    private javax.swing.JTextField txtIdAtendimento;
    private javax.swing.JTextField txtIdCli;
    private javax.swing.JTextField txtNomeCli;
    private javax.swing.JFormattedTextField txtNumHoras;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
